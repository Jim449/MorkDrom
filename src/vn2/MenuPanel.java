package vn2;

import java.awt.Color;
import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.JPanel;


/**
 * Handles menus. When a menu is clicked, a file is read
 * 
 * @author alexi
 */
public class MenuPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private Window window;
	
	private ArrayList<MenuItem> menuList;
	
	
	/**
	 * Creates a menu panel
	 * 
	 * @param window
	 */
	public MenuPanel( Window window )
	{
		this.window = window;
		
		menuList = new ArrayList<MenuItem>(5);
		
		setLayout(new FlowLayout(FlowLayout.LEFT));
	}
	
	
	/**
	 * Creates a menu
	 * 
	 * @param text
	 * @param file
	 * @param function
	 */
	public void addMenu( String text, String file, String function )
	{
		MenuItem item = new MenuItem(text, file, function);
		
		item.setPreferredSize(C.MENU_ITEM_SIZE);
		item.setBackground(Color.BLACK);
		item.setFont(C.MENU_FONT);
		item.setForeground(Color.WHITE);
		item.setFocusable(true);
		item.addActionListener(window);
		menuList.add(item);
		add(item);
	}
	
	
	/**
	 * Returns true if a menu with the given name exists
	 * 
	 * @param name
	 * @return
	 */
	public boolean hasMenu( String name )
	{
		for (MenuItem item: menuList)
		{
			if (item.text.equals(name)) return true;
		}
		return false;
	}
	
	
	/**
	 * Removes all menus
	 */
	public void clear()
	{
		for (MenuItem item: menuList)
		{
			item.removeActionListener(window);
			remove(item);
		}
		
		menuList.clear();
	}
}
