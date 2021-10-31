[https://www.bigocheatsheet.com/](https://www.bigocheatsheet.com/)

# Array에서의 빅오

- array.push(17),

array의 마지막에 17을 넣는건데, 인덱스만 보고 걍 메모리에 바로 접근함. 앞에 인덱스 하나하나 보는게 아님

→O(1)

- array.pop()

얘도 마찬가지로

→O(1)

- array.shif()

얜 맨 앞에있는애를 삭제하고, 뒤에있는애들 하나씩 앞으로 끌어당겨야 하므로 다 조회함

→O(n)

- array.unshif(12)

→O(n)

- array.splice(1,0,'hi')

1번인덱스부터 0개를 삭제하고 거기다가 hi를 집어넣는건데, 중간에 집어넣으면 뒤에거 다 뒤로 미니까

→O(n)

결론: index로 접근할거면 array를 쓰는게 최고. 그러나 중간중간 삭제추가할거면 다른 자료구조를 선택해야함 

# 포인터

- 포인터 사용하지 않는 경우

```jsx
let num1 = 5;
let num2 = num1;
```

이 상태에서 num1 = 6; 으로 바꾸고

num2를 콘솔찍어보면 5로 나옴. 안 바뀐거야

let num2 = num1; 를 하는 순간 걍 num2에다가 5를 선언한거임

- 포인터 사용하는 경우

```jsx
let obj1 = {value:11}
let obj2 = obj1
```

위처럼 한 후에

```jsx
obj1.value = 22;로 하고

obj2콘솔찍으면 22로 바뀌어서 나옴
```

즉, obj1과 obj2는 같은 곳을 포인팅하고 있음

- 가비지콜렉션

만약 위 시나리오에서 덧붙여서 아래처럼 이동시키면

```jsx
obj3 = {value:55}
obj2 = obj3
obj1 = obj2
```

원래 있던 {value:22}는 아무도 포인팅하고 있지 않으므로 메모리낭비임 가비지컬렉션이 가져가서 없애버림
