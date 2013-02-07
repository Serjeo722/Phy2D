package playn.easy;

import java.util.Date;

public class FPSCounter {

	private long prevoiusTime = 0;
	private int frames = 0;
	private int currentFPS = 0;

	private long currentUpdateMs = 0;
	private long currentUpdateTime = 0;

	private long currentFrameMs = 0;
	private long currentFrameTime = 0;

	public void update() {
		currentUpdateTime = System.currentTimeMillis();
	}

	public void endUpdate() {
		currentUpdateMs = System.currentTimeMillis() - currentUpdateTime;
	}

	public void frame() {
		currentFrameTime = System.currentTimeMillis();
		int currentTime = getUnixTimeStamp();
		if (currentTime != prevoiusTime) {
			currentFPS = frames;
			frames = 1;
			prevoiusTime = currentTime;
			Game.log.message("FPS=" + currentFPS + " Update=" + currentUpdateMs + "ms. Frame=" + currentFrameMs+"ms.");
		} else
			frames++;
	}

	public void endFrame() {
		currentFrameMs = System.currentTimeMillis() - currentFrameTime;
	}

	public int get() {
		return (currentFPS);
	}

	private int getUnixTimeStamp() {
		Date date = new Date();
		int iTimeStamp = (int) (date.getTime() * .001);
		return iTimeStamp;
	}
}
