package se.wizard.laststand.engine.spells;

import se.wizard.laststand.engine.Level;
import se.wizard.laststand.engine.Projectile;
import se.wizard.laststand.engine.Spell;
import se.wizard.laststand.engine.Wizard;
import se.wizard.laststand.engine.projectiles.FireBallProjectile;

public class FireBall extends Spell{
	
	private double radius;
	private double speed = 7;
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
		return 1;
	}

	@Override
	public void executeSpell(Wizard source, double x, double y) {
		Projectile p = new FireBallProjectile(source, radius, x, y, speed, damage);
		getLevel().addWorldObject(p);
		
	}

	@Override
	public SpellType getType() {
		return SpellType.FIREBALL;
	}

}
