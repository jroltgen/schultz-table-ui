package table.ui.slider;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TableSlider extends JComponent implements KeyListener,
		SliderListener, ButtonListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8645718190996154617L;

	private String _units;
	private int _max;
	private int _min;
	private int _step;

	private Button downButton;
	private Button upButton;
	private Slider slider;
	private TextField textField;

	public TableSlider(String units, int min, int max, int step) {
		_units = units;
		_max = max;
		_min = min;
		_step = step;

		slider = new Slider(max, min, step, this);
		downButton = new Button(this);
		upButton = new Button(this);
		
		textField = new TextField();

		add(downButton);
		add(upButton);
		add(slider);
		add(textField);
		sliderUpdated(0);
	}

	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, getWidth(), getHeight());
	}

	@Override
	public void setSize(int w, int h) {
		super.setSize(w, h);
		downButton.setLocation(w / 20, (int) (h * 0.9));
		upButton.setLocation((int) (w * 10.5 / 20), (int) (h * 0.9));

		downButton.setSize((int) (w * 8.5 / 20), h / 12);
		upButton.setSize((int) (w * 8.5 / 20), h / 12);

		slider.setLocation(w / 20, (int) (h * 0.15));
		slider.setSize((int) (w * 0.9), (int) (h * 0.73));
		
		textField.setLocation(w / 20, h / 20);
		textField.setSize((int)(w * 0.5), (int)(h * 0.1));
	}

	@Override
	public void sliderUpdated(double newValue) {
		// TODO Auto-generated method stub
		double value = (_min + (1 - newValue) * (_max - _min));
		
		textField.setText("" + Math.round(value));
		System.out.println("Updated." + newValue);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			System.exit(0);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	/**
	 * For testing
	 */
	public static void main(String[] args) {
		JFrame myFrame = new JFrame();
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myFrame.setLayout(null);
		myFrame.setLocation(400, 200);
		myFrame.setSize(640, 480);
		myFrame.setUndecorated(true);

		JPanel myPanel = new JPanel();
		myPanel.setLocation(0, 0);
		myPanel.setSize(640, 480);
		myPanel.setLayout(null);
		myPanel.setBackground(Color.CYAN);

		TableSlider s = new TableSlider("lbs", 0, 30, 5);
		s.setLocation(20, 50);
		s.setSize(180, 350);
		myPanel.add(s);

		TableSlider s2 = new TableSlider("lbs", 0, 10, 5);
		s2.setLocation(220, 50);
		s2.setSize(180, 350);
		myPanel.add(s2);

		TableSlider s3 = new TableSlider("lbs", 0, 100, 10);
		s3.setLocation(420, 50);
		s3.setSize(180, 350);
		myPanel.add(s3);

		myFrame.add(myPanel);
		myFrame.addKeyListener(s);
		myFrame.setVisible(true);
	}

	@Override
	public void buttonPressed(Button src) {
		if (src == downButton) {
			slider.decrease();
		} else if (src == upButton) {
			slider.increase();
		}

	}

}
