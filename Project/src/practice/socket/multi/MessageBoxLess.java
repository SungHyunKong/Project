package practice.socket.multi;


import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class MessageBoxLess extends JDialog implements ActionListener{
	
	private Frame client;
	private Container c;
	
	public MessageBoxLess(JFrame parent, String title, String message) {
		super(parent, true);
		setTitle(title);
		c=getContentPane();
		c.setLayout(null);
		JLabel lbl = new JLabel(message);
		lbl.setFont(new Font("SanSerif",Font.PLAIN,12));
		lbl.setBounds(20,10,190,20);
		c.add(lbl);
		
		JButton bt = new JButton("O K");
		bt.setBounds(60,40,70,25);
		bt.setFont(new Font("SanSerif",Font.PLAIN,12));
		bt.addActionListener(this);
		bt.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
		c.add(bt);
		
		Dimension dim = getToolkit().getScreenSize();
		setSize(200,100);
		setLocation(dim.width/2 - getWidth()/2 , dim.height/2 -getHeight()/2);
		show();
		client = parent;
		
	}
	// MessageBoxLess에 대한 레이아웃으로 보이지만 어떤 레이아웃인지 확인이 필요.
	public void actionPerformed(ActionEvent ae) {
		dispose();
		System.exit(0);
	}
	//버튼을 누를시 시스템을 종료함.


}
