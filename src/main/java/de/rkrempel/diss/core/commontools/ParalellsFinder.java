package de.rkrempel.diss.core.commontools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Finds Paralells Structures in Graphs
 */
public class ParalellsFinder {

	/**
	 * Get Paralell Part of Two Edge Lists
	 * @param a Edgelist 1
	 * @param b Edgelist 2
	 * @return Edgelist Union
	 */
	public static List<Object[]> getParalells(List<Object[]> a,List<Object[]> b){
		List<Object[]> c = new ArrayList<Object[]>(a.size()+b.size());
		
		List<Object[]>  out= new ArrayList<Object[]>();
		
		c.addAll(a);
		c.addAll(b);
		
		
		SortTableByNthField sorter = new SortTableByNthField(0);
		SortTableByNthField sorter1 = new SortTableByNthField(1);
		Collections.sort(c, sorter1);
		Collections.sort(c, sorter);
		Object[] last = new Long[2];
		last[0] = new Long(0);
		last[1] = new Long(0);
		
		for (Iterator<Object[]> iterator = c.iterator(); iterator.hasNext();) {
			Object[] objects = (Object[]) iterator.next();
			
			if(last[0].equals(objects[0])&& last[1].equals(objects[1]))
				out.add(last);
			last = objects;
			
		}
		return out;
		
	}

	/**
	 * Get number of Paralell Edges in Two Edge Lists
	 * @param a Edgelist 1
	 * @param b Edgelist 2
	 * @return Number of Paralell Edges
	 */
	public static Integer getParalellcount(List<Object[]> a,List<Object[]> b){
		List<Object[]> c = new ArrayList<Object[]>(a.size()+b.size());
		
		Integer out= 0;
		
		c.addAll(a);
		c.addAll(b);
		
		
		SortTableByNthField sorter = new SortTableByNthField(0);
		SortTableByNthField sorter1 = new SortTableByNthField(1);
		Collections.sort(c, sorter1);
		Collections.sort(c, sorter);
		Object[] last = new Long[2];
		last[0] = new Long(0);
		last[1] = new Long(0);
		
		for (Iterator<Object[]> iterator = c.iterator(); iterator.hasNext();) {
			Object[] objects = (Object[]) iterator.next();
			
			if(last[0].equals(objects[0])&& last[1].equals(objects[1]))
				out++;
			last = objects;
			
		}

		return out;
		
	}
	

	public static Map<String,Object> getParalellStatistics(List<Object[]> a,List<Object[]> b){
		List<Object[]> c = new ArrayList<Object[]>(a.size()+b.size());
		
		Map<String,Object> out= new HashMap<String,Object>();
		int Parallels=0;
		c.addAll(a);
		c.addAll(b);
		
		
		SortTableByNthField sorter = new SortTableByNthField(0);
		SortTableByNthField sorter1 = new SortTableByNthField(1);
		Collections.sort(c, sorter1);
		Collections.sort(c, sorter);
		Object[] last = new Long[2];
		last[0] = new Long(0);
		last[1] = new Long(0);
		
		for (Iterator<Object[]> iterator = c.iterator(); iterator.hasNext();) {
			Object[] objects = (Object[]) iterator.next();
			
			if(last[0].equals(objects[0])&& last[1].equals(objects[1]))
				Parallels++;
			last = objects;
			
		}
		
		
			
		return out;
		
	}
	

	public static Map<String,Object> getParalellStatistics(List<List<Object[]>> a){
		
		
		
		Map<String,Object> out= new HashMap<String,Object>();
		
		for(int i =0 ;i< a.size();i++)
			for(int j =i ;j< a.size();j++)
				if(i!=j)
					getParalellStatistics(a.get(i),a.get(j));

		
			
		return out;
		
	}

	
	
}
