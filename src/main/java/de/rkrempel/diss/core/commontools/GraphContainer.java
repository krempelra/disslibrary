package de.rkrempel.diss.core.commontools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.rkrempel.diss.core.report.ReportWriter;
/**
 * This Class Should Reference as A Graph Container.
 * @author rasmus
 *
 */
public class GraphContainer {

	
	//The First Element of Object[] is a Long that Carrys The ID
	protected List<Object[]> nodes;
	//The First two Elements of Object[](Corresponds to Node ID)
	protected List<Object[]> edges;
	
	//Carrys The Class Infos of the Nodes
	protected List<Class> nodeTypes;
	//Carrys The Class Infos of the Edges
	protected List<Class> edgeTypes;
	
	//labels that Carry the Description of the Correlations
	protected List<String> nodeLabels;
	protected List<String> edgeLabels;
	
	
	protected String comment;
	protected String name;
	protected boolean hasDegree;
	protected boolean isDegreeuptodate;

	protected boolean valid;
	
	
	
	
	
	public static enum graphConnectionType {
		DirectedGraph, UndirectedGraph
	}
	protected void edit(){
		isDegreeuptodate = false;
		valid = false;
	}
	protected void makeValid(){
		if(valid)
			return;
			
		nodeTypes = GraphContainer.analyseHeaders(nodes);

		edgeTypes = GraphContainer.analyseHeaders(edges);
		
	}
	protected graphConnectionType type;
	
	protected void InduceNodesFromEdges(){
		System.out.println("Inducing nodes");
		List<Object[]> tempnodes = new ArrayList<Object[]>();
		Set<Long> ids = new HashSet<Long>();
		
		for (Object[] objects : edges) {
			ids.add((Long) objects[0]);
			ids.add((Long) objects[1]);
		}
			for (Long long1 : ids) {
			
				Object[] in = new Object[1];
				in[0] = long1;
				tempnodes.add(in);
				
			}

			this.setNodes(tempnodes);
	}
	public static String ShowEdgelistAttributes(List<Object[]> edges,Long nodessize){

		StringBuilder out = new StringBuilder();

		float WeightAvarage=0;
		float relevanceAvarage=0;
		Set<Long> connectedNodes = new HashSet<Long>();
		for (Object[] objects : edges) {
			
			connectedNodes.add((Long)objects[0]); 
			connectedNodes.add((Long)objects[1]); 
			WeightAvarage += (Float)objects[2];
			relevanceAvarage += (Float)objects[3];
		}
		
		WeightAvarage= WeightAvarage/  new Float(edges.size()).floatValue();
		relevanceAvarage=relevanceAvarage/  new Float(edges.size()).floatValue();
		
		

		out.append("Nodes: "+nodessize+"  Edges: "+edges.size()+"\n");
		if(edges.size() == 0)
			return out.toString();
		
		out.append("Maximum Density: "+(2.0f*edges.size())/(nodessize.floatValue()*(nodessize.floatValue()-1.0f))+"\n");
		out.append("Connected Nodes: "+connectedNodes.size() +" Connected Density: "+(2.0f*edges.size())/(new Float(connectedNodes.size()).floatValue()*(new Float(connectedNodes.size()).floatValue()-1.0f))+"\n");
		out.append("Avarage Edge Weight: "+WeightAvarage +" Avarage Edge Uniquity: "+  relevanceAvarage+"\n");
		//Heavy Calculation Step.
		out.append( EdgeListConnectedComponentsAnalysis(edges));

		
		return out.toString();
		
		
	}
	
	public static List<Long> nodesFromEdgeList(List<Object[]> edges){
		
		List<Long> tempnodes = new ArrayList<Long>();
		Set<Long> ids = new HashSet<Long>();
		
		for (Object[] objects : edges) {
			ids.add((Long) objects[0]);
			ids.add((Long) objects[1]);
		}
		tempnodes.addAll(ids);
		return tempnodes;
		
	}
	
