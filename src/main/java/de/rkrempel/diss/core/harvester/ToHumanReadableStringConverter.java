package de.rkrempel.diss.core.harvester;

/**
 * An Interface for things to get an Human Readable String form
 * @param <T> the thing to get a readable name for
 */
public interface ToHumanReadableStringConverter<T> {

	public String toHumanReadable(T thing);
	
}
