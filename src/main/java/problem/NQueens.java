/**
 * 
 */
package problem;

import java.util.ArrayList;
import utilities.Matrix_Util;

/**
 * @author Ahmad_Khalil
 *
 */

public class NQueens implements IProblem{
	
	private int n_queens;
	private int queens_on_field;
	private char[][] field;
	
	private NQueens(final int n, char[][] f) throws Exception{
		this.n_queens = n;
		this.field = new char[n][n];
		for (int i = 0; i < n; i++) {
	        for (int j = 0; j < n; j++) {
	        	this.field[i][j] = ' ';
	        }
	    }
		ArrayList<Position> q_pos = get_queens_positions(f);
		this.queens_on_field = q_pos.size();
		for (Position p: q_pos) this.field = this.put_queen_on_field(p);
	}
	
	public NQueens(ArrayList<String> st) throws Exception{
		this.read_from_String(st);
	}
	
	private char[][] put_queen_on_field(Position p) throws Exception {
		char[][] result = Matrix_Util.clone_array(this.field);
		if (this.is_in_field(p.i(), p.j())) {
			if (result[p.i()][p.j()] == ' ') {
				for (int k = 0; k<this.n_queens; k++) {
					if (p.i()+k < result.length && p.j()+k < result[p.i()].length)
						result[p.i()+k][p.j()+k] = 'X';
					if (p.i()-k >= 0 && p.j()-k >= 0)
						result[p.i()-k][p.j()-k] = 'X';             
					if (p.i()-k >= 0 && p.j()+k < result[p.i()].length)
						result[p.i()-k][p.j()+k] = 'X';
					if (p.i()+k < result.length && p.j()-k >=0)
						result[p.i()+k][p.j()-k] = 'X';
					result[p.i()][k] = 'X';
					result[k][p.j()] = 'X';
				}
				result[p.i()][p.j()] = 'Q';
			} else {
				result = null;
				throw new Exception("Can't put a queen!");
			}
		} else {
			result = null;
			throw new Exception("False position!");
		}
		return result;
	}
	
	private IProblem put_queen(Position p) throws Exception {
		IProblem result;
		char[][] f = this.put_queen_on_field(p);
		if (f != null) {
			result = new NQueens(this.n_queens,f);
		} else {
			result = null;
		}
		return result;
	}
	
	private boolean is_in_field(int i, int j) {
		if (i < this.n_queens & j < this.n_queens) return true; else return false;
	}
	
	private boolean is_pos_empty(Position p) {
		if (field[p.i()][p.j()] == ' ') return true; else return false;
	}
	
	private ArrayList<Position> get_queens_positions(char[][] f) {
		ArrayList<Position> result = new ArrayList<Position>();
		for (int i = 0; i < f.length; i++) {
	        for (int j = 0; j < f[i].length; j++) {
	        	if (f[i][j] == 'Q') {
	        		Position p = new Position(i,j);
	        		result.add(p);
	        	}
	        }
	    }
		return result;
	}
	
	private ArrayList<Position> get_empty_pos() {
		// get all the empty positions
		ArrayList<Position> result = new ArrayList<Position>();
		Position e;
		for (int i = 0; i < this.field.length; i++) {
	        for (int j = 0; j < this.field[i].length; j++) {
	        	e = new Position(i,j);
	        	if (this.is_pos_empty(e)) {
	        		result.add(e);
	        	}
	        }
	    }
		return result;
	}
	
	private ArrayList<Position> get_empty_pos_from_next_line() {
		// get the empty positions from the first empty line
		ArrayList<Position> result = new ArrayList<Position>();
		Position p;
		boolean one_line = false;
		for (int i = 0; i < this.field.length; i++) {
	        for (int j = 0; j < this.field[i].length; j++) {
	        	p = new Position(i,j);
	        	if (this.is_pos_empty(p)) {
	        		result.add(p);
	        		one_line = true;
	        	}
	        }
	        if (one_line) break;
	    }
		return result;
	}

	@Override
	public ArrayList<Operation> successors() {
		ArrayList<Operation> resault = new ArrayList<Operation>();
		// add just the empty positions from the next line
		// it can be more complex with the get_empty_pos() function
		ArrayList<Position> tmp = this.get_empty_pos_from_next_line();//this.get_empty_pos();
		for (Position p : tmp) {
			try {
				resault.add(new Operation(put_queen(p),p.toString()));
			} catch (Exception e) {
				break;
			}
		}
		return resault;
	}

