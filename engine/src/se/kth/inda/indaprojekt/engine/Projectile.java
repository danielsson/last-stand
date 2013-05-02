package se.kth.inda.indaprojekt.engine;

/**
 * A Projectile is a WorldObject that travels in a straight line 
 * towards and through its' target position. The Projectile will hit
 * any Unit that isn't its' caster. Upon impact it will disappear.
 * 
 * @author David Masko
 *
 */
public abstract class Projectile extends WorldObject implements UnitImpacter{

	//The source of the projectile
	private WorldObject caster;
	
	//The angle of movement
	private double angle;
	
	//The speed of movement
	private double speed;
	
	/**
	 * Creates a Projectile at its' caster's position. The Projectile files 
	 * in a straight line with the given speed. The Projectile doesn't stop 
	 * at the target position. The target position is only used to get the angle
	 * the Projectile should travel.
	 * 
	 * @param caster The source of the Projectile.
	 * @param radius The radius of the Projectile's circular shape.
	 * @param xTarget The x-position it is aimed towards .
	 * @param yTarget The y-position it is aimed towards.
	 * @param speed The speed it flies with.
	 */
	public Projectile(WorldObject caster, double radius, double xTarget, double yTarget, double speed) {
		super(caster.getX(), caster.getY(), radius, 200);
		this.caster = caster;
		this.speed = speed;
		angle = Math.atan2(yTarget-getX(),xTarget-getY());
	}

	@Override
	public void onTick() {
		moveTowardsAngle(angle, speed);
	}
	
	/**
	 * The caster of this Projectile. 
	 */
	public WorldObject getCaster(){
		return caster;
	}

}
