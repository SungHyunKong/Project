package practice.socket.multi;

import javax.swing.*;

public class ChatClient {
	

	// �α��� ID�� �Է��ϼ��� ��� ���̾�α� â�� ���
	
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
			
			//�α׿� id���Է��̾ȉ����� thread�� �⺻�����ڸ� ������ ""�� �ְ� �α׿� id�� �Է��̵Ǹ� id���Ű��������� thread�� ����ǰ� id���Է���.
			
		} catch (Exception e) {
		System.out.println(e);
		}
		
	}

}
