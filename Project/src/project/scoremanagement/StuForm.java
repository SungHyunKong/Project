package project.scoremanagement;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class StuForm  extends JFrame {
	public static void main(String[] args) {
		JFrame frame = new JFrame("성적관리 프로그램");
		Container contenpane = frame.getContentPane();
		GridLayout grid = new GridLayout(4,2);
		GridLayout grid2 = new GridLayout(2,1);
		FlowLayout flow = new FlowLayout();
		frame.setLocation(500, 400);
		frame.setPreferredSize(new Dimension(500, 800));
		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel();
		JPanel panel3 = new JPanel();
		JPanel panel4 = new JPanel();
		JLabel label1 = new JLabel("학번");
		JTextField tef1 = new JTextField(10);
		JLabel label2 = new JLabel("국어");
		JTextField tef2 = new JTextField(10);
		JLabel label3 = new JLabel("영어");
		JTextField tef3 = new JTextField(10);
		JLabel label4 = new JLabel("수학");
		JTextField tef4 = new JTextField(10);
		JLabel label5 = new JLabel("학번 국어 영어 수학");
		JTextField tef5 = new JTextField(10);
		JButton btn1 = new JButton("입력");
		JButton btn2 = new JButton("출력");
		JButton btn3 = new JButton("순위");
		JButton btn4 = new JButton("파일저장");
		JButton btn5 = new JButton("파일로딩");
		panel1.setLayout(grid);
		panel1.add(label1);
		panel1.add(tef1);
		panel1.add(label2);
		panel1.add(tef2);
		panel1.add(label3);
		panel1.add(tef3);
		panel1.add(label4);
		panel1.add(tef4);
		panel2.add(label5);
		panel3.add(btn1);
		panel3.add(btn2);
		panel3.add(btn3);
		panel3.add(btn4);
		panel3.add(btn5);
		panel4.add(tef5);
		
		panel2.setLayout(new FlowLayout());
		contenpane.add(panel1,BorderLayout.CENTER);
		contenpane.add(panel4,BorderLayout.EAST);
		contenpane.add(panel2,BorderLayout.EAST);
		contenpane.add(panel3,BorderLayout.SOUTH);
		
		
		
		
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		
	}

}
