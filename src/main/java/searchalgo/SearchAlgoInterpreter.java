/**
 * 
 */
package searchalgo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Scanner;

import graph.GSGraph_Window;
import problem.IProblem;
import problem.IProblem_reader;
import utilities.GSEventBus;
import utilities.Node_Info;
import utilities.File_Util;
import utilities.events.ClearGraphEvent;

/**
 * @author ahmad_khalil
 *
 */
public class SearchAlgoInterpreter {
	private final GSEventBus<SNode> eventBus = new GSEventBus<>();
	private GSGraph_Window<SNode> graph_frame;
	private Algo_Thread thread;
	private Algo_Steps status;
	private int t_o;
	private int cleanup_count;
	private int max_depth;
	protected SearchAlgo search_algo;
	private IProblem start_problem;
	private SNode start_node;
	protected SNode goal;
	private String file_location;
	private String config_file_location;
	private boolean file_loaded;

	public SearchAlgoInterpreter() throws Exception {
		this.config_file_location = "./conf/config";
		try {
			this.t_o = File_Util.read_file_to_get_int(this.config_file_location, "time_out");
			this.cleanup_count = File_Util.read_file_to_get_int(this.config_file_location, "cleanup_count");
			this.max_depth = File_Util.read_file_to_get_int(this.config_file_location, "max_depth");
		}
		catch (Exception e) {
			this.t_o = 100;
			this.cleanup_count = 1;
			this.max_depth = 10;
			this.status=null;
			throw e;
		}
		this.thread_init();
		this.graph_frame = new GSGraph_Window<SNode>(this.eventBus);
	}
	
	public synchronized void one_step() throws Exception {
		/*
		 * 
		 */
		if (!this.is_ready()) return;
		if (!this.is_finish()) {
			this.status = this.search_algo.run_one_step(); //Heir is the one step run
		} else {
			this.goal = this.search_algo.get_goal();
			if (this.goal!= null) show_node(this.goal, "Goal");
			else show_node(null);
		}
	}
	
	public synchronized void more_steps(int n) throws Exception {
		if (!this.is_ready()) return;
		int s = n;
		while (s>=1 & !this.is_finish()) {
			this.status = this.search_algo.run_one_step();
			s--;
		}
		if (this.is_finish()) {
			this.goal = this.search_algo.get_goal();
			if (this.goal!= null) show_node(this.goal, "Goal");
			else show_node(null);
		}
	}
	
	public synchronized void run_upto_loop() throws Exception {
		if (!this.is_ready()) return;
		if (!this.is_finish()) {
			this.status = this.search_algo.run_upto_loop(); //Heir is the one Loop run
		} else {
			this.goal = this.search_algo.get_goal();
			if (this.goal!= null) show_node(this.goal, "Goal");
			else show_node(null);
		}
	}

	public boolean is_ready() {
		return this.search_algo!=null & this.start_problem!=null;
	}
	public boolean is_finish() {
		if (this.status!=null) return this.status!=Algo_Steps.Running;
		else return false;
	}
	public String get_currnet_step() throws Exception {
		if (this.search_algo!=null) return this.search_algo.get_step_string();
		else throw new Exception("Set search algorithm");
	}
	
	public String get_search_algo_html() throws Exception {
		if (this.search_algo!=null) return search_algo.toString();
		else return null;
	}
	public void set_search_algo(String sa) throws Exception {
		this.status=null;
		this.goal = null;
		this.thread_init();
		this.send_clear_graph_event_to_bus();
		try {
			Class<?> prob = Class.forName("searchalgo." + sa);
			Constructor<?> con = prob.getConstructor(GSEventBus.class);
			this.search_algo = (SearchAlgo) con.newInstance(this.eventBus);
		} catch (ClassNotFoundException e) {
			throw new Exception("False algorithm");
		}
		if (this.start_problem!=null) {
			this.search_algo.set_start_problem(this.start_problem);
			this.start_node = this.search_algo.get_start_problem();
		}
		this.search_algo.set_cleanup_count(this.cleanup_count);
		this.search_algo.set_max_depth(this.max_depth);
	}

