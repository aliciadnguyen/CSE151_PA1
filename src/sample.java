import java.io.*;
import java.util.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class sample {
	/**
	 * Function: 	createChart()
	 * Parameters: 	None, uses static int[] trials and double[] mean
	 * Description:	Creates a datasets of x and y plots. Number of trials
	 * 		is on the x-axis and normalized mean is on the y-axis
	 * Returns:	A graph made on Desktop
	 */
	public void createChart(double[] means, int[] trials) {
		XYSeriesCollection dataSet = new XYSeriesCollection();
		XYSeries series = new XYSeries("Plotted Points");
		
		for(int m = 0; m < means.length; m++) {
			series.add(trials[m], means[m]);
			//System.out.println("Trials: " + trials[m]);
			//System.out.println("Means: " + means[m]);
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
			ChartUtilities.saveChartAsJPEG(
				new File("C:\\Users\\Alicia\\Desktop\\chart.jpg"), 
				chart, 500, 300);
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
	public void findSample(Random rnd, double[] totalSample, double size, double percent) {
		// Set the sample size to 10%
		double sampleSize = size * percent; 

		// Number of selected items
		double count = 0;
		double thres = 0;
		
		for(int i = 0; i < size; i++) {
			thres = (sampleSize - count)/(size - i);
			if(rnd.nextDouble() < thres){
				count++;
				totalSample[i]++;
			}
		}
	}
}
