//42번 수정
package practice.socket.multi;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

class ChatRoomDisplay extends JFrame implements ActionListener, KeyListener, ListSelectionListener, ChangeListener {

	private ClientThread cr_thread;
	private String idTo;
	private boolean isSelected;
	public boolean isAdmin;

	private JLabel roomer;
	public JList roomerInfo;
	private JButton coerceOut, sendWord, sendFile, quitRoom;
	private Font font;
	private JViewport view;
	private JScrollPane jsp3;
	public JTextArea messages;
	public JTextField message;

	public ChatRoomDisplay(ClientThread thread) {
		super("Chat-Application-대화방");

		cr_thread = thread;
		isSelected = false;
		isAdmin = false;
		font = new Font("SanSerif", Font.PLAIN, 12);

		Container c = getContentPane();
		c.setLayout(null);

		JPanel p = new JPanel();
		p.setLayout(null);
		p.setBounds(425, 10, 140, 175);
		p.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED), "참여자"));

		roomerInfo = new JList();
		roomerInfo.setFont(font);
		JScrollPane jsp2 = new JScrollPane(roomerInfo);
		roomerInfo.addListSelectionListener(this);
		jsp2.setBounds(15, 25, 110, 135);
		p.add(jsp2);

		c.add(p);

		p = new JPanel();
		p.setLayout(null);
		p.setBounds(10, 10, 410, 340);
		p.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED), "채팅창"));

		view = new JViewport();
		messages = new JTextArea();
		messages.setFont(font);
		messages.setEditable(false);
		view.add(messages);
		view.addChangeListener(this);
		jsp3 = new JScrollPane(view);
		jsp3.setBounds(15, 25, 380, 270);
		p.add(jsp3);

		message = new JTextField();
		message.setFont(font);
		message.addKeyListener(this);
		message.setBounds(15, 305, 380, 20);
		message.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
		p.add(message);

		c.add(p);

		coerceOut = new JButton("강 제 퇴 장");
		coerceOut.setFont(font);
		coerceOut.addActionListener(this);
		coerceOut.setBounds(445, 195, 100, 30);
		coerceOut.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
		c.add(coerceOut);

		sendWord = new JButton("귓말보내기");
		sendWord.setFont(font);
		sendWord.addActionListener(this);
		sendWord.setBounds(445, 235, 100, 30);
		sendWord.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
		c.add(sendWord);

		sendFile = new JButton("파 일 전 송");
		sendFile.setFont(font);
		sendFile.addActionListener(this);
		sendFile.setBounds(445, 275, 100, 30);
		sendFile.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
		c.add(sendFile);

		quitRoom = new JButton("퇴 살 하 기");
		quitRoom.setFont(font);
		quitRoom.addActionListener(this);
		quitRoom.setBounds(445, 315, 100, 30);
		quitRoom.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
		c.add(quitRoom);

		Dimension dim = getToolkit().getScreenSize();
		setSize(580, 400);
		setLocation(dim.width / 2 - getWidth() / 2, dim.height / 2 - getHeight() / 2);

		show();

		addWindowListener(new WindowAdapter() {
			public void windowActivated(WindowEvent e) {
				message.requestFocusInWindow();
			}
		}

		);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				cr_thread.requestQuitRoom();

			}
		}

		);

	}
	// 방을 만들고 생기는 채팅방의 레이아웃임.

	public void resetComponets() {
		messages.setText("");
		message.setText("");
		message.requestFocusInWindow();
	}
	//message,messages 두개를 ""로 초기화하고 message에 포커스를 설정

	public void keyPressed(KeyEvent ke) {
		if (ke.getKeyChar() == KeyEvent.VK_ENTER) {
			String words = message.getText();
			String data;
			String idTo;
			if (words.startsWith("/w")) {
				StringTokenizer st = new StringTokenizer(words, " ");
				String command = st.nextToken();
				idTo = st.nextToken();
				data = st.nextToken();
				cr_thread.requestSendWordTo(data, idTo);
				message.setText("");

			} else {
				cr_thread.requestSendWord(words);
				message.requestFocusInWindow();
			}
		}
	}
	//enter를 눌럿을때만 반응하는 key이벤트이며 words를 message의 텍스트로 가져오며 words를 " " 구분자로 자른 후 첫 문자(/w)를 command, 두번째 문자인 id를 idTo, 세번째 문자인 귓속말내용을 data에 저장후 thread에 data,idTo를 보냄.

	public void valueChanged(ListSelectionEvent e) {
		isSelected = true;
		idTo = String.valueOf(((JList) e.getSource()).getSelectedValue());

	}
	//귓속말 대상인 idTo에 리스트에서 선택된 값을 반환하여 String 형변환하여 저장됨

	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == coerceOut) {
			if (!isAdmin) {
				JOptionPane.showMessageDialog(this, "당신은 방장이 아닙니다.", "강제퇴장", JOptionPane.ERROR_MESSAGE);
			} else if (!isSelected) {
				JOptionPane.showMessageDialog(this, "강제퇴장 ID를 선택하세요.", "강제퇴장", JOptionPane.ERROR_MESSAGE);
			} else {
				cr_thread.requestCoerceOut(idTo);
				isSelected = false;
			}
		} else if (ae.getSource() == quitRoom) {
			cr_thread.requestQuitRoom();

		} else if (ae.getSource() == sendWord) {
			String idTo, data;
			if ((idTo = JOptionPane.showInputDialog("아이디를 입력하세요")) != null) {
				if ((data = JOptionPane.showInputDialog("메세지를 입력하세요.")) != null) {
					cr_thread.requestSendWordTo(data, idTo);
				}
			}
		} else if (ae.getSource() == sendFile) {
			String idTo;
			if ((idTo = JOptionPane.showInputDialog("상대방 아이디를 입력하세요.")) != null) {
				cr_thread.requestSendFile(idTo);
			}
		}
	}
	//각 버튼에 대한 이벤트를 설정 하여 thread에서 각이벤트에대한 정보를 넘겨줌.

	public void stateChanged(ChangeEvent e) {
		jsp3.getVerticalScrollBar().setValue((jsp3.getVerticalScrollBar().getValue() + 20));

	}
	//Scrollbal 의 세로 길이를 설정

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {

	}
}