	//TODO DAMN SLOW MAN! has to be faster
	public static String EdgeListConnectedComponentsAnalysis(List<Object[]> in){
		List<Set> CompleteSets= new ArrayList<Set>();
		List<Float> CompleteSetsAvgDeg= new ArrayList<Float>();
		
		LinkedList<Object[]> tempin = new LinkedList<Object[]>(); 
		Set<Object> nodes = new HashSet<Object>(); 
		for (Object[] objects : in) {
			nodes.add(objects[0]);
			nodes.add(objects[1]);
		}
		
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
		StringBuilder out = new StringBuilder();
		out.append( "Number of Components:"+ CompleteSets.size()+"\n");
		out.append( "Avg size of Components : "+ ((new Float(allKnown.size())).floatValue()/(new Float(CompleteSets.size())).floatValue()) +"\n");
		out.append( "Largest Components : \n");
		for(int i=0;i<size;i++){
			String anmerkung = new String();
			if(largestComponents[i].floatValue()== largestComponentsAvgDeg[i]+1.0f)
				anmerkung = "Completely Connected";
			out.append( (i+1)+". Componentsize "+largestComponents[i] +" Avg. component Degree : "+largestComponentsAvgDeg[i]+" "+anmerkung+"\n");
			
		}
		
		
		return out.toString();
		
	}
	
	public static List<Object> getAllAdjencies(Object start, List<Object[]> edges){
		
		List<Object> out = new ArrayList<Object>();
		
		for (Object[] objects : edges) {
			if(start.equals(objects[0]))
				out.add(objects[1]);
			else if(start.equals(objects[1])){
				out.add(objects[0]);
			}
		}
		
		
		return out;
		
	}
	
	
	
	public GraphContainer( List<Object[]> nodes, List<Object[]> edges , boolean directed ) {
		this.setEdges(edges);
		if(nodes == null || nodes.isEmpty()){
			this.InduceNodesFromEdges();
		}
		else
			this.setNodes(nodes);
		

		if(!directed)
			type = graphConnectionType.UndirectedGraph;
		else
			type = graphConnectionType.DirectedGraph;
		
		nodeTypes = GraphContainer.analyseHeaders(this.nodes);
		nodeLabels = new ArrayList<String>(nodeTypes.size());
		edgeTypes = GraphContainer.analyseHeaders(this.edges);
		comment = new String("This is the Standardized Function");
		name = new String("OutGraph");
		this.autoInitializeLabels();
		valid = true;
		
		
	}
	/**
	 * Consruct a Typicaly Undirected Graph
	 * @param nodes nodes as List<Object>;
	 * @param edges Edges as List<Object>;
	 */
	public GraphContainer( List<Object[]> nodes, List<Object[]> edges  ) {
		this.setEdges(edges);
		if(nodes == null || nodes.isEmpty()){
			this.InduceNodesFromEdges();
		}
		else
			this.setNodes(nodes);
		
		//List<String> nodeLabels = new ArrayList<String>(edges.get(0).length);
		
		type = graphConnectionType.UndirectedGraph;
		
		nodeTypes = GraphContainer.analyseHeaders(this.nodes);

		edgeTypes = GraphContainer.analyseHeaders(this.edges);
		comment = new String("This is the Standardized Function");
		name = new String("OutGraph");
		this.autoInitializeLabels();
		valid = true;		
	}
	
	public void autoInitializeLabels(){

		nodeLabels= autoInitializeLabelsNodes(nodes);

		edgeLabels=autoInitializeLabelsEdges(edges);
		
	}
	
	
	public static List<String> autoInitializeLabelsNodes(List<Object[]> nodes){
		Object[] obj = nodes.get(0);
		int nodeAttribSize =obj.length;
		List<String> nodeLabels = new ArrayList<String>(nodeAttribSize);
		
		nodeLabels.add("ID");

		int i =1;
		
		for (; i<nodeAttribSize;i++){
			if(nodeAttribSize == 1 && obj[1].getClass().equals(String.class)){
				nodeLabels.add("Label");
			}
			else
				nodeLabels.add("NodeAttribute"+i);
		
		
		}
		
		return nodeLabels;
		
		
	}
	
