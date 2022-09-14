package problem;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class IProblem_reader {
	
	public static IProblem read(String file_loc) throws Exception {
		// read the problem from a file
		ArrayList<String> lines = utilities.File_Util.read_file(file_loc);
		return read(lines);
	}
	
	public static IProblem read(ArrayList<String> lines) throws Exception {
		// read the problem from a list of strings which are the lines of a file
		IProblem result = null;
		try {
			Class<?> prob = null;
			Constructor<?> con = null;
			// the first line should be the proble name (class)
			prob = Class.forName("problem." + lines.get(0));
			lines.remove(0);
			//check if the class has a constructor with ArrayList
			con = prob.getConstructor(ArrayList.class);
			result = (IProblem) con.newInstance(lines);
		} catch (ClassNotFoundException  e) {
			throw new Exception ("file is not a Problem");
		} catch (NoSuchMethodException | SecurityException | NullPointerException
				| InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new Exception ("Problem file is corrupted");
		} catch (Exception e) {
			throw new Exception ("Problem file is corrupted");
		}
		return result;
	}
}
