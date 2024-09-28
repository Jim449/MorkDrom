package vn2;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;


/**
 * A panel containing options which the player can choose from
 * 
 * @author alexi
 */
public class ChoicePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private Window window;
	private JLabel prompt;
	private ArrayList<JButton> choiceList;
	private JTextField input;
	
	
	/**
	 * Creates a new Choice panel
	 * 
	 * @param window
	 */
	public ChoicePanel( Window window )
	{
		this.window = window;
		choiceList = new ArrayList<JButton>(9);
	}
	
	
	/**
	 * Sets the prompt (not used, the message panel is used instead)
	 * 
	 * @param wrapper
	 */
	public void setPrompt( TextWrapper wrapper )
	{
		prompt = new JLabel(wrapper.getText(true, 20));
		prompt.setPreferredSize(C.BUTTON_SIZE);
		prompt.setFont(C.INFO_FONT);
		prompt.setForeground(Color.WHITE);
		add(prompt);
	}
	
	
	/**
	 * Adds an option to this panel
	 * 
	 * @param wrapper
	 */
	public void addOption( TextWrapper wrapper )
	{
		int index = choiceList.size();
		JButton choice = new JButton(wrapper.getText(true, 30));
		int lines = wrapper.getLines();
		Dimension size = new Dimension(C.BUTTON_SIZE.width, C.BUTTON_SIZE.height * lines);
		
		choice.setHorizontalAlignment(SwingConstants.LEFT);
		choice.setPreferredSize(size);
		choice.setBackground(Color.BLACK);
		choice.setFont(C.BUTTON_FONT);
		choice.setForeground(Color.WHITE);
		choice.setActionCommand(String.valueOf(index));
		choice.addMouseListener(window);
		choice.setFocusable(true);
		choice.addActionListener(window);
		choiceList.add(choice);
		add(choice);	
	}
	
	
	/**
	 * Adds an input field
	 */
	public void addInput()
	{
		input = new JTextField();
		
		input.setPreferredSize(C.INPUT_SIZE);
		input.setBackground(Color.WHITE);
		input.setFont(C.INFO_FONT);
		input.setForeground(Color.BLACK);
		add(input);
	}
	
	
	/**
	 * Returns the text from the input field
	 * 
	 * @return
	 */
	public String getInput()
	{
		return input.getText();
	}
	
	
	/**
	 * Removes all choices and input fields from this panel
	 */
	public void clear()
	{
		if (prompt != null) remove(prompt);
		if (input != null) remove(input);
		
		for (JButton choice: choiceList)
		{
			choice.removeActionListener(window);
			remove(choice);
		}
		choiceList.clear();
	}
	
	
	/**
	 * Returns the amount of alternatives the player can choose from
	 * 
	 * @return
	 */
	public int options()
	{
		return choiceList.size();
	}
}
