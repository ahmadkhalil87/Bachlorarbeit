package searchalgo;

import java.util.ArrayList;

import problem.IProblem;

public interface SearchAlgo {

	Algo_Steps run_one_step() throws Exception;

	Algo_Steps run_upto_loop() throws Exception;

	SNode get_current();

	SNode get_start_problem();

	int get_allowed_depth();

	boolean is_finish();

	String get_step_string();
	
	int get_step_int();
	
	int get_steps_counter();
	
	void set_max_depth(int n);

	String toString();

	boolean is_update_pos();
	
	void set_start_problem(IProblem p);
	
	void set_cleanup_count(int n);

	void update_open(SNode n) throws Exception;

	ArrayList<SNode> get_successor_nodes();

	ArrayList<SNode> get_CLOSED();

	ArrayList<SNode> get_OPEN();
	
	SNode get_goal();

}