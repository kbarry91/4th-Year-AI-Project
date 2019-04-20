package ie.gmit.sw.ai.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * DataExtractor is a utilty class the holds a method to extract data from a
 * file for the neural network.
 * 
 * @author Kevin Barry - Bachelor of Science (Honours) in Software Development
 */
public class DataExtractor {

	// Set final values for size of data files being read.
	public final static int FILE_SIZE = 21;
	public final static int INPUT_NEURONS = 3;

	/**
	 * Extracts data from a file.
	 * 
	 * @param filePath, the path to the file where data is stored.
	 * @return a 2dim array of the data.
	 */
	public static double[][] extractDataFromFile(String filePath) {

		// Initialise a 2dim double array of the specified size.
		double[][] data = new double[FILE_SIZE][INPUT_NEURONS];

		// Set file reading variables.
		FileReader fileReader = null;
		;
		String[] dataTuple;
		List<String> lines = new ArrayList<String>();
		String currentLine = null;

		// Try create a new file reader.
		try {
			fileReader = new FileReader(filePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		// Set up buffer to read text from input stream.
		BufferedReader bufferedReader = new BufferedReader(fileReader);

		// Try add current line to List.
		try {
			while ((currentLine = bufferedReader.readLine()) != null) {
				lines.add(currentLine);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Close the buffer
		try {
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Convert list to STring array.
		String[] linesRead = lines.toArray(new String[lines.size()]);

		int i = 0;

		// Iterate through linesRead.
		for (String str : linesRead) {
			// SPlit the line.
			dataTuple = str.split(",");

			// Iterate through the line
			for (int j = 0; j < dataTuple.length; j++) {
				data[i][j] = Double.parseDouble(dataTuple[j]);
			}
			i++;

		}
		
		System.out.println("[Neural Net] Data loaded from file");

		return data;
	}

}
