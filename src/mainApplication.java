import java.io.IOException;
import java.util.Random;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class mainApplication {
	public static void main(String[] args) throws IOException {
		int [] trials = {10, 100, 1000, 10000, 100000};
		Random rnd = new Random();
		double percent = 0.10;
		double[] totalSample;
		double [] means;
		double [] stds;
		
		// Instantiate a new object of sample class
		sample test = new sample();
		
		// Allocating arrays to hold means and stds of each trial run
		means = new double[trials.length];
		stds = new double[trials.length];
			
		// Seed RNG		
		rnd.setSeed(0);
			
		// Get dataSize from CSV file
		double dataSize = test.getDataSize("abalone.csv");
		totalSample = new double[(int)dataSize];
			
		// Run the algorithm 10, 100, 1000, etc times
		for(int i = 0; i < trials.length; i++) {
			// Object used to calculated mean and std
			DescriptiveStatistics stats = new DescriptiveStatistics();
			
			for(int j = 0; j < trials[i]; j++) {
				test.findSample(rnd, totalSample, dataSize, percent);
			}
			
			// Calculate Mean and SD
			for(int k = 0; k < totalSample.length; k++) {
				stats.addValue(totalSample[k]);
			}
			
			double sampleMean = stats.getMean();
			double sampleSD = stats.getStandardDeviation();
				
			// Normalize Mean and SD by dividing by number of runs
			double normalMean = sampleMean / trials[i];
			double normalSD = sampleSD / trials[i];
				
			means[i] = normalMean;
			stds[i] = normalSD;
				
			//System.out.println("Sample mean is " + sampleMean);
			//System.out.println("Sample SD is " + sampleSD);
			System.out.println("Normalize mean is " + normalMean);
			System.out.println("Normalize SD is " + normalSD);
			//System.out.println("-----");
			
			// Reset all the counts to zero for each run
			for(int s = 0; s < totalSample.length; s++)
				totalSample[s] = 0;
		}
			
		// Make the graph -- numbers of runs vs. means
		test.createChart(means, trials);
	}
}	
	