package utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Ahmad_Khalil
 *
 */

public class File_Util {
	
	public static ArrayList<String> read_file(String file_loc) throws Exception {
		ArrayList<String> result = new ArrayList<String>();
		try {
		      File my_file = new File(file_loc);
		      Scanner my_reader = new Scanner(my_file);
		      while (my_reader.hasNextLine()) {
		        String data = my_reader.nextLine();
		        result.add(data);
		      }
		      my_reader.close();
		    } catch (FileNotFoundException e) {
		    	throw new Exception("Error by reading the file");
		    }
		return result;
	}
	public static String read_file_to_get_string(String file_loc, String to_find) throws Exception {
		String result = "";
		try {
		      File my_file = new File(file_loc);
		      Scanner my_reader = new Scanner(my_file);
		      while (my_reader.hasNextLine()) {
		        String data = my_reader.nextLine();
		        if (data.contains(to_find)) {
		        	result = data;
		        	break;
		        }
		      }
		      my_reader.close();
		    } catch (FileNotFoundException e) {
		    	throw e;
		    }
		return result;
	}
	public static int read_file_to_get_int(String file_loc, String to_find) throws Exception {
		int result = -1;
		try {
		      File my_file = new File(file_loc);
		      Scanner my_reader = new Scanner(my_file);
		      while (my_reader.hasNextLine()) {
		        String data = my_reader.nextLine();
		        if (data.contains(to_find)) {
		        	data = data.replaceAll("\\D+","");
		        	result = Integer.parseInt(data);
		        	break;
		        }
		      }
		      my_reader.close();
		} catch (FileNotFoundException e) {
			throw e;
		}
		if (result == -1) throw new Exception("Config file korrupted!");
		return result;
	}
	public static void read_file_edit_line(String file_loc, String find, String replace) throws Exception {
		ArrayList<String> lines = new ArrayList<String>();
		String line = null;
		try {
			File my_file = new File(file_loc);
            FileReader fr = new FileReader(my_file);
            BufferedReader br = new BufferedReader(fr);
            line = br.readLine();
            while (line != null) {
            	if (line.contains(find))
                    line = replace;
                lines.add(line);
                line = br.readLine();
            }
            fr.close();
            br.close();
            FileWriter fw = new FileWriter(my_file);
            BufferedWriter out = new BufferedWriter(fw);
            for(String st : lines) {
            	out.write(st + System.lineSeparator());
            }
            out.flush();
            out.close();
		    } catch (IOException e) {
		    	throw new Exception("Error by reading the file");
		    }
	}
}
