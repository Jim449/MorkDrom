package vn2;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;


public class ImagePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private Image background;

	
	public void loadBackground( String filename )
	{
		File file = new File(filename);
		
		try
		{
			background = ImageIO.read(file);
		}
		catch (IOException error)
		{
			System.out.println("Kan inte ladda bild " + file.getPath());
			error.printStackTrace();
		}
	}
	
	
	public void clear()
	{
		background = null;
	}
	
	
	@Override
	public void paintComponent( Graphics graphics )
	{
		super.paintComponent(graphics);
		
		if (background != null)
		{
			graphics.drawImage(background, 0, 0, null);
		}
	}
}
