package de.rkrempel.diss.core.exporters;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;

import de.rkrempel.diss.core.commontools.GraphContainer;

/**
 * Exports Graph to to CSV
 */
public class CSVStandardExporter extends StandardExporter {
	private GraphContainer g;
	private StringBuffer temp;
	private StringBuffer tempNodes;
	private boolean useWriter ;
	private Writer chanalout;
	protected String Seperator;
	
	public CSVStandardExporter(GraphContainer toExport) {
		g= toExport;
		Seperator = ";";
	}
	@Override
	public StringBuffer getResAsStringBuffer() {
		temp = new StringBuffer();
		
		
		createEdgeHeaders();
		createEdges();
		
		
		return temp;
	}
	
	public StringBuffer getNodesAsStringBuffer() {
		tempNodes = new StringBuffer();
		
		
		createNodeHeaders();
		createNodes();
		
		
		return temp;
	}

	private void writeBody() {
		
		
		createNodes();
		
		createEdges();

			
	}
	protected void createEdges() {
		List<Object[]> edges = g.getEdges();

		for (Iterator<Object[]> iterator = edges.iterator(); iterator.hasNext();) {

			Object[] edge = (Object[]) iterator.next();
		    
			temp.append(edge[0]+Seperator+edge[1]);
			
			
			if(2< edge.length-1 )
				temp.append(Seperator);
			else
				temp.append("\n");
			
			for(int j =2; j< edge.length;j++ ){
				temp.append(edge[j]);
				
				if(j< edge.length-1 )
					temp.append(Seperator);
				else
					temp.append("\n");
			}
		    if(useWriter){
		    	try {
					chanalout.append(temp);
				} catch (IOException e) {
					e.printStackTrace();
				}
				temp = new StringBuffer();
			}

		       
		}
	}
	protected void createNodes() {
		List<Object[]> nodes = g.getNodes();

		for (Iterator<Object[]> iterator = nodes.iterator(); iterator.hasNext();) {

			Object[] node = (Object[]) iterator.next();

			for(int j =0; j< node.length;j++ ){
				tempNodes.append(node[j]);
				
				if(j< node.length-1 )
					tempNodes.append(Seperator);
				else
					tempNodes.append("\n");
			}
			
			if(useWriter){
		    	try {
					chanalout.append(tempNodes);
				} catch (IOException e) {
					e.printStackTrace();
				}
		    	tempNodes = new StringBuffer();
			}
			

		       
		}
	}
	
	private void writeHeader() {
		//TODO InterpretTypes
		
		createNodeHeaders();
		
		
		createEdgeHeaders();
		
	}
	protected void createEdgeHeaders() {
		List<Class> etypes = g.getEdgeTypes();
		List<String> elabels = g.getEdgeLabels();
		
		temp.append("source"+Seperator+"target");
		if(2<etypes.size())
			temp.append(Seperator);
		else
			temp.append("\n");
		for(int i = 2;i< etypes.size() ;i++){
			String label = elabels.get(i);
			if(i< etypes.size()-1 )
				temp.append(label+Seperator);
			else
				temp.append(label+"\n");
			
			if(useWriter){
		    	try {
					chanalout.append(temp);
				} catch (IOException e) {
					e.printStackTrace();
				}
		    	temp = new StringBuffer();
			}
			
		}
	}
	protected void createNodeHeaders() {
		List<Class> ntypes = g.getNodeTypes();
		List<String> nlabels = g.getNodeLabels();
		
		for(int i = 0;i< ntypes.size() ;i++){
			
			String label = nlabels.get(i);
			if(i< ntypes.size()-1 )
				tempNodes.append(label+Seperator);
			else
				tempNodes.append(label+"\n");
			
			
			if(useWriter){
		    	try {
					chanalout.append(tempNodes);
				} catch (IOException e) {
					e.printStackTrace();
				}
		    	tempNodes = new StringBuffer();
			}
		}
	}
	@Override
	public void writeResWithWriter(Writer out) throws IOException {
		useWriter =true;
		chanalout = out;
		getResAsStringBuffer();
		useWriter =false;
	}
	
	public void writeNodesWithWriter(Writer out) throws IOException {
		useWriter =true;
		chanalout = out;
		getNodesAsStringBuffer();
		useWriter =false;
	}
	public void setSeperator(String seperator) {
		Seperator = seperator;
	}
	
	
}
