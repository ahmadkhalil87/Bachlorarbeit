package searchalgo;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.Stack;

import problem.IProblem;
import utilities.GSEventBus;
import utilities.events.GraphInitializedEvent;
import utilities.events.NodeReachedEvent;
import utilities.events.NodeRemovedEvent;
import utilities.events.NodeTypeSwitchEvent;

/**
 * @author Ahmad_Khalil
 *
 */

public class BFS implements SearchAlgo {
	private int cleanup_count = 1;
	private int cleanup_counter;
	private int steps_counter;
	private BFS_Steps step;
	private SNode start;
	private SNode current;
	private SNode goal;
	private ArrayList<SNode> OPEN;
	private Stack<SNode> CLOSED;
	private ArrayList<SNode> successor_problems;
	private SNode successor_node;
	private String algo_text;
	private String algo_html_text;
	//Graph
	private final GSEventBus<SNode> eventbus;
	
	public BFS(GSEventBus<SNode> eventbus) {
		this.eventbus = eventbus;
		this.algo_text = open_algo_file("./conf/BFS");
		this.algo_html_text = color_string(this.algo_text,"");
		this.OPEN = new ArrayList<SNode>();
		this.CLOSED = new Stack<SNode>();
		this.step = BFS_Steps.Enqueue_Start_Problem_in_OPEN;
		this.cleanup_counter = 0;
		this.steps_counter = 0;
	}
	
	@Override
	public Algo_Steps run_one_step() throws Exception {
		if (this.start==null) return Algo_Steps.Fail;
		this.algo_html_text = color_string(this.algo_text,this.step.toString());
		this.step = this._run_one_step(this.step);
		this.steps_counter++;
		switch (this.step) {
		case Fail: {
			return Algo_Steps.Fail;
		}
		case Goal_reached :  {
			return Algo_Steps.Goal_reached;
		}
		default: {
			return Algo_Steps.Running;
		}
		}
	}

