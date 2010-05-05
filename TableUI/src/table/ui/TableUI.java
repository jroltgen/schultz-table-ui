package table.ui;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;

import table.ArduinoConnection;
import table.ArduinoConnectionCSharpPipeImpl;
import table.ui.control.ControlPanel;
import table.ui.slider.TableSlider;
import table.ui.widgetlisteners.TableSliderListener;

public class TableUI implements TableSliderListener {
	private TableSlider runTime;
	private TableSlider pressure;
	private TableSlider vibration;
	private TableSlider speed;
	private ControlPanel controlPanel;
	private NamePanel namePanel;

	/**
	 * For testing
	 */
	public static void main(String[] args) {
		ArduinoConnection.getInstance().stop();
		new TableUI().run();
	}
	
	public void run() {
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
		myPanel.setBackground(Color.GRAY);
		
		namePanel = new NamePanel();
		namePanel.setLocation(0, 0);
		namePanel.setSize(640, 45);
		myPanel.add(namePanel);

		runTime = new TableSlider("mins", 0, 30, 5, 5, this);
		runTime.setLocation(0, 30);
		runTime.setSize(160, 360);
		myPanel.add(runTime);

		pressure = new TableSlider("lbs", 0, 50, 5, 15, this);
		pressure.setLocation(160, 30);
		pressure.setSize(160, 360);
		myPanel.add(pressure);

		vibration = new TableSlider("% vib", 0, 100, 10, 0, this);
		vibration.setLocation(320, 30);
		vibration.setSize(160, 360);
		myPanel.add(vibration);
		
		speed = new TableSlider("speed", 0, 7, 1, 4, this);
		speed.setLocation(480, 30);
		speed.setSize(160, 360);
		myPanel.add(speed);
		
		controlPanel = new ControlPanel(this);
		controlPanel.setLocation(0, 400);
		controlPanel.setSize(640, 80);
		controlPanel.setTargetTime(0);
		myPanel.add(controlPanel);
		

		myFrame.add(myPanel);
		myFrame.addKeyListener(runTime);
		myFrame.setVisible(true);
		
		Thread t = new Thread(controlPanel);
		t.start();
		
		// TODO this is rather hacky, updates the control panel.
		runTime.requestUpdate();
	}

	@Override
	public void tableSliderUpdated(double value, TableSlider src) {
		if (src == runTime) {
			controlPanel.setTargetTime((int)(Math.round(value) * 60));
		} else if (src == vibration) {
			ArduinoConnection.getInstance().setVibration((int)value);
		} else if (src == pressure) {
			ArduinoConnection.getInstance().setPressure((int)value);
		} else if (src == speed) {
			ArduinoConnection.getInstance().setSpeed((int)value);
		}
	}

	public void setRunningSliderEnabled(boolean e) {
		//System.out.println("Setting enbled...\n");
		runTime.setComponentEnabled(e);
	}
}
