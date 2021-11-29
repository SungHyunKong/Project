import java.util.Scanner;

public class Lotto {
	
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		int[] answer = new int[2];
		int[] lottos = {44, 1, 0, 0, 31, 25};
		int[] win_nums = {31, 10, 45, 1, 6, 19};
		
		int a = 0;
		int z = 0;
		
		for(int i=0 ; i<lottos.length; i++) {
			for(int j=0; j<win_nums.length; j++) {
				if(lottos[i] == win_nums[j]) {
					a++;
				}
			}
			if(lottos[i] == 0) {
				z++;
			}
		}
		
		if(a+z == 2) {
			answer[0] = 5;
			
		}else if( a+z == 3) {
			answer[0] = 4;
		}else if(a+z == 4) {
			answer[0] = 3;
		}else if(a+z == 5) {
			answer[0] = 2;
		}else if(a+z == 6) {
			answer[0] = 1;
		}else {
			answer[0] = 6;
			
		}
		if(a==2) {
		 answer[1] = 5;
		}else if(a==3) {
			
			answer[1] = 4;
		}else if(a==4) {
			answer[1] = 3;
			
		}else if( a== 5) {
			answer[1] = 2;
			
		}else if(a==6) {
			answer[1] = 1;
			
		}else {
			
			answer[1] = 6;
		}
		
		
	}
}
