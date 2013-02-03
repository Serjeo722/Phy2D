package Util;

import java.util.ArrayList;
import java.util.Collections;

public class SparseIntIntArray<V> {

	private static final int BITS_MASK = 32;
	
	private ArrayList<Long> keys = new ArrayList <Long>();
	private ArrayList<ArrayList<V>> values = new ArrayList <ArrayList<V>>();

	public void put(int x,int y, V value){
		long key = ((long)(x) << BITS_MASK) + y;

		int idx = Collections.binarySearch(keys, key);
		if (idx >= 0) {
			keys.add(idx, key);
			values.get(idx).add(value);
		} else {
			int i=-idx - 1;
			keys.add(i, key);
			values.add(i,new ArrayList<V>());
			values.get(i).add(value);
		}
	}
	
	public Iterator2d<ArrayList<V>> iterator2d(){
		return iter;
	}
	
	private Iterator2d<ArrayList<V>> iter = new SparseIntIntArrayIterator(keys, values);
	
	private class SparseIntIntArrayIterator implements Iterator2d<ArrayList<V>>{
		private ArrayList<Long> keys;
		private ArrayList<ArrayList<V>> values;
		private int pos = 0;
		
		public SparseIntIntArrayIterator(ArrayList<Long> keys, ArrayList<ArrayList<V>> values){
			this.keys = keys;
			this.values = values;
		}
		
		@Override
		public boolean hasNext() {
			if(pos>values.size()-1) return false;
			return true;
		}

		@Override
		public ArrayList<V> next() {
			pos++;
			return values.get(pos - 1);
		}

		@Override
		public void remove() {
			pos--;
			keys.remove(pos);
			values.remove(pos);
		}

		@Override
		public int getX() {
			return (int) (keys.get(pos-1)>>BITS_MASK);
		}

		@Override
		public int getY() {
			return (int)(keys.get(pos-1)-((long)getX()<<BITS_MASK));
		}

		@Override
		public long getKey() {
			return keys.get(pos-1);
		}
	}
}