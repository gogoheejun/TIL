유튜브 [데어유프로그래밍](https://youtu.be/XBG6CUtVCIg) 강의를 보고 요약한 것입니다

---

스프링부트는 내장톰캣을 가지고 있다

# 소켓이란?

: 운영체제가 가지고 있는 것

예를 들어보자
![01](https://user-images.githubusercontent.com/78577071/125571654-5be7a489-7113-4441-84ed-7d146085df1e.png)

- 순서설명
1. B가 A한테 연결하고싶어. 그럼 A에서 5000소켓이 열려서 연결확인됨
2. 5000소켓은 연결확인만 하고  B랑 끊기고, 새로운 소켓 50001번이 B랑 통신을 함
3. 이때 또 C가 A랑 연결하고 싶으면 5000에서 연결확인을 하고 다시 끊고 5002번을 통해 C랑 통신을 함.

ㅇㅋ? 5000은 연결확인용이고, 이걸 A의 main스레드가 함. 글고 5001 5002 이렇게 새로운 소켓이 생길때마다 새로운 스레드가 생김

- 소켓통신 vs http통신:

소켓연결 대상이 오지게 많으면 부하가 커질 수 있음..그래서 http통신은 연결을 지속시키지 않고 연결을 끊어버리는 statelss방식을 씀

stateless란?

위 예로 예를 들어보면 B가 A한테 a.txt달라하면 A가 B한테 주고 바로 연결끊어버리는 것임.

→근데 소켓은 계속 연결되어있어서 상대 B가 누군지 계속 알 수 있음. 반면 http는 새로 연결할때마다 항상 새로운 애로 인식

*번외로,,,http는 사실 스위스 연구소에서 논문 방마다 다 찾기 싫어서 한곳에다가 모으려고 만든 거임. 그래서 문서 전달용이고, 그게 html문서임. 글고 한번 논문다운받으면 연결 끊어도되니까 소켓통신이 아닌것임.*

# 톰캣 vs 웹서버

http는 운영체제가 가지고 있는 소켓을 가지고 만들어짐(시스템콜: 시스템이 가지고 있는 기능을 가져와서 만든것)

- 웹서버

ex 상황) 내가 컴퓨터에 동영상 3개를 가지고 있고, 친구들이 동여상 갖고 싶어해.

친구들은 나에게 공유해달라고 request함. 

1. 이때, 내 컴터가 어딨는지 알아야하므로 ip주소 알아야 함.

2.내 동영상 중 정확히 뭘 원하는지 명시해야 함→ URL(자원을 요청하는 주소)

3. 난 친구한테 response해줌. 난가만히 있다가 친구가 나한테 request할 때 준 정보 토대로 영상 주면됨. 즉, 난 친구의 ip주소를 몰라도 돼. 어차피 요청할때 자기주소 밝히니까

위 예는 **http통신** 기반이야, 난 친구의 ip주소를 모르니까 먼저 연락을 못해 걍 기다리기만 해야 해.

먼저 연락하려면 계속 연결이 되어있어야 하는데 그게 바로 **소켓통신**임.

고로, 내가 response해주는 건 단순히 html문서나 특정 자원이고 모두 **static자원**이야. 정적이란 것임.  어떤 친구들이 요청하든지 그걸 원한다면 똑같이 주면 되는거야 안달라져.

- 웹서버(아차피) + **톰캣**

흔히 웹서버는 아파치라는 웹서버를 사용하는데, 아파치는 그냥 request로 어떤 자원달라하면 response로 그 자원주면 되는거임. 근데...! 만약 request가 .jsp파일이나 java코드로 요청하면 아파치는 그거 못알아들어.  

고로 아파치에다가 톰캣을 달아줌. 그러면 아파치가 이해못하는 걸 톰캣한테 넘기고 톰캣은 .jsp에 있는 자바파일을 컴파일하고 그 컴파일한 데이터를 .html로 만들어서 아파치한테 넘기고 아파치는 그 .html을 response로 넘김.

웹브라우저는 html, js, css, avi밖에 못알아먹어. 그래서 .jsp로 받으면 안되는데 톰캣이 jsp→html로 변환해준걸 아파치가 넘겨주니까 개꿀띠

- 서블릿 객체

html, css , png같은 정적인 파일달라는 request오면 톰캣은 일 안함. 아파치가 일 다함.

근데 java파일 요청하면 톰캣이 일해.

~~*잠깐만 토막상식: URL은 자원에 접근,,, URI는 식별자 통해 접근*~~

URL:  http://naver.com/a.png

URI:  http://naver.com/picture/a

(a는 파일이름이 아니잖오)

스프링은 URL접근방식 다 막아놓음. 그럼 URI로 해야 하는데...이게 무슨말이냐

**특정한 파일요청을 할수 없다는것. 즉 요청시에는 무조건 자바를 거쳐야 한다는것**

그럼 또 무조건 아파치는 톰캣한테 일을 넘기겠네!

이제 ㄹㅇ상황 예시)

