package searchalgo;

public class Test {
	public static void main( String[] args )
    {
		try {
			SearchAlgoInterpreter test_interpreter = new SearchAlgoInterpreter();
			test_interpreter.set_start_problem("./Queens16");
			test_interpreter.set_search_algo("DFS");
			test_interpreter.run_thread();
		} catch (Exception e) {
			System.out.println("Error");
		}
    }
}
