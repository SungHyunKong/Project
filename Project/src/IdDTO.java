
public class IdDTO {
	private String id;
	private String pwd;
	private String name;
	private String addr;
	private int birth;
	private String phone;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public int getBirth() {
		return birth;
	}
	public void setBirth(int birth) {
		this.birth = birth;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@Override
	public String toString() {
		return "DTO = [id = "+id + 
				",pwd = " + pwd +
				",name = " + name +
				", addr = " + addr +
				", birth = "+ birth+
				", phone = " + phone+"]";
	}
	

}
