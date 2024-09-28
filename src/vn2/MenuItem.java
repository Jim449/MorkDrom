package vn2;

import javax.swing.JButton;

public class MenuItem extends JButton {

	private static final long serialVersionUID = 1L;

	public static final String MENU_ITEM = "Menu item";
	
	public String text;
	public String file;
	public String function;
	
	
	public MenuItem( String text, String file, String function )
	{
		this.text = text;
		this.file = file;
		this.function = function;
		
		setText(text);
		setActionCommand(MenuItem.MENU_ITEM);
	}
}
