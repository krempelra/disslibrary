package de.rkrempel.diss.core.harvester;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

public class ContextHarvester_Runner<T> implements Callable<List<Object[]>> {
	//Exclusive Access
	private final List<T> links; 
	private T opc;
	
	
	//private  Set<T> containedSources; 
	private final Set<T> sourcenodes;
	private final List<T> otherPossibleContexts;
	
	private final List<T> thingsofInterest;
	private  float weightLinkTyp2;
	private  float weightLinkTyp1;
	private List<Object[]> edges;
	ContextHarvester_Runner(T opc, 
			List<T> links,
			Set<T> sourcenodes, 
			List<T> otherPossibleContexts,
			List<T> thingsofInterest,
			float weightLinkTyp2,
			float weightLinkTyp1
			){
		//Realy Only Here Accesssed
		this.links = links;
		this.opc = opc;
		this.sourcenodes = sourcenodes;
		this.otherPossibleContexts= otherPossibleContexts;
		//Shared
		this.thingsofInterest = thingsofInterest;
		this.weightLinkTyp2 = weightLinkTyp2;
		this.weightLinkTyp1 = weightLinkTyp1;
		this.edges = new LinkedList<Object[]>();
	}
	
	
	


	@Override
	public List<Object[]> call() throws Exception {
		
		Set<T> containedSources = new HashSet<T> (sourcenodes);
		containedSources.retainAll(links);
		//Links To Other Context
		links.retainAll(otherPossibleContexts);
		
		
		int indexOfOpc = thingsofInterest.indexOf(opc); 
		// For All Retrived Links
		for (T link : links) {


			int temp = thingsofInterest.indexOf(link);
			//NO SELF LOOPS
			if(indexOfOpc == temp)
				continue;

			Object[] in = new Object[4];
			Long one = new Long(indexOfOpc);  
			Long two = new Long(temp); 
			if(one == null || two == null)
				continue;
				
			if(one < two){
				in[0]=one;
				in[1]=two;
			}else{
				in[0]=two;
				in[1]=one;
			}

					
			in[2]= 2;
			in[3]= new Float(weightLinkTyp2);

			edges.add(in);

		
		}
		
		
		
		for (T link : containedSources) {
			

			int temp = thingsofInterest.indexOf(link);
			
				
			Object[] in = new Object[4];
			Long one = new Long(indexOfOpc);  
			Long two = new Long(temp); 
			if(one == null || two == null)
				continue;
			
			if(one < two){
				in[0]=one;
				in[1]=two;
			}else{
				in[0]=two;
				in[1]=one;
			}
				


			in[2]= 1;
			in[3]= new Float(weightLinkTyp1);

			edges.add(in);
		
		}
		return edges;
	}
}