package gui;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import searchalgo.SearchAlgoInterpreter;

public class Memory_Check_Thread extends Thread{
    private boolean stop = false;
    private SearchAlgoInterpreter interpreter;
    private GUI_Thread gui_thread;
    private SearchAlgoVis searchalgovis;
    private JLabel memory_status_lb;
    
    Memory_Check_Thread(SearchAlgoInterpreter i, GUI_Thread t, SearchAlgoVis sav, JLabel ms){
    	this.interpreter = i;
    	this.gui_thread = t;
    	this.searchalgovis = sav;
    	this.memory_status_lb = ms;
    }
    
    @Override
    public void run() {
    	String res = "";
	    Runtime rt = Runtime.getRuntime();
	    long usedMB = (rt.totalMemory() - rt.freeMemory()) / 1024 / 1024;
	    long freeMB = (rt.freeMemory()) / 1024 / 1024;
	    long totalMB = rt.totalMemory() / 1024 / 1024;
        while (true) {
        	if (stop) {
        		this.gui_thread.stopThread();
				this.interpreter.stop_thread();
				this.searchalgovis.deactivate_all_bts();
        	}
            try {
            	usedMB = (rt.totalMemory() - rt.freeMemory()) / 1024 / 1024;
				freeMB = (rt.freeMemory()) / 1024 / 1024;
				if (freeMB*1024 <=256) {
        			this.gui_thread.stopThread();
					this.interpreter.stop_thread();
					this.searchalgovis.deactivate_all_bts();
					JOptionPane.showMessageDialog(new JFrame(), "Out of Memory");
					this.stopThread();
				}
				totalMB = rt.totalMemory() / 1024 / 1024;
				res = "Memory: " + usedMB + " MB" + " / " + totalMB + " MB";
				this.memory_status_lb.setText(res);
                Thread.sleep(100); 
            } catch (Exception e){ 
                Thread.currentThread().interrupt();
                this.stopThread();
            }
		}
    }
    
    public void stopThread() {
    	this.stop = true;
    }
}
