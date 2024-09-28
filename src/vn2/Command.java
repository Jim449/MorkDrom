package vn2;

import java.util.ArrayList;


/**
 * Stores commands for later use.
 * You can use this class to retrieve command names
 * and parameters. The parameters are returned
 * as they are written, as strings.
 * 
 * @author alexi
 *
 */
public class Command {

	private ArrayList<String> contents;
	
	
	/**
	 * Creates a new, empty Command
	 */
	public Command()
	{
		contents = new ArrayList<String>();
	}
	
	
	/**
	 * Creates a new Command containing the given command
	 * 
	 * @param text
	 */
	public Command( String text )
	{
		contents = new ArrayList<String>();
		add(text);
	}
	
	
	/**
	 * Adds a command, dividing it into a command name
	 * followed by parameters
	 * 
	 * @param command
	 */
	public void add( String command )
	{
		String word = "";
		boolean inQuotes = false;
		
		command = command.substring(1, command.length() - 1);
		
		for (char letter: command.toCharArray())
		{
			if (letter == '\'')
			{
				inQuotes = !inQuotes;
				word += letter;
			}
			else if (letter == ',' && !inQuotes)
			{
				contents.add(word.trim());
				word = "";
			}
			else word += letter;
		}
		contents.add(word.trim());
	}
	
	
	/**
	 * Returns the first command name or parameter
	 * which hasn't yet been returned.
	 * Otherwise, return an empty string
	 * 
	 * @return
	 */
	public String getNext()
	{
		if (contents.size() > 0) return contents.remove(0);
		else return "";
	}
	
	
	/**
	 * Returns true if the text is a command
	 * 
	 * @param text
	 * @return
	 */
	public static boolean isCommand( String text )
	{
		return (text != null && text.startsWith("{") && text.endsWith("}"));
	}
	
	
	/**
	 * Returns the command name
	 * 
	 * @param command
	 * @return
	 */
	public static String getName( String command )
	{
		String cut = command.substring(1, command.length() - 1);
		String[] split = cut.split(",");
		return split[0];
	}
}
