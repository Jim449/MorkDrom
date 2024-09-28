package vn2;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * <pre>
 * Stores Attributes. An Attribute is a container for variables.
 * There are two kinds of variables: integer (Values) and string (Names).
 * You can use this class to access those variables.
 * If you try to access a nonexistent variable, it will be created,
 * with the default value of 0 or "".
 * 
 * Even if you don't want to access a variable,
 * you can use this class to convert a String to a valid Name or Value.
 * 
 * Syntax:
 * +1, 2, -3 etc. - non-variable integers.
 * 'Name' - the non-variable string Name.
 * Name - variable string or integer.
 * 
 * Variable - access a Variable from the current Attribute.
 * Attribute.Variable - access a Variable from any Attribute.
 * 
 * Attribute1.Var1.Var2 - finds Attribute2 which is named as Var1 in Attribute1.
 * From Attribute2, access the variable Var2
 * 
 * [blank]. - points to the current Attribute.
 * 
 * </pre>
 * @author alexi
 *
 */
public class Database {

	public HashMap<String, Attribute> attribute;	
	
	private Attribute current;
	
	
	/**
	 * Creates the database and sets Main as the current attribute
	 */
	public Database()
	{
		attribute = new HashMap<String, Attribute>();
		current = new Attribute();
		attribute.put(C.MAIN_ATTR, current);
	}
	
	
	/**
	 * Retrieves an attribute or creates a new one
	 * 
	 * @param key
	 * @return
	 */
	public Attribute getAttribute( String key )
	{
		if (attribute.containsKey(key))
			return attribute.get( key );
		else
		{
			attribute.put(key, new Attribute());
			return attribute.get(key);
		}
	}
	
	
	/**
	 * Adds an attribute to the database
	 * 
	 * @param key
	 * @param attr
	 */
	public void addAttribute( String key, Attribute attr )
	{
		attribute.put(key, attr);
	}
	
	
	/**
	 * Finds an attribute, using one or more strings
	 * to describe where to search.
	 * 
	 * @param key
	 * @return
	 */
	private Attribute findAttribute( String[] key )
	{
		Attribute path;
		String name;
		
		if (key.length == 3)
		{
			if (key[0].length() == 0) path = current;
			else path = getAttribute(key[0]);
			
			name = path.getName(key[1]);
			return getAttribute(name);
		}
		else if (key.length == 2)
		{
			if (key[0].length() == 0) return current;
			else return getAttribute(key[0]);
		}
		else return current;
	}
	
	
	/**
	 * Returns the variable name from a split string array
	 * 
	 * @param key
	 * @return
	 */
	private String findVariable( String[] key )
	{
		String last = key[key.length - 1];
		
		if (last.indexOf('[') == -1) return last;
		else return last.substring(0, last.indexOf('['));
	}
	
	
	/**
	 * Returns the index from a split string array
	 * 
	 * @param key
	 * @return
	 */
	private int findIndex( String[] key )
	{
		String last = key[key.length - 1];
		String index;
		
		if (last.indexOf('[') == -1) return -1;
		else
		{
			index = last.substring(last.indexOf('[') + 1, last.indexOf(']'));
			return getValue(index);
		}
	}
	
	
	/**
	 * Sets the current attribute.
	 * 
	 * If the key is of the form "Attribute.Name",
	 * set Current = Value of Name in Attribute.
	 * 
	 * Otherwise, enter the name of the attribute,
	 * with or without single quotation marks
	 * 
	 * @param key
	 */
	public void setCurrent( String key )
	{
		String[] split = key.split("\\.");
		Attribute attr;
		String var;
		
		if (key.startsWith("'") && key.endsWith("'"))
			current = getAttribute(key.substring(1, key.length() - 1));
		
		else if (split.length == 1) current = getAttribute(split[0]);
		
		else if (split.length == 2)
		{
			attr = getAttribute(split[0]);
			var = attr.getName(split[1]);
			current = getAttribute(var);
		}
	}
	
	
	/**
	 * Turns an attribute into a copy of another
	 * 
	 * @param to
	 * @param from
	 */
	public void copy( String to, String from )
	{
		Attribute source = getAttribute(from);
		
		addAttribute(to, source.copy());
	}
	
	
	/**
	 * Links an attribute to another key
	 * 
	 * @param to
	 * @param from
	 */
	public void setAttribute( String to, String from )
	{
		attribute.put(to, getAttribute(from));
	}
	
	
	/**
	 * Removes an attribute
	 * 
	 * @param key
	 */
	public void remove( String key )
	{
		attribute.remove(key);
	}
	
	
	/**
	 * Clears the database
	 */
	public void clear()
	{
		attribute.clear();
		current = new Attribute();
		attribute.put(C.MAIN_ATTR, current);
	}
	
	
	/**
	 * Interprets a key as a Value, by converting directly
	 * or accessing a variable in the database
	 * 
	 * @param key
	 * @return
	 */
	public int getValue( String key )
	{
		try
		{
			return Integer.parseInt(key);
		}
		catch (NumberFormatException e)
		{
			String[] split = key.split("\\.");
			Attribute attr = findAttribute(split);
			String var = findVariable(split);
			int index = findIndex(split);
			
			if (index >= 0) return attr.getValueList(var).get(index);
			else return attr.getValue(var);
		}
	}
	
	
	/**
	 * Interprets a key as a Name, by removing quotes
	 * or accessing a variable in the database
	 * 
	 * @param key
	 * @return
	 */
	public String getName( String key )
	{
		if (key.startsWith("'") && key.endsWith("'"))
			return key.substring(1, key.length() - 1);
		
		else
		{
			String[] split = key.split("\\.");
			Attribute attr = findAttribute(split);
			String var = findVariable(split);
			int index = findIndex(split);
			
			if (index >= 0) return attr.getNameList(var).get(index);
			else return attr.getName(var);
		}
	}
	
	
	/**
	 * Returns a list of Values
	 * 
	 * @param key
	 * @return
	 */
	public ArrayList<Integer> getValueList( String key )
	{
		String[] split = key.split("\\.");
		Attribute attr = findAttribute(split);
		String var = findVariable(split);
		
		return attr.getValueList(var);
	}
	
	
	/**
	 * Returns a list of Names
	 * 
	 * @param key
	 * @return
	 */
	public ArrayList<String> getNameList( String key )
	{
		String[] split = key.split("\\.");
		Attribute attr = findAttribute(split);
		String var = findVariable(split);
		
		return attr.getNameList(var);
	}
	
	
	/**
	 * Stores a value
	 * 
	 * @param key
	 * @param value
	 */
	public void setValue( String key, int value )
	{
		String[] split = key.split("\\.");
		
		Attribute attr = findAttribute(split);
		String var = findVariable(split);
		int index = findIndex(split);
		
		if (index >= 0) attr.getValueList(var).set(index, value);
		else attr.setValue(var, value);
	}
	
	
	/**
	 * Stores a name
	 * 
	 * @param key
	 * @param name
	 */
	public void setName(String key, String name)
	{
		String[] split = key.split("\\.");
		
		Attribute attr = findAttribute(split);
		String var = findVariable(split);
		int index = findIndex(split);
		
		if (index >= 0) attr.getNameList(var).set(index, name);
		else attr.setName(var, name);
	}
	
	
	/**
	 * Adds a value to the end of a list
	 * 
	 * @param key
	 * @param value
	 */
	public void addValue( String key, int value )
	{
		String[] split = key.split("\\.");
		
		Attribute attr = findAttribute(split);
		String var = findVariable(split);
		
		attr.addListValue(var, value);
	}
	
	
	/**
	 * Adds a name to the end of a list
	 * 
	 * @param key
	 * @param name
	 */
	public void addName( String key, String name )
	{
		String[] split = key.split("\\.");
		
		Attribute attr = findAttribute(split);
		String var = findVariable(split);
		
		attr.addListName(var, name);
	}
	
	
	/**
	 * Clears a list of values
	 * 
	 * @param key
	 */
	public void clearValueList( String key )
	{
		String[] split = key.split("\\.");
		
		Attribute attr = findAttribute(split);
		String var = findVariable(split);
		
		attr.clearValueList(var);
	}
	
	
	/**
	 * Clears a list of names
	 * 
	 * @param key
	 */
	public void clearNameList(String key)
	{
		String[] split = key.split("\\.");
		
		Attribute attr = findAttribute(split);
		String var = findVariable(split);
		
		attr.clearNameList(var);
	}
	
	
	/**
	 * <pre>
	 * Tests a condition
	 * 
	 * has: first >= second
	 * lack: first < second
	 * equal: first = second for values
	 * unequal: first != second for values
	 * same: first = second for names
	 * diff: first != second for names
	 * hasValue: first contains value second
	 * lackValue: first does not contain value second
	 * inValue: first is a valid index for value list second
	 * hasName: first contains name second
	 * lackName: first does not contain name second
	 * inName: first is a valid index for name list second
	 * 
	 * </pre>
	 * 
	 * @param	test
	 * @param	first
	 * @param	second
	 * @return
	 */
	public boolean testCondition( String test, String first, String second )
	{
		int int1;
		int int2;
		String string1;
		String string2;
		ArrayList<Integer> intList;
		ArrayList<String> stringList;
		
		if (test.equals("same"))
		{
			string1 = getName(first);
			string2 = getName(second);
			
			return (string1.equals(string2));
		}
		else if (test.equals("diff"))
		{
			string1 = getName(first);
			string2 = getName(second);
			
			return (string1.equals(string2) == false);
		}
		else if (test.equals("hasValue"))
		{
			intList = getValueList(first);
			int1 = getValue(second);
			
			return (intList.contains(int1));
		}
		else if (test.equals("lackValue"))
		{
			intList = getValueList(first);
			int1 = getValue(second);
			
			return (intList.contains(int1) == false);
		}
		else if (test.equals("inValue"))
		{
			int1 = getValue(first);
			intList = getValueList(second);
			
			return (int1 >= 0 && int1 < intList.size());
		}		
		else if (test.equals("hasName"))
		{
			stringList = getNameList(first);
			string1 = getName(second);
			
			return (stringList.contains(string1));
		}
		else if (test.equals("lackName"))
		{
			stringList = getNameList(first);
			string1 = getName(second);
			
			return (stringList.contains(string1) == false);
		}
		else if (test.equals("inName"))
		{
			int1 = getValue(first);
			stringList = getNameList(second);
			
			return (int1 >= 0 && int1 < stringList.size());
		}
		else
		{
			int1 = getValue(first);
			int2 = getValue(second);
						
			if (test.equals("has")) return (int1 >= int2);
			else if (test.equals("lack")) return (int1 < int2);
			else if (test.equals("equal")) return (int1 == int2);
			else if (test.equals("unequal")) return (int1 == int2);
			else return false;
		}
	}
}
