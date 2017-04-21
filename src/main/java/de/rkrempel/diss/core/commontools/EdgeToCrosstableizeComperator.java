package de.rkrempel.diss.core.commontools;

import java.util.Comparator;

/**
 * Comperator for edges
 */
public class EdgeToCrosstableizeComperator implements Comparator<Object[]>  {

	@Override
	public int compare(Object[] o1, Object[] o2) {

		return ((Long)o1[1]).compareTo((Long)o2[1]);
	}
	


}

