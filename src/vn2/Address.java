package vn2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Address {

	private String name; // The file name
	private String folder; // The game folder
	private String creator; // The name of the menu which opened this address (or null)
	
	private boolean alive; // If false, freeze the game.
	
	private int element; // Current element index.
	private int paragraph; // Current paragraph index.
	
	private ArrayList<String> text; // The text, separated into elements
	private ArrayList<Integer> paragraphList; // The locations of paragraphs
	private ArrayList<Integer> sectionList; // The locations of Section paragraphs
	private ArrayList<Integer> rangeList; // The locations of Range paragraphs
	private ArrayList<Integer> rangeRoof; // The upper limits of Range paragraphs
	private HashMap<String, Integer> functionList; // The locations of Function paragraphs
	
	
	/**
	 * Resets the address
	 */
	public void reset()
	{
		element = 0;
		paragraph = -1;
		alive = true;
	}
	
	
	/**
	 * Freezes the address
	 */
	public void freeze()
	{
		alive = false;
	}
	
	
	/**
	 * Returns the file name
	 * @return
	 */
	public String getFileName()
	{
		return name;
	}
	
	
	/**
	 * Returns the name of the menu which opened this address.
	 * Returns an empty string if this address was opened through other means.
	 * 
	 * @return
	 */
	public String getCreator()
	{
		return creator;
	}
	
	
	/**
	 * Creates a new address
	 * 
	 * @param folder
	 * @param name
	 */
	public Address( String folder, String name )
	{
		this.folder = folder;
		this.name = name;
		this.creator = "";
		
		text = new ArrayList<String>();
		paragraphList = new ArrayList<Integer>();
		sectionList = new ArrayList<Integer>();
		rangeList = new ArrayList<Integer>();
		rangeRoof = new ArrayList<Integer>();
		functionList = new HashMap<String, Integer>();
		
		reset();
	}
	
	
	/**
	 * Creates a new address opened by a menu
	 * 
	 * @param folder
	 * @param name
	 * @param creator
	 */
	public Address( String folder, String name, String creator )
	{
		this.folder = folder;
		this.name = name;
		this.creator = creator;
		
		text = new ArrayList<String>();
		paragraphList = new ArrayList<Integer>();
		sectionList = new ArrayList<Integer>();
		rangeList = new ArrayList<Integer>();
		rangeRoof = new ArrayList<Integer>();
		functionList = new HashMap<String, Integer>();
		
		reset();
	}
	
	
	/**
	 * Adds plain text to the text.
	 * The sentence should not be a command.
	 * If the sentence contains no visible symbols, it is not added
	 * 
	 * @param sentence
	 */
	private void addText( String sentence )
	{
		if (sentence.isBlank() == false)
		{
			text.add(sentence);
		}
	}
	
	
	/**
	 * Adds a command to the text.
	 * If the command marks the start of a paragraph, store its position
	 * in the appropriate lists.
	 * 
	 * Paragraphs (p), sections (s), functions (f) and ranges (r)
	 * are all treated as paragraphs.
	 * 
	 * @param sentence
	 */
	private void addCommand( String sentence )
	{
		String key;
		int roof;
		
		if (sentence.equals("{p}")) // Remember the start of the paragraph
		{
			paragraphList.add(text.size()); // Store the element index
		}
		
		else if (sentence.equals("{s}")) // Remember the start of the section
		{
			sectionList.add(paragraphList.size()); // Store the paragraph index
			paragraphList.add(text.size()); // and the element index is stored in the paragraphList
		}
		
		else if (sentence.startsWith("{f,")) // Remember the start of the function
		{
			key = sentence.substring(3, sentence.length() - 1);
			
			functionList.put(key, paragraphList.size());
			paragraphList.add(text.size());
		}
		
		else if (sentence.startsWith("{r,")) // Remember the start of the range
		{
			key = sentence.substring(3, sentence.length() - 1);
			roof = Integer.valueOf(key);
			
			rangeList.add(paragraphList.size());
			rangeRoof.add(roof);
			paragraphList.add(text.size());
		}
		
		text.add(sentence);
	}
	
	
	/**
	 * Separates the text files contents into elements
	 * which are stored into the text variable
	 */
	private void structure( BufferedReader data )
	{
		int code;
		char symbol;
		String sentence = "";

		try
		{
			// The first character in the UTF-8 file is usually an unknown:
			// BOM - Byte Order Mark. Char FEFF. In UTF-8: char EF BB BF
			// I do a check with the ten-based value 65279
			
			code = data.read();
			symbol = (char)code;
			
			if (code == 65279)
			{
				code = data.read();
				symbol = (char)code;
			}
			
			do
			{
				if (symbol == '{' || symbol == '<') // A command starts. Add the sentence, excluding this symbol
				{
					addText( sentence );
					sentence = "";
				}	
			
				sentence += symbol;
			
				if (symbol == '}') // A command ends. Add the sentence, including this symbol
				{
					addCommand( sentence );
					sentence = "";
				}
			
				else if (symbol == '>') // A comment ends. Ignore it
				{
					sentence = "";
				}
				
				code = data.read();
				symbol = (char)code;
			}
			while (code >= 0);
		}
		
		catch (IOException error)
		{
			System.out.println( "This structure isn't complete, though?" );
		}
	}
	
	
	private void loadMeta( BufferedReader data, Attribute attribute )
	{
		int code;
		char symbol;
		String sentence = "";
		ArrayList<String> content = new ArrayList<String>();
		
		// Check for BOM
		try
		{
			code = data.read();
			symbol = (char)code;
		
			if (code == 65279)
			{
				code = data.read();
				symbol = (char)code;
			}
			
			do
			{
				if (symbol == '{') // A command starts. Add the sentence, excluding this symbol
				{
					sentence = "";
				}
				else if (symbol == '}')
				{
					content.add(sentence);
					sentence = "";
				}
				else if (symbol == ',')
				{
					content.add(sentence);
					sentence = "";
				}
				else
					sentence += symbol;
				
				code = data.read();
				symbol = (char)code;
			}
			while (code >= 0);
			
			for (int i = 0; i < content.size(); i += 3)
			{
				if (content.get(i).equals("meta"))
					attribute.addListName(content.get(i + 1), content.get(i + 2));
			}
		}
		catch (IOException error)
		{
			System.out.println("Failed to load meta!");
		}
	}
	
	
	/**
	 * Creates an address which doesn't load an actual file.
	 * The contents is added as a parameter.
	 * 
	 * @param data
	 */
	public void create( String data )
	{
		char symbol;
		String sentence = "";
		
		for (int i = 0; i < data.length(); i++)
		{
			symbol = data.charAt(i);
			
			if (symbol == '{' || symbol == '<') // A command starts. Add the sentence, excluding this symbol
			{
				addText( sentence );
				sentence = "";
			}	
		
			sentence += symbol;
		
			if (symbol == '}') // A command ends. Add the sentence, including this symbol
			{
				addCommand( sentence );
				sentence = "";
			}
		
			else if (symbol == '>') // A comment ends. Ignore it
			{
				sentence = "";
			}
		}
	}
	
	
	/**
	 * Opens a chapter or resource
	 */
	public void open()
	{
		FileInputStream file;
		InputStreamReader reader;
		BufferedReader buffReader;
		
		try
		{
			if (folder.length() == 0) file = new FileInputStream(name + ".txt");
			else file = new FileInputStream(folder + "/" + name + ".txt");
			
			System.out.println("Reading file...");
			
			reader = new InputStreamReader(file, "UTF-8"); 
			buffReader = new BufferedReader(reader);
			
			structure(buffReader);
			
			System.out.println("Done!");
			buffReader.close();
		}
		
		catch (FileNotFoundException error)
		{
			System.out.println("Now give me some of that... file! Give me that file!!");
		}
		
		catch (IOException error)
		{
			System.out.println("Sweden... We have an IOError!");
		}
	}
	
	
	/**
	 * Reads a save meta file and stores the result into the attribute.
	 * If there is no file, default values are stored
	 * 
	 * @param attribute
	 */
	public void openMeta( Attribute attribute )
	{
		FileInputStream file;
		InputStreamReader reader;
		BufferedReader buffReader;
		
		try
		{
			if (folder.length() == 0) file = new FileInputStream(name + ".txt");
			else file = new FileInputStream(folder + "/" + name + ".txt");
			
			reader = new InputStreamReader(file, "UTF-8"); 
			buffReader = new BufferedReader(reader);
			
			loadMeta(buffReader, attribute);
			
			buffReader.close();
		}
		catch (FileNotFoundException error)
		{
			attribute.addListName("Namn", "");
			attribute.addListName("Plats", "");
			attribute.addListName("Grad", "");
			attribute.addListName("Kapitel", "");
		}
		catch (IOException error)
		{
			System.out.println("Meta IOError!");
		}
	}
	
	
	/**
	 * Returns a text which can be written into a save file.
	 * To load the save file, read it with 'open'.
	 * 
	 * @param database
	 * @return
	 */
	private String getSaveData( Database database, String image )
	{
		String write = "{p}\r\n";
		Attribute attr;
		String value;
		
		write += "{clear}\r\n{p}\r\n";
	
		for (String key: database.attribute.keySet())
		{
			attr = database.getAttribute(key);
		
			write += "{attr," + key + "}\r\n{p}\r\n";
		
			for (Map.Entry<String, Integer> map: attr.intVar.entrySet())
			{
				value = String.valueOf(map.getValue());
				write += "{set," + map.getKey() + "," + value + "}";
			}
			write += "\r\n{p}\r\n";
		
			for (Map.Entry<String, String> map: attr.stringVar.entrySet())
			{
				write += "{name," + map.getKey() + ",'" + map.getValue() + "'}";
			}
			write += "\r\n{p}\r\n";
			
			for (Map.Entry<String, ArrayList<Integer>> map: attr.intList.entrySet())
			{
				for (int item: map.getValue())
				{
					write += "{addVal," + map.getKey() + "," + item + "}";
				}
			}
			write += "\r\n{p}\r\n";
			
			for (Map.Entry<String, ArrayList<String>> map: attr.stringList.entrySet())
			{
				for (String item: map.getValue())
				{
					write += "{addName," + map.getKey() + ",'" + item + "'}";
				}
			}
		}
		write += "\r\n{p}\r\n";
		write += "{attr," + C.MAIN_ATTR + "}";
		write += "{name,Chapter,'" + name + "'}";
		write += "{set,Paragraph," + paragraph + "}";
		write += "{image,'" + image + "'}";
		write += "{info}";
		write += "{return}";
		write += "\r\n{p}";
	
		return write;
	}
	
	
	/**
	 * Returns a text which can be written into a meta save file.
	 * To read the the file, use 'openMeta'.
	 * 
	 * @param meta
	 * @return
	 */
	private String getSaveMeta( Attribute meta )
	{
		String write = "";
		String value;
		
		for (Map.Entry<String, String> map: meta.stringVar.entrySet())
		{
			write += "{meta," + map.getKey() + "," + map.getValue() + "}";
		}
		for (Map.Entry<String, Integer> map: meta.intVar.entrySet())
		{
			value = String.valueOf(map.getValue());
			write += "{meta," + map.getKey() + "," + value + "}";
		}
		return write;
	}
	
	
	/**
	 * Saves all variables in the database into this addresses file
	 * 
	 * @param database
	 */
	public void save( Database database, String filename, String image )
	{
		String saveData = getSaveData(database, image);
		FileOutputStream file;
		OutputStreamWriter writer;
		
		try
		{
			if (folder.length() == 0) file = new FileOutputStream(filename + ".txt");
			else file = new FileOutputStream(folder + "/" + filename + ".txt");
			
			writer = new OutputStreamWriter(file, "UTF-8");
			BufferedWriter buffWriter = new BufferedWriter(writer);
			
			buffWriter.write(saveData);
			buffWriter.close();
		}
		
		catch (IOException error)
		{
			System.out.println("Sweden... We have an IOError!");
		}
	}
	
	
	/**
	 * Saves information about the saved game
	 * 
	 * @param meta
	 * @param filename
	 */
	public void saveMeta( Attribute meta, String filename )
	{
		String saveData = getSaveMeta(meta);
		FileOutputStream file;
		OutputStreamWriter writer;
		
		try
		{
			if (folder.length() == 0) file = new FileOutputStream(filename + ".txt");
			else file = new FileOutputStream(folder + "/" + filename + ".txt");
			
			writer = new OutputStreamWriter(file, "UTF-8");
			BufferedWriter buffWriter = new BufferedWriter(writer);
			
			buffWriter.write(saveData);
			buffWriter.close();
		}
		
		catch (IOException error)
		{
			System.out.println("Sweden... We have an IOError!");
		}
	}
	
	
	/**
	 * Returns the current element
	 * and proceeds to the next
	 * 
	 * @return
	 */
	public String nextElement()
	{
		if (alive && element < text.size())
		{
			String command = text.get(element);
			element += 1;
			return command;
		}
		else
		{
			rewind(1);
			return null;
		}
	}
	
	
	
	/**
	 * Increases the paragraph count by one
	 */
	public void nextParagraph()
	{
		paragraph += 1;
	}
	
	
	/**
	 * Returns the current paragraph
	 * @return
	 */
	public int getParagraph()
	{
		return paragraph;
	}
	
	
	/**
	 * Changes the address to the given paragraph
	 * 
	 * @param value
	 */
	public void goToParagraph( int value )
	{
		paragraph = value;
		element = paragraphList.get(paragraph) + 1;
	}
	
	
	/**
	 * Changes the address to the given section
	 * 
	 * @param value
	 */
	public void goToSection( int value )
	{
		paragraph = sectionList.get(value);
		element = paragraphList.get(paragraph) + 1;
	}
	
	
	/**
	 * Changes the address to the range containing the given number
	 * 
	 * @param value
	 */
	public void goToRange( int value )
	{
		int roof;
		
		for (int i = 0; i < rangeList.size(); i++ )
		{
			roof = rangeRoof.get(i);
		
			if (value < roof)
			{
				paragraph = rangeList.get(i);
				element = paragraphList.get(paragraph) + 1;
				return;
			}
			
			value -= roof;
		}
	}
	
	
	/**
	 * Changes the address to the function with the given name
	 * @param value
	 */
	public void goToFunction( String key)
	{
		if (functionList.containsKey( key ))
		{
			paragraph = functionList.get(key);
			element = paragraphList.get(paragraph) + 1;
		}
	}
	
	
	/**
	 * Skips a number of paragraphs
	 * 
	 * @param steps: the number of paragraphs to skip
	 */
	public void skip( int steps )
	{
		paragraph += steps;
		element = paragraphList.get(paragraph) + 1;
	}
	
	
	/**
	 * Rewinds a number of paragraphs
	 * 
	 * @param steps: the number of paragraphs to rewind where
	 * step = 1 means remaining at the previous paragraph
	 */
	public void rewind( int steps )
	{
		paragraph -= steps;
		element = paragraphList.get(paragraph) + 1;
	}
}
