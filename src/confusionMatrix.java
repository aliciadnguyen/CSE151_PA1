import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

class confusionMatrix {
	
	/**
	 * Make the data into vectors by putting them in a 2D array 
	 */
	public double[][] storeData(String csv) throws IOException {
		double[][] obserVectors = null;
		double[] obArray;
		
		sample s = new sample();
		int count = s.getDataSize(csv);
		
		// Read in CSV file
		InputStream in = sample.class.getClassLoader().getResourceAsStream(csv);
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				
		String csvRow = null;
		int j = 0;
		
		obserVectors = new double[count][5];
		
		// Put all the data of csv file into a 2D array
		while((csvRow = reader.readLine()) != null) {
			int i = 0;
			obArray = new double[5];
			String[] observation = csvRow.split(",");
			for(String feature : observation) {
				obArray[i++] = Double.parseDouble(feature);
			}
			obserVectors[j] = obArray;
			j++;
		}
		
		// Print Array
		System.out.println("Normal Array: --------------- ");
		printArr(obserVectors);
		
		// Swapping columns and rows
		double swap[][] = swapArr(obserVectors);
		double scaleObs[][] = new double [obserVectors[0].length][obserVectors.length];
		
		System.out.println("Swap Array: --------------- ");
		printArr(swap);
		
		// Perform z-scaling on swap array
		for(int sR = 0; sR < swap.length; sR++){
			scaleObs[sR] = zScale(swap[sR]);
		}
		
		System.out.println("Z-scale Array: --------------- ");
		printArr(scaleObs);
		
		double[][] scaleArr = swapArr(scaleObs);
		System.out.println("Swap Z-scale Array: --------------- ");
		printArr(scaleArr);
		
		reader.close();
		return null;
	}
	
	/**
	 * Function:	printArr
	 * Description:	Print the array in better format
	 * Parameters:	2D Array
	 * Returns:		Void
	 */
	public void printArr(double [][] arr){
		for(int r = 0; r < arr.length; r++) {
			for(int c = 0; c < arr[r].length; c++) {
				System.out.print(arr[r][c] + " ");
			}
			System.out.println();
		}
	}
	
	/**
	 * Function:	swapArr
	 * Description:	Swaps the rows and columns of the array
	 * Parameters:	2D Array
	 * Returns:		2D Array with rows and columns switched
	 */
	public double[][] swapArr(double[][] arr) {
		// Swapping columns and rows
		double swap[][] = new double[arr[0].length][arr.length];
		for(int row = 0; row < arr.length; row++) {
			for(int col = 0; col < arr[row].length; col++) {
				swap[col][row] = arr[row][col];					
			}
		}
		return swap;
	}

	/**
	 * Function:	zScale()
	 * Parameters:
	 * Description:	For each column, find the mean and std
	 * 				Then perform z-scale: (vector - mean)/std
	 * Returns: 	A new column with z-scale numbers (an array)
	 */
	public double[] zScale(double[] featureCol) {
		double [] scale = new double [featureCol.length];
		
		DescriptiveStatistics stats = new DescriptiveStatistics();
		
		for(int i = 0; i < featureCol.length; i++) {
			stats.addValue(featureCol[i]);
		}
		
		double mean = stats.getMean();
		double sd = stats.getStandardDeviation();
		
		//System.out.println("Mean is " + mean + " " + "SD is " + sd);
		
		for(int i = 0; i < featureCol.length; i++) {
			scale[i] = (featureCol[i] - mean) / sd;
			//System.out.println(scale[i]);
		}
		
		return scale;
	}
	
	/**
	 * Function:	kNearestNeighbor()
	 * Description:	Find K nearest neighbor of test sample by calculating
	 * 				Euclidean distance formula
	 * 				dist((x, y), (a, b)) = sqrt((x-a)^2) + (y-b)^2);
	 * Parameters:	double[] testPt	 --	as a point on the plane
	 * 				double[] trainPt --	as a point on the plane
	 */
	public double[] kNearestNeighbor(double[] testPt, double[][] trainPt) {
		double [] knn;
		
		return knn;
	}
}