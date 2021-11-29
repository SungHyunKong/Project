package practive.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class FallingWordFrame extends JFrame {
	private GamePanel gamePanel = null;
	private ControlPanel controlPanel = null;
	
	public FallingWordFrame() {
		super("떨어지는 단어 맞추기");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container c =getContentPane();
		gamePanel = new GamePanel();
		c.add(gamePanel, BorderLayout.CENTER);
		controlPanel = new ControlPanel(gamePanel);
		c.add(controlPanel, BorderLayout.SOUTH);
		
		setSize(300,400);
		setVisible(true);
		
		gamePanel.startGame();
	}
	
	class ControlPanel extends JPanel {
		private GamePanel gamePanel;
		private JTextField input = new JTextField(15);
		
		public ControlPanel(GamePanel gamePanel) {
			this.gamePanel = gamePanel;
			this.setLayout(new FlowLayout());
			this.setBackground(Color.LIGHT_GRAY);
			add(input);
			
			input.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					JTextField tf = (JTextField)e.getSource();
					String text = tf.getText();
					if(text.equals("그만"))
						System.exit(0);
						
					if(!gamePanel.isGameOn())
						return;
					
					boolean match = gamePanel.matchWord(text);
					if(match == true) {
						gamePanel.printResult("성공");
						gamePanel.stopGame();
						gamePanel.startGame();
						tf.setText("");
					}
					else {
						gamePanel.printResult(tf.getText()+" 틀림");
					}
					
					
					
				}
			});
		}
	}
	
	class GamePanel extends JPanel {
		private JLabel label = new JLabel();
		private JLabel resLabel = new JLabel();
		private Words words = null;
		private String fallingWord = null;
		private FallingThread thread = null;
		private boolean gameOn = false;
		
		public GamePanel() {
			setLayout(null);
			add(label);
			
			resLabel.setLocation(0,0);
			resLabel.setSize(100,30);
			add(resLabel);
			words = new Words("words.txt");
		}
		public boolean isGameOn() {
			return gameOn;
		}
		public void stopGame() {
			if(thread == null)
				return;
			thread.interrupt();
			thread = null;
			gameOn = false;
		}
		public void stopSelfAndNewGame() {
			startGame();
			
		}
		public void printResult(String text) {
			resLabel.setText(text);
		}
		public void startGame() {
			fallingWord= words.getRandomWord();
			label.setText(fallingWord);
			label.setSize(200,30);
			label.setLocation((getWidth()-200)/2,0);
			label.setForeground(Color.magenta);
			label.setFont(new Font("Tahoma",Font.ITALIC,20));
			
			thread = new FallingThread(gamePanel, this.label);
			thread.start();
			gameOn = true;
		}
		public boolean matchWord(String text) {
			if(fallingWord != null && fallingWord.equals(text))
				return true;
			else
				return false;
		}
		class FallingThread extends Thread{
			private GamePanel panel;
			private JLabel label;
			private long delay = 200;
			private boolean falling = false;
			
			public FallingThread(GamePanel panel, JLabel label) {
				this.panel = panel;
				this.label = label;
			}
			public boolean isFalling() {
				return falling;
			}
			
			@Override
			public void run() {
				falling = true;
				while(true) {
					try {
						sleep(delay);
						int y = label.getY()+5;
						if(y >= panel.getHeight()-label.getHeight()) {
							falling = false;
							label.setText("");
							panel.printResult("시간초과실패");
							panel.stopSelfAndNewGame();
							break;
							
						}
						
						label.setLocation(label.getX(),y);
						GamePanel.this.repaint();
					} catch (Exception e) {
						falling = false;
						return;
					}
				}
			}
		}
	}
	class Words {
		private Vector<String> wordVector = new Vector<String>();
		
		public Words(String fileName) {
			try {
				Scanner s = new Scanner(new FileReader(fileName));
				while(s.hasNext()) {
					String word = s.nextLine();
					wordVector.add(word);
				}
				s.close();
			} catch (Exception e) {
				System.out.println("file not found erro");
				System.exit(0);
			}
		}
		public String getRandomWord() {
			final int WORDMAX = wordVector.size();
			int index = (int)(Math.random()*WORDMAX);
			return wordVector.get(index);
		}
	}
	
	public static void main (String[] args) {
		new FallingWordFrame();
	}

}
