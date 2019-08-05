/**********************************************************
 * Assignment: Text Excel Checkpoint 5: Cell Super Class
 *
 * Author: Sun-Jung Yum
 *
 * Description: This is a super class for all cells, which stores the original
 * value, constructs a cell, and gets the original value to display. It also
 * has an abstract method that returns a String to get the display value to
 * display. Each subclass will do this differently.
 *
 * Academic Integrity: I pledge that this program represents my own work. I
 * received help from no one in designing and debugging my program.
 **********************************************************/

package textExcelEC;

/* Cell represents a single cell in a spreadsheet. */
public abstract class Cell
{
	/* store the original value exactly as entered by the user. */
	private String originalValue;

	/* construct a new cell, given whatever the user typed. */
	public Cell(String value)
	{
		originalValue = value;
	}

	/* get the original value, suitable for individual display. */
	public String getOriginalValue()
	{
		return originalValue;
	}

	/*
	 * get the formatted value, suitable for display within the spreadsheet
	 * grid. Each subclass must write its own logic for this.
	 */
	public abstract String getDisplayValue();

}
