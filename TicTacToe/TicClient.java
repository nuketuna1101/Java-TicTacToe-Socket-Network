package TicTacToe;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class TicClient {

	public static void main(String[] args) {
		// 
		System.out.println("<< TCP TicTacToe Client >>");
		// 이차원 배열 maps 생성 및 0으로 값 초기화
		int[][] maps = new int[3][3];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				maps[i][j] = 0;
			}
		}

		try {
			// 1. 접속 연결 및 클라이언트에게 제약사항 출력
			Socket socket = new Socket(InetAddress.getByName("localhost"), Tic_common.nPort);
			System.out.println("- server connected info : " + socket.getInetAddress() + " : " + socket.getPort());
			System.out.println("- '>' mark shows your turn.");
			System.out.println("- in your turn, type 'n,m' to choose the coordinate (1 <= n, m  <= 3)");
			System.out.println("- type 'quit' to exit the game.");
			System.out.println("- note : (Client : O  Server : X)");
			//
			doClientGame(socket);

			System.out.println();
			System.out.println("- The game End");

		} catch (UnknownHostException e) {
		} catch (IOException e) {
		}
	}

	static void doClientGame(Socket socket) throws IOException {
		// 데이터 입출력 스트림
		DataInputStream dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
		DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
		// 클라이언트의 입력 문자열, 서버의 입력 문자열
		String myStr, serverStr;
		
		// 이차원 배열 maps 생성 및 0으로 값 초기화
		int[][] maps = new int[3][3];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				maps[i][j] = 0;
			}
		}
		
		
		while (true) {
			// 클라이언트 턴임을 명시, 클라이언트 입력받기
			System.out.print("client(me)> ");
			myStr = Tic_common.readConsole();			
			// 클라이언트 입력이 quitword 입력 시 강제종료
			if (myStr.equals(Tic_common.quitWord))
				break;
			
			// 이미 마킹되있을 경우의 flow 처리
			while(Tic_common.isMarked(myStr, maps)) {
				System.out.println("Already marked in this coord. Type a proper one.");
				System.out.print("client(me)> ");
				myStr = Tic_common.readConsole();
				// 클라이언트 입력이 quitword 입력 시 강제종료
				if (myStr.equals(Tic_common.quitWord))
					break;
			}
			dos.writeUTF(myStr);
			dos.flush();
			// 클라이언트 입력 문자열로부터 파싱 : x,y 좌표 구하기
			Tic_common.markIt(myStr, maps, 0);
			// 틱택토 게임판 출력
			Tic_common.showScreen(maps);
			// 빙고 완성여부 체크
			if (Tic_common.isBingo(maps) == 1) {
				System.out.println("+++\n--- Congratulation. You (Client) Win.");
				break;
			}
			// 무승부 체크
			if (Tic_common.isDraw(maps)) {
				System.out.println("+++\n--- TIE.");
				break;
			}
			
			// server로부터 받은 문자열 텍스트
			serverStr = dis.readUTF();
			//System.out.println("\t\t\t" + "server: " + serverStr);
			
			// server에서 quitword 입력 시에도 강제종료
			if (serverStr.equals(Tic_common.quitWord))
				break;
			
//			// server 입력 문자열로부터 파싱 : x,y 좌표 구하기
			Tic_common.markIt(serverStr, maps, 1);
			// 틱택토 게임판 출력
			Tic_common.showScreen(maps);
			// 빙고 완성여부 체크
			if (Tic_common.isBingo(maps) == 2) {
				System.out.println("+++\n--- Try better. You (Client) Lose.");
				break;
			}
			// 무승부 체크
			if (Tic_common.isDraw(maps)) {
				System.out.println("+++\n--- TIE.");
				break;
			}
		}
	}

}
