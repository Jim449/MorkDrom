package vn2;


public class LetterWriter extends Thread {

	private MessagePanel messagePanel;
	private String letter;
	
	
	public LetterWriter( MessagePanel messagePanel, String letter )
	{
		this.messagePanel = messagePanel;
		this.letter = letter;
	}
	
	
	@Override
	public void run()
	{
		
		if (letter == null)
			messagePanel.finishWriting();
		else
			messagePanel.writeOne(letter);
	}
}

