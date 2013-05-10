package se.kth.inda.indaprojekt.engine;

import java.util.ArrayList;
import java.util.Stack;

/**
 * A Level contains a number of WorldObjects and may progress the level forward
 * if told so by moving objects, checking collisions, etc. Extreme bugs may arise
 * if multiple threads are calling the levelTick()
 * 
 * @author David Masko
 *
 */
public class Level {
	
	private LevelState state;
	private Dimension size;
	private Stack<WorldObjectQueueElement> queue = new Stack<WorldObjectQueueElement>();
	
	//A ticker used for rate-limiting of expensive operations.
	private byte ticker = 0;
	
	// These ArrayLists stores all the objects in the Level. The same WorldObject
	// may be stored in multiple arrays and adding and removing is  therefore STRICLY
	// CONFINED to the master<Add/Remove>WorldObject(WorldObject o) in order to keep
	// the storing fail safe.
	private ArrayList<WorldObject> worldObjects = new ArrayList<WorldObject>();
	private ArrayList<Unit> units = new ArrayList<Unit>();
	private ArrayList<Wizard> wizards = new ArrayList<Wizard>();
	private ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	private ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
	private ArrayList<UnitImpacter> unitImpacters = new ArrayList<UnitImpacter>();
	
	/**
	 * Creates an new Level with the given size. The size cannot be
	 * changed once the Level has been created. A Level is a plain 
	 * rectangular open shape.
	 * 
	 * @param size The Size of the Level.
	 */
	public Level(Dimension size){
		this.size = size;
	}
	
	/**
	 * Tags the the given WorldObject to be added into the Level upon the next gameTick.
	 * The WorldObject is only added to the Level before a Level tick in order to keep it 
	 * thread safe. 
	 * 
	 * @param wo WorldObject to be added.
	 * @return true if the WorldObject isn't present in the Level, false otherwise.
	 */
	public boolean addWorldObject(WorldObject wo){
		if(worldObjects.contains(wo))
			return false;
		else{
			queue.push(new WorldObjectQueueElement(wo, QueueAction.ADD));
			return true;
		}
	}
	
	/**
	 * Tags the the given WorldObject to be removed from the Level upon the next gameTick.
	 * The WorldObject is only removed fron the Level before a Level tick in order to keep it 
	 * thread safe. 
	 * 
	 * @param wo WorldObject to be removed
	 */
	public void removeWorldObject(WorldObject wo){
		if(!worldObjects.contains(wo))
			return;
		else
			queue.push(new WorldObjectQueueElement(wo, QueueAction.REMOVE));
	}
	
	/**
	 * Adds the given WorldObject to all necessary Collections
	 * based on it's class.
	 * 
	 * NOTE: THIS METHOD SHOULD NOT BE INVOKED BY ANY OTHER FUNCTION
	 * THAN THE levelTick() FUNCTION IN ORDER TO KEEP THE CLASS
	 * FAIL SAFE.
	 * 
	 * @param o The WorldObject to be added.
	 */
	private void masterAddWorldObject(WorldObject o){
		worldObjects.add(o);
		
		if(o instanceof Unit){
			units.add((Unit) o);
			if(o instanceof Wizard)
				wizards.add((Wizard) o);
			else if(o instanceof Enemy)
				enemies.add((Enemy) o);
		}
		else if(o instanceof Projectile)
			projectiles.add((Projectile) o);
		
		if(o instanceof UnitImpacter)
			unitImpacters.add((UnitImpacter) o);	
	}
	
	/**
	 * Removes the given WorldObject from all necessary Collections
	 * based on it's class.
	 * 
	 * NOTE: THIS METHOD SHOULD NOT BE INVOKED BY ANY OTHER FUNCTION
	 * THAN THE levelTick() FUNCTION IN ORDER TO KEEP THE CLASS
	 * FAIL SAFE.
	 * 
	 * @param o The WorldObject to be removed.
	 */
	private void masterRemoveWorldObject(WorldObject o){
		worldObjects.remove(o);
		
		if(o instanceof Unit){
			units.remove((Unit) o);
			if(o instanceof Wizard)
				wizards.remove((Wizard) o);
			else if(o instanceof Enemy)
				enemies.remove((Enemy) o);
		}
		else if(o instanceof Projectile)
			projectiles.remove((Projectile) o);
		
		if(o instanceof UnitImpacter)
			unitImpacters.remove((UnitImpacter) o);	
	}
	
	/**
	 * Returns all WorldObjects in the level.
	 * SORT NOT YET IMPLEMENTED
	 * 
	 * @param sortedByDrawPriority If the WorldOjects should be sorted by drawPriority.
	 * @return All WorldObjects in the Level.
	 */
	public WorldObject[] getWorldObjects(boolean sortedByDrawPriority){
		WorldObject[] wos = worldObjects.toArray(new WorldObject[0]);
		//TODO
		return wos;
	}
	