1. 클라이언트가 java관련된 것들을 요청
2. 그럼 서블릿컨테이너(=톰캣)에서 스레드를 생성함. 
3. 이 스레드가 **서블릿객체**를 만듬

*만약 스레드 최대로 지정한 개수가 20개라면,* 

→요청이 20개면 서블릿 컨테이너 안에 스레드가 20개 생기고 서블릿객체도 20개가 생김. 스레드로 안하면 하나 객체가 DB접근하는데 3초걸리면 마지막에 온애든 60초 기다려야하잖어 그거 방지함.

그렇다고 무작정 스레드 많이 만들면 느려져서 안됨. 최적의 수를 최대 스레스 수를 지정해줘야 함.

위 상황서 추가 예시)

1. 20번째 꽉차고 21번째 요청이 왔어
2. 그래서 대기하고 있는데 20개 중 하나가 reponse완료했어. 그럼 http통신이니까 통신끊어짐(statelss)
3. 근데 그 스레드는 사라지지 않고 **재사용**됨. 왜냐면 대기하고 있는 21번째 요청을 위해서...만약 지우고 다시만들면 그런거때매 또 오래 걸릴 수 있음.

위 과정이 아래 그림으로 요약됨
![02](https://user-images.githubusercontent.com/78577071/125571681-25b7efa2-ad34-47ac-979a-c41f83f71a87.png)

최종적으로 만들어지는 건 톰캣이 만들어내는 HttpServiceRequest객체와 HttpServiceResponse객체임.

# **web.xml**

ex 상황) 성이 있고 문지기가 있어.  문지기한테 web.xml이란 문서를 던지고 이대로 일해라!라고 시킴. 그 지시내용은 다음과 같음

- ServleContext의 초기파라미터설정:

성으로 외부인이 들어올때 문지기가 암구호(=초기파라미터)를 알려줘. 그럼 성에서 돌아다니다가 암구호검사할 때 대답하면 통과. 성 외부에서 잠입한 애들은 통과 못하지. 초기파라미터는 한번설정하면 성 안에서 어디든지 적용됨.

- Session의 유요시간 설정

세션이란 인증을 통해 사람을 들여보낼때 하는것. 인증은 얼굴보고 하거나 주민등록증 보거나 하겠지. 암튼 문지기의 session이 3일로 설정되었다면, 들어오는 a는 성에서 3일동안 돌아다니다가 다시 문지기한테 와서 3일 갱신해야 함. 근데 몰래 들어온사람은 갱신못하겠지.

- Servlet/JSP 정의, Servlet/JSP 매핑

a란 사람이 성에 들어오는데 목적지가 '다'라는 곳이라고 적혀있어. web.xml에는 '다'는 서울 강동구로 가야한다고 적혀있으면 문지기는 그거 보고 거기로 가라고 알려줌. '다'=서울 강동구 이게  **서블릿정의**이고, 거기로 가면 된다고 알려주는게 **서블릿 매핑**임

