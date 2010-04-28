package table.ui.widgets;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import javax.swing.JComponent;

import table.ui.widgetlisteners.ButtonListener;

public class Button extends JComponent implements MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7298774760816689371L;
	private Vector<ButtonListener> _listeners;
	private boolean _pressed = false;
	private Color _color;
	private String _text;
	private boolean _enabled = true;

	public Button(ButtonListener b) {
		this(b, Color.BLUE, "");
	}
	
	public Button(ButtonListener b, Color c, String text) {
		_listeners = new Vector<ButtonListener>();
		_listeners.add(b);
		addMouseListener(this);
		_color = c;
		_text = text;
	}
	
	public void addListener(ButtonListener l) {
		_listeners.add(l);
	}
	
	public void setComponentEnabled(boolean e) {
		_enabled = e;
		repaint();
	}
	
	@Override
	protected void paintComponent(Graphics g) {

		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		
		
		
		if (_pressed) {
			g2.setColor(Color.WHITE);
		} else {
			if (_enabled) {
				g2.setColor(_color);
			} else {
				g2.setColor(Color.DARK_GRAY);
			}
		}
		g2.fillRect(0, 0, getWidth(), getHeight());
		
		g2.setColor(Color.BLACK);

		g2.setStroke(new BasicStroke(3));
		g2.drawRect(1, 1, getWidth() - 3, getHeight() - 3);
		g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
		g2.drawString(_text, 20, getHeight() / 2 + 10);
	}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
		if (_enabled) {
		// TODO Auto-generated method stub
			_pressed  = true;
			repaint();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		if (_enabled) {
			_pressed = false;
			for (ButtonListener l : _listeners) {
				l.buttonPressed(this);
			}
			repaint();
		}
	}
}
