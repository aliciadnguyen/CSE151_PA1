import java.io.IOException;
import java.util.Random;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class mainApplication {
	public String[] csvFiles = {"abalone.csv", "Seperable.csv", "3percent-miscategorization.csv", "10percent-miscatergorization"};
	public static void main(String[] args) throws IOException {
		double percent = 0.10;
		double[] totalSample;
		Random rnd = new Random();
		
		// Instantiate a new object of sample class
		sample test = new sample();
		
		// Seed RNG		
		rnd.setSeed(0);
		
		// Get dataSize from CSV file
		double dataSize = test.getDataSize("abalone.csv");
		totalSample = new double[(int)dataSize];
		test.findSample(rnd, totalSample, dataSize, percent);
		
		// Apply K Nearest Neighbor on test sample 10%
		confusionMatrix c = new confusionMatrix();
		double[][] vectors = c.storeData("ex.csv");
	}
}	
	