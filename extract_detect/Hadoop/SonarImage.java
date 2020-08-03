
import java.io.File;

import java.net.URI;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.FileWriter;

import javax.imageio.ImageIO;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.OutputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;

/**
 * Hadoop with Multiple Outputs: mine-tagged image and metadata
 * @author Kaylin Li, Pyojeong Kim, Mitali Chowdhury, Seth Knoop, Nancy Daoud
 */

public class SonarImage {
	// later change this to work with any input/output directory that is inputed
	// through the Linux command line
	// also have the file be placed in the output folder
	public static String basePath = "/home/cloudera/Documents/shared/MineProcess/output/";

	// file count is attached to name of png
	public static int count = 0;

	// these paths don't seem to work. It seems like after the main function, the
	// variables here are reset
	public static Path inPath;
	public static Path outPath;
	
	public static Configuration conf;
	private static Job job;

	// variables for storage of image data
	public static byte[] bytes;
	public static byte[][] right_bytes = null;
	public static byte[][] left_bytes = null;
	public static byte[][] image = null;
	public static int bytes_length;
	public static String lastkey;
	public static String currentkey;

	/**
	 * Mapper with: input key = file name (Text.class) 
	 * input value = MSTIFF binary data (BytesWritable.class) 
	 * 
	 * output key = file name (Text.class) 
	 * output value = image with mine tagged in byte form (BytesWritable.class)
	 * output also includes CSV files for metadata and image information
	 */
	public static class Sonar_Mapper extends Mapper<Text, BytesWritable, Text, Text> {

		private MultipleOutputs mos;

		public void setup(Context context) throws IOException, InterruptedException {
			mos = new MultipleOutputs(context);
		}

		@Override
		public void map(Text key, BytesWritable value, Context context) throws IOException, InterruptedException {

			// extract data from file
			currentkey = key.toString();
			System.out.println(currentkey);
			System.out.println("new file, new day!");
			bytes = value.getBytes();
			bytes_length = (int) value.getLength();
			Extract.fileProcess(key.toString());

			// output the metadata
			StringBuilder sb = new StringBuilder();

			String[][] arr = Extract.metaTemp;
			for (int j = 1; j < arr.length; j++) {
				if (arr[j][0] == null)
					break;
				for (int k = 0; k < arr[0].length; k++) {
					
						sb.append(arr[j][k] + ",");
				}
				sb.append(j + "\n");
			}
			mos.write("metadata", key + "\n", new Text(sb.toString()));

			// process the image bytes
			if (right_bytes == null) {
				right_bytes = Extract.imTempR;
				left_bytes = Extract.imTempL;
			} else {
				// find mine
				byte[][] temp = Util.combineVertically(right_bytes, Extract.imTempR);
				boolean r = MacroMaster.findMine(temp, 'R');
				right_bytes = Extract.imTempR;
				
				temp = Util.combineVertically(left_bytes, Extract.imTempL);
				boolean l = MacroMaster.findMine(temp, 'L');
				left_bytes = Extract.imTempL;

				Configuration conf = new Configuration();
				count++; // because count is updated before imgPath, count starts at 1
				System.out.println("img number " + count);
				
				String imgPath = "im" + count + ".png";
				String localPath = basePath + imgPath;
				// writes image name, associated mstiff files, and whether the image contains a mine
				mos.write("mines", imgPath, new Text(lastkey + ", " + currentkey + ", " + (r || l)));
				
				// https://stackoverflow.com/questions/17488534/create-a-file-from-a-bytearrayoutputstream
				// http://codeinventions.blogspot.com/2014/08/creating-file-from-bytearrayoutputstrea.html
				// https://stackoverflow.com/questions/16546040/store-images-videos-into-hadoop-hdfs
				// https://stackoverflow.com/questions/15414259/java-bufferedimage-to-byte-array-and-back
				ByteArrayOutputStream baos = null;
				FSDataInputStream in = null;
				FileSystem fs = FileSystem.get(conf);
				SequenceFile.Writer writer = null;
				try {
					// convert image into a byte array
					baos = new ByteArrayOutputStream();
					ImageIO.write(Util.getImage(Util.combineHorizontally(MacroMaster.imL, MacroMaster.imR), false), "png", baos);
					byte[] imBytes = baos.toByteArray();
					baos.write(imBytes);

					// writes image to vm folder
					System.out.println(localPath);
					File outfile = new File(localPath);
					ImageIO.write(Util.getImage(Util.combineHorizontally(MacroMaster.imL, MacroMaster.imR), false), "png", outfile);

					// writes image as byte array as SequenceFile to hdfs
					mos.write("image", key, new BytesWritable(imBytes));

				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					IOUtils.closeStream(writer);
				}

				// wipes imTemp
				Extract.saveMeta();
			}
			lastkey = currentkey;
		}

