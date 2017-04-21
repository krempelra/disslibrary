package de.rkrempel.diss.core.commontools;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Count Inset List
 * @param <T> The Things that should be Inserted into this class and Counted
 */
public class ThingCountOcc<T> {
	
	private Map<T,Integer> stuff;
	
	public ThingCountOcc() {
		stuff = new HashMap<T,Integer>();
	}
	public void insert(Collection<T> in){
		for (Iterator<T> iterator = in.iterator(); iterator.hasNext();) {
			T t =  iterator.next();
			insert(t);	
		}
		
	}
	public void insert(T in){
		
		Integer temp = stuff.get(in);
		
		if(temp == null){
			temp = new Integer(1);
			
		}else{
			temp =temp+1;
			
			
		}
		stuff.put(in,temp);
		
	}
	public Map<T, Integer> getStuff() {
		return stuff;
	}

}
