package TicTacToe;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class Tic_common {
	// port number
	static final int nPort = 10000;
	static final String quitWord = "quit";
	
	// 틱택토 게임창 콘솔에 출력
	public static void showScreen(int[][] arr) {
		System.out.println("-----");
		for (int i = 0; i < 3; i++) {
			System.out.print("|");
			for (int j = 0; j < 3; j++) {
				if (arr[i][j] == 0)
					System.out.print(" ");
				else if (arr[i][j] == 1)
					System.out.print("O");
				else if (arr[i][j] == 2)
					System.out.print("X");
			}
			System.out.println("|");
		}
		System.out.println("-----");
	}
	
	public static boolean isMarked(String str, int[][] arr) {
		// 클라이언트 입력 문자열로부터 파싱 : x,y 좌표 구하기
		int id_comma = str.indexOf(",");
		int idx = Integer.parseInt(str.substring(0, id_comma));
		int idy = Integer.parseInt(str.substring(id_comma + 1));
		// 해당 좌표에 이미 마킹이 되어있으면
		if (arr[idx-1][idy-1] != 0)
			return true;
		else
			return false;
	}
	
	
	//
	public static void markIt(String str, int[][] arr, int flag) {
		// 클라이언트 입력 문자열로부터 파싱 : x,y 좌표 구하기
		int id_comma = str.indexOf(",");
		int idx = Integer.parseInt(str.substring(0, id_comma));
		int idy = Integer.parseInt(str.substring(id_comma + 1));
		// flag 값은 client인지 = 0, server인지 = 1
		System.out.print("--+ coord " + idx + "," + idy + " checked by ");

		if (flag == 0) {
			System.out.print("Client");
			// 해당 좌표에 클라이언트 마킹
			arr[idx-1][idy-1] = 1;
		}
		else if (flag == 1) {
			System.out.print("Server");
			// 해당 좌표에 클라이언트 마킹
			arr[idx-1][idy-1] = 2;
		}
	}
	
	// 전체 3x3 게임판에서 빙고 생기면 빙고 주인 (클라이언트 1, 서버 2) 반환, 없으면 0 반환
	public static int isBingo(int[][] arr) {
		for (int i = 0; i < 3; i++) {
			if (arr[i][0] == arr[i][1] && arr[i][0] == arr[i][2] && arr[i][0] != 0)
				return arr[i][0];
			if (arr[0][i] == arr[1][i] && arr[0][i] == arr[2][i] && arr[0][i] != 0)
				return arr[i][0];
		}
		if (arr[0][0] == arr[1][1] && arr[0][0] == arr[2][2] && arr[0][0] != 0)
			return arr[0][0];
		else if (arr[1][1] == arr[2][0] && arr[1][1] == arr[0][2] && arr[1][1] != 0)
			return arr[1][1];
		else
			return 0;
	}
	
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
	
	
	static String readConsole() {
		// 콘솔에서 입력받기
		InputStream is = System.in;
	
		byte[] byteArray = new byte[100];
		int count = 0;
		try {
			count = is.read(byteArray);
		} catch (IOException e) { }
		// 입력받은 문자열 전달
		String str1 = new String(byteArray, 0, count, Charset.defaultCharset());
		String str2 = str1.trim();
		return str2;
	}
}