		public void cleanup(Context context) throws IOException, InterruptedException {
			mos.close();
		}

	}

	/**
	 * Reducer with: input key = file name (Text.class) 
	 * input value = whatever the output value of the Mapper is (class is same as Map output class) 
	 * 
	 * output key = file name (Text.class) 
	 * output value = Sonar image data in byte form (BytesWritable.class)
	 */
	public static class Sonar_Reducer extends Reducer<Text, Text, Text, BytesWritable> {

		public void reduce(Text key, Text value, Context context) throws IOException, InterruptedException {

		}
	}

	/**
	 * Keeps files from being split (?)
	 */
	public static class CombineDocumentFileFormat extends CombineFileInputFormat<Text, BytesWritable> {
		@Override
		protected boolean isSplitable(JobContext context, Path file) {
			return false;
		}

		@Override
		public RecordReader<Text, BytesWritable> createRecordReader(InputSplit split, TaskAttemptContext context)
				throws IOException {
			return new CombineFileRecordReader<Text, BytesWritable>((CombineFileSplit) split, context,
					WholeFileRecordReader.class);
		}
	}

	public static class WholeFileRecordReader extends RecordReader<Text, BytesWritable> {
		private CombineFileSplit inputSplit;
		private Integer idx;
		private Text path;
		private Configuration conf;
		private BytesWritable document = new BytesWritable();
		private boolean read;

		public WholeFileRecordReader(CombineFileSplit inputSplit, TaskAttemptContext context, Integer idx) {
			this.inputSplit = inputSplit;
			this.idx = idx;
			this.conf = context.getConfiguration();
			this.read = false;
		}

		@Override
		public void initialize(InputSplit split, TaskAttemptContext context) throws IOException, InterruptedException {
		}

		/**
		 * reads next mstiff file
		 */
		@Override
		public boolean nextKeyValue() throws IOException, InterruptedException {
			if (!read) {
				Path file = inputSplit.getPath(idx);
				FileSystem fs = file.getFileSystem(conf);
				FSDataInputStream input = fs.open(file);
				byte[] bytes = new byte[(int) inputSplit.getLength(idx)];
				int offset = (int) inputSplit.getOffset(idx);
				int length = (int) inputSplit.getLength(idx);
				IOUtils.readFully(input, bytes, offset, length);
				document = new BytesWritable();
				document.set(bytes, offset, length);
				path = new Text(file.toString());
				read = true;
				return true;
			} else {
				return false;
			}
		}

		@Override
		public Text getCurrentKey() throws IOException, InterruptedException {
			return path;
		}

		@Override
		public BytesWritable getCurrentValue() throws IOException, InterruptedException {
			return document;
		}

		@Override
		public float getProgress() throws IOException {
			return read ? 1.0f : 0.0f;
		}

		@Override
		public void close() throws IOException {
			// Don't need to do anything //
		}
	}

	public static void main(String[] args) throws Exception {
		conf = new Configuration();
		job = Job.getInstance(conf, "SonarImage");
		
		// allows for Hadoop to find the jar file
		// by looking at each jar file and seeing if it has the following class in it
		job.setJarByClass(SonarImage.class);
		System.out.println("output main" + args[1]);

		job.setMapperClass(Sonar_Mapper.class);
		job.setReducerClass(Sonar_Reducer.class);

		// makes sure that each input to the mapper is an entire file
		// since Hadoop typically breaks up files by default
		job.setInputFormatClass(CombineDocumentFileFormat.class);
		
		// disables reducer 
		job.setNumReduceTasks(0);

		job.setOutputKeyClass(Text.class);
		
		// set up 3 outputs, one for metadata, one for sonar image, and one for mine information
		MultipleOutputs.addNamedOutput(job, "metadata", TextOutputFormat.class, Text.class, Text.class);
		MultipleOutputs.addNamedOutput(job, "image", SequenceFileOutputFormat.class, Text.class, BytesWritable.class);
		MultipleOutputs.addNamedOutput(job, "mines", TextOutputFormat.class, Text.class, Text.class);
		job.setOutputKeyClass(Text.class);

		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		System.exit(job.waitForCompletion(true) ? 0 : 1);

	}
}