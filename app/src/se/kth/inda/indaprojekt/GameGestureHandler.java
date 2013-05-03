package se.kth.inda.indaprojekt;

import se.kth.inda.indaprojekt.engine.GameEngine;
import se.kth.inda.indaprojekt.engine.Level;
import se.kth.inda.indaprojekt.engine.Spell;
import se.kth.inda.indaprojekt.engine.Wizard;
import android.view.MotionEvent;

public class GameGestureHandler {
	
	public void handle(MotionEvent event, GameEngine engine, GameSurfaceView surface) {
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			Level level = engine.getCurrentLevel();
			Wizard[] wizards = level.getWizards();
			if(wizards.length > 0){
				Spell[] spells = wizards[0].getSpellbook();
				wizards[0].attemptToCastSpell(spells[2], event.getX(), event.getY());
			}
		}
	}
}
