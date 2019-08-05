/**********************************************************
 * Assignment: Text Excel Checkpoint 5: Expression Simplifier
 *
 * Author: Sun-Jung Yum
 *
 * Description: This class simplifies mathematical expressions down to an
 * answer that is a double. Expressions can involve multiplication, division,
 * addition, and subtraction, and answers are calculated using the correct
 * order of operations. It handles references to other cells, including sum
 * and avg reference commands. It also is able to calculate the correct answers
 * of expressions that use parentheses.
 *
 * Academic Integrity: I pledge that this program represents my own work. I
 * received help from no one in designing and debugging my program.
 **********************************************************/

package textExcelEC;

import java.security.InvalidParameterException;
import java.util.ArrayList;

import javax.xml.crypto.Data;

/**
 * ExpressionSimplifier has a static simplify method that can take a string with
 * a mathematical expression in it and reduce it down to a single term. For
 * example, 5 * 4 + 3 * 2 can be simplified to 26.
 */
public class ExpressionSimplifier
{
	/**
	 * The simplify method takes an expression like 3 + 2 as a parameter and
	 * returns a simplified version of it.
	 * 
	 * @param expression
	 *            - a valid math expression like 9 / 2
	 * 
	 * @return - a double representing the result of evaluating the expression
	 */
	public static double simplify(String expression, Spreadsheet sheet)
	{
		/* first check if there are any parenthesis */
		if (expression.indexOf(')') == -1)
		{
			/* make sure that the parenthesis is a pair */
			if (expression.indexOf('(') == -1)
			{
				ArrayList<String> tokens = convertToList(expression);

				/* replaces references with values */
				for (int i = 0; i < tokens.size(); i++)
				{
					if (sheet.isCellReference(tokens.get(i)))
					{
						replaceInList(tokens, i, 1, sheet.lookUp(tokens.get(i)));
					}
				}

				/* searches for sum and avg */
				for (int i = 0; i < tokens.size(); i++)
				{
					if (tokens.get(i).equals("sum") || tokens.get(i).equals("avg"))
					{
						/*
						 * replaces "sum" and reference tokens with actual sum
						 * value
						 */
						if (tokens.get(i).equals("sum"))
						{
							tokens.remove(i);
							tokens.set(i, sheet.findSum(tokens.get(i)));
						}

						/*
						 * replaces "avg" and reference tokens with actual avg
						 * value
						 */
						else
						{
							tokens.remove(i);
							tokens.set(i, sheet.findAvg(tokens.get(i)));
						}
					}
				}

				/* keep going until there is only one token */
				while (tokens.size() > 1)
				{
					int operatorIndex = findIndexOfNextOperator(tokens);
					String leftOperand = tokens.get(operatorIndex - 1);
					String operator = tokens.get(operatorIndex);
					String rightOperand = tokens.get(operatorIndex + 1);

					String result = calculate(leftOperand, operator, rightOperand);
					replaceInList(tokens, operatorIndex - 1, 3, result);
				}

				return Double.parseDouble(tokens.get(0));
			}
			else
			{
				throw new InvalidParameterException("No closing parenthesis was in expression");
			}
		}

		else
		{
			/*
			 * find the parentheses and how many characters will need to be
			 * replaced
			 */
			int close = expression.indexOf(')');
			int open = findOpen(expression, close);
			int howMany = (close - open) + 1;

			String replacement = "" + (int) (simplify(expression.substring(open + 1, close), sheet));

			if (expression.length() + 1 == close)
			{
				/* there's nothing else after */
				expression = expression.substring(0, open) + replacement + expression.substring(close);
			}
			else
			{
				/* there's something after */
				expression = expression.substring(0, open) + replacement + expression.substring(close + 1);
			}

			return simplify(expression, sheet);
		}

	}

	/**
	 * Given a String and the index of the closing parenthesis, this method
	 * finds the index of the corresponding opening parenthesis.
	 * 
	 * @param expression
	 *            - a String with the expression
	 * @param close
	 *            - the index of the closing parenthesis
	 * @return - the index of the appropriate opening parenthesis
	 */
	private static int findOpen(String expression, int close)
	{
		for (int i = close; i >= 0; i--)
		{
			if (expression.charAt(i) == '(')
			{
				return i;
			}
		}

		throw new InvalidParameterException("No opening parenthesis was in expression");
	}

	/**
	 * Given a string, break it up by spaces, so that something like "a + b"
	 * becomes a 3-element list: a, +, b
	 * 
	 * @param str
	 *            - the String to break up into tokens
	 * 
	 * @return - an ArrayList containing the parts of the input string
	 */
	private static ArrayList<String> convertToList(String str)
	{
		ArrayList<String> result = new ArrayList<String>();

		/* until you are left with just one more thing to "tokenize" */
		while (str.indexOf(' ') != -1)
		{
			int nextSpace = str.indexOf(' ');
			result.add(str.substring(0, nextSpace));
			str = str.substring(nextSpace + 1);
		}
		result.add(str);

		return result;
	}

	/**
	 * Given a series of tokens like a, +, b, *, and c, find the index of the
	 * operator that should be evaluated next. In the example here, the next
	 * operator is the '*' at index 3.
	 * 
	 * @param tokens
	 *            - an ArrayList of numbers and math operators that form a valid
	 *            math expression.
	 * 
	 * @return - the index in the ArrayList of the next operator that should be
	 *         evaluated.
	 */
	private static int findIndexOfNextOperator(ArrayList<String> tokens)
	{
		for (int i = 0; i < tokens.size(); i++)
		{
			/* multiplication and division first */
			if (tokens.get(i).equals("*") || tokens.get(i).equals("/"))
			{
				return i;
			}
		}

		for (int i = 0; i < tokens.size(); i++)
		{
			/* addition and subtraction second */
			if (tokens.get(i).equals("+") || tokens.get(i).equals("-"))
			{
				return i;
			}
		}

		throw new InvalidParameterException("No operator found in token list");
	}

	/**
	 * Given left and right operands and an operator for in between them,
	 * calculate the result of the expression and return it.
	 * 
	 * @param left
	 *            - the left operand
	 * @param op
	 *            - the operator (*, /, +, or -)
	 * @param right
	 *            - the right operand
	 * 
	 * @return - the result of evaluating the expression formed by left, op, and
	 *         right
	 */
	private static String calculate(String left, String op, String right)
	{
		double leftNum = Double.parseDouble(left);
		double rightNum = Double.parseDouble(right);
		double result = 0;

		if (op.equals("*"))
		{
			/* multiplication */
			result = leftNum * rightNum;
			return "" + result;
		}
		else if (op.equals("/"))
		{
			/* division */
			result = leftNum / rightNum;
			return "" + result;
		}
		else if (op.equals("+"))
		{
			/* addition */
			result = leftNum + rightNum;
			return "" + result;
		}
		else if (op.equals("-"))
		{
			/* subtraction */
			result = leftNum - rightNum;
			return "" + result;
		}

		throw new InvalidParameterException("'" + op + "' is not a valid operator");
	}

	/**
	 * Replace some items in an ArrayList with a replacement value
	 * 
	 * @param items
	 *            - the ArrayList of items to modify
	 * @param firstIndex
	 *            - the index in the ArrayList of the first item to remove
	 * @param howManyItems
	 *            - how many items to remove
	 * @param newValue
	 *            - the value to replace the removed item(s) with
	 */
	private static void replaceInList(ArrayList<String> items, int firstIndex, int howManyItems, String newValue)
	{
		for (int i = 0; i < howManyItems; i++)
		{
			items.remove(firstIndex);
		}
		items.add(firstIndex, newValue);
	}

}