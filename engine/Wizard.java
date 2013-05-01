package engine;

import java.util.ArrayList;

public class Wizard extends Unit{
		
	private ArrayList<Spell> spells = new ArrayList<Spell>();
	private double mana;
	private int maxMana;
	private double manaRegen;
	
	/**
	 * Creates a new Wizard
	 * 
	 * @param x The initial x-position of the Wizard.
	 * @param y The initial y-position of the Wizard.
	 * @param radius The radius of the Wizards' circular shape.
	 * @param health The initial amount of health.
	 * @param spells The initial spellbook of the Wizard.
	 * @param mana The initial and maximum mana of the Wizard.
	 * @param manaRegen The mana regeneration of the Wizard.
	 */
	public Wizard(double x, double y, double radius, int health, Spell[] spells, int mana, double manaRegen) {
		super(x, y, radius, health, 150);
		for (int i = 0; i < spells.length; i++) {
			this.spells.add(spells[i]);
		}
		maxMana = mana;
		this.mana = maxMana;
		this.manaRegen = manaRegen;
		
	}
	
	/**
	 * Attempts to cast the given spell and if the Wizard succeeds he will lose
	 * mana accordingly to the mana cost of the spell.
	 * In order to cast the spell the Wizard needs to have it in his spellbook
	 * and spells from getSpellbook() is therefore only accepted. Secondly the
	 * Wizard needs to have enough mana to cast the spell.
	 * 
	 * @param spell The spell to be casted (from his spellbook, getSpellbook())
	 * @param x The x-position the spell was called upon.
	 * @param y The y-position the spell was called upon.
	 * @return True if the spell fulfilled the criteria to be casted, false otherwise.
	 */
	public boolean attemptToCastSpell(Spell spell, double x, double y){
		if(spells.contains(spell)){
			if(getMana() >= spell.getManaCost()){
				mana-=spell.getManaCost();
				spell.executeSpell(this, x, y);
				return true;
			}
		}
		return false;
	}

	@Override
	public void onTick() {
		double newMana = mana+manaRegen;
		if(newMana>maxMana)
			mana = maxMana;
		else
			mana = newMana;
	}

	/**
	 * Add the given Spell to the Wizards spellbook.
	 * 
	 * @param spell The Spell to be learned.
	 */
	public void addSpell(Spell spell){
		if(!spells.contains(spell))
			spells.add(spell);
	}

	/**
	 * The Spellbook of the Wizard. These are the only spells that are
	 * accepted to be used in attemptToCastSpell.
	 * 
	 * @return The Spells the Wizard may use.
	 */
	public Spell[] getSpellbook(){
		return spells.toArray(new Spell[0]);
	}
	
	/**
	 * The amount of available mana. 
	 * 
	 * @return the available mana.
	 */
	public int getMana(){
		return (int)mana;
	}
}
