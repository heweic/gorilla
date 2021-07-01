package org.myframe.gorilla.utils;

import java.util.Collection;

public class CollectionUtil {
	
	public static boolean isEmpty(Collection<?> collection) {
		return collection == null || collection.size() == 0;
	}
	
	
}
