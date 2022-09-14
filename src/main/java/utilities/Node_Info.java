/**
 * 
 */
package utilities;

import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import searchalgo.SNode;

/**
 * @author ahmad_khalil
 *
 */
@SuppressWarnings("serial")
public class Node_Info extends JFrame{
	
	public Node_Info(SNode n){
		this(n, n.toString());
	}
	public Node_Info(SNode n,String title){
		this(n,title,false);
	}
	public Node_Info(SNode n,String title, boolean html) {
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setTitle(title);
		this.setBounds(200, 200, 350, 350);
		this.getContentPane().setLayout(new GridLayout(1,1));
		JTextPane node_info = new JTextPane();
		node_info.setEditable(false);
		if (html) {
			node_info.setContentType("text/html");
			if (n!=null) {
				node_info.setText(n.get_info_html());
			} else {
				node_info.setText("Fail");
			}
		}
		else {
			node_info.setFont(new Font("Consolas", Font.PLAIN, 11));
			if (n!=null) {
				node_info.setText(n.get_info());
			} else {
				node_info.setText("Fail");
			}
		}
		JScrollPane node_info_sc = new JScrollPane(node_info);
		this.add(node_info_sc);
		this.setVisible(true);
	}
}
