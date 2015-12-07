package fr.faylixe.jammy.core.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Toolbox class that contains helpful method for dealing
 * with serialization aspect.
 * 
 * @author fv
 */
public final class SerializationUtils {
	
	/** Exception thrown when deserializing and assigning to a not valid class type. **/
	private static final IOException NOT_VALID_CLASS = new IOException("Not a valid class");

	/**
	 * Private constructor for avoiding instantiation.
	 */
	private SerializationUtils() {
		// Do nothing.
	}
	
	/**
	 * Serializes the given <tt>object</tt> into the given <tt>file</tt>.
	 * 
	 * @param object Object to serialize.
	 * @param file File to serialize object into.
	 * @throws IOException If any error occurs while writing file.
	 */
	public static void serialize(final Serializable object, final File file) throws IOException {
		final FileOutputStream filestream = new FileOutputStream(file);
		final ObjectOutputStream stream = new ObjectOutputStream(filestream);
		stream.writeObject(object);
		stream.flush();
		stream.close();
		filestream.close();
	}
	
	/**
	 * Deserializes the given <tt>file</tt> into an object
	 * instance of the given <tt>target</tt> class.
	 * 
	 * @param file File to deserialize.
	 * @param target Target class that object will be deserialize into.
	 * @return Deserialized object.
	 * @throws IOException If any error occurs while reading file content.
	 * @throws ClassNotFoundException If any error occurs while retrieving serialized object class.
	 */
	public static <T> T deserialize(final File file, final Class<T> target) throws IOException, ClassNotFoundException {
		final FileInputStream filestream = new FileInputStream(file);
		final ObjectInputStream stream = new ObjectInputStream(filestream);
		final Object object = stream.readObject();
		stream.close();
		filestream.close();
		if (!target.isAssignableFrom(object.getClass())) {
			throw NOT_VALID_CLASS;
		}
		return target.cast(object);
	}

}
