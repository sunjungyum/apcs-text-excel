/**********************************************************
 * Assignment: Text Excel Checkpoint 5: Date Cell
 *
 * Author: Sun-Jung Yum
 *
 * Description: This is a subclass of Cell, made specifically for Dates. It
 * stores a Date variable, and also uses the object of SimpleDateFormat to
 * properly format a date. It has its own method for getting the display
 * value.
 *
 * Academic Integrity: I pledge that this program represents my own work. I
 * received help from no one in designing and debugging my program.
 **********************************************************/

package textExcelEC;

import java.text.*;
import java.util.Date;

/* Represents a cell that stores a date. */
public class DateCell extends Cell
{
	/* the date this cell holds */
	private Date date;

	/* an object that helps us parse and format the date */
	private SimpleDateFormat format;

	/*
	 * construct a new DateCell. If the input string isn't actually a date, this
	 * will throw an exception so the caller knows it was invalid.
	 */
	public DateCell(String input) throws ParseException
	{
		super(input);

		format = new SimpleDateFormat("mm/dd/yyyy");
		date = format.parse(input);
	}

	/*
	 * return the date appropriately formatted for display in the grid.
	 */
	@Override
	public String getDisplayValue()
	{
		return format.format(date);
	}

}
