package dröm;

public class CC {

	// Move types
	public static final int ANY = 0;
	public static final int ATTACK = 1;
	public static final int COUNTER = 2;
	public static final int BUFF = 3;
	public static final int HEAL = 4;
	public static final int DEBUFF = 5;
	public static final int MANIPULATION = 6;
	public static final int WAIT = 7;
	public static final int SHIELD = 8;
	public static final int EMPTY = 9;
	public static final int CLEANSE = 10;
	public static final int DISPEL = 11;
	public static final int SURE_HIT = 12;
	public static final int BLACK_WING = 13;
	public static final int SELF_MANIPULATION = 14;
	public static final int MASS_MANIPULATION = 15;
	public static final int BUFF_BLOCK = 16;
	public static final int INVITATION = 17;
	
	public static final int UNSCHEDULED = 100;
	public static final int TURN_FRACTIONS = 4;
	
	// Basic status
	public static final String NAME = "Namn";
	public static final String STR = "Styrka";
	public static final String DEF = "Försvar";
	public static final String INT = "Helande kraft";
	public static final String LVL = "Grad";
	public static final String MOVESET = "Drag";
	
	
	/**
	 * <pre>
	 * Returns true if type1 is included
	 * in the category of type2
	 * 
	 * ANY: all types
	 * 
	 * ATTACK: ATTACK, SURE_HIT and COUNTER
	 * 
	 * BUFF: BUFF, HEAL, SHIELD, CLEANSE,
	 * SELF_MANIPULATION and MASS_MANIPULATION
	 * 
	 * DEBUFF: DEBUFF, MANIPULATION, DISPEL and BUFF_BLOCK,
	 * 
	 * MANIPULATION: MANIPULATION,
	 * SELF_MANIPULATION and MASS_MANIPULATION
	 * 
	 * Do not test WAIT or EMPTY here.
	 * 
	 * </pre>
	 * 
	 * @param type1
	 * @param type2
	 * @return
	 */
	public static boolean isType( int type1, int type2 )
	{
		if (type1 == type2) return true;
		
		switch (type2)
		{
			case CC.ANY: return true;
			case CC.ATTACK: return (type1 == CC.COUNTER || type1 == SURE_HIT);
			case CC.BUFF: return (type1 == CC.HEAL || type1 == CC.SHIELD
					|| type1 == CC.CLEANSE || type1 == CC.SELF_MANIPULATION
					|| type1 == CC.MASS_MANIPULATION);
			case CC.DEBUFF: return (type1 == CC.MANIPULATION || type1 == CC.DISPEL
					|| type1 == CC.BUFF_BLOCK);
			case CC.MANIPULATION: return (type1 == CC.SELF_MANIPULATION
					|| type1 == CC.MASS_MANIPULATION);
			default: return false;
		}
	}
	
	
	/**
	 * Returns a priority value from 0 to 8 of a move type
	 * 
	 * @param type
	 * @return
	 */
	public static int getPriority( int type )
	{
		switch (type)
		{
			case CC.SHIELD: return 9;
			case CC.COUNTER: return 8;
			case CC.BUFF_BLOCK: return 7;
			case CC.BUFF: return 6;
			case CC.DEBUFF: return 5;
			case CC.MANIPULATION: return 5;
			case CC.SELF_MANIPULATION: return 5;
			case CC.MASS_MANIPULATION: return 5;
			case CC.CLEANSE: return 4;
			case CC.DISPEL: return 4;
			case CC.HEAL: return 3;
			case CC.ATTACK: return 2;
			case CC.BLACK_WING: return 2;
			case CC.SURE_HIT: return 2;
			case CC.WAIT: return 1;
			default: return 0;
		}
	}
}
