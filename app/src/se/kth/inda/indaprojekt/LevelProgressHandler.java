package se.kth.inda.indaprojekt;

import java.util.Random;

import android.util.Log;

import se.kth.inda.indaprojekt.engine.Dimension;
import se.kth.inda.indaprojekt.engine.Enemy;
import se.kth.inda.indaprojekt.engine.Level;
import se.kth.inda.indaprojekt.engine.LevelSpawner;
import se.kth.inda.indaprojekt.engine.Spell;
import se.kth.inda.indaprojekt.engine.Wizard;
import se.kth.inda.indaprojekt.engine.spells.FireBall;
import se.kth.inda.indaprojekt.engine.spells.ShockwaveSpell;
import se.kth.inda.indaprojekt.engine.spells.TeleportSpell;

public class LevelProgressHandler {
	
	private Level[] levels;
	private int currentLevel = 0;
	
	private boolean hasForcePushUpgrade = false;
	private boolean hasTeleportUpgrade = false;
		
	public synchronized boolean hasForcePushUpgrade() {
		return hasForcePushUpgrade;
	}

	public synchronized void setHasForcePushUpgrade(boolean hasForcePushUpgrade) {
		this.hasForcePushUpgrade = hasForcePushUpgrade;
	}

	public synchronized boolean hasTeleportUpgrade() {
		return hasTeleportUpgrade;
	}

	public synchronized void setHasTeleportUpgrade(boolean hasTeleportUpgrade) {
		this.hasTeleportUpgrade = hasTeleportUpgrade;
	}

	public LevelProgressHandler(Dimension levelSize, int levels, int difficulty){
		this.levels = createLevels(levelSize, levels, difficulty);
	}
	
	public boolean hasNextLevel(){
		return currentLevel < levels.length-1;
	}
	
	public void moveToNextLevel(){
		currentLevel++;
	}
	
	public Wizard addWizard(){
		Level l = getLevel();
		if(l == null)
			return null;
		
		Spell[] spells = new Spell[3];
		spells[0] = new FireBall(l, 8.5);
		
		if(hasTeleportUpgrade)
			spells[1] = new TeleportSpell(l);
		
		if(hasForcePushUpgrade)
			spells[2] = new ShockwaveSpell(l);
		
		Wizard w = new Wizard(l.getSize().getWidth() / 2, l.getSize().getHeight() / 2, 20, 10, spells, 10, 0.075);
		
		l.addWorldObject(w);
		return w;
	}
	
	public Level getLevel(){
		if(currentLevel >= levels.length)
			return null;
		return levels[currentLevel];
	}
	
	private Level[] createLevels(Dimension size, int amount, int difficulty){
		Level[] lvls = new Level[amount];
		for (int i = 0; i < lvls.length ; i++) {
			lvls[i] = new Level(size, new RandomSpawner((int) (1+i+(i*difficulty*0.2)), (int) (75-i*difficulty), difficulty, i));
		}
		return lvls;
	}
	
	private static class RandomSpawner extends LevelSpawner{
		
		private static Random random = new Random();
		
		private int difficulty, level;

		public RandomSpawner(int numberOfEnemies, int spawnDelay, int difficulty, int level) {
			super(numberOfEnemies, spawnDelay);
			this.difficulty = difficulty;
			this.level = level;
		}

		@Override
		protected void spawnEnemy(Level l) {
			
			int offset = 25;
			int x =  - offset;
			int y =  - offset;
			int a = random.nextInt(4);
			if(a == 0){
				x+=random.nextFloat()*(l.getSize().getWidth()+offset*2);
			}
			else if(a == 1){
				y+=random.nextFloat()*(l.getSize().getHeight()+offset*2);
			}
			else if(a == 2){
				x+=random.nextFloat()*(l.getSize().getWidth()+offset*2);
				y+=l.getSize().getHeight()+offset*2;
			}
			else if(a == 3){
				y+=random.nextFloat()*(l.getSize().getHeight()+offset*2);
				x+=l.getSize().getWidth()+offset*2;
			}
						
			Enemy e = new Enemy(x, y, 30-difficulty-level*0.35, (int) (difficulty+level*0.3), (int) (1+difficulty*0.3+level*0.1));
			l.addWorldObject(e);
		}
		
	}
	
	/**
	 * @return The amount of points available to the player at the current level.
	 */
	public int getNumPoints() {
		return 15 + (currentLevel + 2) * 100; //TODO: These numbers were randomly selected.
	}

}
