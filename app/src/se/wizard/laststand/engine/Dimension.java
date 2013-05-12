package se.wizard.laststand.engine;

public class Dimension {
	
	private int x,y;
	
	public Dimension(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public int getWidth(){
		return x;
	}
	
	public int getHeight(){
		return y;
	}

}
