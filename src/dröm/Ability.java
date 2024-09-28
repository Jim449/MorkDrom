package dr�m;


/**
 * Used for performing moves and abilities.
 * After an action has been performed,
 * information about that action is stored as VN-code.
 * 
 * @author alexi
 */
public class Ability {

	private Unit hero;
	private NPCUnit enemy;
	private NPCUnit assistant;
	
	private String vnCode;
	
	
	/**
	 * Resets the VN-code, removing messages
	 * about performed actions
	 */
	public void resetVNCode()
	{
		vnCode = "{s}\n";
	}
	
	
	/**
	 * Creates a new Ability
	 */
	public Ability()
	{
		resetVNCode();
	}
	
	
	/**
	 * Sets the combatants in the current battle
	 * 
	 * @param hero
	 * @param enemy
	 * @param assistant
	 */
	public void addUnits( Unit hero, NPCUnit enemy, NPCUnit assistant )
	{
		this.hero = hero;
		this.enemy = enemy;
		this.assistant = assistant;
	}
	
	
	public void removeAssistant()
	{
		assistant = null;
	}
	
	
	/**
	 * Performs a normal attack
	 * 
	 * @param user
	 * @param target
	 */
	private void attack( Unit user, Unit target )
	{
		int damage = user.getAttack(target);
		int recoil = target.getModifier(Unit.RECOIL);
		
		vnCode += user + " anfaller! ";
		
		if (damage > 0 && user.willWork(CC.ATTACK, target))
		{
			target.inflict(damage);
			vnCode += target + " tar " + damage + " po�ngs skada! ";
			
			if (recoil > 0 && (user.player || !user.allied))
			{
				user.inflict(recoil);
				vnCode += user + " tar " + recoil + " skada i rekyl. ";
			}
			vnCode += "{p}\n";
		}
		else
			vnCode += "Men det hade ingen effekt!{p}\n";
	}
	
	
	/**
	 * The unit skips his action
	 * 
	 * @param user
	 */
	private void wait( Unit user )
	{
		vnCode += user + " v�ntar.{p}\n";
	}
	
	
	/**
	 * An immobilized unit performs an empty action
	 * 
	 * @param user
	 */
	private void emptyMove( Unit user )
	{
		vnCode += user + " kan inte agera!{p}\n";
	}
	
	
	/**
	 * Inflicts damage to the target
	 * 
	 * @param user
	 * @param target
	 * @param power
	 */
	private boolean inflict( Unit user, Unit target, int power )
	{
		if (power > 0 && (user.willWork(user.action.getType(), target)))
		{
			target.inflict(power);
			vnCode += target + " tar " + power + " po�ngs skada! ";
			return true;
		}
		else
		{
			vnCode += "Men det hade ingen effekt! ";
			return false;
		}	
	}
	
	
	/**
	 * Drains health from the target
	 * 
	 * @param user
	 * @param target
	 * @param power
	 */
	private boolean absorb( Unit user, Unit target, int power )
	{
		int damage;
		
		if (power > 0 && user.willWork(CC.ATTACK, target))
		{
			damage = target.inflict(power);
			user.heal(damage);
			vnCode += user + " absorberar " + damage + " h�lsopo�ng fr�n " + target + "! ";
			return true;
		}
		else
		{
			vnCode += "Men det hade ingen effekt! ";
			return false;
		}
	}
	
	
	/**
	 * Heals damage from the user
	 * 
	 * @param user
	 * @param power
	 */
	private boolean heal( Unit user, int power )
	{
		if (power > 0 && user.willWork(CC.HEAL, user))
		{
			user.heal(power);
			vnCode += user + " �terf�r " + power + " h�lsopo�ng! ";
			return true;
		}
		else
		{
			vnCode += "Men det hade ingen effekt! ";
			return false;
		}
	}
	
	
	/**
	 * Increases any stat
	 * 
	 * @param user
	 * @param power
	 * @param parameter
	 */
	private boolean buff( Unit user, int power, String parameter )
	{
		if (power > 0 && user.willWork(CC.BUFF, user))
		{
			user.modifier.addValue(parameter, power);
			vnCode += user + " f�r +" + power + "% " + parameter + "! ";
			return true;
		}
		else if (power < 0)
		{
			user.modifier.addValue(parameter, power);
			return false;
		}
		else
		{
			vnCode += "Men det hade ingen effekt! ";
			return false;
		}
	}
	
	
	/**
	 * Decreases any stat
	 * 
	 * @param user
	 * @param power
	 * @param parameter
	 */
	private boolean debuff( Unit user, Unit target, int power, String parameter, boolean sureHit )
	{
		if (power > 0 && (sureHit || user.willWork(CC.DEBUFF, target)))
		{
			target.modifier.addValue(parameter, 0 - power);
			vnCode += target + " f�rlorar " + power + "% " + parameter + "! ";
			return true;
		}
		else if (power < 0)
		{
			target.modifier.addValue(parameter, 0 - power);
			return false;
		}
		else
		{
			vnCode += "Men det hade ingen effekt! ";
			return false;
		}
	}
	
	
	/**
	 * Increases a stat by a fixed value
	 * 
	 * @param user
	 * @param power
	 * @param parameter
	 * @return
	 */
	private boolean buffAbsolute( Unit user, int power, String parameter )
	{
		if (power > 0 && user.willWork(CC.BUFF, user))
		{
			user.modifier.addValue(parameter, power);
			vnCode += user + " f�r +" + power + " i " + parameter + "! ";
			return true;
		}
		else if (power < 0)
		{
			user.modifier.addValue(parameter, power);
			return false;
		}
		else
		{
			vnCode += "Men det hade ingen effekt! ";
			return false;
		}
	}
	
	
	/**
	 * Increases an enemy stat by a fixed value
	 * 
	 * @param user
	 * @param power
	 * @return
	 */
	private boolean debuffAbsolute( Unit user, Unit target, int power, String parameter )
	{
		if (power > 0 && user.willWork(CC.DEBUFF, target))
		{
			target.modifier.addValue(parameter, power);
			vnCode += target + " �drar sig en " + parameter + " med " + power + " po�ngs styrka! ";
			return true;
		}
		else if (power < 0)
		{
			target.modifier.addValue(parameter, power);
			return false;
		}
		else
		{
			vnCode += "Men det hade ingen effekt! ";
			return false;
		}
	}
	
	
	/**
	 * Forces the target to only use moves of a specific type
	 * 
	 * @param user
	 * @param target
	 * @param power
	 * @param type
	 */
	private boolean manipulate( Unit user, Unit target, int power, int type, boolean selfInflicted )
	{
		if (power > 0 && (selfInflicted || user.willWork(CC.MANIPULATION, target)))
		{
			target.removeEffects(this, CC.MANIPULATION);
			target.enforce(type);
			
			switch (type)
			{
				case CC.ATTACK: vnCode += target + " tvingas till att anv�nda offensiva drag! "; break;
				case CC.DEBUFF: vnCode += target + " tvingas till att anv�nda f�rsvagande drag! "; break;
				case CC.BUFF: vnCode += target + " tvingas till att anv�nda f�rst�rkande drag! "; break;
			}
			return true;
		}
		else if (power < 0)
		{
			target.removeEnforce();
			return false;
		}
		else
		{
			vnCode += "Men " + target + " �r immun mot manipulation! ";
			return false;
		}
	}
	
	
	/**
	 * Attempts to counter the targets move.
	 * If the move has the correct type, it is blocked
	 * and the target takes damage
	 * 
	 * @param user
	 * @param target
	 * @param power
	 * @return
	 */
	private boolean counter( Unit user, Unit target, int power, int counteredType )
	{
		if (power > 0 && user.willCounter(counteredType, target))
		{
			target.modifier.setValue(Unit.COUNTER, 1);
			vnCode += user + " kontrar " + target + "! ";
			inflict(user, target, power);
			return true;
		}
		else
		{
			vnCode += user + " misslyckas med att kontra " + target + "! ";
			return false;
		}
	}
	
	
	/**
	 * <pre>
	 * Grants a merit which has a boolean value.
	 * Type can be one out of:
	 * * DEBUFF_IMMUNITY: Immunity to debuffs
	 * * DISPEL_IMMUNITY: Immunity to buff removal
	 * * MANIPULATION_IMMUNITY: Immunity to manipulation
	 * * EVASION: Immunity to everything
	 * * SURE_HIT: Action never fails
	 * * PIERCE: Ignore defense
	 * * INVISIBILITY: Immunity to everything, self stun
	 * </pre>
	 * 
	 * @param user
	 * @param power
	 * @param type
	 * @return
	 */
	private boolean addMerit( Unit user, int power, String type )
	{
		if (power > 0 && user.willWork(CC.BUFF, user))
		{
			user.modifier.addValue(type, 1);
			
			switch (type)
			{
				case Unit.DEBUFF_IMMUNITY:
					vnCode += user + " �r immun mot f�rsvagningar! "; return true;
				case Unit.DISPEL_IMMUNITY:
					vnCode += user + " skyddar sina f�rst�rkande effekter! "; return true;
				case Unit.MANIPULATION_IMMUNITY:
					vnCode += user + " �r immun mot manipulation! "; return true;
				case Unit.EVASION:
					vnCode += user + " �r skyddad mot alla hot! "; return true;
				case Unit.SURE_HIT:
					vnCode += user + " kan inte misslyckas med sina drag! "; return true;
				case Unit.PIERCE:
					vnCode += user + " ignorerar motst�ndarens f�rsvar! "; return true;
				case Unit.INVISIBILITY:
					vnCode += user + " f�rsvinner in i m�rkret... "; return true;
				case Unit.STORM:
					vnCode += user + " frammanar en storm! "; return true;
				case Unit.SURE_DEBUFF:
					vnCode += user + " ignorerar immunitet mot f�rsvagningar! "; return true;
				default: return true;
			}
		}
		else if (power < 0)
		{
			user.modifier.addValue(type, 0 - 1);
			return false;
		}
		else
		{
			vnCode += "Men det hade ingen effekt! ";
			return false;
		}
	}
	
	
	/**
	 * <pre>
	 * Imposes a demerit which has a boolean value.
	 * Type can be one out of:
	 * * BUFF: Immunity to buffs
	 * * CLEANSE: Immunity to debuff removal
	 * * STUN: Stuns the target
	 * * BLIND: Causes attacks and weakening moves to fail
	 * </pre>
	 * 
	 * @param user
	 * @param power
	 * @param type
	 * @return
	 */
	private boolean addDemerit( Unit user, Unit target, int power, String type )
	{
		if (power > 0
			&& ((type.equals(Unit.STUN) && user.willWork(CC.MANIPULATION, target))
			|| (type.equals(Unit.STUN) == false && user.willWork(CC.DEBUFF, target))))
		{
			target.modifier.addValue(type, 1);
			
			switch (type)
			{
				case Unit.BUFF_IMMUNITY:
					vnCode += target + " �r immun mot f�rst�rkande effekter! "; return true;
				case Unit.CLEANSE_IMMUNITY:
					vnCode += target + " kan inte bli av med sina f�rsvagande effekter! "; return true;
				case Unit.STUN:
					vnCode += target + " �r f�rlamad! "; return true;
				case Unit.BLIND:
					vnCode += target + " kan inte se motst�ndaren! "; return true;
				default: return true;
			}
		}
		else if (power < 0)
		{
			target.modifier.addValue(type, 0 - 1);
			return false;
		}
		else
		{
			vnCode += "Men det hade ingen effekt! ";
			return false;
		}
	}
	
	
	/**
	 * Removes all negative effects from the user
	 * 
	 * @param user
	 * @param power
	 * @return
	 */
	private boolean cleanse( Unit user, int power )
	{
		if (power > 0 && user.willWork(CC.CLEANSE, user))
		{
			vnCode += user + " g�r sig av med alla negativa effekter!{p}\n ";
			user.removeEffects(this, CC.DEBUFF);
			return true;
		}
		else
		{
			vnCode += "Men det hade ingen effekt!" ;
			return false;
		}
	}
	
	
	/**
	 * Removes all positive effects from the target
	 * 
	 * @param user
	 * @param power
	 * @return
	 */
	private boolean dispel( Unit user, Unit target, int power, boolean sureHit )
	{
		if (power > 0 && (sureHit || user.willWork(CC.DISPEL, target)))
		{
			vnCode += user + " tar bort alla positiva effekter fr�n " + target + "!{p}\n ";
			target.removeEffects(this, CC.BUFF);
			return true;
		}
		else
		{
			vnCode += "Men det hade ingen effekt! ";
			return false;
		}
	}
	
	
	/**
	 * Reduces the cooldown of all the targets skills.
	 * Unlike most effects, cooldown reduction
	 * is not applied to the party, only to the unit
	 * 
	 * @param user
	 * @param power
	 * @param rush
	 * @return
	 */
	private boolean countdown( Unit user, int power, boolean rush )
	{
		if (power > 0 && user.willWork(CC.BUFF, user))
		{
			for (Action move: user.moveset)
			{
				move.countdown(power);
				
				if (rush) move.advance();
			}
						
			vnCode += user + " snabbar upp avkylningen av sina f�rm�gor med " + power + " steg! ";
			
			if (rush)
				vnCode += user + " �r ot�lig att anv�nda sina f�rm�gor! ";
			
			return true;
		}
		else
		{
			vnCode += "Men det hade ingen effekt!" ;
			return false;
		}
	}
	
	
	/**
	 * The unit performs any action
	 * 
	 * @param action
	 * @param unit
	 */
	public void activate( Action action, Unit user )
	{
		Unit target;
		int power;
		
		if (user.allied && action.isSelfTargeting()) target = hero;
		else if (user.allied) target = enemy;
		else if (action.isSelfTargeting()) target = enemy;
		else target = hero;
		
		if (user.hasModifier(Unit.COUNTER))
		{
			vnCode += user + " kan inte agera!{p}\n";
			return;
		}
		
		switch (action.toString())
		{
			case "Anfall": attack(user, target); return;
			case "V�nta": wait(user); return;
			case "": emptyMove(user); return;
		}
		
		vnCode += user + " anv�nder " + action + "! ";
		
		switch (action.toString())
		{
			// NORMALA F�RM�GOR
			// ELD
			case "Inferno":
			{
				inflict(user, target, user.getPower(target, 4, false));
				break;
			}
			case "Provokation":
			{
				if (manipulate(user, target, action.getPower(), CC.ATTACK, false))
					target.giveManipulation(action.getEffect());
				break;
			}
			case "Raseri":
			{
				if (buff(target, action.getPower(), CC.STR))
				{
					manipulate(user, target, 1, CC.ATTACK, true);
					target.giveSelfManipulation(action.getEffect());
				}
				break;
			}
			case "Eldsk�ld":
			{
				power = user.getRecoil(target, 2);
				if (buffAbsolute(target, power, Unit.RECOIL))
					target.giveEffect(action.getEffect(power));
				break;
			}
			// VATTEN
			case "Invasiv kyla":
			{
				inflict(user, target, user.getPower(target, 3, true));
				break;
			}
			case "L�kekraft":
			{
				heal(target, user.getHealing(4));
				break;
			}
			case "Fokus":
			{
				if (buff(target, action.getPower(), CC.INT))
					target.giveEffect(action.getEffect());
				break;
			}
			case "Frost":
			{
				if (debuff(user, target, action.getPower(), CC.STR, false))
					target.giveEffect(action.getEffect());
				break;
			}
			// LUFT
			case "Vindsk�ra": 
			{
				inflict(user, target, user.getPower(target, 3, false));
				break;
			}
			case "Vindsk�ld":
			{
				if (addMerit(target, action.getPower(), Unit.EVASION))
					target.giveEffect(action.getEffect());
				break;
			}
			case "Frihet":
			{
				cleanse(target, 1);
				break;
			}
			case "Inspiration":
			{
				if (user.allied)
				{
					countdown(hero, 1, false);
					if (assistant != null) countdown(assistant, 1, false);
				}
				else
					countdown(enemy, 2, false);
				break;
			}
			// JORD
			case "Skalv":
			{
				if (inflict(user, target, user.getPower(target, 3, false)))
				{
					if (addDemerit(user, target, action.getPower(), Unit.STUN))
						target.giveManipulation(action.getEffect());
				}
				break;
			}
			case "Styrka":
			{
				if (buff(target, action.getPower(), CC.STR))
					target.giveEffect(action.getEffect());
				break;
			}
			case "Uth�llighet":
			{
				if (buff(target, action.getPower(), CC.DEF))
					target.giveEffect(action.getEffect());
				break;
			}
			case "Viljestyrka":
			{
				if (addMerit(target, action.getPower(), Unit.MANIPULATION_IMMUNITY))
					target.giveEffect(action.getEffect());
				break;
			}
			// LJUS
			case "Blixt":
			{
				inflict(user, target, user.getPower(target, 3, false));
				break;
			}
			case "Frid":
			{
				if (manipulate(user, target, action.getPower(), CC.BUFF, false))
					target.giveManipulation(action.getEffect());
				break;
			}
			case "Illuminering":
			{
				if (addMerit(target, action.getPower(), Unit.SURE_HIT))
					target.giveEffect(action.getEffect());
				break;
			}
			case "Genomsk�dning":
			{
				if (addMerit(target, action.getPower(), Unit.PIERCE))
					target.giveEffect(action.getEffect());
				break;
			}
			// M�RKER
			case "M�rker":
			{
				if (addMerit(target, action.getPower(), Unit.INVISIBILITY))
					target.giveEffect(action.getEffect());
				break;
			}
			case "Tomhet":
			{
				dispel(user, target, 1, false);
				break;
			}
			case "Bitterhet":
			{
				if (manipulate(user, target, action.getPower(), CC.DEBUFF, false))
					target.giveManipulation(action.getEffect());
				break;
			}
			case "Hoppl�shet":
			{
				counter(user, target, user.getPower(target, 3, false), CC.BUFF);
				break;
			}
			// TR�
			case "Absorption":
			{
				// has to heal the hero
				if (user.allied)
					absorb(hero, target, user.getPower(target, 2, false));
				else
					absorb(user, target, user.getPower(target, 2, false));
				break;
			}
			case "Regenerering":
			{
				power = user.getRegeneration(1);
				if (buffAbsolute(target, power, Unit.RECOVERY))
					target.giveEffect(action.getEffect(power));
				break;
			}
			case "Kontring":
			{
				counter(user, target, user.getPower(target, 3, false), CC.ATTACK);
				break;
			}
			case "Sj�lvbevarelsedrift":
			{
				if (manipulate(user, target, action.getPower(), CC.BUFF, false))
					target.giveManipulation(action.getEffect());
				break;
			}
			// METALL
			case "F�rgiftning":
			{
				power = user.getPoison(target, 1);
				if (debuffAbsolute(user, target, power, Unit.POISON))
					target.giveEffect(action.getEffect(power));
				break;
			}
			case "Erodering":
			{
				if (debuff(user, target, action.getPower(), CC.DEF, false))
					target.giveEffect(action.getEffect());
				break;
			}
			case "Reflektion":
			{
				counter(user, target, user.getPower(target, 3, false), CC.DEBUFF);
				break;
			}
			case "Fanatism":
			{
				if (addMerit(target, 1, Unit.DISPEL_IMMUNITY))
				{
					target.giveEffect(action.getEffect());
					// buff(target, action.getPower(), CC.STR);
				}
				break;
			}
			// SPECIALF�RM�GOR
			// ELD
			case "Rasande eld":
			{
				inflict(user, target, user.getPower(target, 8, false));
				break;
			}
			case "Nova":
			{
				inflict(user, target, user.getPower(target, 8, false));
				
				if (debuff(user, user, action.getPower(), CC.DEF, true))
					user.giveEffect(action.getEffect());
				break;
			}
			case "Urkraft":
			{
				if (buff(target, action.getPower(), CC.STR))
					target.giveEffect(action.getEffect());
				break;
			}
			// VATTEN
			case "F�rfrysning":
			{
				power = user.getPoison(target, 1);
				
				if (addDemerit(user, target, 1, Unit.STUN))
				{
					debuffAbsolute(user, target, power, Unit.POISON);
					target.giveManipulation(action.getEffect(power));
				}
				break;
			}
			case "Meditation":
			{
				power = user.getRegeneration(2);
				
				if (buffAbsolute(target, power, Unit.RECOVERY))
				{
					addMerit(target, 1, Unit.DISPEL_IMMUNITY);
					target.giveEffect(action.getEffect(power));
				}
				break;
			}
			case "Illusion":
			{
				if (manipulate(user, target, 1, CC.ATTACK, false))
				{
					addDemerit(user, target, 1, Unit.BLIND);
					target.giveManipulation(action.getEffect());
				}
				break;
			}
			// LUFT
			case "Storm":
			{
				if (addMerit(target, 1, Unit.STORM))
				{
					addMerit(target, 1, Unit.MANIPULATION_IMMUNITY);
					target.giveSelfManipulation(action.getEffect());
				}
				break;
			}
			case "Tornado":
			{
				inflict(user, target, user.getPower(target, 6, false));
				break;
			}
			case "Hast":
			{
				if (user.allied)
				{
					countdown(hero, 1, false);
					if (assistant != null) countdown(assistant, 1, true);
				}
				else
					countdown(enemy, 2, true);
				break;
			}
			// JORD
			case "Jordb�vning":
			{
				if (inflict(user, target, user.getPower(target, 10, false)))
				{
					if (addDemerit(user, target, action.getPower(), Unit.STUN))
						target.giveManipulation(action.getEffect());
				}
				break;
			}
			case "Stenskinn":
			{
				if (buff(target, action.getPower(), CC.DEF))
					target.giveEffect(action.getEffect());
				break;
			}
			case "Orubblighet":
			{
				if (addMerit(target, 1, Unit.MANIPULATION_IMMUNITY))
					target.giveEffect(action.getEffect());
				break;
			}
			// LJUS
			case "Mental chock":
			{
				inflict(user, target, user.getPower(target, 5, true));
				break;
			}
			case "Prediktion":
			{
				if (addMerit(target, 1, Unit.SURE_HIT))
				{
					addMerit(target, 1, Unit.PIERCE);
					target.giveEffect(action.getEffect());
				}
				break;
			}
			case "Identifiering":
			{
				if (addMerit(target, 1, Unit.SURE_DEBUFF))
				{
					addMerit(target, 1, Unit.SURE_HIT);
					target.giveEffect(action.getEffect());
				}
				break;
			}
			// M�RKER
			case "M�rk vind":
			{
				dispel(user, target, 1, false);
				inflict(user, target, user.getPower(target, 4, false));
				break;
			}
			case "Depression":
			{
				if (addDemerit(user, target, 1, Unit.BUFF_IMMUNITY))
					target.giveEffect(action.getEffect());
				break;
			}
			// TR�
			case "Anpassning":
			{
				if (buff(target, 40, CC.STR))
				{
					buff(target, 20, CC.DEF);
					target.giveEffect(action.getEffect());
				}
				break;
			}
			case "�terh�mtning":
			{
				heal(target, user.getHealing(3));
				cleanse(target, 1);
				break;
			}
			case "F�rt�ring":
			{
				// has to heal the hero
				if (user.allied)
					absorb(hero, target, user.getPower(target, 4, false));
				else
					absorb(user, target, user.getPower(target, 4, false));
				break;
			}
			case "Kanalisering":
			{
				power = user.getRegeneration(1);
				
				if (buff(target, 100, CC.DEF))
				{
					buffAbsolute(target, power, Unit.RECOVERY);
					target.giveEffect(action.getEffect(power));
				}
				break;
			}
			case "Harmoni":
			{
				// The order might seem strange
				// First, check that the user isn't immune to buffs
				// The self-manipulation will always work
				// Second, check that the opponent can be manipulated
				// Otherwise he shouldn't be weakened either
				if (user.allied)
				{
					if (buff(hero, action.getPower(), CC.INT))
					{
						manipulate(hero, hero, 1, CC.BUFF, true);
						hero.giveSelfManipulation(action.getEffect());
					}
					if (manipulate(hero, enemy, 1, CC.BUFF, false))
					{
						buff(enemy, action.getPower(), CC.INT);
						enemy.giveSelfManipulation(action.getEffect());
					}
				}
				else
				{
					if (buff(enemy, action.getPower(), CC.INT))
					{
						manipulate(enemy, enemy, 1, CC.BUFF, true);
						enemy.giveSelfManipulation(action.getEffect());
					}
					if (manipulate(enemy, hero, 1, CC.BUFF, false))
					{
						buff(hero, action.getPower(), CC.INT);
						hero.giveSelfManipulation(action.getEffect());
					}
				}
				break;
			}
			// METALL
			case "�verl�gsenhet":
			{
				if (addMerit(target, 1, Unit.DEBUFF_IMMUNITY))
				{
					addMerit(target, 1, Unit.DISPEL_IMMUNITY);
					target.giveEffect(action.getEffect());
				}
				break;
			}
			case "Djup infektion":
			{
				if (addDemerit(user, target, 1, Unit.CLEANSE_IMMUNITY))
				{
					debuff(user, target, action.getPower(), CC.DEF, false);
					target.giveEffect(action.getEffect());
				}
				break;
			}
			// KAUSALITET
			case "Svarta vingar":
			{
				if (!target.hasEffect(CC.BUFF))
					inflict(user, target, user.getPower(target, 100, false));
				else
					inflict(user, target, 0);
				break;
			}
			case "Os�rbarhet":
			{
				if (addMerit(target, 1, Unit.EVASION))
					target.giveEffect(action.getEffect());
				break;
			}
			case "V�rldslighet":
			{
				if (addDemerit(user, target, 1, Unit.BUFF_IMMUNITY))
					target.giveEffect(action.getEffect());
				break;
			}
			// OVERKLIGHET
			case "Transformation":
			{
				if (buff(target, action.getPower(), CC.STR))
				{
					buff(target, action.getPower(), CC.INT);
					target.giveEffect(action.getEffect());
				}
				break;
			}
			case "Mardr�m":
			{
				inflict(user, target,
						user.getPower(target, 6 * (1 - user.countEffects(CC.BUFF)), false));
				break;
			}
			case "Andeform":
			{
				if (buff(target, action.getPower(), CC.DEF))
					target.giveEffect(action.getEffect());
				break;
			}
		}
		
		vnCode += "{p}\n";
	}
	
	
	public void deactivate( Effect effect, Unit unit )
	{
		vnCode += unit + " �r inte l�ngre p�verkad av " + effect + "!{p}\n";
		
		switch (effect.toString())
		{
			// I'm just going to write 0 - effect.getPower() everywhere.
			// It's mostly unnecessary since I can simply write -1 for most effects.
			// But if a move gives like 125% strength,
			// I only want to write that number in a single place
			// so I can easily tune it
			
			// NORMALA F�RM�GOR
			// ELD
			case "Provokation":
				manipulate(unit, unit, 0 - effect.getPower(), CC.ATTACK, false); break;
			case "Raseri":
			{
				buff(unit, 0 - effect.getPower(), CC.STR);
				manipulate(unit, unit, -1, CC.ATTACK, false);
				break;
			}
			case "Eldsk�ld":
				buffAbsolute(unit, 0 - effect.getPower(), Unit.RECOIL); break;
			// VATTEN
			case "Fokus":
				buff(unit, 0 - effect.getPower(), CC.INT); break;
			case "Frost":
				debuff(unit, unit, 0 - effect.getPower(), CC.STR, false); break;
			// LUFT
			case "Vindsk�ld":
				addMerit(unit, 0 - effect.getPower(), Unit.EVASION); break;
			// JORD
			case "Skalv":
				addDemerit(unit, unit, 0 - effect.getPower(), Unit.STUN); break;
			case "Styrka":
				buff(unit, 0 - effect.getPower(), CC.STR); break;
			case "Uth�llighet":
				buff(unit, 0 - effect.getPower(), CC.DEF); break;
			case "Viljestyrka":
				addMerit(unit, 0 - effect.getPower(), Unit.MANIPULATION_IMMUNITY); break;
			// LJUS
			case "Frid":
				manipulate(unit, unit, 0 - effect.getPower(), CC.BUFF, false); break;
			case "Illuminering":
				addMerit(unit, 0 - effect.getPower(), Unit.SURE_HIT); break;
			case "Genomsk�dning":
				addMerit(unit, 0 - effect.getPower(), Unit.PIERCE); break;
			// M�RKER
			case "M�rker":
				addMerit(unit, 0 - effect.getPower(), Unit.INVISIBILITY); break;
			case "Bitterhet":
				manipulate(unit, unit, 0 - effect.getPower(), CC.DEBUFF, false); break;
			// TR�
			case "Regenerering":
				buffAbsolute(unit, 0 - effect.getPower(), Unit.RECOVERY); break;
			case "Sj�lvbevarelsedrift":
				manipulate(unit, unit, 0 - effect.getPower(), CC.BUFF, false); break;
			// METALL
			case "F�rgiftning":
				debuffAbsolute(unit, unit, 0 - effect.getPower(), Unit.POISON); break;
			case "Erodering":
				debuff(unit, unit, 0 - effect.getPower(), CC.DEF, false); break;
			case "Fanatism":
			{
				addMerit(unit, -1, Unit.DISPEL_IMMUNITY);
				// buff(unit, 0 - effect.getPower(), CC.STR);
				break;
			}
			// SPECIALF�RM�GOR
			// ELD
			case "Nova":
				debuff(unit, unit, 0 - effect.getPower(), CC.DEF, true); break;
			case "Urkraft":
				buff(unit, 0 - effect.getPower(), CC.STR); break;
			// VATTEN
			case "F�rfrysning":
			{
				addDemerit(unit, unit, -1, Unit.STUN);
				debuffAbsolute(unit, unit, 0 - effect.getPower(), Unit.POISON);
				break;
			}
			case "Meditation":
			{
				buffAbsolute(unit, 0 - effect.getPower(), Unit.RECOVERY);
				addMerit(unit, -1, Unit.DISPEL_IMMUNITY);
				break;
			}
			case "Illusion":
			{
				manipulate(unit, unit, -1, CC.ATTACK, false);
				addDemerit(unit, unit, -1, Unit.BLIND);
				break;
			}
			// LUFT
			case "Storm":
			{
				addMerit(unit, -1, Unit.STORM);
				addMerit(unit, -1, Unit.MANIPULATION_IMMUNITY);
				break;
			}
			// JORD
			case "Jordb�vning":
				addDemerit(unit, unit, -1, Unit.STUN); break;
			case "Stenskinn":
				buff(unit, 0 - effect.getPower(), CC.DEF); break;
			case "Orubblighet":
				addMerit(unit, -1, Unit.MANIPULATION_IMMUNITY); break;
			// LJUS
			case "Prediktion":
			{
				addMerit(unit, -1, Unit.SURE_HIT);
				addMerit(unit, -1, Unit.PIERCE);
				break;
			}
			case "Identifiering":
			{
				addMerit(unit, -1, Unit.SURE_DEBUFF);
				addMerit(unit, -1, Unit.SURE_HIT);
				break;
			}
			// M�RKER
			case "Depression":
				addDemerit(unit, unit, -1, Unit.BUFF_IMMUNITY); break;
			// TR�
			case "Anpassning":
			{
				buff(unit, -40, CC.STR);
				buff(unit, -20, CC.DEF);
				break;
			}
			case "Kanalisering":
			{
				buff(unit, -100, CC.DEF);
				buffAbsolute(unit, 0 - effect.getPower(), Unit.RECOVERY);
				break;
			}
			case "Harmoni":
			{
				buff(unit, 0 - effect.getPower(), CC.INT);
				manipulate(unit, unit, -1, CC.BUFF, false);
				break;
			}
			// METALL
			case "�verl�gsenhet":
			{
				addMerit(unit, -1, Unit.DEBUFF_IMMUNITY);
				addMerit(unit, -1, Unit.DISPEL_IMMUNITY);
				break;
			}
			case "Djup infektion":
			{
				addDemerit(unit, unit, -1, Unit.BUFF_IMMUNITY);
				debuff(unit, unit, 0 - effect.getPower(), CC.DEF, false);
				break;
			}
			// KAUSALITET
			case "Os�rbarhet":
				addMerit(unit, -1, Unit.EVASION); break;
			case "V�rldslighet":
			{
				addDemerit(unit, unit, -1, Unit.BUFF_IMMUNITY);
				break;
			}
			// OVERKLIGHET
			case "Transformation":
			{
				buff(unit, 0 - effect.getPower(), CC.STR);
				buff(unit, 0 - effect.getPower(), CC.INT);
				break;
			}
			case "Andeform":
				buff(unit, 0 - effect.getPower(), CC.DEF); break;
		}
	}
	
	
	/**
	 * Returns the VN-code which contains information
	 * about recently performed actions.
	 * Resets the code afterwards
	 * 
	 * @return
	 */
	public String getVNCode()
	{
		String code = vnCode;
		resetVNCode();
		return code;
	}
}
