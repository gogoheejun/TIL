내가 확실히 아는 건 패스하고, 헷갈리거나 나중에 까먹을 것 같은 것만 적었음

# 1. 인터넷 네트워크

- 키워드:
    - IP
    - TCP, UDP
    - PORT
    - DNS

### IP란? 인터넷 프로토콜

- 역할: 지정한 ip address로 데이터 전달, 패킷이라는 통신단위로 데이터전달
- 패킷이란? 전송데이터에다가 출발지IP, 목적지 IP 등 기타 정보를 덧붙인것
- IP프로토콜의 한계:
    - 비연결성

        패킷을 받을 대상이 없거나 서비스 불능상태여도 패킷이 전송되버림. 상대가 없는데 보내버리면 어캄

    - 비신뢰성

        중간에 패킷 사라지거나, 순서대로 서버에 도착하지 않을 수 있어

    - 프로그램 구분

        같은 IP여도 서버에 여러개의 어플리케이션이 있을 수 있어. 이러면 패킷에 목적지IP가지고 구분할수 없게 되지

## TCP

IP프로토콜의 한계를 극복하기 위해, 출발지 PORT, 목적지 PORT, 전송제어, 순서, 검증 정보 를 더한 것임.

![Untitled](https://user-images.githubusercontent.com/78577071/131545548-7a4fe2fc-0576-4d0b-ab6c-2091bd7be363.png)


- 3way handshake

    순서대로

    1. 클라이언트가 서버에 SYN(접속요청)을 보냄
    2. 서버는 요청받으면 클라이언트에 SYN(다시한번요청) + ACK(요청수락)를 보냄.
    3. 클라이언트가 서버에 다시 ACK를 보냄. 즉, 서로 왓다갓다 확인하는것임.
    4. 서버가 그 ACK를 받으면 그제서야 데이터를 전송함

→ 데이터전달보증, 패킷 전달 순서보장됨

## UDP

얜 하얀 도화지에 비유됨. 

연결지향(3way handshake), 데이터 전달보증, 순서보장 다 X

그러나 단순하고 빠름.

즉, IP + PORT + 체크섬 라고 이해

# 2.URI와 웹 브라우저 요청흐름

## URI

는 걍 URL, URN을 모두 합쳐서 지칭하는데, URN은 거의 안쓰므로 걍 URL이라고 생각하면 됨

# 3.http

- TCP: HTTP/1.1, HTTP
- UDP: HTTP

현재는 HTTP/1.1 주로 사용

- 무상태 프로토콜(Statelss)

서버가 클라이언트 상태 바로 까먹는것

장점: 

- **서버확장성** 높음

    중간에 서버가 바뀌어도 된다는 것임. 즉 클라이언트 요청이 갑자기 증가해도 서버를 대거 투입가능함

- 아무서버나 호출해도 됨
- 중간에 서버 장애나도 다른 서버에서 응답 가능

단점: 

- 클라이언트 추가 데이터전송
- 로그인한 경우엔 로그인 했다는 상태를 유지해야 할 필요가 있음

    → 브라우저의 쿠키나 세션등을 사용

상태유지는 최소한으로만 사용해야함.

# 4. HTTP 메서드

URI 설계 시 가장 중요한 것은 **리소스 식별**임

리소스란? "미네랄을 캐라"라는 리퀘스트 중 미네랄이 바로 리소스임

→즉 리소스와 행위를 분리해야 함.

URI는 리소스만 식별하고, 행위는 메서드를 스면 돼.

회원가입: 회원= 리소스, 가입=메서드

회원탈퇴: 회원= 리소스, 탈퇴=메서드

- 주요메서드

    GET, POST(신규등록), PUT(덮어쓰기), PATCH(일부만 수정), DELETE. 알지?

- 포스트와 풋 구별이 헷갈림
    - POST
        1. 새 리소스 생성(등록)
        2. 요청 데이터 처리
            1. 결제완료→배달시작→배달완료 처럼 프로세스의 상태가 변경되는 경우
            2. 꼭 새로운 리소스가 생성되는 게 아닐 수도 있음
        3. 애매하면 걍 POST쓰면 됨
    - POST
        1. 리소스를 대체(있으면 대체, 없으면 생성)
        2. **클라이언트가 리소스의 위치를 아는 상태로 URI를 직접 지정함.(POST와의 차이점)**

## HTTP메서드의 속성

- 멱등메서드
    - GET,PUT,DELETE
    - POST는 멱등이 아님. 두번 호출하면 결제가 중복해서 발생됨.

    →서버가 TIMEOUT등으로 정상응답을 못 주었을때, 클라이언트가 같은요청을 다시해도 되는지 판단근거

- 캐시가능한 메서드

    GET,HEAD정도만 캐시로 사용한다고 생각하삼. 

    POST, PATCH이런건 내용까지 캐시 키로 고려해야하는데 힘듦.

# 5. HTTP 메서드 활용

## 클라이언트에서 서버로 데이터 전송 4가지 상황

- 정적 데이터 조회
    - 이미지, 정적 텍스트 문서
- 동적 데이터 조회
    - 주로 검색, 게시판 목록에서 정렬 필터(검색어)
- HTML Form 을 통한 데이터 전송
    - 회원가입, 상품주문, 데이터변경
- HTTP API를 통한 데이터 전송
    - 회원가입, 상품주문, 데이터 변경
    - 서버 to 서버, 앱 클라이언트, 웹 클라이언트( ajax)

## 정적 데이터 조회

```jsx
GET /static/star.jpg HTTP/1.1
Host: localhost:8080
```

정적 데이터는 일반적으로 쿼리 파라미터 없이 리소스 경로로 단순하게 조회 가능

## 동적 데이터 조회

[https://www.google.com/search?q=hello&hl=ko](https://www.google.com/search?q=hello&hl=ko) 로 검색하면

```jsx
GET /search?q=hello&hl=ko HTTP/1.1
Host: www.google.com
```

라는 http메시지 작성됨

GET은 쿼리 파라미터 사용해서 데이터를 전달

## HTML Form 데이터 전송

```jsx
<form action="/save" method="post">
 <input type="text" name="username" />
 <input type="text" name="age" />
 <button type="submit">전송</button>
</form>
```

위처럼 된 form에 kim, 20을 입력하면

```jsx
POST /save HTTP/1.1
Host: localhost:8080
Content-Type: application/x-www-form-urlencoded

username=kim&age=20
```

위와 같은 http 메시지가 생성되서 요청됨

GET으로 한다면, 조회에서만 사용해야 함.

```jsx
GET /members?username=kim&age=20 HTTP/1.1
Host: localhost:8080
```

- multipart/form-data

```jsx
<form action="/save" method="post" enctype="multipart/form-data">
 <input type="text" name="username" />
 <input type="text" name="age" />
 <input type="file" name="file1" />
 <button type="submit">전송</button>
</form>
```

```jsx
POST /save HTTP/1.1
Host: localhost:8080
Content-Type: multipart/form-data; boundary=-----XXX 
Content-Length: 10457
------XXX
Content-Disposition: form-data; name="username"
kim
------XXX
Content-Disposition: form-data; name="age"
20
------XXX
Content-Disposition: form-data; name="file1"; filename="intro.png"
Content-Type: image/png
109238a9o0p3eqwokjasd09ou3oirjwoe9u34ouief...
------XXX--
```

정리하면

- HTML Form submit시 POST 전송

    예) 회원 가입, 상품 주문, 데이터 변경

- Content-Type: application/x-www-form-urlencoded 사용

    form의 내용을 메시지 바디를 통해서 전송(key=value, 쿼리 파라미터 형식)

    전송 데이터를 url encoding 처리

    예) abc김 -> abc%EA%B9%80

