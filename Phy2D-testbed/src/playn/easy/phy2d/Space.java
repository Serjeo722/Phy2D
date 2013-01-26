package playn.easy.phy2d;

import java.util.ArrayList;

public class Space {

	public enum BarType {
		STATIC, DYNAMIC, RED;
	}

	private ArrayList<DynamicBar> dynamicBars = new ArrayList<DynamicBar>();
	private ArrayList<StaticBar> staticBars = new ArrayList<StaticBar>();
	
	public void step(double delta){

		for(DynamicBar b:dynamicBars){

			boolean collided = false;
			
			// top, right
			if ((b.vy > 0) && (b.vx > 0)) {
				double new_x=0;
				double new_y=0;
				
				double x_min = b.x + b.w + b.vx;
				double y_min = b.y + b.h + b.vy;
				double path_min=b.path();

				boolean horizontal_flip = false;
				boolean vertical_flip = false;				
				
				for (StaticBar bar : staticBars) {
					
					// top
					double x = b.x + (bar.bottom - b.y - b.h) * b.vx / b.vy;
					if (!((bar.right < x - b.w) || (bar.left > x + b.w))) {
						if ((b.y + b.h < bar.bottom) && (b.pathY(bar.bottom-b.y-b.h) < path_min)) {
							y_min = bar.bottom;
							path_min=b.pathY(bar.bottom-b.y-b.h);
							new_y = y_min - b.h;
							new_x = x;
							horizontal_flip = true;
							vertical_flip = false;
						}
					}
					
					// right
					double y = b.y + (bar.left - b.x - b.w) * b.vy / b.vx;
					if (!((bar.top < y - b.h) || (bar.bottom > y + b.h))) {
						if ((b.x + b.w < bar.left) && (b.pathX(bar.left-b.x-b.w) < path_min)) {
							x_min = bar.left;
							path_min=b.pathX(bar.left-b.x-b.w);
							new_x = x_min - b.w;
							new_y = y;
							horizontal_flip = false;
							vertical_flip = true;
						}
					}
				}
				if ((new_x != 0) || (new_y != 0)) {
					b.x = new_x;
					b.y = new_y;
					if (horizontal_flip) b.collide(b.vx, -b.vy);
					if (vertical_flip) b.collide(-b.vx, b.vy);
					collided = horizontal_flip || vertical_flip;
				}
			}

			//bottom, left
			if ((b.vy < 0) && (b.vx < 0)) {
				double new_x=0;
				double new_y=0;
				
				double y_max = b.y - b.h + b.vy;
				double x_max = b.x - b.w + b.vx;
				double path_min=b.path();

				boolean horizontal_flip = false;
				boolean vertical_flip = false;				

				for (StaticBar bar : staticBars) {

					// bottom
					double x = b.x - (b.y - b.h-bar.top) * b.vx / b.vy;
					if (!((bar.right < x - b.w) || (bar.left > x + b.w))) {
						if ((b.y - b.h > bar.top) && (b.pathY(b.y - b.h-bar.top) < path_min)) {
							y_max = bar.top;
							path_min=b.pathY(b.y - b.h-bar.top);
							new_y = y_max + b.h;
							new_x = x;
							horizontal_flip = true;
							vertical_flip = false;
						}
					}
					
					// left
					double y = b.y - (b.x - b.w-bar.right) * b.vy / b.vx;
					if (!((bar.top < y - b.h) || (bar.bottom > y + b.h))) {
						if ((b.x - b.w > bar.right) && (b.pathX(b.x-b.w-bar.right) < path_min)) {
							x_max = bar.right;
							path_min=b.pathX(b.x-b.w-bar.right);
							new_x = x_max + b.w;
							new_y = y;
							horizontal_flip = false;
							vertical_flip = true;
						}
					}

				}
				if ((new_x != 0) || (new_y != 0)) {
					b.x = new_x;
					b.y = new_y;
					if (horizontal_flip) b.collide(b.vx, -b.vy);
					if (vertical_flip) b.collide(-b.vx, b.vy);
					collided = horizontal_flip || vertical_flip;
				}
			}

			
			// top, left
			if ((b.vy > 0) && (b.vx < 0)) {
				double new_x=0;
				double new_y=0;
				
				double x_max = b.x - b.w + b.vx;
				double y_min = b.y + b.h + b.vy;
				double path_min=b.path();
				
				boolean horizontal_flip = false;
				boolean vertical_flip = false;				
				
				for (StaticBar bar : staticBars) {
					double x = b.x + (bar.bottom - b.y - b.h) * b.vx / b.vy;
					
					// top
					if (!((bar.right < x - b.w) || (bar.left > x + b.w))) {
						if ((b.y + b.h < bar.bottom) && (b.pathY(bar.bottom-b.y-b.h) < path_min)) {
							y_min = bar.bottom;
							path_min=b.pathY(bar.bottom-b.y-b.h);
							new_y = y_min - b.h;
							new_x = x;
							horizontal_flip = true;
							vertical_flip = false;
						}
					}
					
					// left
					double y = b.y - (b.x - b.w-bar.right) * b.vy / b.vx;
					if (!((bar.top < y - b.h) || (bar.bottom > y + b.h))) {
						if ((b.x - b.w > bar.right) && (b.pathX(b.x-b.w-bar.right) < path_min)) {
							x_max = bar.right;
							path_min=b.pathX(b.x-b.w-bar.right);
							new_x = x_max + b.w;
							new_y = y;
							horizontal_flip = false;
							vertical_flip = true;
						}
					}
				}
				if ((new_x != 0) || (new_y != 0)) {
					b.x = new_x;
					b.y = new_y;
					if (horizontal_flip) b.collide(b.vx, -b.vy);
					if (vertical_flip) b.collide(-b.vx, b.vy);
					collided = horizontal_flip || vertical_flip;
				}
			}

			
			//bottom, right
			if ((b.vy < 0) && (b.vx > 0)) {
				double new_x=0;
				double new_y=0;
				
				double y_max = b.y - b.h + b.vy;
				double x_min = b.x + b.w + b.vx;
				double path_min=b.path();

				boolean horizontal_flip = false;
				boolean vertical_flip = false;				

				for (StaticBar bar : staticBars) {

					// bottom
					double x = b.x - (b.y - b.h-bar.top) * b.vx / b.vy;
					if (!((bar.right < x - b.w) || (bar.left > x + b.w))) {
						if ((b.y - b.h > bar.top) && (b.pathY(b.y - b.h-bar.top) < path_min)) {
							y_max = bar.top;
							path_min=b.pathY(b.y - b.h-bar.top);
							new_y = y_max + b.h;
							new_x = x;
							horizontal_flip = true;
							vertical_flip = false;
						}
					}
					
					// right
					double y = b.y + (bar.left - b.x - b.w) * b.vy / b.vx;
					if (!((bar.top < y - b.h) || (bar.bottom > y + b.h))) {
						if ((b.x + b.w < bar.left) && (b.pathX(bar.left-b.x-b.w) < path_min)) {
							x_min = bar.left;
							path_min=b.pathX(bar.left-b.x-b.w);
							new_x = x_min - b.w;
							new_y = y;
							horizontal_flip = false;
							vertical_flip = true;
						}
					}
				}
				if ((new_x != 0) || (new_y != 0)) {
					b.x = new_x;
					b.y = new_y;
					if (horizontal_flip) b.collide(b.vx, -b.vy);
					if (vertical_flip) b.collide(-b.vx, b.vy);
					collided = horizontal_flip || vertical_flip;
				}
			}
			
			if(!collided){
				b.x += b.vx;
				b.y += b.vy;
			}
		}
		
		
	}
	
