/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classifier;

import Controller.Controller;

// Import List and Set Library
import java.util.List;
import java.util.Set;

// import Swing Java GUI Library
import javax.swing.JFrame;

/** 
 * Import JFreeChart Library
 * JFreeChart Library is a Java library that's used to make a chart/plot/graph
 * More about JFreeChart library documentation :
 * Link : http://www.jfree.org/jfreechart/api/javadoc/index.html
 */
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;

/**
 *
 * @author asprak101
 * 
 * A class to make a new Scatter plot object
 * based on the provided dataset
 * 
 */
public class ScatterPlot extends JFrame {
    Classifier cl; // Object Classifier
    private XYDataset dataset; // Dataset that will be visualized
    
    /**
     *
     * Create a new Classifier object and call @method createDataset
     * Set @attribute dataset with the return of @method getDataset
     * from Classifier object
     * Create a new @attribute chart with JFreeChart type and put
     * it into a new ChartPanel
     * 
     */
    public ScatterPlot(String title) {
        super(title); // Call the parent constructor
        cl = new Classifier(); // Create new Classifier object
        
        // Call @method createDataset to initialize dataset grouping
        cl.createDataset(Controller.dataset, Controller.datalabel, Controller.label, Controller.selectedHeader, title);
        
        // Assign @attribute dataset with the return of @method getDataset
        this.dataset = cl.getDataset();
        
        // Create new JFreeChart object with the provided chart title, title of
        // x-axis and y-axis, and @attribute dataset
        JFreeChart chart = ChartFactory.createScatterPlot(title, Controller.selectedHeader[0], 
                Controller.selectedHeader[1], this.dataset, PlotOrientation.HORIZONTAL, 
                rootPaneCheckingEnabled, rootPaneCheckingEnabled, rootPaneCheckingEnabled);
        
        // Create new panel to contain the chart
        ChartPanel panel = new ChartPanel(chart);
        setContentPane(panel);
        
    }
    
}
