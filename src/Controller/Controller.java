/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Loader.*;
import Classifier.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 *
 * @author asprak101
 */
public class Controller {
    LoadDataset load;
    String[] sheetsName;
    String[] headerName;
    public static String[] selectedHeader;
    public static List<List<Double>> dataset;
    public static List<String> datalabel;
    private int numClass;
    public static Set<String> label;
    private Classifier cl;
    
    
    public void loadFile(String filename) throws Exception {
        load = new LoadDataset(filename);
        sheetsName = load.getSheetsName();
        selectedHeader = new String[2];
    }
    
    public void chooseSheet(int idx) {
        load.setSpreadsheet(idx);
    }
    
    public void getHeader() {
        headerName = load.getHeader();
    }
    
    public void getData() {
        dataset = load.getDataset();
        datalabel = load.getDatalabel();
        label = new HashSet<>();
        label.addAll(datalabel);
        numClass = label.size();
    }
    
    public void mainMenu() throws Exception {
        Scanner s1 = new Scanner(System.in);
        Scanner s2 = new Scanner(System.in);
        Scanner s3 = new Scanner(System.in);
        Set<Integer> excluded = new HashSet<>();
        System.out.println("======================= Choose the Data File =======================");
        System.out.print("File name (including the file extension) = ");
        String filename = s1.nextLine();
        System.out.println("");
        loadFile(filename);
        System.out.println("");
        System.out.println("==================== Choose the Sheet of the Data ==================");
        int i = 1;
        for (String s : sheetsName) {
            System.out.println(i + ". " + s);
            i++;
        }
        System.out.println("");
        System.out.print("Choose sheet number = ");
        int choice = s2.nextInt();
        chooseSheet(choice-1);
        System.out.println("Chosen sheet = " + sheetsName[choice-1]);
        load.setHeader();
        getHeader();
        System.out.println("");
        System.out.println("============== Exclude attribute(s) to be used in ===================");
        System.out.println("====================== the classification ===========================");
        char ans;
        i = 1;
        for (String s : headerName) {
            System.out.println(i + ". " + s);
            i++;
        }
        do {
            System.out.print("Exclude an Attribute? (Y / N) = ");
            ans = s3.next().charAt(0);
            System.out.println("");
            if (ans == 'y' || ans == 'Y') {
                System.out.print("Choose the attribute to exclude (attribute number) = ");
                int exc = s2.nextInt();
                excluded.add(exc-1);
                System.out.println("Ok!");
                System.out.println("");
            } else if (ans == 'n' || ans == 'N') {
                break;
            }
        } while (ans == 'y' || ans == 'Y' || (ans != 'n' && ans != 'N'));
        System.out.print("Excluded attribute(s) = ");
        for (Integer x : excluded) {
            System.out.print(headerName[x] + ", ");
        }
        System.out.println("");
        System.out.println("");
        System.out.println("================ Choose attribute that will be used =================");
        System.out.println("====================== as x-axis and y-axis =========================");
        i = 1;
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
        selectedHeader[0] = headerName[attr1-1];
        selectedHeader[1] = headerName[attr2-1];
        System.out.println("Selected x-axis = " + selectedHeader[0]);
        System.out.println("Selected y-axis = " + selectedHeader[1]);
        System.out.println("");
        load.setData(excluded, attr1-1, attr2-1);
        getData();
        System.out.println("=========================== Dataset details ========================");
        System.out.println("Dataset length = " + dataset.size());
        System.out.println("Label length = " + datalabel.size());
        System.out.println("Attribute 1 = " + selectedHeader[0]);
        System.out.println("Attribute 2 = " + selectedHeader[1]);
        System.out.println("Number of Class = " + numClass);
        System.out.println("Number of Attribute(s) per data = " + dataset.get(0).size());
        System.out.println("");
        cl = new Classifier();
//        cl.createDataset(dataset, datalabel, label, selectedHeader, sheetsName[choice-1] + " Dataset Classification");
        cl.visualize();
                
    }
}
