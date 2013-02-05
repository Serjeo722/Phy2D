import playn.core.PlayN;
import playn.easy.Game;
import playn.java.JavaPlatform;

public class Main{
	
	public static void main(String[] args) {
		JavaPlatform platform = JavaPlatform.register();
		platform.graphics().setSize(1024,800);
	    PlayN.run(new Game());
	}
}