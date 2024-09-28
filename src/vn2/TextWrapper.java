package vn2;

import java.util.ArrayList;


/**
 * Stores text and prepares it for print,
 * by adding word wrap as needed.
 * 
 * @author alexi
 */
public class TextWrapper {

	private ArrayList<String> content;
	private ArrayList<Boolean> formatted;
	private int lines;
	
	
	/**
	 * Creates a new, empty TextWrapper
	 */
	public TextWrapper()
	{
		content = new ArrayList<String>(5);
		formatted = new ArrayList<Boolean>(5);
	}
	
	
	/**
	 * Adds text. Extra whitespace characters are ignored
	 * and linebreaks will be added as needed.
	 * 
	 * @param text
	 */
	public void addPlainText( String text )
	{
		content.add(text);
		formatted.add(false);
	}
	
	
	/**
	 * Adds text. The text will be added exactly as it is
	 * and linebreaks will not be added.
	 * 
	 * @param text
	 */
	public void addFormattedText( String text )
	{
		content.add(text);
		formatted.add(true);
	}
	
	
	/**
	 * Returns the amount of lines the previously returned text had.
	 * If 'getText' has not been called, the line amount is undefined.
	 * 
	 * @return
	 */
	public int getLines()
	{
		return lines;
	}
	
	
	/**
	 * Retrieves text. You must provide the text containers settings.
	 * Labels and Buttons uses htmlStyle but TextArea doesn't.
	 * Provide a maximum line length.
	 * After returning, the text is reset.
	 * 
	 * @param htmlStyle
	 * @param lineLength
	 * @return
	 */
	public String getText( boolean htmlStyle, int maxLineLength )
	{
		String[] split;
		String lastLine = "";
		String linebreak = (htmlStyle) ? "<br>" : "\n";
		String result = (htmlStyle) ? "<html><p>" : "";
		int lineLength = 0;
		lines = 0;
		
		for (int i = 0; i < content.size(); i++)
		{
			if (formatted.get(i))
			{
				split = content.get(i).split("\n", -1);
				
				boolean beginning = true;
				
				for (String word: split)
				{
					if (beginning && word.length() + lineLength < maxLineLength)
					{
						lastLine += word;
						lineLength += word.length();
						beginning = false;
					}
					else
					{
						result += lastLine + linebreak;
						lastLine = word;
						lineLength = word.length();
						lines += 1;
					}
				}
			}
			else
			{
				split = content.get(i).split("[\\s]+");
				
				for (String word: split)
				{	
					if (word.length() == 0) continue;
					
					else if (lineLength + word.length() > maxLineLength)
					{
						result += lastLine + linebreak;
						lastLine = "";
						lineLength = 0;
						lines += 1;
					}
					
					lastLine += word + " ";
					lineLength += word.length() + 1;
				}
			}
		}
		result += lastLine;
		lines += 1;
		
		if (htmlStyle) result += "</p></html>";
		
		content.clear();
		formatted.clear();
		
		return result;
	}
}
