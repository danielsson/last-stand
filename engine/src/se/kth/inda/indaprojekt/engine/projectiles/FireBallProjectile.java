package se.kth.inda.indaprojekt.engine.projectiles;

import se.kth.inda.indaprojekt.engine.Projectile;
import se.kth.inda.indaprojekt.engine.Unit;
import se.kth.inda.indaprojekt.engine.WorldObject;
import se.kth.inda.indaprojekt.engine.WorldObjectState;

public class FireBallProjectile extends Projectile{
	
	private int damage;
	
	public FireBallProjectile(WorldObject caster, double radius,
			double xTarget, double yTarget, double speed, int damage) {
		super(caster, radius, xTarget, yTarget, speed);
		
		this.damage = damage;
	}

	@Override
	public void onImpact(Unit u) {
		u.inflictDamage(damage);
		setState(WorldObjectState.OBSOLETE);
	}

	@Override
	public boolean isRelevantTarget(Unit u) {
		return getCaster() != u ;
	}

}