	public static List<String> autoInitializeLabelsEdges(List<Object[]> edges){
		Object[] obj = edges.get(0);
		int edgeAttribSize = obj.length;

		
		List<String> edgeLabels = new ArrayList<String>(edgeAttribSize);
		
		
		
		edgeLabels.add("Source");

		edgeLabels.add("Target");
		int i =2;
		if(edgeAttribSize > 2 && obj[2].getClass().equals(Float.class) ){
			edgeLabels.add("Weight");
			i=2;
		}
		
		for (; i<edgeAttribSize;i++){
			edgeLabels.add("EdgeAttribute"+(i-1));
		}
		return edgeLabels;
	}
	
	
	
	public static List<Class> analyseHeaders(List<Object[]> toAnalyze){
		
		List<Class> out = null;
		if(toAnalyze != null && !toAnalyze.isEmpty() ){
			
			Object[] sample = toAnalyze.get(0);
			
			out = new ArrayList<Class>(sample.length);

			for (int i = 0; i < sample.length; i++) {
				
				Class temp = sample[i].getClass();
				out.add(temp);
			}

		}
		return out;
	}
	
	/**
	 * Removed The Unconnected Nodes
	 */
	public void reduceNodes() {
		ReportWriter.getInstance().appendReportEvent("Reducing Nodes");
		//TODO more Efficient
		List<Object[]> newNodes = new ArrayList<Object[]>();
		
		Set<Long> set = new HashSet<Long>();
		for (int i = 0; i < edges.size(); i++) {
			
			set.add((Long)edges.get(i)[0]);  
			set.add((Long)edges.get(i)[1] );
			
			
		}
		
		for (int i = 0; i < nodes.size(); i++) {

			if(set.contains((Long)nodes.get(i)[0]))
				newNodes.add(nodes.get(i));
			
			
		}

		System.out.println("Done");
		ReportWriter.getInstance().appendReportEvent("reduced nodes :"+ (nodes.size()-newNodes.size()));
		nodes = newNodes;
	}

	
	
	public void introduceRunnungNumber() {
		 Map<String,Long> translate = null;
		translate = new HashMap<String,Long>();
		List<Object[]> newnodes = new  ArrayList<Object[]>(nodes.size());
		long runningnumber =0; 
		for (int i = 0; i < nodes.size(); i++) {
			Object[] element = nodes.get(i);
			runningnumber++;
			Object[] NN = new Object[element.length+1];
			NN[0]= runningnumber;
			for( int j=1;j<element.length+1;j++){
				
				NN[j]= element[j-1];
				
			}
			newnodes.add(NN);
			
			translate.put(element[0].toString(),runningnumber);
			
		}
		for (Object[] objects : edges) {
			
			objects[1] = translate.get( objects[1].toString());	
			objects[0] = translate.get( objects[0].toString());
			
		}
		
	}
	//Getter Setter
	
	public void AppendEdges(List<Object[]> edgestoAppend) {
		edit();
		
		ReportWriter.getInstance().appendReportEvent("edges to append" + edgestoAppend.size() + " , number of Old Edges "+ edges.size());

		edges.addAll(edgestoAppend);
		Collections.sort(edges,new  EdgeComperator() );
		edges =EdgeSorter.addupherenewlist(edges);

		ReportWriter.getInstance().appendReportEvent("Edges afeter appending : "+ edges.size());
		
	}
	public void FilterEdgesUnderWeight(float lowercut) {
		int i = 0;
		ReportWriter.getInstance().appendReportEvent("Filtering edges lower than " + lowercut);

		for (Iterator iterator = edges.iterator(); iterator.hasNext();) {
			Object[] edge = (Object[]) iterator.next();
			Float temp = (Float) edge[2];
			if(temp< lowercut){
				iterator.remove();
				i++;
			}
		}
		ReportWriter.getInstance().appendReportEvent("Removed Edges bs lowcut filter" + i);
		
	}
	
