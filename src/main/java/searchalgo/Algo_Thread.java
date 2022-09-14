package searchalgo;

public class Algo_Thread extends Thread {

    private volatile boolean running = true;
    private volatile boolean paused = false;
    private final Object pauseLock = new Object();
    private int steps;
    private SearchAlgoInterpreter interpreter;
    
    Algo_Thread(SearchAlgoInterpreter i) {
    	this.interpreter = i;
    	this.steps = -1;
    }
    
    @Override
    public void run() {
    	int counter = 0;
    	while (!this.interpreter.is_finish() & running) {
            synchronized (pauseLock) {
                if (!running) return;
                if (paused) {
                    try {
                        synchronized (pauseLock) {
                            pauseLock.wait();
                        }
                    } catch (InterruptedException ex) {
                        break;
                    }
                    if (!running) return;
                }
            }
            try {
            	if (this.steps!=-1) if (counter>=this.steps) {this.steps=-1;interrupt();return;}
            	this.interpreter.one_step();
            	counter++;
    			this.interpreter.goal = interpreter.search_algo.get_goal();
                Thread.sleep(this.interpreter.get_timeout()); 
            } catch (Exception e){ 
                Thread.currentThread().interrupt();
            }
        }
    	if (this.interpreter.is_finish())
			try {
				this.interpreter.one_step();
			} catch (Exception e) {
				return;
			}
    }

    public void pauseThread() throws InterruptedException {
        paused = true;
    }

    public void resumeThread() {
    	synchronized (pauseLock) {
            paused = false;
            pauseLock.notifyAll();
        }
    }
    
    public void stopThread() {
    	running = false;
    }
    
    public void set_steps_count(int input) {
    	this.steps = input;
    }
}