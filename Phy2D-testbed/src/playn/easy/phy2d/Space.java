package playn.easy.phy2d;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import playn.easy.Game;

public class Space {

	public enum BarType {
		STATIC, DYNAMIC, RED;
	}
	
	private class StaticBarPair {
		public double bL;
		public double bH;
		public int L;
		public int H;
		public double value;

		public StaticBarPair(double value) {
			this.value = value;
		}

		public StaticBarPair(int l, int h, double bL, double bH) {
			this.L = l;
			this.H = h;
			this.bL = bL;
			this.bH = bH;
		}
	}
	
	private static double MAX_VELOCITY = 100000f;
	//private static double MIN_VELOCITY = 10f;
	
	private ArrayList<DynamicBar> dynamicBars = new ArrayList<DynamicBar>();
	private ArrayList<StaticBar> staticBars = new ArrayList<StaticBar>();
	private VelocityChanger velocityChanger = null;
	
	public Space(int width, int height, VelocityChanger speedChanger) {
		this.velocityChanger = speedChanger;
		
		add(new StaticBar(0, height / 2, 0, height));
		add(new StaticBar(width, height / 2, 0, width));
		add(new StaticBar(width / 2, 0, width, 0));
		add(new StaticBar(width / 2, height, width, 0));
	}

	private List<StaticBar> indexStaticBottom;
	private ArrayList<StaticBarPair> indexStaticPairsBottom;  

	private List<StaticBar> indexStaticTop;
	private ArrayList<StaticBarPair> indexStaticPairsTop;  

	private List<StaticBar> indexStaticLeft;
	private ArrayList<StaticBarPair> indexStaticPairsLeft;  

	private List<StaticBar> indexStaticRight;
	private ArrayList<StaticBarPair> indexStaticPairsRight;  

	private Comparator<StaticBar> bottomComparator = new Comparator<StaticBar>(){
		@Override
		public int compare(StaticBar b1, StaticBar b2) {
			return b1.bottom < b2.bottom ? -1 : b1.bottom > b2.bottom ? 1 : 0;
		}
	};

	private Comparator<StaticBar> leftComparator = new Comparator<StaticBar>(){
		@Override
		public int compare(StaticBar b1, StaticBar b2) {
			return b1.left < b2.left ? -1 : b1.left > b2.left ? 1 : 0;
		}
	};
	
	private Comparator<StaticBar> topComparator = new Comparator<StaticBar>(){
		@Override
		public int compare(StaticBar b1, StaticBar b2) {
			return b1.top > b2.top ? -1 : b1.top < b2.top ? 1 : 0;
		}
	};		

	private Comparator<StaticBar> rightComparator = new Comparator<StaticBar>(){
		@Override
		public int compare(StaticBar b1, StaticBar b2) {
			return b1.right > b2.right ? -1 : b1.right < b2.right ? 1 : 0;
		}
	};		
	
	private int lastStaticChangeN = 0;
	private int previousStaticChangeN = 0;
	
	public void rebuildStaticIndexes(){
		if (previousStaticChangeN != lastStaticChangeN) {
			binaryIndexTop();
			binaryIndexRight();
			binaryIndexBottom();
			binaryIndexLeft();
			previousStaticChangeN = lastStaticChangeN;
		}
	}

	private double dynamicMaxSize = 3;
	private double dynamicIndexSize = 0;
	public void rebuildDynamicIndexes(){
		if (dynamicMaxSize > dynamicIndexSize) {
			Game.log.message("rebuilding dynamic indexes with grid size="+dynamicMaxSize);
			dynamicIndexSize=dynamicMaxSize;
		}
	}
	
