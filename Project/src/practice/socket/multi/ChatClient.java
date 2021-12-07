package practice.socket.multi;

import javax.swing.*;

public class ChatClient {
	

	// 로그인 ID를 입력하세요 라는 다이얼로그 창을 띄움
	
	public static void main(String args[]) {
		
		try {
			if(args.length == 0) {
				ClientThread thread = new ClientThread();
				thread.start();
				thread.requestLogon(id);
				
			}else if(args.length == 1){
				
				ClientThread thread = new ClientThread(args[0]);
				thread.start();
				thread.requestLogon(id);
				
				
			}
			
			//로그온 id가입력이안됬을시 thread의 기본생성자를 실행후 ""을 넣고 로그온 id가 입력이되면 id를매개변수로한 thread가 실행되고 id를입력함.
			
		} catch (Exception e) {
		System.out.println(e);
		}
		
	}

}
