> https://www.youtube.com/watch?v=Tq3W8UyltFs&list=PLy-g2fnSzUTAaDcLW7hpq0e8Jlt7Zfgd6 영상을 보고 

# 1강 컴퓨터 구조와 원리

몰라도 프로그래밍 할 수 있지만, 알면 더 좋다. 솔직히 그냥 알쓸신잡.

## 트랜지스터란?

—NPN— 이렇게 되어있고,  P에 전압이 걸리냐 안걸리냐로 전체 회로에 전기가 흐르냐 안흐르냐가 결정됨

우리 몸은 수천억개의 탄소로 이뤄져있지. 컴퓨터는 수억개의 트랜지스터로 이뤄진거임.

***cpu = 트랜지스터 덩어리***

- 트랜지스터의 기능:

1. 스위치: 이게 중요..
2. 증폭 :이건 컴터에서 별로안중요

- 스위치 기능이 왜 중요한가

흔히 스위치는 물리적인 힘으로 버튼을 누르지만, 트랜지스터는 전류로 작동. 즉 전기만으로 스위치를 껐다 켰다하는게 가능해짐 트랜지스터덕분에.

- 2진법 예시

2진법으로 1011 = 11

1 * 2^3 + 0*2^2 + 1*2^1 + 1*2^0 

컴터에서는 1011 중 자리수 하나를 **1bit**라고 하고 1개의 트랜지스터가 필요함. 

이거 8개 모인것을 **1byte**라고함 이건 8개의 트랜지스터가 있는거지

이게 1024개면 1**키로바이트**...

# 2강 계산기원리

- AND

  0 0 0
  1 0 0
  0 1 0
  1 1 1 

- OR

  0 0 0 
  0 1 1
  1 0 1
  1 1 1

- XOR

  0 0 0
  1 0 1
  0 1 1
  1 1 0

- NOT

  0 1
  1 0

*이걸 트랜지스터로 만드는 것.*

```jsx
근데 이거 알라면 회로에 대한 걸 알아야 함.

- Ohm's law라고 들어봤능가

V = IR

I = V/R

즉, *전류는 전압에 비례하고, 저항에 반비례한다*

도체: 구리, 철, 금, 물 → 저항 거의 0
부도체: 돌, 세라믹 → 저항 거의 무한
반도체: 도체가 됐다가 부도체가 되는것. 즉 트랜지스터임.
```

다시 회로로 오면,

일단 다음속성을 염두해두고

> 전류는 저항이 없는 곳이 있다면 그곳으로만 흐른다

- NOT

그림 넣기 귀찮으니 말로 해보자면,
input이 0이면 트랜지스터가 막혀서 전류가 다 output으로만 흘러서 output이 1이됨

input이 1이면 트랜지스터가 열리는데, 그럼 Ground로 가는 길이 열리게 됨. 그라운드는 저항이 0이라서 모든 전류가 그라운드로 가고, 고로 output으로 가는애들은 없게 되어 output은 0이 됨.(아웃풋은 조금이라도 저항이 있어서ㅠㅠ)

- AND

얜 트랜지스터 두개로 만듦. 인풋도 두개.. 
트랜지스터 두개가 회로 하나 위에 있는 구조임. 두개다 길이 열려야 output으로 가겠지 전류가.

- OR랑 xor는 알아서 구글 고고

***그럼 논리소자가 계산기가 어케 되는가***

- 1bit가산기: 비트 하나씩 더하는거. 예시를 보면(2진법)

0 + 0 = 00
1 + 0 = 01
0 + 1 = 01
1 + 1 = 10

이렇게 되는데 2^0 자리는 XOR로 만들수있고, 2^1자리는 AND네?
그럼 xor랑 and 가산기 하나씩으로 1비트 가산기를 만들수 있게 된것임

위 원리로 2비트 3비트 4비트....64비트 가산기를 만들수있음...이런식으로 가산기,감산기,곱셈,나눗셈 등으로 계산기를 만들수있음

# 3강 논리소자가 컴퓨터가 되는 역사..

튜링 기계. 폰노이만 기계