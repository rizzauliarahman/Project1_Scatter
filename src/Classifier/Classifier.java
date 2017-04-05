/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classifier;

// Import List and Set Library
import java.util.List;
import java.util.Set;

// import Swing Java GUI Library
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import org.jfree.data.xy.XYDataItem;

/** 
 * Import JFreeChart Library
 * JFreeChart Library is a Java library that's used to make a chart/plot/graph
 * More about JFreeChart library documentation :
 * Link : http://www.jfree.org/jfreechart/api/javadoc/index.html
 */
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author asprak101
 * 
 * A class that will be used to classify the dataset and
 * visualize them in scatter plot
 * 
 */
public class Classifier {
    private XYDataset dataset; // dataset that will be used for visualization
    private String[] selectedHeader; // Attribute that will be used for visualization
    private String chartTitle; // Title of the chart
    
    /**
     *
     * A method to initialize @attribute dataset with XYDataset type that will be used
     * as the dataset for scatter plot generation
     * Method will search all appropriate data with each class label, and
     * insert them into @attribute dataset
     * 
     */
    public void createDataset(List<List<Double>> dataset, List<String> datalabel, 
            Set<String> label, String[] selectedHeader, String chartTitle) {
        // Set @attribute selectedHeader with @param selectedHeader
        this.selectedHeader = selectedHeader;
        
        // Set @attribute chartTitle with @param chartTitle
        this.chartTitle = chartTitle;
        
        // Initialize @attribute data with XYSeriesCollectionType
        // that will be used to store dataset for each label
        XYSeriesCollection data = new XYSeriesCollection();
        
        /**
         * 
         * For-loop to search for all data in each label, and
         * store them in @attribute series1
         * 
         */
        for (String s : label) {
            XYSeries series1 = new XYSeries(s);
            for (List<Double> a : dataset) {
                if (datalabel.get(dataset.indexOf(a)).equals(s)) {                    
                    series1.add(a.get(0), a.get(1));
                }
            }
            
            XYSeries preprocessed = preprocessing(series1);
            
            // insert @attribute series1 to @attribute data
            data.addSeries(preprocessed);
        }
        
        // Copy the content of @attribute data to @attribute dataset
        this.dataset = data;
    }
    
    /**
     *
     * A method to visualize the created dataset
     * into a scatter plot with selected attributes as x-axis and y-axis
     * 
     */
    public void visualize(List<List<Double>> dataset, List<String> datalabel, Set<String> label, String[] selectedHeader) {
        SwingUtilities.invokeLater(() -> {           
            //Create a new scatter object to visualize the data
            ScatterPlot scatter = new ScatterPlot(dataset, datalabel, label, chartTitle, selectedHeader);
            
            // Set the size of scatter plot
            scatter.setSize(800, 400);
            
            // Set the location of scatter
            scatter.setLocationRelativeTo(null);
            
            // Set Default Operation to do when Close button is clicked
            scatter.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            
            // Show the scatter plot
            scatter.setVisible(true);
        });        
    }
    
    /**
     *
     * @return @attribute dataset
     * 
     */
    public XYDataset getDataset() {
        return this.dataset;
    }
    
    public XYSeries preprocessing(XYSeries series) {
        XYSeries preprocessed = new XYSeries(series.getKey());
        double x_avg = 0, y_avg = 0;
        
        for (int i = 0; i < series.getItemCount(); i++) {
            x_avg += (Double) series.getX(i);
            y_avg += (Double) series.getY(i);
        }
        
        x_avg /= series.getItemCount();
        y_avg /= series.getItemCount();
        
        /**
         * 
         */
        for (int i = 0; i < series.getItemCount(); i++) {
            double x_dist = Math.abs(((Double) series.getX(i) - x_avg));
            double y_dist = Math.abs(((Double) series.getY(i) - y_avg));
            if ((y_dist <= 0.3*(y_avg - series.getMinY())) && 
                    (x_dist <= 0.3*(x_avg - (Double) series.getMinX()))) {
                preprocessed.add(series.getDataItem(i));
            }
        }
        
        return preprocessed;
    }
    
}
