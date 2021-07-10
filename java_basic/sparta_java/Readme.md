### 퀴즈내용: 
객체지향에서 배운 개념과 문법을 이용해서 다음 요구조건을 만족하는 클래스를 작성하시요. 여러분이 게임을 만든다고 생각해보세요.

요구사항

1. 사람은 자식, 부모님, 조부모님이 있다.
2. 모든 사람은 이름, 나이, 현재 장소정보(x,y좌표)가 있다.
3. 모든 사람은 걸을 수 있다. 장소(x, y좌표)로 이동한다.
4. 자식과 부모님은 뛸 수 있다. 장소(x, y좌표)로 이동한다.
5. 조부모님의 기본속도는 1이다. 부모의 기본속도는 3, 자식의 기본속도는 5이다.
6. 뛸때는 속도가 기본속도대비 +2 빠르다.
7. 수영할때는 속도가 기본속도대비 +1 빠르다.
8. 자식만 수영을 할 수 있다. 장소(x, y좌표)로 이동한다.

위 요구사항을 만족하는 클래스들을 바탕으로, Main 함수를 다음 동작을 출력(`System.out.println`)하며 실행하도록 작성하시오. 이동하는 동작은 각자 순서가 맞아야 합니다.

1. 모든 종류의 사람의 인스턴스는 1개씩 생성한다.
2. 모든 사람의 처음 위치는 x,y 좌표가 (0,0)이다.
3. 모든 사람의 이름, 나이, 속도, 현재위치를 확인한다.
4. 걸을 수 있는 모든 사람이 (1, 1) 위치로 걷는다.
5. 뛸 수 있는 모든 사람은 (2,2) 위치로 뛰어간다.
6. 수영할 수 있는 모든 사람은 (3, -1)위치로 수영해서 간다.

### 이 문제에서 공부할 수 있는 것:
- 부모클래스 상속과 생성자 
- 인터페이스 구현과 오버라이드, 구현한 인터페이스의 추상메소드 사용 
- 추상메소드 구현하고 **그 안에서** 상속받은 부모클래스의 변수와 메소드를 사용하기(_자식클래스안에서 보이지도 않는 부모꺼를 조작하는것_)
얘는 인터페이스
```java
public interface Walkable {
    void walk(int x, int y);
}
```
얘는 부모클래스
```java
public class Human {
    String name;
    int age;
    int speed;
    int x, y;

    public Human(String name, int age, int speed, int x, int y) {
        this.name = name;
        this.age = age;
        this.speed = speed;
        this.x = x;
        this.y = y;
    }

    public Human(String name, int age, int speed) {
        this(name, age, speed, 0, 0);
    }

    public String getLocation() {
        return "(" + x + ", " + y + ")";
    }
    protected void printWhoAmI() {
        System.out.println("My name is " + name + ". " + age + " aged.");
    }
}
```
얘가 바로 위의 인터페이스와 부모클래스를 구현/상속한 자식클래스...
```java
public class GrandParent extends Human implements Walkable{

    public GrandParent(String name, int age) {
        super(name, age,1);
    }


    @Override
    public void walk(int x, int y) {
        printWhoAmI();
        System.out.println("walk speed: " + speed);
        this.x = x;
        this.y = y;
        System.out.println("Walked to " + getLocation());
    }
}

```
근데 사실, this.x =x에서 super.x라고 해야 하는데 자식클래스에 어차피 x가 따로 선언된 게 없으니 부모의 x가 건드려짐.

- 업캐스팅, 업캐스팅을 하는 이유- Human[] 이렇게 배열로 못 묶으면 for못써서 일일히 해야함..
```java
Human parent = new Parent("엄마", 50);
        Human child = new Child("나", 20);

        Human[] humans = { grandParent, parent, child };
        for (Human human : humans) {
            System.out.println(human.name + ", 나이: " + human.age + ", 속도: " + human.speed + ", 장소: " + human
                    .getLocation());
        }
```
- 인터페이스로도 타입캐스팅 가능하다.
```java
//Human[] humans = { grandParent, parent, child }; 각각 원소는 인터페이스 Walkable, Runnable, Swimmable 중 한개이상을 구현한 객체임
for (Human human : humans) {
            if (human instanceof Walkable) {
                ((Walkable) human).walk(1, 1);
                System.out.println(" - - - - - - ");
            }
  ```
