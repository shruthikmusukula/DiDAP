package Function;

import java.io.*;
import java.util.*;

public abstract class ImageArrEdit {

//	reads in file input
	public static double[][] fileRead(String file) throws IOException {
		
/*		Scanner in = new Scanner(new File("Images/" + file + ".csv"));
		ArrayList<ArrayList<Double> > testList = new ArrayList<ArrayList<Double> >();
		
		while(in.hasNext()) {
			String temp = in.nextLine();
			ArrayList<Double> a1 = new ArrayList<Double>();
			while(temp.indexOf(",") != -1) {
				a1.add(Double.parseDouble(temp.substring(0, temp.indexOf(","))));
				temp = temp.substring(temp.indexOf(",") + 1);
			}
			testList.add(a1);
		}
		double[][] send = new double[testList.size()][testList.get(0).size()];
		for (int i = 0; i < testList.size(); i++) {
			ArrayList<Double> l = testList.get(i);
			for (int j = 0; j < l.size(); j++) {
				send[i][j] = l.get(j);
			}
		}
*/
		double[][] send = Image.Convert.convertImageCompress(file, 1);
		System.out.println(send.length + "x" + send[0].length);
		return send;
	}

	//	black = 1	white = 0	grey = 0-1
	public static double[][] colorReduction(double[][] vals, boolean dark) {
		
		double[][] send = new double[vals.length][vals[0].length];
		
		for (int r = 0; r < vals.length; r++)
			for (int c = 0; c < vals[0].length; c++)
				send[r][c] = dark ? (255 - vals[r][c])/255 : (vals[r][c])/255;
			
		return send;
	}	
}
