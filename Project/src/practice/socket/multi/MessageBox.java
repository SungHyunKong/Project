package practice.socket.multi;


import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;


public class MessageBox extends JDialog implements ActionListener{
	private Container c;
	private JButton bt;
	
	public MessageBox(JFrame parent, String title, String message) {
		super(parent,false);
		setTitle(title);
		c=getContentPane();
		c.setLayout(null);
		JLabel lbl = new JLabel(message);
		lbl.setFont(new Font("SanSerif", Font.PLAIN,12));
		lbl.setBounds(20,10,190,20);
		c.add(lbl);
		
		bt = new JButton("확 인");
		bt.setBounds(60,40,70,25);
		bt.setFont(new Font("SanSerif", Font.PLAIN,12));
		bt.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
		bt.addActionListener(this);
		c.add(bt);
		
		Dimension dim = getToolkit().getScreenSize();
		setSize(200,100);
		setLocation(dim.width/2 - getWidth()/2 , dim.height/2 - getHeight()/2);
		show();
		
		addWindowListener(
				new WindowAdapter() {
					public void windowClosing(WindowEvent e) {
						dispose();
					}
				}
				
				);
		
		
	}
	//어떤 레이아웃인지 확인이 필요하지만 MessageBox에 대한 레이아웃임.
	
	public void actionPerformed(ActionEvent ae) {
		if(ae.getSource() == bt) {
			dispose();
		}
	}
	//버튼을 눌렀을시 안보이게함.

}
