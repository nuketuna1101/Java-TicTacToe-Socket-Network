package GUI_Tics;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GTicClient implements Runnable {
	static Socket socket;
	static DataInputStream dis;
	static DataOutputStream dos;

	// GUI 관련
	JPanel	jp;
	JLabel label;
	JButton[][] jbs= new JButton[3][3];
	// 틱택토 게임판 크기 설정
	int sizex = 50;
	int sizey = 50;
	// 턴제이므로 토큰 관리 :: 클라이언트 먼저 TRUE로 초기화.
	boolean token = true;
	
	// 이차원 배열 maps 생성 및 0으로 값 초기화
	int[][] maps = {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
	
	GTicClient(){
		// 틱택토 게임 GUI 생성
		
		// 프레임
		JFrame j = new JFrame();
		j.setTitle("TicTacToe Client");
		j.setSize(500, 400);
		j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	// [x] 버튼 누르면 종료하도록 설정
		
		// 9개 버튼의 이차원 배열로 틱택토 게임판
		for (int i = 0; i < 3; i++) {
			for (int k = 0; k < 3; k++) {
				// 9개 각각을 jbutton으로
				jbs[i][k] = new JButton();
				jbs[i][k].setBounds(50 + sizex * k, 50 + sizey * i, sizex, sizey);
				jbs[i][k].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						// 토큰 가지고 있을 시에만, 버튼 액션
						if (token)	markArea(e, jbs, maps);
					}
				});
				j.add(jbs[i][k]);
			}
		}
		
		// 시스템 메시지 출력창 
		jp = new JPanel();
		jp.setBounds(200, 50, 200, 200);
		jp.setBorder(null);
		label = new JLabel("<html>| Client : O | Server : X |<br>| Turn : Client(You) |</html>");
		
		// 각 요소를 프레임에 등록
		jp.add(label);
		j.add(jp);
		j.setLayout(null);
		j.setVisible(true);	
	}
	
	// 틱택토 게임판 버튼 클릭 이벤트
	public void markArea(ActionEvent e, JButton[][] jbs, int[][] maps) {
		// 눌려진 버튼의 좌표를 데이터로 넘겨주자.
		String str = null;
    	System.out.println("- button clicked.");
    	int BingoFlag = 0;
		// 버튼 클릭 시 클릭한 버튼 위치 정보를 문자열에 저장
		for (int i = 0; i < 3; i++) {
			for (int k = 0; k < 3; k++) {
				// 일치하는 버튼 객체
				if (e.getSource() == jbs[i][k]) {
					str = Integer.toString(i) + "," + Integer.toString(k);
					jbs[i][k].setText("O");
					jbs[i][k].setEnabled(false);
					maps[i][k] = 1;
				}
			}
		}
		// socket으로 전송
		try {
			dos.writeUTF(str);
			dos.flush();
		} catch(IOException e1) {}
		// 전송 성공했으면 토큰 회수
		token = false;
		label.setText("<html>| Client : O | Server : X |<br>| Turn : Server      |</html>");
		// 빙고 완성 여부와 무승부 체크
		BingoFlag = GTic_common.isBingo(maps);
		if (BingoFlag == 1) {
			label.setText("<html>| Client : O | Server : X |<br>|<br><br>|--- Game End. You Win.</html>");
			System.exit(0);
		}
		else if (BingoFlag == 2) {
			label.setText("<html>| Client : O | Server : X |<br>|<br><br>|--- Game End. You Lose.</html>");
			System.exit(0);
		}
		if (GTic_common.isDraw(maps)) {
			label.setText("<html>| Client : O | Server : X |<br>|<br><br>|--- TIE.</html>");
			System.exit(0);
		}
	}
	
	public void run() {
    	System.out.println("- Socket read thread is running...");
		String str = null;
		int BingoFlag = 0;
		// 무한 반복
		while (true) {
			// 메시지 수신
			try {
				str = dis.readUTF();
			} catch (IOException e) {
			}
			// 수신한 문자열을 통해 해당 좌표 버튼 처리
			// 클라이언트 입력 문자열로부터 파싱 : x,y 좌표 구하기
			int id_comma = str.indexOf(",");
			int idx = Integer.parseInt(str.substring(0, id_comma));
			int idy = Integer.parseInt(str.substring(id_comma + 1));
			jbs[idx][idy].setText("X");
			jbs[idx][idy].setEnabled(false);
			maps[idx][idy] = 2;

			// 메시지 수신 동시에 토큰 권한 부여
			token = true;
			label.setText("<html>| Client : O | Server : X |<br>| Turn : Client(You) |</html>");
			// 빙고 완성 여부와 무승부 체크
			BingoFlag = GTic_common.isBingo(maps);
			if (BingoFlag == 1) {
				label.setText("<html>| Client : O | Server : X |<br>|<br><br>|--- Game End. You Win.</html>");
				break;
			}
			else if (BingoFlag == 2) {
				label.setText("<html>| Client : O | Server : X |<br>|<br><br>|--- Game End. You Lose.</html>");
				break;
			}
			if (GTic_common.isDraw(maps)) {
				label.setText("<html>| Client : O | Server : X |<br>|<br><br>|--- TIE.</html>");
				break;
			}
		}
	}
	
	public static void main(String[] args) {
		GTicClient gtc = new GTicClient();
		System.out.println("<< TicTacToe Client >>");
		
		// 1단계) 소켓 만들고 서버에 접속하기
		try {
			socket = new Socket(InetAddress.getByName("localhost"), GTic_common.nPort);
			dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
		} catch (UnknownHostException e) {
		} catch (IOException e) {
			System.out.println(" - error: cannot connect server");
			System.out.println();
			System.exit(0);
		}

		System.out.println("*** server connected: " + socket.getInetAddress() + ":" + socket.getPort());

		// 메시지 수신 Thread 실행
		Thread t1 =new Thread(gtc);  
		t1.start();  

	}
	
}
