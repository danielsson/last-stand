package se.kth.inda.indaprojekt;

import se.kth.inda.indaprojekt.GameSurfaceView.OnSurfaceCreatedListener;
import se.kth.inda.indaprojekt.engine.Dimension;
import se.kth.inda.indaprojekt.engine.GameEngine;
import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class GameActivity extends Activity implements OnSurfaceCreatedListener {
	
	/**
	 * The game engine.
	 */
	private GameEngine engine;
	
	private GameSurfaceView gameSurfaceView;
	private GameGestureHandler gestureHandler = new GameGestureHandler();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
		setContentView(R.layout.activity_fullscreen);
		
		gameSurfaceView = (GameSurfaceView) findViewById(R.id.fullscreen_content2);
		gameSurfaceView.setOnSurfaceCreatedListener(this);
		
		engine = new GameEngine(40);
		
		gameSurfaceView.setGameEngine(engine);
		
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		gestureHandler.handle(event, engine, gameSurfaceView);
		return super.onTouchEvent(event);
	}

	@Override
	public GameEngine onSurfaceCreated(GameSurfaceView view) {
		if(engine.getCurrentLevel() != null)
			return engine;
		
		engine.setCurrentLevel(
				GameEngine.createLevel(
					new Dimension(
						view.getWidth(),
						view.getHeight()),
					30));
			
		engine.run();
		return engine;
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		if(gameSurfaceView != null) gameSurfaceView.onPause();
		engine.pause();
		super.onPause();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		if(gameSurfaceView != null) gameSurfaceView.onResume();
		if(engine.getCurrentLevel() != null) engine.run();
		super.onResume();
	}

}
