package vn2;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * <pre>
 * A container for variables.
 * There are four types of variables,
 * with the following default values:
 * 
 * Integer: 0
 * String: ""
 * Integer list: (empty list)
 * String list: (empty list)
 * 
 * If one tries to retrieve a variable which doesn't exist,
 * the default value is returned.
 * If one tries to retrieve a list which doesn't exist,
 * an empty list is created, stored and returned.
 * If a variables value becomes equal to the default value
 * at any point in time, the variable is erased.
 * </pre>
 * 
 * @author alexi
 */
public class Attribute {

	public HashMap<String, Integer> intVar;
	public HashMap<String, String> stringVar;
	public HashMap<String, ArrayList<Integer>> intList;
	public HashMap<String, ArrayList<String>> stringList;
	
	
	/**
	 * Creates a new attribute
	 * which stores integer and string variables
	 */
	public Attribute()
	{
		intVar = new HashMap<String, Integer>();
		stringVar = new HashMap<String, String>();
		intList = new HashMap<String, ArrayList<Integer>>();
		stringList = new HashMap<String, ArrayList<String>>();
	}
	
	
	/**
	 * Clears all variables
	 */
	public void clear()
	{
		intVar.clear();
		stringVar.clear();
		intList.clear();
		stringList.clear();
	}
	
	
	/**
	 * Returns a value stored in this attribute
	 * 
	 * @param key
	 * @return
	 */
	public int getValue( String key )
	{
		if (intVar.containsKey(key)) return intVar.get(key);
		else return 0;
	}
	
	
	/**
	 * Returns a name stored in this attribute
	 * 
	 * @param key
	 * @return
	 */
	public String getName( String key )
	{
		if (stringVar.containsKey(key)) return stringVar.get(key);
		else return "";
	}
	
	
	/**
	 * Returns a list of values
	 * 
	 * @param key
	 * @return
	 */
	public ArrayList<Integer> getValueList( String key )
	{
		if (intList.containsKey(key))
			return intList.get(key);
		else
		{
			intList.put(key, new ArrayList<Integer>());
			return intList.get(key);
		}
	}
	
	
	/**
	 * Returns a list of names
	 * 
	 * @param key
	 * @return
	 */
	public ArrayList<String> getNameList( String key )
	{
		if (stringList.containsKey(key))
			return stringList.get(key);
		else
		{
			stringList.put(key, new ArrayList<String>());
			return stringList.get(key);
		}
	}
	
	
	/**
	 * Stores a value in this attribute
	 * 
	 * @param key
	 * @param value
	 */
	public void setValue( String key, int value )
	{
		if (value == 0)
			intVar.remove(key);
		else
			intVar.put(key, value);
	}
	
	
	/**
	 * Stores a name in this attribute
	 * 
	 * @param key
	 * @param name
	 */
	public void setName( String key, String name )
	{
		if (name.equals(""))
			stringVar.remove(key);
		else
			stringVar.put(key, name);
	}
	
	
	/**
	 * Increases a value
	 * 
	 * @param key
	 * @param increase
	 */
	public void addValue( String key, int increase )
	{
		int value = getValue(key);
		setValue(key, value + increase);
	}
	
	
	/**
	 * Adds an integer value to the end of a list
	 * 
	 * @param key
	 * @param value
	 */
	public void addListValue( String key, int value )
	{
		if (intList.containsKey(key))
			intList.get(key).add(value);
		else
		{
			intList.put(key, new ArrayList<Integer>());
			intList.get(key).add(value);
		}
	}
	
	
	/**
	 * Adds a string value to the end of a list
	 * 
	 * @param key
	 * @param name
	 */
	public void addListName( String key, String name )
	{
		if (stringList.containsKey(key))
			stringList.get(key).add(name);
		else
		{
			stringList.put(key, new ArrayList<String>());
			stringList.get(key).add(name);
		}
	}
	
	
	/**
	 * Clears a list of values
	 * 
	 * @param key
	 */
	public void clearValueList( String key )
	{
		intList.remove(key);
	}
	
	
	/**
	 * Clears a list of names
	 * 
	 * @param key
	 */
	public void clearNameList( String key )
	{
		stringList.remove(key);
	}
	
	
	/**
	 * Calculates the value of a linear function.
	 * Each string in the variableList points to a value in this Attribute.
	 * That value is multiplied with the respective value in the multiplierList.
	 * Then, the products are summarized.
	 * 
	 * @param variableList
	 * @param multiplierList
	 * @return
	 */
	public int calculate( ArrayList<String> variableList,
			ArrayList<Integer> multiplierList )
	{
		int value = 0;
		
		for (int i = 0; i < variableList.size(); i++ )
		{
			value += getValue(variableList.get(i)) * multiplierList.get(i);
		}
		
		return value;
	}
	
	
	/**
	 * Returns a copy of this attribute.
	 * 
	 * @return
	 */
	public Attribute copy()
	{
		Attribute duplicate = new Attribute();
		
		int value;
		String name;
		ArrayList<Integer> valueList;
		ArrayList<String> nameList;
		
		for (String key: intVar.keySet())
		{
			value = intVar.get(key);
			duplicate.setValue(key, value);
		}
		
		for (String key: stringVar.keySet())
		{
			name = stringVar.get(key);
			duplicate.setName(key, name);
		}
		
		for (String key: intList.keySet())
		{
			valueList = intList.get(key);
			
			for (int i = 0; i < valueList.size(); i++ )
			{
				duplicate.addListValue(key, valueList.get(i));
			}
		}
		
		for (String key: stringList.keySet())
		{
			nameList = stringList.get(key);
			
			for (int i = 0; i < nameList.size(); i++ )
			{
				duplicate.addListName(key, nameList.get(i));
			}
		}
		return duplicate;
	}
}
