package de.rkrempel.diss.core.commontools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import de.rkrempel.diss.core.report.ReportWriter;
/**
 * Creates a Coocurence Network from a Reference Table
 * Creates a One Mode Network from a Two Mode Network
 * The Input is an Edge List
 * @author rasmus
 *
 */
public class TwoModeToOneModeNetworkConverter {
	protected boolean doWeight = false;
	protected List<Long[]> lastCTin =null; 
	protected boolean doWeightInvertedByNumberofLinks = false;
	// This is The Relevance Check. Its there for Blocking large low Weight Clusters
	protected float minimumRelevance= 0.0F;
	
	public void weightInvertetbyNumberofLinkage(){
		doWeight = true;
		doWeightInvertedByNumberofLinks =true;
		
	}
	public void weightaddup(){
		doWeight = true;
		doWeightInvertedByNumberofLinks =false;
		
	}
	public void noweight(){
		doWeight = false;
		doWeightInvertedByNumberofLinks =false;
		
	}
	
	/**
	 * Benutzt den letzen umgewandelten teil um Nodes mit Gewichtung nach Absoluten Referenzen also auch solche die nicht zu Coocurence Verbindungen fürhten zu gewichten.
	 * @return
	 */
	public List<Object[]> NodesWithReferenceWeight(){
		Collections.sort(lastCTin, new SortTableByNthField(0));
		
		
		Object[] last = null;
		List<Object[]> Nodes = new ArrayList<Object[]>();
		int referencecount =0;
		for (Object[] objects : lastCTin) {
			if(last == null ){
				referencecount ++;
				last = objects;
			}else if( last[0].equals(objects[0])){
				referencecount ++;
				last = objects;
			}else{
				
				Object[] temp = new Object[2];
				
				temp[0] = last[0];
				temp[1] = new Integer(referencecount);
				
				Nodes.add(temp);
				last = objects;
				referencecount = 1;
			}
			
			
			
			
		}

			
		Object[] temp = new Object[2];
		
		temp[0] = last[0];
		temp[1] = new Integer(referencecount);
		
		Nodes.add(temp);
		
		return Nodes;
	}
	
	
	
	public List<Object[]> makeCTEdges(List<Long[]> CT){
		

		Collections.sort(CT, new EdgeToCrosstableizeComperator());
		//createCrossTableNet( CT);
		lastCTin= CT;
		
		return this.createCrossTableNet(CT);
		
	}
	
	protected List<Object[]> createCrossTableNet(List<Long[]> CT){

		
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
					for (int j = i+1; j < temporaryIDs.size(); j++) {
						Long one = temporaryIDs.get(i);
						Long two = temporaryIDs.get(j);
						
						if(one >two){
							Long temp = two;
							two= one;
							one= temp;
							
						}
						Object[] tempstring;
						if(doWeight)
							tempstring = new Number[3];
						else
							tempstring = new Number[2];
						tempstring[0] = one;
						tempstring[1] = two;
						//No Weighting
						//tempstring[2] = weight;
						if(doWeight){
							if(doWeightInvertedByNumberofLinks)
								tempstring[2] = weight/(temporaryIDs.size()-1);
							else
								tempstring[2] = weight;
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
		Edges =EdgeSorter.addupherenewlist(Edges);
		System.out.println("Edges after addup"+Edges.size());
		return Edges;
	}
	
	/**
	 * Calls makeCTEdgesAllWeightsMarked with an -1 Mark so nothing will be Marked
	 * @param CT Cross Table A List of Array Of Longs which Represent the Edges with the Node IDs
	 * @return
	 */
	public List<Object[]> makeCTEdgesAllWeights(List<Long[]> CT){
		return makeCTEdgesAllWeightsMarked(CT,-1);
	}
	/**
	 * This Function creates Coocurence Projection With all Measures that there Are
	 * @param CT Cross Table A List of Array Of Longs which Represent the Edges with the Node IDs
	 * @param mark A Positive Integer that will be Appended to the Edges If 0 or negative No mark will be set
	 * @return The Coocurence Table That is Projected!
	 */
	public List<Object[]> makeCTEdgesAllWeightsMarked(List<Long[]> CT, int mark){
		//Initial Sorting of the Crosstable
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
				fromListEdgesAllWeightsMarked(mark, Edges,
						LastID, temporaryIDs, weight);
				temporaryIDs =new Vector<Long>();
				temporaryIDs.add(vec[0]);
			}
			
			LastID = occid;

			
		}
		fromListEdgesAllWeightsMarked(mark, Edges,
				LastID, temporaryIDs, weight);
		
		
		//The Dublicates have to be removed
		Collections.sort(Edges,new  EdgeComperator() );
		System.out.println("Edges bevore addup"+Edges.size());
		
		List<Integer> idsToAddUp = new ArrayList<Integer>(2);
		idsToAddUp.add(2);
		idsToAddUp.add(3);
		
		
		
		Edges =EdgeSorter.addupherenewlist(Edges,idsToAddUp);
		for (int i = 0; i < Edges.size(); i++) {
			Edges.get(i)[4]=((Float) Edges.get(i)[3])/ ((Float) Edges.get(i)[2]);
		}
		
		System.out.println("Edges after addup"+Edges.size());
		return Edges;
	}
	

	/**
	 * TODO Parallelisation !!!!!
	 * Creates Clusters Of Edges between these Things The that are in the Temporary temporaryIDs field
	 * @param mark this is an Optional Mark which can be Given to the Nodes But only if its Bigger than  0 (Zero)
	 * @param Edges This is The Output of The Funtion its a Reference and will be enriched with the Output
	 * @param LastID This is the ID that shows which Node is Responsible for the Creation of the Edges.
	 * @param temporaryIDs This is the List of IDs which will be Connected here
	 * @param weight This a Weigthing Constatr that will Provide the Base for the Relevance Funtion if its Smaller than the Mininmum Relevance this Result will be discarded with a Message to the Report writer
	 */
	protected void  fromListEdgesAllWeightsMarked(int mark,
			List<Object[]> Edges, Long LastID, List<Long> temporaryIDs,
			float weight) {
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
					tempstring = new Number[6];
				else
					tempstring = new Number[5];
				tempstring[0] = one;
				tempstring[1] = two;
				//No Weighting
				//tempstring[2] = weight;
				tempstring[2] = weight;
				
				tempstring[3] = relevance;
				// The Size of This Field is Irrelevant it has tobe Computed after the The Addup is Done Look lower!
				tempstring[4] = weight;
				//Edge Marking
				if(mark >0)
					tempstring[5] = new Integer(mark);
					
				Edges.add(tempstring);
				
				
				
			}
		}

	}
	/**
	 * This sets the Minimum Relevance an Edge hat to have bevore beeing Ignored fo creatin too much Links that Will Densify 
	 * @param minimumRelevance The 1/<Number of Created Links. the Smaller the more Edges can potentional be created. 
	 */
	public void setMinimumRelevance(float minimumRelevance) {
		this.minimumRelevance = minimumRelevance;
		ReportWriter.getInstance().appendReportEvent("Minimum relevance for Edges ist set to "+new Float(minimumRelevance).toString());
	}
	
	
}
