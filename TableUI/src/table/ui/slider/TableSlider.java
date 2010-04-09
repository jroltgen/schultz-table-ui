package table.ui.slider;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import table.ui.widgetlisteners.ButtonListener;
import table.ui.widgetlisteners.SliderListener;
import table.ui.widgetlisteners.TableSliderListener;
import table.ui.widgets.Button;
import table.ui.widgets.Slider;
import table.ui.widgets.TextField;

public class TableSlider extends JComponent implements KeyListener, 
		SliderListener, ButtonListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8645718190996154617L;

	private String _units;
	private int _max;
	private int _min;

	private Button downButton;
	private Button upButton;
	private Slider slider;
	private TextField textField;
	private TableSliderListener _listener;

	public TableSlider(String units, int min, int max, int step, TableSliderListener t) {
		_units = units;
		_max = max;
		_min = min;

		slider = new Slider(max, min, step, this);
		downButton = new Button(this);
		upButton = new Button(this);
		_listener = t;
		
		textField = new TextField();

		add(downButton);
		add(upButton);
		add(slider);
		add(textField);
		sliderUpdated(1);
	}

	@Override
	protected void paintComponent(Graphics g) {
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.GRAY);
		g2.fillRect(0, 0, getWidth(), getHeight());
		g2.setColor(Color.BLACK);
		g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
		g2.drawString(_units, (int)(getWidth() * 0.6), (int)(getHeight() * 0.12));
		//g2.setStroke(new BasicStroke(2));
		//g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
		
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
		double value = (_min + (1 - newValue) * (_max - _min));
		
		textField.setText("" + Math.round(value));
		repaint();
		_listener.tableSliderUpdated(value, this);
	}

	@Override
	public void keyPressed(KeyEvent e) {
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



	@Override
	public void buttonPressed(Button src) {
		if (src == downButton) {
			slider.decrease();
		} else if (src == upButton) {
			slider.increase();
		}

	}

}
