package de.rkrempel.diss.core.converters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import de.rkrempel.diss.core.commontools.GraphContainer;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;

/**
 * This Class Converts Internal Format to The Gephi Toolkit Format
 *
 */
public class GraphContainerToGephi {

	public GraphContainerToGephi() {
		// TODO Auto-generated constructor stub
	}
	public static GraphModel GraphtoGephi(GraphContainer IN, GraphModel gephi ){
		
		
		List<Object[]> nodes = IN.getNodes();
		List<String> nodeLabels = IN.getNodeLabels();
		//List<Class> nodeTypes = IN.getNodeTypes();
		
		HashMap<Long,Node> nodelook = new HashMap<Long,Node>();
		
		for (Object[] inNode : nodes) {
			Node n0 = gephi.factory().newNode(inNode[0].toString());
			nodelook.put((Long)inNode[0], n0);
			for(int i=1; i< nodeLabels.size();i++){
				if("Label".equals(nodeLabels.get(i))){
					n0.getNodeData().setLabel(inNode[i].toString());
					//Debug

					//System.out.println("nodeLabel " );
				}
				else{
					n0.getAttributes().setValue(nodeLabels.get(i), inNode[i]);
					//Debug

					//System.out.println("node "+ nodeLabels.get(i));
				}
				gephi.getUndirectedGraph().addNode(n0);
			}
			
			
		}

        List<Object[]> edges = IN.getEdges();
        List<String> edgeLabels = IN.getEdgeLabels();
		//Debug

		/*for (int i = 0; i < edges.size(); i++) {
			System.out.println(edges.get(i)[0]+" "+edges.get(i)[1] );
		}*/
        //The Identifier of an Edge -> Important for Sigmajs Parsing!
        Integer counter =0;
        for (Object[] inEdge : edges) {
        	//String allelse = new String();
        	
        	
			Edge n0 = gephi.factory().newEdge(counter.toString(), nodelook.get( (Long) inEdge[0]), nodelook.get( (Long) inEdge[1]),1f, false);
			counter++;
			for(int i=2; i< edgeLabels.size();i++){
				if(HashSet.class.equals( inEdge[i].getClass()))
					n0.getAttributes().setValue(edgeLabels.get(i), inEdge[i].toString());
				else
					n0.getAttributes().setValue(edgeLabels.get(i), inEdge[i]);
				//Debug

				//allelse+= " "+ inEdge[i].toString();
				//System.out.println("edge "+ edgeLabels.get(i)+ "Value " + inEdge[i].toString() );
			}
			gephi.getUndirectedGraph().addEdge(n0);
			//Debug

			//System.out.println("EdgeCount gephi :"+gephi.getGraphVisible().getEdgeCount());
			//System.out.println( nodelook.get( (Long) inEdge[0])+"   "+ nodelook.get( (Long) inEdge[1])+" "+allelse);
		}
        //Debug
        /*
        System.out.println("EdgeCount GM :"+IN.getNumEdges());
        System.out.println("EdgeCount gephi :"+gephi.getGraphVisible().getEdgeCount());
        System.out.println("NodeCount GM :"+IN.getNumNodes());
        System.out.println("NodeCount gephi :"+gephi.getGraphVisible().getNodeCount());
        */
		return gephi;
		
		
		
		
	} 
	
	/**
	 * Attach Weighted Edges like in the GraphContainer Definition to a Gephi Graph.
	 * @param edges Edges in the Graph Containter Definition
	 * @param gephi The Graph Model of Gephi
	 * @return List of Edges which have stuff.
	 */
	
	public static List<Edge> AttachEdgesWeightetToGephi(List<Object[]> edges, GraphModel gephi ){
		List<Edge> out = new ArrayList<Edge>();
		
        for (Object[] inEdge : edges) {

			Edge e0 = gephi.factory().newEdge(gephi.getGraph().getNode( ((Long)inEdge[0]).intValue()), gephi.getGraph().getNode( ((Long)inEdge[1]).intValue()),((Number)inEdge[2]).floatValue(), false);
			
			for(int i=3; i< inEdge.length;i++){
				/*if(Long.class.equals( inEdge[i].getClass()))
					System.out.println("PRICK!");*/
				if(HashSet.class.equals( inEdge[i].getClass()))
					e0.getAttributes().setValue("Attribute "+i, inEdge[i].toString());
				else
					e0.getAttributes().setValue("Attribute "+i, inEdge[i]);
			}
			gephi.getUndirectedGraph().addEdge(e0);
			out.add(e0);
		}
		
		
		return out;
	}
	
}
