package playn.client;

import playn.core.PlayN;
import playn.easy.Game;
import playn.html.HtmlGame;
import playn.html.HtmlPlatform;

public class Main extends HtmlGame{
	
	@Override
	public void start() {
		HtmlPlatform platform = HtmlPlatform.register();

		platform.setTitle("Phy2D-testbed");
		platform.assets().setPathPrefix("main/");
		platform.graphics().setSize(1024, 800);
		
		HtmlPlatform.disableRightClickContextMenu();

		PlayN.run(new Game());
	}
}
