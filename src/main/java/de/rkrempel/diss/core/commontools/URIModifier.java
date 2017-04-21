package de.rkrempel.diss.core.commontools;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;

import de.rkrempel.diss.core.harvester.ToHumanReadableStringConverter;

/**
 * Removes Prefix from URL to create String representations
 */
public class URIModifier implements ToHumanReadableStringConverter<URI> {

	private String prefix;

	public URIModifier(String pre) {
		prefix = pre;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	public String getPrefix() {
		return prefix;
	}

	public static String URItoLabel(String uRI,String Prefix){
		String out = uRI;
		try {
			out= URLDecoder.decode(out, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		out = out.replace(Prefix, "");
		
		return out;
	}

	/**
	 * Convert URL to Label
	 * @param uRL URL
	 * @param Prefix The Prefix to remove
	 * @return The part of the URL which acts as Label
	 */
	public static String URItoLabel(URL uRL,String Prefix){
		String out = uRL.toString();
		try {
			out= URLDecoder.decode(out, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		out = out.replace(Prefix, "");
		
		return out;
	}
	/**
	 * Convert URL to Label
	 * @param uRI URI
	 * @param Prefix The Prefix to remove
	 * @return The part of the URL which acts as Label
	 */
	public static String URItoLabel(URI uRI,String Prefix){
		String out = uRI.toString();
		try {
			out= URLDecoder.decode(out, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		out = out.replace(Prefix, "");
		
		return out;
	}

	@Override
	public String toHumanReadable(URI thing) {
		return URItoLabel(thing,prefix);
	}
}
