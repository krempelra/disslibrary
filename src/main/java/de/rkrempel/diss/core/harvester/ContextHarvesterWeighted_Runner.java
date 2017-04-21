package de.rkrempel.diss.core.harvester;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
/**
 * 
 * @author rasmus
 * A bit like the Other Runner Exept that the Source Node Edges can be Ranked
 * @param <T>
 */
public class ContextHarvesterWeighted_Runner<T> implements Callable<List<Object[]>> {
	//Exclusive Access
	private final List<T> links; 
	private T opc;
	
	
	//private  Set<T> containedSources; 
	private final Set<T> sourcenodes;
	private final List<T> otherPossibleContexts;
	
	private final List<T> thingsofInterest;
	private  float weightLinkTyp2;
	Map<T,Map<T,Number>> weightLink1Infos;
	private List<Object[]> edges;
	boolean weightEdgesbyOutDegree;
	
	/**
	 * 
	 * @param opc
	 * @param links
	 * @param sourcenodes
	 * @param otherPossibleContexts
	 * @param thingsofInterest
	 * @param weightLinkTyp2
	 * @param weightLink1Infos
	 * @param weight Context by Number of edges 1/number of Edges weightEdgesbyoutdegree
	 */
	ContextHarvesterWeighted_Runner(T opc, 
			List<T> links,
			Set<T> sourcenodes, 
			List<T> otherPossibleContexts,
			List<T> thingsofInterest,
			float weightLinkTyp2,
			Map<T,Map<T,Number>> weightLink1Infos,boolean weightEdgesbyOutDegree
			){
		//Realy Only Here Accesssed
		this.links = links;
		this.opc = opc;
		this.sourcenodes = sourcenodes;
		this.otherPossibleContexts= otherPossibleContexts;
		//Shared
		this.thingsofInterest = thingsofInterest;
		this.weightLinkTyp2 = weightLinkTyp2;
		this.weightLink1Infos = weightLink1Infos;
		this.edges = new LinkedList<Object[]>();
		this.weightEdgesbyOutDegree= weightEdgesbyOutDegree;
	}
	
	
	


	@Override
	public List<Object[]> call() throws Exception {
		
		Set<T> containedSources = new HashSet<T> (sourcenodes);
		//containedSources.retainAll(links);
		//Links To Other Context
		int reallinks = links.size();
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
			//Seltene Links kommen Häufiger Das +1 bedeutet das alles andere oben drauf kommt!
			//rare links are more commen therefor +1 everything else on top
			if(weightEdgesbyOutDegree){
				float negweight = (new Float(reallinks) / 100f);
				Float weight;
				float posweight = links.size()/2f;
				if(weightLinkTyp2+posweight <= negweight)
					weight =new Float(1  );
				else
					weight =new Float(weightLinkTyp2+posweight - negweight);
				in[3]= weight;
			}
			else
				in[3]= new Float(weightLinkTyp2);
			edges.add(in);

		
		}
		
		Set<T> SourceNodes = weightLink1Infos.keySet();
		
		for (T link : SourceNodes) {
			

			int temp = thingsofInterest.indexOf(link);
			
			Map<T, Number> sourcenodeweights = weightLink1Infos.get(link);
			Number tempWeight = sourcenodeweights.get(opc);
			
			if(tempWeight== null)
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
				


			in[2]= 1;
			
			//Ausnahme Direkt von sourcenodes Verlinte sachen Stärker an den Sourcenode ziehen.
			//Dafür Wird der Weight Ausgetauscht bei 2 und 1 BEi einem Doppellinks wird Stärker Gezogen
			//Exception for Links linked by the sourcenodes, those things are stronger attracted to the sourcenode
			//Exchange link wheight double links are far more attraction
			Float SourceWeight ;
			if(tempWeight.floatValue() == 1f)
				SourceWeight= 10f;
			else if(tempWeight.floatValue() == 2f)
				SourceWeight= 4f;
			else if(tempWeight.floatValue() == 3f)
				SourceWeight=  20f;
			else{
				System.out.println("Unusual beahvior");
				SourceWeight=  0f;
			}
			in[3]= new Float(SourceWeight);

			edges.add(in);
		
		}
		return edges;
	}
}