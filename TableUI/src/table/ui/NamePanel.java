package table.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class NamePanel extends JPanel {
	
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.BLACK);
		
		g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
		
		g2.drawString("Run Time", 15, 30);
		g2.drawString("Pressure", 170, 30);
		g2.drawString("Vibration", 340, 30);
		g2.drawString("Speed", 530, 30);
		
		g2.setStroke(new BasicStroke(2));
		g2.drawLine(0, 40, 640, 40);
		//g2.fillRect(0, 0, getWidth(), getHeight());
	}
}
