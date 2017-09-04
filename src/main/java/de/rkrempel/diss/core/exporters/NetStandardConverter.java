package de.rkrempel.diss.core.exporters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.rkrempel.diss.core.commontools.GraphContainer;

/**
 * Exports for NWB and Pajek
 */
public class NetStandardConverter {
	private List<Object[]> nodes;
	private List<Object[]> edges;
	private List<Class> nodestypes;
	private List<Class> edgestypes;
	private int nodeAttributesSize;
	private int edgeAttributesSize;
	private Map<String,Long> translate = null;
	private boolean useRunningNumber =false;
	private boolean justUseMentionedNodes = true;
	private GraphContainer gt;
	private String csvFieldSperator = ";";
	private String csvLineEnd="\n";
	
	//////////////////////Setter
	
	public void setNodes(List<Object[]> a){
		nodes =a;	
		gt = null;
	}
	
	public void setEdges(List<Object[]> a){
		edges =a;
		gt = null;
	}
	
	public void setGraph(GraphContainer a){
		edges =a.getEdges();
		nodes =a.getNodes();
		gt = a;
		
	}
	
	
	///////////////////////////Output Prducer
	
	public String NWBnet(){
		useRunningNumber = true;
		if(justUseMentionedNodes)
			this.reduceNodes();
		
		this.analyseHeaders(); 
		String out = new String();
		this.produceRunnungNumber();
		
		
		/////////////////////NODES///////////////////
		//write Header for Nodes
		out += "*Nodes "+  nodes.size()+"\n";
		out+="id*int\t";
		for (int i =0; i<nodestypes.size();i++) {
			Class type = (Class) nodestypes.get(i);
			String temp= InfereNWBOutType(type);
			
			out+="thing"+i+"*"+temp;
			
			if(i<(nodestypes.size()-1))

				out+= "\t";
			else
				out+= "\n";	
			
		}
		//Node data
		out+= this.nodesTabbed("\t","\n");
		
		
		//////////////EDGES ////////////////////////////
		
		// Edge Infos
		out+="*UndirectedEdges\n";
		for (int i =0; i<edgestypes.size();i++) {
			Class type = (Class) edgestypes.get(i);
			String temp= InfereNWBOutType(type);
			if(i==0)
				out+="source*"+temp;
			else if(i==1)
				out+="target*"+temp;
			else
				out+="thing"+i+"*"+temp;
			if(i<edgestypes.size()-1)
				out+= "\t";
			else
				out+= "\n";
			
		}
		
		
		// Write Edge Data
		out += edgesTabbed("\t","\n");
		return out;
		
	} 

	
	private void reduceNodes() {
		System.out.println("Reducing Nodes");
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
		System.out.println("reduced nodes :"+ (nodes.size()-newNodes.size()));
		nodes = newNodes;
	}

	public String PajekNet(){
		//http://netwiki.amath.unc.edu/DataFormats/PajekNetAndPajFormats
		useRunningNumber = true;
		if(justUseMentionedNodes)
			this.reduceNodes();
		this.analyseHeaders(); 
		String out = new String();
		this.produceRunnungNumber();
		///////////////NODES//////////////////
		//Node Headers
		out += "*Vertices "+  nodes.size()+" \n";
		//Node Data
		out += this.nodesTabbed(" "," \n");
		//////////////EDGES////////////////////
		//Edge Headers
		out+="*Edges "+edges.size()+ " \n";
		//Edges Data

		out+= this.edgesTabbed(" "," \n");

		return out;
	}
	
	public void GephiSetup(){
		useRunningNumber = true;
		if(justUseMentionedNodes)
			this.reduceNodes();
		
		this.analyseHeaders(); 
		String out = new String();
		this.produceRunnungNumber();
		
	}
	
	public String CSVNodes(){
		List<String> labels = GraphContainer.autoInitializeLabelsNodes(nodes);
		String out = new String();
		
		for (int i = 0; i < labels.size()-1; i++) {
			
			out += labels.get(i)+this.csvFieldSperator;
			
		}
		out += labels.get(labels.size()-1)+this.csvLineEnd;
		
		
		this.analyseHeaders(); 
		out += nodesTabbed(this.csvFieldSperator, this.csvLineEnd);
		
		return out;
	}
	
