/**********************************************************
 * Assignment: Text Excel Checkpoint 5: Formula Cell
 *
 * Author: Sun-Jung Yum
 *
 * Description: This is a subclass of Cell, made specifically for Formulas.
 * It stores a String variable that is the inputted expression. It has its
 * own method for getting the display value.
 *
 * Academic Integrity: I pledge that this program represents my own work. I
 * received help from no one in designing and debugging my program.
 **********************************************************/

package textExcelEC;

/* 
 * FormulaCell represents a cell that contains a formula, anything
 * the user entered by typing something with parentheses around it
 */
public class FormulaCell extends Cell implements Numeric
{
	private String expression;
	private Spreadsheet sheet;

	/*
	 * Creates a new FormulaCell. If value isn't surrounded by parentheses, this
	 * will throw an exception and no FormulaCell will be created. A separate
	 * exception is thrown in the CellFactory if the expression is not valid but
	 * it's surrounded by parentheses.
	 */
	public FormulaCell(String value, Spreadsheet theSheet)
	{
		super(value);
		sheet = theSheet;
		if (!value.startsWith("(") || !value.endsWith(")"))
			throw new IllegalArgumentException(
					"Formulas need to start and end with parentheses. '" + value + "' did not.");
	}

	/*
	 * return a double suitable for display in the grid, after simplifying the
	 * expression
	 */
	@Override
	public String getDisplayValue()
	{
		expression = getOriginalValue().substring(1, getOriginalValue().length() - 1);
		return "" + ExpressionSimplifier.simplify(expression, sheet);
	}

	public double getNumericValue()
	{
		return Double.parseDouble(getDisplayValue());
	}
}
