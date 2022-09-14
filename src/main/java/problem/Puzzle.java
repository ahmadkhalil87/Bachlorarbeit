package problem;

import java.util.ArrayList;
import java.util.Random;

import utilities.Matrix_Util;

/**
 * @author Ahmad_Khalil
 *
 */

public class Puzzle implements IProblem{
	private int puzzle_size;
	private Integer puzzle[][];
	private Integer zero_position[] = new Integer[2];
	private Puzzle goal;
	
	// initialize the puzzle
	public Puzzle(int n) throws Exception{
		// Create a Puzzle with a size "puzzle_size" and a Goal Puzzle with random maximal 50 moves
		try {
			if (n<=0) throw new Exception("puzzle_size must be bigger than 0");
			this.puzzle_size = n;
			this.puzzle = new Integer[n][n];
			this.set_random();
			Random rand = new Random();
			int goal_move = rand.nextInt(50);
			this.goal = create_random_new_goal(goal_move);
		} catch (Exception e) {
			throw e;
		}
	}
	public Puzzle(int n, int goal_move) throws Exception{
		// Create a Puzzle with a size "puzzle_size" and a goal puzzle with "goal_move" moves
		try {
			if (n<=0) throw new Exception("puzzle_size must be bigger than 0");
			if (goal_move < 0) throw new Exception("goal_move must be positive or 0");
			this.puzzle_size = n;
			this.puzzle = new Integer[n][n];
			this.set_random();
			this.goal = this.create_random_new_goal(goal_move);
		} catch (Exception e) {
			throw e;
		}
	}
	public Puzzle(int puzzle_size, Puzzle goal) throws Exception{
		// Create a Puzzle with a size "puzzle_size" and a goal puzzle "goal"
		try {
			if (puzzle_size<=0) throw new Exception("puzzle_size must be bigger than 0");
			if (goal==null) throw new Exception("goal is null, enter a goal!");
			this.puzzle_size = puzzle_size;
			this.puzzle = new Integer[puzzle_size][puzzle_size];
			this.set_random();
			this.goal = goal;
		} catch (Exception e) {
			throw e;
		}
	}
	public Puzzle(Integer puzzle[][]) throws Exception{
		// Create a Puzzle with a given matrix and a goal puzzle with random maximal 50 moves
		try {
			Matrix_Util.is_square_matrix(puzzle);
			Matrix_Util.has_matrix_zero(puzzle);
			this.puzzle_size = puzzle.length;
			this.puzzle = Matrix_Util.clone_array(puzzle);
			this.set_pos();
			Random rand = new Random();
			int goal_move = rand.nextInt(50);
			this.goal = this.create_random_new_goal(goal_move);
		} catch (Exception e) {
			throw e;
		}
	}
	public Puzzle(Integer puzzle[][], int goal_move) throws Exception{
		// Create a Puzzle with a given matrix and a goal puzzle with "goal_move" moves
		try {
			Matrix_Util.is_square_matrix(puzzle);
			Matrix_Util.has_matrix_zero(puzzle);
			if (goal_move < 0) throw new Exception("goal_move must be positive or 0");
			this.puzzle_size = puzzle.length;
			this.puzzle = Matrix_Util.clone_array(puzzle);
			this.set_pos();
			this.goal = this.create_random_new_goal(goal_move);
		} catch (Exception e) {
			throw e;
		}
	}
	public Puzzle(Integer puzzle[][], Puzzle goal) throws Exception{
		// Create a Puzzle with a given matrix and a goal puzzle a given matrix
		try {
			Matrix_Util.is_square_matrix(puzzle);
			Matrix_Util.has_matrix_zero(puzzle);
			this.puzzle_size = puzzle.length;
			this.puzzle = Matrix_Util.clone_array(puzzle);
			this.set_pos();
			this.goal = goal;
		} catch (Exception e) {
			throw e;
		}
	}
	public Puzzle(Integer puzzle[][], Integer g[][]) throws Exception{
		try {
			Matrix_Util.is_square_matrix(puzzle);
			Matrix_Util.has_matrix_zero(puzzle);
			this.puzzle_size = puzzle.length;
			this.puzzle = Matrix_Util.clone_array(puzzle);
			this.set_pos();
			this.goal = new Puzzle(g,goal=null);
		} catch (Exception e) {
			throw e;
		}
	}
	
	public Puzzle(ArrayList<String> st) throws Exception{
		this.read_from_String(st);
	}
	
	// Set the puzzle randomly
	private void set_random() {
		int last = puzzle_size*puzzle_size-1;
		ArrayList<Integer> list = new ArrayList<Integer>(last);
        for(int i = 0; i <= last; i++) {
            list.add(i);
        }
        Random rand = new Random();
        int index;
	    for (int i = 0; i < puzzle_size; i++) {
	        for (int j = 0; j < puzzle_size; j++) {
	        	index = rand.nextInt(list.size());
	        	this.puzzle[i][j] = list.remove(index);
	            if (this.puzzle[i][j] == 0) {
	            	this.zero_position[0]=i;
	            	this.zero_position[1]=j;
	            }
	        }
	    }
	}
	
