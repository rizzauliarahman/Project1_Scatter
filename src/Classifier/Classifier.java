/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classifier;

import Controller.Controller;

// Import List and Set Library
import java.util.ArrayList;
import java.util.Iterator;
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
    private List<Double[]> classBound = new ArrayList<>();
    
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
            
            Double[] bound = {series1.getMinX(), series1.getMaxX(), series1.getMinY(), series1.getMaxY()};
            classBound.add(bound);
            
            data.addSeries(series1);
            
        }
        
        XYSeriesCollection tmp = new XYSeriesCollection();
        
        for (int i = 0; i < data.getSeriesCount(); i++) {
            XYSeries preprocessed = preprocessing(data.getSeries(i), classBound);
            
            tmp.addSeries(preprocessed);
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
    public void visualize() {
        SwingUtilities.invokeLater(() -> {           
            //Create a new scatter object to visualize the data
            ScatterPlot scatter = new ScatterPlot(chartTitle);
            
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
    
    /**
     *
     * A method to preprocess the dataset so only
     * unique data (not-ambiguous) for each class (label)
     * that will be visualized
     * 
     */
    public XYSeries preprocessing(XYSeries series, List<Double[]> classBound) {
        // Create @attribute preprocessed with XYSeries type
        // to contains the preprocessed dataset
        XYSeries preprocessed = new XYSeries(series.getKey());
        List<String> datalabel = Controller.datalabel;
        Object[] tmp = Controller.label.toArray();
        String[] label = new String[tmp.length];
        
        int k = 0;
        for (Object o : tmp) {
            label[k] = (String) o;
            k++;
        }
        
        // Iterate to check if the data is ambiguous
        for (int i = 0; i < series.getItemCount(); i++) {
            boolean check = true;
            for (int j = 0; j < classBound.size(); j++) {
                if (datalabel.get(i) != label[j]) {
                    Double x = (Double) series.getX(i);
                    Double y = (Double) series.getY(i);
                    if ((x >= classBound.get(j)[0]) && (x <= classBound.get(j)[1])) {
                        if ((y >= classBound.get(j)[2]) && (y <= classBound.get(j)[3])) {
                            check = false;
                            break;
                        }
                    }
                }
            }
            
            if (check == true) {
                preprocessed.add(series.getDataItem(i));
            }
        }
        
        return preprocessed;
    }
    
}
