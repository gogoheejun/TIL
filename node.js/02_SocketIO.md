# Socket.IO

프론트와 백엔드 간 실시간 통신을 가능하게 해주는 프레임워크임.

WebSocket이 아님. 웹소켓을 사용하는 것임. 근데 웹소캣만 사용하는 게아님. 

즉, 브라우저가 웹소켓을 지원안하거나 연결문제(와이파이 잠깐끊김)가 생길때 소켓io가 다른 방법을 통해 실시간연결을 해줌

먼저 html에 다음코드 추가

왜냐면 websocket은 원래도 브라우저에 설치되어있어서 괜찮은데 socket.io는 없기때문에 추가해서 보내줘야 해서.

```jsx
script(src="/socket.io/socket.io.js")
```

# 초간단연결

server엔 다음만 추가

```jsx
const httpServer = http.createServer(app);
const wsServer = SocketIO(httpServer); //독자적으로 만들 수 있지만, 3000포트에 http랑 동시에 서버를 열고 싶어서 이렇게 함

wsServer.on("connection", (socket) => {
  console.log(socket);
});

const handleListen = () => console.log("listening to http://localhost");
httpServer.listen(3000, handleListen);
```

프론트엔 다음추가

```jsx
const socket = io(); //이러면 알아서 socket.io를 실행하는 서버를찾아줌
```

 끝! 개간단

# 간단하지만 매우 중요한 예시

app.js

socket.emit()함수에 인자 여러개 보낼수있음. 원하는만큼!

장점: json객체를 걍 바로 보낼수있음. (json 뿐 아니라 걍 아무거나 다 보낼수있음)

특이한 점: 3번째(마지막이면 됨) 인자에 함수를 보냈는데, 이 함수를 서버에서 받아서 실행시키면 서버가 아니라 프론트에서 실행됨.(로그찍으면 브라우저의 콘솔에 찍힘)

```jsx
function handleRoomSubmit(event) {
  event.preventDefault();
  const input = form.querySelector("input");
  socket.emit("enter_room", { payload: input.value }, () => {
    console.log("server is done!");
  }); //웹소켓할땐 json을 string으로 변환시켜서 보냈는데 이젠 걍 보냄
  input.value = "";
}
```

server.js

```jsx
wsServer.on("connection", (socket) => {
  socket.on("enter_room", (msg, done) => {
    console.log(msg);
    setTimeout(() => {
      done();
    }, 5000);
  });
});
```

# Room

채팅룸을 만들다거나, 1대1대화를 한다거나 뭐 그럴때마다 룸이 필요.

socket이 생길때마다 사용자 id가 자동으로 생기게 됨. socket.join을 하게 되면, 프론트에서 보낸 roomname으로 socket.rooms에  저장됨

```jsx
wsServer.on("connection", (socket) => {
  socket.onAny((event) => {
    console.log(`Socket Events:${event}`);
  });
  socket.on("enter_room", (roomName, done) => {
    console.log(socket.id);//IE_ogXSrXEK3KyXWAAAD
    console.log(socket.rooms);//Set(1) { 'IE_ogXSrXEK3KyXWAAAD' } ..id가 프라이빗룸 역할도 함을 알 수 있음
    socket.join(roomName);
    console.log(socket.rooms);//Set(2) { 'IE_ogXSrXEK3KyXWAAAD', 'myroom' }
    setTimeout(() => {
      done("hello from the backend");
    }, 5000);
  });
});
```

- 공식 문서봐보면 설명 되어잇음

```jsx
io.on("connection", (socket) => {

  // to one room
  socket.to("others").emit("an event", { some: "data" });

  // to multiple rooms
  socket.to("room1").to("room2").emit("hello");

  // or with an array
  socket.to(["room1", "room2"]).emit("hello");

  // a private message to another socket
  socket.to(/* another socket id */).emit("hey");

  // WARNING: `socket.to(socket.id).emit()` will NOT work, as it will send to everyone in the room
  // named `socket.id` but the sender. Please use the classic `socket.emit()` instead.
});
```

- 룸에 누가 들어왔을대 알림뜨기

서버.

```jsx
wsServer.on("connection", (socket) => {
  socket.onAny((event) => {
    console.log(`Socket Events:${event}`);
  });
  socket.on("enter_room", (roomName, done) => {
    socket.join(roomName);
    done();
    socket.to(roomName).emit("welcome");
  });
```

프론트

```jsx
function addMessage(message) {  const ul = room.querySelector("ul");  const li = document.createElement("li");  li.innerText = message;  ul.appendChild(li);}socket.on("welcome", () => {  addMessage("someone joined!");});
```

