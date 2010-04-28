package table.ui.widgets;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

import javax.swing.JComponent;

import table.ui.widgetlisteners.SliderListener;

public class Slider extends JComponent implements MouseListener,
		MouseMotionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1455401711398369497L;

	private Vector<SliderListener> _listeners;

	private Vector<Double> allowedPositions;
	private int currentPosition = 0;

	private int slidery;
	private int sliderh;

	private double _max, _min, _step;

	private boolean _selected;
	private boolean _enabled = true;

	public Slider(double max, double min, double step, SliderListener l) {
		_listeners = new Vector<SliderListener>();
		_listeners.add(l);

		allowedPositions = new Vector<Double>();
		System.out.println("Cons");
		for (double i = min; i <= max + 0.01; i += step) {
			System.out.println("Adding position: " + i);
			allowedPositions.add((i - min) / max);
		}
		currentPosition = allowedPositions.size() - 1;
		addMouseListener(this);
		addMouseMotionListener(this);

		_max = max;
		_min = min;
		_step = step;
	}

	public void addListener(SliderListener l) {
		_listeners.add(l);
	}

	@Override
	public void setSize(int w, int h) {
		super.setSize(w, h);
		slidery = (int) (h * 0.05 + h / 6);
		sliderh = (int) (h * 0.9 - h / 3);
	}
	
	public void setComponentEnabled(boolean e) {
		_enabled = e;
		System.out.println("\n\n\n*********Seeting enabled: " + e + "   ******\n\n\n");
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {

		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		//g2.setColor(Color.BLUE);
		//g2.fillRect(0, 0, getWidth(), getHeight());
		g2.setColor(Color.DARK_GRAY);

		int w = getWidth();
		int h = getHeight();

		// Slider rect.
		g2.fillRoundRect(w / 12 + h / 6 - w / 30, (int) (h * 0.05), w / 15,
				(int) (h * 0.9), w / 15, h / 15);

		// Slider circle.
		if (_enabled) {
			g2.setColor(Color.BLUE);
		} else {
			g2.setColor(Color.DARK_GRAY);
		}
		g2.fillOval(w / 12, (int) (slidery
				+ allowedPositions.get(currentPosition) * sliderh - h / 6),
				h / 3, h / 3);
		if (_selected) {
			g2.setColor(Color.WHITE);
		} else {
			g2.setColor(Color.BLACK);
		}
		g2.setStroke(new BasicStroke(h / 50));
		g2.drawOval(w / 12, (int) (slidery
				+ allowedPositions.get(currentPosition) * sliderh - h / 6),
				h / 3, h / 3);

		// Slider tick marks
		g2.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke(3));
		for (double i = _min; i <= _max; i += _step) {
			int y = (int) (slidery + sliderh - sliderh
					* ((i - _min) / (_max - _min)));
			//System.out.println("Slider mark: " + y);
			g2.drawLine(w / 12 + h / 3 + w / 12, y, (w * 11 / 12), y);
		}
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
		if (_enabled) {
			setPosition(e.getY());
			_selected = true;
			repaint();
		}
	}


	@Override
	public void mouseReleased(MouseEvent e) {
		if (_enabled) {
			_selected = false;
			repaint();
		}
	}

	public void increase() {
		if (currentPosition < allowedPositions.size() - 1)
			currentPosition++;
		sliderUpdated();
		repaint();
	}

	private void sliderUpdated() {
		// TODO Auto-generated method stub
		for (SliderListener l : _listeners) {
			l.sliderUpdated(allowedPositions.get(currentPosition));
		}
	}

	public void decrease() {
		if (currentPosition > 0)
			currentPosition--;
		sliderUpdated();
		repaint();
	}

	private void setPosition(int y) {
		double pos = (y - slidery) / (double) sliderh;

		double minDistance = 1;
		int minIndex = 0;
		for (int i = 0; i < allowedPositions.size(); i++) {
			double d = Math.abs(allowedPositions.get(i) - pos);
			if (d < minDistance) {
				minDistance = d;
				minIndex = i;
			}
		}

		currentPosition = minIndex;
		sliderUpdated();
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		if (_enabled) {
			setPosition(arg0.getY());
			repaint();
		}
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
	}
}
