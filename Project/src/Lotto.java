import java.util.Scanner;

public class Lotto {

		public int[] lotto(int[] lottos, int[] win_nums) {
			int[] answer = new int[6];
			int count=0;
			
			for(int i=0; i<lottos.length; i++) {
				for(int j=0 ; j<win_nums.length; j++) {
					if(lottos[i] == win_nums[j]) {
						count++;
					}
				}
			}
			
			answer[0] = count;
			return answer;
		}
	
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		int[] lottos = new int[6];
		int[] win_nums = new int[6];
		int[] answer = new int[6];
		
		for(int i=0; i<lottos.length; i++) {
			lottos[i] = (int)(Math.random()*45+1);
			for(int j=0; j<i; j++) {
				if(lottos[i] == lottos[j]) {
					i--;
				}
			}
		}
		
		for(int i=0; i<lottos.length; i++) {
			win_nums[i] = (int)(Math.random()*45+1);
			for(int j=0; j<i; j++) {
				if(win_nums[i] == win_nums[j]) {
					i--;
				}
			}
		}
		
		Lotto lotto = new Lotto();
		answer = lotto.lotto(lottos, win_nums);
		
		for(int k : answer) {
			System.out.println(k);
		}
	}
}