	public void step(double delta){

		// rebuild indexes if need
		rebuildStaticIndexes();
		rebuildDynamicIndexes();
		
		for(DynamicBar b:dynamicBars){
			if (velocityChanger != null)
				velocityChanger.changeVelocityOf(b);
			
			highVelocityBoundCheck(b);
			//lowVelocityBoundCheck(b);

			boolean collided = false;

			// top
			if ((b.vx == 0) && (b.vy > 0) && (!collided)) {
				double new_y=0;
				double y_min = b.y + b.h + b.vy;
				
				// indexed collision check
				int indexBottom = binarySearchBottom(b.y + b.h);
				while ((indexBottom >= 0) && (indexBottom < indexStaticBottom.size())) {
					double value=indexStaticBottom.get(indexBottom).bottom;
					if (y_min < value) break;
					if (!((indexStaticBottom.get(indexBottom).right < b.x - b.w) || (indexStaticBottom.get(indexBottom).left > b.x + b.w))) {
						y_min = value;
						new_y = y_min - b.h;
						collided = true;
						break;
					}
					indexBottom++;
				}
				
				/* not indexed collision check
				for (StaticBar bar : staticBars) {
					// top
					if (!((bar.right < b.x - b.w) || (bar.left > b.x + b.w))) {
						if ((b.y + b.h <= bar.bottom) && (bar.bottom < y_min)) {
							y_min = bar.bottom;
							new_y = y_min - b.h;
							collided = true;
						}
					}
				}*/
				if (collided) {
					b.y = new_y;
					b.collide(b.vx, -b.vy);
					collided = true;
				}
			}

			// bottom
			if ((b.vx == 0) && (b.vy < 0) && (!collided)) {				
				double new_y=0;
				double y_max = b.y - b.h + b.vy;

				// indexed collision search
				int indexTop = binarySearchTop(b.y - b.h);
				while ((indexTop >= 0) && (indexTop < indexStaticTop.size())) {
					double value=indexStaticTop.get(indexTop).top;
					if (y_max > value) break;
					if (!((indexStaticTop.get(indexTop).right < b.x - b.w) || (indexStaticTop.get(indexTop).left > b.x + b.w))) {
						y_max = value;
						new_y = y_max + b.h;
						collided = true;
						break;
					}
					indexTop++;
				}

				/* not indexed collision search
				for (StaticBar bar : staticBars) {
					// bottom
					if (!((bar.right < b.x - b.w) || (bar.left > b.x + b.w))) {
						if ((b.y - b.h >= bar.top) && (bar.top > y_max)) {
							y_max = bar.top;
							new_y=y_max+b.h;
							collided = true;
						}
					}
				}*/
				if (collided) {
					b.y = new_y;
					b.collide(b.vx, -b.vy);
					collided = true;
				}
			}
			
			// left
			if ((b.vx < 0) && (b.vy == 0) && (!collided)) {
				double new_x = 0;
				double x_max = b.x - b.w + b.vx;
				
				// indexed collision search
				int indexRight = binarySearchRight(b.x - b.w);
				while ((indexRight >= 0) && (indexRight < indexStaticRight.size())) {
					double value=indexStaticRight.get(indexRight).right;
					if (x_max > value) break;
					if (!((indexStaticRight.get(indexRight).top < b.y - b.h) || (indexStaticRight.get(indexRight).bottom > b.y + b.h))) {
						x_max = value;
						new_x = x_max + b.w;
						collided = true;
						break;
					}
					indexRight++;
				}

				
				/* not indexed collision search
				for (StaticBar bar : staticBars) {
					// left
					if (!((bar.top < b.y - b.h) || (bar.bottom > b.y + b.h))) {
						if ((b.x - b.w >= bar.right) && (bar.right > x_max)) {
							x_max = bar.right;
							new_x = x_max + b.w;
							collided = true;
						}
					}
				}*/
				if (collided) {
					b.x = new_x;
					b.collide(-b.vx, b.vy);
					collided = true;
				}
			}			
			
			//right
			if ((b.vx > 0) && (b.vy == 0) && (!collided)) {
				double new_x = 0;
				double x_min = b.x + b.w + b.vx;
				
				// indexed collision check
				int indexLeft = binarySearchLeft(b.x + b.w);
				while ((indexLeft >= 0) && (indexLeft < indexStaticLeft.size())) {
					double value=indexStaticLeft.get(indexLeft).left;
					if (x_min < value) break;
					if (!((indexStaticLeft.get(indexLeft).top < b.y - b.h) || (indexStaticLeft.get(indexLeft).bottom > b.y + b.h))) {
						x_min = value;
						new_x = x_min - b.w;
						collided = true;
					}
					indexLeft++;
				}
				/* not indexed collision check
				for (StaticBar bar : staticBars) {
					// right
					if (!((bar.top < b.y - b.h) || (bar.bottom > b.y + b.h))) {
						if ((b.x + b.w <= bar.left) && (bar.left < x_min)) {
							x_min = bar.left;
							new_x = x_min - b.w;
							collided = true;
						}
					}
				}*/
				if (collided) {
					b.x = new_x;
					b.collide(-b.vx, b.vy);
					collided = true;
				}
			}			
			

			// top, right
			if ((b.vy > 0) && (b.vx > 0) && (!collided)) {
				double new_x=0;
				double new_y=0;
				
				double x_min = b.x + b.w + b.vx;
				double y_min = b.y + b.h + b.vy;
				double path_min = b.path();

				boolean horizontal_flip = false;
				boolean vertical_flip = false;				

				// top indexed collision check
				int indexBottom = binarySearchBottom(b.y + b.h);
				while ((indexBottom >= 0) && (indexBottom < indexStaticBottom.size())) {
					double value=indexStaticBottom.get(indexBottom).bottom;
					if (y_min < value) break;
					double x = b.x + (indexStaticBottom.get(indexBottom).bottom - b.y - b.h) * b.vx / b.vy;
					if (!((indexStaticBottom.get(indexBottom).right < x - b.w) || (indexStaticBottom.get(indexBottom).left > x + b.w))) {
						if ((b.y + b.h <= indexStaticBottom.get(indexBottom).bottom) && (b.pathY(indexStaticBottom.get(indexBottom).bottom - b.y - b.h) < path_min)) {
							y_min = value;
							path_min = b.pathY(indexStaticBottom.get(indexBottom).bottom - b.y - b.h);
							new_y = y_min - b.h;
							new_x = x;
							horizontal_flip = true;
							vertical_flip = false;
							break;
						}
					}
					indexBottom++;
				}

				// right indexed collision check
				int indexLeft = binarySearchLeft(b.x + b.w);
				while ((indexLeft >= 0) && (indexLeft < indexStaticLeft.size())) {
					double value=indexStaticLeft.get(indexLeft).left;
					if (x_min < value) break;
					double y = b.y + (indexStaticLeft.get(indexLeft).left - b.x - b.w) * b.vy / b.vx;
					
					if (!((indexStaticLeft.get(indexLeft).top < y - b.h) || (indexStaticLeft.get(indexLeft).bottom > y + b.h))) {
						if ((b.x + b.w <= indexStaticLeft.get(indexLeft).left) && (b.pathX(indexStaticLeft.get(indexLeft).left - b.x - b.w) < path_min)) {
							x_min = value;
							path_min = b.pathX(indexStaticLeft.get(indexLeft).left - b.x - b.w);
							new_x = x_min - b.w;
							new_y = y;
							horizontal_flip = false;
							vertical_flip = true;
						}
					}
					indexLeft++;
				}
				
				/* top-right not indexed search
				for (StaticBar bar : staticBars) {
					
					// top
					double x = b.x + (bar.bottom - b.y - b.h) * b.vx / b.vy;
					if (!((bar.right < x - b.w) || (bar.left > x + b.w))) {
						if ((b.y + b.h <= bar.bottom) && (b.pathY(bar.bottom-b.y-b.h) < path_min)) {
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
						if ((b.x + b.w <= bar.left) && (b.pathX(bar.left-b.x-b.w) < path_min)) {
							x_min = bar.left;
							path_min=b.pathX(bar.left-b.x-b.w);
							new_x = x_min - b.w;
							new_y = y;
							horizontal_flip = false;
							vertical_flip = true;
						}
					}
				}*/
				collided = horizontal_flip || vertical_flip;
				if (collided) {
					b.x = new_x;
					b.y = new_y;
					if (horizontal_flip) b.collide(b.vx, -b.vy);
					if (vertical_flip) b.collide(-b.vx, b.vy);
				}
			}

			//bottom, left
			if ((b.vy < 0) && (b.vx < 0) && (!collided)) {
				double new_x=0;
				double new_y=0;
				
				double y_max = b.y - b.h + b.vy;
				double x_max = b.x - b.w + b.vx;
				double path_min=b.path();

				boolean horizontal_flip = false;
				boolean vertical_flip = false;				

				// bottom indexed collision search
				int indexTop = binarySearchTop(b.y - b.h);
				while ((indexTop >= 0) && (indexTop < indexStaticTop.size())) {
					double value=indexStaticTop.get(indexTop).top;
					if (y_max > value) break;
					double x = b.x - (b.y - b.h-indexStaticTop.get(indexTop).top) * b.vx / b.vy;
					if (!((indexStaticTop.get(indexTop).right < x - b.w) || (indexStaticTop.get(indexTop).left > x + b.w))) {
						if ((b.y - b.h >= indexStaticTop.get(indexTop).top) && (b.pathY(b.y - b.h-indexStaticTop.get(indexTop).top) < path_min)) {
							y_max = value;
							path_min=b.pathY(b.y - b.h-indexStaticTop.get(indexTop).top);
							new_y = y_max + b.h;
							new_x = x;
							horizontal_flip = true;
							vertical_flip = false;
							break;
						}
					}
					indexTop++;
				}

				// left indexed collision search
				int indexRight = binarySearchRight(b.x - b.w);
				while ((indexRight >= 0) && (indexRight < indexStaticRight.size())) {
					double value=indexStaticRight.get(indexRight).right;
					if (x_max > value) break;
					double y = b.y - (b.x - b.w-indexStaticRight.get(indexRight).right) * b.vy / b.vx;
					if (!((indexStaticRight.get(indexRight).top < y - b.h) || (indexStaticRight.get(indexRight).bottom > y + b.h))) {
						if ((b.x - b.w >= indexStaticRight.get(indexRight).right) && (b.pathX(b.x-b.w-indexStaticRight.get(indexRight).right) < path_min)) {
							x_max = value;
							path_min=b.pathX(b.x-b.w-indexStaticRight.get(indexRight).right);
							new_x = x_max + b.w;
							new_y = y;
							horizontal_flip = false;
							vertical_flip = true;
							break;
						}
					}
					indexRight++;
				}					
					
				/* not indexed bottom+left
				for (StaticBar bar : staticBars) {

					// bottom
					double x = b.x - (b.y - b.h-bar.top) * b.vx / b.vy;
					if (!((bar.right < x - b.w) || (bar.left > x + b.w))) {
						if ((b.y - b.h >= bar.top) && (b.pathY(b.y - b.h-bar.top) < path_min)) {
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
						if ((b.x - b.w >= bar.right) && (b.pathX(b.x-b.w-bar.right) < path_min)) {
							x_max = bar.right;
							path_min=b.pathX(b.x-b.w-bar.right);
							new_x = x_max + b.w;
							new_y = y;
							horizontal_flip = false;
							vertical_flip = true;
						}
					}
				}*/
				collided = horizontal_flip || vertical_flip;
				if (collided) {
					b.x = new_x;
					b.y = new_y;
					if (horizontal_flip) b.collide(b.vx, -b.vy);
					if (vertical_flip) b.collide(-b.vx, b.vy);
				}
			}

			
			// top, left
			if ((b.vy > 0) && (b.vx < 0) && (!collided)) {
				double new_x=0;
				double new_y=0;
				
				double x_max = b.x - b.w + b.vx;
				double y_min = b.y + b.h + b.vy;
				double path_min=b.path();
				
				boolean horizontal_flip = false;
				boolean vertical_flip = false;				
				
				// top indexed collision check
				int indexBottom = binarySearchBottom(b.y + b.h);
				while ((indexBottom >= 0) && (indexBottom < indexStaticBottom.size())) {
					double value=indexStaticBottom.get(indexBottom).bottom;
					if (y_min < value) break;
					double x = b.x + (indexStaticBottom.get(indexBottom).bottom - b.y - b.h) * b.vx / b.vy;
					if (!((indexStaticBottom.get(indexBottom).right < x - b.w) || (indexStaticBottom.get(indexBottom).left > x + b.w))) {
						if ((b.y + b.h <= indexStaticBottom.get(indexBottom).bottom) && (b.pathY(indexStaticBottom.get(indexBottom).bottom - b.y - b.h) < path_min)) {
							y_min = value;
							path_min = b.pathY(indexStaticBottom.get(indexBottom).bottom - b.y - b.h);
							new_y = y_min - b.h;
							new_x = x;
							horizontal_flip = true;
							vertical_flip = false;
							break;
						}
					}
					indexBottom++;
				}
				
				// left indexed collision search
				int indexRight = binarySearchRight(b.x - b.w);
				while ((indexRight >= 0) && (indexRight < indexStaticRight.size())) {
					double value=indexStaticRight.get(indexRight).right;
					if (x_max > value) break;
					double y = b.y - (b.x - b.w-indexStaticRight.get(indexRight).right) * b.vy / b.vx;
					if (!((indexStaticRight.get(indexRight).top < y - b.h) || (indexStaticRight.get(indexRight).bottom > y + b.h))) {
						if ((b.x - b.w >= indexStaticRight.get(indexRight).right) && (b.pathX(b.x-b.w-indexStaticRight.get(indexRight).right) < path_min)) {
							x_max = value;
							path_min=b.pathX(b.x-b.w-indexStaticRight.get(indexRight).right);
							new_x = x_max + b.w;
							new_y = y;
							horizontal_flip = false;
							vertical_flip = true;
							break;
						}
					}
					indexRight++;
				}					
				
				/* top left not indexed collisions
				for (StaticBar bar : staticBars) {
					double x = b.x + (bar.bottom - b.y - b.h) * b.vx / b.vy;
					
					// top
					if (!((bar.right < x - b.w) || (bar.left > x + b.w))) {
						if ((b.y + b.h <= bar.bottom) && (b.pathY(bar.bottom-b.y-b.h) < path_min)) {
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
						if ((b.x - b.w >= bar.right) && (b.pathX(b.x-b.w-bar.right) < path_min)) {
							x_max = bar.right;
							path_min=b.pathX(b.x-b.w-bar.right);
							new_x = x_max + b.w;
							new_y = y;
							horizontal_flip = false;
							vertical_flip = true;
						}
					}
				}
				*/
				collided = horizontal_flip || vertical_flip;
				if (collided) {
					b.x = new_x;
					b.y = new_y;
					if (horizontal_flip) b.collide(b.vx, -b.vy);
					if (vertical_flip) b.collide(-b.vx, b.vy);
				}
			}

			
			//bottom, right
			if ((b.vy < 0) && (b.vx > 0) && (!collided)) {
				double new_x=0;
				double new_y=0;
				
				double y_max = b.y - b.h + b.vy;
				double x_min = b.x + b.w + b.vx;
				double path_min=b.path();

				boolean horizontal_flip = false;
				boolean vertical_flip = false;				

				// right indexed collision check
				int indexLeft = binarySearchLeft(b.x + b.w);
				while ((indexLeft >= 0) && (indexLeft < indexStaticLeft.size())) {
					double value=indexStaticLeft.get(indexLeft).left;
					if (x_min < value) break;
					double y = b.y + (indexStaticLeft.get(indexLeft).left - b.x - b.w) * b.vy / b.vx;
					
					if (!((indexStaticLeft.get(indexLeft).top < y - b.h) || (indexStaticLeft.get(indexLeft).bottom > y + b.h))) {
						if ((b.x + b.w <= indexStaticLeft.get(indexLeft).left) && (b.pathX(indexStaticLeft.get(indexLeft).left - b.x - b.w) < path_min)) {
							x_min = value;
							path_min = b.pathX(indexStaticLeft.get(indexLeft).left - b.x - b.w);
							new_x = x_min - b.w;
							new_y = y;
							horizontal_flip = false;
							vertical_flip = true;
						}
					}
					indexLeft++;
				}

				// bottom indexed collision search
				int indexTop = binarySearchTop(b.y - b.h);
				while ((indexTop >= 0) && (indexTop < indexStaticTop.size())) {
					double value=indexStaticTop.get(indexTop).top;
					if (y_max > value) break;
					double x = b.x - (b.y - b.h-indexStaticTop.get(indexTop).top) * b.vx / b.vy;
					if (!((indexStaticTop.get(indexTop).right < x - b.w) || (indexStaticTop.get(indexTop).left > x + b.w))) {
						if ((b.y - b.h >= indexStaticTop.get(indexTop).top) && (b.pathY(b.y - b.h-indexStaticTop.get(indexTop).top) < path_min)) {
							y_max = value;
							path_min=b.pathY(b.y - b.h-indexStaticTop.get(indexTop).top);
							new_y = y_max + b.h;
							new_x = x;
							horizontal_flip = true;
							vertical_flip = false;
							break;
						}
					}
					indexTop++;
				}

				/*
				for (StaticBar bar : staticBars) {

					// bottom
					double x = b.x - (b.y - b.h-bar.top) * b.vx / b.vy;
					if (!((bar.right < x - b.w) || (bar.left > x + b.w))) {
						if ((b.y - b.h >= bar.top) && (b.pathY(b.y - b.h-bar.top) < path_min)) {
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
						if ((b.x + b.w <= bar.left) && (b.pathX(bar.left-b.x-b.w) < path_min)) {
							x_min = bar.left;
							path_min=b.pathX(bar.left-b.x-b.w);
							new_x = x_min - b.w;
							new_y = y;
							horizontal_flip = false;
							vertical_flip = true;
						}
					}
				}*/
				collided = horizontal_flip || vertical_flip;
				if (collided) {
					b.x = new_x;
					b.y = new_y;
					if (horizontal_flip) b.collide(b.vx, -b.vy);
					if (vertical_flip) b.collide(-b.vx, b.vy);
				}
			}
			
			if(!collided){
				b.x += b.vx;
				b.y += b.vy;
			}
			
			/*
			if((b.y+b.h>1024-30*2)&&(b.y+b.h<1024)){
				PlayN.log().debug("collided="+collided);
				PlayN.log().debug("Error speed="+b.vy+" by="+b.y+" bh="+b.h+" top="+(b.y+b.h)+" bound="+(1024-30*2));
			}
			if(b.y-b.h<30){
				PlayN.log().debug("collided="+collided);
				PlayN.log().debug("Error speed="+b.vy+" by="+b.y+" bh="+b.h+" diff="+(b.y-b.h));
			}*/

		}
	}

