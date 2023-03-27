package TicTacToe;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TicServer {

	public static void main(String[] args) {

		System.out.println("<< TCP TicTacToe Server >>");
		
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(Tic_common.nPort);
		} catch (IOException e) {
			System.out.println(" - error: cannot open port " + Tic_common.nPort);
			System.out.println();
			System.exit(0);
		}
		
		try {
			// 2단계) 클라이언트 연결하여 소켓 만들기 
			System.out.println("- waiting for Client connection...");
			Socket socket = serverSocket.accept();
			System.out.println("- client connected info : " + socket.getInetAddress() + ":" + socket.getPort());
			System.out.println("- '>' mark shows your turn.");
			System.out.println("- in your turn, type 'n,m' to choose the coordinate (1 <= n, m  <= 3)");
			System.out.println("- type 'quit' to exit the game.");
			System.out.println("- note : (Client : O  Server : X)");
			System.out.println();
			
			// 3단계) 채팅 시작
			doServerGame(socket);
			
			System.out.println();
			System.out.println("- The game End");
		
		} catch (IOException e) {
		}
	}

	static void doServerGame(Socket socket) throws IOException {		
		// 데이터 입출력 스트림
		DataInputStream dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
		DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
		// 클라이언트의 입력 문자열, 서버의 입력 문자열
		String myStr, clientStr;
		
		// 이차원 배열 maps 생성 및 0으로 값 초기화
		int[][] maps = new int[3][3];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				maps[i][j] = 0;
			}
		}
		
		while (true) {
			// client로부터 문자열 읽기
			clientStr = dis.readUTF();
			//System.out.println("\t\t\t" + "client: " + clientStr);
			
//			// 클라이언트 입력 문자열로부터 파싱 : x,y 좌표 구하기
			Tic_common.markIt(clientStr, maps, 0);
			// 틱택토 게임판 출력
			Tic_common.showScreen(maps);
			
			// client에서 quitword 입력 시 강제종료
			if (clientStr.equals(Tic_common.quitWord))
				break;
			
			// 빙고 완성여부 체크
			if (Tic_common.isBingo(maps) == 1) {
				System.out.println("+++\n--- Try better. You (Server) Lose.");
				break;
			}
			// 무승부 체크
			if (Tic_common.isDraw(maps)) {
				System.out.println("+++\n--- TIE.");
				break;
			}
			
			// server 단 입력
			System.out.print("server(me)> ");
			myStr = Tic_common.readConsole();
			
			// server에서 quitword 입력 시 강제종료
			if (myStr.equals(Tic_common.quitWord))
				break;
			
			// 이미 마킹되있을 경우의 flow 처리
			while(Tic_common.isMarked(myStr, maps)) {
				System.out.println("Already marked in this coord. Type a proper one.");
				System.out.print("server(me)> ");
				myStr = Tic_common.readConsole();
				// 클라이언트 입력이 quitword 입력 시 강제종료
				if (myStr.equals(Tic_common.quitWord))
					break;
			}
			dos.writeUTF(myStr);
			dos.flush();
			// server 입력 문자열로부터 파싱 : x,y 좌표 구하기
			Tic_common.markIt(myStr, maps, 1);
			// 틱택토 게임판 출력
			Tic_common.showScreen(maps);
			// 빙고 완성여부 체크
			if (Tic_common.isBingo(maps) == 2) {
				System.out.println("+++\n--- Congratulation. You (Server) Win.");
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
