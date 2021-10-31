# 기본클래스2(String,StringBuffer,Class클래스)

# String클래스

### 스트링 클래스를 선언하는 데에는 두가지 방법이 있는데 **전혀 성격이 다름**

```jsx
String str1 = new String("abc");
String str2 = "test";
```

- new를 하는 방식인 str1은 스트링클래스의 인스턴스가 생성되고 str1이 그걸 가리키는 것임. 일반 객체 생성했던 것과 같은 방식
- 그러나 두번째인 str2는 test라는 문자열이 상수풀에 저장되고 이 상수의 주소를 str2가 가리키는 것임.
  - 상수란? 10,a 이런 리터럴들. 얘들은 **상수 풀**에 저장되는데, 처음에 프로그램이 프로세스화되서 메모리에 로드될 때 자리를 잡음. 그곳이 바로 **데이터영역(==메소드영역==static영역==클래스영역)**이고, 여기엔 상수, static 변수가 자리잡음

→

```jsx
String str1 = new String("abc");
String str2 = new String("abc");
System.out.println(str1==str2); //false. ==은 주소를 비교

String str3 = "abc";
String str4 = "abc";
System.out.println(str1==str2);//true 똑같은 상수풀에 있는 상수를 가리니까.
```

잠만 JVM구조 정리:

![Untitled](https://user-images.githubusercontent.com/78577071/132889455-5c764dc6-d29c-4c3b-ad5e-d7668c6666e4.png)

![Untitled 1](https://user-images.githubusercontent.com/78577071/132889465-aa727f48-9fae-45c8-a79a-efc423f5d637.png)

### 한번 생성된 String값은 immutable함

String클래스 타고 올라가보면 final 배열(final byte[])로 되어있기에 한번 선언하면 못바꿈

concat메서드도 사실은 전혀 다른 주소에다가 스트링을 만들어놓는 것임

```jsx
String str1 = new String("java");
String str2 = new String("android");

System.out.println(System.identityHashCode(str1)) //메모리주소 알려줌

str1 = str1.concat(str2); 

System.out.println(System.identityHashCode(str1)) //전혀 다른 메모리주소값 나옴

```

그럼 원래 있던 str1, str2를 안쓰게 된다면 **garbage**가 계속 생김ㅠㅠ

### StringBuilder, StringBuffer

일반 String과 다르게 가변적인 char[]배열을 가짐. 고로 **garbage** 안생겨!

StringBuffer는 멀티쓰레드 프로그래밍에서 동기화를 보장해줌.  단일쓰레드에서는 걍 StringBuilder 사용을 권장됨.

사용법은 buffer.append()해서 이어쓰면 됨. 자세한 사용법은 검색 ㄱ

# Wrapper클래스, 오토박싱, 언박싱

wrapper클래스는 걍 int를 Integer이렇게 감싼걸얘기함

아래에서 원래 Integer는 객체이고 int는 4바이트 기본자료형이라 연산이 불가하기 때문에 아래처럼 오토박싱, 언박싱이 됨

```jsx
Integer num1 = new Integer(100);
int num2 = 200;
int sum = num1 + num2; //num1이 num.intValue()로 언박싱됨
Integer num3 = num2; //Integer.valueOf(num2)로 오토박싱됨
```

# Class 클래스

클래스이 이름이 Class인 클래스

보통 String으로 예를 들면 아래 스샷처럼 str이 String클래스인 걸 아니까 바로 메서드들 다 보여줌
![Untitled 2](https://user-images.githubusercontent.com/78577071/132889477-b64544f5-476b-4b40-86bd-00da518cdca5.png)


근디 이 로컬에 없는 클래스를 로딩하거나 갖다 쓸때는 저렇게 안됨. 못알아먹는 거임. 이런 때를 **리플렉션 프로그래밍**이라고 함. 지금은 많이 안 쓰이지만 그래도 가끔 쓰임 안드로이드에서 봤었음..

리플렉션 프로그래밍: Class클래스를 이용하여 클래스의 정보(생성자, 멤버변수, 메서드)를 가져오고 이를 활용해 인스턴스를 생성하고 메서드를 호출하는 방식. 로컬메모리에 객체가 없어서 객체의 타입을 직접 알 수 없는 경우(원격에 객체가 있는 경우) 객체정보만을 이용해 프로그래밍 할 수 있음

Class 클래스 가져오기

![Untitled 3](https://user-images.githubusercontent.com/78577071/132889490-74e74ddf-a3d9-40d7-a05e-7ee1a0db6a45.png)

```jsx
Person person = new Person();
1.
Class pClass1 = person.gertClass();
System.out.println(pClass.getName()); //패키지명.Person
2.
Class.pClass2 = person2.class()): //getClass랑 똑같
System.out.println(pClass.getName()); //패키지명.Person
3.
Class pClass3 = Class.forName("패키지명.Person");
System.out.println(pClass.getName()); //패키지명.Person
```

근데 1,2는 **스태틱로딩임**. 컴파일할때 이미 다 바인딩 되어있는거. 

그에 반해 3은 런타임때 즉 저 코드를 ㄹㅇ로 읽을 때 저 패키지의 클래스를 가져오는거임. **동적로딩이라고** 함. 이 forName() 많이 쓰임

동적로딩 언제쓰임? 어떤 클래스를 사용할지 모를때 변수로 처리하고 나중에 ㄹㅇ실행될때 그 변수에 대입된 클래스가 실행되도록 함. 예를 들면 여러 디비(mysql, oracle) 섞어 쓸때 그럼.
