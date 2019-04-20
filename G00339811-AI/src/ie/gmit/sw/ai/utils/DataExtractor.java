package ie.gmit.sw.ai.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataExtractor {
	// Set final values for size of data files being read.
	public final static int FILE_SIZE = 21;
	public final static int INPUT_NEURONS = 3;

	
	public static double[][] extractDataFromFile(String fileName) {
		
		FileReader fileReader;
		fileReader = null;
		String[] dataTuple;
		double[][] data;
		data = new double[FILE_SIZE][INPUT_NEURONS];
		try {
			fileReader = new FileReader(fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		List<String> lines = new ArrayList<String>();
		String line = null;
		try {
			while ((line = bufferedReader.readLine()) != null) {
				lines.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String[] myLines = lines.toArray(new String[lines.size()]);

		int i = 0;
		for (String s : myLines) {
			dataTuple = myLines[i].split(",");
			for (int j = 0; j < dataTuple.length; j++) {
				data[i][j] = Double.parseDouble(dataTuple[j]);
			}
			i++;

		}
		System.out.println("[Neural Net] Data loaded from file");
System.out.println(data);
		return data;
	}

}
