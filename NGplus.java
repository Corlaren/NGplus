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
	
	public int xPos = 100, yPos = 700, yMotion = 1, xMotion = 0, counter;
	
	public boolean xPlusStat = false, xMinusStat = false, yPlusStat = false, direction = true, jump = false, grounded = true;
	
	public NGrenderer renderer;
	
	public Random rand;
	
	private BufferedImage heroIdleR, heroIdleL;
	
	public ArrayList<Rectangle> obstacles;
	
	public NGplus() {
		
		JFrame jframe = new JFrame();
		Timer timer = new Timer(9, this);

		renderer = new NGrenderer();
		rand = new Random();
		
		jframe.add(renderer);
		jframe.setTitle("Sol");
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setSize(WIDTH, HEIGHT);
		jframe.setResizable(false);
		jframe.setVisible(true);
		jframe.addKeyListener(this);
		

		obstacles = new ArrayList<Rectangle>();
		
		obstacles.add(new Rectangle(0, 722, WIDTH, HEIGHT-722));
		obstacles.add(new Rectangle(600, 700, WIDTH-700, HEIGHT-700));
		obstacles.add(new Rectangle(10, 400, WIDTH-780, HEIGHT-400));
		obstacles.add(new Rectangle(770, 400, WIDTH-780, HEIGHT-400));
		
		try {
			heroIdleR = ImageIO.read(new File("Hero_idle_R.png"));
			heroIdleL = ImageIO.read(new File("Hero_idle_L.png"));
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
		g.setColor(Color.green);
		g.fillRect(xPos+1, yPos-1, 21, 24);
		
		for (Rectangle obstcl : obstacles) {
			
			paintObstacle(g, obstcl);
		}
		
		if (direction) {
			
			g.drawImage(heroIdleR, xPos, yPos, null);
		}
		else if (!direction) {

			g.drawImage(heroIdleL, xPos, yPos, null);
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		if (counter == 5) {
			
			counter = 0;
		}
		
		if (xPlusStat) {
			
			xMotion -= 1;
			direction = false;
		}
		
		if (xMinusStat) {

			xMotion += 1;
			direction = true;
		}

		if (yPlusStat && grounded) {

			yPos -= 2;
			yMotion = -10;
		}
		
		if (yMotion < 10 && counter % 2 == 1) {
			
			yMotion++;
		}
		
		if (yPos < 700) {
			
			yPos += yMotion;
			grounded = false;
			
			if (yPos >= 700) {
				
				yPos = 700;
				grounded = true;
			}
		}
		
		if (xMotion > 0 && counter == 0) {
			
			xMotion--;
		}
		
		if (xMotion < 0 && counter == 0) {
			
			xMotion++;
		}
		
		if (xMotion > 5) {
			
			xMotion = 5;
		}
		
		if (xMotion < -5) {
			
			xMotion = -5;
		}

		counter++;
		xPos += xMotion;

		if (xPos > 700) {
			
			xPos = 700;
			xMotion = 0;
		}

		if (xPos < 100) {
			
			xPos = 100;
			xMotion = 0;
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