- HTML Form은 GET 전송도 가능
- Content-Type: multipart/form-data

    파일 업로드 같은 바이너리 데이터 전송시 사용

    다른 종류의 여러 파일과 폼의 내용 함께 전송 가능(그래서 이름이 multipart)

- 참고: HTML Form 전송은 GET, POST만 지원

## HTTP API 데이터전송

```jsx
POST /members HTTP/1.1
Content-Type: application/json
{
 "username": "young",
 "age": 20
}
```

- 풋과 포스트 다시한번 비교

## post

회원 관리 시스템 예시임
POST - 신규 자원 등록 특징
• 클라이언트는 등록될 리소스의 URI를 모른다.
• 회원 등록 /members -> POST
• POST /members
• 서버가 새로 등록된 리소스 URI를 생성해준다.
• HTTP/1.1 201 Created
Location: /members/100 
• 컬렉션(Collection)
• 서버가 관리하는 리소스 디렉토리
• 서버가 리소스의 URI를 생성하고 관리
• **여기서 컬렉션은 /members**

# put

파일 관리 시스템
API 설계 - PUT 기반 등록
• 파일 목록 /files -> GET
• 파일 조회 /files/{filename} -> GET
• 파일 등록 /files/{filename} -> PUT
• 파일 삭제 /files/{filename} -> DELETE 
• 파일 대량 등록 /files -> POST

