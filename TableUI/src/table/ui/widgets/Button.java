package table.ui.widgets;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.color.ICC_ColorSpace;
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
	private Color _lightColor;
	private Color _darkColor;

	public Button(ButtonListener b) {
		this(b, Color.BLUE, "");
	}
	
	public Button(ButtonListener b, Color c, String text) {
		_listeners = new Vector<ButtonListener>();
		_listeners.add(b);
		addMouseListener(this);
		setColor(c);
		_text = text;
	}
	
	public void setColor(Color c) {
		_color = c;
		System.out.print("Color: " + c + " ");
		float hsbValues[] = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);

		System.out.print("Values: ");
		for (float f : hsbValues) {
			System.out.print(f + " ");
		}
		System.out.println();
		_lightColor = new Color(Color.HSBtoRGB(hsbValues[0], hsbValues[1], hsbValues[2] - .3f));
		_darkColor = new Color(Color.HSBtoRGB(hsbValues[0], hsbValues[1], hsbValues[2] - .5f));
		repaint();
	}
	
	public void setText(String s) {
		_text = s;
		repaint();
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
		
		
		//if (_pressed) {
			//g2.setColor(Color.WHITE);
		//} else {
			if (_enabled) {
				GradientPaint fill = new GradientPaint(0, 0, _lightColor, getWidth()/2, getHeight()/2, _darkColor, false);
				g2.setPaint(fill);
			} else {
				GradientPaint fill = new GradientPaint(0, 0, Color.GRAY, getWidth()/2, getHeight()/2, Color.DARK_GRAY, false);
				g2.setPaint(fill);
			}
		//}
		g2.fillRoundRect(1, 1, getWidth() - 3, getHeight() - 3, getHeight() / 2, getHeight() / 2);
		
		if (_pressed) {
			g2.setColor(Color.WHITE);
		} else {
			g2.setColor(Color.BLACK);
		}

		g2.setStroke(new BasicStroke(3));
		g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, getHeight() / 2, getHeight() / 2);
		g2.setColor(Color.BLACK);
		g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
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
