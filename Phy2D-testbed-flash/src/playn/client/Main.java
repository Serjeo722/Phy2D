package playn.client;

import playn.core.PlayN;
import playn.easy.Game;
import playn.flash.FlashGame;
import playn.flash.FlashPlatform;

public class Main extends FlashGame{
	
	@Override
	public void start() {
		FlashPlatform platform = FlashPlatform.register();

		platform.assets().setPathPrefix("main/");

		PlayN.run(new Game());
	}
}
