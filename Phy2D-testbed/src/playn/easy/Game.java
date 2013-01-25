package playn.easy;

import playn.core.ImmediateLayer;
import playn.core.ImmediateLayer.Renderer;
import playn.core.PlayN;
import playn.core.Surface;
import playn.easy.phy2d.Bar.Type;
import playn.easy.phy2d.Bar;
import playn.easy.phy2d.DebugRenderer;
import playn.easy.phy2d.Space;

public class Game implements playn.core.Game, DebugRenderer, Renderer {

	private Space space;
	
	@Override
	public void init() {
		space = new Space();
		ImmediateLayer layer=PlayN.graphics().createImmediateLayer(1024,800, this);
		PlayN.graphics().rootLayer().add(layer);
		
		space.add(new Bar(10,400,20,800,Bar.Type.STATIC));
		space.add(new Bar(1024-10,400,20,800,Bar.Type.STATIC));
		space.add(new Bar(1024/2,10,1024,20,Bar.Type.STATIC));
		space.add(new Bar(1024/2,800-10,1024,20,Bar.Type.STATIC));
		
		
		for(int i=0;i<100;i++){
			int x=(int)(Math.random()*1024);
			int y=(int)(Math.random()*800);
			Bar bar = new Bar(x, y, 50, 50, Bar.Type.STATIC);
			if (!space.cross(bar)) space.add(bar);
		}
		
		for(int i=0;i<2000;i++){
			int x=(int)(Math.random()*1024);
			int y=(int)(Math.random()*800);
			double vx=1-Math.random()*2;
			double vy=1-Math.random()*2;

			Bar bar=new Bar(x,y,10,10,Bar.Type.DYNAMIC);
			bar.setSpeed(vx, vy);
			space.add(bar);
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
	public void line(int x1, int y1, int x2, int y2, Type type) {
		int color=0xffffffff;
		if(type==Bar.Type.DYNAMIC) color=0xff00ffff;
		surface.setFillColor(color);
		surface.drawLine(x1, y1, x2, y2, 1);
	}

	private Surface surface;
	
	@Override
	public void render(Surface surface) {
		this.surface=surface;
		space.render(this);
	}

}
