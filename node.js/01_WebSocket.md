# WebSocket 기본

프레임워크 사용안하고 한 것

- 서버

server.js

```jsx
import http from "http";
import WebSocket from "ws";
import express from "express";

const app = express();

app.set("view engine", "pug");
app.set("views", __dirname + "/views");
app.use("/public", express.static(__dirname + "/public"));
app.get("/", (req, res) => res.render("home"));

const handleListen = () => console.log("listening to http://localhost");

const server = http.createServer(app);
const wss = new WebSocket.Server({ server }); //독자적으로 만들 수 있지만, 3000포트에 http랑 동시에 서버를 열고 싶어서 이렇게 함

function onSocketClose() {
  console.log("Disconnected from the Broweser!");
}

const sockets = [];

wss.on("connection", (socket) => {
  sockets.push(socket);
  socket["nickname"] = "익명";
  console.log("connected to brower!");
  socket.on("close", onSocketClose);
  socket.on("message", (msg) => {
    const message = JSON.parse(msg); //string으로 전달된 json을 Json으로 변환
    switch (message.type) {
      case "new_message":
        sockets.forEach((aSocket) =>
          aSocket.send(`${socket.nickname}: ${message.payload}`)
        );
      case "nickname":
        socket["nickname"] = message.payload; //소켓에다가 새로운 속성을 넣은 것임
    }
  });
});

server.listen(3000, handleListen);
```

- 프론트

app.js

```jsx
const messageList = document.querySelector("ul");
const nickFrom = document.querySelector("#nick");
const messageForm = document.querySelector("#message");
const socket = new WebSocket(`ws://${window.location.host}`);

function makeMessage(type, payload) {
  const msg = { type, payload };
  return JSON.stringify(msg);
}

socket.addEventListener("open", () => {
  console.log("connected to server!");
});

socket.addEventListener("close", () => {
  console.log("server closed!");
});

socket.addEventListener("message", (message) => {
  const li = document.createElement("li");
  li.innerText = message.data;
  messageList.append(li);
});

function handleSubmit(event) {
  event.preventDefault();
  const input = messageForm.querySelector("input");

  socket.send(makeMessage("new_message", input.value));
  const li = document.createElement("li");
  li.innerText = `You: ${input.value}`;
  messageList.append(li);
  input.value = "";
}

function handleNickSubmit(event) {
  event.preventDefault();
  const input = nickFrom.querySelector("input");
  socket.send(makeMessage("nickname", input.value));
  input.value = "";
}

messageForm.addEventListener("submit", handleSubmit);
nickFrom.addEventListener("submit", handleNickSubmit);
```

- 예시

크롬에서 닉네임을 크롬이라하고 메시지를 보내면
![Untitled](https://user-images.githubusercontent.com/78577071/129720328-e2d5866e-6574-4b6d-a06f-cb636d0328e9.png)

엣지브라우저, 크롬부라우저 상황임
![Untitled 1](https://user-images.githubusercontent.com/78577071/129720345-e45020ad-bc67-4f63-b4fb-b282bf5a3d3e.png)
![Untitled 2](https://user-images.githubusercontent.com/78577071/129720360-d25177fb-9768-414a-8a5e-5c24bd0174bf.png)

- 더 해야할것:

내가 보낸 메시지는 나한테 전송하지 않도록 해야 함.

또한, 백엔드에서 프론트로 보낼때도 json형식으로 데이터타입붙여서 보내줘야 함(현재는 메시지만 보내서 상관없지만, 앞으로 여러종류의 데이터를 보낼수 있으므로)
