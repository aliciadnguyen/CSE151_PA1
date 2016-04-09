import java.io.*;
import java.util.*;
import org.apache.commons.math3.stat.StatUtils;

public class sample {
	private static double percent = 0.10;
	private static double[] totalSample;
	private static int [] trials = {10, 100, 1000, 10000, 100000};
	private static double [] means;
	private static double [] stds;
	private static Random rnd = new Random(123);
	
	public static void main(String[] args) throws IOException {	
		means = new double[trials.length];
		stds = new double[trials.length];
		
		// Get dataSize from CSV file
		double dataSize = getDataSize("abalone.csv");
		totalSample = new double[(int)dataSize];
		
		// Run the algorithm 10, 100, 1000, etc times
		for(int i = 0; i < trials.length; i++) {
			for(int j = 0; j < trials[i]; j++) {
				findSample(totalSample, dataSize);
			}
			
			// Calculate Mean and SD
			double sampleMean = StatUtils.mean(totalSample);
			double sampleSD = getSD(sampleMean, totalSample);
			
			// Normalize Mean and SD by dividing by number of runs
			double normalMean = sampleMean / trials[i];
			double normalSD = sampleSD / trials[i];
			
			means[i] = normalMean;
			stds[i] = normalSD;
			
			//System.out.println("Sample mean is " + sampleMean);
			//System.out.println("Sample SD is " + sampleSD);
			System.out.println("Normalize mean is " + normalMean);
			System.out.println("Normalize SD is " + normalSD);
		}
		
	}
	
	
	public static int getDataSize(String csv) throws IOException {
		// Read in CSV file
		InputStream in = sample.class.getClassLoader().getResourceAsStream(csv);
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		
		// Get dataSize from CSV file
		int dataSize = 0;
		String row = reader.readLine();
		while(row != null) {
			dataSize++;
			row = reader.readLine();
		}
		
		return dataSize;
	}
	
	
	public static void findSample(double [] sample, double size) {
		// Set the sample size to 10%
		double sampleSize = size * percent; 
		
		// Number of selected items
		double count = 0;
		double thres;
		
		for(int i = 0; i < size; i++) {
			thres = (sampleSize - count)/(size - i);
			if(rnd.nextDouble() < thres){
				count++;
				sample[i]++;
			}
		}
	}
	
	
	public static double getSD(double mean, double[] arr) {
		double sumTotal = 0;
		double sd = 0;
		
		for(int i = 0; i < arr.length; i++) {
			sumTotal += Math.pow(arr[i] - mean, 2);
		}
		
		sd = Math.sqrt(sumTotal/arr.length);
		return sd;
	}
}