package review.classdesign.jammy.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * 
 * @author fv
 */
public final class SerializationUtils {

	/** **/
	private SerializationUtils() {
		
	}
	
	/**
	 * 
	 * @param object
	 * @param file
	 * @throws IOException
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
	 * 
	 * @param file
	 * @param target
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static <T> T deserialize(final File file, final Class<T> target) throws IOException, ClassNotFoundException {
		final FileInputStream filestream = new FileInputStream(file);
		final ObjectInputStream stream = new ObjectInputStream(filestream);
		final Object object = stream.readObject();
		stream.close();
		filestream.close();
		if (!target.isAssignableFrom(object.getClass())) {
			throw new IOException("Not valid class");
		}
		return target.cast(object);
	}

}
