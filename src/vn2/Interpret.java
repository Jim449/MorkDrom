package vn2;

import java.util.ArrayList;


/**
 * Interprets VN code
 * 
 * @author alexi
 */
public class Interpret {

	private Database database;
	private Controller controller;
	private Window window;
	
	private String folder;
	private Address address;
	private ArrayList<Address> addressList;
	
	private Command command;
	private ArrayList<Command> selection;
	
	private String inputVariable;
	private String image;
	
	private boolean dialNumber;
	private boolean skip;
	private boolean skipOnce;
	
	
	/**
	 * Creates the Interpret
	 * 
	 * @param controller
	 * @param window
	 * @param database
	 */
	public Interpret( Controller controller, Window window, Database database )
	{
		folder = "";
		addressList = new ArrayList<Address>();
		
		this.controller = controller;
		this.window = window;
		this.database = database;
	}
	
	
	/**
	 * Saves the game into a file.
	 * Only the lowest address can be saved.
	 * 
	 * @param	filename
	 */
	public void save( String filename )
	{
		Address savepoint = addressList.get(0);
		
		savepoint.save(database, filename, image);
	}
	
	
	/**
	 * Opens a new address on top of the current
	 * 
	 * @param	filename: the name of the file, excluding path and .txt
	 */
	public void open( String filename )
	{
		address = new Address(folder, filename);
		address.open();
		addressList.add(address);
		database.setValue(C.ADDRESS_DEPTH, addressList.size());
	}
	
	
	/**
	 * Opens a file
	 * Use when the player clicks a menu button
	 * 
	 * @param	filename
	 */
	public void openMenu( String name, String filename, String function )
	{
		for (Address openAddress: addressList)
		{
			if (openAddress.getCreator().equals(name)) return;
		}
		
		address.rewind(1);
		address = new Address(folder, filename, name);
		address.open();
		addressList.add(address);
		database.setValue(C.ADDRESS_DEPTH, addressList.size());
		
		if (function != null && function.length() > 0) address.goToFunction(function);
		
		window.setInputType(Window.NONE);
		window.choicePanel.clear();
		window.infoPanel.clearTooltip();
		proceed();
	}
	
	
	/**
	 * Erases all addresses and opens a new address at the lowest level
	 * 
	 * @param	filename
	 */
	public void chapter( String filename )
	{
		address = new Address(folder, filename);
		address.open();
		addressList.clear();
		addressList.add(address);
		database.setValue(C.ADDRESS_DEPTH, 1);
	}
	
	
	/**
	 * Creates an address without opening any file.
	 * 
	 * @param data
	 */
	public void createAddress( String data )
	{
		address = new Address("","");
		address.create(data);
		addressList.clear();
		addressList.add(address);
		database.setValue(C.ADDRESS_DEPTH, 1);
	}
	
	
	/**
	 * Creates an address on top of the current address.
	 * 
	 * @param data
	 */
	public void createOver( String data )
	{
		address = new Address("", "");
		address.create(data);
		addressList.add(address);
		database.setValue(C.ADDRESS_DEPTH, addressList.size());
	}
	
	
	/**
	 * Creates a menu button and links it to a function in a file
	 * 
	 * @param name: name on the menu button
	 * @param file: filename with vnCode
	 * @param function: a location in the file
	 */
	public void createMenu( String name, String file, String function )
	{
		if (window.menuPanel.hasMenu(name) == false)
			window.menuPanel.addMenu(name, file, function);
	}
	
	
	/**
	 * Creates a resource to display a Value
	 * 
	 * @param name
	 * @param variable
	 */
	public void createValueResource( String name, String variable )
	{
		window.resourcePanel.addResource(name, variable, true);
	}
	
	
	/**
	 * Creates a resource to display two Values
	 * 
	 * @param name
	 * @param var1
	 * @param separator
	 * @param var2
	 */
	public void createDualResource( String name, String var1, String separator, String var2 )
	{
		window.resourcePanel.addResource(name, var1, separator, var2);
	}
	
	
	/**
	 * Creates a resource to display a Name
	 * 
	 * @param name
	 * @param variable
	 */
	public void createNameResource( String name, String variable )
	{
		window.resourcePanel.addResource(name, variable, false);
	}
	
	
	
