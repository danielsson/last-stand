package se.wizard.laststand.engine.spells;

import se.wizard.laststand.engine.Level;
import se.wizard.laststand.engine.Spell;
import se.wizard.laststand.engine.Wizard;

public class TeleportSpell extends Spell{

	public TeleportSpell(Level level) {
		super(level);
	}

	@Override
	public int getManaCost() {
		return 4;
	}

	@Override
	public void executeSpell(Wizard source, double x, double y) {
		source.teleportTo(x, y);
	}

	@Override
	public SpellType getType() {
		return SpellType.TELEPORT;
	}

}
