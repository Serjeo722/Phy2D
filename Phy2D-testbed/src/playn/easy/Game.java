package playn.easy;

import playn.core.ImmediateLayer;
import playn.core.ImmediateLayer.Renderer;
import playn.core.PlayN;
import playn.core.Surface;
import playn.easy.phy2d.DebugRenderer;
import playn.easy.phy2d.DynamicBar;
import playn.easy.phy2d.Space;
import playn.easy.phy2d.Space.BarType;
import playn.easy.phy2d.StaticBar;

public class Game implements playn.core.Game, DebugRenderer, Renderer {

	private Space space;
	private int BOUND=30;
	private int SIZE=10;
	
	
	@Override
	public void init() {
		space = new Space();
		ImmediateLayer layer=PlayN.graphics().createImmediateLayer(1024,800, this);
		PlayN.graphics().rootLayer().add(layer);
		
		space.add(new StaticBar(BOUND, 400, 0, 800 - 2 * BOUND));
		space.add(new StaticBar(1024 - BOUND, 400, 0, 800 - 2 * BOUND));
		space.add(new StaticBar(1024 / 2, BOUND, 1024 - 2 * BOUND, 0));
		space.add(new StaticBar(1024 / 2, 800 - BOUND, 1024 - 2 * BOUND, 0));		
		
		for(int i=0;i<100;i++){
			int x=(int)(Math.random()*1024);
			int y=(int)(Math.random()*800);
			StaticBar bar = new StaticBar(x, y, 50, 50);
			space.add(bar);
		}
		
		for(int i=0;i<5000;i++){
			int x=BOUND+SIZE+(int)(Math.random()*(1024-2*(BOUND+SIZE)));
			int y=BOUND+SIZE+(int)(Math.random()*(800-2*(BOUND+SIZE)));
			double vx=Math.random()*1;
			double vy=Math.random()*1;

			DynamicBar bar = new DynamicBar(x, y, SIZE*Math.random(), SIZE*Math.random());
			bar.setSpeed(vx, vy);
			if (!space.cross(bar)) space.add(bar);
		}


	}

	@Override
	public void update(float delta) {
		space.step(delta);
	}

	@Override
	public void paint(float alpha) {
	}

	@Override
	public int updateRate() {
		return 15;
	}

	@Override
	public void line(double x1, double y1, double x2, double y2, Space.BarType type){
		int color=0xffffffff;
		if(type==BarType.DYNAMIC) color=0xff00ffff;
		if(type==BarType.RED) color=0xffFF0000;
		
		surface.setFillColor(color);
		surface.drawLine((int) (x1), (int) (y1), (int) (x2), (int) (y2), 1);
	}

	private Surface surface;
	
	@Override
	public void render(Surface surface) {
		this.surface=surface;
		space.render(this);
	}

}
