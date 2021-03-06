package practice.socket.multi;

import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
//66, 82, 118, 236, 265, 306, 324번 수정

public class ClientThread extends Thread {

	private WaitRoomDisplay ct_waitRoom;
	private ChatRoomDisplay ct_chatRoom;
	private Socket ct_sock;
	private DataInputStream ct_in;
	private DataOutputStream ct_out;
	private StringBuffer ct_buffer;
	private Thread thisThread;
	private String ct_logonID;
	private int ct_roomNumber;
	private static MessageBox msgBox, logonbox, fileTransBox;

	private static final String SEPARATOR = "|";
	private static final String DELIMETER = "'";
	private static final String DELIMETER2 = "=";

	private static final int REQ_LOGON = 1001;
	private static final int REQ_CREATEROOM = 1011;
	private static final int REQ_ENTERROOM = 1021;
	private static final int REQ_QUITROOM = 1031;
	private static final int REQ_LOGOUT = 1041;
	private static final int REQ_SENDWORD = 1051;
	private static final int REQ_SENDWORDTO = 1052;
	private static final int REQ_COERCEOUT = 1053;
	private static final int REQ_SENDFILE = 1061;

	private static final int YES_LOGON = 2001;
	private static final int NO_LOGON = 2002;
	private static final int YES_CREATEROOM = 2011;
	private static final int NO_CREATEROOM = 2012;
	private static final int YES_ENTERROOM = 2021;
	private static final int NO_ENTERROOM = 2022;
	private static final int YES_QUITRROOM = 2031;
	private static final int YES_LOGOUT = 2041;
	private static final int YES_SENDWORD = 2051;
	private static final int YES_SENDWORDTO = 2052;
	private static final int NO_SENDWORDTO = 2053;
	private static final int YES_COERCEOUT = 2054;
	private static final int YES_SENDFILE = 2061;
	private static final int NO_SENDFILE = 2062;
	private static final int MDY_WAITUSER = 2003;
	private static final int MDY_WAITINFO = 2013;
	private static final int MDY_ROOMUSER = 2023;
	private static final int ERR_ALREADYUSER = 3001;
	private static final int ERR_SERVERFULL = 3002;
	private static final int ERR_ROOMSFULL = 3011;
	private static final int ERR_ROOMERFULL = 3021;
	private static final int ERR_PASSWORD = 3022;
	private static final int ERR_REJECTION = 3031;
	private static final int ERR_NOUSER = 3032;

	public ClientThread() {
		ct_waitRoom = new WaitRoomDisplay(this);
		ct_chatRoom = null;
		try {
			ct_sock = new Socket(InetAddress.getLocalHost(), 2777);
			ct_in = new DataInputStream(ct_sock.getInputStream());
			ct_out = new DataOutputStream(ct_sock.getOutputStream());
			ct_buffer = new StringBuffer(4096);
			thisThread = this;
		} catch (Exception e) {
			MessageBoxLess msgout = new MessageBoxLess(ct_waitRoom, "연결에러", "서버에 접속할 수 없습니다.");
			msgout.show();
		}
	}
	//ClientThread의 기본 생성자로 sockt 포트설정 및 Data를 넘겨받을때 크기를 설정해줌

	public ClientThread(String hostaddr) {
		ct_waitRoom = new WaitRoomDisplay(this);
		ct_chatRoom = null;
		try {
			ct_sock = new Socket(hostaddr, 2777);
			ct_in = new DataInputStream(ct_sock.getInputStream());
			ct_out = new DataOutputStream(ct_sock.getOutputStream());
			ct_buffer = new StringBuffer(4096);
			thisThread = this;
		} catch (Exception e) {
			MessageBoxLess msgout = new MessageBoxLess(ct_waitRoom, "연결에러", "서버에 접속할 수 없습니다.");
			msgout.show();

		}
	}
	//host 매개변수를 입력받고 해당 host의 sockt설정 및 데이터를 설정해줌

