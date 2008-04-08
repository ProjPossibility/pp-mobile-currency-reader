package org.projectpossibility;

import javax.microedition.lcdui.*;
import javax.microedition.media.*;
import javax.microedition.midlet.MIDlet;
import java.io.*;


public class MCR extends MIDlet {

	private Display display;
	private boolean offline = false;

	public MCR() {
	}

	public void startApp() {
		display = Display.getDisplay(this);
		VideoRecordingForm aVideoRecordingForm = new VideoRecordingForm("",this,null);
		display.setCurrent(aVideoRecordingForm);
	}

	public void pauseApp() {
	}

	public void destroyApp(boolean unconditional) {
	}

	public Display getDisplay() {
		return display;
	}

}