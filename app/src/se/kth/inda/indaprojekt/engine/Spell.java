package se.kth.inda.indaprojekt.engine;

/**
 * A Spell is an non-material object that is casted by Wizards and 
 * affects a Level. A Spell has a mana cost that defines how much
 * mana a Wizard need to spend in order to execute it.
 * 
 * @author David Masko
 */
public abstract class Spell {
	
	private Level level;

	/**
	 * Creates a new Spell whose effects affects the given Level.
	 * 
	 * @param level The Level this spell will affect once executed.
	 */
	public Spell(Level level){
		this.level = level;
	}
	
	/**
	 * The mana cost of this Spell.
	 * @return The mana cost of this Spell.
	 */
	public abstract int getManaCost();
	
	/**
	 * Unleashes the effects of this spell with the given Wizard as the
	 * source of origin and the given position as target.
	 * 
	 * @param source The Wizard that has executed the spell.
	 * @param x The x-position of the targeted position.
	 * @param y The y-position of the targeted position.
	 */
	public abstract void executeSpell(Wizard source, double x, double y);
	
	/**
	 * The Level this Spell affects.
	 * @return The level this Spell affects.
	 */
	protected Level getLevel(){
		return level;
	}
	
}
