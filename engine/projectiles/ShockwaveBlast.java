package engine.projectiles;

import java.util.LinkedList;

import engine.Projectile;
import engine.Unit;
import engine.WorldObject;

public class ShockwaveBlast extends Projectile{

	private int knockbackDuration;
	private double knockbackForce;
	private LinkedList<Unit> unitsHit = new LinkedList<Unit>();
	
	public ShockwaveBlast(WorldObject caster, double radius, double xTarget,
			double yTarget, double speed, int knockBackDuration, double knockbackForce) {
		super(caster, radius, xTarget, yTarget, speed);
		this.knockbackDuration = knockBackDuration;
		this.knockbackForce = knockbackForce;
	}

	@Override
	public void onImpact(Unit u) {
		unitsHit.add(u);
		u.applyKnockback(getX(), getY(), knockbackForce, knockbackDuration);
	}

	@Override
	public boolean isRelevantTarget(Unit u) {
		return !unitsHit.contains(u) && getCaster() != u;
	}

}