	public void calculateDegree(){
		
		
		if(!hasDegree)
			appendValueToNodesByIDMap(CalculateDegreeWithEdges(edges), "degree");
		else if(!isDegreeuptodate)
			updateValueToNodesByIDMap(CalculateDegreeWithEdges(edges), "degree");
			
		hasDegree= true;
		isDegreeuptodate = true;
		
		
	}
	private void updateValueToNodesByIDMap(
			Map<Long, Object> in, String toUpdate) {
			int indexToUpdate;
			boolean found = false;
			for (indexToUpdate = 0;indexToUpdate < nodeLabels.size(); indexToUpdate++) {
				if(toUpdate.equals(nodeLabels.get(indexToUpdate))){
					found = true;
					break;
				}
			}
			if(!found)
				return;
			
			Object[] node;
			for(int i = 0;i< nodes.size();i++){
				node = nodes.get(i);
				node[indexToUpdate] = in.get((Long)node[0]);
			}
			
			
		
	}

	protected static Map<Long,Object> CalculateDegreeWithEdges(List<Object[]> edges){
		
	Map<Long,Object> degree =  new HashMap<Long,Object>();
		
		
		for (int i = 0; i < edges.size(); i++) {
			
			Object[] edge = edges.get(i);
			Long temp1 = (Long) edge[0];
			Long temp2 = (Long) edge[1];
			Integer tempdeg = (Integer)degree.get(temp1);
			if(tempdeg != null){
				tempdeg++;
			}else{
				tempdeg = new Integer(1);
				
			}
			degree.put(temp1, tempdeg );				
			tempdeg = (Integer)degree.get(temp2);
			if(tempdeg != null){
				tempdeg++;
			}else{
				tempdeg = new Integer(1);
				
			}
			degree.put(temp2, tempdeg );				
			
		}
		return degree;
	}
	
	protected static List<Object[]> getEdgesToNodeID(Long NodeID,List<Object[]> edges){
		
		List<Object[]> outEdges = new ArrayList<Object[]>();
		
		
		for (int i = 0; i < edges.size(); i++) {
			Object[] edge = edges.get(i);
			Long temp1 = (Long) edge[0];
			Long temp2 = (Long) edge[1];
			if(temp1.equals(NodeID)|| temp2.equals(NodeID)){
				
				outEdges.add(edge);
			}
		}
		return outEdges;
	}
	public List<Object[]> GetNodeByID(Long NodeID){
		
		return getEdgesToNodeID(NodeID,edges);
		
	}
	public void removeNodeByID(Long NodeID){
		edit();
		edges = edgesRemoveNodeByID(NodeID,edges);
		nodes = nodesRemoveNodeByID(NodeID,nodes);
		
		
	}
	
	protected static List<Object[]> nodesRemoveNodeByID(Long NodeID, List<Object[]> nodes ){
		
		
		for (int i = 0; i < nodes.size(); i++) {
			Object[] node = nodes.get(i);
			Long temp1 = (Long) node[0];
			if(temp1.equals(NodeID)){
				
				nodes.remove(i);
				break;
			}
		}
		return nodes;
	}
	protected static List<Object[]> edgesRemoveNodeByID(Long NodeID, List<Object[]> edges ){
		
		List<Integer> toremoveEdgeIDs = new ArrayList<Integer>();
		for (int i = 0; i < edges.size(); i++) {
			Object[] edge = edges.get(i);
			Long temp1 = (Long) edge[0];
			Long temp2 = (Long) edge[1];
			if(temp1.equals(NodeID)|| temp2.equals(NodeID)){
				
				toremoveEdgeIDs.add(i);
			}
		}
		for (int i = toremoveEdgeIDs.size(); i > -1; i--) {
			edges.remove(toremoveEdgeIDs.get(i));
		}
		return edges;
	}
	protected static List<Object[]> nodesRemoveNodeByIDs(List<Long>  NodeIDs, List<Object[]> nodes ){
		for (int i = 0; i < NodeIDs.size(); i++) {
			Long NodeID = NodeIDs.get(i);
			for (int j = 0; j < nodes.size(); j++) {
				Object[] node = nodes.get(i);
				Long temp1 = (Long) node[0];
				if(temp1.equals(NodeID)){
					
					nodes.remove(i);
					break;
				}
			}
		}
		return nodes;
	}
	protected static List<Object[]> edgesRemoveNodeByIDs(List<Long> NodeIDs, List<Object[]> edges ){
		
		List<Integer> toremoveEdgeIDs = new ArrayList<Integer>();
		for (int i = 0; i < edges.size(); i++) {
			Object[] edge = edges.get(i);
			Long temp1 = (Long) edge[0];
			Long temp2 = (Long) edge[1];
			for (int j = 0; j < NodeIDs.size(); j++) {
				if(temp1.equals(NodeIDs.get(i))|| temp2.equals(NodeIDs.get(i))){
					toremoveEdgeIDs.add(i);
				}	
			}
			
		}
		for (int i = toremoveEdgeIDs.size(); i > -1; i--) {
			edges.remove(toremoveEdgeIDs.get(i));
		}
		return edges;
	}
	
