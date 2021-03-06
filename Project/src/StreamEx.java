import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class StreamEx extends JFrame{
	static String calc;
	static Double calcN;
	static Double calcN2;
	static Double result;

	public static void main(String[] args) {
		JFrame frame = new JFrame("??????");
		frame.setLocation(500, 400);
		frame.setPreferredSize(new Dimension(300, 500));
		Container contentPane = frame.getContentPane();
		GridLayout layout = new GridLayout(4, 4);
		JPanel panel = new JPanel();
		JButton[] btn = new JButton[16];
		String[] ar = new String[] { "7", "8", "9", "+", "4", "5", "6", "-", "1", "2", "3", "*", "0", "CE", "/", "=" };

		JLabel label = new JLabel("0", SwingConstants.RIGHT);
		label.setFont(new Font("???? ????", Font.BOLD, 40));

		for (int i = 0; i < 16; i++) {
			btn[i] = new JButton(ar[i]);
			btn[i].setBackground(Color.GRAY);
			btn[i].setForeground(Color.WHITE);
			panel.add(btn[i]);
		}
		for (int i = 0; i < 16; i++) {

			btn[i].addActionListener(new ActionListener() {
				String oldText;
				String nowText;
				String newText;

				@Override
				public void actionPerformed(ActionEvent e) {
					String input = e.getActionCommand();

					if (input.equals("+")) {
						calc = "+";
						calcN = calcN2;
						label.setText("0");

					} else if (input.equals("-")) {
						calc = "-";
						calcN = calcN2;
						label.setText("0");
					} else if (input.equals("*")) {
						calc = "*";
						calcN = calcN2;
						label.setText("0");
					} else if (input.equals("CE")) {
						label.setText("0");
					} else if (input.equals("/")) {
						calc = "/";
						calcN = calcN2;
						label.setText("0");
					} else if (input.equals("=")) {

						if (calc.equals("+")) {
							result = calcN + calcN2;
							calcN2 = result;
							label.setText(String.valueOf(result));
							calc = "";
						} else if (calc.equals("-")) {
							result = calcN - calcN2;
							calcN2 = result;
							label.setText(String.valueOf(result));
							calc = "";
						} else if (calc.equals("/")) {
							result = calcN / calcN2;
							calcN2 = result;
							label.setText(String.valueOf(result));
							calc = "";
						} else if (calc.equals("*")) {
							result = calcN * calcN2;
							calcN2 = result;
							label.setText(String.valueOf(result));
							calc = "";
						}
					} else {
						JButton b = (JButton) e.getSource();

						if (label.getText().equals("0")) {
							newText = b.getText();
						} else {
							oldText = label.getText();
							nowText = b.getText();
							newText = oldText + nowText;

						}

						label.setText(newText);
						calcN2 = Double.parseDouble(newText);
					}

				}
			});
		}

		contentPane.add(panel);
		panel.setLayout(layout);
		contentPane.add(label, BorderLayout.NORTH);
		contentPane.add(panel, BorderLayout.CENTER);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

}