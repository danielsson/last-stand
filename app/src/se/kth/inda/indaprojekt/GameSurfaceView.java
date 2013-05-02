package se.kth.inda.indaprojekt;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * TODO: document your custom view class.
 */
public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

	public GameSurfaceView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
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
	
	public void updateSurfaceView() {
		
	}
	
	private static class GameThread extends Thread {
		
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
					sleep(sleepTime);
					GameSurfaceView.this.
				}
			}
		}
		
	}
	
}