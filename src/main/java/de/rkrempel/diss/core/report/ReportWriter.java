package de.rkrempel.diss.core.report;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Report Writer Singleton is a Thing that Collects the Report Information for Appending it to the Data.The Form is Human Readable
 * The Report Writer tries to Collect Technical and Other informations The informations get Collected and Timestamped.
 * @author rasmus
 *
 */
public class ReportWriter {
	protected StringBuffer description;
	protected StringBuffer report;
	protected StringBuffer techReport;
	protected static String catBreak = "\n";
	protected Date StartofReportwriting;
	private static ReportWriter instance = null;
	protected SimpleDateFormat sdf;
	protected ReportWriter() {
		clear();
		
		}

	/**
	 * Clear Information
	 */
	public void clear(){
		
		report = new StringBuffer();
		description = new StringBuffer();
		techReport = new StringBuffer();
		StartofReportwriting = new Date();
		sdf = new SimpleDateFormat("HH:mm:ss.SSS");
		
	}

	/**
	 * Get Instance of Report Writer Singleton
	 * */
	public static ReportWriter getInstance() {
	      if(instance == null) {
	         instance = new ReportWriter();
	      }
	      return instance;
	   }
	/**
	 * Append to Event Log
	 * @param toadd Message to add
	 */
	public void appendReportEvent(String toadd){
		report.append("\n"+sdf.format(new Date())+": \n");
		report.append(toadd);
		
		
	}

	/**
	 * Append to TechEvent Log
	 * @param toadd Message to add
	 */
	public void appendTechEvent(String toadd){
		techReport.append("\n"+sdf.format(new Date())+": \n");
		techReport.append(toadd);

	}
	
	public void description(String toadd){
		description.append(toadd);
	}
	/**
	 * Close report and Flush to String
	 * @return A Formated Report to Add to Things
	 */
	public String CloseReport(){
		
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
		
		return outTmp.toString();
	}
	
	private static void SetWriterType(ReportWriter instance){
	
		instance = instance;
		
	}
	
	
}
