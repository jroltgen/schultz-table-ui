package table;

import java.io.IOException;

public class ArduinoConnectionDummyImpl extends ArduinoConnection {

	@Override
	public boolean connect() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getIndicatedPressure() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected boolean handleAck() throws IOException {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean keepAlive() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean setPressure(int pounds) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean setSpeed(int percentage) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean setVibration(int percentage) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean start() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean stop() {
		// TODO Auto-generated method stub
		return true;
	}

}