	/**
	 * Makes the level execute events for one game tick. This is initiated
	 * by adding and removing all tagged WorldObjects followed by applying
	 * all defined tick effects on all WorldObjects, check for
	 * collision and apply onImpactEffects for all colliding Projectiles.
	 * 
	 * NOTE: levelTick is NOT thread-safe and should NOT be called by multiple
	 * threads. This may have a catastrophic effects on the Level - a crash or
	 * strange bugs may occur.
	 */
	public void levelTick(){
		
		/*
		 * Adds or removes the tagged WorldObjects from the game.
		 * 
		 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!FUNCTIONS!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		 * NOTE: THIS IS THE ONLY PLACE THAT THE MASTERv ADD AND REMOVE FUNCTIONS
		 * SHOULD BE INVOKED IN ORDER TO  KEEP THE CLASS AS THREAD SAFE AS POSSIBLE.
		 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		 */
		
		while(!queue.isEmpty()){
			WorldObjectQueueElement e = queue.pop();
			WorldObject wo = e.getWorldObject();
			if(e.getAction() == QueueAction.ADD)
				masterAddWorldObject(wo);
			else if(e.getAction() == QueueAction.REMOVE)
				masterRemoveWorldObject(wo);
		}
		
		// Apply onTick for every WorldObject or schedule obsolete WorldObjects
		// for removal.
		
		for (int i = 0; i < worldObjects.size(); i++) {
			WorldObject wo = worldObjects.get(i);
			if(wo.getState() == WorldObjectState.OBSOLETE)
				removeWorldObject(wo);
			else if(wo.getState() != WorldObjectState.SECLUDED){
				wo.onTick();
			}
		}
		
		// Check all UnitImpacters for collisions and execute their respective effects.
		for(UnitImpacter ui : unitImpacters){
			for(Unit u : units){
				if(u.getState() == WorldObjectState.OBSOLETE)
					continue;
				if(u.getState() == WorldObjectState.SECLUDED)
					continue;
				if(ui.isRelevantTarget(u))
					if(u.checkCollision((WorldObject) ui))
						ui.onImpact(u);
			}
		}
		
		//Check for OBSOLETE states
		for (int i = 0; i < projectiles.size(); i++) {
			Projectile p = projectiles.get(i);
			if(p.isOutOfBounds(size.getWidth(), size.getHeight()))
				p.setState(WorldObjectState.OBSOLETE);
		}
		
		for (int i = 0; i < units.size(); i++) {
			Unit u = units.get(i);
			if(u.isDead())
				u.setState(WorldObjectState.OBSOLETE);
		}
		
		//Check conditions for the state of the level and change accordingly if needed.
		if(ticker % 10 == 0) //Rate limited.
			updateState();
		
		ticker++;
	}
	
	private boolean updateState(){
		boolean defeat = true;
		boolean victory = true;
		
		for (int i = 0; i < wizards.size(); i++) {
			if(!wizards.get(i).isDead()){
				defeat = false;
				break;
			}
		}
		
		for (int i = 0; i < enemies.size(); i++) {
			if(!enemies.get(i).isDead()){
				victory = false;
				break;
			}
		}
		
		if(victory){
			state = LevelState.VICTORY;
			return true;
		}
		else if(defeat){
			state = LevelState.GAMEOVER;
			return true;
		}
		else if(state == LevelState.UNMODIFIED){
			state = LevelState.RUNNING;
			return true;
		}
		return false;
	}
	
	/**
	 * The current state of this Level.
	 * 
	 * @return the current LevelState of this Level.
	 */
	public LevelState getState(){
		return state;
	}
	
	/**
	 * The Size of the Level.
	 * 
	 * @return The Size of the Level.
	 */
	public Dimension getSize(){
		return new Dimension(size.getWidth(),size.getHeight());
	}
	
	/**
	 * 
	 * @return
	 */
	public Wizard[] getWizards(){
		return wizards.toArray(new Wizard[0]);
	}

	/**
	 * A WorldObjectQueueElement is used to schedule a thread safe
	 * implementation of the adding and removal of WorldObjects in 
	 * a Level.
	 * 
	 * @author David Masko
	 */
	private static class WorldObjectQueueElement{
		private QueueAction action;
		private WorldObject o;
		
		/**
		 * Creates a new WorldObjectQueueElement. The action definies
		 * which action should be done upon the WorldObject. 
		 * @param wo The WorldObject affected by the action.
		 * @param action The Action affecting the WorldObject.
		 */
		public WorldObjectQueueElement(WorldObject wo, QueueAction action){
			this.action = action;
			o = wo;
		}	
		
		/**
		 * The pending action upon the queued WorldObject.
		 * 
		 * @return The action.
		 */
		public QueueAction getAction(){
			return action;
		}
		
		/**
		 * The queued WorldObject.
		 * 
		 * @returnThe queued WorldObject.
		 */
		public WorldObject getWorldObject(){
			return o;
		}
	}
	
	/**
	 * A QueueAction defines the action the level should do
	 * with a WorldObject in a WorldObjectQueueElement.
	 * 
	 * @author David Masko
	 *
	 */
	private static enum QueueAction{
		/**
		 * The WorldObject is supposed to be added to the Level 
		 */
		ADD,
		
		/**
		 * The WorldObject is supposed to be removed from the Level
		 */
		REMOVE;
	}
}
