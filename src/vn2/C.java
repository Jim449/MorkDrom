package vn2;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class C {

	public static final Dimension MAIN_SIZE = new Dimension(1080, 480);
	public static final Dimension CENTER_SIZE = new Dimension(540, 400);
	public static final Dimension MESSAGE_SIZE = new Dimension(540, 120);
	public static final Dimension RESOURCE_SIZE = new Dimension(1080, 40);
	public static final Dimension MENU_SIZE = new Dimension(1080, 40);
	public static final Dimension CHOICE_SIZE = new Dimension(270, 400);
	public static final Dimension INFO_SIZE = new Dimension(270, 400);
	
	public static final Dimension INPUT_SIZE = new Dimension(240, 25);
	public static final Dimension BUTTON_SIZE = new Dimension(240, 25);
	public static final Dimension RESOURCE_ITEM_SIZE = new Dimension(120, 30);
	public static final Dimension MENU_ITEM_SIZE = new Dimension(120, 30);
	
	public static final EmptyBorder BORDER = new EmptyBorder(15, 15, 15, 15);
	public static final LineBorder LINE_BORDER = new LineBorder(Color.WHITE, 1, false);
	
	public static final Font FONT = new Font("Candara", Font.PLAIN, 18);
	public static final Font INFO_FONT = new Font("Candara", Font.PLAIN, 16);
	public static final Font BUTTON_FONT = new Font("Candara", Font.PLAIN, 14);
	public static final Font MENU_FONT = new Font("Candara", Font.PLAIN, 14);
	public static final Font RESOURCE_FONT = new Font("Candara", Font.PLAIN, 14);
	
	public static final int MESSAGE_LINE = 60;
	
	public static final String MAIN_ATTR = "Main";
	public static final String CHOICE = "Choice";
	public static final String ADDRESS_DEPTH = "Address depth";
}
