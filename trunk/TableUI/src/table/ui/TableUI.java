package table.ui;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;

import table.ui.control.ControlPanel;
import table.ui.slider.TableSlider;
import table.ui.slider.TableSliderListener;

public class TableUI implements TableSliderListener {
	private TableSlider runTime;
	private TableSlider pressure;
	private TableSlider vibration;
	private TableSlider speed;
	private ControlPanel controlPanel;

	/**
	 * For testing
	 */
	public static void main(String[] args) {
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

		runTime = new TableSlider("mins", 0, 30, 5, this);
		runTime.setLocation(0, 30);
		runTime.setSize(160, 350);
		myPanel.add(runTime);

		pressure = new TableSlider("lbs", 0, 10, 5, this);
		pressure.setLocation(160, 30);
		pressure.setSize(160, 350);
		myPanel.add(pressure);

		vibration = new TableSlider("% vib", 0, 100, 10, this);
		vibration.setLocation(320, 30);
		vibration.setSize(160, 350);
		myPanel.add(vibration);
		
		speed = new TableSlider("speed", 0, 100, 25, this);
		speed.setLocation(480, 30);
		speed.setSize(160, 350);
		myPanel.add(speed);
		
		controlPanel = new ControlPanel();
		controlPanel.setLocation(0, 400);
		controlPanel.setSize(640, 80);
		controlPanel.setTargetTime(0);
		myPanel.add(controlPanel);
		

		myFrame.add(myPanel);
		myFrame.addKeyListener(runTime);
		myFrame.setVisible(true);
		
		Thread t = new Thread(controlPanel);
		t.start();
	}

	@Override
	public void tableSliderUpdated(double value, TableSlider src) {
		// TODO Auto-generated method stub
		if (src == runTime) {
			controlPanel.setTargetTime((int)(Math.round(value) * 60));
		}
	}
}
