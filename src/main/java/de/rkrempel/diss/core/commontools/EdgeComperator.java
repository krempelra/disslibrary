package de.rkrempel.diss.core.commontools;

import java.util.Comparator;
/**
 * Sorts Edges so that the First Column is orderd and then the Second Collum is Ordered if the First is the Same.
 *
 */
public class EdgeComperator implements Comparator<Object[]>  {

	@Override
	public int compare(Object[] o1, Object[] o2) {
		// TODO Auto-generated method stub
		int temp1 =((Long) o1[0]).compareTo(((Long)o2[0]));
		int temp2;
		//System.out.println("check "+check1+" "+check2);
		//System.out.println("tocheck "+tocheck1+" "+tocheck2);
		//System.out.println("one "+ temp1);		
		//System.out.println("two "+ temp2);

		if(temp1 == 0){
			temp2 = ((Long) o1[1]).compareTo(((Long)o2[1]));
			return temp2;
		}else
			return temp1;
	}
	


}

