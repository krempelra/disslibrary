package de.rkrempel.diss.core.commontools;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GraphCombiner {

	/**
	 * Takes two Graphs and Merges their Edges.
	 * The Directed and Undirected Attribute gets taken from the Weaker form so a Directed and Undirected Graph Results in an Undirected Graph
	 */
	public static GraphContainer MergeGraphByID(GraphContainer a, GraphContainer b){

		List<NodeConvert> list = CombineNodesbyNthAttribute(a,b,0);
		
		List<Object[]> newEdgesA = list.get(0).convertEdges(a.getEdges());
		List<Object[]> newEdgesB = list.get(1).convertEdges(b.getEdges());
		
		GraphContainer out =  new GraphContainer(list.get(0).newNodes,newEdgesA);
		out.AppendEdges(newEdgesB);
		return out;
		
	}
	public static GraphContainer MergeGraphByNthField(GraphContainer a, GraphContainer b,int nthField){
		List<NodeConvert> list = CombineNodesbyNthAttribute(a,b,nthField);
		
		List<Object[]> newEdgesA = list.get(0).convertEdges(a.getEdges());
		List<Object[]> newEdgesB = list.get(1).convertEdges(b.getEdges());
		
		GraphContainer out =  new GraphContainer(list.get(0).newNodes,newEdgesA);
		out.AppendEdges(newEdgesB);
		return out;
	}
	
	public static List<NodeConvert> CombineNodesbyNthAttribute(GraphContainer a, GraphContainer b, int IndexOfAttribute ){
		List<Object[]> nodesA = a.getNodes();
		List<Object[]> nodesB = b.getNodes();
		List<Object[]> newNodes = new ArrayList<Object[]>(); 
		
		List<Class> atypes = a.getNodeTypes();
		List<Class> btypes = b.getNodeTypes();
		List<Class> comptype;
		List<Long[]> aIDSToNewIds = new ArrayList<Long[]>();
		List<Long[]> bIDSToNewIds= new ArrayList<Long[]>();
		
		int lastIndexofSameAttributes= 0; 
		
		if(atypes.size()< btypes.size())
			comptype = atypes;
		else
			comptype = btypes;
		
		for(int i = 0 ; i< comptype.size();i++){
			
			if(!atypes.get(i).equals(btypes.get(i)))
				lastIndexofSameAttributes = i-1;
			
		}
		
		Set<Object> ids = new HashSet<Object>();
		
		for(int i=0;i< nodesA.size();i++){
			Object[] temp = nodesA.get(i);
			if(!ids.contains(temp[IndexOfAttribute])){
				Object[] toInsert = new Object[lastIndexofSameAttributes+1];
				
				toInsert[0]= new Long(newNodes.size());
				
				Long[] lookup = new Long[2];
				lookup[0] = (Long)toInsert[0];
				lookup[1] = (Long)temp[0];
				
				aIDSToNewIds.add(lookup);
				for (int j = 1; j < (lastIndexofSameAttributes+1); j++) {
					toInsert[j]= temp[j];
					newNodes.add(toInsert);
				}
				
				newNodes.add(toInsert);
				
			}
			
			
		}
		
		for(int i=0;i< nodesB.size();i++){
			Object[] temp = nodesB.get(i);
			if(!ids.contains(temp[IndexOfAttribute])){
				Object[] toInsert = new Object[lastIndexofSameAttributes+1];
				toInsert[0]= new Long(newNodes.size());
				
				
				Long[] lookup = new Long[2];
				lookup[0] = (Long)toInsert[0];
				lookup[1] = (Long)temp[0];
				
				bIDSToNewIds.add(lookup);
				
				for (int j = 1; j < (lastIndexofSameAttributes+1); j++) {
					toInsert[j]= temp[j];
					newNodes.add(toInsert);
				}
				
				newNodes.add(toInsert);
				
			}
			
			
		}
		
		List<NodeConvert> out = new ArrayList<NodeConvert>();
		out.add( new NodeConvert(newNodes,aIDSToNewIds));
		out.add( new NodeConvert(newNodes,bIDSToNewIds));
		return out;
	}

	/**
	 * 
	 * @param toCheck List<Object[]> Haystack
	 * @param atThisPlace The Indices of the Object[] where to Search for the comparabale
	 * @param comparable The Thing that should be Compared
	 * @return -1 if its not there elsewise the Index of the Searched Object
	 */
	public static int containsThisObjectatthisPlace(List<Object[]> toCheck,int atThisPlace,Object comparable){
		
		for (int i = 0; i < toCheck.size(); i++) {
			Object[] temp = toCheck.get(i);
			
			if(temp[atThisPlace].equals(comparable))
				return i;
		}
		
		return -1;
	}
	public List<Object[]> undirectEdges(List<Object[]> in){
		List<Object[]>out = new ArrayList<Object[]>();
		return out;
	}
	
	
}
