package Util;

public class StaticBar {

	public double left;
	public double right;
	public double top;
	public double bottom;
	
	public StaticBar(double x, double y, double width, double height) {
		left = x - width / 2;
		right = x + width / 2;
		top = y + height / 2;
		bottom = y - height / 2;
	}

	public boolean isPointInside(double x,double y){
		boolean inside=false;
		if ((x >= left) && (x <= right) && (y >= bottom) && (y <= top)) inside = true;
		return inside;
	}
}
