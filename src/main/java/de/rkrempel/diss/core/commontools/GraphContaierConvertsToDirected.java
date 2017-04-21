package de.rkrempel.diss.core.commontools;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Convert a Graph container to an dicrected Graph
 */
public class GraphContaierConvertsToDirected extends GraphContainer {

	public GraphContaierConvertsToDirected(List<Object[]> nodes,
			List<Object[]> edges) {
		super(nodes, edges);

		Convert();
	}

	private void Convert() {
		Map<Long,Integer> degree =  new HashMap<Long,Integer>(); 		
		int MaxDegree = 0;
		Set<Long> Degree1 = new HashSet<Long>();
		
		for (int i =0 ; i<edges.size();i++) {
			Object[] temp = edges.get(i);
			Long one =  (Long)temp[0];
			Long two =  (Long)temp[1];
			
			
			Integer eins = degree.get(one);
			Integer zwei = degree.get(two);
			if(eins == null){
				degree.put(one, new Integer(1));
				Degree1.add(one);
			}else{
				if(eins ==1 )
					Degree1.remove(one);
				eins ++;
				degree.put(one, eins);
			}
			if(eins >  MaxDegree )
				MaxDegree = eins;
			if(zwei == null){
				degree.put(two, new Integer(1));
				Degree1.add(two);
			}else{
				if(zwei ==1 )
					Degree1.remove(two);
				zwei++;
				degree.put(two, zwei);						
			
			
			}

			if(zwei >  MaxDegree )
				MaxDegree = eins;
			
		}
		

		
		
	}
	
	
	
	

}
