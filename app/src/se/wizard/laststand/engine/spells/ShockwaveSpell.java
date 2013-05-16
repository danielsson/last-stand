package se.wizard.laststand.engine.spells;

import se.wizard.laststand.engine.Level;
import se.wizard.laststand.engine.Projectile;
import se.wizard.laststand.engine.Spell;
import se.wizard.laststand.engine.Wizard;
import se.wizard.laststand.engine.projectiles.ShockwaveBlast;

public class ShockwaveSpell extends Spell{

	private double radius = 40;
	private double knockbackForce = 12;
	private int knockbackDuration = 60;
	private double speed = 14;
	
	public ShockwaveSpell(Level level) {
		super(level);
	}

	@Override
	public int getManaCost() {
		return 3;
	}

	@Override
	public void executeSpell(Wizard source, double x, double y) {
		Projectile p = new ShockwaveBlast(source, radius, x, y, speed, knockbackDuration, knockbackForce);
		getLevel().addWorldObject(p);
	}

	@Override
	public SpellType getType() {
		return SpellType.SHOCKWAVE;
	}

}
