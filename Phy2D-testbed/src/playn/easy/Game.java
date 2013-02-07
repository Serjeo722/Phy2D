package playn.easy;

import playn.easy.phy2d.Canvas;
import playn.easy.phy2d.DebugRenderer;
import playn.easy.phy2d.DynamicBar;
import playn.easy.phy2d.Log;
import playn.easy.phy2d.Space;
import playn.easy.phy2d.Space.BarType;
import playn.easy.phy2d.VelocityChanger;
import playn.easy.phy2d.StaticBar;

public class Game implements DebugRenderer, VelocityChanger {

	public Canvas canvas;
	public static Log log;
	
	private Space space;
	private int BOUND = 30;
	private int SIZE = 5;
	private int STATIC_SIZE = 50;	
	private FPSCounter fps = new FPSCounter();
	
	public void init() {
		
		space = new Space(1024, 800, this);
		
		for(int i=0;i<100;i++){
			int x=BOUND+STATIC_SIZE/2+(int)(Math.random()*(1024-2*(BOUND+STATIC_SIZE/2)));
			int y=BOUND+STATIC_SIZE/2+(int)(Math.random()*(800-2*(BOUND+STATIC_SIZE/2)));
			StaticBar bar = new StaticBar(x, y, STATIC_SIZE*Math.random(), STATIC_SIZE*Math.random());
			space.add(bar);
		}
		
		space.rebuildStaticIndexes();
		
		int i=0;
		while(i<1000){
			int x = BOUND + SIZE + (int) (Math.random() * (1024 - 2 * (BOUND + SIZE)));
			int y = BOUND + SIZE + (int) (Math.random() * (800 - 2 * (BOUND + SIZE)));
			double vx = 5 - Math.random() * 10;
			double vy = 2 - Math.random() * 4;

			DynamicBar bar = new DynamicBar(x, y, SIZE, SIZE);
			bar.setSpeed(vx, vy);
			if (!space.cross(bar)) {
				space.add(bar);
				i++;
			}
		}


	}

	public void update(float delta) {
		fps.update();
		space.step(delta);
		fps.endUpdate();
	}

	public void paint(float alpha) {
	}

	public int updateRate() {
		return 15;
	}

	BarType previousType=BarType.RED;
	
	public void bar(int x, int y, int width, int height, Space.BarType type){
		if(type!=previousType){
			if(type==BarType.STATIC) canvas.setColor(255, 255, 255);
			if(type==BarType.DYNAMIC) canvas.setColor(0, 255, 255);
			if(type==BarType.RED) canvas.setColor(255, 0, 0);
			previousType=type;
		}
		canvas.bar(x, y, width, height);
	}
	
	public void render() {
		fps.frame();
		space.render(this);
		fps.endFrame();
	}

	public void changeVelocityOf(DynamicBar bar) {
		bar.vy+=0.1f;
	}

}
