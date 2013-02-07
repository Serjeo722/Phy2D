import playn.java.JavaPlatform;

public class Main {
	
	static Loader loader;
	
	public static void main(String[] args) {
		JavaPlatform platform = JavaPlatform.register();
		platform.graphics().setSize(1024,800);
		loader=new Loader();
		loader.start();
	}

	
}