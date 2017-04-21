package de.rkrempel.diss.core.commontools;

public class GraphAnalyserResult {
	private int size=5;
	
	//General Statistics;
	public Integer numComponents= null;
	public Integer numConnecetedNodes= null;
	
	public Float Density= null;
	
	
	
	public Integer[] largestComponents=null;
	public Float[] largestComponentsAvgDeg = null;
	public GraphAnalyserResult() {
	}
	
	public GraphAnalyserResult(int sizeofLargestComponentStatistics) {
		size = sizeofLargestComponentStatistics;
	}

	/**
	 * Render result as String
	 * @return The Result as String
	 */
	public String toString(){
		
		StringBuilder out = new StringBuilder();
		if(numComponents!=null)
			out.append( "Number of Components:"+ numComponents+"\n");
		
		if(numComponents!= null && numConnecetedNodes!= null)
			out.append( "Avg size of Components : "+ ((new Float(numComponents)).floatValue()/(new Float(numComponents)).floatValue()) +"\n");
		
		//Component Statistics
		if(largestComponents !=null){
			out.append( "Largest Components : \n");
			for(int i=0;i<size;i++){
				String anmerkung = new String();
				if(largestComponents[i].floatValue()== largestComponentsAvgDeg[i]+1.0f)
					anmerkung = "Completely Connected";
				out.append( (i+1)+". Componentsize "+largestComponents[i] +" Avg. component Degree : "+largestComponentsAvgDeg[i]+" "+anmerkung+"\n");
			
			}
		
		}
		
		
		
		return out.toString();
		
	}
	
}