결과: 같은방에 다른 브라우저로 방 만들면 원래브라우저에 다음처럼 뜸
![Untitled](https://user-images.githubusercontent.com/78577071/129910183-5be7d051-e360-4bc2-9607-1610ab8a24a7.png)


- 메시지 보내기

프론트

```jsx
let roomName;function addMessage(message) {  const ul = room.querySelector("ul");  const li = document.createElement("li");  li.innerText = message;  ul.appendChild(li);}//메시지 보낼때마다 이함수가 실행됨function handleMessageSubmit(event) {  event.preventDefault();  const input = room.querySelector("input");  const value = input.value;	//"new_message"란 키워드로 서버로 보냄  socket.emit("new_message", input.value, roomName, () => {    addMessage(`You: ${value}`);  });  input.value = "";}//먼저 룸이름 치면 아래 함수가 실행되면서 EventListener 달아줌function showRoom() {  welcome.hidden = true;  room.hidden = false;  const h3 = room.querySelector("h3");  h3.innerText = `Room ${roomName}`;  const form = room.querySelector("form");  form.addEventListener("submit", handleMessageSubmit);}socket.on("welcome", () => {  addMessage("someone joined!");});socket.on("bye", () => {  addMessage("someone left ㅠㅠ");});//서버가 "new_message"란 키워드로 보내면 받아서 addMessage함수실행socket.on("new_message", addMessage);
```

서버

```jsx
socket.on("disconnecting", () => {    socket.rooms.forEach((room) => socket.to(room).emit("bye"));  });  socket.on("new_message", (msg, room, done) => {    console.log(msg);    socket.to(room).emit("new_message", msg);    done();  });
```

- 닉네임: 내용  형식으로 메시지 보내기

프론트

```jsx
function handleNicknameSubmit(event) {  event.preventDefault();  const input = room.querySelector("#name input");  socket.emit("nickname", input.value);}....	nameForm.addEventListener("submit", handleNicknameSubmit);....
```

서버

```jsx
...	socket.on("nickname", (nickname) => (socket["nickname"] = nickname));//socket에 속성추가...//이제 메시지오면 소켓에 추가한 닉네임도 붙여서 전송해주면 됨socket.on("new_message", (msg, room, done) => {    console.log(msg);    socket.to(room).emit("new_message", `${socket.nickname}: ${msg}`);    done();  });
```

![Untitled 1](https://user-images.githubusercontent.com/78577071/129910208-f52988d9-0fae-4345-8455-d2f3be449bae.png)


# 어댑터

모든 클라이언트가 동일한 서버에 연결되는 것이 아님. 서버가 두세개 있을 수 있어. 서버 A에 있는 클라이언트가 서버B에 있는 클라이언트한테 메시지 보내고싶다면 어댑터-몽고디비-어댑터 를 통과해야 함

![Untitled 2](https://user-images.githubusercontent.com/78577071/129910230-b08bf70b-0165-49c3-8cc8-9b757a4c558e.png)

- 만들어진 방 개수 보여주기

서버

```jsx
...socket.on("enter_room", (roomName, done) => {    socket.join(roomName);    done();    socket.to(roomName).emit("welcome", socket.nickname);    wsServer.sockets.emit("room_change", publicRooms()); //메시지를 모든 socket에 보냄  });... //방 나갈때도 해줌 방이 없어졌을 수 있으니까socket.on("disconnect", () => {    wsServer.sockets.emit("room_change", publicRooms());  });
```

프론트

```jsx
socket.on("room_change", (rooms) => {  const roomList = welcome.querySelector("ul");  roomList.innerHTML = "";  if (rooms.length === 0) {    return;  }  rooms.forEach((room) => {    const li = document.createElement("li");    li.innerText = room;    roomList.append(li);  });});
```

- 방 안에 몇명 있는지 보여주기

서버

```jsx
//방에 몇명있는지 세기function countRoom(roomName) {  return wsServer.sockets.adapter.rooms.get(roomName)?.size;}//방입장/퇴장시 countRoom()을 인자로 보냄socket.on("enter_room", (roomName, done) => {    socket.join(roomName);    done();    socket.to(roomName).emit("welcome", socket.nickname, countRoom(roomName));    wsServer.sockets.emit("room_change", publicRooms()); //메시지를 모든 socket에 보냄  });  socket.on("disconnecting", () => {    socket.rooms.forEach((room) =>      socket.to(room).emit("bye", socket.nickname, countRoom(room) - 1)    );  });
```

프론트

```jsx
socket.on("welcome", (user, newCount) => {  const h3 = room.querySelector("h3");  h3.innerText = `Room ${roomName} (${newCount})`;  addMessage(`${user} arrived!`);});socket.on("bye", (left, newCount) => {  const h3 = room.querySelector("h3");  h3.innerText = `Room ${roomName} (${newCount})`;  addMessage(`${left} left!ㅠㅠ`);});
```

- 추가로 adimn panel도 할수있음. 나중에 ㄱ
