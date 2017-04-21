package de.rkrempel.diss.core.exporters;

import java.io.IOException;
import java.io.Writer;

/**
 * Interface for the Exporter
 */
public abstract class StandardExporter {

	/**
	 * get the export result as String
	 * @return result as a String
	 */
	public String getResAsString( ){
		
		String out = new String(getResAsStringBuffer());
		
		return out;
	}

	/**
	 * get the export result as StringBuffer
	 * @return result as a StringBuffer
	 */
	public abstract StringBuffer getResAsStringBuffer( );
	
	
	public abstract void writeResWithWriter(Writer out ) throws IOException;
}
