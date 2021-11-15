package project.scoremanagement;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class StuForm  extends JFrame {
	Vector<String> vec;
	
	StuForm(){
		
		JPanel p = new JPanel();
		
		JLabel la1 = new JLabel("     학번");
		JLabel la2 = new JLabel("국어점수");
		JLabel la3 = new JLabel("영어점수");
		JLabel la4 = new JLabel("수학점수");
		
		
		
		JTextField text1 = new JTextField(10);
		JTextField text2 = new JTextField(10);
		JTextField text3 = new JTextField(10);
		JTextField text4 = new JTextField(10);
		JTable tab = new JTable();
		JScrollPane scroll = new JScrollPane(tab);
		DefaultTableModel model = new DefaultTableModel();
		JButton btn1 = new JButton("입력");
		JButton btn2 = new JButton("출력");
		JButton btn3 = new JButton("순위");
		JButton btn4 = new JButton("파일저장");
		JButton btn5 = new JButton("파일읽기");
		
		la1.setBounds(40, 75, 60, 60);
		la2.setBounds(40, 175, 60, 60);
		la3.setBounds(40, 275, 60, 60);
		la4.setBounds(40, 375, 60, 60);
		text1.setBounds(100, 90, 125, 25);
		text2.setBounds(100, 190, 125, 25);
		text3.setBounds(100, 290, 125, 25);
		text4.setBounds(100, 390, 125, 25);
		tab.setBounds(300, 75, 400, 425);
		btn1.setBounds(110,550, 90, 30);
		btn2.setBounds(220,550, 90, 30);
		btn3.setBounds(330,550, 90, 30);
		btn4.setBounds(440,550, 90, 30);
		btn5.setBounds(550,550, 90, 30);
		add(la1);
		add(la2);
		add(la3);
		add(la4);
		add(text1);
		add(text2);
		add(text3);
		add(text4);
		add(tab);
		add(btn1);
		add(btn2);
		add(btn3);
		add(btn4);
		add(btn5);
		add(p);
		
		setTitle("성적관리프로그램");
		setSize(750,650);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new StuForm();
	}

}
