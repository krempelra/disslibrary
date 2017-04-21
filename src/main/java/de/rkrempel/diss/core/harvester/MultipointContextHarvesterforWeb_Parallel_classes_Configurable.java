package de.rkrempel.diss.core.harvester;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import de.rkrempel.diss.core.report.FilterReportWriter;
import de.rkrempel.diss.core.commontools.CooccuredMetaEdge;
import de.rkrempel.diss.core.commontools.CountInsertList;
import de.rkrempel.diss.core.commontools.EdgeComperator;
import de.rkrempel.diss.core.commontools.EdgeSorter;
import de.rkrempel.diss.core.commontools.GraphContainer;

public class MultipointContextHarvesterforWeb_Parallel_classes_Configurable<T> {
	protected Set<T> sourcenodes;
	protected List<T> thingsofInterest;
	protected LinkSource<T> linkSource;
	protected int activePassiveLinks;
	protected ToHumanReadableStringConverter<T> toHumanReadableStringConverter;
	protected GraphContainer graphContainer = null;
	protected ClassSource<T,String> classSource;
	protected boolean Filter;
	protected boolean hasFiltered;
	//Debug
	protected long start_time;
	
	
	private  float weightLinkTyp0 = 5.f;
	
	
	private  float weightLinkTyp1 = 2.f;
	
	private  float weightLinkTyp2 = 5.f;

	//The Minimum Connection of a Thing to the Sourcenode
	private  int MinConncetionThreshold = 2;

	//The Maximum Connection of a Thing to the Sourcenode
	private  int MaxConncetionThreshold = 999999;
	//The Size of the Context which triggers the Filter
	private int ContextSizeFilterGate = 180;
	//The Size of the Context which triggers the Filter on an Individual Level
	private int IndividualContextFilterGate = 50;
	
	// Decoratablility
	
	public MultipointContextHarvesterforWeb_Parallel_classes_Configurable setWeightLinkTyp0(float weightLinkTyp0) {
		this.weightLinkTyp0 = weightLinkTyp0;
		return this;
	}
	public MultipointContextHarvesterforWeb_Parallel_classes_Configurable setWeightLinkTyp1(float weightLinkTyp1) {
		this.weightLinkTyp1 = weightLinkTyp1;
		return this;
	}
	public MultipointContextHarvesterforWeb_Parallel_classes_Configurable setWeightLinkTyp2(float weightLinkTyp2) {
		this.weightLinkTyp2 = weightLinkTyp2;
		return this;
	}
	public MultipointContextHarvesterforWeb_Parallel_classes_Configurable setMaxConncetionThreshold(int maxConncetionThreshold) {
		MaxConncetionThreshold = maxConncetionThreshold;
		return this;
	}
	public MultipointContextHarvesterforWeb_Parallel_classes_Configurable setMinConncetionThreshold(int minConncetionThreshold) {
		MinConncetionThreshold = minConncetionThreshold;
		return this;
	}
	
	public MultipointContextHarvesterforWeb_Parallel_classes_Configurable setContextSizeFilterGate(
		int contextConntectionFilterThreshold) {
		ContextSizeFilterGate = contextConntectionFilterThreshold;
		return this;
	}
	public MultipointContextHarvesterforWeb_Parallel_classes_Configurable setIndividualContextFilterGate(int individualContextFilterGate) {
		IndividualContextFilterGate = individualContextFilterGate;
		return this;
	}
	
	protected void timeStart(){
		 start_time = System.nanoTime();
	
	} 
	protected void timeOut(String Message){
		long end_time = System.nanoTime();
		double difference = (end_time - start_time)/1e6;
		System.out.println(Message + difference);
		
	}
	protected double timeOut(){
		long end_time = System.nanoTime();
		double difference = (end_time - start_time)/1e6;
		return difference;
	}

	public MultipointContextHarvesterforWeb_Parallel_classes_Configurable(Set<T> sourceN,LinkSource<T> ls, int actPassv, ClassSource<T,String> classSource) {
		this(sourceN, ls, actPassv, classSource,true);
	}

	/**
	 * 
	 * @param sourceN Souce Nodes
	 * @param ls a Link source
	 * @param actPassv Active and Passive
	 * @param classSource
	 * @param filt
	 */

