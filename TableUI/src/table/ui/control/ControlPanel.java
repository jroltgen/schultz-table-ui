package table.ui.control;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JComponent;

import table.ArduinoConnection;
import table.ui.TableUI;
import table.ui.widgetlisteners.ButtonListener;
import table.ui.widgets.Button;
import table.ui.widgets.TextField;
import table.ui.widgets.TimeDisplay;

public class ControlPanel extends JComponent implements ButtonListener, Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -443837526010491012L;

	private Button startStopButton;
	//private Button stopButton;
	private Button resetButton;
	
	private TimeDisplay timeDisplay;

	//private TextField timeRemainingField;
	//private TextField timeElapsedField;
	
	private int secondsRemaining;
	
	private boolean _running = false;

	private int _targetRunTimeSeconds;

	private TableUI _tableUI;

	public ControlPanel(TableUI ui) {
		
		_tableUI = ui;
		
		startStopButton = new Button(this, Color.GREEN, "RUN");
		//startButton = new Button(this, Color.GREEN, "RUN");
		//stopButton = new Button(this, Color.RED, "STOP");
		resetButton = new Button(this, Color.YELLOW, "RESET");
		//timeRemainingField = new TextField(18);
		//timeElapsedField = new TextField(18);
		
		timeDisplay = new TimeDisplay();

		//timeRemainingField.setText("15:00");
		//timeElapsedField.setText("00:00");

		startStopButton.setSize(100, 70);
		//stopButton.setSize(100, 70);
		resetButton.setSize(100, 70);
		timeDisplay.setSize(300, 70);
		//timeRemainingField.setSize(80, 30);
		//timeElapsedField.setSize(80, 30);

		//stopButton.setLocation(375, 5);
		startStopButton.setLocation(510, 5);
		resetButton.setLocation(30, 5);
		timeDisplay.setLocation(170, 5);
		
		startStopButton.setComponentEnabled(true);

		//timeRemainingField.setLocation(280, 5);
		//timeElapsedField.setLocation(280, 45);

		add(startStopButton);
		add(resetButton);
		add(timeDisplay);
		//add(startButton);
		//add(timeRemainingField);
		//add(timeElapsedField);
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		g2.setColor(Color.GRAY);
		//g2.fillRect(0, 0, getWidth(), getHeight());

		// Draw stop lights
		/*
		if (_running) {
			g2.setColor(Color.GREEN);
		} else {
			g2.setColor(Color.DARK_GRAY);
		}
		g2.fillOval(15, 15, 20, 20);
		g2.fillOval(getWidth() - 35, 15, 20, 20);
		if (_running) {
			g2.setColor(Color.DARK_GRAY);
		} else {
			g2.setColor(Color.RED);
		}
		g2.fillOval(15, getHeight() - 35, 20, 20);
		g2.fillOval(getWidth() - 35, getHeight() - 35, 20, 20);

		g2.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke(2));
		g2.drawOval(15, 15, 20, 20);
		g2.drawOval(15, getHeight() - 35, 20, 20);
		g2.drawOval(getWidth() - 35, 15, 20, 20);
		g2.drawOval(getWidth() - 35, getHeight() - 35, 20, 20);
		*/
		// Draw texts
		g2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
		g2.drawString("Remaining:", 180, 20 + 6);
		g2.drawString("Elapsed:", 200, 60 + 6);
	}

	@Override
	public void buttonPressed(Button src) {
		if (src == startStopButton && _running == false) {
			start();
		} else if (src == startStopButton && _running == true) {
			stop();
		} else if (src == resetButton) {
			reset();
		}
	}
	
	private void stop() {
		_running = false;
		_tableUI.setRunningSliderEnabled(true);
		resetButton.setComponentEnabled(true);
		startStopButton.setColor(Color.GREEN);
		startStopButton.setText("RUN");
		//startButton.setComponentEnabled(true);
		//stopButton.setComponentEnabled(false);
		ArduinoConnection.getInstance().stop();
		repaint();
	}
	
	private void start() {
		_running = true;
		_tableUI.setRunningSliderEnabled(false);
		resetButton.setComponentEnabled(false);
		
		startStopButton.setColor(Color.RED);
		startStopButton.setText("STOP");
		//stopButton.setComponentEnabled(true);
		//startButton.setComponentEnabled(false);
		ArduinoConnection.getInstance().start();
		repaint();
	}

	private void reset() {
		stop();
		secondsRemaining = _targetRunTimeSeconds;
		updateTime();
	}
	
	public void run() {
		final int timerUpdateRate = 1000;
		final int watchDogUpdateRate = 500;
	
		long lastTimerUpdate = System.currentTimeMillis() - 1000;
		long lastWatchDogUpdate = System.currentTimeMillis() - 1000;
		while(true) {
			long time = System.currentTimeMillis();
			
			if (_running) {
				if (time - lastTimerUpdate > timerUpdateRate) {
					secondsRemaining--;
					updateTime();
					lastTimerUpdate = time;
				}
			}
			if (time - lastWatchDogUpdate > watchDogUpdateRate) {
				petTheWatchdog();
				lastWatchDogUpdate = time;
			}
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (secondsRemaining < 1 && _running) {
				stop();
			}
		}
	}

	

	private void updateTime() {
		timeDisplay.setCurrentSeconds(_targetRunTimeSeconds - secondsRemaining);
		/*int mRemaining = secondsRemaining / 60;
		int sRemaining = secondsRemaining % 60;
		
		String s = "";
		if (mRemaining < 10) {
			s += "0";
		}
		s += mRemaining + ":";
		if (sRemaining < 10) {
			s += "0";
		}
		s += sRemaining;
		//timeRemainingField.setText(s);
		
		int secondsElapsed = _targetRunTimeSeconds - secondsRemaining;
		int mElapsed = secondsElapsed / 60;
		int sElapsed = secondsElapsed % 60;
		s = "";
		if (mElapsed < 10) {
			s += "0";
		}
		s += mElapsed + ":";
		if (sElapsed < 10) {
			s += "0";
		}
		s += sElapsed;
		//timeElapsedField.setText(s);
		*/
		
	}

	private void petTheWatchdog() {
		// TODO Auto-generated method stub
		ArduinoConnection.getInstance().keepAlive();
	}
	
	public void setTargetTime(int tseconds) {
		_targetRunTimeSeconds = tseconds;
		secondsRemaining = _targetRunTimeSeconds;
		timeDisplay.setTotalSeconds(tseconds);
		updateTime();
	}
	
}
