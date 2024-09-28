package vn2;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JTextArea;

public class MessagePanel extends JTextArea {

	private static final long serialVersionUID = 1L;
	private static final Color TRANSPARENT = new Color(0, 0, 0, 200);
	
	private String fullMessage;
	private int progress;
	private boolean awaitingInput;
	private boolean finishQuickly;
	
	public boolean stillWriting = false;
	
	
	public MessagePanel()
	{
		fullMessage = "";
	}
	
	
	public void clearMessage()
	{
		setText("");
		fullMessage = "";
		progress = 0;
	}
	
	
	public void writeMessage( TextWrapper wrapper )
	{
		clearMessage();
		fullMessage = wrapper.getText(false, C.MESSAGE_LINE);
		
		awaitingInput = (fullMessage.length() > 0);
		finishQuickly = false;
	}
	
	
	public boolean isAwaitingInput()
	{
		return awaitingInput;
	}
	
	
	public synchronized void writeOne( String letter )
	{
		stillWriting = true;
		append(letter);
		progress += 1;
		stillWriting = false;
		notify();
	}
	
	
	public synchronized void finishWriting()
	{
		setText(fullMessage);
		progress = fullMessage.length();
		stillWriting = false;
		notify();
	}
	
	
	public boolean proceed()
	{
		if (!awaitingInput)
			return false;
		
		else if (progress < fullMessage.length())
		{
			finishQuickly = true;
			return false;
		}
		else
		{
			awaitingInput = false;
			return true;
		}
	}
	
	
	public synchronized void writeLetter()
	{
		LetterWriter writer;
		
		if (progress < fullMessage.length())
		{
			if (finishQuickly)
				writer = new LetterWriter(this, null);
			else
				writer = new LetterWriter(this, 
						fullMessage.substring(progress, progress + 1));
			
			stillWriting = true;
			writer.start();
			
			while (stillWriting)
			{
				try
				{
					wait();
				}
				catch (InterruptedException error)
				{
					error.printStackTrace();
				}
			}
		}
	}
	
	
	@Override
	public void paintComponent( Graphics graphics )
	{
		graphics.setColor(MessagePanel.TRANSPARENT);
		graphics.fillRect(0, 0, getWidth(), getHeight());
		super.paintComponent(graphics);
	}
}