	public MultipointContextHarvesterforWeb_Parallel_classes_Configurable(Set<T> sourceN,LinkSource<T> ls, int actPassv, ClassSource<T,String> classSource,boolean filt) {
		sourcenodes =sourceN;
		linkSource = ls;
		activePassiveLinks = actPassv;
		toHumanReadableStringConverter=null;
		this.classSource = classSource;
		this.Filter = filt;
		this.hasFiltered = false;
	}
	
	
	 /**
	 * Filter Function that Defines which Kontext Nodes will be Possible Context in this situation
	  * @param otherContext
	  * @param Groups
	  * @return
	  */
	private List<T> calculateOtherContexts(CountInsertList<T> otherContext, Map<T,Set<T>> Groups){
		//Count Insert Set which Has the Coocurence Set
		CountInsertList<Integer> SizeContextClusters = new CountInsertList<Integer>();
		// Refining von Koten die im Kontext von Mehr als Einem Ausgangsknoten Auftauchen
		System.out.println("Total Context :"+ otherContext.getKeys().size());
		System.out.println("Av Contextsize :"+ otherContext.avarageOccurence());
		int purgeNum = otherContext.purgeLessAndOverCount(MinConncetionThreshold,MaxConncetionThreshold);
		List<T> otherPossibleContexts = otherContext.getMinCount(MinConncetionThreshold);
		
		

		System.out.println("Unused Context Removed : "+ purgeNum);

		

		if(otherPossibleContexts.size() > ContextSizeFilterGate && this.Filter){
			this.hasFiltered =true;
			System.out.println("Removing Routine");
			//Nur die Kontexte Holen welche nur mit 2 Ausgangsknoten Verbunden sind

			// Alle Gruppen als Hash Code mit Anzahl zusammenstellen
			Set<T> keys = Groups.keySet();
			
			Map<Integer,Set<T>> lookupGroups = new HashMap<Integer,Set<T>>();
			for (T t : keys) {
				Set<T> temp = Groups.get(t);
				//Only Groups between 2 Things are of Concern!
				if(temp.size() == 2){
					int code = temp.hashCode();
					lookupGroups.put(code, temp);
					SizeContextClusters.insert( code);
					
				}
			}
			//Avarage Size of the Clusters
			Float avStuff = SizeContextClusters.avarageOccurence();
			//Hashes of Source Nodes
			Set<Integer> ContextClusterKeys = SizeContextClusters.getKeys();
			//Context Nodes which will in the End be Removed
			List<Integer> toremove = new ArrayList<Integer>();
			//The Ranked Nodes which will Rank the Sets
			List<Integer> rank = new ArrayList<Integer>();
			//Abweichler finden
			Map<Integer,CooccuredMetaEdge> LookupClusterToMeta = new HashMap<Integer,CooccuredMetaEdge>();
			
			
			for (Integer ContextClusterKey : ContextClusterKeys) {
				Integer count = SizeContextClusters.getOccurenceOf(ContextClusterKey);

				if( count > IndividualContextFilterGate){

					//Writing a Report
					Set<T> Things =  lookupGroups.get(ContextClusterKey);

					T Source =null;
					T Target=null;
					for (T t2 : Things) {
						if(Source == null)
							Source =t2;
						else
							Target =t2;
					}
					
					Integer rel= Things.size();
					CooccuredMetaEdge meta = new CooccuredMetaEdge(Source, Target, rel, new HashSet<Object>(), false);
					LookupClusterToMeta.put(ContextClusterKey,meta);
					//Adding Things to the Remove Candidates
					toremove.add(ContextClusterKey);
					rank.add(count);
				}
			}
			
			
			
			//TODO Refinement Notice.
			//Alle sachen nach dem hash Code Rauswerfen.
			for (Integer rem : toremove) {
				CooccuredMetaEdge metaEdge = LookupClusterToMeta.get(rem);
				FilterReportWriter.getInstance().appendActiveEvent("filter:" + toHumanReadableStringConverter.toHumanReadable( (T)metaEdge.source)+" -X- "+ toHumanReadableStringConverter.toHumanReadable((T) metaEdge.target)) ;
				
				
				for (T t : keys) {
					
					Set<T> connectedSourceNodes = Groups.get(t);
					if(connectedSourceNodes.size()!=2)
						continue;
					
					
					
					int thing = connectedSourceNodes.hashCode();
					if(rem == thing){
						otherPossibleContexts.remove(t);
						metaEdge.coocuredStuff.add(t);
					}
				}
				FilterReportWriter.getInstance().addMetaEdge(metaEdge);
				
				
			}


			
		}
		return otherPossibleContexts;
		

		
		
	}
	
	
	public void setToHumanReadableStringConverter(
			ToHumanReadableStringConverter<T> toHumanReadableStringConverter) {
		this.toHumanReadableStringConverter = toHumanReadableStringConverter;
	}
	
	
	public void Harvest(){
		//Debug

		
		
		FilterReportWriter.getInstance().appendTechEvent("The Passive Links are  followed:\n So the Workflow WILL Include the Links that Go from somwhe Else to them\n");
	
		/*
		 * ------------------Retrival der Ausgangsknoten ----------------------------------
		 */
		
		for (T thing : sourcenodes) {
			
			FilterReportWriter.getInstance().appendTechEvent("Added Article: "+thing.toString()+"\n");
			FilterReportWriter.getInstance().appendActiveEvent("Added Article: "+thing.toString()+"\n");
		}
		
		EdgeComperator ec = new EdgeComperator();
		// Alles Edges im Endgütigen Graphen
		List<Object[]> edges = new ArrayList<Object[]> ();
		// Aufstellung aller im Abgebildeten DBpedia Knoten
		List<T> thingsofInterest= new ArrayList<T>();
		// Anfangsknoten Hinzufügen
		thingsofInterest.addAll(sourcenodes);
		for (T source : sourcenodes){
			
			if(!linkSource.containsLinksFor(source)) {
				//TODO Here is a SimpleShit Thing URL Encode etc.	
				System.out.println(source + " Does not exist!");

			}
		}
			
		
		
		// Counter bis wo die Ausgangsknoten gehen
		int goToNumber = thingsofInterest.size();
		//Anderen Kontext der Nicht Ausgangsknoten ist und die Anzahl der Auftritte in bezug auf die Ausgangsnoten
		CountInsertList<T> otherContext = new CountInsertList<T>();
		Map<T,Set<T>> Groups = new HashMap<T,Set<T>>(); 
		
		/*
		 * ------------- Erstellung der Links zwischen den Ausgangsknoten und Erstes Konext Retrival 
		 */
		
		timeStart();
		//Iterate All Start Nodes
		Map<T, Map<T, Number>> WeightedSourcelinks = new HashMap<T, Map<T, Number>>();
		List<T> notExistingStartnodes = new ArrayList<T>(sourcenodes.size());
		for (T source : sourcenodes) {
			
			Map<T, Number> Weighted = linkSource.GetLinksforResourceWeighted(source, activePassiveLinks);
			WeightedSourcelinks.put(source, Weighted);
			Set<T> links  =Weighted.keySet();
			if(links == null || links.isEmpty()){
				notExistingStartnodes.add(source);
				continue;
				
			}
				
			for (T link : links) {
	
				//Connection between Source Nodes Should not be tooo Frequent
				if(sourcenodes.contains(link)){
					Object[] in = new Object[4];
					Long one = new Long(thingsofInterest.indexOf(source));
					Long two = new Long(thingsofInterest.indexOf(link));
					if(one == null || two == null)
						continue;
					if(one < two){
						in[0]=one;
						in[1]=two;
					}else{
						in[0]=two;
						in[1]=one;
					}
					

					boolean continueToken =false;
					for (Object[] thing : edges) {
						if( 0== ec.compare(thing, in)){
							continueToken = true;
							break;
						}
					}
					if(continueToken)
						continue;
					
						
					in[2]= 0;
					in[3]= new Float(weightLinkTyp0);
					
					
					edges.add(in);
					
				}else{
					
					otherContext.insert(link);
					
					// Hier werden die KOntexte den Ausgangsknoten Zugewiesen durch ein Set
					if(Groups.containsKey(link)){
						Set<T> temp = Groups.get(link);
						temp.add(source);
						Groups.put(link, temp);
					}
					else{
						Set<T> temp = new TreeSet<T>();
						temp.add(source);
						Groups.put(link, temp);
						
					}
				}
			}
		}

		
		timeOut("SourceNodes");
		timeStart();
		List<T> otherPossibleContexts = calculateOtherContexts(otherContext,Groups);
		

	
		// Grad eines Kontextknotens zu anderen Kontextknoten
		
		
		thingsofInterest.addAll(otherPossibleContexts);
		
		
		
		
		/*
		 * --------------------Erstellung aller Links der Kontexte zwichen den Kontexten
		 * Critical LOOP!
		 */

		timeOut("InBetween");
		timeStart();
		
		//Remove non Existing Start Nodes
		//TODO Notice Unconnected Start Nodes
		for (T t : notExistingStartnodes) {
			sourcenodes.remove(t);
			FilterReportWriter.getInstance().appendActiveEvent("Removed "+t.toString() +" " );
			
		}
		//Thread Pool Initialisation
		ExecutorService executor = Executors.newCachedThreadPool();
		List<Future<List<Object[]>>> futures =  new LinkedList<Future<List<Object[]>>>();
		for (int i = 0; i < otherPossibleContexts.size(); i++) {
			//Critical Retrival
			T opc = otherPossibleContexts.get(i);
	
			List<T> links = linkSource.GetLinksforResource(opc, 1);

			
			if(links == null ||links.isEmpty())
				continue;
			//Critical Parallel Computation
			Callable<List<Object[]>> worker = new ContextHarvesterWeighted_Runner<T>(opc,links,sourcenodes,otherPossibleContexts,thingsofInterest,weightLinkTyp2,WeightedSourcelinks,true);
			Future<List<Object[]>> futu = executor.submit(worker);
			futures.add(futu);
			
		}
		timeOut("Critical_Retrival");
		timeStart();
		executor.shutdownNow();
		while (!executor.isTerminated() ) {

		}		
		
		List<Object[]> everything = new LinkedList<Object[]>();
		for (Future<List<Object[]>> future : futures) {
			try {
				everything.addAll(future.get());
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		//This is Executed bevore Addup to get Forward and Backward Links
		//Works with the Identifier of the Edges!!!!!
		CountInsertList<Long> contextDegree = new CountInsertList<Long>();
		for (int i = 0; i < everything.size(); i++) {
			Object[] temp = everything.get(i);
			//This is the Type of The Links Between Contexts
			if(temp[2].equals(2)){
				contextDegree.insert((Long)temp[0]);
				contextDegree.insert((Long)temp[1]);
			}
		}
		
		//Addup
		Collections.sort(everything,new  EdgeComperator() );
		List<Integer> idsToAddUp = new ArrayList<Integer>(2);
		//idsToAddUp.add(200000); //Faulty This isnt in the Result so it Wont Addup Nuddin!
		idsToAddUp.add(3);

		
		
		
		everything = EdgeSorter.addupherenewlist(everything,idsToAddUp);

		
		edges.addAll(everything);
		
		
		timeOut("Parallel_Overhead");
		
		
		timeStart();
		
		 List<Object[]> nodes = new ArrayList<Object[]>();
			
		 //Hidden Context Retrival
		 List<Integer> hiddenContext = new ArrayList<Integer>( thingsofInterest.size());
		 //Alle Wichtigen Kontexte Abgehen
		 for (int i=0;  thingsofInterest.size()>i ;i++){
			 //Kein Kontext sondern Sourcenode
		 	if(goToNumber>i){
		 		hiddenContext.add(i, new Integer(0));
		 		continue;
		 	}
		 	
		 	boolean solution = false;
			for (T t : sourcenodes) {
				
				Map<T, Number> temp = WeightedSourcelinks.get(t);
				//Dies ist Das KReterieum Alle 1er Liks also Diekte Links der Sourcenodes Wenn einer Erreicht ist dann wird unterbrochen
				Integer equal1 = new Integer(1);
				Integer equal2 = new Integer(3);
				// Nullcheck durch equals Vermieden
				if(equal1.equals(temp.get(thingsofInterest.get(i)))||	equal2.equals(temp.get(thingsofInterest.get(i)))){
					hiddenContext.add(i, new Integer(0));
					solution = true;
					break;
				}
				
				
				
			}
			if(!solution){
				
				hiddenContext.add(i, new Integer(1));
			}
		 }
		 
		for (int i = 0; i < thingsofInterest.size(); i++) {
			
			T newnode = thingsofInterest.get(i);
			
			////////////NEw Retrival
			
			
			List<String> temp = classSource.getClasses(newnode);
			
			StringBuffer ClassesList = new StringBuffer("");
			
			for (int j = 0; j < temp.size(); j++) {
				ClassesList.append(temp.get(j));
				if(j < temp.size()-2)
					ClassesList.append(",");
			}
			
			
			///NEW Retrival End
			Long id= new Long(i);
			
			Integer overallInDegree = linkSource.inDegree(newnode);
			Integer overallOutDegree = linkSource.outDegree(newnode);
			//Integer overallDegree = linkSource.allDegree(newnode);
			Float rankBySource = 0.0f;
			Float rankContextToLocal = 0.0f;
			Float rankContextToWorldwide = 0.0f;
			
			//Ranks for ContextNodes
			if(! (goToNumber>i) ){
				Integer cdeg1 = otherContext.getOccurenceOf(newnode);
				
				rankBySource = new Float(cdeg1.floatValue()/sourcenodes.size() );
			
				//TODO Ungenauigkeitbeseitigen
				
				Integer cdeg2 = contextDegree.getOccurenceOf(id);
				Integer cdeg = 0;
				
				//if(cdeg1 != null)
				cdeg += cdeg1;
				//If has NO Connections to Other Contexts just to Source Nodes 
				if(cdeg2 != null)					
					cdeg += cdeg2;
				
				rankContextToLocal = new Float(cdeg.floatValue()/ ((thingsofInterest.size()-1)*2f));
				
				rankContextToWorldwide = new Float(cdeg.floatValue()/ (overallInDegree+overallOutDegree));
				
				
				if(rankContextToWorldwide > 1.0f  || rankContextToWorldwide.equals(Float.NaN) || rankContextToWorldwide.equals(0f) ){
					System.out.println( "Problems"+rankContextToWorldwide+ " cdeg1  "+cdeg1 + " cdeg2 "+cdeg2+" overallInDegree "+overallInDegree+" overallOutDegree "+overallOutDegree);
				
					
				}
					
			}

			Object[] node;
			if(toHumanReadableStringConverter != null)
				node = new Object[10];
			else
				node = new Object[9];
			int runningNumber =0;
			
			node[runningNumber]= id;
			runningNumber++;
			

			if(toHumanReadableStringConverter != null){
			
				node[runningNumber]= new String(   toHumanReadableStringConverter.toHumanReadable(newnode));
				runningNumber++;
			}
			
			
			node[runningNumber]= newnode.toString();
			runningNumber++;
			if(goToNumber>i){
				node[runningNumber]= new Integer(0);
			}
			else
				node[runningNumber]= new Integer(1);
			runningNumber++;
			node[runningNumber] = overallInDegree;
			runningNumber++;
			node[runningNumber] = overallOutDegree;
			runningNumber++;
			node[runningNumber] = rankBySource;
			runningNumber++;
			node[runningNumber] = rankContextToWorldwide;
			runningNumber++;
			node[runningNumber] = ClassesList.toString();
			runningNumber++;
			node[runningNumber] = hiddenContext.get(i);
			runningNumber++;
			nodes.add(node);
		}
		
		timeOut("Dirty Rest");
		
		System.out.println("Number of edges"+ edges.size());
		//System.out.println("Other non Followed Context Nodes"+otherContext.size());
		
		graphContainer = new GraphContainer(nodes,edges);
		graphContainer.autoInitializeLabels();
		
		ArrayList<String> nodeLabels = new ArrayList<String>();
		
		nodeLabels.add("ID");
		nodeLabels.add("Label");
		if(toHumanReadableStringConverter != null){
			nodeLabels.add("LargeLabel");
		}
		nodeLabels.add("IsContext");
		nodeLabels.add("INDegreeInTotalGraph");
		nodeLabels.add("OUTDegreeInTotalGraph");
		nodeLabels.add("SourcenodeRanking");
		nodeLabels.add("ContextToWorldwideRanking");
		nodeLabels.add("Classes");
		nodeLabels.add("HiddenContext");
		graphContainer.setNodeLabels(nodeLabels);
		
		ArrayList<String> edgeLabels = new ArrayList<String>();
		
		edgeLabels.add("Source");
		edgeLabels.add("Target");
		
		edgeLabels.add("ConnectionType");
		edgeLabels.add("RankContextLowSourcesHigh");
		
		graphContainer.setEdgeLabels(edgeLabels);
		
		
		
		
	}
	
	public GraphContainer getResult() {
		return graphContainer;
	}
	
	//Getter
	
	public int getActivePassiveLinks() {
		return activePassiveLinks;
	}
	public GraphContainer getGraphContainer() {
		return graphContainer;
	}
	public LinkSource<T> getLinkSource() {
		return linkSource;
	}
	public Set<T> getSourcenodes() {
		return sourcenodes;
	}
	public List<T> getThingsofInterest() {
		return thingsofInterest;
	}
	public ToHumanReadableStringConverter<T> getToHumanReadableStringConverter() {
		return toHumanReadableStringConverter;
	}
	
	public void setActivePassiveLinks(int activePassiveLinks) {
		this.activePassiveLinks = activePassiveLinks;
	}
	public void setLinkSource(LinkSource<T> linkSource) {
		this.linkSource = linkSource;
	}
	public void setSourcenodes(Set<T> sourcenodes) {
		this.sourcenodes = sourcenodes;
	}
	
	public boolean isHasFiltered() {
		return hasFiltered;
	}
}
