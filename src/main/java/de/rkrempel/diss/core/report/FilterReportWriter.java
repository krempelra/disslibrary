
package de.rkrempel.diss.core.report;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.rkrempel.diss.core.commontools.CooccuredMetaEdge;

/**
 * @author rasmus
 * This is a Report Writer for Reporting the Stuff for the Context Exports
 * FEATRUES: 
 * Can hold Filterd Facts Like the meta Edges comming from the Filter
 * Should provide Output in Report Files on Disk
 */
public class FilterReportWriter extends ReportWriter {
	
	protected StringBuffer activeReport;
	private static FilterReportWriter instance = null;
	private String ActiveReportFileName;
	private PrintStream printStream = null;

	List<CooccuredMetaEdge> FilteredEdgeStuff;
	
	
	/**
	 * Singleton
	 */
	protected FilterReportWriter() {
		clear();
		
		}
	public static FilterReportWriter getInstance() {
	      if(instance == null) {
	         instance = new FilterReportWriter();
	      }
	      return instance;
	   }

	@Override
	public void clear() {
		super.clear();
		FilteredEdgeStuff = new LinkedList<CooccuredMetaEdge>();
		activeReport = new StringBuffer();
	}
	/**
	 * This Starts an Active Report Session
	 * @param repFile This is the Open File in Which will be Written
	 * @return Returns false when the Stream couldnt be Established
	 */

	public boolean setActiveReportFile(String repFile){
		if(printStream != null)
			printStream.close();
		
		ActiveReportFileName = repFile;
		
		FileOutputStream os;
		try {
			os = new FileOutputStream(ActiveReportFileName);
		} catch (FileNotFoundException e) {
			return false;
		}
		printStream = new PrintStream(os);
		
		return true;
		
	}

	public void appendActiveEvent(String toadd){
		activeReport.append("\n"+sdf.format(new Date())+": \n");
		activeReport.append(toadd);
		if(printStream != null)
			printStream.print(toadd+ "\n");
	
	}
	
	/**
	 * This Ends the Active Stream outside
	 */
	
	public void endActiveReport(){
		if(printStream != null){
			printStream.print("THE END"+ "\n");
			printStream.close();
		}
	}
	/**
	 * 
	 * @return A Formated Report to Add to Things
	 */
	public String CloseReport(){
		endActiveReport();
		return FlushReportPlainText();
		
	}
	private String FlushReportPlainText(){
		
		StringBuffer outTmp = new StringBuffer();
		outTmp.append(catBreak+"Report from : " + StartofReportwriting.toString()+catBreak +
				"Report start : " + sdf.format(StartofReportwriting)+catBreak +
				" Report end : " + sdf.format(new Date())+catBreak);

		outTmp.append("Description: "+ catBreak);
		outTmp.append(description);
		outTmp.append(catBreak+catBreak+catBreak);

		outTmp.append("Report: " +catBreak);
		outTmp.append(report);
		outTmp.append(catBreak+catBreak+catBreak);
		outTmp.append("Technical report: " +catBreak);
		outTmp.append(techReport);
		outTmp.append(catBreak);
		outTmp.append(catBreak+"REPORT END"+catBreak);
		outTmp.append("Active report: " +catBreak);
		outTmp.append(activeReport);
		outTmp.append(catBreak);
		if(FilteredEdgeStuff.size()>0){
			outTmp.append(catBreak+"Meta Edges"+catBreak);
			for (Iterator iterator = FilteredEdgeStuff.iterator(); iterator.hasNext();) {
				outTmp.append(iterator.next() +catBreak);
			}	
		}

		outTmp.append(catBreak+"REPORT END"+catBreak);
		return outTmp.toString();
	}
	
	public StringBuffer getActiveReport(){
		StringBuffer outTmp = new StringBuffer();
		outTmp.append("Active report: " +catBreak);
		outTmp.append(activeReport);
		outTmp.append(catBreak);
		return outTmp;
		
	}
	
	////////////////////////META EDGE Storing
	
	public void addMetaEdge(CooccuredMetaEdge in){
		FilteredEdgeStuff.add(in);
		
	}
	
	public List<CooccuredMetaEdge> getMetaEdges(){
		
		return FilteredEdgeStuff;
		
	}
	public List<Object[]> getMetaEdgesAsGraphContainerEdges(){
		List<Object[]> out = new ArrayList<Object[]>();

		for (CooccuredMetaEdge meta :  FilteredEdgeStuff) {
			Object[] temp = new Object[3];
			temp[0]= meta.source;
			temp[1]= meta.target;
			temp[2]= meta.relation;
			out.add(temp);
		}
		
		return out;
		
	}
	
}
