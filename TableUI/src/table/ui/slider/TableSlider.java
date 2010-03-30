package table.ui.slider;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class TableSlider extends JComponent implements KeyListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8645718190996154617L;

	private String _units;
	private int _max;
	private int _min;
	private int _step;
	
	public TableSlider(String units, int min, int max, int step) {
		_units = units;
		_max = max;
		_min = min;
		_step = step;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		g.fillRect(0, 0, getWidth(), getHeight());
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			System.exit(0);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}
	
	/**
	 * For testing
	 */
	public static void main(String[] args) {
		JFrame myFrame = new JFrame("Hi there.");
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myFrame.setLayout(null);
		myFrame.setLocation(400, 200);
		myFrame.setSize(640, 480);
		myFrame.setUndecorated(true);
		
		JPanel myPanel = new JPanel();
		myPanel.setLocation(0, 0);
		myPanel.setSize(640, 480);
		myPanel.setLayout(null);
		
		TableSlider s = new TableSlider("lbs", 30, 0, 5);
		s.setLocation(20, 50);
		s.setSize(150, 350);
		myPanel.add(s);
		
		myFrame.add(myPanel);
		myFrame.addKeyListener(s);
		myFrame.setVisible(true);
	}

	
}
