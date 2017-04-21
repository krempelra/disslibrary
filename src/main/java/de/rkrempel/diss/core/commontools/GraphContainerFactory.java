package de.rkrempel.diss.core.commontools;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This is a Factory for Graph Containers
 */
public class GraphContainerFactory {
	/**
	 * Convert a List of Named Entities and Converts it to a Graph Container Entity
	 * @param edges List of Arrays(size 2) which Represent Entitys.
	 * @return Returns Graph container with auto detected Nodes
	 */
	public static GraphContainer CreateGraphFromEdgeStrings(List<String[]> edges){

		
			List<Object[]> nodes = new ArrayList<Object[]>();
			
			Set<String> ids = new HashSet<String>();
			
			for (String[] strings : edges) {
				ids.add( strings[0]);
				ids.add( strings[1]);
			}
			List<String> ids1 = new ArrayList<String>(ids.size());
			ids1.addAll(ids);
			ids= null;
			
			
			for (int i =0 ; i< ids1.size() ;i++) {
				
				Object[] in = new Object[2];
				in[0] = new Long(i);
				in[1] = ids1.get(i);
				nodes.add(in);
					
			}
			
			List<Object[]> newedges = new ArrayList<Object[]>( edges.size());
			
			for (String[] strings : edges) {
				
				Long[] temp = new Long[2];
				
				int index = ids1.indexOf(strings[0]);
				
				if(index >=0)
					temp[0]= new Long(index);
				else
					continue;

				index =  ids1.indexOf(strings[1]);
				
				if(index >=0)
					temp[1] = new Long(index);
				else
					continue;
				
				newedges.add(temp);
			}
			GraphContainer gc= new GraphContainer(nodes,newedges);
			gc.autoInitializeLabels();
			gc.makeValid();
		return gc;
		}
	
	
}
