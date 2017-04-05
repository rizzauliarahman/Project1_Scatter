/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Loader;

// Import Java Set and I/O Library
import java.io.*;
import java.util.*;

/**
 * 
 * Import Apache-POI Library
 * Apache-POI Library is a library that used to
 * load and write an excel file (.xls, .xlsx)
 * into/from a Java program
 * More documentation about Apache-POI :
 * https://poi.apache.org/apidocs/index.html
 * 
 */
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

/**
 *
 * @author asprak101
 * 
 * A class that used to load dataset and
 * the other details from an excel (dataset) file
 * Component that are loaded : dataset, header, dataset sheet's name,
 * data label (class)
 * 
 */
public class LoadDataset {
    private XSSFWorkbook workbook; // Contains the excel file
    private XSSFSheet spreadsheet; // Contains the selected spreadsheet
    private static XSSFRow row; // Used to read the dataset by each row
    
    // @attribute sheetsName used to contain the sheets list of the excel file
    // @attribute header used to contain the header list of the dataset
    private String[] sheetsName, header;
    
    // Attribute that will be containing the loaded dataset
    private List<List<Double>> dataset = new ArrayList<>();
    
    // Attribute that will be containing the loaded data label (class)
    private List<String> datalabel = new ArrayList<>();
    
    /**
     *
     * Create new File object and load the file
     * based on provided filename
     * If the load process is success show the notifications and
     * so if the load process is failed
     * Get the sheets list of the file and put
     * them in @attribute sheetsName
     * 
     */
    public LoadDataset(String filename) throws Exception {
        // Create new file object with @param filename
        File file = new File(filename);
        
        // Prepare the input stream for the file
        FileInputStream fis = new FileInputStream(file);
        
        // Read the file and put it into @atttribute workbook
        workbook = new XSSFWorkbook(fis);
        
        // Show notifications
        if (file.isFile() && file.exists()) {
            System.out.println("Load File SUCCESS!");
        } else {
            System.out.println("Load File ERROR!");
        }
        
        // Count the number of sheets in the file
        int nSheet = workbook.getNumberOfSheets();
        
        // Determine the size of @attribute sheetsName
        sheetsName = new String[nSheet];
        
        // Iterate to get every sheet's Name in the file and
        // put them in @attribute sheetsName
        for (int i = 0; i < nSheet; i++) {
            sheetsName[i] = workbook.getSheetName(i);
        }
    }

    /**
     *
     * @return @attribute sheetsName
     * 
     */
    public String[] getSheetsName() {
        return sheetsName;
    }

    /**
     *
     * set @attribute spreadsheet with
     * selected spreadsheet (determined
     * by @param idx)
     * 
     */
    public void setSpreadsheet(int idx) {
        this.spreadsheet = workbook.getSheetAt(idx);
    }
    
    /**
     *
     * Get the header of the dataset and
     * put them in @attribute header
     * 
     */
    public void setHeader() {
        // Create @attribute rowIterator to iterate over every row
        Iterator<Row> rowIterator = spreadsheet.iterator();
        
        // Assign @attribute row with the first row
        row = (XSSFRow) rowIterator.next();
        
        // Set the size of @attribute header
        header = new String[row.getLastCellNum()-1];
        
        // Create @attribute cellIterator to iterate over every
        // cells in a row
        Iterator<Cell> cellIterator = row.cellIterator();
        
        // Iterate over every cells, and put the cell contents
        // in @attribute header
        for (int i = 0; i < header.length; i++) {
            Cell cell = cellIterator.next();
            header[i] = cell.getStringCellValue();
        }
    }
    
    /**
     *
     * @return @attribute header
     * 
     */
    public String[] getHeader() {
        return header;
    }
    
    /**
     *
     * Load the dataset where the attribute loaded
     * is determined by @param idx1 and @param idx2
     * and put them in @attribute dataset
     * 
     */
    public void setData(Set<Integer> excluded, int idx1, int idx2) {
        // Create @attribute rowIterator to iterate every rows
        Iterator<Row> rowIterator = spreadsheet.iterator();
        
        // Assign @attribute row with the first row
        row = (XSSFRow) rowIterator.next();
        
        // Iterate as long as rowIterator isn't
        // the last row
        while (rowIterator.hasNext()) {
            // Attribute that will be used to contain the atttribute
            // in a data (row)
            List<Double> data_row = new ArrayList<>();
            
            // Assign @attribute row with next row
            row = (XSSFRow) rowIterator.next();
            
            // Create @attribute cellIterator to iterate every
            // cells in a row
            Iterator<Cell> cellIterator = row.cellIterator();
            
            // Iterate as long as cellIterator isn't
            // the last cell
            while (cellIterator.hasNext()) {
                // Create @attribute cell and assign it with
                // next cell
                Cell cell = cellIterator.next();
                
                // Just take the selected attributes (determined
                // by column @param attr1 and column @param attr2)
                if (excluded.contains(cell.getColumnIndex())) {
                    continue;
                } else if (cell.getColumnIndex() == row.getLastCellNum()-1) {
                    if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                        Double res = cell.getNumericCellValue();
                        datalabel.add(String.valueOf(res.intValue()));
                    } else {
                        datalabel.add(cell.getStringCellValue());
                    }
                } else if ((cell.getColumnIndex() == idx1) || (cell.getColumnIndex() == idx2)) {
                    data_row.add(cell.getNumericCellValue());
                }
            }
            
            // Insert the data into @attribute dataset
            dataset.add(data_row);
        }
    }
    
    /**
     *
     * @return @attribute dataset
     * 
     */
    public List<List<Double>> getDataset() {
        return dataset;
    }
    
    /**
     *
     * @return @attribute datalabel
     * 
     */
    public List<String> getDatalabel() {
        return datalabel;
    }
    
}
