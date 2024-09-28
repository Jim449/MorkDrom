package dröm;

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
		// Tvinga motståndaren att använda en attack under nästa runda.
		// Förmågor prioriteras framför normala attacker.
		
		action = new Action("Raseri", CC.SELF_MANIPULATION, 8);
		action.addEffect(100, 2);
		content.put("Raseri", action);
		// Ökar Styrka med 100% under 2 rundor och tvingar användaren att anfalla.
		// Förmågor prioriteras framför normala attacker.
		
		action = new Action("Eldsköld", CC.BUFF, 8);
		action.addEffect(1, 3);
		content.put("Eldsköld", action);
		// Skapar en eldsköld för 3 rundor som skadar fienden
		// då den anfaller användaren.
		
		// Vatten
		// ---------------------------------------
		content.put("Invasiv kyla", new Action("Invasiv kyla", CC.ATTACK, 8));
		// En attack som ignorerar försvar
		
		content.put("Läkekraft", new Action("Läkekraft", CC.HEAL, 8));
		// Helar användaren från skador
		
		action = new Action("Fokus", CC.BUFF, 8);
		action.addEffect(75, 3);
		content.put("Fokus", action);
		// Ökar effekten av helande förmågor med 75% under 3 rundor
		
		action = new Action("Frost", CC.DEBUFF, 8);
		action.addEffect(40, 3);
		content.put("Frost", action);
		// Minskar motståndarens Styrka med 40% under 3 rundor
		
		// Luft
		// ---------------------------------------
		content.put("Vindskära", new Action("Vindskära", CC.ATTACK, 6));
		// En attack med kort avkylning
		
		action = new Action("Vindsköld", CC.SHIELD, 7);
		action.addEffect(1, 1);
		content.put("Vindsköld", action);
		// Skyddar användaren mot alla attacker och försvagningar under 1 runda
		
		content.put("Frihet", new Action("Frihet", CC.CLEANSE, 7));
		// Tar bort alla negativa effekter från användaren
		
		content.put("Inspiration", new Action("Inspiration", CC.BUFF, 7));
		// Räknar ned avkylningsperioden för alla drag med 1 runda
		
		// Jord
		// ---------------------------------------
		action = new Action("Skalv", CC.ATTACK, 8);
		action.addEffect(1, 2);
		content.put("Skalv", action);
		// En attack som förlamar motståndaren under nästa runda
		
		action = new Action("Styrka", CC.BUFF, 8);
		action.addEffect(75, 3);
		content.put("Styrka", action);
		// Ökar Styrka med 75% under 3 rundor
		
		action = new Action("Uthållighet", CC.BUFF, 8);
		action.addEffect(40, 3);
		content.put("Uthållighet", action);
		// Ökar Försvar med 40% under 3 rundor
		
		action = new Action("Viljestyrka", CC.BUFF, 8);
		action.addEffect(1, 8);
		content.put("Viljestyrka", action);
		// Ger immunitet mot förlamning och manipulation under 8 rundor
		
		// Ljus
		// ---------------------------------------
		content.put("Blixt", new Action("Blixt", CC.SURE_HIT, 8));
		// En attack som inte kan undvikas
		
		action = new Action("Frid", CC.MANIPULATION, 12);
		action.addEffect(1, 4);
		content.put("Frid", action);
		// Tvinga motståndaren att bara använda förstärkande drag
		// under de kommande 3 rundorna
		
		action = new Action("Illuminering", CC.BUFF, 8);
		action.addEffect(1, 8);
		content.put("Illuminering", action);
		// Gör dina drag omöjliga att blockera under 8 rundor
		
		action = new Action("Genomskådning", CC.BUFF, 8);
		action.addEffect(1, 8);
		content.put("Genomskådning", action);
		// Dina attacker ignorerar försvar under 8 rundor
		
		// Mörker
		// ---------------------------------------
		content.put("Tomhet", new Action("Tomhet", CC.DISPEL, 7));
		// Tar bort alla positiva effekter från motståndaren
		
		action = new Action("Mörker", CC.SHIELD, 12);
		action.addEffect(1, 3);
		content.put("Mörker", action);
		// Skyddar mot attacker och försvagningar
		// under 3 rundor men användaren kan inte agera
		
		action = new Action("Hopplöshet", CC.COUNTER, 8);
		action.setCounterType(CC.BUFF);
		content.put("Hopplöshet", action);
		// Blockerar motståndarens förstärkande effekt och kontrar med en attack
		
		action = new Action("Bitterhet", CC.MANIPULATION, 7);
		action.addEffect(1, 2);
		content.put("Bitterhet", action);
		// Tvingar motståndaren att använda en försvagande förmåga under nästa runda
		
		// Trä
		// ---------------------------------------
		content.put("Absorption", new Action("Absorption", CC.ATTACK, 7));
		// En attack som stjäl hälsopoäng från motståndaren
		
		action = new Action("Regenerering", CC.BUFF, 8);
		action.addEffect(1, 5);
		content.put("Regenerering", action);
		// Helar användaren från skador under 5 rundor
		
		action = new Action("Kontring", CC.COUNTER, 8);
		action.setCounterType(CC.ATTACK);
		content.put("Kontring", action);
		// Blockerar motståndarens attack och kontrar med en attack
		
		action = new Action("Självbevarelsedrift", CC.MANIPULATION, 7);
		action.addEffect(1, 2);
		content.put("Självbevarelsedrift", action);
		// Tvingar motståndaren att använda en förstärkande förmåga under nästa runda
		
		// Metall
		// ---------------------------------------
		action = new Action("Reflektion", CC.COUNTER, 8);
		action.setCounterType(CC.DEBUFF);
		content.put("Reflektion", action);
		// Blockerar en försvagande förmåga och kontrar med en attack
		
		action = new Action("Förgiftning", CC.DEBUFF, 8);
		action.addEffect(1, 5);
		content.put("Förgiftning", action);
		// Skadar motståndaren kontinuerligt under 5 rundor
		
		action = new Action("Erodering", CC.DEBUFF, 8);
		action.addEffect(75, 3);
		content.put("Erodering", action);
		// Minskar motståndarens försvar med 75% under 3 rundor
		
		action = new Action("Fanatism", CC.BUFF, 8);
		action.addEffect(1, 8);
		content.put("Fanatism", action);
		// Förhindrar att dina positiva effekter
		// tas bort under 8 rundor

		// Specialförmågor
		// Eld
		// ---------------------------------------
		action = new Action("Rasande eld", CC.ATTACK, 8);
		action.setUnscheduled(true);
		content.put("Rasande eld", action);
		// En extra kraftfull attack som bara kan användas
		// under manipulation
		
		action = new Action("Nova", CC.ATTACK, 8);
		action.addEffect(100, 2);
		content.put("Nova", action);
		// En extra kraftfull attack som minskar
		// användarens eget försvar
		
		action = new Action("Urkraft", CC.BUFF, 100);
		action.addEffect(100, 50, true);
		content.put("Urkraft", action);
		// Förstärker användarens styrka permanenet
		
		// Vatten
		// ---------------------------------------
		action = new Action("Förfrysning", CC.MANIPULATION, 8);
		action.addEffect(1, 3);
		content.put("Förfrysning", action);
		// Förhindrar motståndaren från att agera
		// under de 2 kommande rundorna
		// och åsamkar skada varje runda
		
		action = new Action("Meditation", CC.BUFF, 8);
		action.addEffect(2, 5);
		action.setUnscheduled(true);
		content.put("Meditation", action);
		// Återställer hp varje runda för 5 rundor
		// och skyddar förmågor från avaktivering
		// Kan bara användas under manipulation
		
		action = new Action("Illusion", CC.MANIPULATION, 8);
		action.addEffect(1, 2);
		content.put("Illusion", action);
		// Tvingar motståndaren att anfalla
		// men förhindrar motståndaren från att träffa
		// under 2 rundor
		
		// Luft
		// ---------------------------------------
		action = new Action("Storm", CC.BUFF, 8);
		action.addEffect(1, 3, true);
		content.put("Storm", action);
		// Skyddar användaren mot manipulation
		// och tvingar användaren att använda förmågan Tornado
		// under de kommande två rundorna.
		// Denna förmåga kan inte tas bort av förmågor
		
		content.put("Tornado", new Action("Tornado", CC.ATTACK, 0));
		// En attack som bara kan användas genom Storm
		
		content.put("Hast", new Action("Hast", CC.BUFF, 6));
		// Minskar avkylningsperioden av alla drag
		// och användaren måste använda dragen
		// så fort de blir tillgängliga
		
		// Jord
		// ---------------------------------------
		action = new Action("Jordbävning", CC.ATTACK, 40, 8);
		action.addEffect(1, 6);
		content.put("Jordbävning", action);
		// En extra kraftfull attack som förlamar motståndaren
		// under de 5 kommande rundorna.
		// Har en lång uppvärmningsperiod
		
		action = new Action("Stenskinn", CC.BUFF, 8);
		action.addEffect(80, 3);
		content.put("Stenskinn", action);
		// Ökar användarens försvar med 80% under 3 rundor
		
		action = new Action("Orubblighet", CC.BUFF, 100);
		action.addEffect(1, 50, true);
		content.put("Orubblighet", action);
		// Ger användaren permanent immunitet mot manipulationer
		
		// Ljus
		// ---------------------------------------
		content.put("Mental chock", new Action("Mental chock", CC.SURE_HIT, 8));
		// En kraftfull attack som ignorerar
		// både undvik och försvar
		
		action = new Action("Prediktion", CC.BUFF, 8);
		action.addEffect(1, 8, true);
		content.put("Prediktion", action);
		// Säkerställer att användarens drag lyckas
		// och ignorerar försvar under 8 rundor
		// Denna förmåga kan inte tas bort av förmågor
		
		action = new Action("Identifiering", CC.BUFF, 100);
		action.addEffect(1, 50, true);
		content.put("Identifiering", action);
		// Ger säker träff och ignorerar motståndarens
		// immunitet mot försvagningar permanent
		
		// Mörker
		// ---------------------------------------
		content.put("Mörk vind", new Action("Mörk vind", CC.SURE_HIT, 8));
		// En attack som tar bort motståndarens förstärkningar
		// innan den träffar. Kan inte undvikas
		
		action = new Action("Depression", CC.BUFF_BLOCK, 8);
		action.addEffect(1, 3, true);
		content.put("Depression", action);
		// Motståndaren stöter bort förstärkande förmågor
		// under 5 rundor.
		// Denna förmåga kan inte tas bort av förmågor
		
		// Trä
		// ---------------------------------------
		content.put("Återhämtning", new Action("Återhämtning", CC.HEAL, 5));
		// Återställer hälsopoäng och tar bort negativa effekter från användaren
		
		action = new Action("Harmoni", CC.MASS_MANIPULATION, 8);
		action.addEffect(150, 2);
		content.put("Harmoni", action);
		// Tvingar både användaren och motståndaren
		// att använda förstärkande drag under nästa runda
		// Ökar effekten av läkekraft med 150% för alla
		
		// TODO: kan raderas
		action = new Action("Anpassning", CC.BUFF, 6);
		action.addEffect(1, 40, true);
		content.put("Anpassning", action);
		// Ökar användarens attack med 40%
		// och försvar med 20% under 50 rundor
		// Kan inte avaktiveras i förtid
		
		action = new Action("Kanalisering", CC.BUFF, 60);
		action.addEffect(1, 10, true);
		content.put("Kanalisering", action);
		// Ökar användarens försvar med 100%
		// och ökar återhämtning under 20 rundor
		// Kan inte avaktiveras av förmågor
		
		content.put("Förtäring", new Action("Förtäring", CC.ATTACK, 7));
		// En kraftfull attack som absorberar motståndarens hälsopoäng
		
		// Metall
		// ---------------------------------------
		action = new Action("Överlägsenhet", CC.BUFF, 8);
		action.addEffect(25, 8);
		content.put("Överlägsenhet", action);
		// Ger immunitet mot negativa effekter,
		// skyddar positiva effekter från avaktivering
		// och ökar användarens styrka med 25%
		
		action = new Action("Djup infektion", CC.DEBUFF, 8);
		action.addEffect(50, 8);
		content.put("Djup infektion", action);
		// Förhindrar att motståndaren tar bort försvagningar
		// och minskar försvar med 50% under 8 rundor
		
		// Kausalitet
		// ---------------------------------------
		content.put("Svarta vingar", new Action("Svarta vingar", CC.BLACK_WING, 4));
		// En omedelbart dödande attack som bara fungerar
		// om motståndaren inte är påverkad av några förstärkningar
		
		action = new Action("Osårbarhet", CC.SHIELD, 10);
		action.addEffect(1, 3);
		content.put("Osårbarhet", action);
		// Skyddar användaren mot alla hot under 3 rundor
		
		action = new Action("Världslighet", CC.BUFF_BLOCK, 8);
		action.addEffect(40, 8, true);
		content.put("Världslighet", action);
		// Blockerar motståndarens förstärkningar
		// under 8 rundor
		
		
		// Entropi
		// ---------------------------------------
		
		// Overklighet
		// ---------------------------------------
		action = new Action("Transformation", CC.BUFF, 0);
		action.addEffect(25, 8);
		// Ökar attack och läkekraft med 25%
		// under 8 rundor
		content.put("Transformation", action);
		
		content.put("Mardröm", new Action("Mardröm", CC.SURE_HIT, 8));
		// En attack vars skada beror på antalet positiva effekter
		// som användaren är påverkad av
		
		action = new Action("Andeform", CC.BUFF, 100);
		action.addEffect(100, 50, true);
		content.put("Andeform", action);
		// Ökar försvar med 100% under 100 rundor.
		// Kan inte avaktiveras av förmågor
	}
}
