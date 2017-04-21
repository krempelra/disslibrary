package de.rkrempel.diss.commandlineexecs;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.rkrempel.diss.core.commontools.GraphContainer;
import de.rkrempel.diss.core.commontools.TwoModeToOneModeNetworkConverter;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.lang3.math.NumberUtils;

import de.rkrempel.diss.core.report.ReportWriter;
import au.com.bytecode.opencsv.CSVReader;
import de.rkrempel.diss.core.commontools.SortTableByNthField;

/**
 * This is the main project-executable for two-mode projections.
 * For Instructions howto Use See help on execution!
 */
public class InCoocOutMultiExport extends InCooccOut {
	protected static int endFieldSizeEdge=6;
	//TODO Identifier Verbessern(ID und MD5(oder ein anderer Hash) des Ausführenden Jars) und in Output schreiben
	//TODO Matrix Funktionen und Weights einbauen
	public static void main(String[] args) throws  IOException, ParseException  {
		createOptions();
		boolean headline =false;
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
		if(!cmd.hasOption("headline")){
			
			headline =true;
					System.out.println( "HasHeadline");
		}
			
		
		float minimumUniquity = 1.0f/1000.0f;
		String outfile =cmd.getOptionValue("out");
		ReportWriter.getInstance().appendReportEvent("Outfile Destination_prefix: " + outfile);
		
		String edgefile = cmd.getOptionValue("e");
		
		ReportWriter.getInstance().appendReportEvent("retrived Edges From: " + edgefile);
		String nodefile= "";

		if(cmd.hasOption("n") ){
			nodefile = cmd.getOptionValue("n");

			ReportWriter.getInstance().appendReportEvent("Retrived Nodes From:" + nodefile);
		}
		String[] nextline = null;
		CSVReader reader;
		String Seperator;
		if(cmd.hasOption("sep")){
			Seperator = cmd.getOptionValue("sep");
			
		}else
			Seperator = ";";
		System.out.println("Reading File");
		String[] nodeheadlines=null;
		String[] edgeheadlines=null;
		int skip =0;
		if(headline)
			skip =1;
		try {
			//List<Object[]> res = new ArrayList<Object[]>();
			
			if(headline){
				reader = new CSVReader(new FileReader(edgefile), Seperator.charAt(0), '\n', skip );
				edgeheadlines= reader.readNext();
				
				
			}
			
			
			reader = new CSVReader(new FileReader(edgefile), Seperator.charAt(0), '\n', skip );

			
			
			List<Long[]> res = new ArrayList<Long[]>();
			TwoModeToOneModeNetworkConverter ct = new TwoModeToOneModeNetworkConverter();
			//ct.weightInvertetbyNumberofLinkage();

				
				preprocessTextIdentifiers(reader, res);


				List<List<Long>> allNodeIdsinSets= new ArrayList<List<Long>>();
				
				StringBuilder localDescription = new StringBuilder();
				
				StringBuilder Description = new StringBuilder();
				
				ReportWriter.getInstance().appendTechEvent("Started Constructing");

				
				
				///////////////////Twomode Start
				List<Long> connectedNodes = null;
				List<Object[]>  Nodes1 = null;
				List<Object[]>  Nodes2 = null;
				
				String twoModeFileName="";
				
				Long nodessize =new Long(0);
				Long nodessize1 =new Long(0);
				Long nodessize2 =new Long(0);
				
	
				
				List<Long[]> twoMode = res;
				System.out.println("Two Mode Retrived");
				ArrayList<Object[]> newLinksListComplete = new ArrayList<Object[]>(twoMode.size());
				
				if(twoMode.isEmpty()){
					System.out.println("Its Empty Nothing Here!");
					return;
					
				}
				System.out.println("Create");
								
				System.out.println("Create nodessize");
				System.out.println( translate1.size());
				System.out.println(translate2.size());
				
				
				nodessize = new Long(translate1.size()+ translate2.size());
				
				System.out.println("Create nodessize1");
				nodessize1 = new Long(translate1.size());
				
				Map<String,List<Object>> nods =  new HashMap<String,List<Object>>();
				int nodlen = 0;
				if(nodefile != ""){
					CSVReader Nodereader = new CSVReader(new FileReader(nodefile), Seperator.charAt(0), '\n' );
					boolean one = true;
					Class[] Typeinfer = null;
					int count=0;
					//Inference of Types From Nodes
					while ((nextline = Nodereader.readNext()  ) != null&& count < 10) {
						if(one){
							Typeinfer = new Class[nextline.length];
							nodlen = nextline.length-1;
							for(int j=0;j<Typeinfer.length;j++)
								Typeinfer[j]=null;
							if(headline){
								nodeheadlines = new String[nextline.length+2];
								nodeheadlines[0] ="ID";
								nodeheadlines[1]="OldID";
								nodeheadlines[2]="type";

								for(int i=1;i<nextline.length;i++){
									nodeheadlines[i+2] =nextline[i];
								}
								
								for (int i = 0; i < nodeheadlines.length; i++) {
									System.out.println(nodeheadlines[i]);
								}
							}
							one = false;
							//Start from second Line
							continue;
						}
						
						for(int i=0;i<nextline.length;i++){
							
							Class Infer=null;
							
								
							if(NumberUtils.isNumber(nextline[i])){
								if(nextline[i].contains(".") ||nextline[i].contains(",") )
									Infer =Float.class;
								else
									Infer =Integer.class;
							}else Infer= String.class;
							
							if(Typeinfer[i] != null && Infer != Typeinfer[i] ){
								String msg = "Problems Infering Type from Node File"+ Infer.toString() +" VS " +Typeinfer[i].toString()+ " at Line "+ i+" Whith Value "+ Typeinfer[i];
								System.out.println(msg);
								//System.out.println(label1+"-"+label2+"-"+label1 +"\n");
							
								Description.append(msg);
								//localDescription.append(label1+"-"+label2+"-"+label1 +"\n");
								//Description.append(localDescription);
							
								ReportWriter.getInstance().appendTechEvent(msg);
							}
							Typeinfer[i] = Infer;
						}
						count ++;
					}
					
					Nodereader.close();

					Nodereader = new CSVReader(new FileReader(nodefile), Seperator.charAt(0), '\n', skip );
					while ((nextline = Nodereader.readNext()  ) != null) {
						List<Object> temp = new ArrayList<Object>(nextline.length-1);
						for (int i = 1; i < nextline.length; i++) {
							
							
							String Value = nextline[i];
							
							if(Typeinfer[i]== String.class)
								temp.add( Value);
							else if(Typeinfer[i]== Integer.class)
								temp.add( new Integer(Value));
							else if(Typeinfer[i]== Float.class)
								temp.add( new Float(Value));
							else
								temp.add( Value);
							
						}
						nods.put(nextline[0],temp);
						
					}
					
				}
				System.out.println("Create Nodes1");
				if(nodefile != ""){
					Nodes1= createNodesWithGroupsAndFile(translate1,1,nods,nodlen);
					
				}
				else Nodes1= createNodesWithGroups(translate1,1);
				
				 //Second Type Nodes
				
				System.out.println("Create nodessize2");
				nodessize2 = (long) translate2.size();
				System.out.println("Create Nodes2");
				if(nodefile != ""){
					Nodes2= createNodesWithGroupsAndFile(translate2,2,nods,nodlen);
					
				}else Nodes2= createNodesWithGroups(translate2,2);
				
				
				
				System.out.println("Created!");
				
				
				for (int i = 0; i < twoMode.size(); i++) {
					
					Object[] insert = new Object[endFieldSizeEdge];
					insert[0]= twoMode.get(i)[0];
					insert[1]= twoMode.get(i)[1];
					
					for (int k = 2; k < endFieldSizeEdge-1 ; k++) {
						insert[k] = new Float( 1.0f);
					}
					
					//Mark The Two Mode Network with 0;
					insert[endFieldSizeEdge-1] =new Integer(0);
					newLinksListComplete.add(insert);
				}
				
				String desc = GraphContainer.ShowEdgelistAttributes(newLinksListComplete, nodessize);
				localDescription.append(desc+"\n");
				
				//Create Folder
				
				
				List<Object[]>allNodes = new ArrayList<Object[]>(Nodes1.size()+Nodes2.size());
				allNodes.addAll(Nodes1);
				allNodes.addAll(Nodes2);
				
				twoModeFileName = "twoMode" ;
				
				//ReportWriter.getInstance().description(PreambleTwoMode+localDescription.toString());
				NetOutput(newLinksListComplete,allNodes,  "", outfile, twoModeFileName,nodeheadlines,costumLabs());
				
				allNodes.clear();
				newLinksListComplete.clear();
				
				Description.append(localDescription);
				localDescription = new StringBuilder();
				ReportWriter.getInstance().clear();
				
				System.out.println( desc);
				
				//////////////////Twomode End

				System.out.println("<<< One Modes" );
				//System.out.println(label1+"-"+label2+"-"+label1 +"\n");
				
				Description.append("<<< One Modes\n");
				//localDescription.append(label1+"-"+label2+"-"+label1 +"\n");
				//Description.append(localDescription);
				
				ReportWriter.getInstance().appendTechEvent("Started Constructing");
				ReportWriter.getInstance().appendTechEvent("Source: see File "+twoModeFileName);
				
				//System.out.println(label1+"-"+label2+"-"+label1+"\n" );
				TwoModeToOneModeNetworkConverter twoModeToOneMode = new TwoModeToOneModeNetworkConverter();
				
				if(minimumUniquity != 0.0f)
					twoModeToOneMode.setMinimumRelevance(minimumUniquity);
				
				List<Object[]> result = twoModeToOneMode.makeCTEdgesAllWeightsMarked(twoMode,1);
				
				desc = GraphContainer.ShowEdgelistAttributes(result, nodessize1);
				System.out.println(desc);
				localDescription.append(desc+"\n");
				//ReportWriter.getInstance().description(PreambleOneMode1+localDescription.toString());
				
				if(result.size()>0)
					NetOutput(result,Nodes2,  "", outfile, "OneMode_First",nodeheadlines,costumLabs() );
				
				result.clear();
				Nodes2.clear();
				System.out.println("");
				
				Description.append(localDescription);
				localDescription = new StringBuilder();
				ReportWriter.getInstance().clear();
				System.gc();
				
				///////////Second One Mode
				
				ReportWriter.getInstance().appendTechEvent("Started Constructing");
				ReportWriter.getInstance().appendTechEvent("Source: see File "+twoModeFileName);
				
				//System.out.println(label2+"-"+label1+"-"+label2 );
				//localDescription.append(label2+"-"+label1+"-"+label2+"\n");
				
				twoModeToOneMode = new TwoModeToOneModeNetworkConverter();
				
				if(minimumUniquity != 0.0f)
					twoModeToOneMode.setMinimumRelevance(minimumUniquity);
				
				for (int i = 0; i < twoMode.size(); i++) {
					
					Long temp = twoMode.get(i)[0];
					twoMode.get(i)[0] = twoMode.get(i)[1];
					twoMode.get(i)[1] = temp;	
				}
				
				result = twoModeToOneMode.makeCTEdgesAllWeightsMarked(twoMode,2);
				desc = GraphContainer.ShowEdgelistAttributes(result, nodessize2);
				allNodeIdsinSets.add(connectedNodes);
				//System.out.println(desc);
				localDescription.append(desc+"\n");
				//ReportWriter.getInstance().description(PreambleOneMode2+localDescription.toString());
				if(result.size()>0)
					NetOutput(result,Nodes1,  "", outfile, "OneMode_Second",nodeheadlines,costumLabs() );
				/*
				for(int i =0;i<nodeheadlines.length;i++)
					System.out.println( nodeheadlines[i]);
					*/
				result.clear();
				Nodes1.clear();
				
				Description.append(localDescription);

				for (Long[] longs : twoMode) {
				
					for (int i = 0; i < longs.length; i++) {
						longs[i] =null;
					}
					
					longs= null;
				}
				
				twoMode.clear();
				
				try{
					  // Create file 
					  FileWriter fstream = new FileWriter(outfile +"desc.txt",true);
					  BufferedWriter description = new BufferedWriter(fstream);
					  description.write("----------------------------------\n");
					  description.write(Description.toString());
					  //Close the output stream
					  description.close();
				}catch (Exception e){//Catch exception if any
					  System.err.println("Error: " + e.getMessage());
				}
				
				try{
					  // Create file 
					  FileWriter fstream = new FileWriter(outfile+"desc.txt",true);
					  BufferedWriter description = new BufferedWriter(fstream);
					  description.write("----------------------------------\n");
					  description.write(Description.toString());
					  //Close the output stream
					  description.close();
				}catch (Exception e){
					//Catch exception if any
					System.err.println("Error: " + e.getMessage());
				}		
				
				System.out.println("---------------------------------------------------------------" );
				ReportWriter.getInstance().clear();
				return;
			}catch (Exception e){
				//Catch exception if any
				System.err.println("Error: " + e.getMessage());
			}		
			
			
			
	}
	protected static void preprocessTextIdentifiers(CSVReader reader,
			List<Long[]> res) throws IOException {
		String[] nextline;
		
		translate1 = new HashMap<String,Long>();
		translate2 = new HashMap<String,Long>();
		Long translateNum =new Long(0);
		 
		while ((nextline = reader.readNext()) != null) {
			Long[] element = new Long[2]; 
			
			Long temp = translate1.get(nextline[0]);
			
			if(temp == null){
				translateNum++;
				temp=translateNum;
				translate1.put(nextline[0], temp);
			}
			
			if(cmd.hasOption("InvEdgeParams"))
				element[0]= temp;
			else
				element[1]= temp;
			
			temp = translate2.get(nextline[1]);
			
			if(temp == null){
				translateNum++;
				temp=translateNum;
				translate2.put(nextline[1], temp);
			}
			
			if(cmd.hasOption("InvEdgeParams"))
				element[1]= temp;
			else
				element[0]= temp;
			
			
			res.add(element);
		}
		
		if(cmd.hasOption("InvEdgeParams"))
			Collections.sort(res, new SortTableByNthField(1));
		else
			Collections.sort(res, new SortTableByNthField(0));
	}
	
	
	private static List<Object[]> createNodesWithGroups(Map<String, Long> translate,
			int marker) {
		Set<String> keys = translate.keySet();
		List<Object[]> nodes = new ArrayList<Object[]>(); 
		for (String string : keys) {
			Object[] node = new Object[3];
			node[0]=translate.get(string);
			node[1]=new String(string);
			node[2]=new Integer(marker);
			nodes.add(node);	
		}

		
		return nodes;
	}
	
