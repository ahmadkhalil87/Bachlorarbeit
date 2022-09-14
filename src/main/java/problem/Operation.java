/**
 * 
 */
package problem;

/**
 * @author Ahmad_Khalil
 *
 */
public class Operation {
	private Operator operator;
	private Integer cost;
	private IProblem successor_problem;
	
	public Operation(Operator o) {
		this.operator = o;
		this.cost = 1;
	}
	public Operation(int c) {
		this.operator = null;
		this.cost = c;
	}
	public Operation(Operator o, int c) {
		this.operator = o;
		this.cost = c;
	}
	public Operation(IProblem p, Operator o) {
		this.successor_problem = p;
		this.operator = o;
		this.cost = 1;
	}
	public Operation(IProblem p, Operator o, int c) {
		this.successor_problem = p;
		this.operator = o;
		this.cost = c;
	}
	public Operation(IProblem p, String s) {
		this.successor_problem = p;
		this.operator = new Operator(s);
		this.cost = 1;
	}
	public Operation(IProblem p, String s, int c) {
		this.successor_problem = p;
		this.operator = new Operator(s);
		this.cost = c;
	}
	@Override
	public String toString() {
		//TODO output for the Operation
		// here to put the cost or the h function
		return this.successor_problem.toString();
	}
	public Operator get_operator() {
		return this.operator;
	}
	public Integer get_cost() {
		return this.cost;
	}
	public IProblem get_successor_problem() {
		return this.successor_problem;
	}
}
