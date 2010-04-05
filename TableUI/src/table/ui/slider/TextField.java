package table.ui.slider;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;

public class TextField extends JComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6664005336109701713L;

	private String text = "Hi there!";
	
	public void setText(String t) {
		text = t;
		repaint();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.BLACK);
		g.drawString(text, 0, getHeight());
	}
}
