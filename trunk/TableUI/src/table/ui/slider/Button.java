package table.ui.slider;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import javax.swing.JComponent;

public class Button extends JComponent implements MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7298774760816689371L;
	private Vector<ButtonListener> _listeners;
	private boolean _pressed = false;

	public Button(ButtonListener b) {
		_listeners = new Vector<ButtonListener>();
		_listeners.add(b);
		addMouseListener(this);
	}
	
	public void addListener(ButtonListener l) {
		_listeners.add(l);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		if (_pressed) {
			g.setColor(Color.WHITE);
		} else {
			g.setColor(Color.BLUE);
		}
		g.fillRect(0, 0, getWidth(), getHeight());
	}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		_pressed  = true;
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		_pressed = false;
		for (ButtonListener l : _listeners) {
			l.buttonPressed(this);
		}
		repaint();
	}
}