	public void run() {
		try {
			Thread currThread = Thread.currentThread();
			while (currThread == thisThread) {
				String recvData = ct_in.readUTF();
				StringTokenizer st = new StringTokenizer(recvData, SEPARATOR);
				int command = Integer.parseInt(st.nextToken());

				switch (command) {
				case YES_LOGON: {
					logonbox.dispose();
					ct_roomNumber = 0;
					try {
						StringTokenizer st1 = new StringTokenizer(st.nextToken(), DELIMETER);
						Vector roomInfo = new Vector();
						while (st1.hasMoreTokens()) {
							String temp = st1.nextToken();
							if (!temp.equals("empty")) {
								roomInfo.addElement(temp);
							}
						}
						ct_waitRoom.roomInfo.setListData(roomInfo);
						ct_waitRoom.message.requestFocusInWindow();

					} catch (NoSuchElementException e) {
						ct_waitRoom.message.requestFocusInWindow();
					}
					break;
				}
				case NO_LOGON: {
					String id;
					int errCode = Integer.parseInt(st.nextToken());
					if (errCode == ERR_ALREADYUSER) {
						logonbox.dispose();
						JOptionPane.showMessageDialog(ct_waitRoom, "이미 다른 사용자가 있습니다.", "로그온",
								JOptionPane.ERROR_MESSAGE);
						id = ChatClient.getLogonID();
						requestLogon(id);
					} else if (errCode == ERR_SERVERFULL) {
						logonbox.dispose();
						JOptionPane.showMessageDialog(ct_chatRoom, "대화방이 만원입니다.", "로그온", JOptionPane.ERROR_MESSAGE);
						id = ChatClient.getLogonID();
						requestLogon(id);
					}
					break;
				}
				case MDY_WAITUSER: {
					StringTokenizer st1 = new StringTokenizer(st.nextToken(), DELIMETER);
					Vector user = new Vector();
					while (st1.hasMoreTokens()) {
						user.addElement(st1.nextToken());
					}
					ct_waitRoom.waiterInfo.setListData(user);
					ct_waitRoom.message.requestFocusInWindow();
					break;
				}
				case YES_CREATEROOM: {
					ct_roomNumber = Integer.parseInt(st.nextToken());
					ct_waitRoom.hide();
					if (ct_chatRoom == null) {
						ct_chatRoom = new ChatRoomDisplay(this);
						ct_chatRoom.isAdmin = true;
					} else {
						ct_chatRoom.show();
						ct_chatRoom.isAdmin = true;
						ct_chatRoom.resetComponets();
					}
					break;
				}
				case NO_CREATEROOM: {
					int errCode = Integer.parseInt(st.nextToken());
					if (errCode == ERR_ROOMSFULL) {
						msgBox = new MessageBox(ct_waitRoom, "대화방개설", "더 이상 대화방을 개설 할 수 없습니다.");
						msgBox.show();
					}
					break;
				}
				case MDY_WAITINFO: {
					StringTokenizer st1 = new StringTokenizer(st.nextToken(), DELIMETER);
					StringTokenizer st2 = new StringTokenizer(st.nextToken(), DELIMETER);

					Vector rooms = new Vector();
					Vector users = new Vector();
					while (st1.hasMoreTokens()) {
						String temp = st1.nextToken();
						if (!temp.equals("empty")) {
							rooms.addElement(temp);
						}
					}
					ct_waitRoom.roomInfo.setListData(rooms);

					while (st2.hasMoreTokens()) {
						users.addElement(st2.nextToken());
					}
					ct_waitRoom.waiterInfo.setListData(users);
					ct_waitRoom.message.requestFocusInWindow();

					break;

				}

				case YES_ENTERROOM: {
					ct_roomNumber = Integer.parseInt(st.nextToken());
					String id = st.nextToken();
					ct_waitRoom.hide();
					if (ct_chatRoom == null) {
						ct_chatRoom = new ChatRoomDisplay(this);

					} else {
						ct_chatRoom.show();
						ct_chatRoom.resetComponets();
					}
					break;
				}
				case NO_ENTERROOM: {
					int errCode = Integer.parseInt(st.nextToken());
					if (errCode == ERR_ROOMERFULL) {
						msgBox = new MessageBox(ct_waitRoom, "대화방입장", "대화방이 만원입니다.");
						msgBox.show();
					} else if (errCode == ERR_PASSWORD) {
						msgBox = new MessageBox(ct_waitRoom, "대화방입장", "비밀번호가 틀립니다.");
						msgBox.show();
					}
					break;
				}

				case MDY_ROOMUSER: {
					String id = st.nextToken();
					int code = Integer.parseInt(st.nextToken());

					StringTokenizer st1 = new StringTokenizer(st.nextToken(), DELIMETER);
					Vector user = new Vector();
					while (st1.hasMoreTokens()) {
						user.addElement(st1.nextToken());

					}
					ct_chatRoom.roomerInfo.setListData(user);
					if (code == 1) {
						ct_chatRoom.messages.append("###" + id + "님이 입장하셧습니다. ###\n");
					} else if (code == 2) {
						ct_chatRoom.messages.append("###" + id + "님이 강제퇴장 되었습니다.. ###\n");

					} else {
						ct_chatRoom.messages.append("###" + id + "님이 퇴장하셨습니다.. ###\n");

					}
					ct_chatRoom.message.requestFocusInWindow();
					break;
				}
				case YES_QUITRROOM: {
					String id = st.nextToken();
					if (ct_chatRoom.isAdmin)
						ct_chatRoom.isAdmin = false;
					ct_chatRoom.hide();
					ct_chatRoom.show();
					ct_chatRoom.resetComponets();
					ct_roomNumber = 0;
					break;
				}
				case YES_LOGOUT: {
					ct_waitRoom.dispose();
					if (ct_chatRoom != null) {
						ct_chatRoom.dispose();
					}
					release();
					break;
				}
				case YES_SENDWORD: {
					String id = st.nextToken();
					int roomNumber = Integer.parseInt(st.nextToken());
					try {
						String data = st.nextToken();
						if (roomNumber == 0) {
							ct_waitRoom.messages.append(id + " : " + data + "\n");
							if (id.equals(ct_logonID)) {
								ct_waitRoom.message.setText("");
								ct_waitRoom.message.requestFocusInWindow();

							}
							ct_waitRoom.message.requestFocusInWindow();

						} else {
							ct_chatRoom.messages.append(id + " : " + data + "\n");
							if (id.equals(ct_logonID)) {
								ct_chatRoom.message.setText("");
							}
							ct_chatRoom.message.requestFocusInWindow();
						}
					} catch (NoSuchElementException e) {
						if (roomNumber == 0)
							ct_waitRoom.message.requestFocusInWindow();
						else
							ct_chatRoom.message.requestFocusInWindow();
					}
					break;
				}
				case YES_SENDWORDTO: {
					String id = st.nextToken();
					String idTo = st.nextToken();
					int roomNumber = Integer.parseInt(st.nextToken());
					try {
						String data = st.nextToken();
						if (roomNumber == 0) {
							if (id.equals(ct_logonID)) {
								ct_waitRoom.message.setText("");
								ct_waitRoom.messages.append("귓속말<to :" + idTo + "> :" + data + "\n");
							} else {
								ct_waitRoom.messages.append("귓속말<from:" + id + ">:" + data + "\n");

							}
							ct_waitRoom.message.requestFocusInWindow();
						} else {
							if (id.equals(ct_logonID)) {
								ct_chatRoom.message.setText("");
								ct_chatRoom.messages.append("귓속말<to :" + idTo + "> :" + data + "\n");
							} else {
								ct_chatRoom.messages.append("귓속말<from:" + id + "> :" + data + "\n");
							}
							ct_chatRoom.message.requestFocusInWindow();

						}
					} catch (NoSuchElementException e) {
						if (roomNumber == 0)
							ct_waitRoom.message.requestFocusInWindow();
						else
							ct_chatRoom.message.requestFocusInWindow();
					}
					break;
				}
				case NO_SENDWORDTO: {
					String id = st.nextToken();
					int roomNumber = Integer.parseInt(st.nextToken());
					String message = "";
					if (roomNumber == 0) {
						message = "대기실에 " + id + "님이 존재하지 않습니다.";
						JOptionPane.showMessageDialog(ct_waitRoom, message, "귓속말 에러", JOptionPane.ERROR_MESSAGE);
					} else {
						message = "이 대화방에 " + id + "님이 존재하지 않습니다.";
						JOptionPane.showMessageDialog(ct_waitRoom, message, "귓속말 에러", JOptionPane.ERROR_MESSAGE);
					}
					break;
				}
				case REQ_SENDFILE: {
					String id = st.nextToken();
					int roomNumber = Integer.parseInt(st.nextToken());
					String message = id + "로 부터 파일전송을 수락하시겠습니까?";
					int value = JOptionPane.showConfirmDialog(ct_chatRoom, message, "파일수신", JOptionPane.YES_NO_OPTION);
					if (value == 1) {
						try {
							ct_buffer.setLength(0);
							ct_buffer.append(NO_SENDFILE);
							ct_buffer.append(SEPARATOR);
							ct_buffer.append(ct_logonID);
							ct_buffer.append(SEPARATOR);
							ct_buffer.append(roomNumber);
							ct_buffer.append(SEPARATOR);
							ct_buffer.append(id);
							send(ct_buffer.toString());

						} catch (IOException e) {
							System.out.println(e);
						}
					} else {
						StringTokenizer addr = new StringTokenizer(InetAddress.getLocalHost().toString(), "/");
						String hostname = "";
						String hostaddr = "";

						hostname = addr.nextToken();
						try {
							hostaddr = addr.nextToken();
						} catch (NoSuchElementException e) {
							hostaddr = hostname;
						}
						try {
							ct_buffer.setLength(0);
							ct_buffer.append(YES_SENDFILE);
							ct_buffer.append(SEPARATOR);
							ct_buffer.append(ct_logonID);
							ct_buffer.append(SEPARATOR);
							ct_buffer.append(roomNumber);
							ct_buffer.append(SEPARATOR);
							ct_buffer.append(id);
							ct_buffer.append(SEPARATOR);
							ct_buffer.append(hostaddr);
							send(ct_buffer.toString());

						} catch (IOException e) {
							System.out.println(e);
						}
						new ReciveFile();
					}
					break;
				}
				case NO_SENDFILE: {
					int code = Integer.parseInt(st.nextToken());
					String id = st.nextToken();
					fileTransBox.dispose();

					if (code == ERR_REJECTION) {
						String message = id + "님이 파일수신을 거부하였습니다.";
						JOptionPane.showMessageDialog(ct_chatRoom, message, "파일전송", JOptionPane.ERROR_MESSAGE);
						break;
					} else if (code == ERR_NOUSER) {
						String message = id + "님은 이 방에 존재하지 않습니다.";
						JOptionPane.showMessageDialog(ct_chatRoom, message, "파일전송", JOptionPane.ERROR_MESSAGE);
						break;
					}
				}
				case YES_SENDFILE: {
					String id = st.nextToken();
					String addr = st.nextToken();

					fileTransBox.dispose();

					new SendFile(addr);
					break;
				}
				case YES_COERCEOUT: {
					ct_chatRoom.hide();
					ct_waitRoom.show();
					ct_waitRoom.resetComponents();
					ct_roomNumber = 0;
					ct_waitRoom.messages.append("### 방장에 의해 강제퇴장 되었습니다. ###\n");
					break;
				}

				}
				Thread.sleep(200);
			}
		} catch (InterruptedException e) {
			System.out.println(e);
			release();
		} catch (IOException e) {
			System.out.println(e);
			release();
		}
	}
	//각 case 에 대해 이해가 더 필요하며 각 case 마다 message를 띄우거나 값을 저장시키고있음.

