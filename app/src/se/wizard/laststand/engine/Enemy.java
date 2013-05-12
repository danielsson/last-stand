package se.wizard.laststand.engine;

import android.graphics.Paint;
import android.graphics.Paint.Style;

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
	 */
	public Enemy(double x, double y, double radius, int health, double speed) {
		super(x, y, radius, health, 100);
		this.speed = speed;
	}
	
	/**
	 * Sets a target for this Enemy to run towards.
	 * @param newTarget The Unit to be targeted.
	 */
	public void setTarget(Unit newTarget){
		target = newTarget;
	}

	/**
	 * Moves the Enemy towards its' given target. If it's knockbacked
	 * it will be knockbacked without any standard movement.
	 */
	@Override
	public void onTick() {
		if(isKnockbacked())
			executeKnockbacks();
		else if(target != null)
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

	@Override
	public Paint getPaint() {
		Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
		p.setARGB(255, 255, 0, 0);
		p.setStyle(Style.FILL);
		return p;
	}

}