	public String CSVEdges(){
		List<String> labels = GraphContainer.autoInitializeLabelsEdges(edges);
		String out = new String();
		
		for (int i = 0; i < labels.size()-1; i++) {
			
			out += labels.get(i)+this.csvFieldSperator;
			
		}
		out += labels.get(labels.size()-1)+this.csvLineEnd;
		
		this.analyseHeaders(); 
		out += edgesTabbed(";", "\n");
		
		return out;
	}
	
	public String JungNet(){
		//TODO Everything
		
		return null;
	}
	public String JGraphNet(){
		//TODO Everything
		
		return null;
	}
	////////////Infere Nodes /Edges Attrubtes /////////////////////////////////
	private void analyseHeaders(){
		
		if(nodes != null && !edges.isEmpty() && nodes != null && !edges.isEmpty() ){
			
			Object[] nodesample = nodes.get(0);
			
			Object[] edgesample = edges.get(0);
			nodestypes = new ArrayList<Class>(nodesample.length);
			nodeAttributesSize = nodesample.length;
			
			edgestypes = new ArrayList<Class>(edgesample.length);
			edgeAttributesSize = edgesample.length;
			
			for (int i = 0; i < nodesample.length; i++) {
				
				Class temp = nodesample[i].getClass();
				nodestypes.add(temp);
			}
			
			for (int i = 0; i < edgesample.length; i++) {
				
				Class temp = edgesample[i].getClass();
				edgestypes.add(temp);
			}
			

		}
		
		
	}
	
	
	//Producing a Running Number form the IDS
	//This Esspecialy Important if the Interpreter needs Ids With out holes
	private void produceRunnungNumber() {
		translate = new HashMap<String,Long>();
		long runningnumber =0; 
		for (int i = 0; i < nodes.size(); i++) {
			Object[] element = nodes.get(i);
			runningnumber++;
			translate.put(element[0].toString(),runningnumber);
		}
		
	}
	/////////////FUNCTIONS For Tabbed Output pajek , NBW
	private StringBuffer nodesTabbed(String seperator,String terminator){
		StringBuffer nodesString = new StringBuffer();
		
		
		for (int i = 0; i < nodes.size(); i++) {
			Object[] element = nodes.get(i);
			if(useRunningNumber){
				nodesString.append(translate.get(element[0].toString())+seperator);
			}
			for (int j = 0; j < element.length; j++) {
				
				if(nodestypes.get(j).equals(String.class))
					nodesString.append( "\""+element[j].toString()+"\"");
				else
					nodesString.append(element[j].toString());
				if(j<element.length-1)
					nodesString.append(seperator);
				else
					nodesString.append( terminator);
				
			}
			
		}
		return nodesString;
	}
	
	private StringBuffer edgesTabbed(String seperator,String terminator){
		StringBuffer edgesString = new StringBuffer();
		for (int i = 0; i < edges.size(); i++) {
			Object[] element = edges.get(i);
			for (int j = 0; j < element.length; j++) {
				if(j==0 || j==1){
					if(useRunningNumber)
						edgesString.append(translate.get(element[j].toString()));
					else
						edgesString.append(element[j].toString());
				}else{
					if(edgestypes.get(j).equals(String.class))
						edgesString.append("\""+element[j].toString()+"\"");
					else
						edgesString.append(element[j].toString());
				}
				
				if(j<element.length-1)
					edgesString.append(seperator);
				else
					edgesString.append( terminator);
				
			}
			
		}
		return edgesString;
		
	}
	//////////////////////Type Specific Functions
	private String InfereNWBOutType(Class c){
		
		if(c.equals(String.class))
			return "string";
		if(c.equals(Integer.class) ||c.equals(int.class))
			return "int";

		if(c.equals(Long.class) ||c.equals(long.class))
			return "int";
		if(c.equals(Double.class) ||c.equals(double.class)|| c.equals(Float.class) ||c.equals(float.class))
			return "float";
		
		return "string";
	} 	
	
	
}