	public void requestLogon(String id) {
		try {
			logonbox = new MessageBox(ct_waitRoom, "로그온", "서버에 로그온 중입니다.");
			logonbox.show();
			ct_logonID = id;
			ct_buffer.setLength(0);
			ct_buffer.append(REQ_LOGON);
			ct_buffer.append(SEPARATOR);
			ct_buffer.append(id);
			send(ct_buffer.toString());

		} catch (IOException e) {
			System.out.println(e);
		}
	}
	//id를 입력 후 로그인할때 콘솔에 출력하기위함.

	public void requestCreateRoom(String roomName, int roomMaxUser, int isRock, String password) {
		try {
			ct_buffer.setLength(0);
			ct_buffer.append(REQ_CREATEROOM);
			ct_buffer.append(SEPARATOR);
			ct_buffer.append(ct_logonID);
			ct_buffer.append(SEPARATOR);
			ct_buffer.append(roomName);
			ct_buffer.append(DELIMETER);
			ct_buffer.append(roomMaxUser);
			ct_buffer.append(DELIMETER);
			ct_buffer.append(isRock);
			ct_buffer.append(DELIMETER);
			ct_buffer.append(password);
			send(ct_buffer.toString());

		} catch (IOException e) {
			System.out.println(e);
		}
	}
	//채팅방을 만들때 정보를 StringBuffer에 추가후 toString함.

