/**
 * 
 */
package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

/**
 * @author ahmad_khalil
 *
 */
public class About extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private final int HEIGHT = 440;
	private final int WIDTH = 300;
	private String about_string;
	
	private final Container contentPane;
	
	protected About() {
		super("About");
		new JFrame();
		setSize(this.WIDTH, this.HEIGHT);
		setResizable(false);
		setExtendedState(JFrame.NORMAL);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		contentPane = this.getContentPane();
		initialize();
	}
	
	private void initialize() {
		about_string = "<p style=\"text-align: center;\">This tool was implemented as part of the bachelor thesis<br />Institute for Computer Science<br />Faculty of Electrical Engineering, Computer Science and Mathematics<br />at the Univercity of Paderborn in 2021</p>\r\n"
				+ "<p style=\"text-align: center;\">By</p>\r\n"
				+ "<p style=\"text-align: center;\">Ahmad Hmdi Khalil</p>\r\n"
				+ "<p style=\"text-align: center;\">Supervisor: Dr. Theodor Lettmann<br />Intelligent Systems and Machine Learning Group Heinz Nixdorf Institute and Department of Computer Science Paderborn University</p>\r\n"
				+ "<p style=\"text-align: center;\">This Software is Open Source</p>\r\n"
				+ "<p style=\"text-align: center;\">Home Page: <a href=\"https://git.cs.uni-paderborn.de/khalila/bachelorarbeit/\">GitHub Uni Paderborn</a></p>";
		
		//Center Panel
		JPanel center_pn = new JPanel();
		center_pn.setLayout(new BoxLayout(center_pn, BoxLayout.Y_AXIS));
		contentPane.add(center_pn, BorderLayout.CENTER);
		
		JEditorPane about_text = new JEditorPane();
		about_text.setBackground(this.getBackground());
		about_text.setEditable(false);
		about_text.setContentType("text/html");
		about_text.setText(about_string);
		about_text.addHyperlinkListener(new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent hle) {
                if (HyperlinkEvent.EventType.ACTIVATED.equals(hle.getEventType())) {
                    Desktop desktop = Desktop.getDesktop();
                    try {
                        desktop.browse(hle.getURL().toURI());
                    } catch (Exception ex) {
                        return;
                    }
                }
            }
        });
		center_pn.add(about_text);
		
		//South Panel
		JPanel south_pn = new JPanel();
		south_pn.setLayout(new FlowLayout());
		contentPane.add(south_pn, BorderLayout.SOUTH);
		
		JButton exit_bt = new JButton("Close");
		exit_bt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		south_pn.add(exit_bt);
	}
	
	public void show_window() {
		this.setVisible(true);
	}

}