	/*
	private void lowVelocityBoundCheck(DynamicBar bar){
		if (Math.abs(bar.vx) < MIN_VELOCITY) bar.vx = MIN_VELOCITY * Math.signum(bar.vx);
		if (Math.abs(bar.vy) < MIN_VELOCITY) bar.vy = MIN_VELOCITY * Math.signum(bar.vy);
	}*/
	

	private void highVelocityBoundCheck(DynamicBar bar){
		if (Math.abs(bar.vx) > MAX_VELOCITY) bar.vx = MAX_VELOCITY * Math.signum(bar.vx);
		if (Math.abs(bar.vy) > MAX_VELOCITY) bar.vy = MAX_VELOCITY * Math.signum(bar.vy);
	}

	
	public void add(StaticBar bar){
		staticBars.add(bar);
		lastStaticChangeN++;
	}

	public void add(DynamicBar bar){
		dynamicMaxSize = Math.max(bar.w * 2 + 2, dynamicMaxSize);
		dynamicMaxSize = Math.max(bar.h * 2 + 2, dynamicMaxSize);
		dynamicMaxSize = Math.floor(dynamicMaxSize);
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
			render.bar((int)(bar.left), (int)(bar.bottom), (int)(bar.right-bar.left), (int)(bar.top-bar.bottom), BarType.STATIC);
		}
	}
	
	
	// functions for indexes
	public void binaryIndexTop(){
		indexStaticTop = new ArrayList<StaticBar>(staticBars);
		Collections.sort(indexStaticTop, topComparator);
		indexStaticPairsTop = new ArrayList<StaticBarPair>();
		int l = 1;
		int h = 0;
		for (@SuppressWarnings("unused") StaticBar bar : indexStaticTop) {
			if (l < indexStaticTop.size()) {
				if (indexStaticTop.get(l).top < indexStaticTop.get(h).top) {
					indexStaticPairsTop.add(new StaticBarPair(l, h, indexStaticTop.get(l).top, indexStaticTop.get(h).top));
					h = l;
				}
				l++;
			}
		}
	}
	
	public void binaryIndexRight(){
		indexStaticRight = new ArrayList<StaticBar>(staticBars);
		Collections.sort(indexStaticRight, rightComparator);
		indexStaticPairsRight = new ArrayList<StaticBarPair>();
		int l = 1;
		int h = 0;
		for (@SuppressWarnings("unused") StaticBar bar : indexStaticRight) {
			if (l < indexStaticRight.size()) {
				if (indexStaticRight.get(l).right < indexStaticRight.get(h).right) {
					indexStaticPairsRight.add(new StaticBarPair(l, h, indexStaticRight.get(l).right, indexStaticRight.get(h).right));
					h = l;
				}
				l++;
			}
		}
	}
	
	public void binaryIndexBottom(){
		indexStaticBottom = new ArrayList<StaticBar>(staticBars);
		Collections.sort(indexStaticBottom, bottomComparator);
		indexStaticPairsBottom = new ArrayList<StaticBarPair>();
		int l=0;
		int h=1;
		for (@SuppressWarnings("unused") StaticBar bar : indexStaticBottom) {
			if (h < indexStaticBottom.size()) {
				if (indexStaticBottom.get(h).bottom > indexStaticBottom.get(l).bottom) {
					indexStaticPairsBottom.add(new StaticBarPair(l, h, indexStaticBottom.get(l).bottom, indexStaticBottom.get(h).bottom));
					l = h;
				}
				h++;
			}
		}
	}

	public void binaryIndexLeft(){
		indexStaticLeft = new ArrayList<StaticBar>(staticBars);
		Collections.sort(indexStaticLeft, leftComparator);
		indexStaticPairsLeft = new ArrayList<StaticBarPair>();
		int l=0;
		int h=1;
		for (@SuppressWarnings("unused") StaticBar bar : indexStaticLeft) {
			if (h < indexStaticLeft.size()) {
				if (indexStaticLeft.get(h).left > indexStaticLeft.get(l).left) {
					indexStaticPairsLeft.add(new StaticBarPair(l, h, indexStaticLeft.get(l).left, indexStaticLeft.get(h).left));
					l = h;
				}
				h++;
			}
		}
	}

	public int binarySearchTop(double value){
		if (indexStaticPairsTop.isEmpty()){
			if(indexStaticTop.isEmpty()) return -1;
			else 
				if(indexStaticTop.get(0).top>value) return -1;
		} 
		int i = Collections.binarySearch(indexStaticPairsTop, new StaticBarPair(value), new Comparator<StaticBarPair>(){
			@Override
			public int compare(StaticBarPair pair, StaticBarPair edge) {
				return pair.bH <= edge.value ? 1 : pair.bL > edge.value ? -1 : 0;
			}
		});
		if (i == -1) return 0;
		if (i < -1) {
			if (indexStaticPairsTop.get(indexStaticPairsTop.size() - 1).bH == value) return indexStaticPairsTop.get(indexStaticPairsTop.size() - 1).H;
			return -1;
		}
		if (indexStaticPairsTop.get(i).bH == value) return indexStaticPairsTop.get(i).H;
		return indexStaticPairsTop.get(i).L;
	}

	public int binarySearchRight(double value){
		if (indexStaticPairsRight.isEmpty()){
			if(indexStaticRight.isEmpty()) return -1;
			else 
				if(indexStaticRight.get(0).right>value) return -1;
		} 
		int i = Collections.binarySearch(indexStaticPairsRight, new StaticBarPair(value), new Comparator<StaticBarPair>(){
			@Override
			public int compare(StaticBarPair pair, StaticBarPair edge) {
				return pair.bH <= edge.value ? 1 : pair.bL > edge.value ? -1 : 0;
			}
		});
		if (i == -1) return 0;
		if (i < -1) {
			if (indexStaticPairsRight.get(indexStaticPairsRight.size() - 1).bH == value) return indexStaticPairsRight.get(indexStaticPairsRight.size() - 1).H;
			return -1;
		}
		if (indexStaticPairsRight.get(i).bH == value) return indexStaticPairsRight.get(i).H;
		return indexStaticPairsRight.get(i).L;
	}
	
	public int binarySearchBottom(double value){
		if (indexStaticPairsBottom.isEmpty()){
			if(indexStaticBottom.isEmpty()) return -1;
			else 
				if(indexStaticBottom.get(0).bottom<value) return -1;
		} 
		int i = Collections.binarySearch(indexStaticPairsBottom, new StaticBarPair(value), new Comparator<StaticBarPair>(){
			@Override
			public int compare(StaticBarPair pair, StaticBarPair edge) {
				return pair.bH <= edge.value ? -1 : pair.bL > edge.value ? 1 : 0;
			}
		});
		if (i == -1) return 0;
		if (i < -1) {
			if (indexStaticPairsBottom.get(indexStaticPairsBottom.size() - 1).bH == value) return indexStaticPairsBottom.get(indexStaticPairsBottom.size() - 1).H;
			return -1;
		}
		if (indexStaticPairsBottom.get(i).bL == value) return indexStaticPairsBottom.get(i).L;
		return indexStaticPairsBottom.get(i).H;
	}
	
	public int binarySearchLeft(double value){
		if (indexStaticPairsLeft.isEmpty()){
			if(indexStaticLeft.isEmpty()) return -1;
			else 
				if(indexStaticLeft.get(0).left<value) return -1;
		} 
		int i = Collections.binarySearch(indexStaticPairsLeft, new StaticBarPair(value), new Comparator<StaticBarPair>(){
			@Override
			public int compare(StaticBarPair pair, StaticBarPair edge) {
				return pair.bH <= edge.value ? -1 : pair.bL > edge.value ? 1 : 0;
			}
		});
		if (i == -1) return 0;
		if (i < -1) {
			if (indexStaticPairsLeft.get(indexStaticPairsLeft.size() - 1).bH == value) return indexStaticPairsLeft.get(indexStaticPairsLeft.size() - 1).H;
			return -1;
		}
		if (indexStaticPairsLeft.get(i).bL == value) return indexStaticPairsLeft.get(i).L;
		return indexStaticPairsLeft.get(i).H;
	}
}
