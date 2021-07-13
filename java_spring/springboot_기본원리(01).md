# 스프링 특징

- 오픈소스

내부 뜯어고칠 수 있다....언젠간?ㅋㅋ

- IOC(Inversion of Controll) : 제어의 역전

오브젝트는 원래 직접 s  = new하면 heap메모리에 생기지. 그럼 s 참조변수는 그 메모리 주소를 가리키겠지.

```java
public void make(){
	의자 s = new 의자();
}

public void use(){
	의자 s = new 의자():
}
```

위 두개 메소드의 s는 서로 다른 메모리 주소를 가리키지. 왜냐면 s는 자기가 속한 메소드가 실행될때에만 메모리에 떠있으므로.

근데 스프링이이 ㅈㄴ 많은 오브젝트들을 쫙 스캔해서 heap에다가 쫘르륵 다 띄워서 다 쓸수 있게 해줌. 이게 **IOC**임. 스프링이 이 객체들을 관리한다는 것임

→원하는 모든 클래스의 메소드에서 저 객체들 다 사용가능.

→ 즉 싱그론 객체임

- DI를 지원한다(dependency injection)

heap에 띄워진 객체들을 가져다가 쓰는게 di임ㅋㅋ

- 엄청나게 많은 필터를 가지고 있다

성(톰캣) 안에 왕궁(스프링 컨테이너)이 있어. 성입구에 필터(=web.xml)있고 왕궁앞에 필터(=인터셉터(AOP))있지.

- 엄청나게 많은 어노테이션이 있다(리플렉션, 컴파일체킹)

주석은 컴파일때 무시되지만, 어노테이션은 컴파일러가 무시하지 않음.  

스프링은 어노테이션을 통해 객체를 생성함.

예시로,)

@Component가 붙어있으면 해당 클래스를 메모리에 로딩

@Autowired가 붙어있으면 로딩된 객체를 해당 변수에 집어 넣어

```java
@Component
Class A {
	
}
//→ new안해도, 어차피 IOC되니까 스프링은 위에 어노테이션 보고서 스캔한담에 heap에다가 로딩함.
===================
Class B{
	@Autowired
	A a;
}
//A a = new A()이거 안해도 알아서 a가 A객체랑 연결됨
```

위의 클래스 B를 스캔한다면, 스캔할 때 B클래스안에 어떤애들이 있는지 분석하는걸 **리플렉션**이라고 한다. 분석하면서 메소드, 필드, 어노테이션들에게 무엇을 하라고 지시도 가능. 어노테이션보고 오토와이어드니까 heap에 떠있는 객체들 쭉 읽어들이면서 같은 타입을 이어주는것임. 이때 이렇게 이어주는 걸 **DI**라고 함

- 스프링은 MessageConvert를 가지고 있다

request, response시에 오브젝트를 json으로 바꿔주는 애

- BufferdReader와 BufferdWirter를 쉽게 사용 가능하다

요샌 대부분 utf-8쓰니까 3바이트 통신임

통신할때 선이 있다 치면 그걸 Byte Stream이라 하고 얜 1바이트로 보낸다는것.. 자바는 얘를 InputStream(바이트)나 InputStreamReader(문자하나)로 받는데 여러개 문자 받을라면 배열로 받아야하는데 이러면 크기가 정해져있음...내가 어떤 문자를 받을 줄 알고 미리 정함?

고로 위에꺼 다 안쓰고 **BufferedReader**로 데이터 받음(쓸땐 BufferedWriter or Printwriter)

→ 가변길이의 문자 받을 수 있음

스프링은 근데 이런 클래스를 직접 쓸필요없이 그냥 어노테이션으로 @RequestBody나  @ResponseBody 요렇게 해주면 끝임.

# JPA 개념,특징

- jpa는 Java Persistance API이다

램에다가 저장하면 끄면 다 꺼지니까 하드디스크에다가 저장해야 하는데, 

자바는 하드디스크의 특정영역인 DBMS에다가 기록함. JPA에서의 persistance api라는 건 자바가 dbms에다가 기록할 수 있는 환경을 제공하는 api이다.

- ORM기술이다