	private BFS_Steps _run_one_step(BFS_Steps s) throws Exception {
		BFS_Steps result = null;
		switch (s) {
		case Enqueue_Start_Problem_in_OPEN:{
			this.OPEN.add(0, this.start);
			result = s.next();
			//Graph
			this.eventbus.post(new GraphInitializedEvent<SNode>(this.start));
			break;
		}
		case LOOP_start:{
			result = s.next();
			break;
		}
		case IF_OPEN_empty:{
			if (this.OPEN.isEmpty()) result = s.next();
			else result = BFS_Steps.IF_OPEN_empty_end;
			break;
		}
		case IF_OPEN_empty_true:{
			result = s.next();
			break;
		}
		case return_fail:{
			result = BFS_Steps.Fail;
			break;
		}
		case IF_OPEN_empty_end:{
			result = s.next();
			break;
		}
		case Dequeue_OPEN_n:{
			this.current = this.OPEN.get(0);
			this.OPEN.remove(0);
			result = s.next();
			//Graph
			this.eventbus.post(new NodeTypeSwitchEvent<SNode>(this.current,"node"));
			break;
		}
		case PUSH_n_to_CLOSED:{
			this.CLOSED.push(this.current);
			result = s.next();
			this.eventbus.post(new NodeTypeSwitchEvent<SNode>(this.current,"closed"));
			break;
		}
		case FOR_Loop:{
			if (this.successor_problems == null) {
				this.successor_problems = this.current.sucsesors_rechnen();
				result = s.next();
				this.successor_node = this.successor_problems.get(0);  //here the choice of the successor_node
			} else {
				if (this.successor_problems.isEmpty()) {
					this.successor_problems = null;
					result = BFS_Steps.LOOP_end;
				} else {
					result = s.next();
					this.successor_node = this.successor_problems.get(0);  //here the choice of the successor_node
				}
			}
			break;
		}
		case set_Backpointer:{
			this.successor_node.set_backpointer(this.current);
			result = s.next();
			//Graph
			this.eventbus.post(new NodeReachedEvent<SNode>(this.current,this.successor_node,"node"));
			break;
		}
		case is_Goal_reached:{
			if (successor_node.operation.get_successor_problem().is_goal()) {
				result = s.next();
			} else {
				result = BFS_Steps.is_Goal_reached_end;
			}
			break;
		}
		case is_Goal_reached_true:{
			result = s.next();
			//Graph
			this.eventbus.post(new NodeTypeSwitchEvent<SNode>(this.successor_node,"solution"));
			break;
		}
		case return_goal:{
			result = BFS_Steps.Goal_reached;
			this.goal = this.successor_node;
			break;
		}
		case is_Goal_reached_end:{
			result = s.next();
			break;
		}
		case is_n1_Deadend:{
			if (this.successor_node.dead_end()) {
				result = s.next();
			}
			else result = BFS_Steps.is_n1_Deadend_false;
			break;
		}
		case is_n1_Deadend_true:{
			this.eventbus.post(new NodeTypeSwitchEvent<SNode>(this.successor_node,"deadend"));
			result = s.next();
			break;
		}
		case cleanup_closed_deadend:{
			this.CLOSED.push(this.successor_node);
			if (this.successor_problems.size()<=1) this.cleanup_closed(false);
			else this.cleanup_closed(true);
			result = BFS_Steps.is_n1_Deadend_end;
			break;
		}
		case is_n1_Deadend_false:{
			result = s.next();
			break;
		}
		case Enqueue_n1_to_OPEN:{
			this.OPEN.add(this.OPEN.size(),this.successor_node);
			result = s.next();
			//Graph
			this.eventbus.post(new NodeTypeSwitchEvent<SNode>(this.successor_node,"open"));
			break;
		}
		case is_n1_Deadend_end:{
			result = s.next();
			break;
		}
		case FOR_Loop_end:{
			this.successor_problems.remove(this.successor_node);
			result = BFS_Steps.FOR_Loop;
			break;
		}
		case LOOP_end:{
			result = s.next();
			break;
		}
		default:
			throw new Exception("Wrong Step");
		}
		return result;
	}
	
	private void cleanup_closed(boolean a) {
		if (this.cleanup_counter < this.cleanup_count) {
			this.cleanup_counter++;
			return;
		} else {
			this.cleanup_counter = 1;
			if (this.CLOSED.isEmpty()) return;
			if (this.OPEN.isEmpty()) return;
			for (SNode n: this.OPEN) {
				SNode tmp = n.get_backpointer();
				while (tmp!=null) {
					tmp.set_mark(true);
					tmp = tmp.get_backpointer();
				}
			}
			if (a) {
				this.current.set_mark(true);
				SNode tmp = this.current.get_backpointer();
				while (tmp!=null) {
					tmp.set_mark(true);
					tmp = tmp.get_backpointer();
				}
			}
			ArrayList<SNode> remove_list = new ArrayList<SNode>();
			for (SNode node: this.CLOSED) {
				if (!node.is_mark()) {
					remove_list.add(node);
				}
			}
			for (SNode node: remove_list) {
				this.eventbus.post(new NodeRemovedEvent<SNode>(node));
				this.CLOSED.remove(node);
			}
			for (SNode node: this.CLOSED) {
				node.set_mark(false);
			}
		}
	}
	
	@Override
	public Algo_Steps run_upto_loop() throws Exception {
		if (this.start==null) return Algo_Steps.Fail;
		while (this.step!=BFS_Steps.LOOP_start) {
			if (this.is_finish()) {
				return status();
			}
			this.step = _run_one_step(this.step);
			this.steps_counter++;
		}
		this.algo_html_text = color_string(this.algo_text,this.step.toString());
		this.step = _run_one_step(this.step);
		this.steps_counter++;
		return status();
	}
	
	@Override
	public SNode get_current(){
		return this.current;
	}
	
	@Override
	public SNode get_start_problem() {
		return this.start;
	}
	
	@Override
	public int get_allowed_depth() {
		return 0;
	}
	