여기서는 왜 put을 썼나? 파일이름은 클라이언트만 아니까

즉 다음과 같은 이유일 때 put으로 신규자원을 등록함

PUT - 신규 자원 등록 특징
• **클라이언트가 리소스 URI를 알고 있어야 한다.**

• 파일 등록 /files/{filename} -> PUT

• PUT /files/star.jpg

• 클라이언트가 직접 리소스의 URI를 지정한다.

• **스토어(Store)**

• 클라이언트가 관리하는 리소스 저장소
• 클라이언트가 리소스의 URI를 알고 관리
• 여기서 스토어는 /files

즉 포스트로 등록한다는건 걍 서버야 나 이거 등록해줘~ 이러면 서버가 리소스uri 알아서 만들어주는거. 풋은 클라이언트가 직접 uri에다가 등록시키면 서버는 그대로 해줌

대부분 포스트로 함. 아아주 가끔만 풋으로 함

## 컨트롤 URI

• GET, POST만 지원하므로 제약이 있음
• 이런 제약을 해결하기 위해 동사로 된 리소스 경로 사용
• POST의 /new, /edit, /delete가 컨트롤 URI
• HTTP 메서드로 해결하기 애매한 경우 사용(HTTP API 포함)

## URI 설계시 팁: 개념들

- 문서(document)

• 단일 개념(파일 하나, 객체 인스턴스, 데이터베이스 row)
• 예) /members/100, /files/star.jpg

- 컬렉션(collection)

• 서버가 관리하는 리소스 디렉터리
• 서버가 리소스의 URI를 생성하고 관리
• 예) /members

- 스토어(store)

• 클라이언트가 관리하는 자원 저장소
• 클라이언트가 리소스의 URI를 알고 관리
• 예) /files

- 컨트롤러(controller), 컨트롤 URI

• 문서, 컬렉션, 스토어로 해결하기 어려운 추가 프로세스 실행
• 동사를 직접 사용
• 예) /members/{id}/delete

정리하면, 

"미네랄을 캐라" 를 api로 해보자면, 

리소스 = 미네랄. 이니까 리소스만 가지고 리소스 설계하는 것임

/members, /orders  이렇게...

/mebers/{id}, /orders/{id} 이것만으로 해결이 안된다?

→ 이때 컨트롤 URI 로 해결

즉 컬렉션과 문서로 최대한 해결하고, 안되면 컨트롤URI를 넣어라

[https://restfulapi.net/resource-naming/](https://restfulapi.net/resource-naming/)  여기에 좋은 예제들 있음

# 6.http상태코드

## 2xx

- 200  OK

-요청

```jsx
GET /members/100 HTTP/1.1
Host: localhost:8080
```

-응답

```jsx
HTTP/1.1 200 OK
Content-Type: application/json
Content-Length: 34
{
 "username": "young",
 "age": 20
}
```

- 201 Created

요청 성공해서 새로운 리소스가 생성됨!

요청

```jsx
POST /members HTTP/1.1
Content-Type: application/json
{
 "username": "young",
 "age": 20
}
```

응답- Location 필드로 생성된 리소스 식별가능

```jsx
HTTP/1.1 201 Created
Content-Type: application/json
Content-Length: 34
**Location: /members/100** 
{
 "username": "young",
 "age": 20
}
```

- 204 No Content

요청성공! 근데 딱히 페이로드에 보낼 데이터 없으

문서 편집할때 중간중간 save할때 딱히 응답할 데이터 없잖어

## 3xx

### 영구리다이렉션-301,308

걍 그 리소스의 URI가 아예 바뀌어버린것임. 원래 URL은 사용안해

- 301
    - 리다이렉트시 요청 메서드가 GET으로 변하고, 본문이 제거될 수 있음(May)
- 308
    - 301과 기능 같음. 단, 리다이렉트시 요청 메서드와 본문 유지(첨에 post면 post유지)

### 일시적인 리다이렉션-302,303,307

- 302
    - 리다이렉트시 요청메서드가 GET으로 변하고, 본문제거될 수 있음(May)
- 307
    - 302와 같지만, 요청메서드와 본문유지해야 함
- 303
    - 302와 같음 단 요청메서드 GET으로 변경해야 함

- PRG: Post/Redirect/Get

예시: 
![Untitled2](https://user-images.githubusercontent.com/78577071/131545566-f5da6484-50b5-457c-847c-d272686c71ee.png)

Post로 주문 후에 새로고침할때 중복 주문 방지함. 리다이렉트할 때 GET으로 바꿔주는것임.
