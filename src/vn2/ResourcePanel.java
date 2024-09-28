package vn2;

import java.awt.Color;
import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.JPanel;


/**
 * A panel capable of displaying variables.
 * Variables are read from the database and are updated automatically.
 * 
 * @author alexi
 *
 */
public class ResourcePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private ArrayList<ResourceItem> resourceList;
	private Database database;
	
	
	/**
	 * Creates a new resource panel
	 */
	public ResourcePanel()
	{
		resourceList = new ArrayList<ResourceItem>(10);
		setLayout(new FlowLayout(FlowLayout.LEFT));
	}
	
	
	/**
	 * Sets the database which is required for creating resource items
	 * 
	 * @param database
	 */
	public void setDatabase( Database database )
	{
		this.database = database;
	}
	
	
	/**
	 * Refreshes a single resource item
	 * 
	 * @param item
	 */
	private void refreshItem( ResourceItem item )
	{
		String var1;
		String var2;
		
		if (item.intVar)
		{
			var1 = String.valueOf(database.getValue(item.var1));
			item.refresh(var1);
		}
		else if (item.dualVar)
		{
			var1 = String.valueOf(database.getValue(item.var1));
			var2 = String.valueOf(database.getValue(item.var2));
			item.refresh(var1, var2);
		}
		else
		{
			var1 = database.getName(item.var1);
			item.refresh(var1);
		}
	}
	
	
	/**
	 * Refreshes all resource items
	 */
	public void refresh()
	{
		for (ResourceItem item: resourceList)
		{
			refreshItem(item);
		}
	}
	
	
	/**
	 * Adds a new resource item to this panel
	 * 
	 * @param text text to be displayed
	 * @param variable variable to be displayed
	 * @param intVar true if the variable points at an integer
	 */
	public void addResource( String text, String variable, boolean intVar )
	{
		ResourceItem item = new ResourceItem(text, variable, intVar);
		
		item.setPreferredSize(C.RESOURCE_ITEM_SIZE);
		item.setFont(C.RESOURCE_FONT);
		item.setForeground(Color.WHITE);
		refreshItem(item);
		resourceList.add(item);
		add(item);
	}
	
	
	/**
	 * Adds a new resource to this panel, displaying two values
	 * 
	 * @param text the text to be displayed
	 * @param var1 the first variable to be displayed
	 * @param separator the text which separates the first and second variables
	 * @param var2 the second variable to be displayed
	 */
	public void addResource( String text, String var1, String separator, String var2 )
	{
		ResourceItem item = new ResourceItem(text, var1, separator, var2);
		
		item.setPreferredSize(C.RESOURCE_ITEM_SIZE);
		item.setFont(C.RESOURCE_FONT);
		item.setForeground(Color.WHITE);
		refreshItem(item);
		resourceList.add(item);
		add(item);
	}
	
	
	/**
	 * Removes all resource items
	 */
	public void clear()
	{
		for (ResourceItem item: resourceList)
		{
			remove(item);
		}
		resourceList.clear();
	}
}
