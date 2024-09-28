package dröm;

import java.util.ArrayList;

import vn2.Database;
import vn2.Interpret;
import vn2.Responder;


/**
 * Controls the battle
 * 
 * @author alexi
 */
public class Battle extends Responder {

	public Unit hero;
	public NPCUnit enemy;
	public NPCUnit assistant;
	
	private Ability ability;
	private AbilityList abilityList;
	
	private String vnCode;
	
	private boolean ongoing;
	private boolean victory;
	private boolean hasAssistant;
	
	
	/**
	 * Creates the stage for a battle
	 * 
	 * @param interpret
	 * @param database
	 */
	public Battle( Interpret interpret, Database database )
	{
		super(interpret, database);
		
		ability = new Ability();
		abilityList = new AbilityList();
		
		hero = new Unit();
		enemy = new NPCUnit();
		assistant = null;
		
		clearVNCode();
	}
	
	
	/**
	 * Sends the VN-code to the interpreter in order to display information, 
	 * allow player input or resume the story.
	 * Clears the VN-code variable afterwards.
	 */
	private void sendVNCode()
	{
		interpret.createOver(vnCode);
		interpret.proceed();
		vnCode = "{s}\n";
	}
	
	
	/**
	 * Clears the VN-code
	 */
	private void clearVNCode()
	{
		vnCode = "{s}\n";
	}
	
	
	/**
	 * Sets the players ally
	 * 
	 * @param assistant
	 */
	private void setAssistant( NPCUnit assistant )
	{
		this.assistant = assistant;
		hasAssistant = true;
		interpret.proceed();
	}
	
	
	/**
	 * Removes the player ally
	 */
	private void removeAssistant()
	{
		database.setName("Allierad.Namn", "");
		database.setValue("Allierad.Grad", 0);
		hasAssistant = false;
		ability.removeAssistant();
		interpret.proceed();
	}
	
	
	/**
	 * Sets the enemy
	 * 
	 * @param enemy
	 */
	private void setEnemy( NPCUnit enemy )
	{
		this.enemy = enemy;
	}
	
	
	/**
	 * Sets the player
	 * 
	 * @param hero
	 */
	private void setHero( Unit hero )
	{
		this.hero = hero;
		interpret.proceed();
	}
	
	
	/**
	 * Writes information about the assistant or enemy moves.
	 * In order to write this info into the information window,
	 * add '{info}' only once to the vnCode.
	 * Then you can call this method multiple times.
	 * Finish with '{p}'.
	 * 
	 * @param unit
	 */
	private void writeMoveInfo( NPCUnit unit )
	{
		for (Action move: unit.moveset)
		{
			if (move.getType() != CC.EMPTY)
				vnCode += "{write,'" + move.present() + "','\n'}";
		}
		vnCode += "{write,'\n',''}";
	}
	
	
	/**
	 * Writes information about the hero or enemy active effects.
	 * 
	 * @param unit
	 */
	private void writeEffectInfo( Unit unit )
	{
		vnCode += "{write,'" + unit + "','\n'}";
		
		for (Effect effect: unit.effectList)
		{
			vnCode += "{write,'" + effect.present() + "','\n'}";
		}
		
		if (unit.effectList.size() == 0)
			vnCode += "{write,'(inga effekter)','\n'}";
		
		vnCode += "{write,'\n',''}";
	}


