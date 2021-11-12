package practice.socket;

import java.awt.BorderLayout;

import java.awt.Color;

import java.awt.Dialog;

import java.awt.Frame;

import java.awt.GridLayout;

import java.awt.Label;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;

import java.awt.event.WindowAdapter;

import java.awt.event.WindowEvent;

import java.io.BufferedReader;

import java.io.FileWriter;

import java.io.InputStreamReader;

import java.io.PrintWriter;

import java.net.Socket;

import java.util.StringTokenizer;

import java.util.Vector;

import javax.swing.JButton;

import javax.swing.JFrame;

import javax.swing.JLabel;

import javax.swing.JList;

import javax.swing.JPanel;

import javax.swing.JScrollBar;

import javax.swing.JScrollPane;

import javax.swing.JTextArea;

import javax.swing.JTextField;

public class ChatServer2 extends JFrame

		implements ActionListener, Runnable {

// 접속이 해제되지 않는 버그 서버를 살펴보아도 딱히. close가 없단거 빼곤. 클라이언트를 파해쳐보자

	JButton connectBtn, saveBtn, msgBtn, sendBtn;

	JTextField hostTxt, portTxt, msgTxt;

	JTextArea area;

	JList<String> list;

	JScrollPane scroll;

	JScrollBar sb;

	BufferedReader in;

	PrintWriter out;

	Vector<String> vlist;

	boolean flag = false;

	String listTitle = "*******대화자명단*******";

	String id;

	public ChatServer2() {

		setTitle("MyChat 2.0");

		setSize(470, 500);

		JPanel p1 = new JPanel();

		p1.add(new JLabel("Host", Label.RIGHT));

		p1.add(hostTxt = new JTextField("127.0.0.1", 15));

		p1.add(new JLabel("Port", Label.RIGHT));

		p1.add(portTxt = new JTextField(8010 + "", 8));

		connectBtn = new JButton("connect");

		connectBtn.addActionListener(this);

		p1.add(connectBtn);

		add(BorderLayout.NORTH, p1);

// //////////////////////////////////////////////////////////////////////////////////////////

		area = new JTextArea("MyChat2.0");

		scroll = new JScrollPane(area,

				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,

				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		sb = scroll.getVerticalScrollBar();

		area.setBackground(Color.DARK_GRAY);

		area.setForeground(Color.YELLOW);

		area.setEditable(false);

		add(BorderLayout.CENTER, scroll);

// /////////////////////////////////////////////////////////////////////////////////////////

		JPanel p2 = new JPanel();

		p2.setLayout(new BorderLayout());

		vlist = new Vector<String>();

		vlist.add(listTitle);

		list = new JList(vlist);

		p2.add(BorderLayout.CENTER, list);

		JPanel p3 = new JPanel();

		p3.setLayout(new GridLayout(1, 2));

		saveBtn = new JButton("Save");

		saveBtn.addActionListener(this);

		msgBtn = new JButton("Message");

		msgBtn.addActionListener(this);

		p3.add(saveBtn);

		p3.add(msgBtn);

		p2.add(BorderLayout.SOUTH, p3);

		add(BorderLayout.EAST, p2);

// ///////////////////////////////////////////////////////////////////////////////////////////

		JPanel p4 = new JPanel();

		msgTxt = new JTextField("", 33);

		msgTxt.addActionListener(this);

		sendBtn = new JButton("SEND");

		sendBtn.addActionListener(this);

		p4.add(msgTxt);

		p4.add(sendBtn);

		add(BorderLayout.SOUTH, p4);

		setDefaultCloseOperation(this.EXIT_ON_CLOSE);// 또는 JFrame.EXIT_ON_CLOSE

		setVisible(true);

		setResizable(false);

	}

	public void actionPerformed(ActionEvent e) {

		Object obj = e.getSource();

		if (obj == connectBtn) {

			connect(hostTxt.getText(), Integer.parseInt(portTxt.getText()));

			hostTxt.setEditable(false);

			portTxt.setEditable(false);

			connectBtn.setEnabled(false);

		} else if (obj == saveBtn) {

			String saveStr = area.getText();

			long mills = System.currentTimeMillis();

			try {

				FileWriter fw = new FileWriter(mills + ".txt");

				fw.write(saveStr);

				fw.close();

			} catch (Exception ex) {

// TODO: handle exception

				ex.printStackTrace();

			}

			new SaveMsg(this);

		} else if (obj == msgBtn) {

			new Message("TO:");// 공백에 주의

		} else if (obj == sendBtn || obj == msgTxt) {

// 우선 db 연동을 하지 않는 경우 제작중

			String msg = msgTxt.getText();

			if (filterMgr(msg)) {

				new Warning(this);

				return;

			}

			if (!flag/*아이디 입력일때*/) {

				id = msg;

				sendMessage(Protocol.ID + ":" + id);

				setTitle(getTitle() + " - " + id + "님 접속");

				area.setText("");

				flag = true;

			} else/*Chat 일 때*/ {

// 전체 대화 또는 선택 대화

				int i = list.getSelectedIndex();// 선택되지 않는 케이스일 때도

				if (i == -1 || i == 0)/*전체채팅*/ {

					sendMessage(Protocol.CHATALL + ":" + msg);// :뒤에 \n이 있었다.

				} else {

					String rid/* list 에서 선택된 id*/ = vlist.get(i);

					sendMessage(Protocol.CHAT + ":" + rid + ";" + msg);

					area.append("귓속말보냄 [" + id + "]" + msg + "\n");

//sb.setValue(sb.getMaximum());// 2. 여기에 적으면 귓속말을 보내는 쪽의 대화내용이 계속 보인다. 두 곳중 한곳 택일

				}

			}

		}

		sb.setValue(sb.getMaximum());// 1. 여기에 적던지 아니면 위에

		msgTxt.setText("");

		msgTxt.requestFocus();

// 귓속말일때엔 requestFocus가 동작하지 않는다. save를 누르고 난 후에는 동작을 한다.

// 해결! 3개가 세트이다

	}

	public void sendMessage(String msg) {

		out.println(msg);

	}

	public void run() {

		try {

			boolean done = false;

			while (!done) {

				String line = in.readLine();

				if (line == null)

					done = true;

				else

					routine(line);

				sb.setValue(sb.getMaximum());// 주석처리 ㄴㄴ. 없다면 귓속말을 받는 사람의 대화창이 최하단으로 가지 않기 때문에

			}

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	public void routine(String line) {

//CHATALL:밥 뭐먹냐..

		int idx = line.indexOf(":");

		String cmd = line.substring(0, idx);// CHATALL

		String data = line.substring(idx + 1);// 밥 뭐먹냐..

		if (cmd.equals(Protocol.CHAT) || cmd.equals(Protocol.CHATALL)) {

			area.append(data + "\n");

		} else if (cmd.equals(Protocol.CHATLIST)) {

// CHATLIST:aaa;bbb;ccc;... < 잘라서 벡터에 넣자.

			list.removeAll();

			vlist.removeAllElements();

			vlist.add(listTitle);// "**** 대화자 명단 ****"

			StringTokenizer st = new StringTokenizer(data, ";");// StringTokenizer 새로 배운것!

			while (st.hasMoreTokens()) {

				String s = st.nextToken();

				if (s != null)
					vlist.add(s);

			}

			list.setListData(vlist);

		} else if (cmd.equals(Protocol.MESSAGE)) {

// MESSAGE:bbb;오늘을 잠을 잡시다. 

			idx = data.indexOf(';');

//line을 data로 변경

			cmd = data.substring(0, idx);// bbb

			data = data.substring(idx + 1);// 오늘을 잠을 잡시다.

			new Message("FROM:", cmd, data);

		}

	}

	public void connect(String host, int port) {

		try {

			Socket sock = new Socket(host, port);

			in = new BufferedReader(

					new InputStreamReader(

							sock.getInputStream()));

			out = new PrintWriter(

					sock.getOutputStream(), true);

			area.setText(in.readLine() + "\n");

		} catch (Exception e) {

			System.err.println("Error in Connect");

			e.printStackTrace();

			System.exit(1);

		}

		new Thread(this).start();

	}

	public boolean filterMgr(String msg) {// 오늘 밥 머 먹을까?

		boolean flag = false;// false이면 금지어 아님

		String str[] = { "바보", "개새끼", "새끼", "자바", "java", "반장" };

//하하 호호 히히

		StringTokenizer st = new StringTokenizer(msg);

		String msgs[] = new String[st.countTokens()];

		for (int i = 0; i < msgs.length; i++) {

			msgs[i] = st.nextToken();

		}

		for (int i = 0; i < str.length; i++) {

			if (flag)
				break;

			for (int j = 0; j < msgs.length; j++) {

				if (str[i].equals(msgs[j])) {

					flag = true;

					break;

				}

			}

		}

		return flag;

	}

	class SaveMsg extends Dialog implements ActionListener {

		JButton ok;

		ChatServer2 ct2;

		public SaveMsg(ChatServer2 ct2) {

			super(ct2, "Save", true);

			this.ct2 = ct2;

			addWindowListener(new WindowAdapter() {

				public void windowClosing(WindowEvent e) {

					dispose();

				}

			});

			setLayout(new GridLayout(2, 1));

			Label label = new Label("대화내용을 저장하였습니다.", Label.CENTER);

			add(label);

			add(ok = new JButton("확인"));

			ok.addActionListener(this);

			layset();

			setVisible(true);

		}

		public void layset() {

			int x = ct2.getX();

			int y = ct2.getY();

			int w = ct2.getWidth();

			int h = ct2.getHeight();

			int w1 = 150;

			int h1 = 100;

			setBounds(x + w / 2 - w1 / 2, y + h / 2 - h1 / 2, 200, 100);

		}

		public void actionPerformed(ActionEvent e) {

			ct2.area.setText("");

			setVisible(false);

			dispose();

		}

	}

	class Message extends Frame implements ActionListener {

		JButton send, close;

		JTextField name;

		JTextArea msg;

		String mode;// to/from

		String id;

		public Message(String mode) {

			setTitle("쪽지보내기");

			this.mode = mode;

			id = vlist.get(list.getSelectedIndex());

			layset();

		}

		public Message(String mode, String id, String msg) {

			setTitle("쪽지읽기");

			this.mode = mode;

			this.id = id;

			layset();

			this.msg.setText(msg);

			name.setEditable(false);

		}

		public void layset() {

			addWindowListener(new WindowAdapter() {

				public void windowClosing(WindowEvent e) {

					dispose();

				}

			});

			JPanel p1 = new JPanel();

			p1.add(new Label(mode, Label.CENTER));

			name = new JTextField(id, 10);

			p1.add(name);

			add(BorderLayout.NORTH, p1);

			msg = new JTextArea(10, 30);

			JScrollPane scroll = new JScrollPane(msg,

					JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,

					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

			add(scroll);

			JPanel p2 = new JPanel();

			if (mode.equals("TO:")) {

				p2.add(send = new JButton("send"));

				send.addActionListener(this);

			}

			p2.add(close = new JButton("close"));

			close.addActionListener(this);

			add(BorderLayout.SOUTH, p2);

			setBounds(200, 200, 300, 250);

			setVisible(true);

		}

		public void actionPerformed(ActionEvent e) {

			if (e.getSource() == send) {

				sendMessage(Protocol.MESSAGE +

						":" + id + ";" + msg.getText());

			}

			setVisible(false);

			dispose();

		}

	}

	class Warning extends Dialog implements ActionListener {

		JButton ok;

		ChatServer2 ct2;

		public Warning(ChatServer2 ct2) {

			super(ct2, "경고", true);

			this.ct2 = ct2;

//////////////////////////////////////////////////////////////////////////////////////////

			addWindowListener(new WindowAdapter() {

				public void windowClosing(WindowEvent e) {

					dispose();

				}

			});

/////////////////////////////////////////////////////////////////////////////////////////

			setLayout(new GridLayout(2, 1));

			JLabel label = new JLabel("입력하신 글짜는 금지어입니다."

					, JLabel.CENTER);

			add(label);

			add(ok = new JButton("확인"));

			ok.addActionListener(this);

			layset();

			setVisible(true);

		}

		public void layset() {

			int x = ct2.getX();

			int y = ct2.getY();

			int w = ct2.getWidth();

			int h = ct2.getHeight();

			int w1 = 150;

			int h1 = 100;

			setBounds(x + w / 2 - w1 / 2,

					y + h / 2 - h1 / 2, 200, 100);

		}

		public void actionPerformed(ActionEvent e) {

			ct2.msgTxt.setText("");

			dispose();

		}

	}

	public static void main(String[] args) {

		new ChatServer2();

	}

}