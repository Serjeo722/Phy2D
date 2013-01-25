package playn.easy.phy2d;

public class Bar {

	public double x;
	public double y;
	public double width;
	public double height;
	public Type type;
	public double vx;
	public double vy;
	
	public enum Type {
		STATIC, DYNAMIC;
	}
	
	public Bar(double x,double y,double width,double height, Type type){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.type = type;
	}

	public void setSpeed(double vx, double vy) {
		this.vx=vx;
		this.vy=vy;
	}
	
	public boolean isPointInside(double xP, double yP) {
		boolean inside = false;
		double left = x - width / 2;
		double right = x + width / 2;
		double top = y + height / 2;
		double bottom = y - height / 2;
		if ((xP > left) && (xP < right) && (yP > bottom) && (yP < top)) inside = true;
		return inside;
	}
	
	
	public boolean isHorizontalCross(double leftP, double rightP ,double yP){
		boolean cross = false;
		double left = x - width / 2;
		double right = x + width / 2;
		double top = y + height / 2;
		double bottom = y - height / 2;
		if ((yP >= bottom) && (yP <= top))
			cross = !(((leftP < left) && (rightP < left)) || ((leftP > right) && (rightP > right)));
		return cross;
	}

	
	public boolean isVerticalCross(double bottomP, double topP ,double xP){
		boolean cross = false;
		double left = x - width / 2;
		double right = x + width / 2;
		double top = y + height / 2;
		double bottom = y - height / 2;
		if ((left <= xP) && (xP <= right))
			cross = !(((bottomP < bottom) && (topP < bottom)) || ((bottomP > top) && (topP > top)));
		return cross;
	}


	public void render(DebugRenderer r) {
		int xl = (int) (x - width / 2);
		int xr = (int) (x + width / 2);
		int yt = (int) (y + height / 2);
		int yb = (int) (y - height / 2);

		r.line(xl, yt, xr, yt, type);
		r.line(xl, yb, xr, yb, type);
		r.line(xl, yt, xl, yb, type);
		r.line(xr, yt, xr, yb, type);
	}
	
	
	private Object o;

	public void setUserData(Object o){
		this.o=o;
	}
	
	public Object getUserData(){
		return o;
	}

}