	public void removeNodeByIDs(List<Long> NodeIDs){
		edit();
		edges = edgesRemoveNodeByIDs(NodeIDs,edges);
		nodes = nodesRemoveNodeByIDs(NodeIDs,nodes);

		
	}
	/**
	 * Remove Dublicates etc
	 */
	public void validate(){
		//TODO Remove Dublicates
		removeDublicates();
		
		
	}
	
	private void removeDublicates() {
		//Looks strange but see EdgeSorter.addupherenewlist description
		Collection<Integer> fieldsDummy = new ArrayList<Integer>();
		fieldsDummy.add(0);
		
		edges =EdgeSorter.addupherenewlist(edges, fieldsDummy );
	}
	
	
	public List<Object[]> equalsFilter(String string, Object object) {
		int i=0;
		List<Object[]> out = new ArrayList<Object[]>();
		
		for (i=0;i<edgeLabels.size();i++) {
			
			if(string.equals(edgeLabels.get(i)))
				break;
		}
		
		int j = i;
		
		for (i=0;i<edges.size();i++) {
			
			if(object.equals(edges.get(i)[j])){
				
				out.add(edges.get(i));
				
			}
		
		}
		return out;
	}
	
	protected void appendValueToNodesByIDMap(Map<Long, Object> in, String Name) {


		Object[] node;
		for(int i = 0;i< nodes.size();i++){
			node = nodes.get(i);
			Object[] newObj = new Object[node.length+1];
			for (int j = 0; j < node.length; j++) {
				newObj[j] = node[j];
			}
			newObj[node.length] = in.get((Long)node[0]);
			
			nodes.set(i, newObj);
			
		}
		
		nodeLabels.add(Name);
	}
	//Getter Setter
	
	
	
	

	public Integer getNumNodes() {
		makeValid();
		return nodes.size();
	}
	
	public Integer  getNumEdges() {
		makeValid();
		return edges.size();
	}
	
	
	
	public String getName() {
		makeValid();
		return name;
	}
	public List<String> getEdgeLabels() {
		makeValid();
		return edgeLabels;
	}
	public List<Object[]> getEdges() {
		makeValid();
		return edges;
	}
	public String getComment() {
		makeValid();
		return comment;
	}
	public List<Object[]> getNodes() {
		makeValid();
		return nodes;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public void setEdgeLabels(List<String> edgeLabels) {
		this.edgeLabels = edgeLabels;
	}
	public List<String> getNodeLabels() {
		makeValid();
		return nodeLabels;
	}
	public void setNodeLabels(List<String> nodeLabels) {
		this.nodeLabels = nodeLabels;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setEdges(List<Object[]> edges) {
		edit();
		this.edges = edges;
		edgeTypes = GraphContainer.analyseHeaders(this.edges);
	}
	public void setNodes(List<Object[]> nodes) {
		edit();
		this.nodes = nodes;
		nodeTypes = GraphContainer.analyseHeaders(this.nodes);
	}
	
	public List<Class> getEdgeTypes() {
		 makeValid();
		return edgeTypes;
		
	}
	
	public List<Class> getNodeTypes() {
		 makeValid();
		return nodeTypes;
	}

	
}
