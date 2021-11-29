import java.awt.BorderLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class IdForm extends JFrame implements ActionListener, MouseListener{
	Vector main;
	Vector info;
	DefaultTableModel model;
	JTable jtable;
	JPanel panel;
	JScrollPane pane;
	JButton btnIn;
	JButton btnSerch;
	JButton btnAll;

	public IdForm() {
		super("회원가입");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		main = new Vector<>();
		info = new Vector<>();
		main.add("아이디");
		main.add("비밀번호");
		main.add("이름");
		main.add("주소");
		main.add("생년월일");
		main.add("전화번호");
		info.add("tkrxod");
		info.add("123456");
		info.add("공성현");
		info.add("광주");
		info.add("1994/06/20");
		info.add("010-1111-2222");
		model = new DefaultTableModel(main,0);

		model.addRow(info);

		jtable = new JTable(model);
		pane = new JScrollPane(jtable);
		add(pane);

		panel = new JPanel();

		btnIn = new JButton("회원가입");
		btnSerch = new JButton("검색");
		btnAll = new JButton("전체보기");
		panel.add(btnIn);
		panel.add(btnSerch);
		panel.add(btnAll);
		
		add(panel, BorderLayout.NORTH);
		add(panel, BorderLayout.NORTH);
		add(panel, BorderLayout.NORTH);
		

		setSize(600, 200);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		btnIn.addActionListener(this);
	};
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==btnIn) {
			new IdInfo(this);
		}
		
		
	}

	public static void main(String[] args) {
		new IdForm();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int k = jtable.getSelectedRow();
		String id = (String)jtable.getValueAt(k, 0);
		
		IdInfo idIn = new IdInfo(id,this);
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
