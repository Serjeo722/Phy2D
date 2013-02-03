package Util;

import java.util.Iterator;

public interface Iterator2d<V> extends Iterator<V> {
	int getX();
	int getY();
	long getKey();
}
