https://www.youtube.com/watch?v=NyhbNtOq0Bc     
https://www.youtube.com/watch?v=Ppqc3qN75EE   
우테코 강의 보고 씀

# WAS

기존엔 그냥 Web Server였음.

이건 static한 파일만 클라이언트에 보냈음. 그냥 html만 띡하고 보낸것임

html은 프로그래밍 언어가 아니야. 걍 보여주는 문서일 뿐.

# Web Application Server

의 등장. WAS가 나온것임.

- php, jsp와 같은 언어를 사용해서 동적인 페이지를 생성할 수 있는 서버임
- 프로그램 실행 환경과 데이터베이스 접속 기능 제공
- 웹 어플리케이션 컨테이너: 웹 어플리케이션이 배포되는 공간. (여기서 컨테이너란 jps, servlet을 실행시킬 수 있는 소프트웨어를 말함)
![Untitled](https://user-images.githubusercontent.com/78577071/135027971-10d45e89-e0a4-45a4-becf-90b0cc309654.png)

![Untitled2](https://user-images.githubusercontent.com/78577071/135027976-797b4d94-ef36-48fa-a84d-78e19d5c9b7a.png)


즉, web server와 was는 "상황에 따라 변하는 정보를 제공할 수 있는가"라는 차이로 갈림

---

# JDBC
![Untitled3](https://user-images.githubusercontent.com/78577071/135027990-54dfe721-23e0-4ab5-9435-5675aa37bc82.png)


그럼 jpa는 무슨 기능을 하는가?
![Untitled4](https://user-images.githubusercontent.com/78577071/135027996-031aeea0-a69e-4625-a904-a44456b1272b.png)

### jpa장점

1.  sql문을 직접 java application 내에서 적을 경우가 적어진다
2. sql구조를 java application 내에서 적용하지 않아도 된다
