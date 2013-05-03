package se.kth.inda.indaprojekt;

import se.kth.inda.indaprojekt.engine.GameEngine;
import se.kth.inda.indaprojekt.engine.Level;
import se.kth.inda.indaprojekt.engine.Spell;
import se.kth.inda.indaprojekt.engine.Wizard;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

/**
 * A simple gesturehandler handling gestures from a touchscreen and translates
 * them to actions to the game engine.
 * 
 * @author Mattias
 * 
 */
public class GameGestureHandler extends SimpleOnGestureListener {

	private final GameEngine engine;

	/**
	 * @param e
	 *            The {@link GameEngine} to relay events to.
	 */
	public GameGestureHandler(GameEngine e) {
		engine = e;
		
	}

	/** Teleport the wizard to the doubletapped location. */
	@Override
	public boolean onDoubleTap(MotionEvent event) {
		Level level = engine.getCurrentLevel();
		Wizard[] wizards = level.getWizards();
		if (wizards.length > 0) {
			Spell[] spells = wizards[0].getSpellbook();
			wizards[0]
					.attemptToCastSpell(spells[1], event.getX(), event.getY());
		}
		return super.onDoubleTap(event);
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		Level level = engine.getCurrentLevel();
		Wizard[] wizards = level.getWizards();
		if (wizards.length > 0) {
			Spell[] spells = wizards[0].getSpellbook();
			wizards[0]
					.attemptToCastSpell(spells[0], e2.getX(), e2.getY());
		}
		return super.onFling(e1, e2, velocityX, velocityY);
	}

	/** Send knockback in the specified direction. */
	@Override
	public boolean onSingleTapConfirmed(MotionEvent event) {
		Level level = engine.getCurrentLevel();
		Wizard[] wizards = level.getWizards();
		if (wizards.length > 0) {
			Spell[] spells = wizards[0].getSpellbook();
			wizards[0]
					.attemptToCastSpell(spells[2], event.getX(), event.getY());
		}
		return super.onSingleTapConfirmed(event);
	}

}
