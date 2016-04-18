import java.io.IOException;
import java.util.Random;

public class mainApplication {
	public static String originalCSV = "abalone.csv";
	public static String[] csvFiles = {"Seperable.csv", "3percent-miscategorization.csv", "10percent-miscatergorization.csv"};
	public static int[] kNum = {1, 3, 5, 7, 9};
	
	public static void main(String[] args) throws IOException {
		// Used to find random 10% of data file
		double percent = 0.10;
		Random rnd = new Random();
		
		// Hard-coded numbers to represent number of classifications
		int binaryClass = 2;
		
		// Hard-coded numbers to represent number of columns in abalone and other csv files
		int abaCols = 11;
		int errorCols = 10;
		
		// Arrays to hold predictions and accuracy of each dataset files
		double[] abaPrediction;
		double[] abaAccuracy;
		double[] prediction;
		double[] accuracy;
		
		// Sample - 10% Test data and 90% Training Data
		double [][] abaTestData = null;
		double [][] abaTrainData = null;
		double [][] testData = null;
		double [][] trainData = null;

		// Instantiate a new object of sample class
		sample abaS = new sample(abaTestData, abaTrainData);
		sample s = new sample(testData, trainData);
		
		int abaCount = abaS.getDataSize(originalCSV);
		
		// Seed RNG		
		rnd.setSeed(0);
		
		// Apply K Nearest Neighbor on test sample 10%
		confusionMatrix c = new confusionMatrix();

		/* ------------ ABALONE FILES ----------------*/
		System.out.println("Calculations for Abalone.csv");
		double[][] a = null;
		a = c.abaloneData(originalCSV, abaCount);
		abaS.findSample(rnd, a, percent);

		// Find K Nearest Neighbor with Test and Training Data
		abaPrediction = new double [abaS.test.length];
		abaAccuracy = new double [abaS.test.length];
		
		for(int k = 0; k < kNum.length; k++) {
			System.out.println("kNum is " + kNum[k]);
			for(int testPt = 0; testPt < abaS.test.length; testPt++) {
				abaPrediction[testPt] = c.kNearestNeighbor(abaS.test[testPt], abaS.train, kNum[k]);
			}
			
			for(int i = 0; i < abaS.test.length; i++) {
				abaAccuracy[i] = abaS.test[i][abaCols-1]; 
			}
			
			// Output the accurate and prediction table
			System.out.println("Prediction Array");
			for(int predBin = 0; predBin < abaPrediction.length; predBin++) {
				System.out.print(abaPrediction[predBin] + " ");
			}
			
			System.out.println();
			
			System.out.println("Accuracy Array");
			for(int accBin = 0; accBin < abaAccuracy.length; accBin++) {
				System.out.print(abaAccuracy[accBin] + " ");
			}
			
			System.out.println();
			
			// Calculate the Confusion Matrix
			double largest = Integer.MIN_VALUE;
			for(int num = 0; num < abaPrediction.length; num++) {
				if(abaPrediction[num] > largest)
					largest = abaPrediction[num];
				if(abaAccuracy[num] > largest)
					largest = abaAccuracy[num];
			}
						
			double [][] abaConfuse = c.cmTable(abaPrediction, abaAccuracy, (int)largest + 1);
			System.out.println("Confusion Matrix");
			c.printArr(abaConfuse);
		}
		
		

		/* ------------ ERROR RATE FILES ----------------*/
		for(int file = 0; file < csvFiles.length; file++) {
			System.out.println("Calculations for " + csvFiles[file]);
			double[][] vectors = c.storeData(csvFiles[file]);
			s.findSample(rnd, vectors, percent);

			// Find K Nearest Neighbor with Test and Training Data
			prediction = new double [s.test.length];
			accuracy = new double [s.test.length];
			
			for(int k = 0; k < kNum.length; k++) {
				System.out.println("kNum is " + kNum[k]);
			
				for(int testPt = 0; testPt < s.test.length; testPt++) {
					prediction[testPt] = c.kNearestNeighbor(s.test[testPt], s.train, kNum[k]);
				}
				
				for(int i = 0; i < s.test.length; i++) {
					accuracy[i] = s.test[i][errorCols-1]; 
				}
				
				// Output the accurate and prediction table
				System.out.println("Prediction Array");
				for(int predBin = 0; predBin < prediction.length; predBin++) {
					System.out.print(prediction[predBin] + " ");
				}
				
				System.out.println();
				
				System.out.println("Accuracy Array");
				for(int accBin = 0; accBin < accuracy.length; accBin++) {
					System.out.print(accuracy[accBin] + " ");
				}
				
				System.out.println();
				
				// Calculate the Confusion Matrix
				double [][] confuse = c.cmTable(prediction, accuracy, binaryClass);
				System.out.println("Confusion Matrix");
				c.printArr(confuse);
			}
		}
	}
}	
	