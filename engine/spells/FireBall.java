package engine.spells;

import engine.Level;
import engine.Projectile;
import engine.Spell;
import engine.Wizard;
import engine.projectiles.FireBallProjectile;

public class FireBall extends Spell{
	
	private double radius;
	private double speed = 2;
	private int damage = 3;
	
	public FireBall(Level level, double fireBallRadius) {
		super(level);
		radius = fireBallRadius;
	}
	
	/**
	 * Set the size of the Fireballs that are going to be created
	 * by this spell.
	 * @param radius The radius of the FireBalls.
	 */
	public void setFireBallRadius(double radius){
		this.radius = radius;
	}

	@Override
	public int getManaCost() {
		return 0;
	}

	@Override
	public void executeSpell(Wizard source, double x, double y) {
		Projectile p = new FireBallProjectile(source, radius, x, y, speed, damage);
		getLevel().addWorldObject(p);
		
	}

}
