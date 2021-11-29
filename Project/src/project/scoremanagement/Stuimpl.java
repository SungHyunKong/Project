package project.scoremanagement;

import java.util.Vector;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.io.*;

class StuImple implements Student
{	
	private StuDTO dto; 
	private StuForm sf;
	private ArrayList <StuDTO> li;  //추가된 데이터
	private HashSet <StuDTO> hash;
	private ArrayList <StuDTO> allList;
	private HashSet <StuDTO> allHash;

	public StuImple(StuForm sf){
		this.sf=sf;
		li = new ArrayList<StuDTO>();
		allList= new ArrayList<StuDTO>();
	}
	//입력 메소드
	@Override
	public void insert(){
		dto = new StuDTO(sf.getHak(), sf.getName(), sf.getKor(),sf.getEng(),sf.getMath());
		li.add(dto);
		allList.add(dto);
	}
	//출력 메소드
	@Override
	public DefaultTableModel outputModel(DefaultTableModel model){
		if(li.size()!=0){
			for(StuDTO data:li){
				Vector<Object> v= new Vector<Object>();
				v.add(data.getHak());
				v.add(data.getName());
				v.add(data.getKor());
				v.add(data.getEng());
				v.add(data.getMath());
				v.add(data.getTot());
				v.add(data.getAvg());
				model.addRow(v);
			}
		} else {
			while(model.getRowCount()!=0){model.removeRow(0);}
			for(StuDTO data: allList){
				Vector<Object> v= new Vector<Object>();
				v.add(data.getHak());
				v.add(data.getName());
				v.add(data.getKor());
				v.add(data.getEng());
				v.add(data.getMath());
				v.add(data.getTot());
				v.add(data.getAvg());
				model.addRow(v);
			}
		}
		
		//값 초기화
		while(li.size()!=0){ li.remove(0); }
		
		return model;
	}
	//찾기 메소드
	@Override
	public DefaultTableModel search(String hak,DefaultTableModel searchModel){
		for(int i=0;i<searchModel.getRowCount();i++){
			if(!hak.equals(searchModel.getValueAt(i,0))){  //만약 학번과 같은 값을 찾는다면
				
				searchModel.removeRow(i);
				i=-1;
			}
		}
		return searchModel;
	}
	//정렬 메소드
	@Override
	public DefaultTableModel to_desc(DefaultTableModel model){
		Collections.sort(allList);
		if(li.size()!=0){
			for(StuDTO data:li){
				Vector<Object> v= new Vector<Object>();
				v.add(data.getHak());
				v.add(data.getName());
				v.add(data.getKor());
				v.add(data.getEng());
				v.add(data.getMath());
				v.add(data.getTot());
				v.add(data.getAvg());
				model.addRow(v);
			}
		} else {
			while(model.getRowCount()!=0){model.removeRow(0);}
			System.out.println(model.getRowCount());
			for(StuDTO data: allList){
				Vector<Object> v= new Vector<Object>();
				v.add(data.getHak());
				v.add(data.getName());
				v.add(data.getKor());
				v.add(data.getEng());
				v.add(data.getMath());
				v.add(data.getTot());
				v.add(data.getAvg());
				model.addRow(v);
			}
		}
		//값 초기화
		while(li.size()!=0){ li.remove(0); }
		return model;
	}
	@Override
	public DefaultTableModel delete(String hak, DefaultTableModel model){
		for(int i=0;i<allList.size();i++){
			if(hak.equals(allList.get(i).getHak())){  //만약 학번과 같은 값을 찾는다면
				allList.remove(i);
				break;
			}
		}
		model=outputModel(model);
		return model;
	}

	@Override
	public void save(){
		Object o =allList;
		File file = null;
		JFileChooser chooser = new JFileChooser();
		int result= chooser.showSaveDialog(sf);
		if(result==JFileChooser.APPROVE_OPTION){
			file = chooser.getSelectedFile();
		}
		if(file!=null){
			try{
			FileOutputStream fos = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(o);
			oos.close();
			fos.close();
			} catch(IOException e){}
		}
		
	}
	@Override
	public DefaultTableModel load(DefaultTableModel model){
		try{
			File file = null;
			JFileChooser c= new JFileChooser();
			int result = c.showOpenDialog(sf);
			if(result==JFileChooser.APPROVE_OPTION){
				file = c.getSelectedFile();
				FileInputStream fis = new FileInputStream(file);
				ObjectInputStream ois = new ObjectInputStream(fis);
				Object o = ois.readObject();
				allList=(ArrayList<StuDTO>) o;
				fis.close();
				ois.close();
			}
		} catch(IOException | ClassNotFoundException e){ e.printStackTrace(); }
		model=outputModel(model);
		return model;
	}
}

