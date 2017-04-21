package de.rkrempel.diss.core.dbtools;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.rkrempel.diss.core.report.ReportWriter;

import de.rkrempel.diss.core.commontools.EdgeSorter;
import de.rkrempel.diss.core.commontools.SortTableByNthField;
import de.rkrempel.diss.core.commontools.TwoModeToOneModeNetworkConverter;

/**
 * This Object is a Wrapper class to evaluate the Internal Connection structure of the arachne Database
 */
public class SemanticConnectionMysqlCorelationGetter {

	protected MYSQLRetriver retriver;
	protected String query;
	protected String query2;
	protected List<Object[]> Edges;
	protected ArrayList<Long[]> sqlresult= null;
	private float minimumRelevance;
	public SemanticConnectionMysqlCorelationGetter(String Targettype, String Coocurencetype,MYSQLRetriver ret) {
		
		query = BuildMysql( Targettype,  Coocurencetype);
		
		retriver =ret;
		
	}
	public SemanticConnectionMysqlCorelationGetter(String Targettype,String TargetAccumulation, String Coocurencetype,MYSQLRetriver ret) {
		
		query = BuildMysql( Targettype,  TargetAccumulation);
		query2 = BuildMysql(  Coocurencetype,TargetAccumulation);
		retriver =ret;
		
	}
	
	public SemanticConnectionMysqlCorelationGetter(MYSQLRetriver ret) {

		
		retriver =ret;
		
	}
	public SemanticConnectionMysqlCorelationGetter(String query,MYSQLRetriver ret) {
		
		this.query = query;
		
		retriver =ret;
		
	}
	
	
	
	/**
	 * This Function Creates a Function to Build a Statement to Create a Network from a Crosstable
	 * 
	 * Example
	 * TargetID is FS_ObjektID
	 * CoocurenceID is FS_OrtID
	 * CrossTableName is ortsbezug
	 * 
	 * @param TargetID NAme of The ID OF The Things that will form the Nodes
	 * @param CoocurenceID Name of ID That form the Edges
	 * @param CrossTableName The name of the CrossTable
	 * @return Returns a Query String for Processing to a Network
	 */
	
	protected static String BuildMysql(String TargetType, String CoocurenceType){
		
		return "SELECT Source, Target FROM  SemanticConnection WHERE  	TypeSource = '"+TargetType+"' AND TypeTarget = '"+CoocurenceType+"'  ORDER BY Target,Source ASC ;";
		
		
	}
	
	protected void createSQLResult() throws SQLException{
		List<Long> temp = retriver.getNthfieldsAsLong(1, query);
		List<Long> temp2 = retriver.getNthfieldsAsLong(2, query);
		
		
		
		
		this.sqlresult = new ArrayList<Long[]>(temp.size());
		for (int i = 0; i < temp.size(); i++) {
		
			
			
			Long[] vec = new Long[2];
			vec[0]=new Long(temp.get(i));
			vec[1]=new Long(temp2.get(i));
			sqlresult.add(vec);
		}
		
	}
	
