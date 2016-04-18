import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

class confusionMatrix {
	
	/**
	 * Function:	abaloneData()
	 * Description:	Reads in the abalone.csv file and converts the gender files
	 * 				making proxy variables (M, F, I). Stores all the data from
	 * 				csv file into a 2D array. Then swaps the 2D array's rows and
	 * 				columns in order to perform z-scaling on each features (by
	 * 				finding the mean and std of each column and normalizing the
	 * 				values). Then it swaps back the 2D array again to be in its
	 * 				original format with updated z-scaled numeric values.
	 * Parameters:	csv 	-- file to gather data from
	 * 				count 	-- number of observations in datafile
	 * Returns:		2D array of updated z-scaled values
	 */
	public double[][] abaloneData(String csv, int count) throws IOException {
		double[][] abaObser = null;
		double[] obArray;
		
		// Read in CSV file
		InputStream in = sample.class.getClassLoader().getResourceAsStream(csv);
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				
		String csvRow = null;
		int j = 0;
		
		abaObser = new double[count][11];
		
		// Put all the data of csv file into a 2D array
		while((csvRow = reader.readLine()) != null) {
			int i = 3;
			obArray = new double[11];
			String[] observation = csvRow.split(",");
			for(String feature : observation) {
				if(feature.equals("M"))
					obArray[0] = 1;
				else if(feature.equals("F"))
					obArray[1] = 1;
				else if(feature.equals("I"))
					obArray[2] = 1;
				else
					obArray[i++] = Double.parseDouble(feature);
			}
			abaObser[j] = obArray;
			j++;
		}
		
		// Swapping columns and rows
		double swap[][] = swapArr(abaObser);
		double scaleObs[][] = new double [abaObser[0].length][abaObser.length];
		
		// Perform z-scaling on swap array on index 3 to preserve M, F, I cols
		int sR = 3;
		for(; sR < swap.length-1; sR++){
			scaleObs[sR] = zScale(swap[sR]);
		}
		
		scaleObs[0] = swap[0];
		scaleObs[1] = swap[1];
		scaleObs[2] = swap[2];
		scaleObs[sR] = swap[sR];
		
		double[][] scaleArr = swapArr(scaleObs);
		
		reader.close();	
		return scaleArr;	
	}
	
	
	/**
	 * Function:	storeData()
	 * Description:	Reads in the error rates files. Stores all the data from
	 * 				csv file into a 2D array. Then swaps the 2D array's rows and
	 * 				columns in order to perform z-scaling on each features (by
	 * 				finding the mean and std of each column and normalizing the
	 * 				values). Then it swaps back the 2D array again to be in its
	 * 				original format with updated z-scaled numeric values.
	 * Parameters:	csv 	-- file to gather data from
	 * Returns:		2D array of updated z-scaled values
	 */
	public double[][] storeData(String csv) throws IOException {
		double[][] obserVectors = null;
		double[] obArray;
		
		// Sample - 10% Test data and 90% Training Data
		double [][] testData = null;
		double [][] trainData = null;
		
		sample s = new sample(testData, trainData);
		int count = s.getDataSize(csv);
		
		// Read in CSV file
		InputStream in = sample.class.getClassLoader().getResourceAsStream(csv);
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				
		String csvRow = null;
		int j = 0;
		
		obserVectors = new double[count][10];
		
		// Put all the data of csv file into a 2D array
		while((csvRow = reader.readLine()) != null) {
			int i = 0;
			obArray = new double[10];
			String[] observation = csvRow.split(",");
			for(String feature : observation) {
				obArray[i++] = Double.parseDouble(feature);
			}
			obserVectors[j] = obArray;
			j++;
		}
		
		// Swapping columns and rows
		double swap[][] = swapArr(obserVectors);
		double scaleObs[][] = new double [obserVectors[0].length][obserVectors.length];
		
		// Perform z-scaling on swap array
		int sR = 0;
		for(; sR < swap.length-1; sR++){
			scaleObs[sR] = zScale(swap[sR]);
		}
		scaleObs[sR] = swap[sR];	
		double[][] scaleArr = swapArr(scaleObs);
		
		reader.close();
		return scaleArr;
	}
	
	
	/**
	 * Function:	cmTable()
	 * Description:	Calculates the Confusion Matrix table for each dataset file
	 * Parameters:			p --	prediction array made from calculating knn
	 * 						a --	accuracy array found from test dataset 
	 * 				numLabels -- 	number of labels in dataset 
	 * Returns:		2D array of confusion matrix
	 */
	public double[][] cmTable(double[] p, double[] a, int numLabels) {
		double[][] cm = new double [numLabels][numLabels];
		
		for(int i = 0; i < p.length; i++) {
			cm[(int)p[i]][(int)a[i]]++;
		}
		
		return cm;
	}
	
	
	/**
	 * Function:	errorRate()
	 * Description:	Calculates error rate from the Confusion Matrix.
	 * 				accuracy = # classified correctly / total # classified
	 * Parameters:	table	--	table used to find error rates
	 * Returns:		the error rate of that table
	 */
	public double errorRate(double[][] table, double total) {
		double er = 0.0;
		double correct = 0.0;
		
		for(int i = 0; i < table.length; i++) {
			for(int j = 0; j < table[i].length; j++) {
				if(i == j) {
					correct += table[i][j];
				}
			}
		}
		
		er = correct/total;
		
		return er;
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
		System.out.println();
		System.out.println();
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
	 * Parameters:	featureCol	-- the column of each specified feature
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
				
		for(int i = 0; i < featureCol.length; i++) {
			scale[i] = (featureCol[i] - mean) / sd;
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
	 * Returns:		A predicted classifier number
	 */
	public double kNearestNeighbor(double[] testPt, double[][] trainPt, int k) {
		List<Result> resultList = new ArrayList<Result>();
		
		// Calculate the Euclidean distance of two points
		for(int row = 0; row < trainPt.length; row++) {
			double dist = 0.0;
			int classification = 0;
			for(int col = 0; col < trainPt[row].length - 1; col++) {
				dist += Math.pow(trainPt[row][col] - testPt[col], 2);
				classification++;
			}
			double distance = Math.sqrt(dist);
			resultList.add(new Result(distance, trainPt[row][classification]));
		}
		
		// Sort the array based on distances in ascending order
		Collections.sort(resultList, new DistanceComparator());
		
		double[] knn = new double[k];
		for(int x = 0; x < k; x++){
			knn[x] = resultList.get(x).classifier;
		}
		
		// Find the most common element in array
		double majElem = majorityElement(knn);

		// Return the smallest distance element
		return majElem;
	}
	
	
	/**
	 * Function:	majorityElement()
	 * Description:	Find the highest number in any given array
	 * Parameters:	a	-- the array to look for the highest number in
	 * Returns:		The highest number
	 */
	public double majorityElement(double[] a) {
		int count = 1, tempCount;
		double popular = a[0];
		double temp = 0;
		for (int i = 0; i < (a.length - 1); i++) {
		    temp = a[i];
		    tempCount = 0;
		    for (int j = 1; j < a.length; j++)
		    {
		      if (temp == a[j])
		        tempCount++;
		    }
		    if (tempCount > count)
		    {
		      popular = temp;
		      count = tempCount;
		    }
		  }
		  return popular;
	}
	
	
	/**
	 * Class:		Result
	 * Description:	This object holds the values of distance and its classifier
	 * 				label. Result is used as an object when finding the
	 * 				k nearest neighbor.
	 * Attributes:	distance 	-- holds the euclidean distance from two points
	 * 				classifier	-- the number it is labeled as
	 */
	static class Result {
		double distance;
		double classifier;
		public Result(double distance, double c) {
			this.distance = distance;
			this.classifier = c;
		}
	}
	
	
	/**
	 * Class:		DistanceComparator
	 * Description:	An object used to order an list of Results object based on
	 * 				its distances. It will sort the list based on its distance
	 * 				on ascending order. This function is used to find k nearest
	 * 				neighbors.
	 * Attributes:	compare		--  function to override in order to sort distances
	 * 								in ascending order
	 */
	static class DistanceComparator implements Comparator<Result> {
		@Override
		public int compare(Result a, Result b) {
			return a.distance < b.distance ? -1 : a.distance == b.distance ? 0 : 1;
		}
	}
}