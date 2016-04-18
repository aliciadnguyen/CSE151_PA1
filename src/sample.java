import java.io.*;
import java.util.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class sample {
	double [][] test;
	double [][] train;
	
	public sample(double[][] test, double[][] train) {
		this.test = test;
		this.train = train;
	}
	
	/**
	 * Function: 	createChart()
	 * Parameters: 	None, uses static int[] trials and double[] mean
	 * Description:	Creates a datasets of x and y plots. Number of trials
	 * 		is on the x-axis and normalized mean is on the y-axis
	 * Returns:	A graph made on Desktop
	 */
	public void createChart(double[] means, double[] stds, int[] trials) {
		XYSeriesCollection meanDataSet = new XYSeriesCollection();
		XYSeriesCollection stdDataSet = new XYSeriesCollection();
		XYSeries meanSeries = new XYSeries("Plotted Points");
		XYSeries stdSeries = new XYSeries("Plotted Points");
		
		for(int m = 0; m < means.length; m++) {
			meanSeries.add(trials[m], means[m]);
			stdSeries.add(trials[m], stds[m]);
			//System.out.println("Trials: " + trials[m]);
			//System.out.println("Means: " + means[m]);
		}
		
		meanDataSet.addSeries(meanSeries);
		stdDataSet.addSeries(stdSeries);
		
		String meanChartTitle = "Runs vs Mean Graph";
		String stdChartTitle = "Runs vs Standard Deviation Graph";
		String xAxisLabel = "X";
		String yAxisLabel = "Y";
		
		JFreeChart meanChart = ChartFactory.createXYLineChart(
					meanChartTitle, xAxisLabel, 
					yAxisLabel, meanDataSet,
					PlotOrientation.VERTICAL,
					true,
					true,
					false);
		JFreeChart stdChart = ChartFactory.createXYLineChart(
				stdChartTitle, xAxisLabel, 
				yAxisLabel, stdDataSet,
				PlotOrientation.VERTICAL,
				true,
				true,
				false);
		
		try {
			ChartUtilities.saveChartAsJPEG(
				new File("C:\\Users\\Alicia\\Desktop\\meanChart.jpg"), 
				meanChart, 500, 300);
			ChartUtilities.saveChartAsJPEG(
				new File("C:\\Users\\Alicia\\Desktop\\stdChart.jpg"), 
				stdChart, 500, 300);
		} catch (IOException e) {
			System.err.println("Problem occurred creating chart: " + e);
		}
	}
	
	
	/**
	 * Function: 	getDataSize()
	 * Parameters: 	String csv --	the name of the CSV file
	 * Description:	Finds the number of rows (data) in the CSV file by reading
	 * 		each row.
	 * Returns:	The data size of the CSV file
	 */
	public int getDataSize(String csv) throws IOException {
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
		
		reader.close();
		return dataSize;
	}
	
	
	/**
	 * Function: 	findSample()
	 * Parameters: 	double size	--	data size of the csv file
	 * Description:	Finds a specified percentage (eg. 10%) of the total
	 * 		data in the CSV file and marks if the index has occurred
	 * 		based on the random number being less than the threshold.
	 * 		Updates the threshold value based on Nn (number needed to
	 * 		complete sample) and Nr (Number of elements left to be
	 * 		tested)
	 * Returns:	totalSample array with updated occurrences of indices
	 */
	public sample findSample(Random rnd, double[][] totalSample, double percent) {
		
		// Set the sample size to 10%
		double size = totalSample.length;
		double sampleSize = size * percent;
		
		int cols = totalSample[0].length;
		
		test = new double[(int)sampleSize][cols];
		train = new double[(int)(size - sampleSize)][cols];

		// Number of selected items
		double count = 0;
		double thres;
		
		int trainI = 0;
		int testI = 0;
		
		for(int i = 0; i < size - 1; i++) {
			thres = (sampleSize - count)/(size - i);
			if(rnd.nextDouble() < thres){
				count++;
				for(int j = 0; j < totalSample[i].length; j++) {
					test[testI][j] = totalSample[i][j];
				} 
				testI++;
			}
			else {
				for(int j = 0; j < totalSample[i].length; j++) {
					train[trainI][j] = totalSample[i][j];
				} 
				trainI++;
			}
		}
			
		sample s = new sample(test, train);
		return s;
	}
}