- Mime type

a가 쌀을 가지고 성입구에 왔어. 그럼 문지기가 니 마임타입뭐냐?고 물어보면 a는 쌀이요!라고 말하는것임. 즉 마임타입을 알아야 문지기가 어디로 보낼지 아는것임. 
아무것도 안들고 온애들은 http의 get방식을 쓰고 이건 Select라고 함. 
반면 쌀이라는 데이터를 가져오면 문지기가 쌀창고로 보냄. 그럼 쌀창고에서 이 쌀이 그 성에서 먹을 수 있는 건지 판단하고 먹을 수 있도록 가공을 함.  만약 쌀을 물탱크로 보내는 순간 고장나는 것임.

- Welcom File list

원래 a가 어디로 갈지 목적지를 가져오든가 데이터를 가져와야 문지기가 어디로 가라 알려주는데, 아무것도 없이 딸랑딸랑오면, 광장으로 보내라고 되어있음.

- ErrorPage List

성에는 가,나,다밖에 없는데 a가 '라'로 온다네? 그럼 에러동네로 보내라고 되어있음

- 리스너/필터 설정

**필터**: a란 외부인이 B나라사람인데 A나라의 성으로 오려고 하는거면 못들어오고, 또 대마초는 A에서 금진데 대마초 가져오면 대마초 뺏고 들여보냄.

**리스너**: 성입구에 a,b,c,d사람들이 줄서있어. 문지기는 web.xml보면서 한명한명 검사하고있는데, 성의 귀족(관리자)이 술친구 구하고 싶다고 술 잘먹는애 뽑기 위해 **대리인(리스너)을 문지기 옆에 놓고** a,b,c,d 중에 술잘먹는애 먼저 뽑아감. 문지기가 **검사할필요없이 걍 데려감.**

- 보안

이상한 애들 쫓아내는거. 현상수배범 오면 쫓아내거나 감옥에 넣기

→즉, 웹서버에 진입을 하면 최초로 도는게 web.xml이다.

근데 여기서 문지기가 해야 할 일이 너무 많기에, FrontController패턴을 사용하여 일을 줄여준다

# DispatcherServlet

= FrontController패턴 + RequestDispatcher

- FrontController패턴

문지기가 Servlet/JSP 정의, Servlet/JSP 매핑 이거 할때, web.xml이 다 정의하기 힘들기에 필요한 클래스에 넘겨준다

다시한번 예시 ex)
![03](https://user-images.githubusercontent.com/78577071/125571721-d6f7c0ba-dbcc-4234-98f6-6d4d987c61fb.png)

순서)