	@Override
	public boolean is_goal() {
		return this.n_queens==this.queens_on_field;
	}

	@Override
	public boolean equals(IProblem e) {
		return this.toString().equals(e.toString());
	}

	private void read_from_String(ArrayList<String> st) throws Exception {
		ArrayList<Position> positions = null;
		boolean size_done = false;
		boolean queens_pos_done = false;
		for (String s: st) {
			if (s.startsWith("size")) {
				if (!size_done) {
					if (s.contains("=")) {
						try {
							if (!s.split("=")[0].trim().equals("size") || s.split("=").length>2) throw new Exception ("Problem file is corrupted");
							int size = Integer.parseInt(s.split("=")[1].trim());
							if (size > 0) this.n_queens = size;
							else throw new Exception ("size value is wrong, input a value bigger than 0");
						}
						catch (Exception e) {
							throw new Exception ("size has no value");
						}
						}
					else throw new Exception ("size has no value, please add a \"=\" symbole");
					size_done = true;
				} else {
					throw new Exception ("size exists more than once");
				}
			}
			else if (s.startsWith("queens_pos")) {
				if (!queens_pos_done) {
					if (!s.split("=")[0].trim().equals("queens_pos") || s.split("=").length>2) throw new Exception ("Problem file is corrupted");
					positions = new ArrayList<Position>();
					queens_pos_done = true;
					if (s.contains("=")) {
						String q_ps[] = s.split("=")[1].split("\\(|\\)");
						for (String position: q_ps) {
							try {
								String[] position_i = position.split(",");
								if (position_i.length<2) continue;
								if (position_i[0].length()==0) continue;
								int i = Integer.parseInt(position_i[0].trim());
								if (position_i[1].length()==0) continue;
								int j = Integer.parseInt(position_i[1].trim());
								positions.add(new Position(i,j));
							} catch (Exception e) {
								throw new Exception ("The queens postions are not correct");
							}
						}
					}
					else {
						throw new Exception ("queens_pos, please add a \"=\" symbole");
					}
				} else {
					throw new Exception ("queens_pos exists more than once");
				}
			}
			else if (s.length()!=0) if (s.getBytes()[0]=='%') continue;
			else throw new Exception ("Problem file is corrupted");
		}
		if (this.n_queens>0) {
			this.field = new char[this.n_queens][this.n_queens];
			for (int i = 0; i < this.n_queens; i++) {
		        for (int j = 0; j < this.n_queens; j++) {
		        	this.field[i][j] = ' ';
		        }
		    }
		}
		if (positions!=null) {
			for (Position p: positions) {
				try {
					this.field = this.put_queen_on_field(p);
				} catch (Exception e) {
					this.n_queens = 0;
					this.field = null;
					throw new Exception ("Problem file is corrupted");
				}
		    }
			this.queens_on_field = positions.size();
		}
	}
	
	@Override
	public String toString() {
		String result = "+" + "--".repeat(this.n_queens) + "-+\n";
		for (int i = 0; i < this.field.length; i++) {
	        for (int j = 0; j < this.field[i].length; j++) {
	        	if (j==0) result += "| ";
	        	result += this.field[i][j];
	        	if (j < this.field[i].length-1) result += " ";
	        }
	        if (i < this.field.length-1) result += " |\n";
	    }
		result += " |\n+" + "--".repeat(this.n_queens) + "-+";
		result += "\nStill " + (this.n_queens-this.queens_on_field) +" queens to put.";
		return result;
	}
	
	@Override
	public boolean dead_end(IProblem e) {
		// here is the dead end when there is no more empty positions on the field
		return this.get_empty_pos().size()==0;
	}
	
	//Class for the positions of the queens
	private class Position {
		private final int i;
		private final int j;
		
		Position(int a, int b){
			this.i=a;
			this.j=b;
		}
		
		public int i() {
			return this.i;
		}
		
		public int j() {
			return this.j;
		}
		
		@Override
		public String toString() {
			return "(" + i + ", "+ j + ")";
		}
	}
}
