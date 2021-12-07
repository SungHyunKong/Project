import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class IdInfo extends JFrame implements ActionListener {

	JButton btnIn, btnOut, btnUpdate, btnDelete;
	JLabel lbId,lbPwd,lbName,lbAddr,lbBirth,lbPhone;
	JTextField tfId,tfPwd,tfName,tfAddr,tfBirth,tfPhone;
	
	GridBagLayout gb;
	GridBagConstraints gc;
	
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
		gb= new GridBagLayout();
		setLayout(gb);
		gc = new GridBagConstraints();
		gc.fill= GridBagConstraints.BOTH;
		gc.weightx=1.0;
		gc.weighty=1.0;
		
		lbId= new JLabel("아이디 : ");
		tfId = new JTextField(13);
		btnUpdate = new JButton("check");
		gbAdd(lbId,0,0,1,1);
		gbAdd(btnUpdate,2,2,3,1);
		gbAdd(tfId,1,1,3,1);
		
		
		lbPwd= new JLabel("비밀번호 : ");
		tfPwd = new JPasswordField(20);
		gbAdd(lbPwd,0,0,1,1);
		gbAdd(tfPwd,1,1,3,1);
		
		lbName= new JLabel("이름 : ");
		tfName = new JTextField(20);
		gbAdd(lbName,0,2,3,1);
		gbAdd(tfName,1,2,3,1);
		
		
		
		
		

	}
	
	private void gbAdd(JComponent c, int x, int y , int w , int h) {
		gc.gridx = x;
		gc.gridy = y;
		gc.gridwidth= w;
		gc.gridheight = h;
		gb.setConstraints(c, gc);
		gc.insets = new Insets(2, 2, 2, 2);
		add(c,gc);
		
	
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}
}