	/**
	 * Reads the players moveset
	 * and sets the warmup for all abilities.
	 */
	private void prepare()
	{
		hero.loadMoveset(abilityList);
		hero.prepareMoves();
		enemy.prepareMoves();
		if (hasAssistant) assistant.prepareMoves();
		ongoing = true;
		
		while (enemy.getPreparationMove())
			act(enemy, enemy.action);
	}
	
	
	/**
	 * Determines an enemy or assistant action
	 * 
	 * @param enemy
	 */
	private void selectAIAction( NPCUnit user, Unit ally, Unit target )
	{
		if (user.isUsingStorm() && user.canAct() && !user.allied)
		{
			user.action = abilityList.getMove("Tornado");
			if (user.doubleAction) user.secondAction = user.action;
		}
		else if (user.canAct())
		{
			user.chooseAction(ally, target, true);
			
			if (user.doubleAction)
			{
				user.checkMoves();
				user.chooseAction(ally, target, false);
			}
		}
		else
		{
			user.action = user.emptyMove;
			user.secondAction = user.emptyMove;
		}
	}
	
	
	/**
	 * Generates VN-code which allows the player to select a move
	 * 
	 * @param unit
	 */
	private void selectPlayerAction( Unit unit )
	{
		if (unit.isUsingStorm() && unit.canAct())
		{
			vnCode += "{name,Fas,'Agera'}{set,Val,-2}{p}\n";
			vnCode += "{return}{call}{p}\n";
		}
		else if (unit.canAct())
		{
			Action action;
			int choice;
			
			vnCode += "{info}";
			vnCode += enemy.stats.getName("Namn") + " " + enemy.stats.getValue("Hp")
			+ "/" + enemy.stats.getValue("Max hp");
			vnCode += "{write,'','\n'}";
			writeMoveInfo(enemy);
			
			if (hasAssistant)
			{
				vnCode += assistant.stats.getName("Namn");
				vnCode += "{write,'','\n'}";
				writeMoveInfo(assistant);
			}
			
			vnCode += "{p}\n";
			
			vnCode += "{name,Fas,'Agera'}{set,Val,-1}{p}\n";
			vnCode += "Välj ditt drag.\n";
			
			if (unit.attackMove.available)
				vnCode += "{ans}" + unit.attackMove.present() + "{set,Val,0}\n";
			else
				vnCode += "{ans}" + unit.attackMove.present() + "{set,Val,-1}\n";
			
			if (unit.waitMove.available)
				vnCode += "{ans}" + unit.waitMove.present() + "{set,Val,1}\n";
			else
				vnCode += "{ans}" + unit.waitMove.present() + "{set,Val,-1}\n";
			
			for (int i = 0; i < 4; i++)
			{
				action = unit.moveset[i];
				choice = i + 2;
				
				if (action.available && action.getType() != CC.EMPTY)
					vnCode += "{ans}" + action.present() + "{set,Val," + choice + "}\n";
				else if (action.getType() != CC.EMPTY)
					vnCode += "{ans}" + action.present() + "{set,Val,-1}\n";
			}
			vnCode += "{ans}Se effekter{find,1}";			
			vnCode += "{p}\n";
			vnCode += "{if}{has,Val,0}{return}{call}{p}\n";
			vnCode += "Ogiltigt drag. Försök igen.{find,0}{s}\n";
			vnCode += "Aktiva effekter.{info}";
			writeEffectInfo(hero);
			writeEffectInfo(enemy);
			vnCode += "{find,0}{p}\n";
		}
		else
		{
			vnCode += "{name,Fas,'Agera'}{set,Val,-1}{p}\n";
			vnCode += "{return}{call}{p}\n";
		}
	}
	
	
	/**
	 * All units selects their actions
	 */
	private void selectAction()
	{
		if (hasAssistant) assistant.checkMoves();
		enemy.checkMoves();
		hero.checkMoves();
		
		selectPlayerAction(hero);
		if (hasAssistant) selectAIAction(assistant, hero, enemy);
		selectAIAction(enemy, enemy, hero);
		
		sendVNCode();
	}
	
	
	/**
	 * Ends combat with player victory.
	 * Hands out rewards and resumes the game
	 */
	private void victory()
	{
		hero.removeEffects(ability, CC.ANY);
		ability.resetVNCode();
		database.setValue("Belöning", enemy.stats.getValue("Belöning"));
		
		vnCode += enemy + " har blivit besegrad!{p}\n";
		vnCode += "{info}{p}\n";
		vnCode += "{return}{open,'Erfarenhet'}{p}\n";		
		
		sendVNCode();
	}
	
	
	/**
	 * Ends combat with player loss
	 */
	private void defeat()
	{
		vnCode += "Du har blivit besegrad!{p}\n";
		vnCode += "Gå till menyn för att ladda eller starta ett nytt spel.{rewind,1}{p}";
		
		sendVNCode();
	}
	
	
	/**
	 * Ends the turn, counts down cooldown and proceeds to the next turn
	 */
	private void nextTurn()
	{
		hero.cooldown();
		enemy.cooldown();
		if (hasAssistant) assistant.cooldown();
		
		hero.countdown(ability);
		enemy.countdown(ability);
		ability.resetVNCode(); // Do not inform when abilities deactivate
		
		vnCode += "{p}Aktiva effekter.{info}";
		writeEffectInfo(hero);
		writeEffectInfo(enemy);
		vnCode += "{p}\n";
		
		vnCode += "{name,Fas,'Välj drag'}{return}{call}{p}\n";
		
		sendVNCode();
	}
	
	
	/**
	 * Activates recovery and poison effects
	 * 
	 * @param unit
	 */
	private void tickEffects( Unit unit )
	{
		int regen = unit.getModifier(Unit.RECOVERY);
		int poison = unit.getModifier(Unit.POISON);
		
		if (regen > 0)
		{
			unit.heal(regen);
			vnCode += unit + " regenererar " + regen + " hälsopoäng. ";
		}
		if (poison > 0)
		{
			unit.inflict(poison);
			vnCode += unit + " tar " + poison + " poängs skada från gift. ";
			checkOngoing();
		}
		vnCode += "{p}\n";
		
	}
	
	
	/**
	 * Checks for player victory or loss
	 */
	private void checkOngoing()
	{
		if (hero.stats.getValue("Hp") <= 0)
		{
			ongoing = false;
			victory = false;
		}
		else if (enemy.stats.getValue("Hp") <= 0)
		{
			ongoing = false;
			victory = true;
		}
		else
			ongoing = true;
	}
	
	
	/**
	 * Performs a specific unit action.
	 * Checks for victory
	 * 
	 * @param unit
	 * @param action
	 */
	private void act( Unit unit, Action action )
	{
		if (ongoing)
		{
			ability.activate(action, unit);
			vnCode += ability.getVNCode();
			checkOngoing();
		}
	}
	
	
	/**
	 * Sorts the elements in a list in descending order and returns an array
	 * representing the new order of the elements.
	 * The list you want to sort can only contain positive elements and -1.
	 * -1 would mean a undefined value and will be represented as -1 in the returned order
	 * This method will manipulate the list which is sorted.
	 * In case of a ties, lower index elements will be placed
	 * earlier than higher index elements
	 * 
	 * @param prio
	 * @return
	 */
	private ArrayList<Integer> sort( ArrayList<Integer> prio )
	{
		int element;
		int index;
		
		ArrayList<Integer> order = new ArrayList<Integer>(4);
		
		for (int i = 0; i < 4; i++)
		{
			if (prio.get(i) != -1) order.add(i);
			else order.add(-1);
			
			prio.set(i, (1 + prio.get(i)) * prio.size() - i);
		}
		
		for (int i = 0; i < 3; i++)
		{
			element = prio.get(i);
			index = order.get(i);
			
			for (int j = i + 1; j < 4; j++)
			{
				if (prio.get(j) > element)
				{
					prio.set(i, prio.get(j));
					prio.set(j, element);
					order.set(i, order.get(j));
					order.set(j, index);
					element = prio.get(i);
					index = order.get(i);
				}
			}
		}
		return order;
	}
	
	
	/**
	 * Performs the selected actions of all units.
	 * Checks for victory and proceeds accordingly
	 */
	private void act()
	{
		int heroPrio = CC.getPriority(hero.action.getType());
		int enemyPrio = CC.getPriority(enemy.action.getType());
		int assistantPrio = (hasAssistant) ? CC.getPriority(assistant.action.getType()) : -1;
		int secondPrio = (enemy.doubleAction) ? CC.getPriority(enemy.secondAction.getType()) : -1;
		
		ArrayList<Integer> prio = new ArrayList<Integer>(4);
		ArrayList<Integer> order;
		
		prio.add(assistantPrio);
		prio.add(heroPrio);
		prio.add(enemyPrio);
		prio.add(secondPrio);
		
		order = sort(prio);
		
		for (int i = 0; i < 4; i++)
		{
			switch (order.get(i))
			{
				case 0: act(assistant, assistant.action); break;
				case 1: act(hero, hero.action); break;
				case 2: act(enemy, enemy.action); break;
				case 3: act(enemy, enemy.secondAction); break;
			}
		}
		
		if (ongoing) tickEffects(hero);
		if (ongoing) tickEffects(enemy);
		
		if (ongoing) nextTurn();
		else if (victory) victory();
		else defeat();
	}
	
	
	/**
	 * Reads player input from the database in order to find
	 * the selected move
	 */
	private void readPlayerAction()
	{
		int choice = database.getValue("Val");
		
		switch (choice)
		{
			case -2: hero.action = abilityList.getMove("Tornado"); break;
			case -1: hero.action = hero.emptyMove; break;
			case 0: hero.action = hero.attackMove; break;
			case 1: hero.action = hero.waitMove; break;
			case 2: hero.action = hero.moveset[0]; break;
			case 3: hero.action = hero.moveset[1]; break;
			case 4: hero.action = hero.moveset[2]; break;
			case 5: hero.action = hero.moveset[3]; break;
		}
		hero.action.startCooldown();
	}
	
	
	/**
	 * <pre>
	 * Should be called in response to the VN command 'Call'.
	 * Initiates an action based on the 'Fas' variable in the database.
	 * This is the starting point for all actions in Battle.
	 * 
	 * Available values for 'Fas' includes:
	 * 
	 * Skapa: creates a new hero using the 'Main' attribute.
	 * Call once at the start of the game.
	 * 
	 * Assistent: creates a new assistant using the 'Allierad' attribute.
	 * Call once when an ally joins.
	 * 
	 * Solo: removes the assistant.
	 * 
	 * Strid: creates a new enemy using the 'Fiende' attribute.
	 * Starts a battle against the created enemy.
	 * 
	 * Välj drag: all combatants selects their next move.
	 * Called by the Battle VN code.
	 * 
	 * Agera: all combatants carries out their selected moves.
	 * End the turn or the battle.
	 * Called by the Battle VN code.
	 * 
	 * </pre>
	 */
	public void respond()
	{
		String phase = database.getName("Fas");
		
		switch (phase)
		{
			case "Skapa": setHero(new Unit(database.getAttribute("Main"))); break;
			case "Assistent":
			{
				if (database.getName("Allierad.Namn").equals(""))
					removeAssistant();
				else
					setAssistant(new NPCUnit(database.getName("Allierad.Namn"),
						database.getValue("Allierad.Grad"), hero, abilityList));
				break;
			}
					
			case "Solo": removeAssistant(); break;
			case "Strid":
			{
				setEnemy(new NPCUnit(database.getName("Fiende.Namn"),
						database.getValue("Fiende.Grad"), abilityList));
				ability.addUnits(hero, enemy, assistant);
				prepare();
				selectAction();
				break;
			}
			case "Välj drag": selectAction(); break;
			case "Agera": readPlayerAction(); act(); break;
		}
	}
}
