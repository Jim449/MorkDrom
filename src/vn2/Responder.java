package vn2;

public abstract class Responder {

	public Interpret interpret;
	public Database database;
	
	
	public Responder( Interpret interpret, Database database )
	{
		this.interpret = interpret;
		this.database = database;
	}
	
	
	public abstract void respond();
}
