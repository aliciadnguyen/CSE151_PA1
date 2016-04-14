import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

class confusionMatrix {
	
	/**
	 * Make the data into vectors by putting them in a 2D array 
	 */
	public double[][] storeData(String csv) throws IOException {
		double[][] obserVectors;
		double[] obArray;
		
		sample s = new sample();
		int count = s.getDataSize(csv);
		
		// Read in CSV file
		InputStream in = sample.class.getClassLoader().getResourceAsStream(csv);
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				
		String row = null;
		int j = 0;
		while((row = reader.readLine()) != null) {
			int i = 0;
			String[] observation = row.split(",");
			obArray = new double[observation.length];
			obserVectors = new double[count][observation.length];
			for(String feature : observation) {
				obArray[i] = Double.parseDouble(feature);
				System.out.println(obArray[i]);
			}
			i++;
			obserVectors[j][i] = obArray[i];
			j++;
		}
		
		
		reader.close();
		return null;
	}
	

	/**
	 * Perform z-scaling on dataset
	 */
	public double[] zScale() {
		double [] scale;
		
		return scale;
	}
	
	/**
	 * Find K nearest neighbor of test sample 
	 */
	public double[] kNearestNeighbor() {
		double [] knn;
		
		return knn;
	}
}