package de.rkrempel.diss.commandlineexecs;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.rkrempel.diss.core.commontools.GraphContainer;
import de.rkrempel.diss.core.exporters.XMLStandardConverter;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

import de.rkrempel.diss.core.report.ReportWriter;
import au.com.bytecode.opencsv.CSVReader;
import de.rkrempel.diss.core.commontools.SortTableByNthField;
import de.rkrempel.diss.core.exporters.CSVStandardExporter;

public class InOut {

	protected static Options options;
	protected static CommandLine cmd;
	protected static Map<String,Long> translate1;
	protected static Map<String,Long> translate2;

	protected static void writeWithNodes(String outfile, String nodefile,
			List<Object[]> edges) throws FileNotFoundException, IOException {
				String[] nextline;
				CSVReader reader;
				List<Object[]> nodes = new ArrayList<Object[]>();
				if(cmd.hasOption("textIdentifiers") && nodefile.isEmpty() ){
					
					Map<String,Long> transl;
					
					if(cmd.hasOption("InvEdgeParams"))
						transl = translate1;
					else
						transl = translate2;
					
					
					Set<String> keys = transl.keySet();
					
					
					for (Iterator iterator = keys.iterator(); iterator
							.hasNext();) {
						String string = (String) iterator.next();
						Object[] newNode =  new Object[2];
						newNode[1]= string;
						newNode[0]= transl.get(string);
						nodes.add(newNode);
					}
					Collections.sort(nodes, new SortTableByNthField(0));
					
					
					
				}else{
					
					reader = new CSVReader(new FileReader(nodefile), ';', '\n' );
				
					while ((nextline = reader.readNext()) != null) {
						// nextLine[] is an array of values from the line
						Object[] newNode =  new Object[nextline.length];
						newNode[0] = new Long( nextline[0]);
						for (int i = 1; i < newNode.length; i++) {
							newNode[i] = nextline[i];
				    			
						}
				
						nodes.add(newNode);
						}
				
					}
				fileWriter(outfile , edges, nodes);
			}

	protected static void processIntIdentifiers(CSVReader reader, List<Long[]> res)
			throws IOException {
				String[] nextline;
				while ((nextline = reader.readNext()) != null) {
					// nextLine[] is an array of values from the line
					Long[] newEd =  new Long[2];
			
					if(cmd.hasOption("InvEdgeParams")){
						newEd[0] = new Long( nextline[0]);
						newEd[1] = new Long( nextline[1]);
					}else{
						newEd[1] = new Long( nextline[0]);
						newEd[0] = new Long( nextline[1]);
					}
					res.add(newEd);
				}
			}

	protected static void preprocessTextIdentifiers(CSVReader reader, List<Long[]> res)
			throws IOException {
				String[] nextline;
				translate1 = new HashMap<String,Long>();
				translate2 = new HashMap<String,Long>();
				Long translateNum1 =new Long(0);
				Long translateNum2 =new Long(0);
				 
				while ((nextline = reader.readNext()) != null) {
					Long[] element = new Long[2]; 
					
					Long temp = translate1.get(nextline[0]);
					
					if(temp == null){
						translateNum1++;
						temp=translateNum1;
						translate1.put(nextline[0], temp);
					}
					
					if(cmd.hasOption("InvEdgeParams"))
						element[0]= temp;
					else
						element[1]= temp;
					
					temp = translate2.get(nextline[1]);
					
					if(temp == null){
						translateNum2++;
						temp=translateNum2;
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

	protected static void fileWriter(String filename, List<Object[]> edges, List<Object[]> nodes) {
		//Produce Export
		GraphContainer graph = new GraphContainer(nodes, edges);
		graph.autoInitializeLabels();
		graph.reduceNodes();
		
		//Produce Export
		XMLStandardConverter xsc = new XMLStandardConverter(graph);
		//xsc.setEdges(result);
		
		
		
		//xsc.setNodes(nodes);
		
		String out = xsc.getResAsString();
		
	
		try {
			FileWriter fstream = new FileWriter(filename +
					".GraphMl");
			//fstream = new FileWriter("/Users/archaeopool/paralells.net");
			
			BufferedWriter to = new BufferedWriter(fstream);
			to.write(out);
			to.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

	protected static void createOptions() {

		
	}
	protected static void NetOutput(List<Object[]> edges,List<Object[]> nodes, String Description, String localpath, String filename){
		NetOutput( edges, nodes,  Description,  localpath,  filename,null,null);
	}
	protected static void NetOutput(List<Object[]> edges,List<Object[]> nodes, String Description, String localpath, String filename, String[] nodelabels, String[] edgelabels ){
		//TODO Node Desctiptions Hinzufügen!
		//TODO Descriptions für Felder in Graphen Machen //Kanten Gemacht
		//TODO Reports Nachtragen Von Ignores Also Minimum Relevance
		//TODO Schreiben aller "Interessanten" Netze in Verschienden Formaten.
		
		GraphContainer out;
		ReportWriter.getInstance().description(Description.toString());
		out = new GraphContainer(nodes, edges);

		out.autoInitializeLabels();
		
		List<String> edgeLabels = null;
		if(edgeLabels != null)
			out.setEdgeLabels(edgeLabels);
		
		if(edgelabels != null)
			out.setEdgeLabels(Arrays.asList(edgelabels));
		if(nodelabels != null)
			out.setNodeLabels(Arrays.asList(nodelabels));
		
				

		
		//TODO Reports Nachtragen Von Ignores Also Minimum Relevance
		
		//Produce Export
		
		if(cmd.hasOption("outtype")&& cmd.getOptionValue("outtype").equals("csv")){

			CSVStandardExporter csv = new CSVStandardExporter(out); 
			if(cmd.hasOption("sep"))
				csv.setSeperator(cmd.getOptionValue("Seperator"));
			try {
				FileWriter fstream = new FileWriter(localpath+filename+"Edges.csv");
				//fstream = new FileWriter("/Users/archaeopool/paralells.net");	
				Writer to = new BufferedWriter(fstream);
				csv.writeResWithWriter(to);
				to.close();

				fstream = new FileWriter(localpath+filename+"Nodes.csv");
				//fstream = new FileWriter("/Users/archaeopool/paralells.net");	
				to = new BufferedWriter(fstream);
				csv.writeNodesWithWriter(to);
				to.close();

				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}else{
			XMLStandardConverter xsc = new XMLStandardConverter(out);
			try {
				FileWriter fstream = new FileWriter(localpath+filename+".graphml");
				//fstream = new FileWriter("/Users/archaeopool/paralells.net");	
				Writer to = new BufferedWriter(fstream);
				xsc.writeResWithWriter(to);
				to.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		//xsc.setEdges(result);
		//xsc.setNodes(nodes);
		//StringBuffer output = xsc.getResAsStringBuffer();
	
		//TODO Schreiben aller "Interessanten" Netze in Verschienden Formaten. 
	
		
	}


	public InOut() {
		super();
	}

}