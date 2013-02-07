package playn.client;

import playn.easy.Game;
import playn.easy.phy2d.Log;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

public class Main implements EntryPoint, playn.easy.phy2d.Canvas, Log {

	// canvas size, in px
	private static final int width = 1024;
	private static final int height = 800;
	private static final String holderId = "canvasholder";
	private final CssColor redrawColor = CssColor.make("rgba(0,0,0,1)");

	private Game game;

	private Canvas[] buffers = new Canvas[2];

	@Override
	public void onModuleLoad() {
		for (int j = 0; j < 2; j++) {
			buffers[j] = Canvas.createIfSupported();
			if (buffers[j] == null) {
				RootPanel.get(holderId).add(
						new Label("HTML5 Canvas not supported"));
				return;
			} else {
				buffers[j].setWidth(width + "px");
				buffers[j].setHeight(height + "px");
				buffers[j].setCoordinateSpaceWidth(width);
				buffers[j].setCoordinateSpaceHeight(height);

				Context2d context = buffers[j].getContext2d();

				context.setShadowBlur(0);
				context.setShadowOffsetX(0);
				context.setShadowOffsetY(0);
				context.setLineWidth(1);

				RootPanel.get(holderId).add(buffers[j]);
			}
		}

		game = new Game();
		game.init();
		game.canvas=this;
		Game.log=this;
		
		animate();
	}

	native void animate() /*-{
		this.@playn.client.Main::doUpdate()();
		var self = this;
		var callback = $entry(function() {
			self.@playn.client.Main::animate()();
		});
		$wnd.requestAnimFrame(callback);
	}-*/;

	int drawingBuffer = 0;

	private void doUpdate() {
		buffers[1 - drawingBuffer].setVisible(false);
		buffers[drawingBuffer].setVisible(true);
		render(buffers[1 - drawingBuffer]);
		drawingBuffer = 1 - drawingBuffer;
	}

	private Context2d context;
	
	private void render(Canvas canvas) {
		context = canvas.getContext2d();
		context.setFillStyle(CssColor.make("rgba(0,0,0,1)"));
		context.fillRect(0, 0, width, height);
		
		game.update(1000/60);
		game.render();
	}

	public void setColor(int r, int g, int b) {
		context.setStrokeStyle(CssColor.make("rgba(" + r + "," + g + "," + b + ",1)"));
	}

	public void bar(int x, int y, int width, int height) {
		context.strokeRect(x - 0.5f, y - 0.5f, width, height);
	}

	public void message(String message) {
		RootPanel.get("fps").getElement().setInnerHTML(message);
	}
}
