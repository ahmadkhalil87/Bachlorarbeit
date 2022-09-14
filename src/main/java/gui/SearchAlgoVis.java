package gui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTextPane;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.border.LineBorder;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import com.google.common.reflect.ClassPath;
import searchalgo.SNode;
import searchalgo.SearchAlgoInterpreter;
import utilities.GSEventBus;

public class SearchAlgoVis {

	private JFrame frame;
	private JMenuBar menuBar;
	private JMenu mb_file;
	private JMenuItem mb_file_exit;
	private JSpinner step_input;
	private JButton run_bt;
	private JButton step_bt;
	private JButton pause_bt;
	private JButton stop_bt;
	private JPanel var_panel;
	private Container start_state_show_pl;
	private JPanel south_panel;
	private JMenuItem mb_set_problem;
	private JSeparator mb_sp2;
	private JPanel algo_panel;
	private JPanel bt_panel;
	private JPanel bt1_panel;
	private JPanel bt2_panel;
	private JPanel bt3_panel;
	private JPanel right_panel;
	private JTextPane algo_tp;
	private JLabel status_lbl;
	private JLabel steps_count_lb;
	private JMenu mb_config;
	private JButton run_one_loop_bt;
	private JButton reset_algo_bt;
	private JButton single_step_bt;
	private JButton start_state_show;
	private JMenu mm_graph;
	private JMenuItem mb_show_graph;
	private JMenu mb_help;
	private JMenuItem mb_about;
	private JMenu mb_set_algo;
	private JMenuItem mb_save;
	private JMenuItem mb_load;
	private JSeparator mb_sp1;
	private JLabel memory_lb;
	private JPanel node_n_pl;
	private JPanel show_node_n_pl;
	private JPanel open_list_pl;
	private JLabel node_n_lb;
	private JPanel open_list_cb_pl;
	private JButton show_node_n_bt;
	private JPanel open_panel_bts_pl;
	private JButton open_show_bt;
	private JButton open_update_bt;
	private JComboBox<SNode> open_list_cb;
	private JLabel open_list_lb;
	private JPanel closed_list_pl;
	private JLabel closed_list_lb;
	private JComboBox<SNode> closed_list_cb;
	private JButton closed_list_show_bt;
	private JButton succ_nodes_show_bt;
	private JPanel succ_nodes_show_pl;
	private JPanel succ_nodes_pl;
	private JLabel succ_nodes_lb;
	private JComboBox<SNode> succ_nodes_cb;
	private JPanel succ_nodes_cb_pl;
	private JPanel closed_list_show_pl;
	private JPanel closed_list_cb_pl;
	private JSeparator separator;
	private JSeparator separator_1;
	private JSeparator separator_2;
	private JSeparator separator_3;
	
