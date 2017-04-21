package de.rkrempel.diss.core.harvester;

import java.util.List;

/**
 * A Source for thing that should be categorized
 * @param <T> Type of things to attach classes to
 * @param <C> Class System used
 */
public interface ClassSource<T,C> {
	/**
	 * 
	 * @param ToClassify The Thing that Should be Classified
	 * @return Returns Classes (Multiple)
	 */
	public List<C> getClasses(T ToClassify);
	
	
	
	

}
