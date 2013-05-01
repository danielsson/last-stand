package engineTest;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.Timer;

import engine.GameEngine;
import engine.Level;
import engine.Spell;
import engine.Wizard;
import engine.WorldObject;

public class Window extends JComponent implements MouseListener, MouseMotionListener{

	private GameEngine e;
	private Timer t;
	private int x,y;

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		
		Window w = new Window();
		frame.add(w, BorderLayout.CENTER);
		frame.addMouseListener(w);
		frame.addMouseMotionListener(w);
		
		frame.setVisible(true);
		frame.pack();
		
		w.init();
	}
	
	public Window(){
		e = new GameEngine(40);
		e.setCurrentLevel(GameEngine.createLevel(new Dimension(500,500), 30));
		setSize(e.getCurrentLevel().getSize());
		setPreferredSize(e.getCurrentLevel().getSize());
		
		t = new Timer(1000/40, new ActionListener(){
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				repaint();
			}
			
		});
	}
	
	public void init(){
		t.start();
		e.run();
	}
	
	
	public void paintComponent(Graphics g){
		Level l = e.getCurrentLevel();
		WorldObject[] objects = l.getWorldObjects(false);
		
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.BLACK);
		for (int i = 0; i < objects.length; i++) {
			WorldObject wo = objects[i];
			g.fillOval((int)(wo.getX()-wo.getRadius()), (int)(wo.getY()-wo.getRadius()), (int)(wo.getRadius()*2), (int)(wo.getRadius()*2));
		}
		int size = 1;
		g.fillOval(x-size,y-size,size*2,size*2);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		Level l = this.e.getCurrentLevel();
		Wizard[] wiz = l.getWizards();
		Spell[] spells = wiz[0].getSpellbook();
		wiz[0].attemptToCastSpell(spells[2], e.getX(), e.getY());
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		x = e.getX();
		y = e.getY();
		
	}

}
