package ngPlus;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.Timer;

public class NGplus 
implements ActionListener, KeyListener, MouseListener{
	
	public static NGplus ngPlus;
	
	public final int WIDTH = 800, HEIGHT = 800;
	
	public int xPos, yPos, yMotion, xMotion, runFrame, idleFrame, fallTick, armed;
	
	public boolean moveLeft, moveRight, acsend, hitLeft, hitRight, hitTop, direction, grounded, jumped;
	
	public NGrenderer renderer;
	
	public Rectangle shadow, proximityshadow;
	
	public Random rand;
	
	private BufferedImage heroIdleR, heroIdleL, heroRunningR, heroRunningL;
	
	public ArrayList<Rectangle> obstacles;
	
	public ArrayList<Bullet> bullets;
	
	public NGplus() {
		
		JFrame jframe = new JFrame();
		Timer timer = new Timer(10, this);

		renderer = new NGrenderer();
		rand = new Random();
		
		jframe.add(renderer);
		jframe.setTitle("Sol");
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setSize(WIDTH, HEIGHT);
		jframe.setUndecorated(true);
		jframe.setAlwaysOnTop(true);
		jframe.setResizable(false);
		jframe.setVisible(true);
		jframe.addKeyListener(this);
		jframe.addMouseListener(this);
		
		makeFrameFullSize(jframe);
		
		moveLeft = false;
		moveRight = false;
		acsend = false;
		jumped = false;
		hitLeft = false;
		hitRight = false;
		hitTop = false;
		direction = true;
		grounded = true;
		
		runFrame = 0;
		idleFrame = 0;
		fallTick = 0;
		xPos = 100;
		yPos = 600;
		yMotion = 0;
		xMotion = 0;
		
		obstacles = new ArrayList<Rectangle>();
		
		obstacles.add(new Rectangle(0, 722, 800, 78));
		obstacles.add(new Rectangle(600, 700, 20, 25));
		obstacles.add(new Rectangle(400, 700, 3, 25));
		obstacles.add(new Rectangle(530, 630, 20, 20));
		obstacles.add(new Rectangle(470, 620, 20, 20));
		obstacles.add(new Rectangle(510, 580, 20, 20));
		obstacles.add(new Rectangle(10, 400, 20, 400));
		obstacles.add(new Rectangle(770, 400, 20, 400));
		
		bullets = new ArrayList<Bullet>();
		
		try {
			heroIdleR = ImageIO.read(new File("HeroIdleR.png"));
			heroIdleL = ImageIO.read(new File("HeroIdleL.png"));
			heroRunningR = ImageIO.read(new File("HeroRunningR.png"));
			heroRunningL = ImageIO.read(new File("HeroRunningL.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		timer.start();
	}
	
	private void makeFrameFullSize(JFrame aFrame) {
		
	    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    aFrame.setSize(screenSize.width, screenSize.height);
	}
	
	public void paintObstacle(Graphics g, Rectangle obstcl, Color c) {
		
		g.setColor(c);
		g.fillRect(obstcl.x, obstcl.y, obstcl.width, obstcl.height);
	}
	
	public void repaint(Graphics g) {

		g.setColor(Color.gray.darker());
		g.fillRect(0, 0, 1980, 1080);
		
		for (Rectangle obstcl : obstacles) {
			
			paintObstacle(g, obstcl, Color.gray);
		}
		
		for (Bullet bull : bullets) {
			
			bull.fly();
			paintObstacle(g, bull.bulletshadow, Color.yellow);
		}
		
		if (direction) {

			if (xMotion != 0 && grounded) {
			
				g.drawImage(heroRunningR.getSubimage((runFrame/6)*30, 0, 30, 58), xPos, yPos, null);
				runFrame++;
				if (runFrame >= 47) runFrame = 0;
			}
			else if (!grounded) {
				
				g.drawImage(heroRunningR.getSubimage(30, 0, 30, 58), xPos, yPos, null);
			}
			else {

				g.drawImage(heroIdleR.getSubimage((idleFrame/40)*30, 0, 30, 58), xPos, yPos, null);
				idleFrame++;
				if (idleFrame >= 79) idleFrame = 0;
			}
		}
		else if (!direction) {

			if (xMotion != 0 && grounded) {
			
				g.drawImage(heroRunningL.getSubimage((runFrame/6)*30, 0, 30, 58), xPos, yPos, null);
				runFrame--;
				if (runFrame <= 0) runFrame = 47;
			}
			else if (!grounded) {
				
				g.drawImage(heroRunningL.getSubimage(180, 0, 30, 58), xPos, yPos, null);
			}
			else {
				
				g.drawImage(heroIdleL.getSubimage((idleFrame/40)*30, 0, 30, 58), xPos, yPos, null);
				idleFrame++;
				if (idleFrame >= 79) idleFrame = 0;
			}
		}
		g.drawString(grounded + " ", 10, 10);
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		if (moveLeft) {
			
			xMotion--;
		}
		
		if (moveRight) {

			xMotion++;
		}
		
		if (xMotion > 0) {

			direction = true;
		}
		
		if (xMotion < 0) {

			direction = false;
		}

		if (acsend && grounded && !jumped) {

			yMotion = -5;
			fallTick = 0;
			grounded = false;
			jumped = true;
		}
		
		if (yMotion < 10) {
			
			if (fallTick < 4) fallTick++;
			else {
				
				yMotion++;
				fallTick = 0;
			}
		}
		
		if (xMotion > 2) {
			
			xMotion = 2;
		}
		
		if (xMotion < -2) {
			
			xMotion = -2;
		}
		
		if (xMotion > 0 && !moveRight) {
			
			xMotion--;
		}
		
		if (xMotion < 0 && !moveLeft) {
			
			xMotion++;
		}

		if (xMotion != 0 || yMotion != 0) collision(xPos, yPos, xMotion, yMotion);
		
		if (yMotion != 0) grounded = false;
		
		xPos += xMotion;
		yPos += yMotion;
		
		renderer.repaint();
	}
	
	public void collision (int xP, int yP, int xM, int yM) {
		
		int xCol = xPos;
		int yCol = yPos;
		
		for (Rectangle obstcl : obstacles) {
			
			proximityshadow = new Rectangle(xCol - 15, yCol - 15, 60, 88);
			
			if (obstcl.intersects(proximityshadow)) {
			
				for (int i = 1; i <= Math.max(Math.abs(xM), Math.abs(yM)); i++) {
					
					if (xMotion != 0) xCol = xP + (xM * i) / Math.max(Math.abs(xM), Math.abs(yM));
					if (yMotion != 0) yCol = yP + (yM * i) / Math.max(Math.abs(xM), Math.abs(yM));
					
					shadow = new Rectangle(xCol + 8, yCol-1, 14, 59);
			
					if (obstcl.intersects(shadow)) {
					
						if (xCol + 8 < obstcl.x + obstcl.width && xCol + 22 > obstcl.x && yCol + 50 < obstcl.y && yMotion >= 0) {
						
							yPos = yCol - 1;
							yMotion = 0;
							grounded = true;
						}
						else if (yCol + 2 > obstcl.y + obstcl.height && yMotion <= 0) {
						
							yPos = yCol;
							yMotion = 0;
						}
						else if (xCol + 20 < obstcl.x) {
						
							xPos = xCol - 1;
							xMotion = 0;
						}
						else {
						
							xPos = xCol + 1;
							xMotion = 0;
						}
					}
				}
			}
		}
	}

	public static void main(String[] args){
		
		ngPlus = new NGplus();
	}

	@Override
	public synchronized void keyPressed(KeyEvent e) {
		
		if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			
			moveLeft = true;
		}
		else if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			
			moveRight = true;
		}
		else if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			
			acsend = true;
		}
		else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			
			
		}
		else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			
			acsend = true;
		}
		else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			
			System.exit(0);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
		if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			
			moveLeft = false;
		}
		else if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			
			moveRight = false;
		}
		else if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			
			acsend = false;
			jumped = false;
		}
		else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			
			acsend = false;
			jumped = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {

		bullets.add(new Bullet(xPos,yPos,e.getX()-xPos-30,e.getY()-yPos-30));
	}
}
