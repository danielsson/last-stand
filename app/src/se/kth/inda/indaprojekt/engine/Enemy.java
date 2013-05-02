package se.kth.inda.indaprojekt.engine;

/**
 * An Enemy is a Unit that runs towards a target and explodes, inflicting damage,
 * upon reaching it but dies in the process.
 * 
 * @author David Masko
 */
public class Enemy extends Unit implements UnitImpacter{

	private Unit target; // The unit the enemy wants to reach and damage
	private double speed; // The speed it travels towards it's target. 
	
	/**
	 * Creates a new Enemy.
	 * 
	 * @param x The x-position of the Enemy.
	 * @param y The x-position of the Enemy.
	 * @param health The initial health of the Enemy.
	 * @param radius The radius of the Enemies' circular shape.
	 * @param speed The speed which the Enemy runs towards its' target.
	 * @param target The target the Enemy runs towards.
	 */
	public Enemy(double x, double y, double radius, int health, double speed, Unit target) {
		super(x, y, radius, health, 100);
		this.target = target;
		this.speed = speed;
	}

	/**
	 * Moves the Enemy towards its' given target. IF it's knockbacked
	 * it will be knockbacked without any standard movement.
	 */
	@Override
	public void onTick() {
		if(isKnockbacked())
			executeKnockbacks();
		else
			moveTowardsPoint(target.getX(), target.getY(), speed);
	}

	@Override
	public void onImpact(Unit u) {
		u.inflictDamage(1);
		inflictDamage(getHealth());
		
	}

	@Override
	public boolean isRelevantTarget(Unit u) {
		return u == target;
	}

}
