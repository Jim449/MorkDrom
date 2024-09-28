package dröm;

import vn2.Attribute;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.google.gson.Gson;


/**
 * Represent a non-player character during combat.
 * An NPC can be an enemy or an assistant.
 * Assistants give the hero extra, ai-controlled attacks
 * but doesn't appear on the battlefield as seperate units.
 * 
 * @author alexi
 */
public class NPCUnit extends Unit {
	
	private ArrayList<String> preparations;
	
	
	/**
	 * Creates a new enemy using the values in attribute
	 * 
	 * @param attribute
	 */
	public NPCUnit( Attribute attribute, AbilityList abilityList )
	{
		super(attribute);
		
		int level = stats.getValue("Grad");
		
		stats.setValue("Styrka", stats.getValue("Styrka") * level);
		stats.setValue("Hp", stats.getValue("Hp") * level);
		stats.setValue("Max hp", stats.getValue("Hp"));
		stats.setValue("Belöning", stats.getValue("Belöning") * level);
		
		allied = false;
		player = false;
		abilityMultiplier = 3;
		
		loadMoveset(abilityList);
	}
	
	
	/**
	 * Creates a new ally to the given hero using the values in attribute
	 * 
	 * @param attribute
	 * @param hero
	 */
	public NPCUnit( Attribute attribute, Unit hero, AbilityList abilityList )
	{
		super(attribute);
		
		stats.setValue("Styrka", stats.getValue("Styrka") * stats.getValue("Grad"));
		
		allied = true;
		player = false;
		modifier = hero.modifier;
		
		loadMoveset(abilityList);
	}
	
	
	/**
	 * Creates a new enemy using by reading values from a json file
	 * with the given name.
	 * 
	 * @param name
	 * @param level
	 * @param abilityList
	 */
	public NPCUnit( String name, int level, AbilityList abilityList )
	{
		super();
		
		Gson gson = new Gson();
		BufferedReader reader;
		FileInputStream file;
		UnitSketch sketch;
		
		try
		{
			file = new FileInputStream("Mörk dröm/Fiender/" + name + ".json");
			reader = new BufferedReader(new InputStreamReader(file, "UTF-8"));
			sketch = gson.fromJson(reader, UnitSketch.class);
			
			stats = new Attribute();
			stats.setName("Namn", sketch.namn);
			stats.setName("Element", sketch.element);
			stats.setValue("Grad", level);
			stats.setValue("Styrka", 2 + level);
			stats.setValue("Hp", sketch.hp * (2+level));
			stats.setValue("Max hp", sketch.hp * (2 + level));
			stats.setValue("Belöning", sketch.belöning * level);
			
			loadMoveset(abilityList, sketch.förmågor, true);
			doubleAction = sketch.extradrag;
			
			if (sketch.förberedelser != null)
				preparations = (ArrayList<String>) sketch.förberedelser.clone();
			else
				preparations = new ArrayList<String>();
			
			reader.close();
		}
		catch (IOException error)
		{
			error.printStackTrace();
		}
		
		allied = false;
		player = false;
		abilityMultiplier = 3;
	}
	
	
	/**
	 * Creates a new ally to the hero
	 * by reading values from a json file 
	 * with the given name.
	 * 
	 * @param name
	 * @param level
	 * @param abilityList
	 */
	public NPCUnit( String name, int level, Unit hero, AbilityList abilityList )
	{
		super();
		
		Gson gson = new Gson();
		BufferedReader reader;
		FileInputStream file;
		UnitSketch sketch;
		
		try
		{
			file = new FileInputStream("Mörk dröm/Allierade/" + name + ".json");
			reader = new BufferedReader(new InputStreamReader(file, "UTF-8"));
			sketch = gson.fromJson(reader, UnitSketch.class);
			
			stats = new Attribute();
			
			stats.setName("Namn", sketch.namn);
			stats.setName("Element", sketch.element);
			stats.setValue("Grad", level);
			stats.setValue("Styrka", 2 + level);
			
			loadMoveset(abilityList, sketch.förmågor, false);
			
			reader.close();
		}
		catch (FileNotFoundException error)
		{
			error.printStackTrace();
		}
		catch (IOException error)
		{
			error.printStackTrace();
		}
		
		modifier = hero.modifier;
		allied = true;
		player = false;
	}
	
	
	/**
	 * Creates an empty enemy
	 */
	public NPCUnit()
	{
		super();
	}
	
	
	/**
	 * <pre>
	 * Returns true if a move with the given type
	 * can do something meaningful at this time.
	 * 
	 * * Shields are useful only if they have a chance
	 * to block a move.
	 * 
	 * * Cleanses and dispels are useful
	 * only if they would remove at least one effect.
	 * 
	 * * Manipulations are useful only if the target can act
	 * and is not affected by another user-inflicted manipulation effect,
	 * except manipulation which wears off at the end of the current turn
	 * 
	 * * The Black wing move is useful only if the targets have no buffs.
	 * </pre>
	 * 
	 * @param type
	 * @param target
	 * @return
	 */
	private boolean isUseful( int type, Unit ally, Unit target )
	{
		if (type == CC.SHIELD)
			return (target.getModifier(Unit.ENFORCE_TYPE) != CC.BUFF
					&& target.canAct()
					&& target.canHit(this)
					&& !hasModifier(Unit.BUFF_IMMUNITY));
		
		else if (type == CC.CLEANSE)
			return (ally.hasEffect(CC.DEBUFF) && hasModifier(Unit.CLEANSE_IMMUNITY) == false);
		
		else if (type == CC.DISPEL)
			return (target.hasEffect(CC.BUFF)
					&& target.hasModifier(Unit.DISPEL_IMMUNITY) == false);
		
		else if (type == CC.MANIPULATION)
			return (willWork(type, target) && target.enemyManipulation <= 1);
		
		else if (type == CC.BLACK_WING)
			return (!target.hasEffect(CC.BUFF));
		
		else
			return true;
	}
	
	
	/**
	 * Checks if a counter may work against the target,
	 * if the targets next move is unknown.
	 * The enforced move type is considered.
	 * The targets moveset is not considered.
	 * 
	 * @param type
	 * @param target
	 * @return
	 */
	private boolean mayCounter( int type, Unit target )
	{
		int enforce = target.getModifier(Unit.ENFORCE_TYPE);
		
		if (target.hasModifier(Unit.SURE_HIT) || !target.canAct()) return false;
		else if (enforce == CC.ANY || enforce == type) return true;
		else return false;
	}
	
	
	/**
	 * <pre>
	 * Assigns all available moves a priority
	 * and returns the max priority.
	 * Priorities will be considered
	 * when actions are selected.
	 * 
	 * Priority:
	 * -2. Move can never be used
	 * -1. Move should not be used but the unit
	 * may be forced to use it
	 * 0+. Move should be used
	 * 
	 * Efficient moves have priorities based
	 * on the amount of time they've been delayed.
	 * Note that moves are scheduled
	 * to be used 0-2 turns after they exit cooldown.
	 * These 0-2 turn does not count as delay.
	 * </pre>
	 */
	private int assessMoves( Unit ally, Unit enemy )
	{
		int type;
		int maxPrio = -2;
		
		for (Action move: moveset)
		{
			type = move.getType();
			
			if (!move.available)
				move.priority = -2;
			
			else if (type == CC.COUNTER)
			{
				if (mayCounter(move.getCounterType(), enemy))
					move.priority = Math.max(0 - move.getUntilScheduled(), 0);
				else
					move.priority = -1;
			}
			else if (type == CC.SHIELD || type == CC.CLEANSE || type == CC.DISPEL
					|| type == CC.MANIPULATION || type == CC.BLACK_WING)
			{
				if (isUseful(type, ally, enemy))
					move.priority = Math.max(0 - move.getUntilScheduled(), 0);
				else
					move.priority = -1;
			}
			else
			{
				if (willWork(move.getType(), enemy))
					move.priority = Math.max(0 - move.getUntilScheduled(), 0);
				else
					move.priority = -1;
			}
			
			if (move.priority >= 0 && move.isUnscheduled()) move.priority = 100;
			
			maxPrio = Math.max(move.priority, maxPrio);
		}
		attackMove.priority = (willWork(CC.ATTACK, enemy)) ? 0 : -1;
		
		return maxPrio;
	}
	
	
	/**
	 * <pre>
	 * The enemy chooses an action randomly.
	 * NOTE: checkMove must be called first.
	 * 
	 * Rules for choosing actions:
	 * 
	 * * Each action has a cooldown.
	 * 
	 * * Each action has a scheduled wait,
	 * equal to cooldown plus a random value from 0 to 2.
	 * 
	 * The enemy will choose an action according to the priority
	 * 1. Ability
	 * 2. Normal attack
	 * 3. Wait
	 * 
	 * * If several abilities are valid choices,
	 * each has an equal chance of being chosen.
	 * 
	 * * Normally, the enemy isn't allowed to choose
	 * an ability which is on a scheduled wait.
	 * 
	 * * If the enemy is affected by taunt
	 * he is allowed to choose an ability
	 * which is on a scheduled wait
	 * 
	 * * Cooldown and scheduled wait for each action
	 * will decrease by 1 each round, without exceptions
	 * 
	 * </pre>
	 * 
	 * @return
	 */
	public Action chooseAction( Unit ally, Unit enemy, boolean first )
	{
		int random;
		int maxPrio;
		int enforceType = getModifier(Unit.ENFORCE_TYPE);
		Action chosen;
		
		maxPrio = assessMoves(ally, enemy);
		
		if ((enforceType == CC.ANY && maxPrio >= 0)
				|| (enforceType != CC.ANY && maxPrio >= -1))
		{
			do
			{
				random = (int)(Math.random() * moveset.length);
				chosen = moveset[random];
			}
			while (chosen.available == false || chosen.priority < maxPrio);
		}
		else if (attackMove.available
				&& (waitMove.available == false || attackMove.priority >= 0))
			chosen = attackMove;
		else
			chosen = waitMove;
		
		if (first) action = chosen;
		else secondAction = chosen;
		
		chosen.schedule();
		
		return chosen;
	}
	
	
	/**
	 * Readies all moves for use
	 */
	@Override
	public void prepareMoves()
	{
		for (Action move: moveset)
		{
			move.initialSchedule();
		}
	}
	
	
	/**
	 * Retrieves a move which should be used
	 * before the start of a battle
	 * and sets action to equal that move.
	 * Call again to retrieve the next move
	 * until there are none left.
	 * Returns true if a move was found
	 * 
	 * @return
	 */
	public boolean getPreparationMove()
	{
		String name;
		
		if (preparations.size() > 0)
		{
			name = preparations.remove(0);
			
			for (Action move: moveset)
			{
				if (name.equals(move.toString()))
				{
					action = move;
					action.schedule();
					return true;
				}
			}
		}
		return false;
	}
}
