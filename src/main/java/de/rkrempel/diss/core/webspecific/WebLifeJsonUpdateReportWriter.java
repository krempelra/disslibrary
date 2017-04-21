package de.rkrempel.diss.core.webspecific;

import java.util.Date;

import de.rkrempel.diss.core.report.ReportWriter;

/**
 * This Writer should continous Write Reports to Show The Status of the Programm
 * @author rkrempel
 *
 */
public class WebLifeJsonUpdateReportWriter extends ReportWriter {
	protected String outputfile;
	
	
	WebLifeJsonUpdateReportWriter(String filename){
		super();
		outputfile = filename;
	}
	public void appendReportEvent(String toadd){
		report.append("\n"+sdf.format(new Date())+": \n");
		report.append(toadd);
		
		
	}
	
}
