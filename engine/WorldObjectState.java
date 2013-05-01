package engine;

/**
 * Defines the state of a WorldObject
 * 
 * @author David Masko
 *
 */
public enum WorldObjectState {
	
	/**
	 * Should be drawn and interacted with.
	 */
	ACTIVE(), 
	
	/**
	 * Should not be drawn but interacted with.
	 */
	HIDDEN(), 
	
	/**
	 * Should not be drawn nor interacted with.
	 */
	SECLUDED(),
	
	/**
	 * Will be secluded forever, remove it.
	 */
	OBSOLETE();
}
