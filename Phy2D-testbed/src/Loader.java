import playn.core.ImmediateLayer;
import playn.core.ImmediateLayer.Renderer;
import playn.core.PlayN;
import playn.core.Surface;
import playn.easy.Game;
import playn.easy.phy2d.Log;

class Loader implements playn.easy.phy2d.Canvas, Log, Renderer, playn.core.Game {
	private Game game;
	
	public Loader() {
		ImmediateLayer layer=PlayN.graphics().createImmediateLayer(1024,800, this);
		PlayN.graphics().rootLayer().add(layer);

		game = new playn.easy.Game();

		game.canvas = this;
		Game.log = this;
	}

	public void start(){
		PlayN.run(this);
	}
	
	@Override
	public void message(String message) {
		PlayN.log().debug(message);
	}

	@Override
	public void setColor(int r, int g, int b) {
		surface.setFillColor(0xFF000000+r*0x10000+g*0x100+b);
	}

	@Override
	public void bar(int x, int y, int width, int height) {
		surface.drawLine(x, y, x+width, y, 1);
		surface.drawLine(x, y, x, y+height, 1);
		surface.drawLine(x+width, y+height, x+width, y, 1);
		surface.drawLine(x+width, y+height, x, y+height, 1);
	}

	private Surface surface;
	@Override
	public void render(Surface surface) {
		this.surface=surface;
		game.render();
	}

	@Override
	public void init() {
		game.init();
	}

	@Override
	public void update(float delta) {
		game.update(delta);
	}

	@Override
	public void paint(float alpha) {
		game.paint(alpha);
	}

	@Override
	public int updateRate() {
		return game.updateRate();
	}

}