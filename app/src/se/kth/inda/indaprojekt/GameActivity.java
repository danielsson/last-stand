package se.kth.inda.indaprojekt;

import se.kth.inda.indaprojekt.engine.GameEngine;
import se.kth.inda.indaprojekt.engine.Level;
import se.kth.inda.indaprojekt.engine.Spell;
import se.kth.inda.indaprojekt.engine.Wizard;
import se.kth.inda.indaprojekt.util.SystemUiHider;
import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class GameActivity extends Activity {
	
	/**
	 * The game engine.
	 */
	private GameEngine engine;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
		setContentView(R.layout.sample_game_surface_view);
		
		GameSurfaceView gamesSurfaceView = (GameSurfaceView) findViewById(R.id.fullscreen_content2);
		
		engine = new GameEngine(40);
		
		gamesSurfaceView.setGameEngine(engine);
		
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_UP){
			Level level = engine.getCurrentLevel();
			Wizard[] wizards = level.getWizards();
			if(wizards.length > 0){
				Spell[] spells = wizards[0].getSpellbook();
				wizards[0].attemptToCastSpell(spells[2], event.getX(), event.getY());
			}
		}
		return super.onTouchEvent(event);
	}

}
