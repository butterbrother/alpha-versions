import java.awt.*;
import java.applet.*;
import java.net.*;

public class AudioClipTest extends Applet {
	private AudioClip clip = null;
	private String status = null;

	public void init() {
		String mediaFile = getParameter("media");
		if (mediaFile == null)
			mediaFile = "test.mp3";

		try {
			clip = getAppletContext().getAudioClip(new URL(getCodeBase() + "test.wav"));
		} catch (MalformedURLException e) {
			status = "file not found, " + e.toString();
		}
	}

	public void start() {
		if (status != null)
			showStatus(status);
		if (clip != null)
			clip.play();
	}
}
