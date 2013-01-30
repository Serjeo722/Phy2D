package Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Space {
	
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
	
	private ArrayList<StaticBar> staticBars = new ArrayList<StaticBar>();
	
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

	public Space(){
		
		
		for(int i=1;i<10;i++){
			int v= (int)(10*Math.random());
			staticBars.add(new StaticBar(v,v+10,2,2));
		}
		
		
		binaryIndexTop();
		binaryIndexRight();
		binaryIndexBottom();
		binaryIndexLeft();
		
		
		int i=0;
		
		for(StaticBar bar:indexStaticRight){
			System.out.println(i+") right="+bar.right);
			i++;
		}


		for(StaticBarPair sbp:indexStaticPairsRight)
			System.out.println("right High["+sbp.H+"]="+sbp.bH+" Low["+sbp.L+"]="+sbp.bL);


		
		for(double j=-3;j<13;j+=0.5)
		System.out.println("j="+j+" searched="+binarySearchRight(j));

		
		double vx = 0;
		double leftBound = 5;
		int indexRight = binarySearchRight(leftBound);
		while ((indexRight >= 0) && (indexRight < indexStaticRight.size())) {
			double value=indexStaticRight.get(indexRight).right;
			if (leftBound + vx < value) break;
			System.out.println("5 rights=" + indexRight);
			indexRight++;
		}
	}

	
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
