package vn2;

import java.util.ArrayList;

import javax.swing.JTextArea;


/**
 * A panel which displays information.
 * After a text has been given to the InfoPanel,
 * it remains until it is replaced.
 * The same information can be shown over several paragraphs.
 * You can provide tooltips to the InfoPanel.
 * A tooltip should be shown when the mouse hovers over an option.
 * It should replace the info until the mouse leaves the option.
 * 
 * @author alexi
 */
public class InfoPanel extends JTextArea {

	private static final long serialVersionUID = 1L;

	private String info;
	private ArrayList<String> tooltip;
	
	
	public InfoPanel()
	{
		tooltip = new ArrayList<String>(10);
		info = "";
	}
	
	
	public void addText( TextWrapper wrapper )
	{
		info = wrapper.getText(false, 30);
		setText(info);
	}
	
	
	public void addTooltip( TextWrapper wrapper )
	{
		tooltip.add(wrapper.getText(false, 30));
	}
	
	
	public void showTooltip( int index )
	{
		setText(tooltip.get(index));
	}
	
	
	public void showInfo()
	{
		setText(info);
	}
	
	
	public void clearTooltip()
	{
		tooltip.clear();
		setText(info);
	}
}
