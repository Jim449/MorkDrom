package dröm;

import java.util.ArrayList;

import vn2.Attribute;


/**
 * An unit represents the player during combat.
 * The Visual novel provides an Attribute stats,
 * which describes the players stats.
 * Any changes which last beyond a single battle
 * can only be done in the stats.
 * 
 * @author alexi
 *
 */
public class Unit {

	// Special status
	public static final String STUN = "Förlamad";
	public static final String COUNTER = "Kontrad";
	public static final String SURE_HIT = "Säker träff";
	public static final String BLIND = "Blindhet";
	public static final String PIERCE = "Ignorera försvar";
	public static final String EVASION = "Osårbarhet";
	public static final String INVISIBILITY = "Osynlighet";
	public static final String BUFF_IMMUNITY = "Immun mot förstärkningar";
	public static final String DEBUFF_IMMUNITY = "Immun mot försvagningar";
	public static final String DISPEL_IMMUNITY = "Rotade förstärkningar";
	public static final String CLEANSE_IMMUNITY = "Rotade försvagningar";
	public static final String MANIPULATION_IMMUNITY = "Immun mot manipulation";
	public static final String ENFORCE_TYPE = "Manipulation";
	public static final String STORM = "Storm";
	public static final String SURE_DEBUFF = "Säker försvagning";
	
	// Special status (non-boolean)
	public static final String RECOVERY = "Återhämtning";
	public static final String POISON = "Förgiftning";
	public static final String RECOIL = "Rekyl";
	
	public Attribute stats;
	public Attribute modifier;
	public ArrayList<Effect> effectList;
	
	public Action attackMove = new Action("Anfall", CC.ATTACK);
	public Action waitMove = new Action("Vänta", CC.WAIT);
	public Action emptyMove = new Action();
	public Action[] moveset;
	
	public boolean doubleAction;
	public Action action;
	public Action secondAction;
	
	public boolean allied;
	public boolean player;
	public int abilityMultiplier;
	
	public boolean allowNormalAction;
	
	public int enemyManipulation;
	
	
	/**
	 * Creates a new unit using attribute to determine stats
	 * 
	 * @param attribute
	 */
	public Unit( Attribute attribute )
	{
		stats = attribute;
		modifier = new Attribute();
		effectList = new ArrayList<Effect>();
		
		allied = true;
		player = true;
		abilityMultiplier = 1;
	}
	
	
	/**
	 * Creates a new, empty unit
	 */
	public Unit()
	{
		stats = new Attribute();
		modifier = new Attribute();
		effectList = new ArrayList<Effect>();
		
		allied = true;
		player = true;
		abilityMultiplier = 1;
	}
	
	
	/**
	 * Returns a modifier
	 * 
	 * @param key
	 * @return
	 */
	public int getModifier( String key )
	{
		return modifier.getValue(key);
	}
	
	
	/**
	 * Checks if a modifier has been enabled
	 * 
	 * @param key
	 * @return
	 */
	public boolean hasModifier( String key )
	{
		return (modifier.getValue(key) > 0);
	}
	
	
	/**
	 * Loads the current moveset from stats
	 */
	public void loadMoveset( AbilityList abilityList )
	{
		ArrayList<String> moveList = stats.getNameList(CC.MOVESET);
		
		moveset = new Action[moveList.size()];
		
		for (int i = 0; i < moveList.size(); i++)
		{
			moveset[i] = abilityList.getMove(moveList.get(i));
		}
	}
	
	
	/**
	 * Loads the current moveset from a string array
	 */
	public void loadMoveset( AbilityList abilityList, ArrayList<String> moveList,
			boolean lengthen )
	{
		moveset = new Action[moveList.size()];
		
		for (int i = 0; i < moveList.size(); i++)
		{
			moveset[i] = abilityList.getMove(moveList.get(i));
			
			if (lengthen)
				moveset[i].lengthenEffect();
		}
	}
	
	
	/**
	 * Checks if a counter will work against the targets selected move
	 * 
	 * @param type
	 * @param target
	 * @return
	 */
	public boolean willCounter( int type, Unit target )
	{
		int type2 = target.action.getType();
		
		if (!canHit(target) || target.hasModifier(Unit.SURE_HIT)) return false;
		
		switch (type)
		{
			case CC.ATTACK: return (type2 == CC.ATTACK || type2 == CC.BLACK_WING);
			case CC.BUFF: return (type2 == CC.BUFF || type2 == CC.HEAL || type2 == CC.CLEANSE
					|| type2 == CC.SELF_MANIPULATION || type2 == CC.MASS_MANIPULATION);
			case CC.DEBUFF: return (type2 == CC.DEBUFF || type2 == CC.MANIPULATION
					|| type2 == CC.DISPEL || type2 == CC.BUFF_BLOCK);
			default: return false;
		}
	}
	
	
	/**
	 * Returns true if the unit is protected from all harm
	 * 
	 * @return
	 */
	public boolean hasEvasion()
	{
		return (hasModifier(Unit.EVASION) || hasModifier(Unit.INVISIBILITY));
	}
	
	
	/**
	 * Returns true if the unit can hit the target with an attack or weakening ability
	 * 
	 * @param target
	 * @return
	 */
	public boolean canHit( Unit target )
	{
		return (hasModifier(Unit.SURE_HIT) 
				|| (hasModifier(Unit.BLIND) || target.hasModifier(EVASION)
					|| target.hasModifier(Unit.INVISIBILITY)) == false);
	}
	
	
	
