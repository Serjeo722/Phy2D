package playn.easy;

import playn.core.ImmediateLayer;
import playn.core.ImmediateLayer.Renderer;
import playn.core.PlayN;
import playn.core.Surface;
import playn.easy.phy2d.DebugRenderer;
import playn.easy.phy2d.DynamicBar;
import playn.easy.phy2d.Space;
import playn.easy.phy2d.Space.BarType;
import playn.easy.phy2d.VelocityChanger;
import playn.easy.phy2d.StaticBar;

public class Game implements playn.core.Game, DebugRenderer, Renderer, VelocityChanger {

	private Space space;
	private int BOUND = 30;
	private int SIZE = 4;
	private int STATIC_SIZE = 20;	
	private FPSCounter fps=new FPSCounter();
	
	@Override
	public void init() {
		
		space = new Space(1024, 800, this);
		ImmediateLayer layer=PlayN.graphics().createImmediateLayer(1024,800, this);
		PlayN.graphics().rootLayer().add(layer);
		
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
			double vx = 2 - Math.random() * 4;
			double vy = 2 - Math.random() * 4;

			DynamicBar bar = new DynamicBar(x, y, SIZE, SIZE);
			bar.setSpeed(vx, vy);
			if (!space.cross(bar)) {
				space.add(bar);
				i++;
			}
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
		fps.frame();
		this.surface=surface;
		space.render(this);
	}

	@Override
	public void changeVelocityOf(DynamicBar bar) {
		bar.vy+=0.1f;
	}

}
