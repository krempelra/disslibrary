package de.rkrempel.diss.core.exporters;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.rkrempel.diss.core.commontools.GraphContainer;
import org.apache.commons.lang3.StringEscapeUtils;

import de.rkrempel.diss.core.report.ReportWriter;

/**
 * GraphML XML exporter (GraphML)
 */
public class XMLStandardConverter extends StandardExporter {

	private GraphContainer g;
	private StringBuffer temp;
	private boolean useWriter ;
	private Writer chanalout;
	
	public  XMLStandardConverter(GraphContainer G){
		g=G;
		useWriter = false;
		
	}
	
	

	public StringBuffer getResAsStringBuffer( ){
		temp = new StringBuffer();
		
		//Construction
		
		//Write Header
		temp.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
		"<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://graphml.graphdrawing.org/xmlns http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd\">\n");
		
		//Write Comment with Report
		temp.append("<!--"+StringEscapeUtils.escapeXml(ReportWriter.getInstance().CloseReport())+"-->");
		
		
		graphMLSetup();
		//TODO Undirected Detect
		temp.append("<graph id=\"G\" edgedefault=\"undirected\">\n");
		graphMLContent();
		
		temp.append("</graph>\n"+
				"</graphml>\n");
		
		return temp;
	}
	
	
	public void writeResWithWriter(Writer out ) throws IOException{
		this.chanalout= out;
		useWriter = true;
		//Construction
		
		//Write Header
		chanalout.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
		"<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://graphml.graphdrawing.org/xmlns http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd\">\n");
		
		//Write Comment with Report
		chanalout.append("<!--"+StringEscapeUtils.escapeXml(ReportWriter.getInstance().CloseReport())+"-->");
		
		temp = new StringBuffer();
		graphMLSetup();
		chanalout.append(temp);
		temp = new StringBuffer();
		
		
		//TODO Undirected Detect
		chanalout.append("<graph id=\"G\" edgedefault=\"undirected\">\n");
		graphMLContent();
		chanalout.append(temp);
		temp = new StringBuffer();
		
		chanalout.append("</graph>\n"+
				"</graphml>\n");
		
	}
	
	
	
	private String graphMLInterpretType(Class c){
		if(Float.class ==c )
			return "float";
		if(Long.class ==c )
			return "long";
		if(String.class ==c )
			return "string";
		if(Integer.class ==c )
			return "int";
		if(Double.class ==c )
			return "double";
		if(Boolean.class ==c )
			return "boolean";
		else
			return null;
		
		
	}
	

	private void  graphMLContent(){
		
	
		List<Object[]> nodes = g.getNodes();
		int i=0;
		for (Iterator<Object[]> iterator = nodes.iterator(); iterator.hasNext();) {
			i++;
			Object[] objects = (Object[]) iterator.next();
			this.graphMLWriteNode(objects);
			       
			if(useWriter){
				try {
					chanalout.append(temp);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				temp = new StringBuffer();
			}
		}
		
		List<Object[]> edges = g.getEdges();
		i=0;
		for (Iterator<Object[]> iterator = edges.iterator(); iterator.hasNext();) {
			i++;
			Object[] objects = (Object[]) iterator.next();
		    this.graphMLWriteEdge(objects,i);
		    
		    if(useWriter){
		    	try {
					chanalout.append(temp);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				temp = new StringBuffer();
			}
		       
		}
	
	}
	
	
	private void graphMLWriteEdge(Object[] edge,int nth) {
		
		temp.append("<edge id=\"e"+edge[0]+"_"+edge[1]+"\" source=\""+edge[0]+"\" target=\""+edge[1]+"\">\n");
		
		for(int i =2; i< edge.length;i++ )		
			writeKey( "de"+i, edge[i]  );
			
			
				
		temp.append("</edge>\n");
		
	}


	private void graphMLWriteNode(Object[] node) {
		
		temp.append("<node id=\""+node[0].toString()+"\">\n");
			
		for(int i =1; i< node.length;i++ )		
			writeKey( "dn"+i, node[i]  );
			
			
				
		temp.append("</node>\n");
		
		
	}


	private void writeKey(String key, Object object) {
		//TODO Nicer
		if(object.getClass().equals(String.class) && !object.toString().contains("http://")){
			String aah = (String) object;
			
			Pattern replace = Pattern.compile("\\s+");
			Matcher matcher = replace.matcher(aah);
			aah =  matcher.replaceAll(" ");
			if(aah.length()>60)
				aah =aah.substring(0, 60);
			aah = StringEscapeUtils.escapeXml(aah);

			temp = temp.append("<data key=\""+key+"\">"+ aah +"</data>\n");
		}
		else{
			String aah = object.toString();
			aah = StringEscapeUtils.escapeXml(aah);
			temp = temp.append("<data key=\""+key+"\">"+ aah+"</data>\n");
		
		}
		
	}


	private void graphMLSetup(){
		
		
		List<Class> ntypes = g.getNodeTypes();
		List<String> nlabels = g.getNodeLabels();

		for(int i = 1;i< ntypes.size() ;i++){
			String type = graphMLInterpretType(ntypes.get(i));
			String label = nlabels.get(i);

			temp.append("<key id=\"dn"+i+"\" for=\"node\" attr.name=\""+label+"\" attr.type=\""+type+"\"/>\n");
			
				//TODO Default Value Inferencer	
				//"<default>yellow</default>"+
				//"</key>");

		}
		
		
		List<Class> etypes = g.getEdgeTypes();
		List<String> elabels = g.getEdgeLabels();
		
		for(int i = 2;i< etypes.size() ;i++){
			String type = graphMLInterpretType(etypes.get(i));
			String label = elabels.get(i);
			temp.append("<key id=\"de"+i+"\" for=\"edge\" attr.name=\""+label+"\" attr.type=\""+type+"\"/>\n");
			
				//TODO Default Value Inferencer	
				//"<default>yellow</default>"+
				//"</key>");
			
			
		}
		
	
	}
	
	
}
