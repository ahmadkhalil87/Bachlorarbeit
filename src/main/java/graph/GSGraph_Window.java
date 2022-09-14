package graph;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import com.google.common.eventbus.Subscribe;

import utilities.GSEventBus;
import utilities.events.ClearGraphEvent;

public class GSGraph_Window<T> extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private final GSEventBus<T> eventSupplier;
	private GSGraph_Panel<T> panel;
	private final Container contentPane;
	
	public GSGraph_Window(GSEventBus<T> e) {
		super("Search Algorithm Visualiser");
		this.eventSupplier = e;
		this.eventSupplier.register(this);
		new JFrame();
		setBounds(100, 100, 800, 600);
		setMinimumSize(new Dimension(600, 600));
		setExtendedState(JFrame.NORMAL);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		contentPane = this.getContentPane();
		contentPane.setLayout(new BorderLayout());
		this.panel =  new GSGraph_Panel<T>(e);
		panel.setBounds(0, 0, this.getWidth(), this.getHeight());
		contentPane.add(panel,BorderLayout.CENTER);
		JButton btn = new JButton("Reset view");
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GSGraph_Window.this.panel.reset_view();
			}
		});
		btn.setBounds(0, 0, 10, 20);
		contentPane.add(btn,BorderLayout.SOUTH);
	}
	
	@Subscribe
	public synchronized void receiveCloseEvent(ClearGraphEvent<T> e) {
		contentPane.remove(this.panel);
		this.panel =  new GSGraph_Panel<T>(this.eventSupplier);
		panel.setBounds(0, 0, this.getWidth(), this.getHeight());
		contentPane.add(this.panel,BorderLayout.CENTER);
		contentPane.validate();
	}

}
