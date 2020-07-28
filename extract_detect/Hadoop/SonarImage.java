
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
//import java.io.BufferedOutputStream;
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
//import org.apache.hadoop.mapred.TextOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
//import org.w3c.dom.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.*;
//import org.apache.hadoop.mapred.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;

/**
 * Hadoop with Multiple Outputs, both image and metadata version
 * 
 * Linux commands: $ javac -cp /usr/lib/hadoop/*:/usr/lib/hadoop/client-0.20/*
 * -d Host_Exchange/HadoopSonar/SonarImage
 * Host_Exchange/HadoopSonar/SonarImage/SonarImage.java
 * Host_Exchange/HadoopSonar/SonarImage/Extract.java
 * Host_Exchange/HadoopSonar/SonarImage/Util.java $ jar -cvf SonarImage.jar -C
 * Host_Exchange/HadoopSonar/SonarImage . $ hadoop jar SonarImage.jar SonarImage
 * /user/cloudera/Host_Exchange/remusfiles/florida
 * /user/cloudera/Host_Exchange/HadoopSonar/SonarImage/output ^ change name of
 * output folder in third line accordingly
 */

public class SonarImage {
	// later change this to work with any input/output directory that is inputed
	// through the Linux command line
	// also have the file be placed in the output folder
	public static String basePath = "/home/cloudera/Documents/shared/MineProcess/output/";

	// file count is attached to name of png to see if multiple pngs are being
	// printed
	public static int count = 0;

	// these paths don't seem to work. It seems like after the main function, the
	// variables here are reset
	public static Path inPath;
	public static Path outPath;
	// conf seems to be fine but that's mainly because we don't need its value from
	// the main function
	public static Configuration conf;

	public static byte[] bytes;
	public static byte[][] all_bytes = null;
	public static byte[][] image = null;
	public static int bytes_length;
	public static String currentkey;
	private static Job job;

	/**
	 * Mapper with: input key = file name (Text.class) input value = MSTIFF binary
	 * data (BytesWritable.class) output key = file name (Text.class) output value =
	 * image in byte form (BytesWritable.class)
	 */
	public static class Sonar_Mapper extends Mapper<Text, BytesWritable, Text, Text> {

		private MultipleOutputs mos;

		public void setup(Context context) throws IOException, InterruptedException {
			mos = new MultipleOutputs(context);

		}

		@Override
		public void map(Text key, BytesWritable value, Context context) throws IOException, InterruptedException {

			// process the files
			currentkey = key.toString();
			System.out.println(currentkey);
			System.out.println("new file, new day!");
			bytes = value.getBytes();
			bytes_length = (int) value.getLength();
			Extract.fileProcess(key.toString());

			System.out.println("not sure if this is working");
			System.out.println("First bytes" + bytes[1]);

			// output the metadata
			StringBuilder sb = new StringBuilder();

			String[][] arr = Extract.metaTemp;
			for (int j = 0; j < arr.length; j++) {
				for (int k = 0; k < arr[0].length; k++) {
					sb.append(arr[j][k] + ",");
				}
				sb.append('\n');
			}
			mos.write("metadata", key, new Text(sb.toString()));

			// process the image bytes
			if (all_bytes == null)
				all_bytes = Extract.imTemp;
			else {
				byte[][] temp = Util.combineVertically(all_bytes, Extract.imTemp);
//				Util.saveIm(temp, "im" + Extract.count, false);
				image = MacroMaster.findMine(temp, "im" + Extract.count);
				all_bytes = Extract.imTemp;

				System.out.println("Finished with all");

				System.out.println("img label " + Util.imgLabel);

				Configuration conf = new Configuration();
				count++; // because count is updated before imgPath, count starts at 1
				String imgPath = Util.imgLabel + count + ".png";

				String localPath = basePath + Util.imgLabel + count + ".png";

				// https://stackoverflow.com/questions/17488534/create-a-file-from-a-bytearrayoutputstream
				// http://codeinventions.blogspot.com/2014/08/creating-file-from-bytearrayoutputstrea.html
				// https://stackoverflow.com/questions/16546040/store-images-videos-into-hadoop-hdfs
				// https://stackoverflow.com/questions/15414259/java-bufferedimage-to-byte-array-and-back
				ByteArrayOutputStream baos = null;
				FSDataInputStream in = null;
				FileSystem fs = FileSystem.get(conf);
				SequenceFile.Writer writer = null;
				try {
					// convert Util.img into a byte array
					baos = new ByteArrayOutputStream();
					ImageIO.write(Util.img, "png", baos);
					byte[] imBytes = baos.toByteArray();
					baos.write(imBytes);

					// writes image to vm folder
					System.out.println(localPath);
					File outfile = new File(localPath);
					ImageIO.write(Util.img, "png", outfile);

					// writes image as byte array as SequenceFile to hdfs
					mos.write("image", key, new BytesWritable(imBytes));

				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					IOUtils.closeStream(writer);
				}

				// wipes imTemp
				Extract.saveMeta();
				// context.write(new Text(key), new Text("das good"));
			}
		}

		public void cleanup(Context context) throws IOException, InterruptedException {
			mos.close();
		}

	}

	/**
	 * Reducer with: input key = file name (Text.class) input value = whatever the
	 * output value of the Mapper is (class is same as Map output class) output key
	 * = file name (Text.class) output value = Sonar image data in byte form
	 * (BytesWritable.class)
	 */
	public static class Sonar_Reducer extends Reducer<Text, Text, Text, BytesWritable> {

		public void reduce(Text key, Text value, Context context) throws IOException, InterruptedException {

		}
	}

	/**
	 * Hadoop by default splits files into smaller chunks which are fed to the
	 * mapper I think that this makes sure that the files aren't split when fed into
	 * the mapper Not sure though, this was written by @author Pyojeong Kim
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
		// disables reducer essentially
		job.setNumReduceTasks(0);

		job.setOutputKeyClass(Text.class);
		// set up 2 Hadoop jobs, one for metadata and one for sonar image
		MultipleOutputs.addNamedOutput(job, "metadata", TextOutputFormat.class, Text.class, Text.class);
		MultipleOutputs.addNamedOutput(job, "image", SequenceFileOutputFormat.class, Text.class, BytesWritable.class);
		job.setOutputKeyClass(Text.class);

		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		System.exit(job.waitForCompletion(true) ? 0 : 1);

	}
}