	// getter for the puzzle matrix
	private Integer[][] get_puzzle(){
		return this.puzzle;
	}
	
	// setter for the empty puzzle tile
	private void set_pos() {
		for (int i = 0; i < puzzle_size; i++) {
	        for (int j = 0; j < puzzle_size; j++) {
	            if (this.puzzle[i][j] == 0) {
	            	this.zero_position[0]=i;
	            	this.zero_position[1]=j;
	            }
	        }
	    }
	}
	
	// compare if the given puzzle equal to this puzzle
	private boolean is_equal(Integer p2[][]) {
		boolean resault = true;
		for (int i = 0; i < puzzle_size; i++) {
	        for (int j = 0; j < puzzle_size; j++) {
	        	if (this.puzzle[i][j] != p2[i][j]) {
	        		resault = false;
	        		break;
	        	}
	        }
	    }
		return resault;
	}
	
	// move the empty puzzle tile in one direction
	private Puzzle move(String dir) {
		Integer[][] p = Matrix_Util.clone_array(this.puzzle);
		int tmp;
		Integer new_pos[] = this.zero_position.clone();
		switch (dir) {
		case "left": {
			if (new_pos[1]>0) {
				new_pos[1]--;
			}
			break;
		}
		case "right": {
			if (new_pos[1]<this.puzzle_size-1) {
				new_pos[1]++;
			}
			break;
		}
		case "up": {
			if (new_pos[0]>0) {
				new_pos[0]--;
			}
			break;
		}
		case "down": {
			if (new_pos[0]<this.puzzle_size-1) {
				new_pos[0]++;
			}
			break;
		}
		default: return null;
		}
		tmp = p[new_pos[0]][new_pos[1]];
		p[new_pos[0]][new_pos[1]] = 0;
		p[this.zero_position[0]][this.zero_position[1]] = tmp;
		Puzzle result;
		try {
			result = new Puzzle(p,goal);
		} catch (Exception e) {
			return null;
		}
		return result;
	}
	
	// get the possible moves as a list of strings
	private ArrayList<String> get_posible_dir() {
		ArrayList<String> result = new ArrayList<String>();
		if (zero_position[0]<this.puzzle_size-1) result.add("down");
		if (zero_position[1]<this.puzzle_size-1) result.add("right");
		if (zero_position[0]>0) result.add("up");
		if (zero_position[1]>0) result.add("left");
		return result;
	}
	
