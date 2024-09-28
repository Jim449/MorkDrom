package dr�m;

import java.util.HashMap;


/**
 * En databas av drag
 * 
 * @author alexi
 */
public class AbilityList {

	private HashMap<String, Action> content;
	
	
	/**
	 * Returnerar en kopia av ett drag
	 * 
	 * @param name
	 * @return
	 */
	public Action getMove( String name )
	{
		return content.get(name).copy();
	}
	
	
	/**
	 * Skapar databasen av drag
	 */
	public AbilityList()
	{
		Action action;
		
		content = new HashMap<String, Action>(100);
		
		content.put("", new Action());
		// Eld
		// ---------------------------------------
		content.put("Inferno", new Action("Inferno", CC.ATTACK, 8));
		// En kraftfull attack
		
		action = new Action("Provokation", CC.MANIPULATION, 7);
		action.addEffect(1, 2);
		content.put("Provokation", action);
		// Tvinga motst�ndaren att anv�nda en attack under n�sta runda.
		// F�rm�gor prioriteras framf�r normala attacker.
		
		action = new Action("Raseri", CC.SELF_MANIPULATION, 8);
		action.addEffect(100, 2);
		content.put("Raseri", action);
		// �kar Styrka med 100% under 2 rundor och tvingar anv�ndaren att anfalla.
		// F�rm�gor prioriteras framf�r normala attacker.
		
		action = new Action("Eldsk�ld", CC.BUFF, 8);
		action.addEffect(1, 3);
		content.put("Eldsk�ld", action);
		// Skapar en eldsk�ld f�r 3 rundor som skadar fienden
		// d� den anfaller anv�ndaren.
		
		// Vatten
		// ---------------------------------------
		content.put("Invasiv kyla", new Action("Invasiv kyla", CC.ATTACK, 8));
		// En attack som ignorerar f�rsvar
		
		content.put("L�kekraft", new Action("L�kekraft", CC.HEAL, 8));
		// Helar anv�ndaren fr�n skador
		
		action = new Action("Fokus", CC.BUFF, 8);
		action.addEffect(75, 3);
		content.put("Fokus", action);
		// �kar effekten av helande f�rm�gor med 75% under 3 rundor
		
		action = new Action("Frost", CC.DEBUFF, 8);
		action.addEffect(40, 3);
		content.put("Frost", action);
		// Minskar motst�ndarens Styrka med 40% under 3 rundor
		
		// Luft
		// ---------------------------------------
		content.put("Vindsk�ra", new Action("Vindsk�ra", CC.ATTACK, 6));
		// En attack med kort avkylning
		
		action = new Action("Vindsk�ld", CC.SHIELD, 7);
		action.addEffect(1, 1);
		content.put("Vindsk�ld", action);
		// Skyddar anv�ndaren mot alla attacker och f�rsvagningar under 1 runda
		
		content.put("Frihet", new Action("Frihet", CC.CLEANSE, 7));
		// Tar bort alla negativa effekter fr�n anv�ndaren
		
		content.put("Inspiration", new Action("Inspiration", CC.BUFF, 7));
		// R�knar ned avkylningsperioden f�r alla drag med 1 runda
		
		// Jord
		// ---------------------------------------
		action = new Action("Skalv", CC.ATTACK, 8);
		action.addEffect(1, 2);
		content.put("Skalv", action);
		// En attack som f�rlamar motst�ndaren under n�sta runda
		
		action = new Action("Styrka", CC.BUFF, 8);
		action.addEffect(75, 3);
		content.put("Styrka", action);
		// �kar Styrka med 75% under 3 rundor
		
		action = new Action("Uth�llighet", CC.BUFF, 8);
		action.addEffect(40, 3);
		content.put("Uth�llighet", action);
		// �kar F�rsvar med 40% under 3 rundor
		
		action = new Action("Viljestyrka", CC.BUFF, 8);
		action.addEffect(1, 8);
		content.put("Viljestyrka", action);
		// Ger immunitet mot f�rlamning och manipulation under 8 rundor
		
		// Ljus
		// ---------------------------------------
		content.put("Blixt", new Action("Blixt", CC.SURE_HIT, 8));
		// En attack som inte kan undvikas
		
		action = new Action("Frid", CC.MANIPULATION, 12);
		action.addEffect(1, 4);
		content.put("Frid", action);
		// Tvinga motst�ndaren att bara anv�nda f�rst�rkande drag
		// under de kommande 3 rundorna
		
		action = new Action("Illuminering", CC.BUFF, 8);
		action.addEffect(1, 8);
		content.put("Illuminering", action);
		// G�r dina drag om�jliga att blockera under 8 rundor
		
		action = new Action("Genomsk�dning", CC.BUFF, 8);
		action.addEffect(1, 8);
		content.put("Genomsk�dning", action);
		// Dina attacker ignorerar f�rsvar under 8 rundor
		
		// M�rker
		// ---------------------------------------
		content.put("Tomhet", new Action("Tomhet", CC.DISPEL, 7));
		// Tar bort alla positiva effekter fr�n motst�ndaren
		
		action = new Action("M�rker", CC.SHIELD, 12);
		action.addEffect(1, 3);
		content.put("M�rker", action);
		// Skyddar mot attacker och f�rsvagningar
		// under 3 rundor men anv�ndaren kan inte agera
		
		action = new Action("Hoppl�shet", CC.COUNTER, 8);
		action.setCounterType(CC.BUFF);
		content.put("Hoppl�shet", action);
		// Blockerar motst�ndarens f�rst�rkande effekt och kontrar med en attack
		
		action = new Action("Bitterhet", CC.MANIPULATION, 7);
		action.addEffect(1, 2);
		content.put("Bitterhet", action);
		// Tvingar motst�ndaren att anv�nda en f�rsvagande f�rm�ga under n�sta runda
		
		// Tr�
		// ---------------------------------------
		content.put("Absorption", new Action("Absorption", CC.ATTACK, 7));
		// En attack som stj�l h�lsopo�ng fr�n motst�ndaren
		
		action = new Action("Regenerering", CC.BUFF, 8);
		action.addEffect(1, 5);
		content.put("Regenerering", action);
		// Helar anv�ndaren fr�n skador under 5 rundor
		
		action = new Action("Kontring", CC.COUNTER, 8);
		action.setCounterType(CC.ATTACK);
		content.put("Kontring", action);
		// Blockerar motst�ndarens attack och kontrar med en attack
		
		action = new Action("Sj�lvbevarelsedrift", CC.MANIPULATION, 7);
		action.addEffect(1, 2);
		content.put("Sj�lvbevarelsedrift", action);
		// Tvingar motst�ndaren att anv�nda en f�rst�rkande f�rm�ga under n�sta runda
		
		// Metall
		// ---------------------------------------
		action = new Action("Reflektion", CC.COUNTER, 8);
		action.setCounterType(CC.DEBUFF);
		content.put("Reflektion", action);
		// Blockerar en f�rsvagande f�rm�ga och kontrar med en attack
		
		action = new Action("F�rgiftning", CC.DEBUFF, 8);
		action.addEffect(1, 5);
		content.put("F�rgiftning", action);
		// Skadar motst�ndaren kontinuerligt under 5 rundor
		
		action = new Action("Erodering", CC.DEBUFF, 8);
		action.addEffect(75, 3);
		content.put("Erodering", action);
		// Minskar motst�ndarens f�rsvar med 75% under 3 rundor
		
		action = new Action("Fanatism", CC.BUFF, 8);
		action.addEffect(1, 8);
		content.put("Fanatism", action);
		// F�rhindrar att dina positiva effekter
		// tas bort under 8 rundor

		// Specialf�rm�gor
		// Eld
		// ---------------------------------------
		action = new Action("Rasande eld", CC.ATTACK, 8);
		action.setUnscheduled(true);
		content.put("Rasande eld", action);
		// En extra kraftfull attack som bara kan anv�ndas
		// under manipulation
		
		action = new Action("Nova", CC.ATTACK, 8);
		action.addEffect(100, 2);
		content.put("Nova", action);
		// En extra kraftfull attack som minskar
		// anv�ndarens eget f�rsvar
		
		action = new Action("Urkraft", CC.BUFF, 100);
		action.addEffect(100, 50, true);
		content.put("Urkraft", action);
		// F�rst�rker anv�ndarens styrka permanenet
		
		// Vatten
		// ---------------------------------------
		action = new Action("F�rfrysning", CC.MANIPULATION, 8);
		action.addEffect(1, 3);
		content.put("F�rfrysning", action);
		// F�rhindrar motst�ndaren fr�n att agera
		// under de 2 kommande rundorna
		// och �samkar skada varje runda
		
		action = new Action("Meditation", CC.BUFF, 8);
		action.addEffect(2, 5);
		action.setUnscheduled(true);
		content.put("Meditation", action);
		// �terst�ller hp varje runda f�r 5 rundor
		// och skyddar f�rm�gor fr�n avaktivering
		// Kan bara anv�ndas under manipulation
		
		action = new Action("Illusion", CC.MANIPULATION, 8);
		action.addEffect(1, 2);
		content.put("Illusion", action);
		// Tvingar motst�ndaren att anfalla
		// men f�rhindrar motst�ndaren fr�n att tr�ffa
		// under 2 rundor
		
		// Luft
		// ---------------------------------------
		action = new Action("Storm", CC.BUFF, 8);
		action.addEffect(1, 3, true);
		content.put("Storm", action);
		// Skyddar anv�ndaren mot manipulation
		// och tvingar anv�ndaren att anv�nda f�rm�gan Tornado
		// under de kommande tv� rundorna.
		// Denna f�rm�ga kan inte tas bort av f�rm�gor
		
		content.put("Tornado", new Action("Tornado", CC.ATTACK, 0));
		// En attack som bara kan anv�ndas genom Storm
		
		content.put("Hast", new Action("Hast", CC.BUFF, 6));
		// Minskar avkylningsperioden av alla drag
		// och anv�ndaren m�ste anv�nda dragen
		// s� fort de blir tillg�ngliga
		
		// Jord
		// ---------------------------------------
		action = new Action("Jordb�vning", CC.ATTACK, 40, 8);
		action.addEffect(1, 6);
		content.put("Jordb�vning", action);
		// En extra kraftfull attack som f�rlamar motst�ndaren
		// under de 5 kommande rundorna.
		// Har en l�ng uppv�rmningsperiod
		
		action = new Action("Stenskinn", CC.BUFF, 8);
		action.addEffect(80, 3);
		content.put("Stenskinn", action);
		// �kar anv�ndarens f�rsvar med 80% under 3 rundor
		
		action = new Action("Orubblighet", CC.BUFF, 100);
		action.addEffect(1, 50, true);
		content.put("Orubblighet", action);
		// Ger anv�ndaren permanent immunitet mot manipulationer
		
		// Ljus
		// ---------------------------------------
		content.put("Mental chock", new Action("Mental chock", CC.SURE_HIT, 8));
		// En kraftfull attack som ignorerar
		// b�de undvik och f�rsvar
		
		action = new Action("Prediktion", CC.BUFF, 8);
		action.addEffect(1, 8, true);
		content.put("Prediktion", action);
		// S�kerst�ller att anv�ndarens drag lyckas
		// och ignorerar f�rsvar under 8 rundor
		// Denna f�rm�ga kan inte tas bort av f�rm�gor
		
		action = new Action("Identifiering", CC.BUFF, 100);
		action.addEffect(1, 50, true);
		content.put("Identifiering", action);
		// Ger s�ker tr�ff och ignorerar motst�ndarens
		// immunitet mot f�rsvagningar permanent
		
		// M�rker
		// ---------------------------------------
		content.put("M�rk vind", new Action("M�rk vind", CC.SURE_HIT, 8));
		// En attack som tar bort motst�ndarens f�rst�rkningar
		// innan den tr�ffar. Kan inte undvikas
		
		action = new Action("Depression", CC.BUFF_BLOCK, 8);
		action.addEffect(1, 3, true);
		content.put("Depression", action);
		// Motst�ndaren st�ter bort f�rst�rkande f�rm�gor
		// under 5 rundor.
		// Denna f�rm�ga kan inte tas bort av f�rm�gor
		
		// Tr�
		// ---------------------------------------
		content.put("�terh�mtning", new Action("�terh�mtning", CC.HEAL, 5));
		// �terst�ller h�lsopo�ng och tar bort negativa effekter fr�n anv�ndaren
		
		action = new Action("Harmoni", CC.MASS_MANIPULATION, 8);
		action.addEffect(150, 2);
		content.put("Harmoni", action);
		// Tvingar b�de anv�ndaren och motst�ndaren
		// att anv�nda f�rst�rkande drag under n�sta runda
		// �kar effekten av l�kekraft med 150% f�r alla
		
		// TODO: kan raderas
		action = new Action("Anpassning", CC.BUFF, 6);
		action.addEffect(1, 40, true);
		content.put("Anpassning", action);
		// �kar anv�ndarens attack med 40%
		// och f�rsvar med 20% under 50 rundor
		// Kan inte avaktiveras i f�rtid
		
		action = new Action("Kanalisering", CC.BUFF, 60);
		action.addEffect(1, 10, true);
		content.put("Kanalisering", action);
		// �kar anv�ndarens f�rsvar med 100%
		// och �kar �terh�mtning under 20 rundor
		// Kan inte avaktiveras av f�rm�gor
		
		content.put("F�rt�ring", new Action("F�rt�ring", CC.ATTACK, 7));
		// En kraftfull attack som absorberar motst�ndarens h�lsopo�ng
		
		// Metall
		// ---------------------------------------
		action = new Action("�verl�gsenhet", CC.BUFF, 8);
		action.addEffect(25, 8);
		content.put("�verl�gsenhet", action);
		// Ger immunitet mot negativa effekter,
		// skyddar positiva effekter fr�n avaktivering
		// och �kar anv�ndarens styrka med 25%
		
		action = new Action("Djup infektion", CC.DEBUFF, 8);
		action.addEffect(50, 8);
		content.put("Djup infektion", action);
		// F�rhindrar att motst�ndaren tar bort f�rsvagningar
		// och minskar f�rsvar med 50% under 8 rundor
		
		// Kausalitet
		// ---------------------------------------
		content.put("Svarta vingar", new Action("Svarta vingar", CC.BLACK_WING, 4));
		// En omedelbart d�dande attack som bara fungerar
		// om motst�ndaren inte �r p�verkad av n�gra f�rst�rkningar
		
		action = new Action("Os�rbarhet", CC.SHIELD, 10);
		action.addEffect(1, 3);
		content.put("Os�rbarhet", action);
		// Skyddar anv�ndaren mot alla hot under 3 rundor
		
		action = new Action("V�rldslighet", CC.BUFF_BLOCK, 8);
		action.addEffect(40, 8, true);
		content.put("V�rldslighet", action);
		// Blockerar motst�ndarens f�rst�rkningar
		// under 8 rundor
		
		
		// Entropi
		// ---------------------------------------
		
		// Overklighet
		// ---------------------------------------
		action = new Action("Transformation", CC.BUFF, 0);
		action.addEffect(25, 8);
		// �kar attack och l�kekraft med 25%
		// under 8 rundor
		content.put("Transformation", action);
		
		content.put("Mardr�m", new Action("Mardr�m", CC.SURE_HIT, 8));
		// En attack vars skada beror p� antalet positiva effekter
		// som anv�ndaren �r p�verkad av
		
		action = new Action("Andeform", CC.BUFF, 100);
		action.addEffect(100, 50, true);
		content.put("Andeform", action);
		// �kar f�rsvar med 100% under 100 rundor.
		// Kan inte avaktiveras av f�rm�gor
	}
}
