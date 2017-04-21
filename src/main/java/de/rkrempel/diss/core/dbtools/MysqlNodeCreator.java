package de.rkrempel.diss.core.dbtools;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import de.rkrempel.diss.core.report.ReportWriter;


public class MysqlNodeCreator {
	protected MYSQLRetriver retriver;
	protected String URIPrefix = "http://arachne.uni-koeln.de/entity/";
	
	
	public MysqlNodeCreator(MYSQLRetriver retriver) {
		this.retriver = retriver;
		// TODO Auto-generated constructor stub
	}
	/**
	 * 
	 * @param query This Query has to have 2 Things First must be an ID second A String as Label 
	 * @return Nodes where the First Part ob the Object Array is an Long the Second part is a String as Label
	 */
	public List<Object[]> createNodesByQuery(String query){
		List<Long> everything= null;
		List<String> everything1 = null;
		ReportWriter.getInstance().appendReportEvent("Creating Nodes for Graph");

		ReportWriter.getInstance().appendTechEvent("Creating Nodes for Graph Using Query: \n"+query);
		try {
			everything = retriver.getNthfieldsAsLong(1, query);
			everything1 = retriver.getNthfieldsAsString(2, query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<Object[]> nodes = new ArrayList<Object[]>(); 
		for (int i = 0; i < everything.size(); i++) {
			Object[] temp = new Object[2];
			temp[0]= everything.get(i);
			temp[1]= everything1.get(i);
			nodes.add(temp);
			
		}
		ReportWriter.getInstance().appendReportEvent("Nodes created : " + nodes.size());
	
		return nodes;
		
	}
	
	/**
	 * 
	 * @param ids are the Arachne Entity Ids.  
	 * @return Nodes where the First Part ob the Object Array is an Long the Second part is a String as Label
	 */
	public List<Object[]> createNodesByArachneEntityIds(List<Long> ids){
		/*List<Long> everything= null;
		List<String> everything1 = null;
		ReportWriter.getInstance().appendReportEvent("Creating Nodes for Graph");

		ReportWriter.getInstance().appendTechEvent("Creating Nodes for from list of EntityIDs: \n size: "+ids.size()+"\n");
		try {
			everything = retriver.getNthfieldsAsLong(1, );
			everything1 = retriver.getNthfieldsAsString(2, query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<Object[]> nodes = new ArrayList<Object[]>(); 
		for (int i = 0; i < everything.size(); i++) {
			Object[] temp = new Object[2];
			temp[0]= everything.get(i);
			temp[1]= everything1.get(i);
			nodes.add(temp);
			
		}
		ReportWriter.getInstance().appendReportEvent("Nodes created : " + nodes.size());
		 */
		
		List<Object[]> nodes = new ArrayList<Object[]>(); 
		
		for (Long objs : ids) {
			
			Object[] node = new Object[2];
			node[0]=objs;
			node[1]=new String(URIPrefix+objs);
			nodes.add(node);
		}
		
		
		return nodes;
		
	}
	
	/**
	 * 
	 * @param allNodeIdsinSets are the Arachne Entity Ids.
	 * @return Nodes where the First Part ob the Object Array is an Long the Second part is a String as Label
	 */
	public List<Object[]> createNodesByArachneEntityGroupsIds(List<List<Long>> allNodeIdsinSets){

		
		List<Object[]> nodes = new ArrayList<Object[]>(); 
		
		for (int i=0;i<allNodeIdsinSets.size();i++) {
			for(int j=0;j<allNodeIdsinSets.get(i).size();j++){
				

				Object[] node = new Object[3];
				node[0]=allNodeIdsinSets.get(i).get(j);
				node[1]=new String(URIPrefix+allNodeIdsinSets.get(i).get(j));
				node[2]=new Integer(i);
				nodes.add(node);	
				
			}
			
		}
		
		
		return nodes;
		
	}
	/**
	 * 
	 * @param allNodeIdsinSets are the Arachne Entity Ids.
	 * @param marker An Integer that Marks the Group of the Nodes
	 * @return Nodes where the First Part ob the Object Array is an Long the Second part is a String as Label
	 */
	public List<Object[]> createNodesByArachneEntityGroupsIds(List<Long> allNodeIdsinSets,int marker ){

		
		List<Object[]> nodes = new ArrayList<Object[]>(); 
		
		for (int i=0;i<allNodeIdsinSets.size();i++) {

				Object[] node = new Object[3];
				node[0]=allNodeIdsinSets.get(i);
				node[1]=new String(URIPrefix+allNodeIdsinSets.get(i));
				node[2]=new Integer(marker);
				nodes.add(node);	
				
			}
		
		
		
		return nodes;
		
	}
	public List<Object[]> createNodesByArachneEntityGroupsIds(List<Long> allNodeIdsinSets,List<String> allNodeLabelsinSets ,int marker ){

		
		List<Object[]> nodes = new ArrayList<Object[]>(); 
		
		for (int i=0;i<allNodeIdsinSets.size();i++) {

				Object[] node = new Object[4];
				node[0]=allNodeIdsinSets.get(i);
				node[1]=new String(URIPrefix+allNodeIdsinSets.get(i));
				node[2]=new Integer(marker);
				node[3]= allNodeLabelsinSets.get(i);
				nodes.add(node);	
				
			}
		
		
		
		return nodes;
		
	}
	public String getURIPrefix() {
		return URIPrefix;
	}
	public void setURIPrefix(String uRIPrefix) {
		URIPrefix = uRIPrefix;
	}
	
}