	// successors function // the operator is in the puzzle (you can get it with get_operator)
	@Override
	public ArrayList<Operation> successors() {
		ArrayList<Operation> resault = new ArrayList<Operation>();
		ArrayList<String> tmp = this.get_posible_dir();
		for (String p : tmp) {
			try {
				resault.add(new Operation(this.move(p),p));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return resault;
	}
	
	@Override
	public boolean is_goal() {
		return this.is_equal(goal.get_puzzle());
	}
	
	private Puzzle create_random_new_goal(int moves) throws Exception {
		Integer[][] p = Matrix_Util.clone_array(this.puzzle);
		Puzzle res_puzzle = new Puzzle(p,goal=null);
		int counter = 0;
		ArrayList<String> list;
		while (counter < moves) {
			Random rand = new Random();
			list = res_puzzle.get_posible_dir();
			res_puzzle = res_puzzle.move(list.get(rand.nextInt(list.size())));
			counter++;
		}
		return res_puzzle;
	}
	
	@Override
	public String toString() {
		int form_int = (int) Math.log10(this.puzzle_size*this.puzzle_size) + 1;
		String result = "Transform\n";
		String form = "%"+form_int+"d";
		for (int i = 0; i < this.puzzle.length; i++) {
	        for (int j = 0; j < this.puzzle[i].length; j++) {
	        	result += String.format(form, this.puzzle[i][j]);
	        	if (j < this.puzzle[i].length-1) result += " ";
	        }
	        if (i < this.puzzle.length-1) result += "\n";
	    }
		result += "\ninto\n";
		for (int i = 0; i < this.goal.puzzle.length; i++) {
	        for (int j = 0; j < this.goal.puzzle[i].length; j++) {
	        	result += String.format(form, this.goal.puzzle[i][j]);
	        	if (j < this.goal.puzzle[i].length-1) result += " ";
	        }
	        if (i < this.goal.puzzle.length-1) result += "\n";
	    }
		return result;
	}
	
	@Override
	public boolean dead_end(IProblem e) {
		return this.toString().equals(e.toString());
	}
	
	@Override
	public boolean equals(IProblem e) {
		return this.toString().equals(e.toString());
	}
	
	private void read_from_String(ArrayList<String> st) throws Exception {
		boolean puzzle_done = false;
		boolean goal_done = false;
		for (String s: st) {
			if (s.startsWith("matrix_size")) {
				if (!puzzle_done){
					if (s.contains("=")) {
						try {
							if (!s.split("=")[0].trim().equals("matrix_size") || s.split("=").length>2) throw new Exception ("Problem file is corrupted");
							int size = Integer.parseInt(s.split("=")[1].trim());
							if (size>0) this.puzzle_size = size;
							else throw new Exception ("matrix_size value is wrong, input a value bigger than 0");
							this.puzzle = new Integer[this.puzzle_size][this.puzzle_size];
							this.set_random();
							puzzle_done = true;
						}
						catch (Exception e) {
							throw new Exception ("matrix_size has no value");
						}
					} else {
						throw new Exception ("matrix_size has no value");
					}
				} else {
					throw new Exception ("puzzle exists more than once");
				}
				
			}
			else if (s.startsWith("goal_moves")) {
				if (!goal_done){
					if (s.contains("=")) {
						try {
							if (!s.split("=")[0].trim().equals("goal_moves") || s.split("=").length>2) throw new Exception ("Problem file is corrupted");
							int goal_moves = Integer.parseInt(s.split("=")[1].trim());
							if (goal_moves>0) this.goal = this.create_random_new_goal(goal_moves);
							else throw new Exception ("goal_moves value is wrong, input a value bigger than 0");
							goal_done = true;
						}
						catch (Exception e) {
							throw new Exception ("goal_moves has no value");
						}
					} else {
						throw new Exception ("goal_moves has no value");
					}
				} else {
					throw new Exception ("goal exists more than once");
				}
			}
			else if (s.startsWith("matrix")) {
				if (!puzzle_done) {
					if (s.contains("=")) {
						if (!s.split("=")[0].trim().equals("matrix") || s.split("=").length>2) throw new Exception ("Problem file is corrupted");
						try {
							int n = 0;
							String matrix_lines[] = s.split("=")[1].split("\\{|\\}");
							ArrayList<String> matrix = new ArrayList<String>();
							for (String m_l: matrix_lines) {
								if(m_l.matches(".*\\d.*")) {
									matrix.add(m_l);
									n++;
								}
							}
							this.puzzle_size = n;
							this.puzzle = new Integer[this.puzzle_size][this.puzzle_size];
							for (int i=0; i<this.puzzle.length; i++) {
								String[] tmp = matrix.get(i).split(",");
								if (tmp.length!=n) throw new Exception("Puzzle matrix is wrong");
								for (int j=0; j<this.puzzle[i].length; j++) {
									this.puzzle[i][j] = Integer.parseInt(tmp[j].trim());
								}
							}
							Matrix_Util.has_matrix_zero(this.puzzle);
							this.set_pos();
							puzzle_done = true;
						}
						catch (Exception e) {
							throw new Exception ("matrix values are wrong");
						}
					} else {
						throw new Exception ("matrix has no value");
					}
				} else {
					throw new Exception ("puzzle exists more than once");
				}
			}
			else if (s.startsWith("goal_matrix")) {
				if (!goal_done) {
					if (s.contains("=")) {
						if (!s.split("=")[0].trim().equals("goal_matrix") || s.split("=").length>2) throw new Exception ("Problem file is corrupted");
						try {
							int n = 0;
							String matrix_lines[] = s.split("=")[1].split("\\{|\\}");
							ArrayList<String> g_matrix = new ArrayList<String>();
							for (String m_l: matrix_lines) {
								if(m_l.matches(".*\\d.*")) {
									g_matrix.add(m_l);
									n++;
								}
							}
							Integer[][] goal_matrix = new Integer[this.puzzle_size][this.puzzle_size];
							for (int i=0; i<goal_matrix.length; i++) {
								String[] tmp = g_matrix.get(i).split(",");
								if (tmp.length!=n) throw new Exception("Goal Matrix is wrong");
								for (int j=0; j<goal_matrix[i].length; j++) {
									goal_matrix[i][j] = Integer.parseInt(tmp[j].trim());
								}
							}
							if (n!=this.puzzle_size) throw new Exception("Goal Matrix is not matching with the Puzzle matrix");
							Matrix_Util.has_matrix_zero(goal_matrix);
							this.goal = new Puzzle(goal_matrix,goal=null);
							goal_done = true;
						}
						catch (Exception e) {
							throw new Exception ("goal_matrix values are wrong");
						}
					} else {
						throw new Exception ("goal_matrix has no value");
					}
				} else {
					throw new Exception ("goal exists more than once");
				}
			}
			else if (s.length()!=0) if (s.getBytes()[0]=='%') continue;
		}
	}
}