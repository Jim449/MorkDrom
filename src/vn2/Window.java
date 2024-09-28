package vn2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.border.CompoundBorder;


public class Window extends JFrame implements MouseListener, KeyListener, ActionListener {

	private static final long serialVersionUID = 1L;

	public static int NONE = 1;
	public static int ACCEPT = 1;
	public static int CHOICE = 2;
	public static int INPUT = 3;
	
	public ResourcePanel resourcePanel;
	public MenuPanel menuPanel;
	public ImagePanel imagePanel;
	public MessagePanel messagePanel;
	public ChoicePanel choicePanel;
	public InfoPanel infoPanel;
	public JLayeredPane centerPanel;
	
	private Interpret interpret;
	
	private long lastClick;
	
	private int inputType;
	
	
	public Window()
	{
		CompoundBorder border = new CompoundBorder(C.LINE_BORDER, C.BORDER);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setPreferredSize(C.MAIN_SIZE);
		setFocusable(true);
		addKeyListener(this);
		setResizable(false);
		
		centerPanel = new JLayeredPane();
		centerPanel.setPreferredSize(C.CENTER_SIZE);
		centerPanel.setFocusable(true);
		centerPanel.addKeyListener(this);
		add(centerPanel, BorderLayout.CENTER);
		
		resourcePanel = new ResourcePanel();
		resourcePanel.setPreferredSize(C.RESOURCE_SIZE);
		resourcePanel.setBackground(Color.BLACK);
		resourcePanel.setBorder(C.LINE_BORDER);
		resourcePanel.setFocusable(true);
		resourcePanel.addKeyListener(this);
		add(resourcePanel, BorderLayout.NORTH);
		
		menuPanel = new MenuPanel(this);
		menuPanel.setPreferredSize(C.MENU_SIZE);
		menuPanel.setBackground(Color.BLACK);
		menuPanel.setBorder(C.LINE_BORDER);
		menuPanel.setFocusable(true);
		menuPanel.addKeyListener(this);
		add(menuPanel, BorderLayout.SOUTH);
		
		choicePanel = new ChoicePanel(this);
		choicePanel.setPreferredSize(C.CHOICE_SIZE);
		choicePanel.setBackground(Color.BLACK);
		choicePanel.setBorder(border);
		choicePanel.addMouseListener(this);
		choicePanel.addKeyListener(this);
		choicePanel.setFocusable(true);
		add(choicePanel, BorderLayout.WEST);
		
		infoPanel = new InfoPanel();
		infoPanel.setPreferredSize(C.INFO_SIZE);
		infoPanel.setBackground(Color.BLACK);
		infoPanel.setBorder(border);
		infoPanel.setForeground(Color.WHITE);
		infoPanel.setFont(C.INFO_FONT);
		infoPanel.setEditable(false);
		infoPanel.addKeyListener(this);
		infoPanel.setFocusable(true);
		add(infoPanel, BorderLayout.EAST);
		
		pack();
		
		imagePanel = new ImagePanel();
		imagePanel.setSize(C.CENTER_SIZE);
		imagePanel.setBackground(Color.BLACK);
		imagePanel.addMouseListener(this);
		imagePanel.addKeyListener(this);
		imagePanel.setFocusable(true);
		centerPanel.add(imagePanel, Integer.valueOf(0));
		
		messagePanel = new MessagePanel();
		messagePanel.setSize(C.MESSAGE_SIZE);
		messagePanel.setLocation(0, C.CENTER_SIZE.height - C.MESSAGE_SIZE.height);
		messagePanel.setBackground(Color.BLACK);
		messagePanel.setOpaque(false);
		messagePanel.setBorder(border);
		messagePanel.setForeground(Color.WHITE);
		messagePanel.setFont(C.FONT);
		messagePanel.setEditable(false);
		messagePanel.addMouseListener(this);
		messagePanel.addKeyListener(this);
		messagePanel.setFocusable(true);
		centerPanel.add(messagePanel, Integer.valueOf(1));
		
		addKeyListener(this);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	
	public void setInterpret( Interpret interpret )
	{
		this.interpret = interpret;
	}
	
	
	public void setInputType( int type )
	{
		inputType = type;
	}
	
	
	private boolean antiDoubleClick()
	{
		long now = System.currentTimeMillis();
		
		if (now - lastClick < 200) return false;
		else
		{
			lastClick = now;
			return true;
		}
	}
	
	
	private void sendChoice( int choice )
	{
		if (choice >= 0 && choice < choicePanel.options())
		{
			choicePanel.clear();
			infoPanel.clearTooltip();
			setInputType(Window.NONE);
			interpret.sendChoice(choice);
		}
	}
	
	
	private void sendInput( int choice )
	{
		String input = choicePanel.getInput();
		
		if (choice >= 0 && choice < choicePanel.options())
		{	
			choicePanel.clear();
			infoPanel.clearTooltip();
			setInputType(Window.NONE);
			interpret.sendInputChoice(choice, input);
		}
	}
	


	@Override
	public void actionPerformed(ActionEvent e)
	{
		MenuItem menu;
		int index;
		
		if (antiDoubleClick())
		{
			if (e.getActionCommand().equals(MenuItem.MENU_ITEM))
			{
				menu = (MenuItem)e.getSource();
				interpret.openMenu(menu.text, menu.file, menu.function);
			}
			else
			{
				index = Integer.valueOf(e.getActionCommand());
				
				if (inputType == Window.CHOICE) sendChoice(index);
				else if (inputType == Window.INPUT) sendInput(index);
			}
		}
	}


	@Override
	public void keyTyped(KeyEvent e)
	{
	}


	@Override
	public void keyPressed(KeyEvent e)
	{
		imagePanel.requestFocus();
		
		if (antiDoubleClick())
		{
			if (messagePanel.proceed() && inputType == Window.ACCEPT)
			{
				setInputType(Window.NONE);
				interpret.sendAccept();
			}
		}
	}


	@Override
	public void keyReleased(KeyEvent e)
	{
	}


	@Override
	public void mouseClicked(MouseEvent e)
	{
		imagePanel.requestFocus();
		
		if (antiDoubleClick())
		{
			if (messagePanel.proceed() && inputType == Window.ACCEPT)
			{
				setInputType(Window.NONE);
				interpret.sendAccept();
			}
		}
	}


	@Override
	public void mousePressed(MouseEvent e)
	{
	}


	@Override
	public void mouseReleased(MouseEvent e)
	{
	}


	@Override
	public void mouseEntered(MouseEvent e)
	{
		JButton button;
		int index;
		
		try
		{
			button = (JButton)e.getSource();
			index = Integer.valueOf(button.getActionCommand());
			infoPanel.showTooltip(index);
		}
		catch (Exception error)
		{
			
		}
	}


	@Override
	public void mouseExited(MouseEvent e)
	{
		JButton button;
		
		try
		{
			button = (JButton)e.getSource();
			infoPanel.showInfo();
		}
		catch (Exception error)
		{
			
		}
	}
	
}