	public void requestEnterRoom(int roomNumber, String password) {
		try {
			ct_buffer.setLength(0);
			ct_buffer.append(REQ_ENTERROOM);
			ct_buffer.append(SEPARATOR);
			ct_buffer.append(ct_logonID);
			ct_buffer.append(SEPARATOR);
			ct_buffer.append(roomNumber);
			ct_buffer.append(SEPARATOR);
			ct_buffer.append(password);
			send(ct_buffer.toString());
		} catch (IOException e) {
			System.out.println(e);
		}
	}
	//채팅방에 들어갈때의 정보를 넘겨받음

	public void requestQuitRoom() {
		try {
			ct_buffer.setLength(0);
			ct_buffer.append(REQ_QUITROOM);
			ct_buffer.append(SEPARATOR);
			ct_buffer.append(ct_logonID);
			ct_buffer.append(SEPARATOR);
			ct_buffer.append(ct_roomNumber);
			send(ct_buffer.toString());

		} catch (IOException e) {
			System.out.println(e);
		}
	}
	//채팅방을 나갈때의 정보를 넘겨받음.

	public void requestLogout() {
		try {
			ct_buffer.setLength(0);
			ct_buffer.append(REQ_LOGOUT);
			ct_buffer.append(SEPARATOR);
			ct_buffer.append(ct_logonID);
			send(ct_buffer.toString());

		} catch (IOException e) {
			System.out.println(e);
		}
	}
	//로그아웃버튼을 눌렀을때 데이터를 넘겨받음.

