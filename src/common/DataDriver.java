package common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Hashtable;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DataDriver
{
	// Used to hold data workbook name
	static String dataSource = null;
	
	// Number of rows and columns in data sheet
	public static int numColumns;
	public static int numRows;
	
	// Hash to store column names in data sheets
	public static Hashtable<String, Integer> columnNamesHash = new Hashtable<String, Integer>();
	
	// Location of the data sheet containing test data	
	public static void assignDataSource(String dataLocation, String testName)
	{
		dataSource = dataLocation + "\\" + testName + ".xlsx";
	}
	
	// Passes name of data sheet into method which then calls method to read its data
	public static String[][] getData(String dataSheet)
	{
		// Specify data to be retrieved into an array
		String[][] dataForTest = null;
		
		try {
				dataForTest = (getDataSheet(dataSheet));
			} catch (IOException e)
				{
					System.out.println("Problem getting data from spreadsheet!");
				}
		return dataForTest;
	}
	
	// Reads data from sheet
	public static String[][] getDataSheet(String dataSheet) throws IOException
	{
		// Read information from sheet into a two dimensional array.
		// No cells should contain numeric data - Example: If cell value is 0 change it to '0 in the sheet
		// so it is treated as text and not as a numeric value - Failure to do so will cause a failure.
		// Sheets must not contain blank cells or Null Data Exception will occur!
		// Tip: Open sheet, press Ctrl + End and make sure cell focus is on last cell containing data.
		// If not then correct the sheet to avoid the problem.
		// The for loop which handles the reading of data must start at 1 instead of 0
		// because row 0 is used to hold column names.
		File automationTestData = new File(dataSource);
		FileInputStream dataStream = new FileInputStream(automationTestData);

		XSSFWorkbook workbook = new XSSFWorkbook(dataStream);
		XSSFSheet worksheet = workbook.getSheet(dataSheet);
		
		numRows = worksheet.getLastRowNum() + 1;
		numColumns = worksheet.getRow(0).getLastCellNum();
		
		String[][] dataForTest = new String[numRows][numColumns];
		
		for (int rowIndex = 1; rowIndex < numRows; rowIndex ++) // Start at 1 to ignore column names on row 0
		{
			XSSFRow row = worksheet.getRow(rowIndex);
			for (int columnIndex = 0; columnIndex < numColumns; columnIndex ++)
			{
				XSSFCell cell = row.getCell(columnIndex);
                String value = cell.getStringCellValue();
                dataForTest[rowIndex][columnIndex] = value;
			}
		}
		return dataForTest;
	}
	
	// Passes name of data sheet into method which then calls method to read its column names
	public static String[] getDataColumns(String dataSheet)
	{
		// Specify data to be retrieved into an array
		String[] dataColumnsForTest = null;
		
		try {
				dataColumnsForTest = (getDataSheetColumns(dataSheet));
			} catch (IOException e)
				{
					System.out.println("Problem getting data from spreadsheet!");
				}
		return dataColumnsForTest;
	}
	
	// Reads the column names from a data sheet
	// Column names are to be on the first row (0) of the data sheet
	public static String[] getDataSheetColumns(String dataSheet) throws IOException
	{
		File automationTestData = new File(dataSource);
		FileInputStream dataStream = new FileInputStream(automationTestData);

		XSSFWorkbook workbook = new XSSFWorkbook(dataStream);
		XSSFSheet worksheet = workbook.getSheet(dataSheet);
		
		int columnNamesRow = 0;
		int numDataColumns = worksheet.getRow(columnNamesRow).getLastCellNum();
		
		String[] dataColumnsForTest = new String[numDataColumns];
		
			XSSFRow row = worksheet.getRow(columnNamesRow);
			for (int columnIndex = 0; columnIndex < numDataColumns; columnIndex ++)
			{
				XSSFCell cell = row.getCell(columnIndex);
                String value = cell.getStringCellValue();
                dataColumnsForTest[columnIndex] = value;
			}
			return dataColumnsForTest;
	}
	
	// Creates a hash table to store column names.
	// Allows a column to be referenced by a name instead of a predefined index number
	// Data in a sheet can be referenced through a row number (which will be a predefined value
	// declared in the class or the iteration row number if the TestNG DataProvider is used) and a
	// column name called via "column.get("Column Name")" defined in the hash table.
	public static Hashtable<String, Integer> getColumnNamesFromSheet(String dataSheet)
	{
		String[] columnNames = DataDriver.getDataColumns(dataSheet);
		
		int numDataColumns = columnNames.length;
		
		for (int hashIndex = 0; hashIndex < numDataColumns; hashIndex ++)
		{
			columnNamesHash.put(columnNames[hashIndex], hashIndex);
		}
		
		return columnNamesHash;
	}
}