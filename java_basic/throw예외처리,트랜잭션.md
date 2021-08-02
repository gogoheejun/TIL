-참고자료: 점프 투 자바를 요약 한 것입니다.

# **07-4 예외처리 (Exception)**

## **예외는 언제 발생하는가?**

- 없는 파일을 열려고 시도해 보자.

```
BufferedReader br =new BufferedReader(new FileReader("나없는파일"));
br.readLine();
br.close();

```

위코드를 실행하면 다음과 같은 오류가 발생한다.

```
Exception in thread "main" java.io.FileNotFoundException: 나없는파일 (지정된 파일을 찾을 수 없습니다)
    at java.io.FileInputStream.open(Native Method)
    at java.io.FileInputStream.<init>(Unknown Source)
    at java.io.FileInputStream.<init>(Unknown Source)
    at java.io.FileReader.<init>(Unknown Source)
    ...

```

→ FileNotFoundException라는 이름의 예외가 발생

- 0으로 어떤 다른 숫자를 나누는 경우

```
int c = 4 / 0;
```

오류내용은 다음과 같다.

```
Exception in thread "main" java.lang.ArithmeticException: / by zero
    at Test.main(Test.java:14)
```

→ ArithmeticException라는 이름의 예외가발생

- 배열범위 벗어나는 에러

```
int[] a = {1, 2, 3};
System.out.println(a[3]);
```

오류내용은 다음과 같다.

```
Exception in thread "main" java.lang.ArrayIndexOutOfBoundsException: 3
    at Test.main(Test.java:17)
```

a는 {1, 2, 3}이란 배열인데 a[3]은 a 배열에서 구할 수 없는 값이기 때문에 ArrayIndexOutOfBoundsException가 나게 된다.

*자바는 이런 예외가 발생하면 프로그램을 중단하고 에러메시지를 보여준다.*

## **예외 처리하기**

- try, catch.. 기본임

```
try {
    ...
}catch(예외1) {
    ...
}catch(예외2) {
    ...
...
}

```

try문안의 수행할 문장들에서 예외가 발생하지 않는다면 catch문 다음의 문장들은 수행이 되지 않는다. 하지만 try문안의 문장들을 수행 중 해당예외가 발생하면 예외에 해당되는 catch문이 수행된다.

숫자를 0으로 나누었을 때 발생하는 예외를 처리하려면 다음과 같이 할 수 있다.

```
int c;
try {
    c = 4 / 0;
}catch(ArithmeticException e) {
    c = -1;
}

```

ArithmeticException 이 발생하면 c에 -1을 대입하도록 예외처리한 것이다.

### **finally**

프로그램 수행 도중 예외가 발생하면 프로그램이 중지되거나 예외처리를 했을 경우 catch구문이 실행된다. 하지만 어떤 예외가 발생하더라도 반드시 실행되어야 하는 부분이 있어야 한다면 어떻게 해야 할까?

```
publicclassTest {
publicvoid shouldBeRun() {
        System.out.println("ok thanks.");
    }

publicstaticvoid main(String[] args) {
        Test test =new Test();
int c;
try {
            c = 4 / 0;
        }catch (ArithmeticException e) {
            c = -1;
        }finally {
            test.shouldBeRun();
        }
    }
}

```

finally 구문은 try문장 수행 중 예외발생 여부에 상관없이 무조건 실행된다. 따라서 위 코드를 실행하면 test.shouldBeRun() 메소드가 수행되어 "ok, thanks"라는 문장이 출력될 것이다.

## **예외 발생시키기 (throw, throws)**

이번에는 예외를 작성해 보고 어떻게 활용할 수 있는가에 대해서 알아보자.

이전 챕터에서 보았던 다음 예제를 보자.

```
publicclassTest {
publicvoid sayNick(String nick) {
if("fool".equals(nick)) {
return;
        }
        System.out.println("당신의 별명은 "+nick+" 입니다.");
    }

publicstaticvoid main(String[] args) {
        Test test =new Test();
        test.sayNick("fool");
        test.sayNick("genious");
    }
}

```

sayNick 메소드는 fool이라는 문자열이 입력되면 return 으로 메소드를 종료 해 별명이 출력되지 못하도록 하고 있다.

### throw

