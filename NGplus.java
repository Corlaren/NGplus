package ngPlus;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.Timer;

public class NGplus 
implements ActionListener, KeyListener{
	
	public static NGplus ngPlus;
	
	public final int WIDTH = 800, HEIGHT = 800;
	
	public int xPos, yPos, yMotion, xMotion, counter, jumpCounter;
	
	public boolean xPlusStat, xMinusStat, yPlusStat, hitLeft, hitRight, hitTop, direction, grounded, armed;
	
	public NGrenderer renderer;
	
	public Rectangle shadow;
	
	public Random rand;
	
	private BufferedImage heroIdleR, heroIdleL, heroIdleRAr, heroIdleLAr;
	
	public ArrayList<Rectangle> obstacles;
	
	public NGplus() {
		
		JFrame jframe = new JFrame();
		Timer timer = new Timer(10, this);

		renderer = new NGrenderer();
		rand = new Random();
		
		jframe.add(renderer);
		jframe.setTitle("Sol");
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setSize(WIDTH, HEIGHT);
		jframe.setResizable(false);
		jframe.setVisible(true);
		jframe.addKeyListener(this);
		
		xPlusStat = false;
		xMinusStat = false;
		yPlusStat = false;
		hitLeft = false;
		hitRight = false;
		hitTop = false;
		armed = true;
		direction = true;
		grounded = true;
		
		xPos = 100;
		yPos = 700;
		yMotion = 1;
		xMotion = 0;
		counter = 0;
		jumpCounter = 0;
		
		obstacles = new ArrayList<Rectangle>();
		
		obstacles.add(new Rectangle(0, 722, 800, 78));
		obstacles.add(new Rectangle(600, 700, 20, 25));
		obstacles.add(new Rectangle(530, 650, 20, 20));
		obstacles.add(new Rectangle(470, 620, 20, 20));
		obstacles.add(new Rectangle(510, 580, 20, 20));
		obstacles.add(new Rectangle(10, 400, 20, 400));
		obstacles.add(new Rectangle(770, 400, 20, 400));
		
		try {
			heroIdleR = ImageIO.read(new File("Hero_idle_R.png"));
			heroIdleL = ImageIO.read(new File("Hero_idle_L.png"));
			heroIdleRAr = ImageIO.read(new File("Hero_idle_R_Armed.png"));
			heroIdleLAr = ImageIO.read(new File("Hero_idle_L_Armed.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		timer.start();
	}
	
	public void paintObstacle(Graphics g, Rectangle obstcl) {
		
		g.setColor(Color.gray);
		g.fillRect(obstcl.x, obstcl.y, obstcl.width, obstcl.height);
	}
	
	public void repaint(Graphics g) {

		g.setColor(Color.black);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		for (Rectangle obstcl : obstacles) {
			
			paintObstacle(g, obstcl);
		}
		
		
		
		if (direction) {
			
			if (!armed) g.drawImage(heroIdleR, xPos, yPos, null);
			else g.drawImage(heroIdleRAr, xPos, yPos, null);
		}
		else if (!direction) {

			if (!armed) g.drawImage(heroIdleL, xPos, yPos, null);
			else g.drawImage(heroIdleLAr, xPos-9, yPos, null);
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		if (counter == 12) {
			
			counter = 0;
		}
		if (jumpCounter == 48) {
			
			jumpCounter = 0;
		}
		
		if (xPlusStat) {
			
			if (grounded) xMotion--;
			else if (counter % 6 == 0) xMotion--;
			direction = false;
		}
		
		if (xMinusStat) {

			if (grounded) xMotion++;
			else if (counter % 6 == 0) xMotion++;
			direction = true;
		}

		if (yPlusStat && grounded) {

			yPos--;
			yMotion = -5;
			jumpCounter = 1;
			grounded = false;
		}
		
		if (yMotion < 10 && jumpCounter % 6 == 0) {
			
			yMotion++;
		}
		
		if (yPos < 700) {
			
			yPos += yMotion;
			
			if (yPos >= 700) {
				
				yPos = 700;
				grounded = true;
			}
		}
		
		if (xMotion > 2) {
			
			xMotion = 2;
		}
		
		if (xMotion < -2) {
			
			xMotion = -2;
		}
		
		if (xMotion > 0 && !xMinusStat) {
			
			if (grounded && counter %3 == 0) xMotion--;
			else if (jumpCounter % 22 == 1) xMotion--;
		}
		
		if (xMotion < 0 && !xPlusStat) {
			
			if (grounded && counter %3 == 0) xMotion++;
			else if (jumpCounter % 22 == 1) xMotion++;
		}

		counter++;
		jumpCounter++;
		xPos += xMotion;
		
		shadow = new Rectangle(xPos+1, yPos-1, 21, 24);
		
		for (Rectangle obstcl : obstacles) {
			
			if (obstcl.intersects(shadow)) {
				
				if (shadow.x + (shadow.width / 2 + 2) < obstcl.x) {
					
					xPos = obstcl.x - shadow.width;
					xMotion = 0;
				}
				
				else if (shadow.x + (shadow.width / 2 - 2) > obstcl.x + obstcl.width) {
					
					xPos = obstcl.x + obstcl.width -1;
					xMotion = 0;
				}
				else if (shadow.y > obstcl.y) {
					
					yPos = obstcl.y + obstcl.height;
					yMotion = 0;
				}
				else {
					
					yPos = obstcl.y - 22;
					yMotion = 0;
					grounded = true;
				}
			}
		}
		
		renderer.repaint();
	}

	public static void main(String[] args){
		
		ngPlus = new NGplus();
	}

	@Override
	public synchronized void keyPressed(KeyEvent e) {
		
		if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			
			xPlusStat = true;
		}
		else if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			
			xMinusStat = true;
		}
		else if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			
			yPlusStat = true;
		}
		else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			if (armed) armed = false;
			else armed = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
		if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			
			xPlusStat = false;
		}
		else if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			
			xMinusStat = false;
		}
		else if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			
			yPlusStat = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
		
	}
}
