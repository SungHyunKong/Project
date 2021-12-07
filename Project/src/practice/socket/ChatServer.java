package practice.socket;

import java.io.BufferedReader;

import java.io.InputStreamReader;

import java.io.PrintWriter;

import java.net.ServerSocket;

import java.net.Socket;

import java.util.Vector;


		System.out.println("**************************");

		System.out.println("Ŭ���̾�Ʈ�� ������ ��ٸ��� �ֽ��ϴ�.");

		System.out.println("**************************");

		try {

			while (true) {

				Socket sock = server.accept();

				ClientThread2 ct = new ClientThread2(sock);

				ct.start();

				vc.add(ct);

			}

		} catch (Exception e) {

// TODO: handle exception

			System.err.println("Error in Socket");

			e.printStackTrace();

			System.exit(1);// 1�� ���������� ����

		}

	}

// ���ӵ� ��� client���� �޼��� ����

	public void sendAllClient(String msg) {

		for (int i = 0; i < vc.size(); i++) {

			ClientThread2 ct = vc.get(i);

			ct.sendMessage(msg);

		}

	}

// Vector���� ClientThread2�� ����

	public void removeClient(ClientThread2 ct) {

// ���⿡ �߰��� �ϸ� ����� ������

		vc.remove(ct);

	}

	class ClientThread2 extends Thread {

		Socket sock;

		BufferedReader in;

		PrintWriter out;

		String id = "�ͷ�";

		public ClientThread2(Socket sock) {

			try {

				this.sock = sock;

			} catch (Exception e) {

// TODO: handle exception

				e.printStackTrace();

			}

		}

		@Override

		public void run() {

			try {

				in = new BufferedReader(

						new InputStreamReader(

								sock.getInputStream()));

				out = new PrintWriter(

						sock.getOutputStream(), true/*auto flush*/);

				System.out.println(sock + "���ӵ�...");

// ������ �ش� Ŭ���� ������ �ȿ� �־��� �����̳� �޼ҵ�� ���ȴ�.

				out.println("���̵� �Է��ϼ���");

				boolean done = true;

				while (done) {

					String line = in.readLine();

					if (line == null)
						done = false;

// Client ���� ���� data�� routine���� �м�, ó���Ѵ�.

					else
						routine(line);

				}

			} catch (Exception e) {

				removeClient(this);

				System.err.println(sock + "[" + id + "]" + "������...");

			}

		}

// ������ ������ �ƴϴ�

		public void routine(String line) {

// CHATALL:���ɿ� �� ����? // ':'�� �������� �ڷḦ �߶�� �Ѵ�.

			int idx = line.indexOf(':');

			String cmd = line.substring(0, idx);// CHATALL

			String data = line.substring(idx + 1);// ���ɿ� �� ����?

			if (cmd.equals(Protocol.ID)) {

				if (data != null && data.length() > 0) {

					id = data;

					sendAllClient(Protocol.CHATLIST + ":" + getIds());

					sendAllClient(Protocol.CHATALL + ":" + "[" + id + "] �� �����Ͽ����ϴ�.");

				}

			} else if (cmd.equals(Protocol.CHAT)) {

// CHAT:bbb;�ƹ��ų�����. bbb�� �޴� ���̵�, ���� ���̵�� aaa

				idx = data.indexOf(';');

				cmd = data.substring(0, idx);// bbb// ���Ϳ��� ã�´�.

				data = data.substring(idx + 1);// �ƹ��ų�����.

				ClientThread2 ct = findThread(cmd/*bbb*/);

				if (ct != null) {

// �޼��� ������ ������ �� ������, ��Ʈ��ũ ���� ������� �𸥴�

					ct.sendMessage(Protocol.CHAT + ":�ӼӸ��ޱ�[" + id/*aaa*/ + "]" + data);

				} else/* bbb ������ ���� */ {

					sendMessage(Protocol.CHAT + ":[" + cmd/*bbb*/ + "]���ӵ� ����ڰ� �ƴմϴ�");

// ct�� ������ ����� ������ ���� �־���.

				}

			} else if (cmd.equals(Protocol.CHATALL)) {

				sendAllClient(Protocol.CHATALL + ":[" + id/*aaa*/ + "]" + data);

			} else if (cmd.equals(Protocol.MESSAGE)) {

// MESSAGE:bbb;������ ���� ��ô�. (���� ����ؼ� ���� �� ����) 

				idx = data.indexOf(';');

				cmd = data.substring(0, idx);// bbb// ���Ϳ��� ã�´�.

				data = data.substring(idx + 1);// ������ ���� ��ô�.

				ClientThread2 ct = findThread(cmd/*bbb*/);

				if (ct != null) {

// ���� ������ ������ �� ������, ��Ʈ��ũ ���� ������� �𸥴�

					ct.sendMessage(Protocol.MESSAGE + ":" + id/*aaa*/ + ";" + data);

				} else/* bbb ������ ���� */ {

					sendMessage(Protocol.CHAT + ":[" + cmd/*bbb*/ + "]���ӵ� ����ڰ� �ƴմϴ�");

				}

			}

		}

// �Ű������� ���� id�� ClientThread2�� Vector���� ã�´�.

		public ClientThread2 findThread(String id) {

			ClientThread2 ct = null;

			for (int i = 0; i < vc.size(); i++) {

// �� ���� Ŭ������ ������ �� �˰��̴�. Ŭ������ �޼ҵ�ó�� ����� ���� ��.

				ct = vc.get(i);

				if (ct.id.equals(id))
					break;

			}

			return ct;

		}

// ���ӵ� id�� ����(������ ;) ex)aaa;bbb;ccc;

		public String getIds() {

			String ids = "";

			for (int i = 0; i < vc.size(); i++) {

				ClientThread2 ct = vc.get(i);

				ids += ct.id + ";";

			}

			return ids;

		}

		public void sendMessage(String msg) {

			out.println(msg);

		}

	}

	public static void main(String[] args) {

// TODO Auto-generated method stub

		new ChatServer();

	}

}