package GUI_Tics;

import javax.swing.JButton;

public class GTic_common {
	static final int nPort = 10000;
	//static final String quitWord = "quit";
	
	
	// 두 버튼 비교해서 일치할 경우 1 or 2 (클라이언트, 서버), 아니면 0
	public static int markMatch(JButton x, JButton y) {
		if (x.getText().equals(y.getText()) && x.getText().equals("O"))
			return 1;
		else if (x.getText().equals(y.getText()) && x.getText().equals("X"))
			return 2;
		else
			return 0;
	}
	
//	// 전체 3x3 게임판에서 빙고 생기면 빙고 주인 (클라이언트 1, 서버 2) 반환, 없으면 0 반환
//	public static int isBingo(JButton[][] jbs) {
//		for (int i = 0; i < 3; i++) {
//			if (markMatch(jbs[i][0], jbs[i][1]) == markMatch(jbs[i][0], jbs[i][2]))
//				return markMatch(jbs[i][0], jbs[i][1]);
//			if (markMatch(jbs[0][i], jbs[1][i]) == markMatch(jbs[0][i], jbs[2][i]))
//				return markMatch(jbs[0][i], jbs[1][i]);
//		}
//		if (markMatch(jbs[0][0], jbs[1][1]) == markMatch(jbs[0][0], jbs[2][2]))
//			return markMatch(jbs[0][0], jbs[1][1]);
//		else if (markMatch(jbs[1][1], jbs[2][0]) == markMatch(jbs[1][1], jbs[0][2]))
//			return markMatch(jbs[1][1], jbs[2][0]);
//		else
//			return 0;
//	}
	
	// 전체 3x3 게임판에서 빙고 생기면 빙고 주인 (클라이언트 1, 서버 2) 반환, 없으면 0 반환
	public static int isBingo(int[][] arr) {
		for (int i = 0; i < 3; i++) {
			if (arr[i][0] == arr[i][1] && arr[i][0] == arr[i][2] && arr[i][0] != 0)
				return arr[i][0];
			if (arr[0][i] == arr[1][i] && arr[0][i] == arr[2][i] && arr[0][i] != 0)
				return arr[0][i];
		}
		if (arr[0][0] == arr[1][1] && arr[0][0] == arr[2][2] && arr[0][0] != 0)
			return arr[0][0];
		else if (arr[1][1] == arr[2][0] && arr[1][1] == arr[0][2] && arr[1][1] != 0)
			return arr[1][1];
		else
			return 0;
	}
	
//	// 전체 3x3 게임판에서 모든 칸 다 채워졌는데 bingo 없으면 무승부
//	public static boolean isDraw(JButton[][] jbs) {
//		// cnt는 게임판에서 마킹되어 있지 않은 칸의 개수
//		int cnt = 9;
//		for (int i = 0; i < 3; i++) {
//			for (int j = 0; j < 3; j++) {
//				if (jbs[i][j].getText() == "O" || jbs[i][j].getText() == "X")
//					cnt--;
//			}
//		}
//		// 빙고도 없고 마킹도 다 되어있고
//		if(isBingo(jbs) == 0 && cnt == 0)
//			return true;
//		else
//			return false;
//	}
	
	// 전체 3x3 게임판에서 모든 칸 다 채워졌는데 bingo 없으면 무승부
	public static boolean isDraw(int[][] arr) {
		// 모든 칸이 다 채워졌다면, 배열의 9개 원소곱이 0이 아닐것
		int mul = 1;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				mul *= arr[i][j];
			}
		}
		
		if(isBingo(arr) == 0 && mul != 0)
			return true;
		else
			return false;
	}
	
}
