package ngPlus;

import java.awt.Graphics;

import javax.swing.JPanel;

public class NGrenderer extends JPanel{

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		NGplus.ngPlus.repaint(g);
	}

}
