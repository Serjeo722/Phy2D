package playn.easy;

import java.util.Date;

import playn.core.PlayN;

public class FPSCounter {
	
	private int prevoiusTime=0;
	private int frames=0;
	private int currentFPS=0;
	
	public void frame(){
		int currentTime=getUnixTimeStamp();
		if(currentTime!=prevoiusTime){
			currentFPS=frames;
			frames=1;
			prevoiusTime=currentTime;
			PlayN.log().debug("FPS="+currentFPS);
		} else
			frames++;
	}
	
	public int get(){
		return(currentFPS);
	}
	
	private int getUnixTimeStamp() {
        Date date = new Date();
        int iTimeStamp = (int) (date.getTime() * .001);
        return iTimeStamp;
	}
}
