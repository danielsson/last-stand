package se.kth.inda.indaprojekt;

import se.kth.inda.indaprojekt.engine.Wizard;
import se.kth.inda.indaprojekt.engine.spells.SpellType;
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

	private Wizard wizard;
	
	public void setWizard(Wizard w){
		wizard = w;
	}

	/** Teleport the wizard to the doubletapped location. */
	@Override
	public boolean onDoubleTap(MotionEvent event) {
		castSpell(SpellType.TELEPORT, event.getX(), event.getY());

		return super.onDoubleTap(event);
	}
	
	/** Send chockwave in the specified direction. */
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		castSpell(SpellType.SHOCKWAVE, e2.getX(), e2.getY());
		
		return super.onFling(e1, e2, velocityX, velocityY);
	}

	/** Send fireball */
	@Override
	public boolean onSingleTapConfirmed(MotionEvent event) {
		castSpell(SpellType.FIREBALL, event.getX(), event.getY());
		
		return super.onSingleTapConfirmed(event);
	}
	
	private void castSpell(SpellType type, float x, float y){
		if(wizard != null){
			if (!wizard.isDead()) {
				wizard.attemptToCastSpell(type, x, y);
			}
		}
	}

}
