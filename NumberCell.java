/**********************************************************
 * Assignment: Text Excel Checkpoint 5: Number Cell
 *
 * Author: Sun-Jung Yum
 *
 * Description: This is a subclass of Cell, made specifically for Numbers.
 * It stores a double variable that is the inputted value. It has its own
 * method for getting the display value.
 *
 * Academic Integrity: I pledge that this program represents my own work. I
 * received help from no one in designing and debugging my program.
 **********************************************************/

package textExcelEC;

/* Represents a cell that stores a number */
public class NumberCell extends Cell implements Numeric
{
	/* store the number in its native form. */
	private double value;

	/*
	 * constructs a new NumberCell. If the input can't be parsed, this will throw
	 * a NumberFormatException and the caller will know that no NumberCell could
	 * be constructed.
	 */
	public NumberCell(String input)
	{
		super(input);

		value = Double.parseDouble(input);
	}

	/* return the number in the correct form for display in the grid. */
	@Override
	public String getDisplayValue()
	{
		return "" + value;
	}

	public double getNumericValue()
	{
		// TODO Auto-generated method stub
		return value;
	}


}
