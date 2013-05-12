package se.wizard.laststand.engine;

public class KnockbackEffect {
	
	private WorldObject target;
	private double angle;
	private double speed;
	private int duration;
	private double decrease;
	
	public KnockbackEffect(WorldObject target, double originX, double originY, double speed, int duration){
		this.target = target;
		angle = Math.atan2(target.getY()-originY,target.getX()-originX);
		this.speed = speed;
		this.duration = duration;
		decrease = speed/duration;
	}
	
	/**
	 * Applies the Knockback effect on its' target.
	 * @throws IllegalStateException if the duration has been exceeded.
	 * 
	 */
	public void knockBack() throws IllegalStateException{
		if(!finished()){
			target.moveTowardsAngle(angle, speed);
			duration--;
			speed-=decrease;
		}
		else
			throw new IllegalStateException("Duration is over");
	}
	
	/**
	 * If the Knockback has finished it's effect.
	 * A Knockback that's finished won't affect
	 * its' target when knockBack() is called.
	 * 
	 * @return True if the knockback is finished, false
	 * otherwise.
	 */
	public boolean finished(){
		return duration < 1;
	}

}
