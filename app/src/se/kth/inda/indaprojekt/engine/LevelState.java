package se.kth.inda.indaprojekt.engine;

public enum LevelState {

	/**
	 * The Level has been created and hasn't been affected by any input.
	 */
	UNMODIFIED,
	
	/**
	 * The Level has been modified by input and is considered
	 * active running.
	 */
	RUNNING,
	
	/**
	 * The Level has reached the conditions to make the player(s) victorious.
	 */
	VICTORY,
	
	/**
	 * The Level has reached the conditions for failure.
	 */
	GAMEOVER;
	
	
}