	public String get_start_problem() {
		if (start_problem!=null) return start_problem.toString();
		else return null;
	}
	public String get_start_problem_class() {
		if (start_problem!=null) return start_problem.getClass().getSimpleName();
		else return null;
	}
	public SNode get_currnet_node() throws Exception {
		if (this.search_algo!=null) return this.search_algo.get_current();
		else throw new Exception("Set Search Algorithm");
	}
	public SNode get_goal() {
		return this.goal;
	}
	public void set_start_problem(IProblem start_problem) {
		this.start_problem = start_problem;
	}
	public void set_start_problem(String file_loc) throws Exception {
		try {
			this.start_problem = IProblem_reader.read(file_loc);
			this.file_location = file_loc;
			if (this.search_algo!=null) {
				this.set_search_algo(this.search_algo.getClass().getSimpleName());
				this.search_algo.set_start_problem(start_problem);
				this.start_node = this.search_algo.get_start_problem();
				this.file_location = file_loc;
			}
		} catch (Exception e) {
			this.status=null;
			this.start_problem = null;
			throw e;
		}
		this.goal = null;
		this.file_loaded=false;
	}
	
	public int get_timeout() {
		return this.t_o;
	}
	public void set_timeout(int t_o) throws Exception {
		this.t_o = t_o;
		try {
			File_Util.read_file_edit_line(this.config_file_location, "time_out = ", "time_out = " + t_o);
		} catch (Exception e) {
			throw new Exception("Time out value is wrong");
		}
	}
	public int get_steps_counter() {
		if (this.search_algo!=null) return this.search_algo.get_steps_counter();
		else return 0;
	}
	public int get_cleanup_count() {
		return this.cleanup_count;
	}
	public void set_cleanup_count(int n) throws Exception {
		this.cleanup_count = n;
		if (this.search_algo!=null) this.search_algo.set_cleanup_count(this.cleanup_count);
		try {
			File_Util.read_file_edit_line(this.config_file_location, "cleanup_count = ", "cleanup_count = " + n);
		} catch (Exception e) {
			throw new Exception("CleanUp count value is wrong");
		}
		
	}
	public String get_problem_file_location() {
		return file_location;
	}
	public int get_max_depth() {
		return this.max_depth;
	}
	public void set_max_depth(int n) throws Exception {
		this.max_depth = n;
		if (this.search_algo!=null) this.search_algo.set_max_depth(this.max_depth);
		try {
			File_Util.read_file_edit_line(this.config_file_location, "max_depth = ", "max_depth = " + n);
		} catch (Exception e) {
			throw new Exception("Max depth value is wrong");
		}
	}
	
	public Thread thread_init() {
		this.thread = new Algo_Thread(this);
		return this.thread;
	}
	public void run_thread() {
		this.thread.start();
	}
	public void run_thread(int n) {
		this.thread.set_steps_count(n);
		this.thread.start();
	}
	public void stop_thread() {
		this.thread.stopThread();
	}
	public void suspend_thread() {
		try {
			this.thread.pauseThread();
		} catch (InterruptedException e) {
			return;
		}
	}
	public void resume_thread() {
		this.thread.resumeThread();
	}
	public boolean is_thread_interrupted() {
		return this.thread.isInterrupted();
	}
	
	public void show_current_node() throws Exception {
		if (this.search_algo!=null) {
			if (this.search_algo.get_current()!=null) {
				this.show_node(this.search_algo.get_current());
			}
		} else {
			throw new Exception("Set Search Algorthm");
		}
	}
	public ArrayList<SNode> get_OPEN_list() {
		if (this.search_algo!=null) {
			return this.search_algo.get_OPEN();
		} else return null;
	}
	public ArrayList<SNode> get_CLOSED_list() {
		if (this.search_algo!=null) {
			return this.search_algo.get_CLOSED();
		} else return null;
	}
	public ArrayList<SNode> get_successor_nodes() {
		if (this.search_algo!=null) {
			return this.search_algo.get_successor_nodes();
		} else return null;
	}
	public void update_open(SNode n) throws Exception {
		if (this.search_algo!=null) {
			this.search_algo.update_open(n);
		} else throw new Exception("Set Search Algorthm");
	}
	public boolean is_update_pos() {
		if (this.search_algo!=null) return this.search_algo.is_update_pos();
		else return false;
	}
	
