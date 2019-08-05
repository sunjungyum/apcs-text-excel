/**********************************************************
 * Assignment: Text Excel Checkpoint 5: Spreadsheet
 *
 * Author: Sun-Jung Yum
 *
 * Description: This is a class that handles the spreadsheet. It has rows
 * and columns, as well as headers. It can print the entire spreadsheet by
 * printing the contents of each of the cells and displaying them. It also
 * is able to center Strings and clear cells, as well as compute the sum
 * and avg of a rectangular range of cells. Lastly, it can sort a rectangular
 * range of numeric cells and sort them, both in an ascending and descending
 * order.
 *
 * Academic Integrity: I pledge that this program represents my own work. I
 * received help from no one in designing and debugging my program.
 **********************************************************/

package textExcelEC;

import java.util.Arrays;

/* 
 * Spreadsheet stores a 2d array of Cell objects and is able to 
 * modify the array and display it by printing to System.out.
 */
public class Spreadsheet
{
	private final static int COLS = 7;
	private final static int ROWS = 10;
	private final static int CELL_WIDTH = 12;

	/* store the cells in a 2D array */
	private Cell[][] data;

	/*
	 * construct a new spreadsheet. each cell in 'data' will be null initially,
	 * representing an empty cell.
	 */
	public Spreadsheet()
	{
		data = new Cell[ROWS][COLS];
	}

	/* print the spreadsheet to system.out */
	public void print()
	{
		printHorizontalLine();
		printColumnHeaders();
		printHorizontalLine();

		for (int r = 0; r < ROWS; r++)
		{
			printRow(r);
			printHorizontalLine();
		}
	}

	/*
	 * check to see if 'ref' is a valid cell reference, like A3 or G10. if ref
	 * can't be parsed as a column and row, or if it is not in the appropriate
	 * range for this spreadsheet, return false.
	 */
	public boolean isCellReference(String ref)
	{
		if (ref == null || ref.length() < 2 || ref.length() > 3)
			return false;

		if (ref.charAt(0) < 'A' || ref.charAt(0) > 'A' + COLS)
			return false;

		int row = Integer.parseInt(ref.substring(1));
		if (row < 1 || row > ROWS)
			return false;

		return true;
	}

	/*
	 * display the value of a single cell, represented by ref, by printing it to
	 * system.out.
	 */
	public void displayCell(String ref)
	{
		Cell c = data[getRow(ref)][getCol(ref)];
		String value = (c == null) ? "<empty>" : c.getOriginalValue();
		System.out.println(ref + " = " + value);
	}

	/*
	 * store a cell at the specified location in the grid, replacing whatever
	 * might be there already.
	 */
	public void setCell(String ref, Cell cell)
	{
		try
		{
			data[getRow(ref)][getCol(ref)] = cell;
		} catch (Exception ex)
		{
			/* if it's not a valid cell */
		}

	}

	/*
	 * given a string that is supposed to be a reference to a cell, parse the
	 * row index from it (i.e. F4 will return 3 because 3 is the index of the
	 * 4th row in 'data').
	 */
	private int getRow(String ref)
	{
		if (!isCellReference(ref))
			throw new IllegalArgumentException(ref + " is not a valid cell reference");

		return Integer.parseInt(ref.substring(1)) - 1;
	}

	/*
	 * given a string that is supposed to be a reference to a column, parse the
	 * column index from it (e.g. C7 will return 2, since C is the 3rd column
	 * and its zero-based index in data would therefore be 2).
	 */
	private int getCol(String ref)
	{
		if (!isCellReference(ref))
			throw new IllegalArgumentException(ref + " is not a valid cell reference");

		return ref.charAt(0) - 'A';
	}

	/* print one line of +------------+--- etc. */
	private void printHorizontalLine()
	{
		for (int col = 0; col < COLS + 1; col++)
		{
			System.out.print("+");
			for (int ch = 0; ch < CELL_WIDTH; ch++)
				System.out.print("-");
		}
		System.out.println("+");
	}

	/* print the column header row */
	private void printColumnHeaders()
	{
		/* blank cell at top left */
		System.out.print("|" + center("", CELL_WIDTH));

		for (int col = 0; col < COLS; col++)
		{
			System.out.print("|");
			System.out.print(center((char) (col + 'A') + "", CELL_WIDTH));
		}
		System.out.println("|");
	}

	/*
	 * print the specified row of cells, including their left and right borders
	 */
	private void printRow(int row)
	{
		/* header column */
		System.out.print("|" + center(row + 1 + "", CELL_WIDTH));

		for (int col = 0; col < COLS; col++)
		{
			String value = (data[row][col] == null) ? "" : data[row][col].getDisplayValue();
			System.out.print("|" + center(value, CELL_WIDTH));
		}

		System.out.println("|");
	}

	/*
	 * given a string 'text', truncate or pad it to make it fit exactly in
	 * 'width' characters
	 */
	private String center(String text, int width)
	{
		if (text.length() > width)
			return text.substring(0, width - 1) + ">";

		String centered = "";
		int leftSpaces = (width - text.length()) / 2;
		for (int c = 0; c < leftSpaces; c++)
			centered += " ";
		centered += text;
		for (int c = centered.length(); c < width; c++)
			centered += " ";

		return centered;
	}

	/* clears the entire spreadsheet by setting each cell to null */
	public void clear()
	{
		for (int row = 0; row < ROWS; row++)
		{
			for (int col = 0; col < COLS; col++)
			{
				data[row][col] = null;
			}
		}
	}