	private static List<Object[]> createNodesWithGroupsAndFile(Map<String, Long> translate,
			int marker,Map<String, List<Object>> nod, int nodLength) {
		
		Set<String> keys = translate.keySet();
		List<Object[]> nodes = new ArrayList<Object[]>(); 
		for (String string : keys) {
			Object[] node = new Object[3+nodLength];
			node[0]=translate.get(string);
			node[1]=new String(string);
			node[2]=new Integer(marker);
			List<Object> listi = nod.get(string);
			for (int i = 0; i < listi.size(); i++) {
				node[3+i]=listi.get(i);
			}
			nodes.add(node);	
		}

		
		return nodes;
	}

protected static String[] costumLabs() {
		String[] edgeLabels = new String[endFieldSizeEdge];
		
		edgeLabels[0]="source";
		edgeLabels[1]="target";
		edgeLabels[2]="Weight";
		edgeLabels[3]="uniquity";
		edgeLabels[4]="avarage uniquity";
		edgeLabels[5]="projection_type";
		return edgeLabels;
	}
	
	protected static void createOptions() {
		options = new Options();

		// add t option
		Option inEdges   = OptionBuilder.withArgName( "csvFile" )
                .hasArg()
                .withDescription(  "(Required)The two Mode Network Edges. In General a 2 Column CSV. First column with Reference things(form edges) Second is the Coocuring IDS (Form Nodes). required" )
                .create( "e" );

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

		Option nodeFiles   = OptionBuilder.withArgName( "csvFile" )
                .hasArg()
                .withDescription(  "(Optional).Nodes of the Things that are Described. Optional" )
                .create( "n" );

		
		Option OutputType   = OptionBuilder.withArgName( "Type" )
                .hasArg()
                .withDescription(  "(Optional)This is the Output Format of the Files, possible: csv, graphml  Standard: graphml," )
                .create( "outtype" );
		

		
		
		options.addOption("h", false, "Show this!");
		options.addOption(inEdges);
		options.addOption(destination);			
		options.addOption(nodeFiles);
		options.addOption(seperator);
		options.addOption(Message);
		options.addOption(OutputType);
		options.addOption("headlines",false,"Set if Input files have a Headlines");
		//options.addOption(textIdentifiers);
		
	}
}