	/**
	 * Changes the background image
	 * 
	 * @param file
	 */
	public void setBackground( String file )
	{
		String filename = folder + "/" + file + ".png";
		image = file;
		window.imagePanel.loadBackground(filename);
	}
	
	
	/**
	 * Sets the current folder
	 * 
	 * @param file
	 */
	public void setGameFolder( String file )
	{
		folder = file;
	}
	
	
	/**
	 * Activates commands
	 * 
	 * @param command
	 */
	private void activate( Command command )
	{
		String element = command.getNext();
		
		int intArg1;
		int intArg2;
		String stringArg1;
		String stringArg2;
		String stringArg3;
		String stringArg4;
		
		boolean refreshResource = false;
		dialNumber = false;
		
		while (element.length() > 0)
		{
			switch (element)
			{
				/*
				 * Address commands
				 */
				// {go, paragraph} - go to paragraph
				case "go":
				{
					intArg1 = database.getValue(command.getNext());
					System.out.println("go " + intArg1);
					address.goToParagraph(intArg1);
					break;
				}
				// {find, section} - go to section
				case "find":
				{
					intArg1 = database.getValue(command.getNext());
					System.out.println("find " + intArg1);
					address.goToSection(intArg1);
					break;
				}
				// {act, function} - go to function
				case "act":
				{
					stringArg1 = database.getName(command.getNext());
					System.out.println("activate " + stringArg1);
					address.goToFunction(stringArg1);
					break;
				}
				// {range, number} - go to range containing number
				case "range":
				{
					intArg1 = database.getValue(command.getNext());
					System.out.println("range " + intArg1);
					address.goToRange(intArg1);
					break;
				}
				// {skip, paragraphs} - skips a number of paragraphs
				case "skip":
				{
					intArg1 = database.getValue(command.getNext());
					System.out.println("skip " + intArg1);
					address.skip(intArg1);
					break;
				}
				// {rewind, paragraphs} - rewinds a number of paragraphs
				case "rewind":
				{
					intArg1 = database.getValue(command.getNext());
					System.out.println("rewind " + intArg1);
					address.rewind(intArg1);
					break;
				}
				// {chap, chapter} - disposes of all addresses and opens a chapter
				case "chap":
				{
					stringArg1 = database.getName(command.getNext());
					System.out.println("chapter " + stringArg1);
					chapter(stringArg1);
					break;
				}
				// {open, chapter} - opens a chapter on top of current addresses
				case "open":
				{
					stringArg1 = database.getName(command.getNext());
					System.out.println("open " + stringArg1);
					open(stringArg1);
					break;
				}
				// {return} - disposes of the top address
				case "return":
				{
					if (addressList.size() > 1)
					{
						System.out.println("return");
						addressList.remove(addressList.size() - 1);
						address = addressList.get(addressList.size() - 1);
						database.setValue(C.ADDRESS_DEPTH, addressList.size());
					}
					break;
				}
				// {game, folder} - changes the game folder
				case "game":
				{
					stringArg1 = database.getName(command.getNext());
					System.out.println("game " + stringArg1);
					setGameFolder(stringArg1);
					break;
				}
				/*
				 * Variable commands
				 */
				// {set, variable, value} - sets the value of a variable
				case "set":
				{
					stringArg1 = command.getNext();
					intArg1 = database.getValue(command.getNext());
					System.out.println("set " + stringArg1 + " to " + intArg1);
					database.setValue(stringArg1, intArg1);
					refreshResource = true;
					break;
				}
				// {add, variable, value} - increases the value of a variable
				case "add":
				{
					stringArg1 = command.getNext();
					intArg1 = database.getValue(stringArg1);
					intArg2 = database.getValue(command.getNext());
					System.out.println("add " + stringArg1 + " by " + intArg2);
					database.setValue(stringArg1, intArg1 + intArg2);
					refreshResource = true;
					break;
				}
				// {sub, variable, value} - decreases the value of a variable
				case "sub":
				{
					stringArg1 = command.getNext();
					intArg1 = database.getValue(stringArg1);
					intArg2 = database.getValue(command.getNext());
					System.out.println("subtract " + stringArg1 + " by " + intArg2);
					database.setValue( stringArg1, intArg1 - intArg2);
					refreshResource = true;
					break;
				}
				// {rand, value} - increases a value by (min <= random < max)
				case "rand":
				{
					stringArg1 = command.getNext();
					intArg1 = database.getValue(stringArg1);
					intArg2 = database.getValue(command.getNext());
					System.out.println("random " + stringArg1 + " by " + intArg2);
					intArg2 = (int)(Math.random() * intArg2);
					database.setValue(stringArg1, intArg1 + intArg2);
					refreshResource = true;
					break;
				}
				// {name, name} - sets the name of a variable
				case "name":
				{
					stringArg1 = command.getNext();
					stringArg2 = database.getName( command.getNext() );
					System.out.println("name " + stringArg1 + " as " + stringArg2);
					database.setName(stringArg1, stringArg2);
					refreshResource = true;
					break;
				}
				// {attr, attribute} - sets the current attribute
				case "attr":
				{
					stringArg1 = command.getNext();
					System.out.println("set current attribute " + stringArg1);
					database.setCurrent(stringArg1);
					break;
				}
				// {remove, attribute} - removes an attribute
				case "remove":
				{
					stringArg1 = command.getNext();
					System.out.println("remove attribute " + stringArg1);
					database.remove(stringArg1);
					break;
				}
				// {clear} - clears variables, resources and menus
				case "clear":
				{
					System.out.println("clear");
					database.clear();
					window.resourcePanel.clear();
					window.menuPanel.clear();
					break;
				}
				// {newVal, value list} - clears a value list
				case "newVal":
				{
					stringArg1 = command.getNext();
					System.out.println("new value list " + stringArg1);
					database.clearValueList(stringArg1);
					break;
				}
				// {addVal, value list, value} - adds a value to a value list
				case "addVal":
				{
					stringArg1 = command.getNext();
					intArg1 = database.getValue(command.getNext());
					System.out.println("add value " + intArg1 + " to " + stringArg1);
					database.addValue(stringArg1, intArg1);
					break;
				}
				// {newName, name list} - clears a name list
				case "newName":
				{
					stringArg1 = command.getNext();
					System.out.println("new name list " + stringArg1);
					database.clearNameList(stringArg1);
					break;
				}
				// {addName, name list, name} - adds a name to a name list
				case "addName":
				{
					stringArg1 = command.getNext();
					stringArg2 = database.getName(command.getNext());
					System.out.println("add name " + stringArg2 + " to " + stringArg1);
					database.addName(stringArg1, stringArg2);
					break;
				}
				/*
				 * Graphic interface commands
				 */
				// {menu, name, file, function} - creates a menu
				// sets display name, file to be opened and function to activate (optional)
				case "menu":
				{
					stringArg1 = database.getName(command.getNext());
					stringArg2 = database.getName(command.getNext());
					stringArg3 = database.getName(command.getNext());
					System.out.println("menu " + stringArg1 + " using file "
					+ stringArg2 + " at " + stringArg3);
					createMenu(stringArg1, stringArg2, stringArg3);
					break;
				}
				// {resVal, display name, variable} - displays a value in the resource bar
				case "resVal":
				{
					stringArg1 = database.getName(command.getNext());
					stringArg2 = command.getNext();
					System.out.println("create value resource "
							+ stringArg1 + " using value " + stringArg2);
					createValueResource(stringArg1, stringArg2);
					break;
				}
				// {resDual, display name, variable1, separator, variable2}
				// - displays two values in a single resource bar item
				case "resDual":
				{
					stringArg1 = database.getName(command.getNext());
					stringArg2 = command.getNext();
					stringArg3 = database.getName(command.getNext());
					stringArg4 = command.getNext();
					System.out.println("create dual resource "
							+ stringArg1 + " using values " + stringArg2
							+ " and " + stringArg4 + " with separator " + stringArg3);
					createDualResource(stringArg1, stringArg2, stringArg3, stringArg4);
					break;
				}
				// {resName, display name, variable} - displays a name in the resource bar
				case "resName":
				{
					stringArg1 = database.getName(command.getNext());
					stringArg2 = command.getNext();
					System.out.println("create name resource "
					+ stringArg1 + " using value " + stringArg2);
					createNameResource(stringArg1, stringArg2);
					break;
				}
				// {image, file} - removes all images and sets a new background image
				case "image":
				{
					stringArg1 = database.getName(command.getNext());
					setBackground(stringArg1);
					break;
				}
				/*
				 * Game commands
				 */
				// {save, file} - saves the game by generating a VN code file
				// reading this file will load all variables and set the address
				// only the lowest level address is saved
				case "save":
				{
					stringArg1 = database.getName(command.getNext());
					System.out.println("save " + stringArg1);
					save(stringArg1);
					break;
				}
				// {die} - freezes the top address of the game
				// it is still possible to click a menu to open a non-frozen address
				// the frozen address can be disposed of by the chap command
				case "die":
				{
					System.out.println("die");
					address.freeze();
					break;
				}
				// {call} - informs the controller that advanced operations
				// may have to be performed
				// it's up to the controller to retrieve necessary data from the database
				// use call in advanced games which has extra java code
				case "call":
				{
					System.out.println("call");
					dialNumber = true;
					break;
				}
				// {auto} - automatically proceeds to next paragraph
				// without writing messages, until next player choice
				case "auto":
				{
					System.out.println("auto");
					skip = true;
					break;
				}
			}
			element = command.getNext();
		}
		if (refreshResource) window.resourcePanel.refresh();
	}
	
	
	/**
	 * Returns true if the element marks the start of a paragraph
	 * 
	 * @param element
	 * @return
	 */
	private boolean isParagraph( String element )
	{
		return (element.equals("{p}") || element.equals("{s}")
				|| element.startsWith("{f,") || element.startsWith("{r,"));
	}
	
	
	/**
	 * Returns true if the element is a conditional
	 * 
	 * @param element
	 * @return
	 */
	private boolean isTest( String element )
	{
		return (element.equals("has") || element.equals("lack")
				|| element.equals("equal") || element.equals("unequal")
				|| element.equals("same") || element.equals("diff") 
				|| element.equals("hasValue") || element.equals("lackValue")
				|| element.equals("hasName") || element.equals("lackName")
				|| element.equals("inValue") || element.equals("inName"));
	}
	
	
	/**
	 * Given a command, {test, variable1, variable2},
	 * return the boolean result.
	 * 
	 * @param	command
	 * @return
	 */
	private boolean performTest( String command )
	{
		String[] split;
		
		command = command.substring(1, command.length() - 1);
		split = command.split(",[ ]*");
		
		return database.testCondition(split[0], split[1], split[2]);
	}
	
	
	/**
	 * Given a command 'show' or 'write', writes formatted text into the wrapper
	 * 
	 * @param element
	 * @param wrapper
	 */
	private void write( String element, TextWrapper wrapper )
	{
		String function;
		int value;
		String name;
		String end;
		Command command = new Command(element);
		
		function = command.getNext();
		
		if (function.equals("show"))
		{
			value = database.getValue(command.getNext());
			end = database.getName(command.getNext());
			wrapper.addFormattedText(String.valueOf(value) + end);
		}
		else
		{
			name = database.getName(command.getNext());
			end = database.getName(command.getNext());
			wrapper.addFormattedText(name + end);
		}
	}
	
	
	/**
	 * Increases the value of a variable
	 * 
	 * @param element
	 */
	private void count( String element )
	{
		Command command = new Command(element);
		String variable;
		int value;
		int addition;
		
		command.getNext();
		variable = command.getNext();
		value = database.getValue(variable);
		addition = database.getValue(command.getNext());
		
		database.setValue(variable, value + addition);
	}
	
	
	/**
	 * Returns the variable which should store a player input
	 * 
	 * @param command
	 * @return
	 */
	private String getInputVariable( String command )
	{
		String[] split;
		
		command = command.substring(1, command.length() - 1);
		split = command.split(",");
		
		return split[1];
	}
	
	
	/**
	 * Reads the paragraph. Activates flow control
	 * and stores commands.
	 */
	private void readParagraph()
	{
		String element;
		String function;
		TextWrapper wrapper = new TextWrapper();
		
		boolean testFor = true; // Test for truth or falsity
		boolean testProgress = true; // True if no conditional has failed
		boolean testResult = true; // Final test result
		boolean testEnd = false; // If true, stop skipping commands, starting from the next flow command
		boolean allowElif = false; // If false, elif and else are ignored
		
		// Where to add text
		boolean info = false; // Add to info window as info
		boolean tooltip = false; // Add to info window as tooltip
		boolean choice = false; // Add to choice window as option
		boolean input = false; // flag that the player should input a string
		
		int options = 0;
		
		command = new Command();
		selection = new ArrayList<Command>();
		inputVariable = null;
		
		
		do
		{
			element = address.nextElement();
			
			if (Command.isCommand(element)) function = Command.getName(element);
			else function = "";
			
			// Paragraph. Ask for player input
			if (element == null || isParagraph(element))
			{
				address.nextParagraph();
				// Increase the paragraph
				// When I open the menu I have to rewind 1 paragraph.
				
				if (tooltip) window.infoPanel.addTooltip(wrapper);
				else if (choice)
				{
					window.choicePanel.addOption(wrapper);
					skip = false;
				}
				else if (info) window.infoPanel.addText(wrapper);
				else if (!skip) window.messagePanel.writeMessage(wrapper);
				
				if (input) 
					window.setInputType(Window.INPUT);
				else if (choice) 
					window.setInputType(Window.CHOICE);
				else if (window.messagePanel.isAwaitingInput())
					window.setInputType(Window.ACCEPT);
				else 
					skipOnce = true;
				
				return;
			}
			// Creates an option which can be selected
			else if (function.equals("ans"))
			{
				if (testResult || testEnd)
				{
					testResult = true;
					
					if (info) window.infoPanel.addText(wrapper);
					else if (tooltip) window.infoPanel.addTooltip(wrapper);
					else if (choice) window.choicePanel.addOption(wrapper);
					else window.messagePanel.writeMessage(wrapper);
					// or set a prompt in the choicePanel
					// but I'm going to try with a normal message
					
					choice = true;
					info = false;
					tooltip = false;
					selection.add(new Command());
					options += 1;
				}
				else testEnd = true;
			}
			// Creates a conditional
			else if (function.equals("if"))
			{
				if (testResult || testEnd)
				{
					testFor = true;
					testProgress = true;
					testResult = true;
					testEnd = false;
					allowElif = true;
				}
			}
			// Creates a reversed conditional
			else if (function.equals("not"))
			{
				if (testResult || testEnd)
				{
					testFor = false;
					testProgress = true;
					testResult = false;
					testEnd = false;
					allowElif = true;
				}
			}
			// Creates a new conditional if the first one failed
			else if (function.equals("elif"))
			{
				if (testResult || allowElif == false)
				{
					testFor = true;
					testProgress = false;
					testResult = false;
					testEnd = false;
					allowElif = false;
				}
				else if (testResult == false && allowElif)
				{
					testFor = true;
					testProgress = true;
					testResult = true;
					testEnd = false;
					allowElif = true;
				}
			}
			// Text should be added to the info window
			else if (function.equals("info"))
			{
				testEnd = true;
				
				if (testResult)
				{
					if (choice)
					{
						window.choicePanel.addOption(wrapper);
						tooltip = true;
					}
					else
					{
						window.messagePanel.writeMessage(wrapper);
						info = true;
					}
				}
			}
			// Tests a condition
			else if (isTest(function))
			{
				if (testProgress)
				{
					testProgress = performTest(element);
					testResult = (testProgress == testFor);
				}
			}
			// Displays an integer or a string
			else if (function.equals("show") || function.equals("write"))
			{
				testEnd = true;
				
				if (testResult) write(element, wrapper);
			}
			// Asks the user to write a name
			else if (function.equals("input"))
			{
				testEnd = true;
				
				if (testResult)
				{
					inputVariable = getInputVariable(element);
					input = true;
					window.choicePanel.addInput();
				}
			}
			// Immediately increases a variable, regardless of conditionals
			else if (function.equals("count"))
			{
				count(element);
			}
			// Stores a command
			else if (Command.isCommand(element))
			{
				testEnd = true;
				
				if (testResult)
				{
					if (options == 0) command.add(element);
					else selection.get(options-1).add(element);
				}
			}
			// In this case it must be a plain text
			else
			{
				testEnd = true;
				
				if (testResult) wrapper.addPlainText(element);
			}
		}
		while (true);
	}
	
	
	/**
	 * Continuously reads paragraph as long as they don't
	 * contain visible text or text skip has been activated
	 */
	private void fastForward()
	{
		while (skip || skipOnce)
		{
			skipOnce = false;
			activate(command);
			
			if (dialNumber)
			{
				controller.prepareCall();
				return;
			}
			
			readParagraph();
		}
	}
	
	
	/**
	 * Activates commands and continues to the next paragraph
	 */
	public void sendAccept()
	{
		activate(command);
		if (dialNumber) controller.prepareCall();
		else
		{
			readParagraph();
			fastForward();
		}
	}
	
	
	/**
	 * Activates commands associated with the given choice
	 * and continues to the next paragraph
	 */
	public void sendChoice( int choice )
	{
		database.setValue(C.CHOICE, choice);
		activate(command);
		activate(selection.get(choice));
		if (dialNumber) controller.prepareCall();
		else
		{
			readParagraph();
			fastForward();
		}
	}
	
	
	/**
	 * Stores a name typed by the player into a variable,
	 * activates commands associated with the given choice
	 * and continues to the next paragraph
	 * 
	 * @param writing
	 */
	public void sendInputChoice( int choice, String writing )
	{
		database.setName(inputVariable, writing);
		database.setValue(C.CHOICE, choice);
		activate(command);
		activate(selection.get(choice));
		if (dialNumber) controller.prepareCall();
		else
		{
			readParagraph();
			fastForward();
		}
	}
	
	
	/**
	 * Reads the next paragraph
	 */
	public void proceed()
	{
		readParagraph();
		fastForward();
	}
}