1. request가 들어올 때 .do이란 특정주소로 오면 톰캣안에서 web.xml이 FrontController한테 넘기고, FrontController안에서 [a.do](http://a.do) [b.do](http://b.do) 라는 형식으로 저장
2. FC는 [a.do](http://a.do) b.do라는 자원을 찾기 위해 웹 안의 자원에 접근한다. 이것 또한 request임. 
3. 근데 FC가 재요청 할때 톰캣이 만든 request,response가 사라져버리는 문제가 발생.(http라 그런듯)
4. 그래서 기존에 FC에 도착한 request를 유지해줘야 할 필요성이 있는데 이걸 **RequestDispatcher**가 해줌

그.래.서

위 기능이 언제 많이쓰이냐: **데이터를 들고 다른페이지로 이동할때**

ex) 

1. 사용자가 a.jsp를 요청하면 서버는 a.html을 response해줌
2. 그럼 화면에 a.html이 보임
3. 버튼클릭해서 b.html로 넘어가려해
4. 그럼 b.jsp를 요청하고 서버는 b.html을 response해주지. 이 과정에서 기존의 request는 사라지겠지.
5. 어?근데 그럼 a서 사용했던거 b에서도 쓰고싶으면 어캄?

이렇게 a에서 쓰던 데이터를 b에서도 쓰고 싶을때 기존의 request를 유지시켜주는게 RequestDispatcher임.

근데 이런거 직접 구현할필요 노노, 스프링이 **DispatchServlet(** FrontController패턴 + RequestDispatcher**)** 을 제공

# ApplicationContext

스프링은 일단, 다 uri로 오기 때문에 request오면 걍 다 web.xml거치고 톰캣으로 간다 생각하면 된다. 글고 얘는 바로 위에서 한 **DispatchServlet** 을 거치지. DispatchServlet의 궁극적 목적은 결국 **주소분배**야. 이주소로 오면 여기로가고, 저 주소로오면 저기로 가! 이거임.

그.런.데! 

A클래스로 보낼라면 A클래스의 주소가 있어야 하잖아? 

→ 이 말은 주소로 분배하기 전에 자바의 src파일에 있는 자바파일들이 메모리에 있어야 된다는것..!

static클래스는 main메소드가 실행되기 전부터 메모리에 있어. 근데 걍 일반 자바클래스는 생성이 필요해. 자바파일이 모두 스태틱일 순 없잖아. 그니까 대부분 new를 해야 하는데..

스프링은 IoC니까, DispatchServlet가 **컴포넌트 스캔**을 통해 src파일에 있는 **모든** 애들을 메모리에 띄운다! 어디를? 특정 패키지 이하의 모~든 애들을.

*이때 어노테이션이 ㅈㄴ중요해짐.*

@Controller, @RestController, @Configration, @Repository, @Service , @Component란 주석이 있는애들을 찾아서 다 메모리에 띄우는거야.

**다시 결론, *DispatchServlet* 은 주소분배가 목적이지만, 그러기 위해 쫙 다 스캔해서 어노테이션을 보고 메모리에 띄운다.**

근데 DispatchServlet에 오기전에, 

request→web.xml→**Context Loader Listener** →Dispatch Servlet

Context Loader Listener 얘가 있네?

얘는 db에 관련된 거나 모든 스레드가 사용해도 되는 것들은 미리 메모리에 띄움. root_**ApplicationContext**파일을 읽으면 이 파일이 db나 공통적인 애들을 메모리에 띄워주는 것임

그럼 db관련객체가 먼저 생성되고 , 그담에 다른 객체들이 메모리에 뜨는 순서잖어?

그래서 db에선 그 객체들에 접근을 못하지만, 거꾸로는 가능해짐. 생성순서때문에!

그 다음 자세한 내용은 복붙. 출처:[https://getinthere.tistory.com/11](https://getinthere.tistory.com/11)

---

**ApplicationContext**

IoC란 제어의 역전을 의미한다. 개발자가 직접 new를 통해 객체를 생성하게 된다면 해당 객체를 가르키는 레퍼런스 변수를 관리하기 어렵다. 그래서 스프링이 직접 해당 객체를 관리한다. 이때 우리는 주소를 몰라도 된다. 왜냐하면 필요할 때 DI하면 되기 때문이다.

DI를 의존성 주입이라고 한다. 필요한 곳에서 ApplicationContext에 접근하여 필요한 객체를 가져올 수 있다. ApplicationContext는 싱글톤으로 관리되기 때문에 어디에서 접근하든 동일한 객체라는 것을 보장해준다.

ApplicationContext의 종류에는 두가지가 있는데 (root-applicationContext와 servlet-applicationContext) 이다.

**a. servlet-applicationContext(***웹과 관련된 애들만 띄우는 애들..*DispatcherServlet에 의해 실행됨**)**

servlet-applicationContext는 ViewResolver, Interceptor, MultipartResolver 객체를 생성하고 웹과 관련된 어노테이션 Controller, RestController를 스캔 한다.

============> 해당 파일은 DispatcherServlet에 의해 실행된다.

**b. root-applicationContext(**ContextLoaderListener에 의해 실행됨**)**

root-applicationContext는 해당 어노테이션을 제외한 어노테이션 Service, Repository등을 스캔하고 DB관련 객체를 생성한다. (스캔이란 : 메모리에 로딩한다는 뜻)

============> 해당 파일은 ContextLoaderListener에 의해 실행된다. ContextLoaderListener를 실행해주는 녀석은 web.xml이기 때문에 root-applicationContext는 servlet-applicationContext보다 먼저 로드 된다.

당연히 servlet-applicationContext에서는 root-applicationContext가 로드한 객체를 참조할 수 있지만 그 반대는 불가능하다. 생성 시점이 다르기 때문이다.

![04](https://user-images.githubusercontent.com/78577071/125571772-e58dd16d-920f-48e3-9edb-24a693020a25.png)

**Bean Factory**

필요한 객체를 Bean Factory에 등록할 수 도 있다. 여기에 등록하면 초기에 메모리에 로드되지 않고 필요할 때 getBean()이라는 메소드를 통하여 호출하여 메모리에 로드할 수 있다. [지금은 걍 @Bean이라고 어노테이션만 붙여주면 됨]

이것 또한 IoC이다. 그리고 필요할 때 DI하여 사용할 수 있다. ApplicationContext와 다른 점은 Bean Factory에 로드되는 객체들은 미리 로드되지 **않고** 필요할 때 호출하여 로드하기 때문에 lazy-loading이 된다는 점이다.  

# 요청 주소에 따른 적절한 컨트롤러 요청

요청오면, Handler Mapping이란 애가 적절한 컨트롤러의 함수를 찾아서 실행함.

그럼 이제 응답을 하겠지?

다시 예)

```python
A{
		String Hello(){
			return "hello";
	}
}
```

1. Dispatch servlet이 컴포넌트매핑을 통해 A란 클래스를 메모리에 띄움.
2. 그 담에 주소분배를 하기 위해 자기가 안하고 handler mapping한테 넘김
3. handler mapping이란 애가 적절한 함수를 찾음. 그게 저 Hello함수임. 글고 실행시켜
4. 그럼 실행되면 단순한 "hello"라는 메시지가 response되것지 이땐 MessageConverter가 작동함(json형식으로)
5. 근데 만약 리턴이 저런 헬로라는 메시지가 아니라 html파일을 리턴하고싶으면 ViewResolver가 관여해서 return "hello"앞뒤로 수정해서 주소/주소/주소/hello.jsp 이렇게 바꿔줌

여기서,

# 최종정리 그림

![05](https://user-images.githubusercontent.com/78577071/125571791-0532f939-de9d-4e9c-99ad-8eebf37e0a55.png)

1. 톰캣 실행시(서버컴터 켜질때...그니까 아무요청없는상황)
2. 성벽이 만들어지고 문지기가  web.xml읽으면서 아 할일 너무 많다~이러고 있어
3. 이때 contextLoaderListener가 로드되면서 applicationContext.xml이 읽어짐. 이 applicationContext.xml이 읽어질 때 roo-context.xml이 읽어지는데 
4. 이때 보통 DB관련 객체들을 컴포넌트스캔해서 메모리에 올림(ServiceImpl, DAO, VO 이런애들)
5. 이제 드디어 사용자한테 request요청이 들어옴
6. 요청이 들어오면 DispatcherServlet이 동작하는데
7.  앤 servlet-context.xml파일에 의해 읽힘. 글고 웹과 관련된 애들을 메모리에 띄움(Controller 같은애들)
8. DispatcherServlet이 막 주소분배를 하겠지 그럼 이제 response 해줘야 하는데 응답해주는 게 data로 할지 html로 할지에 따라 달라짐

→ 복잡하게 생각하지마 이런거보다 더 중요한건 비즈니스 로직이니깐