	/**
	 * Checks if a move of the given type will be successful.
	 * This method will consider Counter as successful if it is able
	 * to inflict damage on the opponent after blocking a move.
	 * 
	 * @param type
	 * @param target
	 * @return
	 */
	public boolean willWork( int type, Unit target )
	{
		switch (type)
		{
			case CC.ATTACK: return canHit(target);
			case CC.COUNTER: return canHit(target);
			case CC.SURE_HIT: return true;
			case CC.DEBUFF: return canHit(target)
					&& (hasModifier(Unit.SURE_DEBUFF)
						|| !target.hasModifier(Unit.DEBUFF_IMMUNITY));
			case CC.MANIPULATION: return canHit(target)
					&& (hasModifier(Unit.SURE_DEBUFF)
						|| (!target.hasModifier(Unit.DEBUFF_IMMUNITY)
							&& !target.hasModifier(Unit.MANIPULATION_IMMUNITY)));
			case CC.SELF_MANIPULATION: return true;
			case CC.MASS_MANIPULATION: return true;
			case CC.DISPEL: return !target.hasModifier(Unit.DISPEL_IMMUNITY);
			case CC.BUFF: return !hasModifier(Unit.BUFF_IMMUNITY);
			case CC.HEAL: return true;
			case CC.SHIELD: return !hasModifier(Unit.BUFF_IMMUNITY);
			case CC.CLEANSE: return !hasModifier(Unit.CLEANSE_IMMUNITY);
			case CC.BLACK_WING: return !target.hasEffect(CC.BUFF);
			case CC.BUFF_BLOCK: return canHit(target)
					&& (hasModifier(Unit.SURE_DEBUFF)
						|| !target.hasModifier(Unit.DEBUFF_IMMUNITY));
			default: return false;
		}
	}
	
	
	/**
	 * Checks if an ability action can be activated.
	 * Sets available and disabled fields of action
	 * 
	 * @param action
	 * @return
	 */
	public boolean canActivate( Action action )
	{
		int type = action.getType();
		
		if (type == CC.EMPTY || action.getUntilReady() > 0)
		{
			action.available = false;
			action.disabled = false;
			return false;
		}
		else if (getModifier(Unit.ENFORCE_TYPE) != CC.ANY)
		{
			if (CC.isType(type, getModifier(Unit.ENFORCE_TYPE)))
			{
				action.available = true;
				action.disabled = false;
				return true;
			}
			else
			{
				action.available = false;
				action.disabled = true;
				return false;
			}
		}
		else if (action.getUntilScheduled() <= 0)
		{
			action.available = true;
			action.disabled = false;
			return true;
		}
		else
		{
			action.available = false;
			action.disabled = false;
			return false;
		}
	}
	
	
	/**
	 * Checks which moves can be activated at this point
	 */
	public void checkMoves()
	{
		int enforce = getModifier(Unit.ENFORCE_TYPE);
		
		allowNormalAction = true;
		
		
		for (Action move: moveset)
		{
			if (canActivate(move) && enforce != CC.ANY)
				allowNormalAction = false;
		}
		
		if (allowNormalAction)
		{
			if (enforce == CC.ANY)
			{
				attackMove.available = true;
				attackMove.disabled = false;
				waitMove.available = true;
				waitMove.disabled = false;
			}
			else if (enforce == CC.ATTACK)
			{
				attackMove.available = true;
				attackMove.disabled = false;
				waitMove.available = false;
				waitMove.disabled = true;
			}
			else 
			{
				attackMove.available = false;
				attackMove.disabled = true;
				waitMove.available = true;
				waitMove.disabled = false;
			}
		}
		else
		{
			attackMove.available = false;
			attackMove.disabled = true;
			waitMove.available = false;
			waitMove.disabled = true;
		}
	}
	
	
	/**
	 * Readies all moves for use
	 */
	public void prepareMoves()
	{
		for (Action move: moveset)
		{
			if (allied) move.prepare(true);
			else move.prepare(false);
		}
	}
	
	
	/**
	 * Returns true if the unit is not immobilized
	 * 
	 * @return
	 */
	public boolean canAct()
	{
		return !(hasModifier(Unit.STUN) || hasModifier(Unit.INVISIBILITY));
	}
	
	
	/**
	 * Returns true if the unit is using the Storm skill
	 * 
	 * @return
	 */
	public boolean isUsingStorm()
	{
		return hasModifier(Unit.STORM);
	}
	
	
	/**
	 * Adds a buff or debuff to this unit
	 * 
	 * @param effect
	 */
	public void giveEffect( Effect effect )
	{
		effectList.add(effect);
	}
	
	
	/**
	 * Adds a manipulation effect to this unit.
	 * The manipulation should have
	 * the type SELF_MANIPULATION or MASS_MANIPULATION
	 * 
	 * @param effect
	 */
	public void giveSelfManipulation( Effect effect )
	{
		effectList.add(effect);
		enemyManipulation = 0;
	}
	
	
	/**
	 * Adds a manipulation debuff to this unit.
	 * It must have the MANIPULATION type
	 * 
	 * @param effect
	 */
	public void giveManipulation( Effect effect )
	{
		effectList.add(effect);
		enemyManipulation = effect.getDuration();
	}
	
	
	/**
	 * Counts down the duration of buffs and debuffs
	 * 
	 * @param ability
	 */
	public void countdown( Ability ability )
	{
		Effect effect;
		
		for (int i = effectList.size() - 1; i >= 0; i--)
		{
			effect = effectList.get(i);
			
			if (effect.countdown())
			{
				ability.deactivate(effect, this);
				effectList.remove(i);
			}
		}
		
		enemyManipulation = Math.max(enemyManipulation - 1, 0);
		
		// Counter never immobilizes a unit for more than 1 turn, just remove it
		modifier.setValue(Unit.COUNTER, 0);
	}
	
	
	/**
	 * Removes all effects of the given type
	 * 
	 * @param ability
	 * @param type
	 */
	public void removeEffects( Ability ability, int type )
	{
		Effect effect;
		
		for (int i = effectList.size() - 1; i >= 0; i--)
		{
			effect = effectList.get(i);
			
			if (type == CC.ANY
				|| (CC.isType(effect.getType(), type) && !effect.isPermanent()))
			{
				ability.deactivate(effect, this);
				effectList.remove(i);
			}
		}
	}
	
	
	/**
	 * Returns true if the unit has any effects of the given type
	 * 
	 * @param type
	 * @return
	 */
	public boolean hasEffect( int type )
	{
		for (Effect effect: effectList)
		{
			if (CC.isType(effect.getType(), type)) return true;
		}
		return false;
	}
	
	
	/**
	 * Returns the number of active effects of the given type
	 * 
	 * @param type
	 * @return
	 */
	public int countEffects( int type )
	{
		int amount = 0;
		
		for (Effect effect: effectList)
		{
			if (CC.isType(effect.getType(), type)) amount += 1;
		}
		return amount;
	}
	
	
	/**
	 * Decreases cooldown
	 */
	public void cooldown()
	{
		for (Action move: moveset)
		{
			move.countdown(1);
		}
	}
	
	
	/**
	 * Forces the unit to use a move of the given type
	 * 
	 * @param type
	 */
	public void enforce( int type )
	{
		modifier.setValue(Unit.ENFORCE_TYPE, type);
	}
	
	
	/**
	 * Allows the unit to use moves of any type
	 * 
	 * @param type
	 */
	public void removeEnforce()
	{
		modifier.setValue(Unit.ENFORCE_TYPE, CC.ANY);
	}
	
	
	/**
	 * Returns normal attack power
	 * 
	 * @param target
	 * @return
	 */
	public int getAttack( Unit target )
	{
		float base = stats.getValue(CC.STR);
		float bonus = getModifier(CC.STR);
		float penalty = target.getModifier(CC.DEF);
		
		if (hasModifier(Unit.PIERCE))
		{
			bonus = Math.max(bonus, 0);
			penalty = Math.min(bonus, 0);
		}
		
		return Math.round(base * (100 + bonus - penalty) / 100);
	}
	
	
	/**
	 * Returns ability attack power. Enemies deal 3x normal damage
	 * 
	 * @param target
	 * @param ignoreDefense
	 * @return
	 */
	public int getPower( Unit target, int multiplier, boolean ignoreDefense )
	{
		float base = stats.getValue(CC.STR) * abilityMultiplier;
		float bonus = getModifier(CC.STR);
		float penalty = target.getModifier(CC.DEF);
		
		if (ignoreDefense || hasModifier(Unit.PIERCE))
		{
			bonus = Math.max(bonus, 0);
			penalty = Math.min(penalty, 0);
		}
		
		return Math.round(base * multiplier * (100 + bonus - penalty) / 100);
	}
	
	
	/**
	 * Returns poison attack power. Enemies deals 3x normal damage
	 * 
	 * @param target
	 * @return
	 */
	public int getPoison( Unit target, int multiplier )
	{
		float base = stats.getValue(CC.STR);
		float bonus = getModifier(CC.STR);
		float penalty = target.getModifier(CC.DEF);
		
		if (allied)
			return Math.round(base * multiplier * (100 + bonus - penalty) / 100);
		else
			return Math.round(base * 3 * multiplier * (100 + bonus - penalty) / 100);
	}
	
	
	/**
	 * Returns recoil attack power. Enemies deals 3x normal damage.
	 * Recoil is unaffected by strength and defense modifiers
	 * 
	 * @param target
	 * @return
	 */
	public int getRecoil( Unit target, int multiplier )
	{
		int base = stats.getValue(CC.STR);
		
		if (allied)
			return base * multiplier;
		else
			return base * 3 * multiplier;
	}
	
	
	/**
	 * Returns healing power. Enemies heals 3x normal damage
	 * 
	 * @return
	 */
	public int getHealing( int multiplier )
	{
		float base = stats.getValue(CC.STR) * abilityMultiplier;
		float bonus = getModifier(CC.INT);
		
		return Math.round(base * multiplier * (100 + bonus) / 100);
	}
	
	
	/**
	 * Returns regeneration power. Enemies heals 3x normal damage
	 * 
	 * @return
	 */
	public int getRegeneration( int multiplier )
	{
		float base = stats.getValue(CC.STR);
		float bonus = getModifier(CC.INT);
		
		if (allied)
			return Math.round(base * multiplier * (100 + bonus) / 100);
		else
			return Math.round(base * 3 * multiplier * (100 + bonus) / 100);
	}
	
	
	/**
	 * Inflicts damage to this unit, to a minimum of 0
	 * and returns the amount inflicted
	 * 
	 * @param damage
	 * @return
	 */
	public int inflict( int damage )
	{
		int hp = stats.getValue("Hp");
		damage = Math.min(damage, hp);
		stats.setValue("Hp", hp - damage);
		
		return damage;
	}
	
	
	/**
	 * Restores health to this unit, up to the maximum health value,
	 * and returns the amount inflicted
	 * 
	 * @param damage
	 * @return
	 */
	public int heal( int damage )
	{
		int injuries = stats.getValue("Max hp") - stats.getValue("Hp");
		damage = Math.min(damage, injuries);
		stats.setValue("Hp", stats.getValue("Hp") + damage);
		
		return damage;
	}
	
	
	@Override
	public String toString()
	{
		return stats.getName(CC.NAME);
	}
}
