package ngPlus;

import java.awt.Rectangle;

public class Bullet {
	
	public final int xVar, yVar, xMov, yMov;
	public int xt, yt, tick;
	
	public Rectangle bulletshadow;
	
	public Bullet(int x, int y, int xM, int yM) {
		
		tick = 0;
		
		xVar = x + 30;
		yVar = y + 30;
		xt = xVar;
		yt = yVar;
		xMov = xM;
		yMov = yM;
		
		bulletshadow = new Rectangle(xVar, yVar, 5, 5);
	}
	
	public void fly() {
		
		if (xMov != 0) xt = xVar + (xMov * tick * 20) / Math.max(Math.abs(xMov), Math.abs(yMov));
		if (yMov != 0) yt = yVar + (yMov * tick * 20) / Math.max(Math.abs(xMov), Math.abs(yMov));
		
		bulletshadow = new Rectangle(xt, yt, 5, 5);
		
		tick++;
	}	
}