	private GUI_Thread gui_thread;
	private Memory_Check_Thread mem_thread;
	private SearchAlgoInterpreter interpreter;
	private Settings setting_window;
	private About about_window;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SearchAlgoVis window = new SearchAlgoVis();
					window.frame.setVisible(true);}
				catch (Exception e) {
					JOptionPane.showMessageDialog(new JFrame(), e);
					System.exit(1);
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SearchAlgoVis() {
		try {
			initialize();
			this.gui_thread = new GUI_Thread(algo_tp,steps_count_lb,interpreter,this);
			this.mem_thread = new Memory_Check_Thread(this.interpreter,this.gui_thread,this,this.memory_lb);
			this.mem_thread.start();
		}
		catch (OutOfMemoryError e) {
			JOptionPane.showMessageDialog(frame, "Out of Memory");
			return;
		}
		catch (Exception e) {
			JOptionPane.showMessageDialog(frame, e);
			return;
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		try {
			this.interpreter = new SearchAlgoInterpreter();
			this.setting_window = new Settings(interpreter,200,200);
			this.about_window = new About();
		} catch (Exception e2) {
			return;
		}
		
		frame = new JFrame("Search Algorithm Visualiser");
		frame.setBounds(0, 0, 800, 700);
		frame.setMinimumSize(new Dimension(800, 700));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout());
		
		//algo panel
		algo_panel = new JPanel();
		algo_panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		algo_panel.setLayout(new GridLayout());
		frame.getContentPane().add(algo_panel, BorderLayout.CENTER);
		
		algo_tp = new JTextPane();
		algo_tp.setContentType("text/html");
		algo_tp.setText("");
		algo_tp.setEditable(false);
		
		JScrollPane algo_tp_scrollPane = new JScrollPane(algo_tp);
		algo_tp_scrollPane.setAutoscrolls(false);
		algo_panel.add(algo_tp_scrollPane);
		
		//south panel
		south_panel = new JPanel();
		south_panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		south_panel.setLayout(new BorderLayout());
		frame.getContentPane().add(south_panel, BorderLayout.SOUTH);
		
		status_lbl = new JLabel("");
		south_panel.add(status_lbl, BorderLayout.WEST);
		
		steps_count_lb = new JLabel("");
		steps_count_lb.setHorizontalAlignment(0);
		south_panel.add(steps_count_lb, BorderLayout.CENTER);
		
		memory_lb = new JLabel();
		south_panel.add(memory_lb,BorderLayout.EAST);
		
		//right panel
		right_panel = new JPanel();
		BorderLayout bl_right_panel = new BorderLayout();
		right_panel.setLayout(bl_right_panel);
		frame.getContentPane().add(right_panel, BorderLayout.EAST);
		
		//buttons panel
		bt_panel = new JPanel();
		bt_panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		bt_panel.setLayout(new BoxLayout(bt_panel, BoxLayout.Y_AXIS));
		right_panel.add(bt_panel, BorderLayout.NORTH);
		
		//buttons panel line 1
		bt1_panel = new JPanel();
		bt1_panel.setLayout(new FlowLayout());
		bt_panel.add(bt1_panel);
		
		run_bt = new JButton("Run");
		run_bt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				initialize_gui_thread();
				interpreter.thread_init();
				status_lbl.setText("Algorithm is running");
				pause_bt.setText("Suspend");
				activate_run_modus();
				reset_algo_variable();
				gui_thread.start();
				interpreter.run_thread();
			}
		});
		run_bt.setEnabled(false);
		bt1_panel.add(run_bt);
		
		pause_bt = new JButton("Suspend");
		pause_bt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (pause_bt.getText().equalsIgnoreCase("Suspend")){
					try {
						gui_thread.pauseThread();
						interpreter.suspend_thread();
					} catch (InterruptedException e1) {
						return;
					}
					status_lbl.setText("Algorithm suspended");
					pause_bt.setText("Resume");
					activate_pause_modus();
					update_algo_variables();
				}
				else {
					status_lbl.setText("Algorithm resumed");
					pause_bt.setText("Suspend");
					activate_run_modus();
					interpreter.resume_thread();
					gui_thread.resumeThread();
					reset_algo_variable();
				}
			}
		});
		pause_bt.setEnabled(false);
		bt1_panel.add(pause_bt);
		
		stop_bt = new JButton("Terminate");
		stop_bt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				status_lbl.setText("Algorithm is terminated");
				pause_bt.setText("Suspend");
				deactivate_navigate_bts();
				interpreter.stop_thread();
				gui_thread.stopThread();
				reset_algo_variable();
				interpreter.send_clear_graph_event_to_bus();
			}
		});
		stop_bt.setEnabled(false);
		bt1_panel.add(stop_bt);
		
		//buttons panel line 2
		bt2_panel = new JPanel();
		bt2_panel.setLayout(new FlowLayout());
		bt_panel.add(bt2_panel);
		
		single_step_bt = new JButton("Single Step ->");
		single_step_bt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				status_lbl.setText("Single step");
				try {
					interpreter.one_step();
					if (interpreter.is_finish()) {
						interpreter.one_step();
						status_lbl.setText(interpreter.get_currnet_step());
						deactivate_navigate_bts();
					}
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(frame, e1.getMessage());
				}
				update_algo_variables();
			}
		});
		single_step_bt.setEnabled(false);
		bt2_panel.add(single_step_bt);
		
		step_input = new JSpinner();
		step_input.setModel(new SpinnerNumberModel(1, 1, null, 1));
		bt2_panel.add(step_input);
		Component mySpinnerEditor = step_input.getEditor();
		JFormattedTextField jftf = ((JSpinner.DefaultEditor) mySpinnerEditor).getTextField();
		jftf.setColumns(6);
		jftf.setHorizontalAlignment(0);
		
		step_bt = new JButton("Step ->");
		step_bt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				interpreter.thread_init();
				initialize_gui_thread();
				int steps;
				steps = (int) step_input.getValue();
				if (steps<1) {JOptionPane.showMessageDialog(frame, "False Value, enter value bigger than 0");step_input.setValue(1);return;}
				status_lbl.setText(step_input.getValue() + " Step(s) is(are) executed");
				pause_bt.setText("Suspend");
				activate_run_modus();
				reset_algo_variable();
				interpreter.run_thread(steps);
				gui_thread.start();
			}
		});
		step_bt.setEnabled(false);
		bt2_panel.add(step_bt);
		
		//buttons panel line 3
		bt3_panel = new JPanel();
		bt3_panel.setLayout(new FlowLayout());
		bt_panel.add(bt3_panel);
		
		run_one_loop_bt = new JButton("Run upto Loop");
		run_one_loop_bt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				status_lbl.setText("Run upto Loop");
				try {
					interpreter.run_upto_loop();
					if (interpreter.is_finish()) {
						interpreter.one_step();
						deactivate_navigate_bts();
					}
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(frame, e1.getMessage());
				}
				update_algo_variables();
			}
		});
		run_one_loop_bt.setEnabled(false);
		bt3_panel.add(run_one_loop_bt);
		
		reset_algo_bt = new JButton("Reset");
		reset_algo_bt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//reset algo
				interpreter.reset();
				update_algo_variables();
				status_lbl.setText("Algorithm reset");
				activate_navigate_bts();
			}
		});
		reset_algo_bt.setEnabled(false);
		bt3_panel.add(reset_algo_bt);
		
		//variables and start state panel
		var_panel = new JPanel();
		var_panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		var_panel.setForeground(new Color(0, 0, 0));
		var_panel.setLayout(new GridLayout(18, 1, 0, 0));
		right_panel.add(var_panel, BorderLayout.CENTER);
		
		//variables panel
		start_state_show_pl = new JPanel();
		start_state_show_pl.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		var_panel.add(start_state_show_pl);
		
		start_state_show = new JButton("Show start node (s)");
		start_state_show.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					interpreter.show_start_node();
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(frame, "Set start problem" + '\n' + "and set search algorithm in the file menu");
				}
			}
		});
		start_state_show.setEnabled(false);
		start_state_show_pl.add(start_state_show);
		
		separator = new JSeparator();
		var_panel.add(separator);
		
		node_n_pl = new JPanel();
		FlowLayout fl_node_n_pl = (FlowLayout) node_n_pl.getLayout();
		fl_node_n_pl.setVgap(0);
		fl_node_n_pl.setHgap(0);
		var_panel.add(node_n_pl);
		
		node_n_lb = new JLabel("Node (n):               ");
		node_n_lb.setHorizontalAlignment(SwingConstants.CENTER);
		node_n_pl.add(node_n_lb);
		
		show_node_n_pl = new JPanel();
		FlowLayout fl_show_node_n_pl = (FlowLayout) show_node_n_pl.getLayout();
		fl_show_node_n_pl.setHgap(0);
		fl_show_node_n_pl.setVgap(0);
		var_panel.add(show_node_n_pl);
		
		show_node_n_bt = new JButton("Show Node (n)");
		show_node_n_bt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					interpreter.show_current_node();
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(frame,"Set Algorithm!");
				}
			}
		});
		show_node_n_bt.setHorizontalTextPosition(SwingConstants.CENTER);
		show_node_n_bt.setEnabled(false);
		show_node_n_pl.add(show_node_n_bt);
		
		separator_1 = new JSeparator();
		var_panel.add(separator_1);
		
		open_list_pl = new JPanel();
		FlowLayout fl_open_list_pl = (FlowLayout) open_list_pl.getLayout();
		fl_open_list_pl.setVgap(0);
		fl_open_list_pl.setHgap(0);
		var_panel.add(open_list_pl);
		
		open_list_lb = new JLabel("OPEN:                    ");
		open_list_lb.setHorizontalTextPosition(SwingConstants.CENTER);
		open_list_lb.setHorizontalAlignment(SwingConstants.CENTER);
		open_list_pl.add(open_list_lb);
		
		open_list_cb_pl = new JPanel();
		var_panel.add(open_list_cb_pl);
		open_list_cb_pl.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		
		open_list_cb = new JComboBox<SNode>();
		open_list_cb.setEnabled(false);
		open_list_cb.setMinimumSize(new Dimension(150,10));
		open_list_cb.setPreferredSize(new Dimension(150,open_list_cb.getPreferredSize().height));
		open_list_cb_pl.add(open_list_cb);
		
		open_panel_bts_pl = new JPanel();
		var_panel.add(open_panel_bts_pl);
		FlowLayout fl_open_panel_bts_pl = new FlowLayout();
		fl_open_panel_bts_pl.setVgap(0);
		open_panel_bts_pl.setLayout(fl_open_panel_bts_pl);
		
		open_show_bt = new JButton("Show");
		open_show_bt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (open_list_cb.getSelectedIndex() == -1) {
					JOptionPane.showMessageDialog(frame,"OPEN list is empty");
				} else {
					interpreter.show_node((SNode) open_list_cb.getSelectedItem());
				}
			}
		});
		open_show_bt.setEnabled(false);
		open_panel_bts_pl.add(open_show_bt);
		
		open_update_bt = new JButton("Update");
		open_update_bt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (open_list_cb.getSelectedIndex() == -1) {
					JOptionPane.showMessageDialog(frame,"OPEN list is empty");
				} else {
					try {
						interpreter.update_open(open_list_cb.getItemAt(open_list_cb.getSelectedIndex()));
						node_n_lb.setText("Node (n): " + interpreter.get_currnet_node().toString());
						status_lbl.setText("OPEN ist updated");
						update_algo_variables();
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(frame,"The Node does not excist!");
					};
				}
			}
		});
		open_update_bt.setEnabled(false);
		open_panel_bts_pl.add(open_update_bt);
		
		separator_2 = new JSeparator();
		var_panel.add(separator_2);
		
		closed_list_pl = new JPanel();
		var_panel.add(closed_list_pl);
		closed_list_pl.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
		
		closed_list_lb = new JLabel("CLOSED:                    ");
		closed_list_lb.setHorizontalTextPosition(SwingConstants.CENTER);
		closed_list_lb.setHorizontalAlignment(SwingConstants.CENTER);
		closed_list_pl.add(closed_list_lb);
		
		closed_list_cb_pl = new JPanel();
		FlowLayout fl_closed_list_cb_pl = (FlowLayout) closed_list_cb_pl.getLayout();
		fl_closed_list_cb_pl.setVgap(0);
		var_panel.add(closed_list_cb_pl);
		
		closed_list_cb = new JComboBox<SNode>();
		closed_list_cb.setMinimumSize(new Dimension(150,10));
		closed_list_cb.setPreferredSize(new Dimension(150,closed_list_cb.getPreferredSize().height));
		closed_list_cb_pl.add(closed_list_cb);
		closed_list_cb.setEnabled(false);
		
		closed_list_show_pl = new JPanel();
		FlowLayout fl_closed_list_show_pl = (FlowLayout) closed_list_show_pl.getLayout();
		fl_closed_list_show_pl.setVgap(0);
		var_panel.add(closed_list_show_pl);
		
		closed_list_show_bt = new JButton("Show");
		closed_list_show_bt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (closed_list_cb.getSelectedIndex() == -1) {
					JOptionPane.showMessageDialog(frame,"CLOSED list is empty");
				} else {
					interpreter.show_node((SNode) closed_list_cb.getSelectedItem());
				}
			}
		});
		closed_list_show_pl.add(closed_list_show_bt);
		closed_list_show_bt.setEnabled(false);
		
		separator_3 = new JSeparator();
		var_panel.add(separator_3);
		
		succ_nodes_pl = new JPanel();
		var_panel.add(succ_nodes_pl);
		succ_nodes_pl.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
		
		succ_nodes_lb = new JLabel("Successors to process:                    ");
		succ_nodes_lb.setHorizontalTextPosition(SwingConstants.CENTER);
		succ_nodes_lb.setHorizontalAlignment(SwingConstants.CENTER);
		succ_nodes_pl.add(succ_nodes_lb);
		
		succ_nodes_cb_pl = new JPanel();
		FlowLayout fl_succ_nodes_cb_pl = (FlowLayout) succ_nodes_cb_pl.getLayout();
		fl_succ_nodes_cb_pl.setVgap(0);
		var_panel.add(succ_nodes_cb_pl);
		
		succ_nodes_cb = new JComboBox<SNode>();
		succ_nodes_cb.setMinimumSize(new Dimension(150,10));
		succ_nodes_cb.setPreferredSize(new Dimension(150,succ_nodes_cb.getPreferredSize().height));
		succ_nodes_cb_pl.add(succ_nodes_cb);
		succ_nodes_cb.setEnabled(false);
		
		succ_nodes_show_pl = new JPanel();
		FlowLayout fl_succ_nodes_show_pl = (FlowLayout) succ_nodes_show_pl.getLayout();
		fl_succ_nodes_show_pl.setVgap(0);
		var_panel.add(succ_nodes_show_pl);
		
		succ_nodes_show_bt = new JButton("Show");
		succ_nodes_show_bt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (succ_nodes_cb.getSelectedIndex() == -1) {
					JOptionPane.showMessageDialog(frame,"Successors list is empty");
				} else {
					interpreter.show_node((SNode) succ_nodes_cb.getSelectedItem());
				}
			}
		});
		succ_nodes_show_pl.add(succ_nodes_show_bt);
		succ_nodes_show_bt.setEnabled(false);
		
		// Menu Bar elements
		menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		mb_file = new JMenu("File");
		menuBar.add(mb_file);
		
		mb_save = new JMenuItem("Save file");
		mb_save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setDialogTitle("Specify a file to save");   
					fileChooser.setCurrentDirectory(new File("./"));
					int userSelection = fileChooser.showSaveDialog(frame);
					if (userSelection == JFileChooser.APPROVE_OPTION) {
					    File fileToSave = fileChooser.getSelectedFile();
					    interpreter.save_file(fileToSave.getAbsolutePath());
					    status_lbl.setText("File saved");
					}
				}
				catch (NullPointerException err){
					status_lbl.setText("File can not be saved");
					JOptionPane.showMessageDialog(frame,"Select a file");
				}
				catch (Exception er) {
					status_lbl.setText("File can not be saved");
					JOptionPane.showMessageDialog(frame,er.getMessage());
				}
			}
		});
		mb_file.add(mb_save);
		
		mb_load = new JMenuItem("Load file");
		mb_load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					interpreter.load_file(open_file_choser());
					update_algo_variables();
					activate_navigate_bts();
					status_lbl.setText("File loaded");
				}
				catch (NullPointerException err){
					deactivate_all_bts();
					status_lbl.setText("No file has been selected, load a file");
					return;
				}
				catch (Exception er) {
					deactivate_all_bts();
					status_lbl.setText("File can not be loaded");
					JOptionPane.showMessageDialog(frame,"File can not be loaded");
				}
			}
		});
		mb_file.add(mb_load);
		
		mb_sp1 = new JSeparator();
		mb_file.add(mb_sp1);
		
		mb_set_problem = new JMenuItem("Set problem");
		mb_set_problem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String problem_file_location = open_file_choser();
					interpreter.set_start_problem(problem_file_location);
					status_lbl.setText("Problem set (" + interpreter.get_start_problem_class() +")");
					if (interpreter.is_ready()) {
						activate_navigate_bts();
						update_algo_variables();
					}
					else deactivate_all_bts();
					if (interpreter.get_search_algo_html()!=null) update_algo_variables();
				}
				catch (NullPointerException err){
					status_lbl.setText("No file has been selected");
					deactivate_all_bts();
					return;
				}
				catch (NoSuchElementException er) {
					status_lbl.setText("No file has been selected");
					deactivate_all_bts();
					return;
				}
				catch (Exception er) {
					status_lbl.setText("problem can not be loaded");
					deactivate_all_bts();
					JOptionPane.showMessageDialog(frame,er.getMessage());
				}
			}
		});
		mb_file.add(mb_set_problem);
		
		mb_set_algo = new JMenu("Set search algorithm");
		mb_file.add(mb_set_algo);
		
		//search algo menu
		for (final String search_algo_name: get_search_algos()) {
			JMenuItem mb = new JMenuItem(search_algo_name);
			mb_set_algo.add(mb);
			mb.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						interpreter.set_search_algo(search_algo_name);
						status_lbl.setText(search_algo_name + " algorithm");
						if (interpreter.is_ready()) activate_navigate_bts();
						update_algo_variables();
					} catch (Exception e1) {
						status_lbl.setText("algorithm has not been loaded");
						JOptionPane.showMessageDialog(frame,e1.getMessage());
					}
				}
			});
		}
		
		mb_sp2 = new JSeparator();
		mb_file.add(mb_sp2);
		
		mb_file_exit = new JMenuItem("Exit");
		mb_file_exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		mb_file.add(mb_file_exit);
		
		mm_graph = new JMenu("Graph");
		menuBar.add(mm_graph);
		
		mb_show_graph = new JMenuItem("Show graph");
		mb_show_graph.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					interpreter.show_graph();
				} catch (Exception e1) {
					status_lbl.setText("Graph can not be showed");
					JOptionPane.showMessageDialog(frame,e1.getMessage());
				}
			}
		});
		mm_graph.add(mb_show_graph);
		
		mb_config = new JMenu("Config");
		menuBar.add(mb_config);
		
		JMenuItem mb_set_config = new JMenuItem("Settings");
		mb_set_config.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setting_window.show_window();
			}
		});
		mb_config.add(mb_set_config);
		
		mb_help = new JMenu("Help");
		menuBar.add(mb_help);
		
		mb_about = new JMenuItem("About");
		mb_about.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				about_window.show_window();
			}
		});
		mb_help.add(mb_about);
	}
	
	private String open_file_choser() {
		File selectedFile = null;
		JFileChooser fileChooser = new JFileChooser();
		//fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		fileChooser.setCurrentDirectory(new File("./"));
		int res = fileChooser.showOpenDialog(frame);
		if (res == JFileChooser.APPROVE_OPTION) {
		    selectedFile = fileChooser.getSelectedFile();
		}
		return selectedFile.getAbsolutePath();
	}
	
	protected synchronized void update_algo_variables() {
		try {
			if (interpreter.is_finish()) status_lbl.setText(interpreter.get_currnet_step());
			algo_tp.setText(interpreter.get_search_algo_html());
			if (interpreter.get_start_problem()!=null) start_state_show.setEnabled(true);
			else start_state_show.setEnabled(false);
			if (interpreter.get_currnet_node()!=null) {
				show_node_n_bt.setEnabled(true);
				node_n_lb.setText("Node (n): " + interpreter.get_currnet_node().toString());
			} else {
				show_node_n_bt.setEnabled(false);
				node_n_lb.setText("Node (n):               ");
			}
			update_combobox_nodes(open_list_cb,interpreter.get_OPEN_list());
			open_list_lb.setText("OPEN: Current Size (" + Integer.toString(interpreter.get_OPEN_list().size()) + ")");
			update_combobox_nodes(closed_list_cb,interpreter.get_CLOSED_list());
			if (closed_list_cb.getItemCount()>0) closed_list_show_bt.setEnabled(true); else closed_list_show_bt.setEnabled(false);
			closed_list_lb.setText("CLOSED: Current Size (" + Integer.toString(interpreter.get_CLOSED_list().size()) + ")");
			update_combobox_nodes(succ_nodes_cb,interpreter.get_successor_nodes());
			if (interpreter.get_successor_nodes()!=null)
				if (interpreter.get_successor_nodes().size()<=0) {
					succ_nodes_lb.setText("Successors to process: Remaining nodes (0)");
					succ_nodes_show_bt.setEnabled(false);
				} else {
					succ_nodes_lb.setText("Successors to process: Remaining nodes (" + Integer.toString(interpreter.get_successor_nodes().size()) + ")");
					succ_nodes_show_bt.setEnabled(true);
				}
			else {
				succ_nodes_lb.setText("Successors to process:                    ");
				succ_nodes_show_bt.setEnabled(false);
			}
			if (interpreter.is_update_pos()) open_update_bt.setEnabled(true);
			else open_update_bt.setEnabled(false);
			if (open_list_cb.getItemCount()>0) open_show_bt.setEnabled(true); else {open_show_bt.setEnabled(false); open_update_bt.setEnabled(false);}
			steps_count_lb.setText("Steps count: " + String.valueOf(interpreter.get_steps_counter()));
		}
		catch (Exception e) {
			JOptionPane.showMessageDialog(frame,"The search algorithm is not set, set it. \n File -> Set search algorithm");
		}
	}
	private void reset_algo_variable() {
		if (this.interpreter.get_start_problem()!=null) start_state_show.setEnabled(true);
		else start_state_show.setEnabled(false);
		open_update_bt.setEnabled(false);
		open_show_bt.setEnabled(false);
		closed_list_show_bt.setEnabled(false);
		show_node_n_bt.setEnabled(false);
		succ_nodes_show_bt.setEnabled(false);
		open_list_cb.removeAllItems();
		open_list_cb.setEnabled(false);
		closed_list_cb.removeAllItems();
		closed_list_cb.setEnabled(false);
		succ_nodes_cb.removeAllItems();
		succ_nodes_cb.setEnabled(false);
		steps_count_lb.setText("");
		succ_nodes_lb.setText("Successors to process:                    ");
		closed_list_lb.setText("CLOSED:                    ");
		open_list_lb.setText("OPEN:                    ");
		node_n_lb.setText("Node (n):               ");
	}
	
	private void update_combobox_nodes(JComboBox<SNode> cb, ArrayList<SNode> al) {
		cb.removeAllItems();
		if (al != null) {
			for (SNode node: al) {
				cb.addItem(node);
			}
		}
		if (cb.getItemCount()==0) cb.setEnabled(false); else cb.setEnabled(true);
	}

	protected void activate_navigate_bts() {
		step_bt.setEnabled(true);
		run_bt.setEnabled(true);
		single_step_bt.setEnabled(true);
		run_one_loop_bt.setEnabled(true);
		reset_algo_bt.setEnabled(true);
		pause_bt.setEnabled(false);
		stop_bt.setEnabled(false);
	}
	protected void deactivate_navigate_bts() {
		step_bt.setEnabled(false);
		run_bt.setEnabled(false);
		single_step_bt.setEnabled(false);
		run_one_loop_bt.setEnabled(false);
		reset_algo_bt.setEnabled(true);
		pause_bt.setEnabled(false);
		stop_bt.setEnabled(false);
	}
	private void activate_run_modus() {
		step_bt.setEnabled(false);
		run_bt.setEnabled(false);
		single_step_bt.setEnabled(false);
		run_one_loop_bt.setEnabled(false);
		reset_algo_bt.setEnabled(false);
		pause_bt.setEnabled(true);
		stop_bt.setEnabled(true);
	}
	private void activate_pause_modus() {
		step_bt.setEnabled(true);
		run_bt.setEnabled(false);
		single_step_bt.setEnabled(true);
		run_one_loop_bt.setEnabled(true);
		reset_algo_bt.setEnabled(true);
		pause_bt.setEnabled(true);
		stop_bt.setEnabled(false);
	}
	
	protected void deactivate_all_bts() {
		step_bt.setEnabled(false);
		run_bt.setEnabled(false);
		single_step_bt.setEnabled(false);
		run_one_loop_bt.setEnabled(false);
		reset_algo_bt.setEnabled(false);
		open_update_bt.setEnabled(false);
		open_show_bt.setEnabled(false);
		closed_list_show_bt.setEnabled(false);
		open_list_cb.removeAllItems();
		open_list_cb.setEnabled(false);
		closed_list_cb.removeAllItems();
		closed_list_cb.setEnabled(false);
		succ_nodes_cb.removeAllItems();
		succ_nodes_cb.setEnabled(false);
		start_state_show.setEnabled(false);
		show_node_n_bt.setEnabled(false);
		succ_nodes_show_bt.setEnabled(false);
		steps_count_lb.setText("");
		algo_tp.setText("");
		succ_nodes_lb.setText("Successors to process:                    ");
		closed_list_lb.setText("CLOSED:                    ");
		open_list_lb.setText("OPEN:                    ");
		node_n_lb.setText("Node (n):               ");
	}
	
	private ArrayList<String> get_search_algos(){
		/*
		 * scan all the classes in the package searchalgo to find all the classes
		 * which had a constructor with GSEventBus parameter
		 */
		ArrayList<String> result = new ArrayList<String>();
		final ClassLoader loader = Thread.currentThread().getContextClassLoader();
		try {
			for (final ClassPath.ClassInfo info : ClassPath.from(loader).getTopLevelClasses()) {
				try {
					if (info.getName().startsWith("searchalgo.")) {
					    Class<?> clazz = info.load();
					    clazz.getConstructor(GSEventBus.class);
					    result.add(clazz.getSimpleName());
					  }
				}
				catch (Exception e) {
					continue;
				}
			}
		} catch (IOException e) {
			return new ArrayList<String>();
		}
		return result;
	}
	private void initialize_gui_thread() {
		this.gui_thread = new GUI_Thread(this.algo_tp, this.steps_count_lb, this.interpreter,this);
	}
}
