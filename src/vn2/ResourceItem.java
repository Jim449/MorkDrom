package vn2;

import javax.swing.JLabel;
import javax.swing.SwingConstants;


/**
 * Displays variables
 * 
 * @author alexi
 */
public class ResourceItem extends JLabel {

	private static final long serialVersionUID = 1L;

	private String text;
	private String separator;
	
	public String var1;
	public String var2;
	
	public boolean intVar;
	public boolean dualVar;
	
	
	/**
	 * Creates a new resource item to display a string or integer
	 * 
	 * @param text
	 * @param variable
	 * @param intVar
	 */
	public ResourceItem( String text, String variable, boolean intVar )
	{
		this.text = text;
		this.var1 = variable;
		this.intVar = intVar;
		setHorizontalAlignment(SwingConstants.CENTER);
	}
	
	
	/**
	 * Creates a new resource item to display two integers
	 * 
	 * @param text
	 * @param var1
	 * @param separator
	 * @param var2
	 */
	public ResourceItem( String text, String var1, String separator, String var2 )
	{
		this.text = text;
		this.var1 = var1;
		this.separator = separator;
		this.var2 = var2;
		this.dualVar = true;
		setHorizontalAlignment(SwingConstants.CENTER);
	}
	
	
	/**
	 * Refreshes this resource item with the given value
	 * 
	 * @param value
	 */
	public void refresh( String value )
	{
		setText(text + " " + value);
	}
	
	
	/**
	 * Refreshes this resource item with the two given values
	 * 
	 * @param value1
	 * @param value2
	 */
	public void refresh( String value1, String value2 )
	{
		setText(text + " " + value1 + separator + value2);
	}
}