	@Override
	public boolean is_finish() {
		return this.step.ordinal()<2;
	}
	
	@Override
	public String get_step_string() {
		return this.step.toString();
	}
	
	@Override
	public int get_step_int() {
		return this.step.ordinal()-2;
	}
	
	@Override
	public int get_steps_counter() {
		return this.steps_counter;
	}
	
	@Override
	public void set_max_depth(int n) {
		return;
	}
	
	@Override
	public String toString(){
		return this.algo_html_text;
	}
	
	@Override
	public boolean is_update_pos() {
		return this.step==BFS_Steps.PUSH_n_to_CLOSED;
	}
	
	@Override
	public void set_start_problem(IProblem p) {
		this.start = new SNode(p);
	}
	
	@Override
	public void set_cleanup_count(int n) {
		this.cleanup_count = n;
	}
	
	@Override
	public void update_open(SNode n) throws Exception {
		if (this.OPEN.contains(n)) {
			int index = this.OPEN.indexOf(n);
			if (!this.OPEN.contains(this.current)) {
				this.OPEN.add(this.OPEN.size(),this.current);
				this.current = n;
				this.OPEN.remove(n);
			} else {
				this.OPEN.remove(index);
				this.OPEN.add(0,n);
			}
		} else {
			throw new Exception("Node does not excist!");
		}
	}
	
	private String open_algo_file(String file) {
		String result = "";
		try {
		      File my_file = new File(file);
		      Scanner my_reader = new Scanner(my_file);
		      while (my_reader.hasNextLine()) {
		        String data = my_reader.nextLine();
		        result += data + "#";
		      }
		      my_reader.close();
		    } catch (FileNotFoundException e) {
		      System.out.println("Error by reading the file");
		      e.printStackTrace();
		    }
		if (!result.contains("//")) {
			result="";
		}
		return result;
	}
	
	private String color_string(String input, String color) {
		if (input.equals("")) {
			return "";
		}
		String result = "<html><body>";
		String[] commands = input.split("//");
		result += "<p>" + commands[0].substring(0,commands[0].length()-1) + "</p>";
		for (String command: commands) {
			String[] tmp = command.split("#");
			String com = "";
			for (int j=1; j<=tmp.length-1; j++) {
				int spaceCount = 0;
				for (char c: tmp[j].toCharArray()) {
					if (c == ' ' | c == '	') {
						if (c == '	') spaceCount+=3;
				        spaceCount++;
				    } else break;
				}
				if (tmp[0].equalsIgnoreCase(color)) {
					com += "<div style=\"padding-left: " +Integer.toString(spaceCount*5) + "px;background-color:red;\">" + tmp[j] + "<br></div>";
				} else {
					com += "<div style=\"padding-left: " +Integer.toString(spaceCount*5) + "px\">" + tmp[j] + "<br></div>";
				}
			}
			result += com;
		}
		result = result + "</body></html>";
		return result;
	}
	
	@Override
	public ArrayList<SNode> get_successor_nodes() {
		return this.successor_problems;
	}
	@Override
	public ArrayList<SNode> get_CLOSED() {
		ArrayList<SNode> result = null;
		if (this.CLOSED!=null) {
			result = new ArrayList<SNode>(this.CLOSED);
			Collections.reverse(result);
		}
		return result;
	}
	@Override
	public ArrayList<SNode> get_OPEN() {
		return this.OPEN;
	}
	
	@Override
	public SNode get_goal() {
		return this.goal;
	}
	
	private Algo_Steps status() {
		switch (this.step) {
		case Fail: {
			return Algo_Steps.Fail;
		}
		case Goal_reached :  {
			return Algo_Steps.Goal_reached;
		}
		default: {
			return Algo_Steps.Running;
		}
		}
	}
	
	protected SNode run() throws Exception {
		while (!is_finish()) {
			this._run_one_step(this.step);
		}
		System.out.println(this.step.toString());
		return this.goal;
	}
}
