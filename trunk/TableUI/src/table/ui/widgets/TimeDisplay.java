package table.ui.widgets;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

public class TimeDisplay extends JComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 598303829874626248L;

	private int totalSeconds = 600;
	private int currentSeconds = 0;
	private Color c = new Color(53, 152, 225);
	private Color _lightColor;
	private Color _darkColor;
	
	private final int size = 20;
	
	public TimeDisplay() {
		float hsbValues[] = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);

		System.out.print("Values: ");
		for (float f : hsbValues) {
			System.out.print(f + " ");
		}
		System.out.println();
		_lightColor = new Color(Color.HSBtoRGB(hsbValues[0], hsbValues[1], hsbValues[2] - .3f));
		_darkColor = new Color(Color.HSBtoRGB(hsbValues[0], hsbValues[1], hsbValues[2] - .5f));
		
	}
	

	public void setTotalSeconds(int totalSeconds) {
		this.totalSeconds = totalSeconds;
		repaint();
	}

	public void setCurrentSeconds(int currentSeconds) {
		this.currentSeconds = currentSeconds;
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		g2.fillRoundRect((int) (getWidth() * 0.1 - size/2), (int) (getHeight() * 0.5),
				(int) (getWidth() * 0.8 + size), (int) (getHeight() * 0.2), 10, 10);
		
		// Draw oval
		int sliderCenterX = (int)((0.1 + (currentSeconds / (float)totalSeconds) * 0.8) * getWidth());
		int sliderCenterY = (int)(getHeight() * 0.6);
		
		
		g2.setColor(_darkColor);
		g2.fillOval(sliderCenterX - size/2, sliderCenterY - size/2, size, size);
		
		g2.setStroke(new BasicStroke(2));
		g2.setColor(Color.BLACK);
		g2.drawOval(sliderCenterX - size/2, sliderCenterY - size/2, size, size);
		
		// Draw time
		g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
		g2.drawString(getTimeString(currentSeconds), sliderCenterX - 20, (int)(getHeight() * 0.35));

	}
	
	private String getTimeString(int seconds) {
		int m = seconds / 60;
		int s = seconds % 60;
		
		String str = "";
		if (m < 10) {
			str += "0";
		}
		str += m + ":";
		if (s < 10) {
			str += "0";
		}
		str += s;
		return str;
	}
}
