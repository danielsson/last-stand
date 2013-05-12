package se.wizard.laststand;

import se.wizard.laststand.R;
import se.wizard.laststand.GameSurfaceView.OnSurfaceCreatedListener;
import se.wizard.laststand.UpgradeView.OnUpgradedListener;
import se.wizard.laststand.engine.Dimension;
import se.wizard.laststand.engine.GameEngine;
import se.wizard.laststand.engine.Level;
import se.wizard.laststand.engine.Wizard;
import se.wizard.laststand.engine.GameEngine.GameEngineEventListener;
import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

/**
 * An activity that runs a gameengine and renders it using a {@link GameSurfaceView}.
 */
public class GameActivity extends Activity implements OnSurfaceCreatedListener, OnUpgradedListener {
	
	/**
	 * The game engine.
	 */
	private GameEngine engine;
	
	private GameSurfaceView gameSurfaceView;
	private GestureDetector gestureHandler;
	private GameGestureHandler gameGestureHandler;
	
	private UpgradeView upgradeView;
	
	private LevelProgressHandler levelHandler;
	
	/**
	 * This handles all gamecycle events from the game engine. That is, victory
	 * and failure.
	 */
	GameEngineEventListener gameEventListener = new GameEngineEventListener() {
		
		@Override
		public void onLevelVictory(Level level) {
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					if(levelHandler.hasNextLevel()){
						upgradeView.show(levelHandler.getNumPoints());
					}
				}
			});
		}
		
		@Override
		public void onLevelLost(Level level) {
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					Toast.makeText(GameActivity.this, R.string.game_over, Toast.LENGTH_LONG).show();
				}
			});
		}
		
		@Override
		public void onLevelCreated(Level level) {
			// PASS
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_fullscreen);
		
		gameSurfaceView = (GameSurfaceView) findViewById(R.id.fullscreen_content2);
		gameSurfaceView.setOnSurfaceCreatedListener(this);
		
		upgradeView = (UpgradeView) findViewById(R.id.upgradeLayout);
		upgradeView.create(this);
		
		
		engine = new GameEngine(40, gameEventListener);
		
		gameSurfaceView.setGameEngine(engine);
		gameGestureHandler = new GameGestureHandler();
		gestureHandler = new GestureDetector(this, gameGestureHandler);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return gestureHandler.onTouchEvent(event);
	}

	@Override
	public GameEngine onSurfaceCreated(GameSurfaceView view) {
		if(engine.getCurrentLevel() != null)
			return engine;
		
		levelHandler = new LevelProgressHandler(new Dimension(view.getWidth(),view.getHeight()), 30, 5);
		upgradeView.setLevelHandler(levelHandler);
		
		initiateNextLevel();
		
		return engine;
	}

	@Override
	protected void onPause() {
		if(gameSurfaceView != null) gameSurfaceView.onPause();
		engine.pause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		if(gameSurfaceView != null)
			gameSurfaceView.onResume();
		if(engine.getCurrentLevel() != null) 
			engine.run();
		super.onResume();
	}
	
	/**
	 * Helper method to start the next level. It makes sure
	 * that the engine and gesturehandler are still with us.
	 */
	private void initiateNextLevel(){
		
		Wizard w = levelHandler.addWizard();
		Level l = levelHandler.getLevel();
		gameGestureHandler.setWizard(w);
		
		engine.setCurrentLevel(l);
		
		onResume();
	}

	@Override
	public void onUpgraded() {
		upgradeView.hide();
		
		levelHandler.moveToNextLevel();
		initiateNextLevel();
	}

}
