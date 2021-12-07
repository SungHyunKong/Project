package practice.socket;

import java.io.BufferedReader;

import java.io.InputStreamReader;

import java.io.PrintWriter;

import java.net.ServerSocket;

import java.net.Socket;

import java.util.Vector;

public class ChatServer {


// TODO: handle exception

			System.err.println("Error in CharServer");

			e.printStackTrace();

			System.exit(1);// 1은 비정상적인 종료

		}

		System.out.println("**************************");

		System.out.println("클라이언트의 접속을 기다리고 있습니다.");

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

			System.exit(1);// 1은 비정상적인 종료

		}

	}

// 접속된 모든 client에게 메세지 전송

	public void sendAllClient(String msg) {

		for (int i = 0; i < vc.size(); i++) {

			ClientThread2 ct = vc.get(i);

			ct.sendMessage(msg);

		}

	}

// Vector에서 ClientThread2를 제거

	public void removeClient(ClientThread2 ct) {

// 여기에 추가를 하면 사람이 없어짐

		vc.remove(ct);

	}

	class ClientThread2 extends Thread {

		Socket sock;

		BufferedReader in;

		PrintWriter out;

		String id = "익룡";

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

				System.out.println(sock + "접속됨...");

// 위까지 해당 클래스 생성자 안에 있었던 내용이나 메소드로 돌렸다.

				out.println("아이디를 입력하세요");

				boolean done = true;

				while (done) {

					String line = in.readLine();

					if (line == null)
						done = false;

// Client 에서 받은 data는 routine에서 분석, 처리한다.

					else
						routine(line);

				}

			} catch (Exception e) {

				removeClient(this);

				System.err.println(sock + "[" + id + "]" + "끊어짐...");

			}

		}

// 복잡한 내용은 아니다

		public void routine(String line) {

// CHATALL:점심에 뭐 먹지? // ':'을 기준으로 자료를 잘라야 한다.

			int idx = line.indexOf(':');

			String cmd = line.substring(0, idx);// CHATALL

			String data = line.substring(idx + 1);// 점심에 뭐 먹지?

			if (cmd.equals(Protocol.ID)) {

				if (data != null && data.length() > 0) {

					id = data;

					sendAllClient(Protocol.CHATLIST + ":" + getIds());

					sendAllClient(Protocol.CHATALL + ":" + "[" + id + "] 님 입장하였습니다.");

				}

			} else if (cmd.equals(Protocol.CHAT)) {

// CHAT:bbb;아무거나먹자. bbb는 받는 아이디, 보낸 아이디는 aaa

				idx = data.indexOf(';');

				cmd = data.substring(0, idx);// bbb// 백터에서 찾는다.

				data = data.substring(idx + 1);// 아무거나먹자.

				ClientThread2 ct = findThread(cmd/*bbb*/);

				if (ct != null) {

// 메세지 보내고 끊어질 수 있으니, 네트워크 상은 어찌될지 모른다

					ct.sendMessage(Protocol.CHAT + ":귓속말받기[" + id/*aaa*/ + "]" + data);

				} else/* bbb 비접속 상태 */ {

					sendMessage(Protocol.CHAT + ":[" + cmd/*bbb*/ + "]접속된 사용자가 아닙니다");

// ct를 적으니 노란색 밑줄이 쳐져 있었다.

				}

			} else if (cmd.equals(Protocol.CHATALL)) {

				sendAllClient(Protocol.CHATALL + ":[" + id/*aaa*/ + "]" + data);

			} else if (cmd.equals(Protocol.MESSAGE)) {

// MESSAGE:bbb;오늘을 잠을 잡시다. (위랑 비슷해서 복붙 후 수정) 

				idx = data.indexOf(';');

				cmd = data.substring(0, idx);// bbb// 백터에서 찾는다.

				data = data.substring(idx + 1);// 오늘을 잠을 잡시다.

				ClientThread2 ct = findThread(cmd/*bbb*/);

				if (ct != null) {

// 쪽지 보내고 끊어질 수 있으니, 네트워크 상은 어찌될지 모른다

					ct.sendMessage(Protocol.MESSAGE + ":" + id/*aaa*/ + ";" + data);

				} else/* bbb 비접속 상태 */ {

					sendMessage(Protocol.CHAT + ":[" + cmd/*bbb*/ + "]접속된 사용자가 아닙니다");

				}

			}

		}

// 매개변수로 받은 id로 ClientThread2를 Vector에서 찾는다.

		public ClientThread2 findThread(String id) {

			ClientThread2 ct = null;

			for (int i = 0; i < vc.size(); i++) {

// 이 내부 클래스의 장점은 잘 알것이다. 클래스를 메소드처럼 사용이 가능 등.

				ct = vc.get(i);

				if (ct.id.equals(id))
					break;

			}

			return ct;

		}

// 접속된 id를 리턴(구분자 ;) ex)aaa;bbb;ccc;

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