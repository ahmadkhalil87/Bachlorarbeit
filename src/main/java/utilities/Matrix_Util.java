package utilities;

import java.util.ArrayList;

/**
 * @author Ahmad_Khalil
 *
 */

public class Matrix_Util {
	
	public static Integer[][] clone_array(Integer[][] src) {
		// clone a integer matrix
		Integer[][] res = new Integer[src.length][src.length];
		for(int i=0; i<src.length; i++)
			  for(int j=0; j<src[i].length; j++)
				  res[i][j]=src[i][j];
		return res;
	}
	public static char[][] clone_array(char[][] src) {
		// clone a char matrix
		char[][] res = new char[src.length][src.length];
		for(int i=0; i<src.length; i++)
			  for(int j=0; j<src[i].length; j++)
				  res[i][j]=src[i][j];
		return res;
	}
	public static boolean is_square_matrix(Integer matrix[][]) throws Exception {
		//check if the matrix square
		for(Integer[] e :matrix){
			if (e.length!=matrix.length) throw new Exception("The matrix is not square");
		}
		return true;
	}
	public static boolean has_matrix_zero(Integer matrix[][]) throws Exception {
		//check if the matrix has a zero
		for (int i = 0; i <= matrix.length-1; i++) {
	        for (int j = 0; j <= matrix[i].length-1; j++) {
	            if (matrix[i][j] == 0) {
	            	return true;
	            }
	        }
	    }
		throw new Exception("The matrix does not have a zero");
	}
	public static boolean is_same_matrix(Integer matrix1[][], Integer matrix2[][]) throws Exception {
		//check if the matrices are the same with different positions of the numbers
		ArrayList<Integer> m_1 = new ArrayList<Integer>(), m_2 = new ArrayList<Integer>();
		if (matrix1.length!=matrix2.length) {throw new Exception("The matrics have diffrent sizes");}
		for(int i = 0; i <= matrix1.length-1; i++){
			if (matrix1[i].length!=matrix2[i].length) throw new Exception("The matrics have diffrent sizes");
			for(int j = 0; j <= matrix1[i].length-1; j++){
				m_1.add(matrix1[i][j]);
				m_2.add(matrix2[i][j]);
			}
		}
		m_1.sort(null);
		m_2.sort(null);
		for(int i = 0; i <= m_1.size()-1; i++){
			if (m_1.get(i) != m_2.get(i)) {throw new Exception("The matrics do not match");}
		}
		return true;
	}
}
