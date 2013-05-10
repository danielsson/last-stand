package se.kth.inda.indaprojekt.engine;

/**
 * This is far from the most elegant solution, but for the moment it will have
 * to make do.
 * 
 */
public class GameState {
	volatile int currentLevel = 0;
	volatile boolean hasTeleportUpgrade = false;
	volatile boolean hasPushUpgrade = false;
	public synchronized int getCurrentLevel() {
		return currentLevel;
	}
	public synchronized void setCurrentLevel(int currentLevel) {
		this.currentLevel = currentLevel;
	}
	public synchronized boolean hasTeleportUpgrade() {
		return hasTeleportUpgrade;
	}
	public synchronized void setHasTeleportUpgrade(boolean hasTeleportUpgrade) {
		this.hasTeleportUpgrade = hasTeleportUpgrade;
	}
	public synchronized boolean hasPushUpgrade() {
		return hasPushUpgrade;
	}
	public synchronized void setHasPushUpgrade(boolean hasPushUpgrade) {
		this.hasPushUpgrade = hasPushUpgrade;
	}
}
