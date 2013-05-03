package se.kth.inda.indaprojekt.engine.spells;

import se.kth.inda.indaprojekt.engine.Level;
import se.kth.inda.indaprojekt.engine.Spell;
import se.kth.inda.indaprojekt.engine.Wizard;

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

}
