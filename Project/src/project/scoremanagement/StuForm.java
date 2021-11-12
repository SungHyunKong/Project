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
		JTextArea tea = new JTextArea();
		
		la1.setBounds(40, 75, 60, 60);
		la2.setBounds(40, 175, 60, 60);
		la3.setBounds(40, 275, 60, 60);
		la4.setBounds(40, 375, 60, 60);
		text1.setBounds(100, 90, 125, 25);
		text2.setBounds(100, 190, 125, 25);
		text3.setBounds(100, 290, 125, 25);
		text4.setBounds(100, 390, 125, 25);
		add(la1);
		add(la2);
		add(la3);
		add(la4);
		add(text1);
		add(text2);
		add(text3);
		add(text4);
		add(p);
		
		setTitle("성적관리프로그램");
		setSize(600,700);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new StuForm();
	}

}
