package de.rkrempel.diss.core.commontools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import de.rkrempel.diss.core.report.ReportWriter;

public class TwoModeToOneModeNetworkConverterEgesAsSets extends
		TwoModeToOneModeNetworkConverter {
	@Override
	public List<Object[]> makeCTEdgesAllWeightsMarked(List<Long[]> CT, int mark){
		Collections.sort(CT, new EdgeToCrosstableizeComperator());
		lastCTin= CT;
		
		List<Object[]> Edges = new ArrayList<Object[]>();

		Long LastID = new Long(0);
		List<Long> temporaryIDs = new ArrayList<Long>(10);
		float weight = 1.0f;
		for (Iterator<Long[]> iterator = CT.iterator(); iterator.hasNext();) {
			Long[] vec = (Long[]) iterator.next();
			
			Long occid = vec[1];
			if(occid.equals(LastID)){
				temporaryIDs.add(vec[0]);
				
			}else{
				//InserEvent
				
				for (int i = 0; i < temporaryIDs.size(); i++) {
					float relevance = weight/(temporaryIDs.size()-1);
					// This is The Relevance Check. Its there for Blocking large low Weight Clusters
					if(relevance < minimumRelevance){
						System.out.println("Minimum Relevance Filter! Count : "+temporaryIDs.size() +" id: "+LastID+" ");
						ReportWriter.getInstance().appendReportEvent("Minimum Relevance Filter Snapped!\n Number of Linked Objects : "+temporaryIDs.size() +"\n ID connecting Dataset: "+LastID+" ");
						break;
					}
					for (int j = i+1; j < temporaryIDs.size(); j++) {
						Long one = temporaryIDs.get(i);
						Long two = temporaryIDs.get(j);
						
						if(one >two){
							Long temp = two;
							two= one;
							one= temp;
							
						}
						Object[] tempstring;
						
						
						if(mark >0)
							tempstring = new Object[7];
						else
							tempstring = new Object[6];
						tempstring[0] = one;
						tempstring[1] = two;
						//No Weighting
						//tempstring[2] = weight;
						tempstring[2] = weight;
						
						tempstring[3] = relevance;
						// The Size of This Field is Irrelevant it has tobe Computed after the The Addup is Done Look lower!
						tempstring[4] = weight;
						
						
						//Edge Marking
						if(mark >0){
							tempstring[5] = new Integer(mark);
							//tempstring[6] = LastID.toString();
							tempstring[6] = LastID;
						}else{
							//tempstring[5] = LastID.toString();
							tempstring[5] = LastID;
						}
						
						Edges.add(tempstring);
						
						
						
					}
				}
				temporaryIDs =new Vector<Long>();
				temporaryIDs.add(vec[0]);

			}
			
			LastID = occid;

			
		}
		Collections.sort(Edges,new  EdgeComperator() );
		System.out.println("Edges bevore addup"+Edges.size());
		
		List<Integer> idsToAddUp = new ArrayList<Integer>(2);
		idsToAddUp.add(2);
		idsToAddUp.add(3);
		//Give the Commandto Create Sets from Edges
		if(mark>0)
			idsToAddUp.add(6);
		else 
			idsToAddUp.add(5);
		for (int i = 0; i < Edges.size(); i++) {
			System.out.println(Edges.get(i)[0]+" "+Edges.get(i)[1]+" "+Edges.get(i)[5] );
		}
		Edges =EdgeSorter.addupherenewlistTypeSensitive(Edges,idsToAddUp);
		for (int i = 0; i < Edges.size(); i++) {
			Edges.get(i)[4]=((Float) Edges.get(i)[3])/ ((Float) Edges.get(i)[2]);
		}
		
		System.out.println("Edges after addup"+Edges.size());
		return Edges;
	}
}