	/* clears one cell given a reference like "A4" */
	public void clear(String ref)
	{
		try
		{
			data[getRow(ref)][getCol(ref)] = null;
		} catch (Exception ex)
		{
			/* if it's not a valid cell */
		}
	}

	/*
	 * looks up the value of a certain cell, using its reference, as long as
	 * it's a numeric cell
	 */
	public String lookUp(String ref)
	{
		/* check if valid cell reference */
		if (!isCellReference(ref))
		{
			System.out.println(ref + " is not a valid cell reference");
			throw new IllegalArgumentException();
		}

		/* check if numeric */
		if (!(data[getRow(ref)][getCol(ref)] instanceof Numeric))
		{
			System.out.println(ref + " is not a valid numeric cell reference");
			throw new IllegalArgumentException();
		}

		return data[getRow(ref)][getCol(ref)].getDisplayValue();
	}

	/*
	 * finds the sum of a rectangular selection of cells, as long as they are
	 * numeric cells
	 */
	public String findSum(String ref)
	{
		/* find first and second reference */
		String first = ref.substring(0, ref.indexOf(":"));
		String second = ref.substring(ref.indexOf(":") + 1);

		/* declare/initialize sum */
		int sum = 0;

		/* go through each cell and add to sum if numeric */
		for (int i = getRow(first); i < getRow(second) + 1; i++)
		{
			for (int j = getCol(first); j < getCol(second) + 1; j++)
			{
				checkNumeric(i, j);

				/* add value to sum */
				sum += Double.parseDouble(data[i][j].getDisplayValue());
			}
		}

		/* concatenation */
		return sum + "";
	}

	private String checkNumeric(int i, int j)
	{
		if (!(data[i][j] instanceof Numeric))
		{
			System.out.println("The cell reference includes cells that are not numeric");
			throw new IllegalArgumentException();
		}
		return data[i][j].getDisplayValue();
	}

	/*
	 * finds the average of a rectangular selection of cells, as long as they
	 * are numeric cells
	 */
	public String findAvg(String ref)
	{
		/* find first and second reference */
		String first = ref.substring(0, ref.indexOf(":"));
		String second = ref.substring(ref.indexOf(":") + 1);

		/* find number of cells */
		int numOfCells = Math.abs(((getRow(second) - getRow(first)) + 1) * ((getCol(second) - getCol(first)) + 1));

		/* divide sum by number of cells and concatenate */
		return (Double.parseDouble(findSum(ref)) / numOfCells) + "";
	}

	/*
	 * sorts a rectangular selection of cells (or a single row/column) in
	 * row-major order, as long as they are numeric cells
	 */
	public void sort(String ref, char command)
	{
		/* create one-dimensional array */
		Cell values[] = makeSortingArray(ref);

		/* merge sort the array */
		selectionSort(values);

		/* replace the sheet cells with the newly sorted cells */
		replaceInSheet(values, ref, command);
	}

	/*
	 * replaces the cells in the spreadsheet with the sorted cells in the
	 * one-dimensional sorting array
	 */
	private void replaceInSheet(Cell[] values, String ref, char command)
	{
		/* find first and second reference */
		String first = ref.substring(0, ref.indexOf(":"));
		String second = ref.substring(ref.indexOf(":") + 1);

		/* declare and initialize counter based on ascending or descending */
		int counter = 0;
		if (command == 'a')
		{
			counter = 0;
		}
		else
		{
			counter = values.length - 1;
		}

		/* put values back into array in correct order */
		for (int i = getRow(first); i <= getRow(second); i++)
		{
			for (int j = getCol(first); j <= getCol(second); j++)
			{
				data[i][j] = values[counter];
				if (command == 'a') /* if ascending */
				{
					counter++;
				}
				else /* if descending */
				{
					counter--;
				}
			}
		}
	}

	/* creates a one-dimensional array of cells that need to be sorted */
	private Cell[] makeSortingArray(String ref)
	{
		/* find first and second reference */
		String first = ref.substring(0, ref.indexOf(":"));
		String second = ref.substring(ref.indexOf(":") + 1);

		/* find number of cells */
		int numOfCells = Math.abs(((getRow(second) - getRow(first)) + 1) * ((getCol(second) - getCol(first)) + 1));

		/* declare array and counter */
		Cell[] values = new Cell[numOfCells];
		int counter = 0;

		/* put values into array if numeric */
		for (int i = getRow(first); i <= getRow(second); i++)
		{
			for (int j = getCol(first); j <= getCol(second); j++)
			{
				checkNumeric(i, j);

				/* add values to array */
				values[counter] = data[i][j];
				counter++;
			}
		}

		return values;
	}

	/*
	 * sorts the elements in an array of cells in ascending order, using the
	 * selection sort algorithm
	 */
	private void selectionSort(Cell[] values)
	{
		for (int i = 0; i < values.length; i++)
		{
			int min = i; /* temporary minimum that is compared to each value */
			for (int j = i + 1; j < values.length; j++)
			{
				/* if a different cell's value is less than the minimum */
				if (Double.parseDouble(values[j].getDisplayValue()) < Double.parseDouble(values[min].getDisplayValue()))
				{
					min = j;
				}
			}

			/* if the minimum was not the original minimum */
			if (min != i)
			{
				Cell temp = values[i];
				values[i] = values[min];
				values[min] = temp;
			}
		}
	}

}
