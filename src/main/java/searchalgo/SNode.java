/**
 * 
 */
package searchalgo;

import java.util.ArrayList;

import problem.IProblem;
import problem.Operation;

/**
 * @author Ahmad_Khalil
 *
 */
public class SNode {
	private SNode backpointer;
	public Operation operation;
	public int depth;
	private boolean mark;
	
	public SNode(Operation o, int d) {
		this.operation = o;
		this.depth = d;
		this.mark = false;
	}
	public SNode(IProblem p) {
		this.operation = new Operation(p,"");
		this.depth = 0;
		this.mark = false;
	}
	public SNode() {
		this.operation = null;
		this.depth = 0;
		this.mark = false;
	}
	
	public void set_backpointer(SNode setter) {
		this.backpointer = setter;
	}
	public SNode get_backpointer() {
		return this.backpointer;
	}
	public IProblem get_IProblem() {
		return this.operation.get_successor_problem();
	}
	
	public ArrayList<SNode> sucsesors_rechnen(){
		ArrayList<SNode> result = new ArrayList<SNode>();
		for (Operation p : operation.get_successor_problem().successors()) {
			SNode tmp = new SNode(p,this.depth+1);
			tmp.set_backpointer(this);
			result.add(tmp);
		}
		//here can sort the list of any parameter you want before return it
		return result;
	}
	
	public boolean dead_end() {
		SNode compare = this.backpointer;
		while (compare!=null) {
			if (this.operation.get_successor_problem().dead_end(compare.get_IProblem())) return true;
			else compare = compare.backpointer;
		}
		return false;
	}
	
	public String toString(){
		return "Node_" + Integer.toString(this.hashCode());
	}
	
	public String get_info() {
		String result = "";
		result += "Node depth: " + this.depth + System.lineSeparator();
		if (this.backpointer!=null) {
			if (this.operation.get_successor_problem().is_goal()) {
				result += "Goal:";
			} else {
				result += "Current problem:";
			}
		} else {
			result += "Start problem:";
		}
		result += System.lineSeparator() + this.operation.get_successor_problem().toString() + System.lineSeparator();
		if (this.backpointer!=null) {
			result += "Backpointer: " + this.backpointer.toString() + System.lineSeparator();
			result += "Operators to root:" + System.lineSeparator();
			String res = this.operation.get_operator().get_operator();
			SNode c = this.backpointer;
			while (c!=null) {
				if (c.operation.get_operator().get_operator()!="")
				res = c.operation.get_operator().get_operator() + ", " + res;
				c=c.backpointer;
			}
			result += res;
		} else {
			result += "Root Node" + System.lineSeparator();
		}
		return result;
	}
	public String get_info_html() {
		String result = "<html><body>";
		//Add node depth
		result += "<p>" + "Node depth: " + this.depth + "</p>";
		//Add Node type
		result += "<p>";
		if (this.backpointer!=null) {
			if (this.operation.get_successor_problem().is_goal()) {
				result += "Goal:";
			} else {
				result += "Current problem:";
			}
		} else {
			result += "Start problem:";
		}
		result += "</p>";
		//Add problem string
		result += "<p>" + this.operation.get_successor_problem().toString() + "</p>";
		//Add backpointers
		result += "<p>";
		if (this.backpointer!=null) {
			result += "Operators to root:" + System.lineSeparator();
			String res = this.operation.get_operator().get_operator();
			SNode c = this.backpointer;
			while (c!=null) {
				if (c.operation.get_operator().get_operator()!="")
				res = c.operation.get_operator().get_operator() + ", " + res;
				c=c.backpointer;
			}
			result += res;
		} else {
			result += "Root Node" + System.lineSeparator();
		}
		result += "</p>";
		//End the html file
		result = result + "</body></html>";
		return result;
	}
	public boolean is_mark() {
		return mark;
	}
	public void set_mark(boolean mark) {
		this.mark = mark;
	}
}
