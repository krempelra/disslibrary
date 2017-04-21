package de.rkrempel.diss.core.commontools;

import java.util.Comparator;
/**
 * Sort table by the Nth Field
 * @author rasmus
 *
 */
public class SortTableByNthField implements Comparator<Object[]>  {
	int nth = 0;
	public SortTableByNthField( int n) {
	
		nth = n;
	}
	
	@Override
	public int compare(Object[] o1, Object[] o2) {

		return ((Comparable)o1[nth]).compareTo((Comparable)o2[nth]);
	}
	

}
