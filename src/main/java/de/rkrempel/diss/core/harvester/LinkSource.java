package de.rkrempel.diss.core.harvester;

import java.util.List;
import java.util.Map;

/**
 * Generic Interface for a Links Source
 * @param <T> The Type to get Links for
 */
public interface LinkSource<T> {
	/**
	 * Get Links for Resource
	 * @param in The Thing to get The Context for
	 * @param passiveActive 1 = Only Links From, 2 Only Links To the Resource, 3 Both Active and Passive Links 
	 * @return List of the Linked Resources
	 */
	
	public List<T> GetLinksforResource(T in, int passiveActive );
	
	/**
	 * Get Links for Resource with weight
	 * @param in The Thing to get The Context fo
	 * @param passiveActive 1 = Only Links From, 2 Only Links To the Resource, 3 Both Active and Passive Links 
	 * @return List of the Linked Resources with their Indivdual Weight
	 */
	
	public Map<T,Number> GetLinksforResourceWeighted(T in, int passiveActive );
	
	/**
	 * Get In Degree for the Resource 
	 * @param getDegreefor Resource to get The Network IN Degree from
	 * @return inDegree
	 */
	public int inDegree(T getDegreefor );
	/**
	 * Get Out Degree for the Resource 
	 * @param getDegreefor Resource to get The Network OUT Degree from
	 * @return OUTDegree
	 */
	public int outDegree(T getDegreefor );
	/**
	 * Get overall Degree for the Resource (in Degree and out degree in+Out connection count as 1 Degree )
	 * @param getDegreefor Directed overlaping links wont be counted
	 * @return Exists in the Link Collection(true) OR NOT(false)
	 */
	public int allDegree(T getDegreefor );
	/**
	 * Get Out + In Degree for the Resource without dublicates counted
	 * @param TheThingThatWeDontKnowifItHasLinks The Thing That we want to check of it exists in the Links Collection
	 * @return Exists in the Link Collection(true) OR NOT(false)
	 */
	public boolean containsLinksFor(T TheThingThatWeDontKnowifItHasLinks );
	
	public void quit();
}
