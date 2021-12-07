public class Solution {

	static String old_id;
	static String new_id;

	public static void main(String[] args) {
		int[] ar = { 1, 3, 4, 5, 8, 2, 1, 4, 5, 9, 5 };
		int a = 0;
		int b = 0;
		int c1 = 0;
		int c2 = 0;
		String c = "";
		String hands = "right";
		for (int i = 0; i < ar.length; i++) {
			if (ar[i] == 1) {
				a = 3;
				c += "L";
			}
			if (ar[i] == 4) {
				a = 2;
				c += "L";
			}
			if (ar[i] == 7) {
				a = 1;
				c += "L";
			}
			if (ar[i] == 3) {
				b = 3;
				c += "R";
			}
			if (ar[i] == 6) {
				b = 2;
				c += "R";
			}
			if (ar[i] == 9) {
				b = 1;
				c += "R";
			}
			if (ar[i] == 2) {
				
			}
			if (ar[i] == 5) {
				

			}
			if (ar[i] == 8) {
				
			}
			if (ar[i] == 0) {
				
			}

		}
		System.out.println(c);

	}
}
