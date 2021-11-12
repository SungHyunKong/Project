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

// ������ �������� �ʴ� ���� ������ ���캸�Ƶ� ����. close�� ���ܰ� ����. Ŭ���̾�Ʈ�� �����ĺ���

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

	String listTitle = "*******��ȭ�ڸ��*******";

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

		setDefaultCloseOperation(this.EXIT_ON_CLOSE);// �Ǵ� JFrame.EXIT_ON_CLOSE

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

			new Message("TO:");// ���鿡 ����

		} else if (obj == sendBtn || obj == msgTxt) {

// �켱 db ������ ���� �ʴ� ��� ������

			String msg = msgTxt.getText();

			if (filterMgr(msg)) {

				new Warning(this);

				return;

			}

			if (!flag/*���̵� �Է��϶�*/) {

				id = msg;

				sendMessage(Protocol.ID + ":" + id);

				setTitle(getTitle() + " - " + id + "�� ����");

				area.setText("");

				flag = true;

			} else/*Chat �� ��*/ {

// ��ü ��ȭ �Ǵ� ���� ��ȭ

				int i = list.getSelectedIndex();// ���õ��� �ʴ� ���̽��� ����

				if (i == -1 || i == 0)/*��üä��*/ {

					sendMessage(Protocol.CHATALL + ":" + msg);// :�ڿ� \n�� �־���.

				} else {

					String rid/* list ���� ���õ� id*/ = vlist.get(i);

					sendMessage(Protocol.CHAT + ":" + rid + ";" + msg);

					area.append("�ӼӸ����� [" + id + "]" + msg + "\n");

//sb.setValue(sb.getMaximum());// 2. ���⿡ ������ �ӼӸ��� ������ ���� ��ȭ������ ��� ���δ�. �� ���� �Ѱ� ����

				}

			}

		}

		sb.setValue(sb.getMaximum());// 1. ���⿡ ������ �ƴϸ� ����

		msgTxt.setText("");

		msgTxt.requestFocus();

// �ӼӸ��϶��� requestFocus�� �������� �ʴ´�. save�� ������ �� �Ŀ��� ������ �Ѵ�.

// �ذ�! 3���� ��Ʈ�̴�

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

				sb.setValue(sb.getMaximum());// �ּ�ó�� ����. ���ٸ� �ӼӸ��� �޴� ����� ��ȭâ�� ���ϴ����� ���� �ʱ� ������

			}

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	public void routine(String line) {

//CHATALL:�� ���Գ�..

		int idx = line.indexOf(":");

		String cmd = line.substring(0, idx);// CHATALL

		String data = line.substring(idx + 1);// �� ���Գ�..

		if (cmd.equals(Protocol.CHAT) || cmd.equals(Protocol.CHATALL)) {

			area.append(data + "\n");

		} else if (cmd.equals(Protocol.CHATLIST)) {

// CHATLIST:aaa;bbb;ccc;... < �߶� ���Ϳ� ����.

			list.removeAll();

			vlist.removeAllElements();

			vlist.add(listTitle);// "**** ��ȭ�� ��� ****"

			StringTokenizer st = new StringTokenizer(data, ";");// StringTokenizer ���� ����!

			while (st.hasMoreTokens()) {

				String s = st.nextToken();

				if (s != null)
					vlist.add(s);

			}

			list.setListData(vlist);

		} else if (cmd.equals(Protocol.MESSAGE)) {

// MESSAGE:bbb;������ ���� ��ô�. 

			idx = data.indexOf(';');

//line�� data�� ����

			cmd = data.substring(0, idx);// bbb

			data = data.substring(idx + 1);// ������ ���� ��ô�.

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

	public boolean filterMgr(String msg) {// ���� �� �� ������?

		boolean flag = false;// false�̸� ������ �ƴ�

		String str[] = { "�ٺ�", "������", "����", "�ڹ�", "java", "����" };

//���� ȣȣ ����

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

			Label label = new Label("��ȭ������ �����Ͽ����ϴ�.", Label.CENTER);

			add(label);

			add(ok = new JButton("Ȯ��"));

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

			setTitle("����������");

			this.mode = mode;

			id = vlist.get(list.getSelectedIndex());

			layset();

		}

		public Message(String mode, String id, String msg) {

			setTitle("�����б�");

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

			super(ct2, "���", true);

			this.ct2 = ct2;

//////////////////////////////////////////////////////////////////////////////////////////

			addWindowListener(new WindowAdapter() {

				public void windowClosing(WindowEvent e) {

					dispose();

				}

			});

/////////////////////////////////////////////////////////////////////////////////////////

			setLayout(new GridLayout(2, 1));

			JLabel label = new JLabel("�Է��Ͻ� ��¥�� �������Դϴ�."

					, JLabel.CENTER);

			add(label);

			add(ok = new JButton("Ȯ��"));

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