/**********************************************************
 * Assignment: Text Excel Extra Credit
 *
 * Author: Sun-Jung Yum
 *
 * Description: This extra-credit version of the program will not crash with
 * bad input and instead show an error message. It also handles formulas using
 * the correct order of operations, and works with parentheses. Lastly, it has
 * a "help" function!
 *
 * Academic Integrity: I pledge that this program represents my own work. I
 * received help from no one in designing and debugging my program.
 **********************************************************/

package textExcelEC;

import java.util.Scanner;

/* 
 * TextExcel is the main entry point for a console-based spreadsheet
 * program. It supports commands to create, display, modify, and delete 
 * various cells from the spreadsheet. 
 */
public class TextExcel
{
	public static void main(String[] args)
	{
		/* create the spreadsheet and scanner objects we'll use throughout */
		Spreadsheet sheet = new Spreadsheet();
		Scanner console = new Scanner(System.in);

		System.out.println("Welcome to Text Excel!");

		String command = getCommand(console);
		while (!command.equals("quit"))
		{
			try
			{
				/* process this command */
				handleCommand(command, sheet);
			} catch (Exception ex)
			{
				/*
				 * if anything goes wrong anywhere in the handling of this
				 * command, the code there can throw an exception. we'll catch
				 * it here and display an error message.
				 */
				ex.printStackTrace();
			}

			command = getCommand(console);
		}

		System.out.println("Farewell!");
	}

	/* prompt the user for a command and return whatever she enters */
	private static String getCommand(Scanner s)
	{
		System.out.print("Enter a command: ");
		return s.nextLine();
	}

	/*
	 * parse the specified command and tell the spreadsheet object what to do
	 * with it. any failures should result in an exception being thrown.
	 */
	private static void handleCommand(String command, Spreadsheet sheet)
	{
		/* ignore empty input */

		if (command == null || command.isEmpty())
			return;

		if (command.equals("print"))
		{
			sheet.print();
			return;
		}

		/*
		 * for some commands, we need to know the first part of the line (like
		 * 'A1' in 'A1 = 3.14'), so pull that out into a firstPart variable.
		 */
		int space = command.indexOf(" ");
		String firstPart = (space == -1) ? command : command.substring(0, space);

		if (sheet.isCellReference(firstPart))
		{
			/* the whole command is just a cell reference, so display it. */
			if (firstPart.equals(command))
			{
				sheet.displayCell(command);
				return;
			}

			/*
			 * the command is a cell reference followed by ' = ' and something
			 * else. Pass the 'something else' to the cell factory to create a
			 * new cell, and tell the spreadsheet to put the new cell at the
			 * location in firstPart.
			 */
			if (command.substring(space).startsWith(" = "))
			{
				sheet.setCell(firstPart, CellFactory.create(command.substring(space + 3), sheet));
				return;
			}
		}

		/* identifies any of the two types of clear methods */
		if (firstPart.equals("clear"))
		{
			if (command.equals(firstPart))
			{
				/* clear the entire sheet */
				sheet.clear();
				return;
			}
			else
			{
				/* clear just one cell */
				String ref = command.substring(space + 1);
				if (sheet.isCellReference(ref))
				{
					sheet.clear(ref);
					return;
				}
			}
		}

		/* identifies any of the two types of clear methods */
		if (firstPart.equals("sorta") || firstPart.equals("sortd"))
		{
			String ref = command.substring(space + 1);
			sheet.sort(ref, firstPart.charAt(firstPart.length() - 1));
			return;
		}

		/* identifies help commands */
		if (firstPart.equals("help"))
		{
			if (command.equals(firstPart))
			{
				System.out.println("General Help: This program functions like a Text Excel ");
				System.out.println("spreadsheet. You may assign cells by using the format \"A1 = 27\". ");
				System.out.println("It will accept String cells, number cells, formula cells, and date ");
				System.out.println("cells. String cells should be surrounded by quotation marks, and ");
				System.out.println("formula cells should be surrounded by parentheses. The value of ");
				System.out.println("formula cells will be calculated using the correct order of operations, ");
				System.out.println("including the usage of parentheses. For help about printing, use the ");
				System.out.println("command \"help print,\" and for help about clearing, use the command ");
				System.out.println("\"help clear.\" For help about sorting, use the command \"help sort\".");
				System.out.println();
				return;
			}
			else
			{
				String specific = command.substring(space + 1);
				if (specific.equals("print"))
				{
					System.out.println("Print Help: To print the entire spreadsheet, enter the");
					System.out.println("command \"print.\" To print the contents of one cell, enter a");
					System.out.println("command like \"print A4,\" with the cell identifier after the");
					System.out.println("words \"print.\"");
					System.out.println();
					return;
				}
				else if (specific.equals("clear"))
				{
					System.out.println("Clear Help: To clear the entire spreadsheet, enter the");
					System.out.println("command \"clear.\" To clear one cell, enter a command like \"sort");
					System.out.println("A4,\" with the cell identifier after the word \"sort\"");
					System.out.println();
					return;
				}
				else if (specific.equals("sort"))
				{
					System.out.println("Sort Help: To sort a rectangular range of numeric cells, enter a");
					System.out.println("command like \"sort A1:B7.\" The cells must be either number cells");
					System.out.println("or formula cells.");
					System.out.println();
					return;
				}
			}
		}

		/* invalid input */
		throw new IllegalArgumentException(command + " is not recognized as a valid command.");
	}
}