	public void requestSendWord(String data) {

		try {
			ct_buffer.setLength(0);
			ct_buffer.append(REQ_SENDWORD);
			ct_buffer.append(SEPARATOR);
			ct_buffer.append(ct_logonID);
			ct_buffer.append(SEPARATOR);
			ct_buffer.append(ct_roomNumber);
			ct_buffer.append(SEPARATOR);
			ct_buffer.append(data);
			send(ct_buffer.toString());

		} catch (IOException e) {
			System.out.println(e);
		}

	}
	//채팅을할때의 데이터를넘겨받음.

	public void requestSendWordTo(String data, String idTo) {
		try {
			ct_buffer.setLength(0);
			ct_buffer.append(REQ_SENDWORDTO);
			ct_buffer.append(SEPARATOR);
			ct_buffer.append(ct_logonID);
			ct_buffer.append(SEPARATOR);
			ct_buffer.append(ct_roomNumber);
			ct_buffer.append(SEPARATOR);
			ct_buffer.append(idTo);
			ct_buffer.append(SEPARATOR);
			ct_buffer.append(data);
			send(ct_buffer.toString());
		} catch (IOException e) {
			System.out.println(e);
		}
	}
	//귓속말을할떄의 데이터를넘겨받음.

	public void requestCoerceOut(String idTo) {
		try {
			ct_buffer.setLength(0);
			ct_buffer.append(REQ_COERCEOUT);
			ct_buffer.append(SEPARATOR);
			ct_buffer.append(ct_roomNumber);
			ct_buffer.append(SEPARATOR);
			ct_buffer.append(idTo);
			send(ct_buffer.toString());
		} catch (IOException e) {
			System.out.println(e);
		}
	}
	//강제퇴장 할시에 데이터를넘겨받음

	public void requestSendFile(String idTo) {
		fileTransBox = new MessageBox(ct_chatRoom, "파일전송", "상대방의 승인을 기다립니다.");
		fileTransBox.show();

		try {
			ct_buffer.setLength(0);
			ct_buffer.append(REQ_SENDFILE);
			ct_buffer.append(SEPARATOR);
			ct_buffer.append(ct_logonID);
			ct_buffer.append(SEPARATOR);
			ct_buffer.append(ct_roomNumber);
			ct_buffer.append(SEPARATOR);
			ct_buffer.append(idTo);
			send(ct_buffer.toString());

		} catch (Exception e) {
			System.out.println(e);
		}
	}
	//파일을 전송할떄의 데이터를넘겨받음.

	private void send(String sendData) throws IOException {
		ct_out.writeUTF(sendData);
		ct_out.flush();
	}
	//데이터를 넘겨줄떄 UTF 형식으로 쓰기위해.

	public void release() {
		if (thisThread != null) {
			thisThread = null;
		}
		try {
			if (ct_out != null) {
				ct_out.close();
			}
		} catch (IOException e) {
		} finally {
			ct_out = null;
		}
		try {
			if (ct_in != null) {
				ct_in.close();

			}
		} catch (IOException e) {
		} finally {
			ct_in = null;
		}
		try {
			if (ct_sock != null) {
				ct_sock.close();
			}
		} catch (IOException e) {
		} finally {
			ct_sock = null;

		}
		System.exit(0);
	}
	//이해가 필요 . thread를 동기화해줄때 초기화하는걸로보임.
}
