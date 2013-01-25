package playn.easy.phy2d;

import java.util.ArrayList;

public class Space {
	
	private ArrayList<Bar> dynamicBars = new ArrayList<Bar>();
	private ArrayList<Bar> staticBars = new ArrayList<Bar>();
	private ArrayList<Bar> deleted = new ArrayList<Bar>();
	
	public void step(double delta){

		for(Bar bar:dynamicBars){
			bar.x+=bar.vx;
			bar.y+=bar.vy;
		}
		
		for(Bar del:deleted){
			if(del.type==Bar.Type.DYNAMIC) dynamicBars.remove(del);
			if(del.type==Bar.Type.STATIC) staticBars.remove(del);
		} 
	}
	
	public void add(Bar bar){
		if(bar.type==Bar.Type.DYNAMIC) dynamicBars.add(bar);
		if(bar.type==Bar.Type.STATIC) staticBars.add(bar);
		
	}

	public void delete(Bar bar){
		deleted.add(bar);
	}

	public boolean cross(Bar b){
		boolean cross = false;
		double left = b.x - b.width / 2;
		double right = b.x + b.width / 2;
		double top = b.y + b.height / 2;
		double bottom = b.y - b.height / 2;
		
		for (Bar bar : dynamicBars)
			cross = cross || bar.isPointInside(left, top)
					|| bar.isPointInside(left, bottom)
					|| bar.isPointInside(right, top)
					|| bar.isPointInside(right, bottom);
		for (Bar bar : staticBars)
			cross = cross || bar.isPointInside(left, top)
					|| bar.isPointInside(left, bottom)
					|| bar.isPointInside(right, top)
					|| bar.isPointInside(right, bottom);
		return cross;
	}
	
	public void render(DebugRenderer render){
		for(Bar bar:dynamicBars) bar.render(render);
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
		} 
	}
	
}
