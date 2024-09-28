package dröm;


/**
 * Keeps track of unit status changes
 * caused by an ability
 * 
 * @author alexi
 */
public class Effect {

	private String name;
	private int type;
	private int power;
	private int duration;
	
	private boolean permanent;
	
	
	/**
	 * Creates a new effect
	 * 
	 * @param name
	 * @param type
	 * @param power
	 * @param duration
	 */
	public Effect( String name, int type, int power, int duration, boolean permanent )
	{
		this.name = name;
		this.type = type;
		this.power = power;
		this.duration = duration;
		this.permanent = permanent;
	}
	
	
	/**
	 * Creates a new effect out of the given action
	 * 
	 * @param owner
	 * @param power
	 * @param duration
	 * @param permanent
	 */
	public Effect( Action owner, int power, int duration, boolean permanent )
	{
		this.name = owner.toString();
		this.type = owner.getType();
		this.power = power;
		this.duration = duration;
		this.permanent = permanent;
	}
	
	
	/**
	 * Reduces the duration of this effect by 1 and returns true
	 * if this effect has expired
	 * 
	 * @return
	 */
	public boolean countdown()
	{
		duration -= 1;
		return (duration <= 0);
	}
	
	
	/**
	 * Returns the type of this effect
	 * 
	 * @param type
	 * @return
	 */
	public int getType()
	{
		return type;
	}
	
	
	/**
	 * Returns the power of this effect
	 * 
	 * @return
	 */
	public int getPower()
	{
		return power;
	}
	
	
	public int getDuration()
	{
		return duration;
	}
	
	
	/**
	 * Returns true if this effect cannot be dispelled
	 * 
	 * @return
	 */
	public boolean isPermanent()
	{
		return permanent;
	}
	
	
	/**
	 * Returns a copy with the given power level
	 * 
	 * @param power
	 * @return
	 */
	public Effect copy( int power )
	{
		return new Effect(name, type, power, duration, permanent);
	}
	
	
	/**
	 * Returns a copy of this effect
	 * 
	 * @return
	 */
	public Effect copy()
	{
		return new Effect(name, type, power, duration, permanent);
	}
	
	
	/**
	 * Increases the duration
	 * 
	 * @return
	 */
	public void improveDuration()
	{
		duration = 2*duration - 1;
	}
	
	
	/**
	 * Writes effect name and duration
	 * 
	 * @return
	 */
	public String present()
	{
		return name + " (" + duration + ")";
	}
	
	
	@Override
	public String toString()
	{
		return name;
	}
}
