package se.kth.inda.indaprojekt;

import java.util.LinkedList;
import java.util.Queue;

import se.kth.inda.indaprojekt.engine.GameEngine;
import se.kth.inda.indaprojekt.engine.Level;
import se.kth.inda.indaprojekt.engine.Spell;
import se.kth.inda.indaprojekt.engine.Wizard;
import android.util.Log;
import android.view.MotionEvent;

public class GameGestureHandler {
	private final static int QUEUE_MAX_SIZE = 50;
	private final static String LOG_TAG = "GestureHandler";
	
	private Queue<MotionEvent> events = new LinkedList<MotionEvent>();
	
	
	public void handle(MotionEvent event, GameEngine engine, GameSurfaceView surface) {
		
		switch(event.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
			Level level = engine.getCurrentLevel();
			Wizard[] wizards = level.getWizards();
			if(wizards.length > 0){
				Spell[] spells = wizards[0].getSpellbook();
				wizards[0].attemptToCastSpell(spells[2], event.getX(), event.getY());
			}
			break;
		case MotionEvent.ACTION_UP:
			Log.d(LOG_TAG, Integer.toString(events.size()));
			
			if(events.size() > 2) {
				
			}
				
				
			events.clear();
		}
		
		
		
		if(events.size() > QUEUE_MAX_SIZE)
			events.poll();
		
		events.add(event);
	}
}
