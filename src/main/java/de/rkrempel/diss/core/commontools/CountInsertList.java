package de.rkrempel.diss.core.commontools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;



public class CountInsertList<T> {
	
	Map<T,Integer> thing;
	
	public CountInsertList() {
		thing = new HashMap<T,Integer>();
	}
	

/**
 * Insert an Element If it is Allready in there then its counted up
 * @param toInsert The Thing to inser/count
 */
public void insert(T toInsert){
	
	Integer tmp = thing.get(toInsert); 
	if(tmp == null){
		thing.put(toInsert, new Integer(1));
	}else{

		tmp++;
		thing.put(toInsert, tmp);
	}
}
/**
 * Filter with minimal Occurence
 * @param min The minimal occurence of the Elemnts that will be Returned
 * @return The Things that are Minimum Occuring the min times When OccurenceofElement >=min
 */
public List<T> getMinCount(int min){
	
	List<T> out = new ArrayList<T>();
	Set<T> keys = thing.keySet();
	for (T object : keys) {
		
		if(thing.get(object).intValue()>= min){
			out.add(object);
		}
		
	}
	return out;
}
/**
 * Minimum and Maxmum Filter Combined
 * @param min Minimum Filter
 * @param max Maximum Filter
 * @return
 */
public List<T> getMinMaxCount(int min,int max){
	
	List<T> out = new ArrayList<T>();
	Set<T> keys = thing.keySet();
	for (T object : keys) {
		int count = thing.get(object).intValue();
		if(count>= min && count <= max)
			out.add(object);
		
		
	}
	return out;
}
/**
 * Removes Elements that occure less than min times from the List to save space 
 * @param min Purge everything less than
 * @return Number of Things Purged
 */
public int purgeLessThanCount(int min){
	
	int count=0;
	Set<T> keys = thing.keySet();
	HashSet<T> remove = new HashSet<T>();
	for (T object : keys) {
		
		if(thing.get(object).intValue()< min){
			remove.add(object);
			
		}
		
	}
	count = remove.size();
	for (T t : remove) {
		thing.remove(t);
	}
	
	return count;
}
/**
 * Removes Elements that occure less than min times from the List to save space 
 * @param min Purge everything less than
 * @param max Purge everything more than
 * @return Number of Things Purged
 */
public int purgeLessAndOverCount(int min,int max){
	
	int count=0;
	Set<T> keys = thing.keySet();
	HashSet<T> remove = new HashSet<T>();
	for (T object : keys) {
		Integer temp=thing.get(object);
		if(temp.intValue()< min ||temp.intValue()>max){
			remove.add(object);
			
		}
		
	}

	
	count = remove.size();
	for (T t : remove) {
		thing.remove(t);
	}
	
	return count;
}
/**
 * Returns the times key has been Inserted
 * @param key
 * @return
 */
public Integer getOccurenceOf(T key){
	return thing.get(key);
	
	
}


/**
 * Avarage Occurence of the Elements
 * @return Avarage Occurence of the Elements
 */
public float avarageOccurence(){
	
	float sum = 0;
	Collection<Integer> occs = thing.values();
	
	
	for (Integer integer : occs) {
		
		sum += integer.floatValue();
		
	}
	
	
	return sum/occs.size();
	
	
}

public int size(){
	return thing.size();
	
}

/**
 * Get the total Number insertion Attemps
 * @return total Number insertion Attemps
 */
public int Allcounts(){
	int out = 0;
	Collection<Integer> temp = thing.values();
	for (Integer integer : temp) {
		out +=integer;
	}
	return out;
}

public Set<T> getKeys(){
	
	return thing.keySet();
	
}

}
