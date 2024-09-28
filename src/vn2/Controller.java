package vn2;

import dröm.Battle;

/**
 * The VN controller!
 * 
 * @author alexi
 */
public class Controller extends Thread {
	
	public Window window;
	public Interpret interpret;
	public Database database;
	public Responder responder;
	
	public int textDelay = 40;
	
	private boolean shouldCall;
	
	
	/**
	 * Creates the controller
	 */
	public Controller()
	{
		this.window = new Window();
		this.database = new Database();
		this.interpret = new Interpret(this, window, database);
		
		window.setInterpret(interpret);
		window.resourcePanel.setDatabase(database);
	}
	
	
	/**
	 * Runs the main loop
	 */
	public void run()
	{	
		while (true)
		{
			try
			{
				sleep(textDelay);
				
				if (shouldCall) call();
				
				window.messagePanel.writeLetter();
				window.repaint();
			}
			catch (InterruptedException error)
			{
				error.printStackTrace();
			}
		}
	}
	
	
	/**
	 * Calls extra java code.
	 * To use, extend Responder and override respond.
	 * Write '{call}' in your VN code.
	 * The responder should decide the next action
	 * by reading variables from the database.
	 * Call interpret.proceed from the responder to resume
	 */
	private void call()
	{
		shouldCall = false;
		responder.respond();
	}
	
	
	/**
	 * Calls extra java code
	 */
	public void prepareCall()
	{
		shouldCall = true;
	}
	
	
	/**
	 * Starts the game!
	 * 
	 * @param arg
	 */
	public static void main( String arg[] )
	{
		Controller controller = new Controller();
		
		controller.responder = new Battle(controller.interpret, controller.database);
		controller.interpret.chapter("Start");
		controller.interpret.proceed();
		
		controller.start();
	}
}
