package se.kth.inda.indaprojekt;

import se.kth.inda.indaprojekt.engine.Dimension;
import se.kth.inda.indaprojekt.engine.Enemy;
import se.kth.inda.indaprojekt.engine.GameEngine;
import se.kth.inda.indaprojekt.engine.Level;
import se.kth.inda.indaprojekt.engine.Spell;
import se.kth.inda.indaprojekt.engine.Wizard;
import se.kth.inda.indaprojekt.engine.WorldObject;
import se.kth.inda.indaprojekt.engine.projectiles.ShockwaveBlast;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * This class renders a running {@link GameEngine} to the screen in an efficient
 * manner. Rendering starts when a {@link GameEngine} is supplied to the
 * setGameEngine method.
 * 
 * @author Mattias
 * 
 */
public class GameSurfaceView extends SurfaceView implements
		SurfaceHolder.Callback {

	private SurfaceHolder surfaceHolder;

	private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG); //Temp until worldObjects render themselves.
	private GameEngine engine;
	
	private GameThread thread;

	public GameSurfaceView(Context context, AttributeSet attr) {
		super(context, attr);

		surfaceHolder = getHolder();
		surfaceHolder.addCallback(this);

		setFocusable(true);
	}

	/**
	 * Set the {@link GameEngine} to be rendered. The engine MUST NOT be
	 * running, and should not have any level set.
	 * 
	 * @param e
	 *            The engine to render.
	 */
	public void setGameEngine(GameEngine e) {
		engine = e;

		thread = new GameThread();
		thread.setRunning(true);
		thread.start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// Pass
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (engine != null) {
			engine.setCurrentLevel(
				GameEngine.createLevel(
					new Dimension(
						getWidth(),
						getHeight()),
					30));
			
			engine.run();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		thread.setRunning(false);
		try {
			thread.join();
		} catch (InterruptedException e) {}
	}

	/**
	 * Force redraw of the canvas. This method is thread safe.
	 */
	public void updateSurfaceView() {
		Canvas canvas = null;

		try {
			canvas = getHolder().lockCanvas();

			synchronized (surfaceHolder) {
				drawStuff(canvas);
			}
		} finally {
			if (canvas != null)
				getHolder().unlockCanvasAndPost(canvas);
		}
	}

	/**
	 * Draw all the objects in the engine to the supplied canvas. Make sure that
	 * the canvas is thread exclusive for the duration of this method.
	 * 
	 * @param canvas
	 *            The canvas to draw to.
	 */
	public void drawStuff(Canvas canvas) {
//		Log.d("GSW", "onDraw" + getHolder().getSurface().isValid());

		Level level = engine.getCurrentLevel();
		WorldObject[] objects = level.getWorldObjects(false);

		// Flush
		paint.setARGB(30, 0, 0, 0);
		canvas.drawPaint(paint);

		// render objects
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(12);
		for (WorldObject wo : objects) {
			if(wo instanceof ShockwaveBlast){
				paint.setARGB(225, 0, 0, 255);
			}
			else if(wo instanceof Enemy){
				paint.setARGB(255, 255, 0, 0);
			}
			else{
				paint.setARGB(255, 255, 255, 255);
			}
			canvas.drawCircle((float) wo.getX(), (float) wo.getY(),
					(float) wo.getRadius(), paint);
		}
	}
	
	/**
	 * TODO Temporary input code.
	 */
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

	/**
	 * This is a simple thread that causes the view to be re-rendered. The
	 * framerate is determined by android.
	 */
	private class GameThread extends Thread {

		volatile boolean isRunning = false;

		/**
		 * @return isRunning
		 */
		@SuppressWarnings("unused")
		public synchronized boolean isRunning() {
			return isRunning;
		}

		/**
		 * @param isRunning
		 *            the isRunning to set
		 */
		public synchronized void setRunning(boolean isRunning) {
			this.isRunning = isRunning;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			while (isRunning) {
				try {
					updateSurfaceView();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}

	}

}