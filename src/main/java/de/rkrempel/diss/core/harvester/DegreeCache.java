package de.rkrempel.diss.core.harvester;

import java.util.HashMap;
import java.util.Map;
/**
 * 
 * @author rkrempel
 *
 * @param <T> This is The Type The Degree cache should reference to
 */
public class DegreeCache<T> {
	private  Map<T,Integer> degreesCacheIn=null;
	private  Map<T,Integer> degreesCacheOut=null;
	private  Map<T,Integer> degreesCacheAll=null;
	
	public DegreeCache(){
		flushcaches();
		
	}
	
	public void flushcaches(){
		if(degreesCacheIn ==null)
			degreesCacheIn = new HashMap<T,Integer>();
		else
			degreesCacheIn.clear();
		
		if(degreesCacheOut ==null)
			degreesCacheOut = new HashMap<T,Integer>();
		else
			degreesCacheOut.clear();
		
		if(degreesCacheAll ==null)
			degreesCacheAll = new HashMap<T,Integer>();
		else
			degreesCacheAll.clear();
		
		
	}
	
	public void setAllDegree(T in,Integer number){
		degreesCacheAll.put(in, number);
		
		
	}
	public void setInDegree(T in,Integer number){
		
		degreesCacheIn.put(in, number);
		
	}
	public void setOutDegree(T in,Integer number){
		degreesCacheOut.put(in, number);
	
	
	}
	public void setDegree(T in,Integer number,int type){
		
		if(type ==1 )
			degreesCacheIn.put(in, number);
		else if(type==2)
			degreesCacheOut.put(in, number);
		else
			degreesCacheAll.put(in, number);
	}
	
	
	public Integer getAllDegree(T in){
		return degreesCacheAll.get(in);
	}
	
	public Integer getInDegree(T in){
		return degreesCacheIn.get(in);
	}
	public Integer getOutDegree(T in){
		return degreesCacheOut.get(in);
	}
	
}
