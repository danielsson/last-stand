package se.wizard.laststand;

import se.wizard.laststand.engine.GameEngine;
import se.wizard.laststand.engine.Level;
import se.wizard.laststand.engine.Wizard;
import se.wizard.laststand.engine.WorldObject;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
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
	
	public interface OnSurfaceCreatedListener {
		public GameEngine onSurfaceCreated(GameSurfaceView view);
	}
	
	private OnSurfaceCreatedListener listener;
	private SurfaceHolder surfaceHolder;

	//TODO temp until background is made
	private Paint background = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	
	private GameEngine engine;
	private GameThread thread;

	public GameSurfaceView(Context context, AttributeSet attr) {
		super(context, attr);

		background.setARGB(30, 0, 0, 0);
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
	
	public void setOnSurfaceCreatedListener(OnSurfaceCreatedListener l) {
		listener = l;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// Pass
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		listener.onSurfaceCreated(this);
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
	 * Make sure that the surface is valid before this method is called.
	 */
	public void updateSurfaceView() {
		Canvas canvas = null;
		
		try {
			canvas = surfaceHolder.lockCanvas();

			synchronized (surfaceHolder) {
				drawStuff(canvas);
			}
		} finally {
			if (canvas != null)
				surfaceHolder.unlockCanvasAndPost(canvas);
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

		Level level = engine.getCurrentLevel();
		WorldObject[] objects = level.getWorldObjects(false);

		// Flush
		//TODO Tmp until background added
		canvas.drawPaint(background);

		// render objects
		for (WorldObject wo : objects) {
			wo.paintWorldObject(canvas);
		}
		
		//Render health and mana Bar
		Wizard z = level.getWizards()[0];
		Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
		p.setARGB(255,75,0,0);
		p.setStyle(Style.FILL);
		
		int yHeight = getHeight()-50;
		int xMiddle = getWidth()/2;
		p.setARGB(255,75,0,0);
		canvas.drawRect(0, yHeight, xMiddle, getHeight(), p);
		p.setARGB(255,255,0,0);
		canvas.drawRect(0, yHeight, (float) (xMiddle*((double)(z.getHealth())/z.getMaxHealth())), getHeight(), p);
		
		p.setARGB(255,0,0,75);
		canvas.drawRect(xMiddle, yHeight, getWidth(), getHeight(), p);
		p.setARGB(255,0,0,255);
		canvas.drawRect((float) (getWidth()-xMiddle*((double)(z.getMana())/z.getMaxMana())), yHeight, getWidth(), getHeight(), p);
	}

	
	public void onResume() {
		if(engine.getCurrentLevel() == null)
			return; // The surface has never been started, so there is nothing to resume.
		
		if(!thread.isRunning()) {
			thread = new GameThread();
			thread.setRunning(true);
			thread.start();
		}
	}
	
	public void onPause() {
		if(thread.isRunning()) {
			try{ 
				thread.setRunning(false);
				thread.join();
			} catch (InterruptedException e) {}
			
		}
	}
	
	/**
	 * This is a simple thread that causes the view to be re-rendered. The
	 * framerate is determined by android.
	 */
	private class GameThread extends Thread {
		private static final String T_NAME = "GameRendererThread";
		volatile boolean isRunning = false;
		
		public GameThread() {
			setName(T_NAME);
		}
		/**
		 * @return isRunning
		 */
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

		@Override
		public void run() {
			boolean knownValid = false; //Calling isValid is expensive.
			while (isRunning) {
				try {
					if(!(knownValid || surfaceHolder.getSurface().isValid())) {
						/* Profiling revealed that trying to render to a non-valid surface
						 * wasted enough cycles to power a small manned flight to the moon.
						 */
						sleep(100);
						continue;
					}
					
					knownValid = true;
					updateSurfaceView();
					
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}

	}

}