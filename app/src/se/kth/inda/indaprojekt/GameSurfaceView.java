package se.kth.inda.indaprojekt;

import se.kth.inda.indaprojekt.engine.Dimension;
import se.kth.inda.indaprojekt.engine.GameEngine;
import se.kth.inda.indaprojekt.engine.Level;
import se.kth.inda.indaprojekt.engine.WorldObject;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * TODO: document your custom view class.
 */
public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
	private SurfaceHolder surfaceHolder;
	
	private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private GameEngine engine = new GameEngine(40);

	public GameSurfaceView(Context context, AttributeSet attr) {
		super(context, attr);
		
		engine.setCurrentLevel(GameEngine.createLevel(new Dimension(100, 100), 30));
		engine.run();
		
		surfaceHolder = getHolder();
		surfaceHolder.addCallback(this);
		
		GameThread thread = new GameThread(40);
		thread.setRunning(true);
		thread.start();
		
		setFocusable(true);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}
	
	@SuppressLint("WrongCall")
	public void updateSurfaceView() {
		Canvas canvas = null;
		
		try {
			canvas = getHolder().lockCanvas();
			
			
			synchronized (surfaceHolder) {
				drawStuff(canvas);
			}
		} finally {
			if(canvas != null)
				getHolder().unlockCanvasAndPost(canvas);
		}
	}
	
	
	public void drawStuff(Canvas canvas) {
		Log.d("GSW", "onDraw" +
				getHolder().getSurface().isValid());

		//Level level = engine.getCurrentLevel();
		//WorldObject[] objects = level.getWorldObjects(false);
		
		//Flush
		paint.setARGB(1, 1, 0, 0);
		canvas.drawPaint(paint);
		
		//render objects
		//paint.setARGB(1, 0, 0, 0);
	//	paint.setStyle(Style.STROKE);
		//paint.setStrokeWidth(5);
		//for(WorldObject wo : objects) {
		//	canvas.drawCircle((float) wo.getX(), (float) wo.getY(), (float) wo.getRadius(), paint);
		//}
	}

	private class GameThread extends Thread {
		
		volatile boolean isRunning = false;
		long sleepTime = 0;
		
		public GameThread(long st) {
			sleepTime = st;
		}

		/**
		 * @return the isRunning
		 */
		public synchronized boolean isRunning() {
			return isRunning;
		}

		/**
		 * @param isRunning the isRunning to set
		 */
		public synchronized void setRunning(boolean isRunning) {
			this.isRunning = isRunning;
		}

		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			while(isRunning) {
				try {
					//sleep(sleepTime);
					updateSurfaceView();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}
		
	}
	
	
	
}