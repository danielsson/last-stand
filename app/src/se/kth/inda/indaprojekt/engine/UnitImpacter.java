package se.kth.inda.indaprojekt.engine;

/**
 * A UnitImpacter is an object that can hit other Units and
 * affect them on impact.
 * 
 * @author David Masko
 *
 */
public interface UnitImpacter {
	
	/**
	 * Executes the effects that should happen on the impacted
	 * Unit when this UnitImpacter hits it.
	 * 
	 * @param u The Unit that has been hit.
	 */
	public void onImpact(Unit u);
	
	/**
	 * Checks if the given unit could be hit by this UnitImpacter
	 * and therefore if it is worth checking the Unit for impact.
	 * 
	 * @param u The unit to check.
	 * @return True if the Unit could be hit by the UnitImpacter,
	 * returns false otherwise.
	 */
	public boolean isRelevantTarget(Unit u);

}