	public void show_graph() throws Exception {
		if (graph_frame!=null) {
			graph_frame.setVisible(true);
		} else {
			throw new Exception("There is no graph");
		}
	}
	
	//Show node begin
	public void show_node(SNode node) {
		if (node!=null) {
			new Node_Info(node);
		}
		else show_node(null,"Fail");
	}
	private void show_node(SNode node, String s) {
		new Node_Info(node,s);
	}
	public void show_start_node() throws Exception {
		if (this.start_node!=null) {
			show_node(this.start_node, "Start node: " + this.start_node.toString());
		} else {
			throw new Exception("Set start problem and search algorithm");
		}
	}
	//Show node end
	
	public void reset() {
		this.goal = null;
		this.status = null;
		this.send_clear_graph_event_to_bus();
		this.thread_init();
		if (this.start_problem!=null) {
			this.search_algo.set_start_problem(this.start_problem);
		}
		if (this.search_algo!=null) {
			String s = this.search_algo.getClass().getSimpleName();
			try {
				this.set_search_algo(s);
			} catch (Exception e) {
				return;
			}
		}
		this.search_algo.set_start_problem(this.start_problem);
		this.start_node = this.search_algo.get_start_problem();
	}
	public void save_file(String my_file) throws Exception {
		try {
			File file = new File(my_file);
			file.createNewFile();
			FileWriter fw = new FileWriter(my_file);
            BufferedWriter out = new BufferedWriter(fw);
            if (this.file_loaded) {
            	File f = new File(this.file_location);
    		    Scanner my_reader = new Scanner(f);
    		    while (my_reader.hasNextLine()) {
    		    	String data = my_reader.nextLine();
    		    	if (data.contains("steps_count")) {
    		    		out.write("steps_count = " + this.search_algo.get_steps_counter() + System.lineSeparator());
    		    	} else if (data.contains("search_aglo")) {
    		    		out.write("search_aglo = " + this.search_algo.getClass().getSimpleName() + System.lineSeparator());
    		    	} else {
    		    		out.write(data + System.lineSeparator());
    		    	}
    		    }
    		    my_reader.close();
    		    out.flush();
                out.close();
            } else {
            	File f = new File(this.file_location);
    		    Scanner my_reader = new Scanner(f);
    		    while (my_reader.hasNextLine()) {
    		        String data = my_reader.nextLine();
    		        out.write(data + System.lineSeparator());
    		    }
    		    my_reader.close();
    		    out.write("search_aglo = " + this.search_algo.getClass().getSimpleName() + System.lineSeparator());
    		    out.write("steps_count = " + this.search_algo.get_steps_counter() + System.lineSeparator());
                out.flush();
                out.close();
            }
		} catch (IOException e) {
			throw new Exception("Error by reading and writing the file");
		}
	}
	public void load_file(String file_loc) throws Exception {
		this.goal = null;
		this.status = null;
		send_clear_graph_event_to_bus();
		this.thread_init();
		int run_times = 0;
		ArrayList<String> lines = File_Util.read_file(file_loc);
		ArrayList<String> problem_lines = new ArrayList<String>();
		for (String s: lines) {
			if (s.contains("search_aglo")) {
				try {
					this.set_search_algo(s.split("=")[1].trim());
				}
				catch (Exception e) {
					this.search_algo=null;
					this.start_problem=null;
					throw new Exception("File is corrupted");
				}
			}
			else if (s.contains("steps_count")) {
				try {
					run_times = Integer.parseInt(s.split("=")[1].trim());
					if (run_times<0) throw new Exception("File is corrupted");
				}
				catch (Exception e) {
					this.search_algo=null;
					this.start_problem=null;
					throw new Exception("File is corrupted");
				}
			}
			else {
				problem_lines.add(s);
			}
		}
		try {
			this.start_problem = IProblem_reader.read(problem_lines);
		}
		catch (Exception e) {
			this.search_algo=null;
			this.start_problem=null;
			throw e;
		}
		this.search_algo.set_start_problem(this.start_problem);
		this.start_node = this.search_algo.get_start_problem();
		this.more_steps(run_times);
		this.file_loaded=true;
		this.file_location = file_loc;
	}
	
	public void send_clear_graph_event_to_bus() {
		eventBus.post(new ClearGraphEvent<SNode>());
	}
}
