package de.rkrempel.diss.commandlineexecs;

import java.io.IOException;

import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

/**
 * Process Paralells in Nets
 */
public class ParallelNets extends InOut {

	
	public static void main(String[] args) throws  IOException, ParseException  {
		createOptions();

		CommandLineParser parser = new PosixParser();
		try {
			 cmd = parser.parse( options, args);
				
		} catch (Exception e) {
			System.out.println(e.getMessage());
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp( "Parallel [Parameters]", options );
			
			return;
			
		}
	}
	
	protected static void createOptions() {
		options = new Options();

		// add t option
		Option inEdges1   = OptionBuilder.withArgName( "csvFile" )
                .hasArg()
                .withDescription(  "(Required) The First(1.) Input File" )
                .create( "e1" );

		// add t option
		Option inEdges2   = OptionBuilder.withArgName( "csvFile" )
                .hasArg()
                .withDescription(  "(Required) The Second(2.) Input File" )
                .create( "e1" );
		
		

		Option nodeFilesDescs1   = OptionBuilder.withArgName( "NamedNet" )
                .hasArg()
                .withDescription(  "Description of  the Columns in th Edges Lists. Leave blank von one modeNet Standard Group Nodes" +
                		"For naming add 'FirstSet,SecondSet' Name or ',TheOtherThing' " +
                		"Same groups will be used to the set of Names" )
                .create( "n1" );
		
		Option nodeFilesDescs2   = OptionBuilder.withArgName( "NamedNet" )
                .hasArg()
                .withDescription(  "Description of  the Columns in th Edges Lists. Leaf Blank von One modeNet Standard Group Nodes" +
                		"For naming add 'FirstSet,SecondSet' Name or ',TheOtherThing' The Nothing is the DefaultSet" +
                		"Same groups will be used to the set of Names" )
                .create( "n1" );
		
		
		
		Option nodeFiles1   = OptionBuilder.withArgName( "csvFile" )
                .hasArg()
                .withDescription(  "(Optional) Nodes of First Net (1.)" )
                .create( "n1" );
		

		Option nodeFiles2   = OptionBuilder.withArgName( "csvFile" )
                .hasArg()
                .withDescription(  "(Optional) Nodes of Second Net (2.)" )
                .create( "n2" );
		

		
		
		//Option textIdentifiers   = OptionBuilder.withDescription(  "The Edges are Identified by Strings rather than Numbers" )
        //        .create( "textIdentifiers" );	
		Option destination   = OptionBuilder.withArgName( "out" )
                .hasArg()
                .withDescription(  "(Required)Where to put the Output This is The Base Name Everything will be created after this.\n like: <out>OneMode_Second.graphml " )
                .create( "out" );
		
		Option Message   = OptionBuilder.withArgName( "Text" )
                .hasArg()
                .withDescription(  "(Optional)Message that will be attached to the GraphML File as Comment for the Documentiation." )
                .create( "m" );
		

		Option seperator   = OptionBuilder.withArgName( "Char" )
                .hasArg()
                .withDescription(  "(Optional)Seperator used in the Input CSV File. Standard \";\"" )
                .create( "sep" );


		
		Option OutputType   = OptionBuilder.withArgName( "Type" )
                .hasArg()
                .withDescription(  "(Optional)This is the Output Format of the Files, possible: csv, graphml  Standard: graphml," )
                .create( "outtype" );
		
		options.addOption("h", false, "Show this!");
		options.addOption(inEdges1);
		options.addOption(inEdges2);
		options.addOption(destination);			
		options.addOption(nodeFiles1);
		options.addOption(nodeFiles2);
	
		options.addOption(seperator);
		options.addOption(Message);
		options.addOption(OutputType);
		//options.addOption(textIdentifiers);
		
	}
	
	
}
