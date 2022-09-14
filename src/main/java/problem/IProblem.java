package problem;

import java.util.ArrayList;

public interface IProblem {

	ArrayList<Operation> successors();

	boolean is_goal();

	String toString();

	boolean equals(IProblem e);
	
	boolean dead_end(IProblem e);

}