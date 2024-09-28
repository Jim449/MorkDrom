package dröm;


/**
 * Describes actions which can be used during combat.
 * Typically, actions have cooldowns between uses.
 * They may carry effects which activates upon use
 * 
 * @author alexi
 */
public class Action {

	private String name;
	
	private int type;
	private int counterType;
	private int warmup; // wait before first use
	private int cooldown; // wait until next use
	private int untilReady; // wait until off cooldown
	private int untilScheduled; // wait until enemy plans to activate
	
	private boolean unscheduled; // never scheduled for activation, only used when forced
	private boolean targetSelf;

	private Effect effect;
	
	public boolean available;
	public boolean disabled;
	
	public int priority;
	
	public String description;
	
	
	/**
	 * Creates a new action with an instantaneous effect
	 * 
	 * @param name the name of the action
	 * @param type the type of action
	 * @param cooldown the cooldown period after activation
	 */
	public Action( String name, int type, int cooldown )
	{
		this.name = name;
		this.type = type;
		this.cooldown = cooldown;
		this.targetSelf = CC.isType(type, CC.BUFF);
		
		unscheduled = false;
	}
	
	
	/**
	 * Creates a new action with a warmup period
	 * 
	 * @param name the name of the action
	 * @param type the type of action
	 * @param warmup the length of the warmup period
	 * @param cooldown the length of the cooldown period
	 */
	public Action( String name, int type, int warmup, int cooldown )
	{
		this.name = name;
		this.type = type;
		this.warmup = warmup;
		this.cooldown = cooldown;
		this.targetSelf = CC.isType(type, CC.BUFF);
		
		unscheduled = false;
	}
	
	
	/**
	 * Creates a basic action without a cooldown
	 * 
	 * @param name the name of the action
	 * @param type the type of action
	 */
	public Action( String name, int type )
	{
		this.name = name;
		this.type = type;
		this.cooldown = 0;
		this.targetSelf = CC.isType(type, CC.BUFF);
		
		unscheduled = true;
	}
	
	
	/**
	 * Creates the empty action
	 */
	public Action()
	{
		this.name = "";
		this.type = CC.EMPTY;
		
		unscheduled = true;
	}
	
	
	/**
	 * Set the countered type
	 * 
	 * @param type
	 */
	public void setCounterType( int type )
	{
		counterType = type;
	}
	
	
	/**
	 * Returns the countered type (only for counter-type moves)
	 * 
	 * @return
	 */
	public int getCounterType()
	{
		return counterType;
	}
	
	
	/**
	 * Sets the unscheduled property.
	 * Unscheduled moves can't be used unless
	 * the unit is being taunted
	 * 
	 * @param flag
	 */
	public void setUnscheduled( boolean flag )
	{
		unscheduled = flag;
	}
	
	
	/**
	 * Returns true if this move has the unscheduled property.
	 * 
	 * @return
	 */
	public boolean isUnscheduled()
	{
		return unscheduled;
	}
	
	
	/**
	 * Gives a continuous effect to this action
	 * 
	 * @param duration
	 */
	public void addEffect( int power, int duration )
	{
		effect = new Effect(this, power, duration, false);
	}
	
	
	/**
	 * Gives a continuous effect to this action
	 * 
	 * @param duration
	 */
	public void addEffect( int power, int duration, boolean permanent )
	{
		effect = new Effect(this, power, duration, permanent);
	}
	
	
	/**
	 * Increases the duration of this actions effect
	 */
	public void lengthenEffect()
	{
		if (effect != null)
			effect.improveDuration();
	}
	
	
	/**
	 * Returns the power of this actions effect
	 * 
	 * @return
	 */
	public int getPower()
	{
		return effect.getPower();
	}
	
	
	/**
	 * Returns a copy of this actions effect,
	 * with its power set to the given value
	 * 
	 * @param power
	 * @return
	 */
	public Effect getEffect( int power )
	{
		return effect.copy(power);
	}
	
	
	/**
	 * Returns a copy of this actions effect
	 * 
	 * @return
	 */
	public Effect getEffect()
	{
		return effect.copy();
	}
	
	
	/**
	 * Returns true if this action has a continuous effect
	 * 
	 * @return
	 */
	public boolean hasEffect()
	{
		return (effect != null);
	}
	
	
	/**
	 * Returns true if this action targets the user
	 * 
	 * @return
	 */
	public boolean isSelfTargeting()
	{
		return targetSelf;
	}
	
	
	/**
	 * Schedules activation at the start of a battle
	 */
	public void initialSchedule()
	{
		if (unscheduled)
		{
			untilReady = warmup;
			untilScheduled = CC.UNSCHEDULED;
		}
		else
		{
			untilReady = warmup;
			untilScheduled = untilReady + (int)(Math.random() * 3);
		}
	}
	
	
	/**
	 * Readies action for use at the start of a battle 
	 */
	public void prepare( boolean lengthen )
	{
		if (warmup > 0 && lengthen)
			untilReady = warmup + 20;
		else
			untilReady = warmup;
		
		if (unscheduled)
			untilScheduled = CC.UNSCHEDULED;
		else
			untilScheduled = warmup;
	}
	
	
	/**
	 * Starts the cooldown and schedules the next use
	 */
	public void schedule()
	{
		if (unscheduled)
		{
			untilReady = cooldown;
			untilScheduled = CC.UNSCHEDULED;
		}
		else
		{
			untilReady = cooldown;
			untilScheduled = untilReady + (int)(Math.random() * 3);
		}
	}
	
	
	/**
	 * Starts the cooldown
	 * 
	 * @param round
	 */
	public void startCooldown()
	{
		untilReady = cooldown;
		untilScheduled = cooldown;
	}
	
	
	/**
	 * Counts down the moves cooldown
	 */
	public void countdown( int turns )
	{
		untilReady = Math.max(untilReady - turns, 0);
		
		if (untilScheduled != CC.UNSCHEDULED)
			untilScheduled -= 1;
	}
	
	
	/**
	 * Returns true if this action can be scheduled
	 * 
	 * @return
	 */
	public boolean canSchedule()
	{
		return (!unscheduled);
	}
	
	
	/**
	 * Returns the amount of rounds until scheduled
	 * 
	 * @return
	 */
	public int getUntilScheduled()
	{
		return untilScheduled;
	}
	
	
	/**
	 * Returns the amount of rounds until ready
	 * 
	 * @return
	 */
	public int getUntilReady()
	{
		return untilReady;
	}
	
	
	/**
	 * Returns the type of this action
	 * 
	 * @param type
	 * @return
	 */
	public int getType()
	{
		return type;
	}
	
	
	/**
	 * Advances scheduled activation to the earliest round possible
	 * 
	 * @param round
	 */
	public void advance()
	{
		untilScheduled = untilReady;
	}
	
	
	/**
	 * Writes the actions name and remaining cooldown
	 * 
	 * @return
	 */
	public String present()
	{
		if (type == CC.EMPTY) return "(X)";
		else if (untilReady > 0) return name + " (" + untilReady + ")";
		else if (disabled) return name + " (X)";
		else return name;
	}
	
	
	/**
	 * Returns a copy of this action
	 * 
	 * @return
	 */
	public Action copy()
	{
		Action copy = new Action(name, type, cooldown);
		
		copy.warmup = warmup;
		copy.unscheduled = unscheduled;
		copy.counterType = counterType;
		if (effect != null)
			copy.effect = effect.copy();
		return copy;
	}
	
	
	public String toString()
	{
		return name;
	}
}
