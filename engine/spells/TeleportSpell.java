package engine.spells;

import engine.Level;
import engine.Spell;
import engine.Wizard;

public class TeleportSpell extends Spell{

	public TeleportSpell(Level level) {
		super(level);
	}

	@Override
	public int getManaCost() {
		return 1;
	}

	@Override
	public void executeSpell(Wizard source, double x, double y) {
		source.teleportTo(x, y);
	}

}