Object Relational Mapping, 내 하인이라고 생각하면 편하다

Team이란 table이 있다 치자. 

ID : inst
Name: varchar
Year: varchar

요롷게 되어있고, DB에 있는 데이터를 자바에다가 모델링하면

```java
Class Team{
	int id;
	String name;
	String year;
}
```

보통 테이블을 보고→ 데이터 모델링을 하는게 원래 업무방식이야

그.러.나 **ORM은 달라. 거꾸로야**

JPA의 인터페이스 규칙을 지키면 클래스만들면 알아서 db에 테이블이 생긴다

- 반복적인 CRUD작업 생략하게 해줌

원랜 자바가 db에 세션오픈해달라 한담에 오픈되면 쿼리를 전송함. 그럼 디비는 쿼리대로 data찾아서 응답함. 근데 자료구조가 서로 다르니까 그 데이터를 자바object로 바꿔줘야 하는데 이거 ~~개노가다임.~~ 응답해주면 다시 자바랑 디비연결 끊어줘야힘

*근데 이런 일련의 과정들을 jpa의  ORM이 알아서 해준다. 그니까 내 하인임.*

- 영속성 Context를 가지고 있다

컨텍스트는 대상의 모~든 정보를 가지고 있어. 근데 이런 애를 영구적으로 가지고 있다?

**자바 - 영속성컨텍스트 - DB** 이렇게 자바와 db사이에 context가 있음(자바는 crud요청을 컨텍스트에다가 하는것임)

자바에서 데이터 db에 넣으려고 쏘면→ 컨텍스트에서 받아서 자기한테 저장하고→ 디비에 넣어서 디비에서 저장됨. 삭제도 똑같이됨. 컨텍스트 안에 있는 데이터와 db안에 있는  데이터는 동기화가 되는것임.

그러나, **타입은 달라,** 자바에서 fruit데이터를 select하면 db에 있는 건 db 형식에 맞는 타입이지만 컨텍스트가 db에서 가져온 fruit데이터는 자바 object로 저장되고 이걸 자바한테 던져주는것임.

근데 여기서 자바가 fruit을 animal로 바꾸고 커밋했어. 그럼 영속성컨텍스트에서 자동으로 update문을 호출해서 db에다가 업데이트 시킴. 

- DB와 OOP의 불일치성을 해결해주는 방법론 제공

Player와  team table이 있어.

ID Name teamId

1   박찬호    1

2   류현진    1

ID Name

1 한화

2 sk

얘들을 자바에서 모델링하면

```java
Class Team{
	int id;
	String name;
	String year;
}

Class Player{
	int id;
	String name;
	int teamId;
}
```

요렇게 되는데 웹페이지에다가 선수 팀이 어딘지 보여주려면 셀렉트 두번하든가 조인을 해야함. 힘들잖어. 

다음처럼 하면 쉽겠지. **진정한 OOP임**

```java
Class Player{
	int id;
	String name;
	Team team;
}
```

이렇게 하는게 **ORM**이야. 자바가 주도권을 쥐는 모델링방식.

위 클래스 이용해서 데이터 넣거나 할 때 jpa가 알아서  foreking key씌워서 래핑해줌

- OOP관점에서의 모델링(composition)을 할 수 있게 해줌

![02](https://user-images.githubusercontent.com/78577071/125445501-63738519-4ef6-4776-b6ce-07a8e6e35083.png)

car 모델로 데이터에다 넣으면 car테이블과 engine테이블이 자동으로 생성된다!

또한 다른 클래스를 상속한다면 상속한것도 아예 같이 테이블에 붙여줌

Car클래스가 EntityDate이란 날짜알려주는 애를 상속한다면 테이블에도 붙여줌

![03](https://user-images.githubusercontent.com/78577071/125445541-8fdd2938-ed52-4049-a148-eb0e7a565436.png)

- 방언처리가 용이하여 Migration하기 좋음. 유지보수에도 좋음

jpa는 추상화객체를 가지고  있는데 얘가 오라클, 마리아db, mysql등등으로 다 바뀌게 되서 편함. 중간에 바꿔도 알아서 됨
