package se.kth.inda.indaprojekt.engine.projectiles;

import java.util.LinkedList;

import se.kth.inda.indaprojekt.engine.Projectile;
import se.kth.inda.indaprojekt.engine.Unit;
import se.kth.inda.indaprojekt.engine.WorldObject;
import android.graphics.Paint;
import android.graphics.Paint.Style;


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
	
	@Override
	public Paint getPaint() {
		Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
		p.setARGB(255, 0, 0, 255);
		p.setStyle(Style.STROKE);
		p.setStrokeWidth(25);
		return p;
	}

}
