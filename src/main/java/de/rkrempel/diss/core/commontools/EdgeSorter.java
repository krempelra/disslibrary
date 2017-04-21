package de.rkrempel.diss.core.commontools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class EdgeSorter {
	
	
	public static boolean insertSorted(List<Object[]> a, Long one,Long two, Float weight){

		if(a.isEmpty()){
			a.add(buildinsert(one,two,weight));
			return false;
		}
		
		//Object[] tempstart = a.firstElement();
		Object[] tempend = a.get(a.size()-1);
		// 12 >23 -> 1

		if(0>check((Long)tempend[0], (Long)tempend[1], one, two) ){
			a.add(buildinsert(one,two,weight));
			return false;
		}

		
		return recinsertSorted( a,  one, two,  weight,1,a.size()-1);
		
		
	}
	public static boolean checkSorted(List<Object[]> a, Long one,Long two, Float weight){

		if(a.isEmpty()){
			return false;
		}
		
		Object[] tempend = a.get(a.size()-1);
		if(0>check((Long)tempend[0], (Long)tempend[1], one, two) ){
			return false;
		}

		return recCheckSorted( a,  one, two,  weight,1,a.size()-1);
		
		
	}
	public static Object[] buildinsert(Long one,Long two, Float weight){
		Object[] out = new Object[3];
		out[0] = one;
		out[1] = two;
		out[2] = weight;
		return out;
	}
	
	
	public static boolean recinsertSorted(List<Object[]> a, Long one,Long two, Float weight,int min, int max){
		
		for(int mid= (min + max) /2;min < max;mid= (min + max) /2){
			/*
			if(max-min ==1)
				break;
			*/
			Object[] temp = a.get(mid);
			
			// temp > one -> 1 //Going up
			// temp < one -> -1 //Going Down
			int temp1 = ((Long)temp[0]).compareTo(one);
			
			if(temp1 == 0 ){
				//System.out.println( temp1);
				int temp2 = ((Long)temp[1]).compareTo(two);
				if(temp2==0){
					//System.out.println( temp2);
					temp[2] = (Float)temp[2]+weight;
					return true;
				} else if(temp2 >0){
					min = mid+1;
				} else if(temp2 <0 ){
					max = mid-1;
				}	
			}else if(temp1 >0){
				//Going up
				min = mid+1;	
			}else if(temp1 <0  ){
				//Going down
				
				max = mid-1;
			}
		}
		

		
		int inser;
		if(max < min)
			inser=min;
		else
			inser=max;

		a.add(inser,buildinsert(one,two,weight));
		return true;
	}

public static boolean recCheckSorted(List<Object[]> a, Long one,Long two, Float weight,int min, int max){
		
		for(int mid= (min + max) /2;min < max;mid= (min + max) /2){
			/*
			if(max-min ==1)
				break;
			*/
			Object[] temp = a.get(mid);
			
			// temp > one -> 1 //Going up
			// temp < one -> -1 //Going Down
			int temp1 = ((Long)temp[0]).compareTo(one);
			
			if(temp1 == 0 ){
				//System.out.println( temp1);
				int temp2 = ((Long)temp[1]).compareTo(two);
				if(temp2==0){
					return true;
				} else if(temp2 >0){
					min = mid+1;
				} else if(temp2 <0 ){
					max = mid-1;
				}	
			}else if(temp1 >0){
				//Going up
				min = mid+1;	
			}else if(temp1 <0  ){
				//Going down
				
				max = mid-1;
			}
		}
		
		return false;
	}

	
	//check > tocheck ->1
	//check < tocheck -> -1
	
	public static int check(Long check1, Long check2, Long tocheck1, Long tocheck2){

		
		int temp1 = check1.compareTo(tocheck1);
		int temp2;

		if(temp1 == 0){
			temp2 = check2.compareTo(tocheck2);
			return temp2;
		}else
			return temp1;
		
	} 

	
	
	public static boolean isSortingOkay(List<Object[]> a){
		if(a.isEmpty())
			return true;
		boolean good = true;
		
		Object[] last = a.get(0);
		for (int i = 1; i < a.size(); i++) {
			Object[] current = a.get(i);
			if(!(0>check((Long) last[0],(Long) last[1],(Long) current[0],(Long) current[1] ))){
				System.out.println("Fraut! ORDER DISRUPTED");
				System.out.println("Last :"+ (i-1)+" " + last[0]+" "+last[1]);
				System.out.println("current :"+ (i)+" " +current[0]+ " " +current[1] );
				System.out.println("size :"+ a.size());
				good= false;
				return good;
			}	
			current = last;
		}
		return good;	
	}
	
	public static Object[][] addup(List<Object[]> edges){
		Object[] last = null;
		EdgeComperator ed =new EdgeComperator();
		int expectedlength=nelength(edges);
		//System.out.println(expectedlength);
		Object[][] out = new Object[expectedlength][3];
		int curr =expectedlength-1;
		for(int i = edges.size()-1;i>=0;i-- ){
			Object[] object = edges.get(i);
			if(last == null){
				last = object;
				continue;
			}
			if( 0!=ed.compare(object,last)){
				out[curr] = object;
				
			}
			else{
				Float temp1 = (Float) object[2];
				       
				Float temp2 = (Float) last[2];
				object[2] = temp1+temp2; 
			}
				
		}
		
		return out;
		
		
		
	}
	
	public static void adduphere(List<Object[]> edges){
		Object[] last = null;
		EdgeComperator ed =new EdgeComperator();
		//System.out.println(expectedlength);
		//Object[][] out = new Object[expectedlength][3];

		List<Object[]> toremove = new ArrayList<Object[]>(10);
		for(int i = edges.size()-1;i>=0;i-- ){
			
			Object[] object = edges.get(i);
			if(last == null){
				last = object;
				continue;
			}
			if( 0==ed.compare(object,last)){
				Float temp1 = (Float) object[2];

				Float temp2 = (Float) last[2];
				object[2] = temp1+temp2;
				edges.remove(last);
				toremove.add(last);
			}else if(!toremove.isEmpty()){
				System.out.println("removing all "+toremove.size()+" Index "+i);
				edges.removeAll(toremove);
				toremove.clear();
			}
			
			
			last = object;
		}
		System.out.println("removing all");
		//edges.removeAll(toremove);
		
		
	}
	
	//Fastest!
	
	public static List<Object[]> addupherenewlist(List<Object[]> edges){
		ArrayList<Integer> temp = new ArrayList<Integer>();
		return addupherenewlist(edges,temp);
	}

	/**
	 * 
	 * @param edges Edges to Addup
	 * @param FieldsIDs A List Of Ids of the Array That Should be Added Up if Its empty Everything that isnt source or target is Added UP To Avoid Problems just enter an Element that not Exists to create Addup Nothing
	 * @return List od Added Up Edges
	 */
	public static List<Object[]> addupherenewlist(List<Object[]> edges,Collection<Integer> FieldsIDs){
		Object[] last = null;
		EdgeComperator ed =new EdgeComperator();
		
		List<Object[]>out  = new ArrayList<Object[]>(0);
		

		
		
		for(int i = 0;i<edges.size();i++ ){
			
			Object[] object = edges.get(i);
			if(last == null){
				last = object;			
				if(i==edges.size()-1)
					out.add(last);
				
				continue;
				
				
			}
			if( 0==ed.compare(object,last)){
				
				for(int j = 2;j<object.length && j<last.length;j++){
					//Check if it should be Added Up
					if(!FieldsIDs.isEmpty() && !FieldsIDs.contains(j) )
						continue;
					
				
					Number temp1 = (Number) object[j];

					Number temp2 = (Number) last[j];
					object[j]= new Float( temp1.floatValue()+ temp2.floatValue());
				}
			}else{

				out.add(last);
				
			}
			

			last = object;
			if(i==edges.size()-1)
				out.add(last);
		}
		Collections.reverse(out);
		return out;
		
	}
	/**
	 * This is like the AddupwehrenewList Funtion Except that its more Typt sensitive and takes more than Floats 
	 * Longs Are handled As IDS so they are converted into Sets!
	 * @param edges Edges to Addup
	 * @param FieldsIDs A List Of Ids of the Array That Should be Added Up if Its empty Everything that isnt source or target is Added UP
	 * @return List od Added Up Edges
	 */
	public static List<Object[]> addupherenewlistTypeSensitive(List<Object[]> edges,Collection<Integer> FieldsIDs){
		Object[] last = null;
		EdgeComperator ed =new EdgeComperator();
		//int expectedlength=nelength(edges);
		//System.out.println(expectedlength);
		List<Object[]>out  = new ArrayList<Object[]>(0);
		
		
		//	int curr =expectedlength-1;
		//List<Object[]> toremove = new ArrayList<Object[]>(10);
		//for(int i = edges.size()-1;i>=0;i-- ){
		/*for(int i = 0;i<edges.size();i++ ){
		
		
		}*/
		
		
		for(int i = 0;i<edges.size();i++ ){
			
			Object[] object = edges.get(i);
			if(last == null){
				last = object;			
				if(i==edges.size()-1){
					//TODO Unschöne Ausnahme Das ganze steht nochmal am Schluss Müsste besser gemacht werde oder Formalisiert !!!!!
					if(!FieldsIDs.isEmpty() ){
						for (Iterator<Integer> iterator = FieldsIDs.iterator(); iterator.hasNext();) {
							Integer field =  iterator.next();
							if(last[field.intValue()].getClass().equals(Long.class)){
								
								HashSet<Long> temo = new HashSet<Long>();
								temo.add((Long)last[field.intValue()]);
								last[field.intValue()]= temo;
								
							}
								
						}
					}
					
					out.add(last);
				}
				continue;
				
				
			}
			if( 0==ed.compare(object,last)){
				
				for(int j = 2;j<object.length && j<last.length;j++){
					//Check if it should be Added Up
					if(!FieldsIDs.isEmpty() && !FieldsIDs.contains(j) )
						continue;
					
					if(Float.class.equals(object[j].getClass())){
						Float temp1 = (Float) object[j];

						Float temp2 = (Float) last[j];
						object[j] = temp1+temp2;
					}
					else if(Integer.class.equals(object[j].getClass())){
						Integer temp1 = (Integer) object[j];

						Integer temp2 = (Integer) last[j];
						object[j] = temp1+temp2;
					}
					//Strings get Concatinated by comma
					else if(String.class.equals(object[j].getClass())){
						String temp1 = object[j].toString();

						String temp2 = last[j].toString();
						Object tempResner = (Object) temp1+","+temp2;
						object[j] = tempResner;
					}
					//TODO BETTER ID Handling -> Make Sets There has to be some better Type Handling
					
					
					//IDs become Strings and these are concatinated by comma
					else if(Long.class.equals(object[j].getClass())){
						
						if(last[j].getClass().equals(HashSet.class)){
							((Set<Long>)last[j]).add((Long)object[j]); 
							
						}else{
							
							Set<Long> ttemp = new HashSet<Long>(); 
							ttemp.add((Long) object[j]);
							ttemp.add((Long) last[j]);
							//last[j] = null;
							System.out.println(last.getClass().toString());
							last[j] = ttemp ;
							
						}
							
						object[j] = null;
						object[j] = last[j];
					}					
					
				}
			}else{
				
				if(!FieldsIDs.isEmpty() ){
					for (Iterator<Integer> iterator = FieldsIDs.iterator(); iterator.hasNext();) {
						Integer field =  iterator.next();
						if(last[field.intValue()].getClass().equals(Long.class)){
							
							HashSet<Long> temo = new HashSet<Long>();
							temo.add((Long)last[field.intValue()]);
							last[field.intValue()]= temo;
							
						}
							
					}
				}
				out.add(last);
					

			}
			

			last = object;
			if(i==edges.size()-1){
				
				//TODO siehe Oben
				if(!FieldsIDs.isEmpty() ){
					for (Iterator<Integer> iterator = FieldsIDs.iterator(); iterator.hasNext();) {
						Integer field =  iterator.next();
						if(last[field.intValue()].getClass().equals(Long.class)){
							
							HashSet<Long> temo = new HashSet<Long>();
							temo.add((Long)last[field.intValue()]);
							last[field.intValue()]= temo;
							
						}
							
					}
				}
				
				out.add(last);
			}
				
		}/*
		for (int i = 0; i < out.size(); i++) {
			System.out.println(out.get(i)[0]+" "+out.get(i)[1]+" "+out.get(i)[5] );
		}*/
		
		Collections.reverse(out);
		return out;
		
	}
	public static List<Long[]> addupherenewlistLong(List<Long[]> edges){
		Long[] last = null;
		EdgeComperator ed =new EdgeComperator();

		List<Long[]>out  = new ArrayList<Long[]>(0);
		

		for(int i = 0;i<edges.size();i++ ){
			
			Long[] object = edges.get(i);
			if(last == null){
				last = object;			
				if(i==edges.size()-1)
					out.add(last);
				
				continue;
				
				
			}
			if( 0==ed.compare(object,last)){
				
			}else{

				out.add(last);
				
			}
			

			last = object;
			if(i==edges.size()-1)
				out.add(last);
		}
		Collections.reverse(out);
		return out;
		
	}
	
	public static int nelength(List<Object[]> in){
		Object[] last = null;
		EdgeComperator ed =new EdgeComperator();
		int counter=0;
		for (int i = 0; i < in.size(); i++) {
			
			Object[] object = in.get(i);
			if(last == null){
				last = object;
				continue;
			}
			
			if( 0!=ed.compare(object,last))
				counter++;
			last = object;	
			
		}
		return counter;
		
		
		
		
	}
	
	
		
}