단순히 별명을 출력하지 못하도록 하는 것이 아니라 적극적으로 예외를 발생시켜 보도록 하자.

다음과 같은 FoolException 클래스를 작성한다.

*FoolException.java*

```
publicclassFoolExceptionextendsRuntimeException {
}

```

그리고 다음과 같이 예제를 변경 해 보자.

```
publicclassTest {
publicvoid sayNick(String nick) {
if("fool".equals(nick)) {
throw new FoolException();
        }
        System.out.println("당신의 별명은 "+nick+" 입니다.");
    }

publicstaticvoid main(String[] args) {
        Test test =new Test();
        test.sayNick("fool");
        test.sayNick("genious");
    }
}

```

return 부분을 `throw new FoolException()` 이라는 문장으로 변경하였다.

이제 위 프로그램을 실행하면 "fool"이라는 입력값으로 sayNick 메소드 실행 시 다음과 같은 예외가 발생한다.

```
Exception in thread "main" FoolException
    at Test.sayNick(Test.java:7)
    at Test.main(Test.java:15)

```

return 으로 메소드를 빠져나오는 대신에 `throw new FoolException()`을 이용해 예외를 강제로 발생시켜 보았다.

FoolException 이 상속받은 클래스는 **RuntimeException**이다. **Exception**은 크게 두가지로 구분된다.

1. RuntimeException
2. Exception

RuntimeException은 실행 시 발생하는 예외이고 Exception은 컴파일 시 발생하는 예외이다. 즉, Exception은 프로그램 작성 시 이미 예측가능한 예외를 작성할 때 사용하고 RuntimeException은 발생 할수도 발생 안 할수도 있는 경우에 작성한다.

다른 말로 Exception을 Checked Exception, RuntimeException을 Unchecked Exception이라고도 한다.

### **예외 던지기 (throws)**

위 예제를 보면 sayNick 이라는 메서드에서 FoolException을 발생시키고 예외처리도 sayNick이라는 메서드에서 했는데 이렇게 하지 않고 sayNick을 호출한 곳에서 FoolException을 처리하도록 예외를 위로 던질 수 있는 방법이 있다.

다음의 예제를 보자.

```
publicvoid sayNick(String nick)throws FoolException {
if("fool".equals(nick)) {
thrownew FoolException();
    }
    System.out.println("당신의 별명은 "+nick+" 입니다.");
}

```

sayNick 메소드 뒷부분에 **throws** 라는 구문을 이용하여 FoolException을 위로 보낼 수가 있다. ("예외를 뒤로 미루기"라고도 한다.)

위와 같이 sayNick 메소드를 변경하면 main 메소드에서 컴파일 에러가 발생할 것이다. throws 구문 때문에 FoolException의 예외를 처리해야 하는 대상이 sayNick 메소드에서 main 메소드(sayNick 메소드를 호출하는 메소드)로 변경되었기 때문이다.

main메소드는 컴파일이 되기 위해서 다음과 같이 변경되어야 한다.

```
publicstaticvoid main(String[] args) {
    Test test =new Test();
try {
        test.sayNick("fool");
        test.sayNick("genious");
    }catch(FoolException e) {
        System.err.println("FoolException이 발생했습니다.");
    }
}

```

main 메소드에서 try... catch로 sayNick 메소드에 대한 FoolException 예외를 처리하였다.

자, 이제 한가지 고민이 남아있다. FoolException 처리를 sayNick 메소드에서 하는것이 좋을까? 아니면 main 메소드에서 하는것이 좋을까?

sayNick 메소드에서 처리하는 것과 main 메소드에서 처리하는 것에는 아주 큰 차이가 있다.

sayNick 메소드에서 예외를 처리하는 경우에는 다음의 두 문장이 모두 수행이된다.

```
test.sayNick("fool");
test.sayNick("genious");

```

물론 `test.sayNick("fool");` 문장 수행 시에는 FoolException이 발생하겠지만 그 다음 문장인 `test.sayNick("genious");` 역시 수행이 된다.

하지만 main 메소드에서 다음과 같이 예외 처리를 한 경우에는 두번 째 문장인 `test.sayNick("genious");`가 수행되지 않을 것이다. 이미 첫번 째 문장에서 예외가 발생하여 catch 문으로 빠져버리기 때문이다.

