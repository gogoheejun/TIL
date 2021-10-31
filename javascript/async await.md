Udemy-Asynchronous Javascript Deep Dive

# async함수의 리턴값

```jsx
const plainFunction = async function () {
  console.log("start");
  return "done";
};

let result = plainFunction();
```

콘솔에서 result쳐보면

```jsx
Promise {<fulfilled>: "done"} 이케 뜸
```

즉 resolve된 promise를 리턴시킨다. 즉 

```jsx
new Promise(()=>{
	resolve("done"); 처럼 리졸브를 invoke시키는것임
})
```

- 프로미스를 리턴시키니까 then으로 받아보면

```jsx
const plainFunction = async function () {
  console.log("start");
  return "done";
};

plainFunction().then((val) => console.log(val));
```

```jsx
start
done//promise로 감쌌던 리졸브안에 든 밸류만 나오는것임
```

- await

await 안쓰고 그냥 해보면

```jsx
const asyncFunction = function () {
  return new Promise((resolve, reject) => {
    setTimeout(() => {
      resolve("hihi");
    }, 2000);
  });
};

const asyncFun = async function () {
  let p1 = asyncFunction();
  console.log(p1);
  console.log(`${p1}-lala`);
};

asyncFun();
```

p1에 프로미스가 전달되긴 하지만, resolve벨류가 가지 않아. 그냥 pending상태임

```jsx
//콘솔
Promise{<pending>}
[object Promise]-lala
```

await을 쓰면

```jsx
const asyncFunction = function () {
  return new Promise((resolve, reject) => {
    setTimeout(() => {
      resolve("hihi");
    }, 2000);
  });
};

const asyncFun = async function () {
  let p1 = await asyncFunction(); 
  console.log(p1);
  console.log(`${p1}-lala`);
};

asyncFun();
```

2초뒤에 프로미스의 리졸브 벨류를 가져와서 p1에 저장됨.

```jsx
//콘솔
hihi
hihi-lala
```

# 40장- 연습-코드순서보기

아래에서 콘솔로그 순서 어케 될까?

```jsx
const starwars = async function () {
  let url = "http://swapi.dev/api/films/",
    filmsData = {},
    films = [];

  console.log("starwars 1");

  filmsData = await fetch(url).then((data) => data.json());
  films = filmsData.results.map((obj) => obj.title);
  console.log(films);

  console.log("starwars 2");
};

starwars();

console.log("end code");
```

```jsx
//콘솔
starwars 1
end code
[원소들,원소들,원소들]
starwars 2
```

결과를 보면, await하면 해당함수에서 await 아래코드는 다 안돌아가는걸 볼 수 잇음

# 43강- 활용연습

```jsx
let user3Posts;
const retrievePosts = async function (userID) {
  let url = "https://jsonplaceholder.typicode.com/posts",
    posts = [];

  posts = await fetch(url).then((resp) => resp.json());
  return posts.filter((obj) => obj.userId === userID);
};

 //retrievePost의 리턴이 프로미스이므로 then으로 리졸빙해줘야함
retrievePosts(3).then((val) => (user3Posts = val));

console.log("end of code");
```

//콘솔에 user3Posts 치면 배열뜸

# 47강-프로미스→Async로 리팩토링

아래의 코드를 Async로 바꿔보자

```jsx
let todo = {
  completed: false,
  userId: 1,
  title: "Learn Promises",
};

fetch("https://jsonplaceholder.typicode.com/todos/", {
  method: "POST",
  headers: {
    "Content-type": "application/json",
  },
  body: JSON.stringify(todo),
})
  .then((resp) => resp.json())
  .then((obj) => console.log(obj))
  .catch((reject) => console.log(reject));

console.log("code ends");
```

→

```jsx
let addTodo = async function (todo) {
  try {
    let resp = await fetch("https://jsonplaceholder.typicode.com/todos/", {
      method: "POST",
      headers: {
        "Content-type": "application/json",
      },
      body: JSON.stringify(todo),
    });
    let results = await resp.json();

    console.log(results);
  } catch (e) {
    console.log(e);
  }
};

addTodo(todo);

console.log("end code");
```

# 50강- 예제2-프로미스→async

```jsx
var MAINAPP = (function(nsp) {
    "use strict";

    let url = 'https://jsonplaceholder.typicode.com/';

    fetch(url + 'posts/')
    .then(response1 => response1.json())
    .then(posts => nsp.posts = posts)
    .catch(err => console.log(`Problem retrieving posts: ${err}`));

    //public
    return nsp;
})(MAINAPP || {});
```

→

```jsx
var MAINAPP = (function (nsp) {
  "use strict";

  let url = "https://jsonplaceholder.typicode.com/";
  (async function () {
    try {
      let data = await fetch(url + "posts/"),
        posts = await data.json();

      nsp.posts = posts;
    } catch (e) {
      console.log(e);
    }
  })();

  return nsp;
})(MAINAPP || {});
```

# 51강-Promise.all with async await

```jsx
let firstName = function () {
  return new Promise(function (resolve, reject) {
    setTimeout(function () {
      resolve("Steven");
    }, 1000);
  });
};

let lastName = function () {
  return new Promise(function (resolve, reject) {
    setTimeout(function () {
      resolve("Hancock");
    }, 3000);
  });
};

let middleName = function () {
  return new Promise(function (resolve, reject) {
    setTimeout(function () {
      resolve("W.");
    }, 4000);
  });
};

(async function () {
  let names = await Promise.all([firstName(), lastName(), middleName()]);
  console.log(names[0] + names[1] + names[2]);
})();

// Promise.all([firstName(), lastName(), middleName()])
//   .then(function (msg) {
//     console.log(msg[0] + " " + msg[2] + " " + msg[1]);
//   })
//   .catch(function (msg) {
//     console.log(msg);
//   });

console.log("Remaining Code");
```

# 54강-async사용시 주의사항

await없이 async만 붙인 함수 써보자

```jsx
let todo = {
  completed: false,
  userId: 1,
  title: "Learn Promises",
};
async function num1() {
  console.log(1);
  return 1;
}

async function num2() {
  console.log(2);
  return 2;
}

async function main() {
  console.log("start main");
  num1();
  num2();
  console.log("end main");
  return "main";
}

main();

console.log("last line");
```

콘솔: 비동기따위 없고 그냥 일반함수 실행과 똑같음을 알 수 있다. 즉 async자체가 비동기를 만드는게 아니라, await를 붙이기 위한 하나의 패턴일 뿐이다.

```jsx
start main
1
2
end main
last line
```

- 아래 함수 순서를 예측해보고 분석해보자

```jsx
async function num1() {
  setTimeout(() => console.log(1), 0);
  return 1;
}

async function num2() {
  console.log(2);
  return 2;
}

async function main() {
  console.log("start main");
  num1();
  num2();
  console.log("end main");
  return "main";
}

main().then((val) => console.log(val));

console.log("last line");
```

→콘솔:setTimeout은 Macro task이고, then함수는 micro task임(프로미스는 모두 마이크로). micro가 macro보다 우선순위가 더 높아서 먼저 처리됨. 그래서 main출력되고 1출력됨

```jsx
start main
2
end main
last line
main
1
```
