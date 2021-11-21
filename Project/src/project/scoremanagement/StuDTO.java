package project.scoremanagement;

import java.util.*;
import java.io.*;
class  StuDTO implements Serializable, Comparable <StuDTO>
{
	private String hak;
	private String name;
	private int kor;
	private int eng;
	private int math;
	private int tot;
	private double avg;
	public StuDTO(String hak, String name, int kor, int eng, int math){
		this.name=name;
		this.hak=hak;
		this.kor=kor;
		this.eng=eng;
		this.math=math;
		this.tot=kor+eng+math;
		this.avg= tot/3;
	}

	public String getName(){ return name; }
	public String getHak(){ return hak; }
	public int getKor(){ return kor; }
	public int getEng(){ return eng; }
	public int getMath(){ return math; }
	public int getTot(){ return tot; }
	public double getAvg(){ return avg; }

	public int compareTo(StuDTO o){  //compareTo 오버라이딩(나이를 기준으로)
        if(getTot()<o.getTot()) return -1;
        else if(getTot()==o.getTot()) return 0;
        else return 1;
    }
	 @Override
	public String toString() {
		return hak +  "," + name +"," + kor+"," + eng+"," + math+"," + tot+"," + avg;
	}
}
/*
ScoreForm.java  레이어드만
Score.java  implements 
ScoreDTO     .java
Scoreimpl
ScoreMain
학번, 이름, 국어, 영어, 수학
*/