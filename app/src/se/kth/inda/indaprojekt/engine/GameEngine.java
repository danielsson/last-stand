package se.kth.inda.indaprojekt.engine;

import se.kth.inda.indaprojekt.engine.spells.FireBall;
import se.kth.inda.indaprojekt.engine.spells.ShockwaveSpell;
import se.kth.inda.indaprojekt.engine.spells.TeleportSpell;

//TODO: DOC
public class GameEngine {
	/**
	 * Interface definition for a callback to be invoked when
	 * en event has taken place in the engine.
	 */
	public interface GameEngineEventListener {
		/**
		 * Called after a level has been set.
		 * @param level The level that was set.
		 */
		public void onLevelCreated(Level level);
		
		public void onLevelVictory(Level level);
		public void onLevelLost(Level level);
		
	}

	// The level the gameTicker will affect.
	private Level currentLevel;

	// The timer that runs the se.kth.inda.indaprojekt.engine.
	private GameTicker gameTicker;
	
	//The gameeventlistener currently registered.
	private GameEngineEventListener listener;
	
	/**
	 * Creates a GameEngine that progresses Levels forward with the given amount
	 * of ticks per second.
	 * 
	 * @param ticksPerSecond
	 *            How many times the Level is supposed to tick per second while
	 *            this GameEngine is active.
	 */
	public GameEngine(int ticksPerSecond) {
		gameTicker = new GameTicker(1000 / ticksPerSecond);
	}
	
	/**
	 * Creates a GameEngine that progresses Levels forward with the given amount
	 * of ticks per second.
	 * 
	 * @param ticksPerSecond
	 *            How many times the Level is supposed to tick per second while
	 *            this GameEngine is active.
	 * @param l A {@link GameEngineEventListener} to recieve callbacks.
	 */
	public GameEngine(int ticksPerSecond, GameEngineEventListener l) {
		this(ticksPerSecond);
		listener = l;
	}

	/**
	 * Executes what the GameEngine is supposed to do upon the current Level
	 * ends in victory.
	 */
	private void onLevelVictory() {
		gameTicker.stop();
		
		if(listener != null)
			listener.onLevelVictory(getCurrentLevel());
	}

	/**
	 * Executes what the GameEngine is supposed to do upon the current Level
	 * ends in a losing.
	 */
	private void onLevelLost() {
		gameTicker.stop();
		
		if(listener != null)
			listener.onLevelLost(getCurrentLevel());
	}

	/**
	 * The se.kth.inda.indaprojekt.engine will periodically progress the current
	 * Level forward until stopped. Or the Level is finished by loss or victory.
	 * 
	 * @throws IllgegalStateException
	 *             if no current Level has been set for the GameEngine.
	 */
	public void run() {
		if (currentLevel == null)
			throw new IllegalStateException(
					"No existing level in the se.kth.inda.indaprojekt.engine");
		gameTicker.start();
	}

	/**
	 * Pause the periodic progressing of the current Level.
	 */
	public void pause() {
		gameTicker.stop();
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		gameTicker.stop();
	}

	/**
	 * Set the Level the Engine should progress forward while running. Multiple
	 * GameEngines may affect the same level .
	 * 
	 * @param l
	 *            The Level to be progressed by the GameEngine.
	 */
	public void setCurrentLevel(Level l) {
		currentLevel = l;
		
		if(listener != null)
			listener.onLevelCreated(getCurrentLevel());
	}

	/**
	 * The Level the se.kth.inda.indaprojekt.engine currently is progressing.
	 * 
	 * @return The Level the se.kth.inda.indaprojekt.engine currently is
	 *         progressing.
	 */
	public Level getCurrentLevel() {
		return currentLevel;
	}

	private class GameTicker {

		private boolean running = false;
		private int delay;
		private Thread t;

		public GameTicker(final int delay) {
			this.delay = delay;
			t = makeNewThread(delay);
		}

		public void stop() {
			running = false;
		}

		public void start() {
			running = true;
			if (!t.isAlive()) {
				t = makeNewThread(delay);
				t.start();
			}
		}

		private Thread makeNewThread(final int delay) {
			return new Thread(new Runnable() {

				@Override
				public void run() {
					while (running) {
						periodicEvent();
						try {
							Thread.sleep(delay);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}

			});
		}

		public void periodicEvent() {
			if (currentLevel != null) {
				currentLevel.levelTick();
				if (currentLevel.getState() == LevelState.VICTORY)
					onLevelVictory();
				else if (currentLevel.getState() == LevelState.GAMEOVER)
					onLevelLost();
			} else
				throw new IllegalStateException(
						"No existing level in the se.kth.inda.indaprojekt.engine");
		}
	}
}
