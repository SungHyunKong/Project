import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class IdInfo extends JFrame implements ActionListener {

	JButton btnIn, btnOut, btnUpdate, btnDelete;

	IdForm idf;

	public IdInfo() {
		IdUi();
		btnUpdate.setEnabled(false);
		btnUpdate.setVisible(false);
		btnDelete.setEnabled(false);
		btnDelete.setVisible(false);
	}

	public IdInfo(IdForm idf) {
		IdUi();
		btnUpdate.setEnabled(false);
		btnUpdate.setVisible(false);
		btnDelete.setEnabled(false);
		btnDelete.setVisible(false);
		this.idf = idf;
	}

	public IdInfo(String id, IdForm idf) {
		IdUi();
		btnIn.setEnabled(false);
		btnIn.setVisible(false);
		this.idf = idf;
		
		System.out.println("id="+id);
	}

	private void IdUi() {
		this.setTitle("회원가입 정보입력");

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}
}
