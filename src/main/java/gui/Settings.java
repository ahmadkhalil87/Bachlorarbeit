/**
 * 
 */
package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import searchalgo.SearchAlgoInterpreter;

/**
 * @author ahmad_khalil
 *
 */
public class Settings extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private final int HEIGHT = 280;
	private final int WIDTH = 200;
	
	private int timeout;
	private int cleanup_count;
	private int max_depth;
	private JSpinner timeout_s;
	private JSpinner cleanup_count_s;
	private JSpinner max_depth_s;
	private final Container contentPane;
	private SearchAlgoInterpreter interpreter;
	
	protected Settings(SearchAlgoInterpreter algo) {
		super("Settings");
		new JFrame();
		setSize(this.HEIGHT, this.WIDTH);
		setResizable(false);
		setExtendedState(JFrame.NORMAL);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		contentPane = this.getContentPane();
		this.interpreter = algo;
		this.timeout = algo.get_timeout();
		this.cleanup_count = algo.get_cleanup_count();
		this.max_depth = algo.get_max_depth();
		initialize();
	}
	protected Settings(SearchAlgoInterpreter i, int x, int y) {
		this(i);
		setLocation(new Point(x, y));
	}
	
	private void initialize() {
		//Center Panel
		JPanel center_pn = new JPanel();
		center_pn.setLayout(new BoxLayout(center_pn, BoxLayout.Y_AXIS));
		contentPane.add(center_pn, BorderLayout.CENTER);
		
		//time out
		JLabel timeout_lb = new JLabel("Time out (Ms): ");
		timeout_lb.setLabelFor(timeout_s);
		center_pn.add(timeout_lb);
		
		timeout_s = new JSpinner();
		timeout_s.setModel(new SpinnerNumberModel(0, 0, null, 1));
		timeout_s.setValue(this.timeout);
		center_pn.add(timeout_s);
		
		Component mySpinnerEditor = timeout_s.getEditor();
		JFormattedTextField jftf = ((JSpinner.DefaultEditor) mySpinnerEditor).getTextField();
		jftf.setColumns(6);
		
		//clean up closed
		JLabel cleanup_count_lb = new JLabel("CleanUp count (Calls): ");
		cleanup_count_lb.setLabelFor(cleanup_count_s);
		center_pn.add(cleanup_count_lb);
		
		cleanup_count_s = new JSpinner();
		cleanup_count_s.setModel(new SpinnerNumberModel(0, 0, null, 1));
		cleanup_count_s.setValue(this.cleanup_count);
		center_pn.add(cleanup_count_s);
		
		mySpinnerEditor = cleanup_count_s.getEditor();
		jftf = ((JSpinner.DefaultEditor) mySpinnerEditor).getTextField();
		jftf.setColumns(6);
		
		//max depth
		JLabel max_depth_lb = new JLabel("Max depth : ");
		max_depth_lb.setLabelFor(max_depth_s);
		center_pn.add(max_depth_lb);
		
		max_depth_s = new JSpinner();
		max_depth_s.setModel(new SpinnerNumberModel(0, 0, null, 1));
		max_depth_s.setValue(this.max_depth);
		center_pn.add(max_depth_s);
		
		mySpinnerEditor = max_depth_s.getEditor();
		jftf = ((JSpinner.DefaultEditor) mySpinnerEditor).getTextField();
		jftf.setColumns(6);
		
		//South Panel buttons
		JPanel south_pn = new JPanel();
		south_pn.setLayout(new FlowLayout());
		contentPane.add(south_pn, BorderLayout.SOUTH);
		
		JButton save_bt = new JButton("Save & Close");
		save_bt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					timeout = (int) timeout_s.getValue();
					interpreter.set_timeout(timeout);
					max_depth = (int) max_depth_s.getValue();
					interpreter.set_max_depth(max_depth);
					cleanup_count = (int) cleanup_count_s.getValue();
					interpreter.set_cleanup_count(cleanup_count);
					dispose();
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(new JFrame(),e1);
				}
			}
		});
		south_pn.add(save_bt);
		
		JButton reset_bt = new JButton("Reset");
		reset_bt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				timeout_s.setValue(timeout);
				max_depth_s.setValue(max_depth);
				cleanup_count_s.setValue(cleanup_count);
			}
		});
		south_pn.add(reset_bt);
		
		JButton exit_bt = new JButton("Close");
		exit_bt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		south_pn.add(exit_bt);
	}
	
	public void show_window() {
		timeout_s.setValue(this.timeout);
		max_depth_s.setValue(this.max_depth);
		cleanup_count_s.setValue(this.cleanup_count);
		this.setVisible(true);
	}

}
