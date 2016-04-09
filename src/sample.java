import java.io.*;
import java.util.*;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class sample {
	private static int [] trials = {10, 100, 1000, 10000, 100000};
	private static Random rnd = new Random();
	private static double percent = 0.10;
	private static double[] totalSample;
	private static double [] means;
	private static double [] stds;
	
	
	public static void main(String[] args) throws IOException {	
		
		// Allocating arrays to hold means and stds of each trial run
		means = new double[trials.length];
		stds = new double[trials.length];
		
		// Seed RNG		
		rnd.setSeed(0);
		
		// Get dataSize from CSV file
		double dataSize = getDataSize("abalone.csv");
		totalSample = new double[(int)dataSize];
		
		// Run the algorithm 10, 100, 1000, etc times
		for(int i = 0; i < trials.length; i++) {
			// Object used to calculated mean and std
			DescriptiveStatistics stats = new DescriptiveStatistics();
			
			for(int j = 0; j < trials[i]; j++) {
				findSample(dataSize);
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
			//System.out.println("Normalize mean is " + normalMean);
			//System.out.println("Normalize SD is " + normalSD);
			//System.out.println("-----");
		}
		
		// Make the graph -- numbers of runs vs. means
		createChart();
	}
	
	
	/**
	 * Function: 	createChart()
	 * Parameters: 	None, uses static int[] trials and double[] mean
	 * Description:	Creates a datasets of x and y plots. Number of trials
	 * 				is on the x-axis and normalized mean is on the y-axis
	 * Returns:		A graph made on Desktop
	 */
	public static void createChart() {
		XYSeriesCollection dataSet = new XYSeriesCollection();
		XYSeries series = new XYSeries("Plotted Points");
		
		for(int m = 0; m < means.length; m++) {
			series.add(trials[m], means[m]);
			System.out.println("Trials: " + trials[m]);
			System.out.println("Means: " + means[m]);
		}
		
		dataSet.addSeries(series);
		
		String chartTitle = "Runs vs Mean Graph";
		String xAxisLabel = "X";
		String yAxisLabel = "Y";
		
		JFreeChart chart = ChartFactory.createXYLineChart(
								chartTitle, xAxisLabel, 
								yAxisLabel, dataSet,
								PlotOrientation.VERTICAL,
								true,
								true,
								false);
		
		try {
			ChartUtilities.saveChartAsJPEG(new File("C:\\Users\\Alicia\\Desktop\\chart.jpg"), 
													chart, 500, 300);
		} catch (IOException e) {
			System.err.println("Problem occurred creating chart: " + e);
		}
	}
	
	
	/**
	 * Function: 	getDataSize()
	 * Parameters: 	String csv --	the name of the CSV file
	 * Description:	Finds the number of rows (data) in the CSV file by reading
	 * 				each row.
	 * Returns:		The data size of the CSV file
	 */
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
	
	
	/**
	 * Function: 	findSample()
	 * Parameters: 	double size	--	data size of the csv file
	 * Description:	Finds a specified percentage (eg. 10%) of the total
	 * 				data in the CSV file and marks if the index has occurred
	 * 				based on the random number being less than the threshold.
	 * 				Updates the threshold value based on Nn (number needed to
	 * 				complete sample) and Nr (Number of elements left to be
	 * 				tested)
	 * Returns:		totalSample array with updated occurrences of indices
	 */
	public static void findSample(double size) {
		// Set the sample size to 10%
		double sampleSize = size * percent; 
		
		// Number of selected items
		double count = 0;
		double thres;
		
		for(int i = 0; i < size; i++) {
			thres = (sampleSize - count)/(size - i);
			if(rnd.nextDouble() < thres){
				count++;
				totalSample[i]++;
			}
		}
	}
}