```
try {
    test.sayNick("fool");
    test.sayNick("genious");
}catch(FoolException e) {
    System.err.println("FoolException이 발생했습니다.");
}

```

프로그래밍 시 Exception을 처리하는 위치는 대단히 중요하다. 프로그램의 수행여부를 결정하기도 하고 트랜잭션 처리와도 밀접한 관계가 있기 때문이다.

### **트랜잭션 (Transaction)**

갑자기 "트랜잭션"이라는것이 나와서 뜬금없다고 생각할 수도 있겠지만 트랜잭션과 예외처리는 매우 밀접한 관련이 있다. 트랜잭션과 예외처리가 서로 어떤 관련이 있는지 알아보도록 하자.

트랜잭션은 하나의 작업 단위를 뜻한다.

쇼핑몰의 "상품발송"이라는 트랜잭션을 가정 해 보자.

"상품발송"이라는 트랜잭션에는 다음과 같은 작업들이 있을 수 있다.

- 포장
- 영수증발행
- 발송

이 3가지 일들 중 하나라도 실패하면 3가지 모두 취소하고 "상품발송"전 상태로 되돌리고 싶을 것이다. (모두 취소하지 않으면 데이터의 정합성이 크게 흔들리게 된다. 이렇게 모두 취소하는 행위를 보통 전문용어로 롤백(Rollback)이라고 말한다.)

프로그램이 다음과 같이 작성되어 있다고 가정 해 보자. (※ 아래는 실제 코드가 아니라 어떻게 동작하는지를 간략하게 표현한 pseudo 코드[1](https://wikidocs.net/229#fn:pseudo)이다.)

```
상품발송() {
    포장();
    영수증발행();
    발송();
}

포장() {
   ...
}

영수증발행() {
   ...
}

발송() {
   ...
}

```

쇼핑몰 운영자는 포장, 영수증발행, 발송이라는 세가지 중 1가지라도 실패하면 모두 취소하고 싶어한다. 이런경우 어떻게 예외처리를 하는 것이 좋겠는가? ^^

다음과 같이 포장, 영수증발행, 발송 메서드에서는 예외를 throw하고 상품발송 메서드에서 throw된 예외를 처리하여 모두 취소하는 것이 완벽한 트랜잭션 처리 방법이다.

```
상품발송() {
try {
        포장();
        영수증발행();
        발송();
    }catch(예외) {
       모두취소();
    }
}

포장()throws 예외 {
   ...
}

영수증발행()throws 예외 {
   ...
}

발송()throws 예외 {
   ...
}

```

위와 같이 코드를 작성하면 포장, 영수증발행, 발송이라는 세개의 단위작업 중 하나라도 실패할 경우 "예외"가 발생되어 상품발송이 모두 취소 될 것이다.

만약 위 처럼 "상품발송" 메서드가 아닌 포장, 영수증발행, 발송메소드에 각각 예외처리가 되어 있다고 가정 해 보자.

```
상품발송() {
    포장();
    영수증발행();
    발송();
}

포장(){
try {
       ...
    }catch(예외) {
       포장취소();
    }
}

영수증발행() {
try {
       ...
    }catch(예외) {
       영수증발행취소();
    }
}

발송() {
try {
       ...
    }catch(예외) {
       발송취소();
    }
}

```

이렇게 각각의 메소드에 예외가 처리되어 있다면 포장은 되었는데 발송은 안되고 포장도 안되었는데 발송이 되고 이런 뒤죽박죽의 상황이 연출될 것이다.

실제 프로젝트에서도 두번째 경우처럼 트랜잭션관리를 잘못하여 고생하는 경우를 많이 보았는데 이것은 일종의 재앙에 가깝다.

이번 챕터에서는 자바의 예외처리에 대해서 알아보았다. 사실 예외처리는 자바에서 좀 난이도가 있는 부분에 속한다.

보통 프로그래머의 실력을 평가 할 때 이 예외처리를 어떻게 하고 있는지를 보면 그 사람의 실력을 어느정도 가늠해 볼 수 있다고들 말한다. 예외처리는 부분만 알아서는 안되고 전체를 관통하여 모두 알아야만 정확히 할 수 있기 때문이다.
