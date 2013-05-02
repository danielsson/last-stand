package se.kth.inda.indaprojekt.engine;

/**
 * A Unit is a WorldObject that is defined by its' living
 * characteristics, it has health that can be decreased by damage
 * and ultimately die if it takes too much of a beating.
 * 
 * @author David Masko
 *
 */
public abstract class Unit extends WorldObject{

	private int health;

	/**
	 * Creates a new Unit with drawPriority 0.
	 * 
	 * @param x The x-position of the Unit.
	 * @param y The x-position of the Unit.
	 * @param health The initial health of the Unit.
	 * @param radius The radius of the Unit's circular shape.
	 */
	public Unit(double x, double y, double radius, int health) {
		this(x, y, radius, health, 0);
	}
	
	/**
	 * Creates a new Unit.
	 * 
	 * @param x The x-position of the Unit.
	 * @param y The x-position of the Unit.
	 * @param health The initial health of the Unit.
	 * @param radius The radius of the Unit's circular shape.
	 * drawPriority The drawPriority of the Unit.
	 */
	public Unit(double x, double y, double radius, int health, int drawPriority) {
		super(x, y, radius, drawPriority);
		this.health = health;
	}
	
	/**
	 * Damages the target, reduces it's health with the given value.
	 * 
	 * @param value The damage to be inflicted.
	 * @throws IllegalArgumentException if value < 0
	 */
	public void inflictDamage(int value){
		if(value < 0)
			throw new IllegalArgumentException("A Unit cannot take negative damage");
		health-=value;
	}
	
	/**
	 * If the unit's health is depleted.
	 * 
	 * @return True if the unit is dead, false otherwise.
	 */
	public boolean isDead(){
		return health <= 0;
	}
	
	/**
	 * The health of the unit.
	 * 
	 * @return The health of the unit.
	 */
	public int getHealth(){
		return health;
	}

}
