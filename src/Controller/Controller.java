/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Loader.*;
import Classifier.*;

// Import Java List, ArrayList, Set, HashSet, and Scanner Library
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 *
 * @author asprak101
 * 
 * A class that will be the controller of the program
 * All Input-Output process will be defined here
 * Main menu also will be defined here
 * 
 */
public class Controller {
    LoadDataset load; // Create @object load from class LoadDataset
    String[] sheetsName; // Array to store the name of all sheets in the file
    String[] headerName; // Array to store the title of all attributes in the dataset
    String[] selectedHeader; // Array to store the title of selected attributes
    List<List<Double>> dataset; // List to store the dataset from the file
    List<String> datalabel; // List to store label of each data in dataset
    private int numClass; // Number of classes in the dataset
    Set<String> label; // Label list of all classes in dataset
    private Classifier cl; // Create @object cl from class Classifier
    
    /**
     * A method to instantiate @object load and
     * store the sheets name in @attribute sheetsName
     */
    public void loadFile(String filename) throws Exception {
        load = new LoadDataset(filename); // Instantiate @object load
        sheetsName = load.getSheetsName(); // assign @attribute sheetsName
        selectedHeader = new String[2]; // Instantiate @aatribute selectedHeader
    }
    
    /**
     * A method to set the selected Sheet for
     * classification by calling @method setSpreadsheet
     * in @object load
     */
    public void chooseSheet(int idx) {
        // Call @method setSpreadsheet in @object load
        load.setSpreadsheet(idx);
    }
    
    /**
     * A method to assign @attribute headerName
     * with return value of @method getHeader in
     * @object load
     */
    public void getHeader() {
        headerName = load.getHeader();
    }
    
    /**
     * A method to load dataset and data label
     * by calling @method getDataset and getDatalabel
     * in @object load
     * Instantiate and assign the value in @attribute datalabel
     * to @attribute label
     * Assign the size of @attribute label to @attribute numClass
     */
    public void getData() {
        dataset = load.getDataset();
        datalabel = load.getDatalabel();
        label = new HashSet<>();
        label.addAll(datalabel);
        numClass = label.size();
    }
    
    /**
     * A method to show the main menu and
     * Input-Output process
     */
    public void mainMenu() throws Exception {
        // Create new Scanner object to utilize the input process
        Scanner s1 = new Scanner(System.in);
        Scanner s2 = new Scanner(System.in);
        Scanner s3 = new Scanner(System.in);
        
        // Create @attribute excluded to store the excluded attribute(s) index
        Set<Integer> excluded = new HashSet<>();
        
        // Ask for file name input from user
        System.out.println("======================= Choose the Data File =======================");
        System.out.print("File name (including the file extension) = "); // Ask for user input
        String filename = s1.nextLine();
        System.out.println("");
        loadFile(filename); // Call @method loadFile
        System.out.println("");
        
        // Ask for sheets name input that will be used for classification
        System.out.println("==================== Choose the Sheet of the Data ==================");
        int i = 1;
        
        // Iterate to show the sheets name in the file
        for (String s : sheetsName) {
            System.out.println(i + ". " + s);
            i++;
        }
        System.out.println("");
        System.out.print("Choose sheet number = "); // Ask for user input
        int choice = s2.nextInt();
        chooseSheet(choice-1); // Call @method chooseSheet
        
        // Show the title of chosen sheet
        System.out.println("Chosen sheet = " + sheetsName[choice-1]);
        
        load.setHeader(); // Call @method setHeader in @object load
        getHeader(); // Call @method getHeader
        System.out.println("");
        
        // Ask user if there is any attribute that will be
        // excluded from the classification
        System.out.println("============== Exclude attribute(s) to be used in ===================");
        System.out.println("====================== the classification ===========================");
        char ans;
        i = 1;
        
        // Iterate to show the attribute label
        for (String s : headerName) {
            System.out.println(i + ". " + s);
            i++;
        }
        
        // Iterate to ask the excluded attribute
        // Iteration will stop if user input is 'n' or 'N'
        do {
            System.out.print("Exclude an attribute (Y / N) = ");
            ans = s3.next().charAt(0);
            
            // Check if @attribute ans is 'y' or 'Y'
            if (ans == 'y' || ans == 'Y') {
                System.out.print("Choose the attribute to exclude (attribute number) = ");
                int exc = s2.nextInt();
                
                // Add the excluded attribute index to @attribute excluded
                excluded.add(exc-1);
                
                System.out.println("Ok!");
            }
            System.out.println("");
        } while (ans == 'y' || ans == 'Y' || (ans != 'n' && ans != 'N'));
        
        // Show the excluded attribute(s)
        System.out.print("Excluded attribute(s) = ");
        // Iterate to show the label of excluded attribute(s)
        for (Integer x : excluded) {
            System.out.print(headerName[x] + ", ");
        }
        System.out.println("");
        System.out.println("");
        
        // Ask user to input two attributes that will be
        // used as x-axis and y-axis in the visualization
        System.out.println("================ Choose attribute that will be used =================");
        System.out.println("====================== as x-axis and y-axis =========================");
        i = 1;
        
        // Iterate to show the attributes label
        for (String s : headerName) {
            String c = "";
            if (excluded.contains(i-1)) {
                c = "(EXCLUDED)";
            }
            System.out.println(i + ". " + s + " " + c);
            i++;
        }
        System.out.println("");
        boolean check;
        int attr1, attr2;
        
        // Ask for user input on @attribute attr1, attr2
        // Iteration will not stop as long as attr1 = attr2, or
        // attr1 or attr2 is the excluded attribute
        do {
            check = true;
            System.out.print("Choose the x-axis (attribute number) = ");
            attr1 = s2.nextInt();
            System.out.print("Choose the y-axis (attribute number) = ");
            attr2 = s2.nextInt();
            if (attr1 == attr2) {
                System.out.println("x-axis and y-axis can't be the same attribute");
                System.out.println("");
                check = false;
            }
            if (excluded.contains(attr1-1) || excluded.contains(attr2-1)) {
                System.out.println("You can't select the excluded attribute");
                System.out.println("");
                check = false;
            }
        } while(check == false);
        System.out.println("");
        
        // Assign the selected header with selected attributes
        selectedHeader[0] = headerName[attr1-1];
        selectedHeader[1] = headerName[attr2-1];
        
        // Show the selected attributes
        System.out.println("Selected x-axis = " + selectedHeader[0]);
        System.out.println("Selected y-axis = " + selectedHeader[1]);
        System.out.println("");
        
        // Call @method setData in @object load
        load.setData(excluded, attr1-1, attr2-1);
        getData(); // Call @method getData
        
        // Show the dataset details
        System.out.println("=========================== Dataset details ========================");
        System.out.println("Dataset length = " + dataset.size());
        System.out.println("Label length = " + datalabel.size());
        System.out.println("Attribute 1 = " + selectedHeader[0]);
        System.out.println("Attribute 2 = " + selectedHeader[1]);
        System.out.println("Number of Class = " + numClass);
        System.out.println("Number of Attribute(s) per data = " + dataset.get(0).size());
        System.out.println("");
        cl = new Classifier(); // Instantiate @attribute cl
//        cl.createDataset(dataset, datalabel, label, selectedHeader, sheetsName[choice-1] + " Dataset Classification");
        
        // Visualize the dataset
        cl.visualize(dataset, datalabel, label, selectedHeader);
                
    }
}
