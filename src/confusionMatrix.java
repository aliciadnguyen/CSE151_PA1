import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.util.Pair;

class confusionMatrix {
	
	/**
	 * Make the data into vectors by putting them in a 2D array 
	 */
	public double[][] abaloneData(String csv) throws IOException {
		double[] prediction;
		double[] accuracy;
		
		double[][] abaObser = null;
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
		
		printArr(abaObser);
		return abaObser;
		
	}
	
	public double[][] storeData(String csv) throws IOException {
		double[][] obserVectors = null;
		double[] obArray;
		Random rnd = new Random();
		double percent = 0.10;
		int binaryClass = 2;
		int abaloneClass = 20;
		
		double[] prediction;
		double[] accuracy;
		
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
		
		// Print Array
		//System.out.println("Normal Array: --------------- ");
		//printArr(obserVectors);
		
		// Swapping columns and rows
		double swap[][] = swapArr(obserVectors);
		double scaleObs[][] = new double [obserVectors[0].length][obserVectors.length];
		
		//System.out.println("Swap Array: --------------- ");
		//printArr(swap);
		
		// Perform z-scaling on swap array
		int sR = 0;
		for(; sR < swap.length-1; sR++){
			scaleObs[sR] = zScale(swap[sR]);
		}
		scaleObs[sR] = swap[sR];
		
		//System.out.println("Z-scale Array: --------------- ");
		//printArr(scaleObs);
		
		double[][] scaleArr = swapArr(scaleObs);
		//System.out.println("Swap Z-scale Array: --------------- ");
		//printArr(scaleArr);
		
		s.findSample(rnd, obserVectors, percent);
		
		//System.out.println("Length: " + s.test.length);
		
		// Find K Nearest Neighbor with Test and Training Data
		prediction = new double [s.test.length];
		accuracy = new double [s.test.length];
		
		for(int i = 0; i < s.test.length; i++) {
			prediction[i] = kNearestNeighbor(s.test[i], s.train, 5);
		}
		
		for(int i = 0; i < s.test.length; i++) {
			// Index 9 holds the classification column
			accuracy[i] = s.test[i][9]; 
		}
		
		// Output the accurate and prediction table
		System.out.println("Prediction Array");
		for(int i = 0; i < prediction.length; i++) {
			System.out.print(prediction[i] + " ");
		}
		
		System.out.println();
		
		System.out.println("Accuracy Array");
		for(int i = 0; i < accuracy.length; i++) {
			System.out.print(accuracy[i] + " ");
		}
		System.out.println();
		
		// Calculate the Confusion Matrix
		double [][] confuse = cmTable(prediction, accuracy);
		printArr(confuse);
		
		reader.close();
		return null;
	}
	
	public double[][] cmTable(double[] p, double[] a) {
		double[][] cm = new double [2][2];
		
		for(int i = 0; i < p.length; i++) {
			cm[(int)p[i]][(int)a[i]]++;
		}
		
		return cm;
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
	public double kNearestNeighbor(double[] testPt, double[][] trainPt, int k) {
		List<Result> resultList = new ArrayList<Result>();
		
		for(int row = 0; row < trainPt.length; row++) {
			double dist = 0.0;
			int classification = 0;
			for(int col = 0; col < trainPt[row].length - 1; col++) {
				dist += Math.pow(trainPt[row][col] - testPt[col], 2);
				classification++;
			}
			double distance = Math.sqrt(dist);
			resultList.add(new Result(distance, trainPt[row][classification]));
			//System.out.println("Classifier: " + trainPt[row][classification] + " Distance is: " + distance);
		}
		
		Collections.sort(resultList, new DistanceComparator());
		
		double[] knn = new double[k];
		for(int x = 0; x < k; x++){
			knn[x] = resultList.get(x).classifier;
			//System.out.println(knn[x]);
		}
		
		
		double majElem = majorityElement(knn);

		//.out.println("ME: " + majElem);
		//System.out.println("-------------------");
		// Return the smallest distance element
		return majElem;
	}
	
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
	
	static class Result {
		double distance;
		double classifier;
		public Result(double distance, double c) {
			this.distance = distance;
			this.classifier = c;
		}
	}
	
	static class DistanceComparator implements Comparator<Result> {
		@Override
		public int compare(Result a, Result b) {
			return a.distance < b.distance ? -1 : a.distance == b.distance ? 0 : 1;
		}
	}
}