package se.kth.inda.indaprojekt.engine;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Every existing object in a level is a WorldObject. A WorldObject is an object that has a
 * position in the world and a given radius - all WorldObjects has the shape of a circle.
 * 
 * @author David Masko
 * 
 */
public abstract class WorldObject {
	
	private WorldObjectState state = WorldObjectState.ACTIVE;
	private double x,y; // position.
	private double radius; // all worldobjects are circles.
	private int drawPriority; // A higher drawpriority makes an object appear above other objects.
	private ArrayList<KnockbackEffect> knockbacks = new ArrayList<KnockbackEffect>();
	
	private Paint paint;
	
	/**
	 * Creates a new WorldObject with drawPriority 0.
	 * 
	 * @param x The x-position of the WorldObject.
	 * @param y The x-position of the WorldObject.
	 * @param radius The radius of the WorldObject's circular shape.
	 */
	public WorldObject(double x, double y, double radius){
		this(x,y,radius,0);
	}
	
	/**
	 * Creates a new WorldObject.
	 * 
	 * @param x The x-position of the WorldObject.
	 * @param y The x-position of the WorldObject.
	 * @param radius The radius of the WorldObject's circular shape.
	 * @param drawPriority The drawPriority of the WorldObject.
	 */
	public WorldObject(double x, double y, double radius, int drawPriority){
		this.x = x;
		this.y = y;
		this.radius = radius;
		this.drawPriority = drawPriority;
		paint = getPaint();
	}
	
	/**
	 * Defines the change of state of the object every game tick. This generally
	 * includes movement. Collision detection should not be done here.
	 */
	public abstract void onTick();
	
	/**
	 * Get the Paint for this WorldObject.
	 * 
	 * @return a deep cloned instance of this WorldObjects paint.
	 */
	public abstract Paint getPaint();
	
	/**
	 * Draws this WorldObject on the given canvas with its' Paint.
	 * @param canvas
	 */
	public void paintWorldObject(Canvas canvas){
		canvas.drawCircle((float) getX(), (float) getY(), (float) getRadius(), paint);
	}
	
	/**
	 * Moves towards the given position once with the given distance.
	 * If the given distance is longer than the actual space, the WorldObject
	 * will end up on the target position.
	 * 
	 * @param x X-position of the target position.
	 * @param y Y-position of the target position.
	 * @param distance How far the WorldObject should travel.
	 */
	protected void moveTowardsPoint(double x, double y, double distance){
		double deltaX = x-this.x, deltaY = y-this.y;
		double angle = Math.atan2(deltaY,deltaX);
		this.x+=distance*Math.cos(angle);
		this.y+=distance*Math.sin(angle);
		
		
		//Check if object has passed target point
		double newDeltaX = x-this.x, newDeltaY = y-this.y;
		if(newDeltaX*newDeltaY*deltaX*deltaY < 0){ //Object has passed target point.
			this.x = x;
			this.y = y;
		}
	}
	
	/**
	 * Moves the WorldObject the given distance towards the given direction at once.
	 * 
	 * @param angle The direction.
	 * @param distance How far the WorldObject should be moved.
	 */
	protected void moveTowardsAngle(double angle, double distance){
		this.x+=distance*Math.cos(angle);
		this.y+=distance*Math.sin(angle);
	}
	
	/**
	 * Check if this WorldObject intersects with the given worldObject.
	 * 
	 * @param o The object collision is going to be checked towards.
	 * @return True if the given WorldObject intersects with this object.
	 * Returns false otherwise.
	 */
	public boolean checkCollision(WorldObject o){
		double calcX = x-o.getX();
		double calcY = y-o.getY();
		double radius = this.radius+o.getRadius();
		
		return calcX*calcX + calcY*calcY <= radius*radius;
	}
	
	/**
	 * Applies all knockbackEffects on the worldObject.
	 */
	protected void executeKnockbacks(){
		for (int i = 0; i < knockbacks.size(); i++) {
			KnockbackEffect knock = knockbacks.get(i);
			if(knock.finished()){
				knockbacks.remove(knock);
				i--;
				continue;
			}
			knock.knockBack();
		}
	}
	
	/**
	 * Adds a new knockback effekt to the Worldobject. The orldObject
	 * will be pushed away from the origin of the knockback with the given
	 * force during the given duration.
	 * 
	 * @param originX x-Position of the source of the Knockback.
	 * @param originY y-Position of the source of the Knockback.
	 * @param force How fast the target will be pushed by the knockback.
	 * @param duration How many engineTicks the knockback should last.
	 */
	public void applyKnockback(double originX, double originY, double force, int duration){
		KnockbackEffect k = new KnockbackEffect(this, originX, originY, force, duration);
		knockbacks.add(k);
	}
	
	/**
	 * Checks if the object is entirely out of bounds. I.e if the entire
	 * circumference of the object is outside the box created by the (0,0)
	 * position and the (width,height) position.
	 * 
	 * @param width The width of the bounds from coordinate 0.
	 * @param height The height of the bounds from coordinate 0.
	 * @return True if the object is outside the bounds, false otherwise.
	 */
	public boolean isOutOfBounds(double width, double height){
		if(x+radius < 0)
			return true;
		else if(y+radius < 0)
			return true;
		else if(x-radius > width)
			return true;
		else if(y-radius > height)
			return true;
		else
			return false;
	}
	
	/**
	 * Instantly moves to object to the given position.
	 * 
	 * @param x The x-position of the location.
	 * @param y The y-position of the location.
	 */
	public void teleportTo(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Change the state of this WorldObject.
	 * 
	 * @param state The new state of the object
	 */
	protected void setState(WorldObjectState state){
		this.state = state;
	}
	
	/**
	 * The radius of the objects circular shape.
	 * 
	 * @return The radius of the object.
	 */
	public double getRadius(){
		return radius;
	}
	
	/**
	 * The x-position of the objects center.
	 * 
	 * @return the x-position of the objects center.
	 */
	public double getX(){
		return x;
	}
	
	/**
	 * The y-position of the objects center.
	 * 
	 * @return the y-position of the objects center.
	 */
	public double getY(){
		return y;
	}
	
	/**
	 * The drawPriority of an object defines the priority the
	 * object has above other objects to be drawn over them. An object
	 * with a higher draw priority should therefore be drawed after an object
	 * with a lower drawPriority. In the case of equality there is no rule
	 * except that consistency should be adopted.
	 * 
	 * @return the drawPriority of the WorldObject.
	 */
	public int getDrawPriority(){
		return drawPriority;
	}
	
	/**
	 * Get the state of this WorldObject;
	 * 
	 * @return The state of the WorldObject.
	 */
	public WorldObjectState getState(){
		return state;
	}
	
	/**
	 * If the WorldObject currently is affected by any
	 * knockback effects.
	 * 
	 * @return True if the WorldObject is knockbacked, false
	 * otherwise.
	 */
	public boolean isKnockbacked(){
		return !knockbacks.isEmpty();
	}
}
