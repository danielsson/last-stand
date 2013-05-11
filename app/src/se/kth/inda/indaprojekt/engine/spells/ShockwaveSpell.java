package se.kth.inda.indaprojekt.engine.spells;

import se.kth.inda.indaprojekt.engine.Level;
import se.kth.inda.indaprojekt.engine.Projectile;
import se.kth.inda.indaprojekt.engine.Spell;
import se.kth.inda.indaprojekt.engine.Wizard;
import se.kth.inda.indaprojekt.engine.projectiles.ShockwaveBlast;

public class ShockwaveSpell extends Spell{

	private double radius = 40;
	private double knockbackForce = 8;
	private int knockbackDuration = 40;
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
