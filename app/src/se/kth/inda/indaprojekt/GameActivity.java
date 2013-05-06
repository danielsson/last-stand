package se.kth.inda.indaprojekt;

import se.kth.inda.indaprojekt.GameSurfaceView.OnSurfaceCreatedListener;
import se.kth.inda.indaprojekt.engine.Dimension;
import se.kth.inda.indaprojekt.engine.GameEngine;
import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.TextView;

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
	private GestureDetector gestureHandler;
	
	private LinearLayout upgradeView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_fullscreen);
		
		gameSurfaceView = (GameSurfaceView) findViewById(R.id.fullscreen_content2);
		gameSurfaceView.setOnSurfaceCreatedListener(this);
		
		upgradeView = (LinearLayout) findViewById(R.id.upgradeLayout);
		
		engine = new GameEngine(40);
		
		gameSurfaceView.setGameEngine(engine);
		gestureHandler = new GestureDetector(this, new GameGestureHandler(engine));
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return gestureHandler.onTouchEvent(event);
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

	@Override
	protected void onPause() {
		if(gameSurfaceView != null) gameSurfaceView.onPause();
		engine.pause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		if(gameSurfaceView != null) gameSurfaceView.onResume();
		if(engine.getCurrentLevel() != null) engine.run();
		super.onResume();
	}
	
	public void displayUpgradeView() {
		upgradeView.setVisibility(LinearLayout.VISIBLE);
		TextView txtNumPoints = (TextView) upgradeView.findViewById(R.id.txtNumPoints);
		txtNumPoints.setText(
				String.format(getResources().getString(R.string.num_points), 55)
			);
		
	}

}
