# Java-TicTacToe-Socket-Network
TicTacToe game using Java Socket network

# Java를 이용한 네트워크 소켓 스터디성 개인 프로젝트

## 프로젝트의 동기 및 개요

학부 교과과정에서 컴퓨터 네트워크, 데이터 통신 과목을 수강하며 네트워크와 통신과 관련한 이론을 배울 수 있었으나 실습이 없는 부분이 아쉽게 느껴졌습니다. 따라서 네트워크 강의 속에서 배운 내용을 기반으로 소켓 네트워크에 관련하여 간단한 스터디성으로 응용해보고 싶은 욕구로 시작하게 되었습니다.

본 프로젝트는 간단하게 자바 소켓을 이용한 Server-Client 구조에 뿌리를 두어 틱택토 게임을 1대1로 두는 형식입니다. 첫 번째 버전은 간단히 콘솔 창 입출력을 통해 구현한 틱택토 게임입니다. 간단하게 첫 번째 버전을 완성하고 코드를 복기하는 중에, GUI를 추가하여 사실감을 더하는 방향의 발전요소를 생각했습니다. 그리하여 Java의 Swing을 이용하여 GUI 를 적용한 두 번째 틱택토 게임도 만들어 보았습니다.


![Untitled](https://user-images.githubusercontent.com/103434648/228871050-9d66f843-7459-4249-9796-687ff8b5b3b4.png)
![Untitled (2)](https://user-images.githubusercontent.com/103434648/228871234-a791c585-6e49-40a0-8de3-c64a5a489923.png)


## 역경과 성장의 지점

- 학부 교과과정에서 이론으로만 배우던 TCP 통신을 실습해 보는 좋은 경험이었습니다. 소켓 프로그래밍에 대한 이해를 할 수 있었으나, 위의 실습들은 블로킹하는 방식이기 때문에, 이와 달리 논블로킹 소켓 방식과 웹 서버 네트워크까지도 심화하여 공부하고자 합니다.
- GUI를 이용한 버전에서 문자열 일치 판정에 대해 정수형 변수를 다루듯이 == 연산자로 비교하려는 오류를 범했습니다. 하지만 문자열 String에는 equals() 메소드를 이용해 이를 해결할 수 있었습니다.
- 하지만 위의 해결한 것도 좋았지만 View와 Data를 분리하여 다루는 기본원칙인 MVC 디자인 패턴을 이해하는 데 큰 도움이 되었습니다. View를 담당하는 부분과 Data를 처리하는 부분을 의존적인 것보다 분리시켜야 디버깅을 하는 데에 훨씬 유리합니다. 처음에는 View를 구성하는 GUI로부터 O, X 마크된 여부를 이용하는 코드를 짰지만, 앞서 언급한 부분을 유념하고 별도의 이차원 배열 자료구조로 데이터를 다루는 코드로 수정하였습니다.
