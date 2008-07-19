package org.projectpossibility;

import javax.microedition.media.Player;
import javax.microedition.lcdui.*;
import javax.microedition.media.MediaException;
import javax.microedition.media.control.VideoControl;

public class VideoCanvas
    extends Canvas {
  public VideoCanvas() {

  }

  public void initControls(VideoControl videoControl, Player player) {

    int width = getWidth();
    int height = getHeight();

    videoControl.initDisplayMode(VideoControl.USE_DIRECT_VIDEO, this);
    //videoControl.initDisplayMode(VideoControl.USE_GUI_PRIMITIVE, this);
    try {
      videoControl.setDisplayLocation(0, 0);
      videoControl.setDisplaySize(width, height);
    }
    catch (MediaException me) {
      try { videoControl.setDisplayFullScreen(true); }
      catch (MediaException me2) {}
    }
    videoControl.setVisible(true);
  }

  public void paint(Graphics g) {
  }

}
