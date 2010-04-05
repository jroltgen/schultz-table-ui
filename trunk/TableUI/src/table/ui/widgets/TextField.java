package table.ui.widgets;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JComponent;

public class TextField extends JComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6664005336109701713L;

	private String text = "";

	private int _textSize;
	
	public TextField() {
		this(30);
	}
	
	public TextField(int textSize) {
		_textSize = textSize;
	}

	public void setText(String t) {
		text = t;
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, getWidth(), getHeight());
		
		g2.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke(3));
		g2.drawRect(1, 1, getWidth() - 3, getHeight() - 3);
		
		g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, _textSize));
		g2.drawString(text, 20, getHeight() / 2 + _textSize * 3 / 8);
	}
}
