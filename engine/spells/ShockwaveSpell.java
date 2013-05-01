package engine.spells;

import engine.Level;
import engine.Projectile;
import engine.Spell;
import engine.Wizard;
import engine.projectiles.ShockwaveBlast;

public class ShockwaveSpell extends Spell{

	private double radius = 25;
	private double knockbackForce = 8;
	private int knockbackDuration = 40;
	private double speed = 8;
	
	public ShockwaveSpell(Level level) {
		super(level);
	}

	@Override
	public int getManaCost() {
		return 1;
	}

	@Override
	public void executeSpell(Wizard source, double x, double y) {
		Projectile p = new ShockwaveBlast(source, radius, x, y, speed, knockbackDuration, knockbackForce);
		getLevel().addWorldObject(p);
	}

}
