import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class JDBCex {

	public static void main(String[] args) {
		
		
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			
			Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/sales","root","123456");
			
			Statement stmt = conn.createStatement();
			
			Scanner input = new Scanner(System.in);
			System.out.println("이름 입력 : ");
			String name = input.next();
			
			ResultSet rs = stmt.executeQuery("insert into customer values('kiwi','"+name+"','남','서울','1995-03-21','010-3326-5524','silver',0)");
			/*while(rs.next()) {
				System.out.println(rs.getString("cus_id"));
				System.out.println(rs.getString("cus_name"));
				System.out.println(rs.getString("sung"));
				System.out.println(rs.getString("addr"));
				System.out.println(rs.getString("birth"));
				System.out.println(rs.getString("phone"));
				System.out.println(rs.getString("grade"));
				System.out.println(rs.getString("rest"));
			}*/
			System.out.println("DB 접속 성공");
		} catch (ClassNotFoundException e) {
			System.out.println("JDBC 드라이버 로드 에러");
		}catch(SQLException e) {
			System.out.println("DB 에러");
		}
		

	}

}
