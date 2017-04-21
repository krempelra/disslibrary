package de.rkrempel.diss.core.commontools;

import java.util.ArrayList;
import java.util.List;

public class NodeConvert {
	
	List<Object[]> newNodes;
	// NewIDs /oldIDS
	List<Long[]> idLookup;
	
	public NodeConvert(List<Object[]> newNodes, List<Long[]> idLookup) {
		this.newNodes = newNodes;
		this.idLookup = idLookup;
	}
	
	public List<Object[]> convertEdges(List<Object[]> toConvert){
		List<Object[]> out = new ArrayList<Object[]>();
		
		for (int i = 0; i < toConvert.size(); i++) {
			
			Object[] temp = toConvert.get(i).clone();
			
			temp[0]=LookupID((Long)temp[0]);
			temp[1]=LookupID((Long)temp[1]);
			if(temp[0]==null || temp[1]==null){
				System.out.println("ID not Found!!");
				continue;
			}
			out.add(temp);
		}	
		
		return out;
	} 
	
	private Long LookupID(Long oldID){
		for (int i = 0; i < idLookup.size(); i++) {
			oldID.equals((Long) idLookup.get(i)[1]);
			return idLookup.get(i)[0];
		}
		return null;
		
	}
	
	
	
}
