package project.scoremanagement;

import java.awt.Dimension;

import javax.swing.JFrame;

public class StuForm  extends JFrame {
	public static void main(String[] args) {
		JFrame frame = new JFrame("己利包府 橇肺弊伐");
		frame.setLocation(500, 400);
		frame.setPreferredSize(new Dimension(300, 500));
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		
	}

}
