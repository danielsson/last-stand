package se.wizard.laststand.engine;


public abstract class LevelSpawner {
	
	private int enemiesStandby, spawnDelay, currentTick = 0;
	
	public LevelSpawner(int numberOfEnemies, int spawnDelay){
		enemiesStandby = numberOfEnemies;
		this.spawnDelay = spawnDelay;
	}
	
	public boolean spawningDone(){
		return enemiesStandby < 1;
	}
	
	public boolean checkSpawning(Level l){
		if(!spawningDone()){
			currentTick++;
			if(spawnDelay <= currentTick){
				currentTick = 0;
				enemiesStandby--;
				spawnEnemy(l);
				return true;
			}
		}
		return false;
	}
	
	protected abstract void spawnEnemy(Level l);
}
