package de.rkrempel.diss.core.commontools;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Class to Analyse Attributes from Graph
 */
public class GraphAnalyser {

	/**
	 * Analise the Connected Components of an Edge List
	 * @param in List of Object Arrays representing the Edge list. 0 ist source 1 is target
	 * @return GraphAnalyserResult
	 */
	public static GraphAnalyserResult EdgeListConnectedComponentsAnalysis(List<Object[]> in){
		//Takes the Complete Size of 
		List<Set> CompleteSets= new ArrayList<Set>();
		
		List<Float> CompleteSetsAvgDeg= new ArrayList<Float>();
		
		LinkedList<Object[]> tempin = new LinkedList<Object[]>();
		
		Set<Object> nodes = new HashSet<Object>(); 
		//Create Unique NodeList
		for (Object[] objects : in) {
			nodes.add(objects[0]);
			nodes.add(objects[1]);
		}
		//List Of All nodes
		tempin.addAll(in);
		
		Set<Object> allKnown = new HashSet<Object>();
		
		
		
		for (Object node : nodes) {
			
			Set<Object> set = new HashSet<Object>();
			Float degcount=0.0f;
			
			//Check if its allready known
			
			if(allKnown.size()== nodes.size() || allKnown.contains(node) )
				continue;
			
			set.add(node);
			allKnown.add(node);
			List<Object> toprocess= new LinkedList<Object>();
			toprocess.add(node);
			
			while(!toprocess.isEmpty()){
				
				List<Object> newtoprocess= new LinkedList<Object>();
				
				for (Object start : toprocess) {
					for (Iterator<Object[]> iterator = tempin.iterator(); iterator
							.hasNext();) {
						
						Object[] objectes =  iterator.next();
					
						Object thing=null;
						
						if(start.equals(objectes[0])){
							thing = objectes[1];
						}
						else if(start.equals(objectes[1])){
							thing = objectes[0];
						}
						
						if(thing == null)
							continue;
						else
						//Add Two because all Edges are just once visited so count for each adjency.
							degcount+= 2.0f;
						
						if(!set.contains(thing)){
							newtoprocess.add(thing);
							set.add(thing);
							allKnown.add(thing);
						}
						//Reduce List of Nodes for Faster Processing
						iterator.remove();
						
					}
					
					
				}
				
				toprocess = newtoprocess;
			}
			if(set.size()>0){
				CompleteSets.add(set);
				CompleteSetsAvgDeg.add(degcount /(new Float(set.size())) );
			}
		}

		//create Largest Components
		
		int size= 5;
		Integer[] largestComponents=  new Integer[size];
		Float[] largestComponentsAvgDeg=  new Float[size];
		for(int i=0;i<size;i++){
			largestComponents[i]=0;
			largestComponentsAvgDeg[i]= 0.0f;
			
		}
		for (int a =0;a< CompleteSets.size();a++) {
			
			for(int i=0;i<size;i++){
				if(CompleteSets.get(a).size()< largestComponents[size-1])
					continue;
				
				if(CompleteSets.get(a).size()>largestComponents[i]){
					largestComponents[i]=CompleteSets.get(a).size();
					largestComponentsAvgDeg[i]= CompleteSetsAvgDeg.get(a);
					break;
				}
				
			}
			
			
			
		}
		GraphAnalyserResult  result = new GraphAnalyserResult();
		
		result.numComponents= CompleteSets.size();
		result.numConnecetedNodes = allKnown.size();
		result.largestComponentsAvgDeg= largestComponentsAvgDeg;
		result.largestComponents= largestComponents;
		
		return result;
		
	}
	
	


	
	
	
}
