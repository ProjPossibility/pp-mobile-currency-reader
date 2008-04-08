package org.projectpossibility;

import javax.microedition.lcdui.*;
import javax.microedition.media.*;
import javax.microedition.media.control.*;
import javax.microedition.midlet.MIDlet;
import java.io.*;

public class VideoRecordingForm extends Form
	implements CommandListener, PlayerListener {

		
	private final static Command CMD_EXIT = new Command("Exit", Command.EXIT, 1);
	private final static Command CMD_RECORD = new Command("Take Snapshot", Command.SCREEN, 1);

	private Player player;

	private VideoControl videoControl;

	private VideoCanvas aVideoCanvas;

	String contentType = null;
	ByteArrayOutputStream output = null;

	VideoRecordingThread videoRecordThread = null;

	private MCR parentMidlet = null;
	private Form parentForm = null;

	protected VideoRecordingForm(String in, MCR parentMidlet_, Form parentForm_) {
		super(in);
		this.parentMidlet = parentMidlet_;
		this.parentForm = parentForm_;
		initComponents();
	}

	public void initComponents() {

		addCommand(CMD_EXIT);
		addCommand(CMD_RECORD);
		setCommandListener(this);

		(new CameraThread()).start();
	}
        

	public void commandAction(Command c, Displayable d) {
		if (c == CMD_EXIT) {
			parentMidlet.destroyApp(true);
			parentMidlet.notifyDestroyed();
		}
		else if (c == CMD_RECORD) {
			videoRecordThread = new VideoRecordingThread();
			videoRecordThread.start();
		}
	}

	public void playerUpdate(Player plyr, String evt, Object evtData) {
	}

	private void showCamera() {

		try {

			releaseResources();
			player = Manager.createPlayer("capture://video");
			player.addPlayerListener(this);
			player.realize();

			videoControl = (VideoControl)player.getControl("VideoControl");
			aVideoCanvas = new VideoCanvas();
			aVideoCanvas.initControls(videoControl, player);

			aVideoCanvas.addCommand(CMD_RECORD);
			aVideoCanvas.addCommand(CMD_EXIT);
			aVideoCanvas.setCommandListener(this);
			parentMidlet.getDisplay().setCurrent(aVideoCanvas);

			player.start();
			contentType = player.getContentType();


		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void releaseResources() {
		if ( player != null ) {
			try {
				player.stop();
				player.close();
			}catch(Exception e){}
		}
	}

	class CameraThread extends Thread {

		public CameraThread() {
		}

		public void run() {
			showCamera();
		}
	}

	class VideoRecordingThread extends Thread {
		public VideoRecordingThread() {
		}

		public void run() {
			try{
//				byte [] snap = videoControl.getSnapshot("encoding=jpeg&width=160&height=120");
				byte [] snap = videoControl.getSnapshot("encoding=jpeg");
				if (snap != null) {
					Image im = Image.createImage(snap, 0, snap.length);
					int a = im.getHeight();
					int b = im.getWidth();
					
					System.out.println("my height is "+ a);
					System.out.println("My width is "+ b);
					Alert al = new Alert("MyImage",
						"My dimensions are:"+b+","+a,
						im,
						AlertType.INFO);
					al.setTimeout(2000);
					parentMidlet.getDisplay().setCurrent(al, aVideoCanvas);
				}
			} catch (MediaException me) {
				System.err.println(me);
			}	
		}

	}

}