	protected void createCrossTableNet(){
		ReportWriter.getInstance().appendReportEvent("Appending Result");
		ReportWriter.getInstance().appendTechEvent("Retriving By query \n"+query);
		if(sqlresult == null){
		try {
				this.createSQLResult();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		ReportWriter.getInstance().appendTechEvent("queryFinished");
		
		
		TwoModeToOneModeNetworkConverter twoModeToOneMode = new TwoModeToOneModeNetworkConverter();
		if(minimumRelevance != 0.0f)
			twoModeToOneMode.setMinimumRelevance(minimumRelevance);
		Edges = twoModeToOneMode.makeCTEdgesAllWeights(sqlresult);
		

	}
	
	public void setMinimumRelevance(float minimumRelevance) {
		this.minimumRelevance = minimumRelevance;
	}
	public Object[][] oneModeAsMultiArray(){
		createCrossTableNet();
		Object[][] theBigone = new Object[Edges.size()][3];
		for (int i = 0; i < Edges.size(); i++) {
			Object[] oldvec = Edges.get(i);
			
			theBigone[i]= (Object[])oldvec;
			
			
		}
		return theBigone;
	}
	public List<Object[]> oneModeAsListofArrays(){
		createCrossTableNet();
		return (List<Object[]>) Edges;
	}
	
	public List<Long[]> twoModeAsListofArrays(){
		try {
			this.createSQLResult();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.sqlresult;
	}
	
	public List<Long[]> twoModeAsListOverOtherofArrays(){
		
		
		
		try {
			System.out.println(query);
			this.createSQLResult();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<Long[]> res = this.sqlresult;
		if(res.isEmpty())
			return new ArrayList<Long[]>();
		query = query2;
		try {
			System.out.println(query);
			this.createSQLResult();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(this.sqlresult.isEmpty())
			return new ArrayList<Long[]>();
		//temp= connectSimplepathJoin( temp,  this.sqlresult);
		List<Long[]> temp = new ArrayList<Long[]>();
		temp=connectComplexpathJoin( res,  this.sqlresult);
		res.clear();
		Collections.sort(temp, new SortTableByNthField(0));
		Collections.sort(temp, new SortTableByNthField(1));
		
		res = EdgeSorter.addupherenewlistLong(temp);
		temp.clear();
		return res;
	}
	
	private List<Long[]> connectComplexpathJoin(List<Long[]> one, List<Long[]> two){
		List<Long[]> out = new ArrayList<Long[]>();
		int counter1 = 0;
		int counter2 = 0;
		List<Long> tempConnect1= new ArrayList<Long>(10);
		List<Long> tempConnect2= new ArrayList<Long>(10);
		
		
		
		while( counter1< one.size() && counter2< two.size() ){
			int comparison = one.get(counter1)[1].compareTo(two.get(counter2)[1]);
			
					
			if( comparison ==0 ){
				Long space = one.get(counter1)[1];
				
				while( counter1< one.size() && one.get(counter1)[1].equals(space)){
					tempConnect1.add(one.get(counter1)[0]);
					counter1++;
				}
				
				while(counter2< two.size() && two.get(counter2)[1].equals(space)  ){
					tempConnect2.add(two.get(counter2)[0]);
					counter2++;
				}
				
				for (int i = 0; i < tempConnect1.size(); i++) {
					for (int j = 0; j < tempConnect2.size(); j++) {
						Long[] tmpout = new Long[2];
						
						tmpout[0]= tempConnect1.get(i);
						tmpout[1]= tempConnect2.get(j);
						out.add(tmpout);
					}
					
				}
				tempConnect1.clear();
				tempConnect2.clear();
				
			}else if(comparison <0 ){
				//Iterate first Counter
				counter1++;
				
			}else if(comparison >0 ){
				//Iterate first Counter
				counter2++;
			}	
		}
		
		return out;
	}
	
	
	private List<Long[]> connectSimplepathJoin(List<Long[]> one, List<Long[]> two){
		//Just runs with the Sorting of 3 String Constructor;
		/*
		
		This Expects Two Things
		
		like
		one:
		Whole - Parts
		two:
		toConnectto -Parts
		
		one:
		Buch - Buchseite
		two:
		Objekt - Buchseite
		
		Buchseiten werden durch Buch Ids Verbunden.
		
		
		Beide Listen sind nach Parts sortiert
		Vorgehen:
		
		two Durchlaufen und Paralell zu One Durchlaufen,
		
		
		
		
		
		*/
		List<Long[]> out = new ArrayList<Long[]>(two.size());
		Long tempPartID = new Long(0); 
		Long tempMainID = new Long(0);
		int runningnumber=  0;
		for (int i = 0; i < two.size(); i++) {
	
			Long id = two.get(i)[1];
			if(tempPartID.equals( id) ){
				Long[] tmp = new Long[2];
				
				tmp[0] = two.get(i)[0];
				
				tmp[1] = tempMainID;
				out.add(tmp);
				
				
			}else{
				for (; runningnumber <one.size(); runningnumber++) {
					if(one.get(runningnumber)[1].equals(id)){
						
						tempPartID =one.get(runningnumber)[1]; 
						tempMainID = one.get(runningnumber)[0];
						//two.get(i)[1] = tempMainID;
						Long[] tmp = new Long[2];
						
						tmp[0] = two.get(i)[0];
						
						tmp[1] = tempMainID;
						out.add(tmp);
						break;
					}
				}
			}
		}
		return out;
	}
	
	
	
	
}
