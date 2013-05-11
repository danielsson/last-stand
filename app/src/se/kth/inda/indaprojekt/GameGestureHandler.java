package se.kth.inda.indaprojekt;

import java.util.List;

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

	private Wizard wizard;
	
	public void setWizard(Wizard w){
		wizard = w;
	}

	/** Teleport the wizard to the doubletapped location. */
	@Override
	public boolean onDoubleTap(MotionEvent event) {
		castSpell(1 , event.getX(), event.getY());

		return super.onDoubleTap(event);
	}
	
	/** Send knockback in the specified direction. */
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		castSpell(2 , e2.getX(), e2.getY());
		
		return super.onFling(e1, e2, velocityX, velocityY);
	}

	/** Send fireball */
	@Override
	public boolean onSingleTapConfirmed(MotionEvent event) {
		castSpell(0 , event.getX(), event.getY());
		
		return super.onSingleTapConfirmed(event);
	}
	
	private void castSpell(int index, float x, float y){
		if(wizard != null){
			if (!wizard.isDead()) {
				List<Spell> spells = wizard.getSpellbook();
				if(index < spells.size())
					wizard.attemptToCastSpell(spells.get(index), x, y);
			}
		}
	}

}
