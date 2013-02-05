package Util;

import java.util.ArrayList;

public class Main {
	
	public static void main(String[] args) {
		//Space space = new Space();
		
		
		SparseIntIntArray<String> table = new SparseIntIntArray<String> ();
		
		for (int i = 0; i < 1000; i++) {
			int x=(int)(10*Math.random());
			int y=(int)(10*Math.random());
			table.put(x, y, "["+x+";"+y+"]");
		}
		
		Iterator2d<ArrayList<String>> i = table.iterator2d();
		
		while (i.hasNext()){
			
			ArrayList<String> strings=i.next();
			System.out.println("key="+i.getKey()+" x=" + i.getX() + " y=" + i.getY());
			
			for(String s:strings)
				System.out.println(" value=" + s);
			
		}
		
	}
}
