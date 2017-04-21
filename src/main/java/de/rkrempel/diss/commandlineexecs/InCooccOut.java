package de.rkrempel.diss.commandlineexecs;

	import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

    import de.rkrempel.diss.core.commontools.TwoModeToOneModeNetworkConverter;
    import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import de.rkrempel.diss.core.report.ReportWriter;
	import au.com.bytecode.opencsv.CSVReader;

/**
 * Simple two-mode to One-Mode Converter tool
 */
public class InCooccOut extends InOut {

		public static void main(String[] args) throws  IOException, ParseException  {
			createOptions();
			translate1=null;
			translate2=null;
			CommandLineParser parser = new PosixParser();
			try {
				 cmd = parser.parse( options, args);
					
			} catch (Exception e) {
				System.out.println(e.getMessage());
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp( "2modeto1mode [Parameters]", options );
				
				return;
				
			}
			if(cmd.hasOption("h")){
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp( "2modeto1mode [Parameters]", options );
				
				return;
				
			}
			
			if(!cmd.hasOption("out")||!cmd.hasOption("e")){
				
				System.out.println("Necessary Options not Found!");
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp( "2modeto1mode [Parameters]", options );
				
				return;
			
			}
			
			String outfile =cmd.getOptionValue("out");
			ReportWriter.getInstance().appendReportEvent("Outfile Destination: " + outfile);
			
			String edgefile = cmd.getOptionValue("e");
			
			ReportWriter.getInstance().appendReportEvent("retrived Edges From: " + edgefile);
			String nodefile= "";

			if(cmd.hasOption("n") ){
				nodefile = cmd.getOptionValue("n");

				ReportWriter.getInstance().appendReportEvent("Retrived Nodes From" + nodefile);
			}
			String[] nextline = null;
			CSVReader reader;
			String Seperator;
			if(cmd.hasOption("sep")){
				Seperator = cmd.getOptionValue("sep");
				
			}else
				Seperator = ";";
			
			
			try {
				//List<Object[]> res = new ArrayList<Object[]>();
				reader = new CSVReader(new FileReader(edgefile), Seperator.charAt(0), '\n' );
			
				List<Long[]> res = new ArrayList<Long[]>();
				TwoModeToOneModeNetworkConverter ct = new TwoModeToOneModeNetworkConverter();
				//ct.weightInvertetbyNumberofLinkage();

    
				if(cmd.hasOption("textIdentifiers")){
					
						preprocessTextIdentifiers(reader, res);
					}else{
				
						processIntIdentifiers(reader, res);
				}
				List<Object[]> edges = ct.makeCTEdgesAllWeights(res);
				//System.out.println(edges.size());
				/*for (Object[] objects : edges) {
		    	System.out.println(objects[0].toString() );
				}*/
				reader.close();
				if(nodefile.isEmpty()&&!cmd.hasOption("textIdentifiers") )
					fileWriter(outfile , edges, null  );
				else
				{
					writeWithNodes(outfile, nodefile, edges);
				}
		    
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		protected static void createOptions() {
			options = new Options();
		
			// add t option
			Option inEdges   = OptionBuilder.withArgName( "csvFile" )
		            .hasArg()
		            .withDescription(  "The two Mode Network Edges. In General a 2 Column CSV. First column with Reference things(form edges) Second is the Coocuring IDS (Form Nodes). required" )
		            .create( "e" );
		
			Option textIdentifiers   = OptionBuilder.withDescription(  "The Edges are Identified by Strings rather than Numbers" )
		            .create( "textIdentifiers" );
			
			Option invertEdgeThing   = OptionBuilder.withDescription(  "Invert the Sequence of the Edges so that: First column is the Coocuring IDS (Form Nodes) and Second with Reference things(form edges)" )
		            .create( "InvEdgeParams" );
			
			
			Option Message   = OptionBuilder.withArgName( "Text" )
		            .hasArg()
		            .withDescription(  "Message that will be attached to the GraphML File as Comment for the Documentiation." )
		            .create( "m" );
			
		
			Option seperator   = OptionBuilder.withArgName( "Char" )
		            .hasArg()
		            .withDescription(  "Seperator used in the Input CSV File. Standard \";\"" )
		            .create( "sep" );
		
			Option nodeFiles   = OptionBuilder.withArgName( "csvFile" )
		            .hasArg()
		            .withDescription(  "Nodes of the Things that are Described. Optional" )
		            .create( "n" );
			Option destination   = OptionBuilder.withArgName( "out" )
		            .hasArg()
		            .withDescription(  "Where to put the Output .required" )
		            .create( "out" );
			options.addOption("h", false, "Show this!");
			options.addOption(destination);			
			options.addOption(inEdges);
			options.addOption(nodeFiles);
			options.addOption(seperator);
			options.addOption(Message);
			options.addOption(invertEdgeThing);
			options.addOption(textIdentifiers);
			
		}
		protected static List<String> costumLabels() {
			// TODO Auto-generated method stub
			return null;
		}
	}
