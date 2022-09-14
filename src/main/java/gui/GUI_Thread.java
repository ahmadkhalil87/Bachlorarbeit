package gui;

import javax.swing.JLabel;
import javax.swing.JTextPane;

import searchalgo.SearchAlgoInterpreter;

public class GUI_Thread extends Thread {
	private volatile boolean running = true;
    private volatile boolean paused = false;
    private final Object pauseLock = new Object();
    private SearchAlgoInterpreter interpreter;
    private SearchAlgoVis search_algo_vis;
    private JTextPane algorithm_tp;
    private JLabel counter_lb;
    
    GUI_Thread(JTextPane atp, JLabel c, SearchAlgoInterpreter i, SearchAlgoVis sav) {
    	this.interpreter = i;
    	this.search_algo_vis = sav;
    	this.algorithm_tp = atp;
    	this.counter_lb = c;
    }
    
    @Override
    public void run() {
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
            	this.algorithm_tp.setText(this.interpreter.get_search_algo_html());
            	this.counter_lb.setText("Steps count: " + String.valueOf(this.interpreter.get_steps_counter()));
            	if (this.interpreter.is_thread_interrupted()) {
            		this.search_algo_vis.activate_navigate_bts();
            		this.search_algo_vis.update_algo_variables();
            		return;
            	}
            	int time_out = this.interpreter.get_timeout();
            	if (time_out<10) time_out=10;
                Thread.sleep(time_out); 
            } catch (Exception e){ 
                Thread.currentThread().interrupt();
            }
        }
    	if (this.interpreter.is_finish()) {
    		this.search_algo_vis.deactivate_navigate_bts();
    		this.search_algo_vis.update_algo_variables();
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
}
