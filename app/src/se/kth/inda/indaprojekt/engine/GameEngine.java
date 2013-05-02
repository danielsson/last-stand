package se.kth.inda.indaprojekt.engine;


import se.kth.inda.indaprojekt.engine.spells.FireBall;
import se.kth.inda.indaprojekt.engine.spells.ShockwaveSpell;
import se.kth.inda.indaprojekt.engine.spells.TeleportSpell;


public class GameEngine {
		
	//The level the gameTicker will affect.
	private Level currentLevel;
	
	//The timer that runs the se.kth.inda.indaprojekt.engine.
	private GameTicker gameTicker;
	
	/**
	 * Creates a GameEngine that progresses Levels forward with the
	 * given amount of ticks per second.
	 * 
	 * @param ticksPerSecond How many times the Level is supposed to tick
	 * per second while this GameEngine is active.
	 */
	public GameEngine(int ticksPerSecond){
		gameTicker = new GameTicker(1000/ticksPerSecond);
	}
	
	/**
	 * TODO DOC + CODE
	 * @param d
	 * @param enemies
	 * @return
	 */
	public static Level createLevel(Dimension d, int enemies){
		Level l = new Level(d);
		
		Spell[] spells = new Spell[3];
		spells[0] = new FireBall(l,5);
		spells[1] = new TeleportSpell(l);
		spells[2] = new ShockwaveSpell(l);
		Wizard w = new Wizard(d.getWidth()/2, d.getHeight()/2, 5, 10, spells, 10, 0.1);
		l.addWorldObject(w);
		
		for (int i = 0; i < enemies; i++) {
			l.addWorldObject(new Enemy((d.getWidth()/enemies)*(i+1),-15 , 5, 5, 1, w));
		}
		
		return l;
	}
	
	/**
	 * Executes what the GameEngine is supposed to do upon
	 * the current Level ends in victory.
	 */
	private void onLevelVictory(){
		gameTicker.stop();
		//TODO
	}
	
	/**
	 * Executes what the GameEngine is supposed to do upon
	 * the current Level ends in a losing.
	 */
	private void onLevelLost(){
		gameTicker.stop();
		//TODO
		
	}
	
	/**
	 * The se.kth.inda.indaprojekt.engine will periodically progress the current Level forward until stopped.
	 * Or the Level is finished by loss or victory.
	 * 
	 * @throws IllgegalStateException if no current Level has been set for the GameEngine.
	 */
	public void run(){
		if(currentLevel == null)
			throw new IllegalStateException("No existing level in the se.kth.inda.indaprojekt.engine");
		gameTicker.start();
	}
	
	/**
	 * Pause the periodic progressing of the current Level.
	 */
	public void pause(){
		gameTicker.stop();
	}
	
	@Override
	protected void finalize() throws Throwable{
		super.finalize();
		gameTicker.stop();
	}
	
	/**
	 * Set the Level the Engine should progress forward while running.
	 * Multiple GameEngines may affect the same level .
	 * 
	 * @param l The Level to be progressed by the GameEngine.
	 */
	public void setCurrentLevel(Level l){
		currentLevel = l;
	}
	
	/**
	 * The Level the se.kth.inda.indaprojekt.engine currently is progressing.
	 * 
	 * @return The Level the se.kth.inda.indaprojekt.engine currently is progressing.
	 */
	public Level getCurrentLevel(){
		return currentLevel;
	}
	
	private class GameTicker{
		
		private boolean running = false;
		private Thread t;
		
		public GameTicker(final int delay){
			t = new Thread(new Runnable(){

				@Override
				public void run() {
					while(running){
						periodicEvent();
						try {Thread.sleep(delay);} catch (InterruptedException e) {e.printStackTrace();}
					}
				}
				
			});
		}
		
		public void stop(){
			running = false;
		}
		
		public void start(){
			running = true;
			if(!t.isAlive())
				t.start();
		}
		
		public void periodicEvent(){
			if(currentLevel != null){
				currentLevel.levelTick();
				if(currentLevel.getState() == LevelState.VICTORY)
					onLevelVictory();
				else if(currentLevel.getState() == LevelState.GAMEOVER)
					onLevelLost();
			}
			else
				throw new IllegalStateException("No existing level in the se.kth.inda.indaprojekt.engine");
		}
	}
}
