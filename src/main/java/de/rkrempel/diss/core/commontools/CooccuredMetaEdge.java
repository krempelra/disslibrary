package de.rkrempel.diss.core.commontools;

import java.util.Iterator;
import java.util.Set;

/**
 * 
 * @author rasmus
 *This is a Class that Can Hold an RDF Like Connection The Source and Target must be Nodes
 *Feels a Bit Drity Programming this
 */
public class CooccuredMetaEdge {
	
	public Object source;
	public Object target;
	public Object relation;
	public boolean directed; 
	public Set<Object> coocuredStuff;
	
	public CooccuredMetaEdge() {
	
		directed = false;
	}
	
	public CooccuredMetaEdge(Object Source,Object Target,Object Relation,Set<Object> CoocuredStuff,boolean Directed) {
		this.source = Source;
		this.target = Target;
		this.relation = Relation;
		this.coocuredStuff = CoocuredStuff;
		this.directed =Directed; 
	}
	
	@Override
	public String toString() {
		return this.source.toString()+" "+this.relation.toString()+" "+this.target.toString() ;
	}
	
	/**
	 * Creates an Output that is More Complex and Contains all what is in the Set
	 * @return String with Line Breaks The Relation To String and the Stuff in the Relation
	 */
	public String toLongString() {
		 StringBuffer out =new StringBuffer() ;
		 out.append( this.source.toString()+" "+this.relation.toString()+" "+this.target.toString()+"\n");
		 for (Iterator iterator = coocuredStuff.iterator(); iterator.hasNext();) {
			 out.append( iterator.next().toString());
			 out.append("\n");
		}
		 return out.toString();
	}
	

}
