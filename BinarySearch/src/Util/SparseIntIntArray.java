package Util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

public class SparseIntIntArray<V> {

	private static final int BITS_MASK = 32;
	
	private SortedMap<Long,ArrayList<V>> map=new TreeMap<Long,ArrayList<V>>();

	public void put(int x,int y, V value){
		long key = ((long)(x) << BITS_MASK) + y;

		if(map.containsKey(key)){
			ArrayList<V> array = map.get(key);
			array.add(value);
		}else{
			ArrayList<V> array = new ArrayList<V>();
			array.add(value);
			map.put(key, array);
		}
	}

	private Iterator2d<ArrayList<V>> iter = new SparseIntIntArrayIterator(map);
	
	public Iterator2d<ArrayList<V>> iterator2d(){
		return iter;
	}
	
	private class SparseIntIntArrayIterator implements Iterator2d<ArrayList<V>>{
		private Iterator<Entry<Long,ArrayList<V>>> iterator;
		private Entry<Long,ArrayList<V>> entry;
		private SortedMap<Long,ArrayList<V>> map;
		private boolean started=false;
		
		public SparseIntIntArrayIterator(SortedMap<Long,ArrayList<V>> map){
			this.map = map;
			iterator = map.entrySet().iterator();
		}
		
		
		@Override
		public boolean hasNext() {
			if(!started){
				iterator = map.entrySet().iterator();
				started=true;
			}
			return iterator.hasNext();
		}

		@Override
		public ArrayList<V> next() {
			entry = iterator.next();
			return entry.getValue();
		}

		@Override
		public void remove() {
			iterator.remove();
		}

		@Override
		public int getX() {
			return (int) (entry.getKey()>>BITS_MASK);
		}

		@Override
		public int getY() {
			return (int)(entry.getKey()-((long)getX()<<BITS_MASK));
		}

		@Override
		public long getKey() {
			return entry.getKey();
		}
	}
}