	public void add(StaticBar bar){
		staticBars.add(bar);
	}

	public void add(DynamicBar bar){
		dynamicBars.add(bar);
	}

	
	public boolean cross(DynamicBar b){
		boolean cross = false;
		
		for (StaticBar bar : staticBars)
			cross = cross || bar.isPointInside(b.x-b.w, b.y-b.h)
					|| bar.isPointInside(b.x+b.w, b.y-b.h)
					|| bar.isPointInside(b.x-b.w, b.y+b.h)
					|| bar.isPointInside(b.x+b.w, b.y+b.h);
		return cross;
	}
	
	public void render(DebugRenderer render){
		for(DynamicBar bar:dynamicBars) bar.render(render);

		for(StaticBar bar:staticBars){
			render.line(bar.left, bar.top, bar.right, bar.top, BarType.STATIC);
			render.line(bar.left, bar.bottom, bar.right, bar.bottom, BarType.STATIC);
			render.line(bar.left, bar.top, bar.left, bar.bottom, BarType.STATIC);
			render.line(bar.right, bar.top, bar.right, bar.bottom, BarType.STATIC);
		}
		
		
		/*
		for(Bar bar:staticBars){
			
			Bar.Type type1 = Bar.Type.STATIC;
			Bar.Type type2 = Bar.Type.STATIC;
			Bar.Type type3 = Bar.Type.STATIC;
			Bar.Type type4 = Bar.Type.STATIC;

			int L = (int) (bar.x - bar.width / 2);
			int R = (int) (bar.x + bar.width / 2);
			int T = (int) (bar.y + bar.height / 2);
			int B = (int) (bar.y - bar.height / 2);

			for(Bar b2:dynamicBars){
				if (b2.isHorizontalCross(L, R, T)) type1=Bar.Type.DYNAMIC;
				if (b2.isHorizontalCross(L, R, B)) type2=Bar.Type.DYNAMIC;
				if (b2.isVerticalCross(B, T, L)) type3 = Bar.Type.DYNAMIC;
				if (b2.isVerticalCross(B, T, R)) type4 = Bar.Type.DYNAMIC;
			}
			
			render.line(L, T, R, T, type1);
			render.line(L, B, R, B, type2);
			render.line(L, T, L, B, type3);
			render.line(R, T, R, B, type4);
		}*/ 
	}
	
}
