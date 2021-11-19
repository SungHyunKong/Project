package practice.socket.multi;
import java.util.*;


public class ChatRoom {
	
	private static final String DELIMETER = "'";
	private static final String DELIMETER1 = "=";
	public static int roomNumber = 0;
	private Vector userVector;
	private Hashtable userHash;
	private String roomName;
	private int roomMaxUser;
	private int roomUser;
	private boolean isRock;
	private String password;
	private String admin;
	
	public ChatRoom(String roomName, int roomMaxUser, boolean isRock, String password, String admin) {
		roomNumber++;
		this.roomName = roomName;
		this.roomMaxUser = roomMaxUser;
		this.roomUser = 0;
		this.isRock = isRock;
		this.password = password;
		this.admin= admin;
		this.userVector = new Vector(roomMaxUser);
		this.userHash = new Hashtable(roomMaxUser);
	}
	
	public boolean addUser(String id, ServerThread client) {
		if(roomUser == roomMaxUser) {
			return false;
			
		}
		//방의 유저가 host가 설정한 roomMaxUser 의 수와 같으면 false를 출력함
		userVector.addElement(id);
		userHash.put(id, client);
		roomUser++;
		return true;
	}
	// roomMaxUser수보다 적을때 userVector 배열에 id를 추가하고 id,client를 userhash 에도 추가한 후 roomUser의 수를 1 증가시키고 true를리턴함.
	public boolean checkPassword(String passwd) {
		return password.equals(passwd);
	}
	///비밀번호 확인
	public boolean checkUserIDs(String id) {
		Enumeration ids = userVector.elements();
		while(ids.hasMoreElements()) {
			String tempId = (String) ids.nextElement();
			if(tempId.equals(id)) return true;
		}
		return false;
	}
	//userVector의 id 값을 Enumeration으로 출력을 할 때 tempId 에 저장후 tmpId의 id 값과 매게변수로 가져오는 id값을 비교해서 같은 id인지 확인
	public boolean isRocked() {
		return isRock;
	}
	//isRock을 return
	public boolean delUser(String id) {
		userVector.removeElement(id);
		userHash.remove(id);
		roomUser--;
		return userVector.isEmpty();
	}
	//입력받은 id 값을 vector 배열에서 삭제후 roomuser값을 1 감소시킴.
	public synchronized String getUsers() {
		StringBuffer id = new StringBuffer();
		String ids;
		Enumeration enu = userVector.elements();
		while(enu.hasMoreElements()) {
			id.append(enu.nextElement());
			id.append(DELIMETER);
			
		}
		//userVector의 id 값을 Stringbuffer id 에 추가후 ""을 추가.
		try {
			ids = new String(id);
			ids = ids.substring(0, ids.length() -1);
		} catch (StringIndexOutOfBoundsException e) {
			return "";
		}
		return ids;
	}
	//StringBuffer id 값을 ids 에 넣은 후 ids를 subString한 후 리턴(목적을 모르겠음.)
	
	 public ServerThread getUser(String id){
	 ServerThread client = null;
	 client = (ServerThread) userHash.get(id);
	 return client;
	 }
	 //client를 null로 초기화후 userhash에서 id를 입력 하여 해당 client를 얻어서 SErverThread로 강제형변환하여 client에 저장
	
	
	public Hashtable getClients() {
		return userHash;
	}
	//userHash 를 get하기위함
	
	public String toString() {
		StringBuffer room = new StringBuffer();
		room.append(roomName);
		room.append(DELIMETER1);
		room.append(String.valueOf(roomUser));
		room.append(DELIMETER1);
		room.append(String.valueOf(roomMaxUser));
		room.append(DELIMETER1);
		if(isRock) {
			room.append("비공개");
			
		}else {
			room.append("공개");
		}
		room.append(DELIMETER1);
		room.append(admin);
		return room.toString();
	}
	
	//toString를 오버라이딩하여 출력문을 재정의함.
	public static synchronized int getRoomNumber() {
		return roomNumber;
	}
	//roomNumber를 동기화
}

