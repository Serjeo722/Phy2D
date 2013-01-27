package playn.easy.phy2d;

import playn.easy.phy2d.Space.BarType;

public class DynamicBar {
	public double x;
	public double y;
	public double w;
	public double h;
	public double vx;
	public double vy;
	public double px;
	public double py;
	
	
	public DynamicBar(double x,double y,double width,double height){
		this.x = x;
		this.y = y;
		this.w = width / 2;
		this.h = height / 2;
	}
	
	public void setSpeed(double vx, double vy) {
		this.vx=vx;
		this.vy=vy;
		this.px=vx;
		this.py=vy;
	}

	public void render(DebugRenderer r) {
		r.line(x-w, y+h, x+w, y+h, BarType.DYNAMIC);
		r.line(x-w, y-h, x+w, y-h, BarType.DYNAMIC);
		r.line(x-w, y+h, x-w, y-h, BarType.DYNAMIC);
		r.line(x+w, y+h, x+w, y-h, BarType.DYNAMIC);

		//r.line(x, y, x+px, y+py, BarType.RED);
	}
	
	public double path(){
		return Math.sqrt(vx * vx + vy * vy);
	}
	
	public double pathX(double dx){
		return Math.abs(path()*dx/vx);
	}

	public double pathY(double dy){
		return Math.abs(path()*dy/vy);
	}
	
	public void collide(double new_vx, double new_vy){
		this.px=new_vx;
		this.py=new_vy;
		this.vx=new_vx;
		this.vy=new_vy/1.01f;
	}

}
