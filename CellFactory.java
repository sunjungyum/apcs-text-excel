/**********************************************************
 * Assignment: Text Excel Checkpoint 5: Cell Factory
 *
 * Author: Sun-Jung Yum
 *
 * Description: This class directs an input to the correct Cell subclass,
 * based on what it is (String, Date, Number, Formula). It will then create
 * the new, appropriate cell.
 *
 * Academic Integrity: I pledge that this program represents my own work. I
 * received help from no one in designing and debugging my program.
 **********************************************************/

package textExcelEC;

/* 
 * CellFactory contains a static method for creating new cells given
 * user input.
 */
public class CellFactory
{
	/*
	 * Given some user input like '3.14159' or '2/20/1950' or '"hi"', try to
	 * create each possible kind of cell until we find one that works.
	 */
	public static Cell create(String input, Spreadsheet sheet)
	{
		try
		{
			return new StringCell(input);
		} catch (Exception ex)
		{
			/* not a StringCell... */
		}

		try
		{
			return new NumberCell(input);
		} catch (Exception ex)
		{
			/* not a NumberCell, either */
		}

		try
		{
			return new DateCell(input);
		} catch (Exception ex)
		{
			/* not a DateCell */
		}

		try
		{
			/*
			 * check if expression is valid
			 */
			ExpressionSimplifier.simplify(input, sheet);
			return new FormulaCell(input, sheet);
		} catch (Exception ex)
		{
			/* not a FormulaCell */
		}

		/*
		 * if we get to here, the user input doesn't match any known cell type
		 * so we'll throw an exception
		 */
		throw new IllegalArgumentException("'" + input + "' cannot be parsed as a valid type of cell.");
	}

}
