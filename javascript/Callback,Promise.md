udemy-Asynchronous Javascript Deep Dive

# 7강-EventLoop

이벤트루프는 계속 메시지큐를 확인함. 만약 setTimout으로 정한시간이 되서 콜백함수가 실행되면 큐에 메시지(함수)가 추가됨. 현재 일반적으로 선언된 코드들이 다 끝나면 메시지큐를 체크하고 거기에 있는 콜백함수를 실행시킴

# 11강-콜백 직접 만들어보기

```jsx
let students = [{name:"Mary",score:90,school:"East"},
{name:"James",score:100,school:"East"},
{name:"Steve",score:40,school:"East"},
{name:"Gabe",score:90,school:"West"},
{name:"Rachel",score:85,school:"East"},
{name:"Rochelle",score:95,school:"West"},
{name:"Lynette",score:75,school:"East"}];

let processStudents = function(data, callback) {
    for (let i = 0; i < data.length; i++) {
        if (data[i].school.toLowerCase() === "east") {
            if (typeof callback === "function") {
                callback(data[i]);
            }
        }
    }
}

processStudents(students, function(obj) {
    if (obj.score > 60) {
        console.log(obj.name + " passed!");
    }
});

let determineTotal = function() {
    let total = 0,
        count = 0;

    processStudents(students, function(obj) {
        total = total + obj.score;
        count++;
    });

    console.log("Total Score: " + total + " - Total Count: " + count);
}

determineTotal();
```

근데 주의할 점이: 위에서 determineTotal(); 다음에 콘솔을 찍어보면 비동기적으로 실행되지 않음. 

위 코드에 아래처럼 콘솔추가해보면

```jsx
determineTotal();
console.log("hihi"); 
```

determineTotal(); 실행 다 끝나야지만 콘솔찍힘. 

지금은 문제 1도 안되지만, 만약 determineTotal()안에 데이터가 엄청나게 대용량이라면??? 그럼 문제생김

왜 위 코드는 Async가 아닌가? →바로 event loop를 사용하지 않았기 때문임

# 14강-위 코드를 Async로 만들어보기

processStudents 함수에서 대량의 데이터가 들어가서 시간이 오래걸릴수도 있잖어. 어싱크로 만들어보자.

- 시도 1: processStudents 함수의 인자로들어가는 콜백함수를 어싱크로 보내버리면?

안됨. callback()이렇게 괄호가 붙여진것 자체가 이미 걍 바로 실행되버렸단것임. 다 실행된 담에 setTimout에 들어가봤자 노의미.

```jsx
let processStudents = function (data, callback) {
  for (let i = 0; i < data.length; i++) {
    if (data[i].school.toLowerCase() === "east") {
      if (typeof callback === "function") {
        **setTimout(callback(data[i]),0);**
      }
    }
  }
};
```

- 시도2:

```jsx
let processStudents = function (data, callback) {
  for (let i = 0; i < data.length; i++) {
    if (data[i].school.toLowerCase() === "east") {
      if (typeof callback === "function") {
        **setTimout(callback,0, data[i]);**
      }
    }
  }
};
```

Before determineTotal
Total Score: 0 - Total Count: 0
End of code

위처럼 결과뜸. 즉 전체적으로는 어싱크가 아니란것임.

- 정답

**determineTotal 자체에다가 setTimout걸어버림**

```jsx
let processStudents = function (data, callback) {
  for (let i = 0; i < data.length; i++) {
    if (data[i].school.toLowerCase() === "east") {
      if (typeof callback === "function") {
        callback(data[i]);
      }
    }
  }
};
console.log("Before determineTotal");

let determineTotal = function () {
  let total = 0,
    count = 0;

  processStudents(students, function (obj) {
    total = total + obj.score;
    count++;
  });

  console.log("Total Score: " + total + " - Total Count: " + count);
};

**setTimeout(determineTotal, 0);**

console.log("End of code");
```

Before determineTotal
End of code
Total Score: 390 - Total Count: 5

제대로 됨!

# 17강-프로미스

프로미스는 속성과 메서드로 이뤄진 object임.

체이닝해보기

```jsx
let promise = asyncFunction();

let promise2 = promise.then(function(val){
	console.log("haha"+val);
	return asyncFunction2();
});

promise2.then(function(val){
	console.log("Second promise:" + val);
});

console.log("finish");
```

→체이닝:

```jsx
asyncFunction()
.then(function(val){
	console.log("haha"+val);
	return asyncFunction2();
})
.then(function(val){
	console.log("Second promise:" + val);
});

console.log("finish");
```

# 21강-프로미스 연습:스타워즈

- fetch api사용

async이기 때문에 Promise를 반환함

[https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API/Using_Fetch](https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API/Using_Fetch)

```jsx
const swapi = function (num) {
  let url = "https://swapi.dev/api/people/";
  fetch(url + num + "/")
    .then((data) => data.json())
    .then((obj) => {
      console.log(obj);
      return fetch(obj.homeworld);
    })
    .then((hwdata) => hwdata.json())
    .then((hwobj) => console.log(hwobj));
};

swapi(1);

console.log("Other commands!");
```

# 22강- 연습2

[https://jsonplaceholder.typicode.com/](https://jsonplaceholder.typicode.com/) 여기서 제공하는 연습용 api로 crud연습가능함

아래처럼 해보면

```jsx
fetch("https://jsonplaceholder.typicode.com/todos/5")
  .then((response) => response.json())
  .then((json) => console.log(json));

console.log("Other code");
```

아래처럼 콘솔로그찍힘

```jsx
{userId: 1, id: 5, title: "laboriosam mollitia et enim quasi adipisci quia provident illum", completed: false}
```

그럼 직접 넣어보자

```jsx
"use strict";

let todo = {
  completed: false,
  userId: 1,
  title: "learning promises",
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
  .catch((reject) => console.log(`Unable to create todo ${reject}`));

console.log("Other code");
```

콘솔로그:

```jsx
{completed: false, userId: 1, title: "learning promises", id: 201}
```

# 29강- Promise 만들어보기

아래처럼 만들어보면

```jsx
let a = new Promise(function (resolve, reject) {
  setTimeout(function () {
    resolve("Done");
  }, 4000);
});

console.log(a);

console.log("finished");
```

콘솔에 프로미스나옴

```jsx
Promise {<pending>}
	[[Prototype]]: Promise
	[[PromiseState]]: "fulfilled"
	[[PromiseResult]]: "Done" //4초지나야 done뜸
finished
```

- then써보자

```jsx
let a = new Promise(function (resolve, reject) {
  setTimeout(function () {
    resolve("Done");
  }, 4000);
});

a.then(function (val) {
  console.log(val);
});

console.log("finished");
```

```jsx
finished
Done
```

- resolve대신 reject해보자

```jsx
let a = new Promise(function (resolve, reject) {
  setTimeout(function () {
    reject("Done");
  }, 4000);
});

a.then(
  function (val) {
    console.log(val);
  },
  function (val) {
    console.log("rejected: " + val);
  }
);

console.log("finished");
```

```jsx
finished
rejected: Done
```

- 다른 예제에서 했던 프로미스함수를 이해할수있음이제 다시함 봐보셈

```jsx
let asyncFunction = function () {
  return new Promise(function (resolve, reject) {
    setTimeout(function () {
      resolve("async has resolved");
    }, 3000);
  });
};
```

```jsx
리턴으로 프로미스객체만 나옴
```

- setTimout을 프로미스로 바꿔보자

```jsx
let setTimeoutP = function (time) {
  return new Promise(function (res, rej) {
    if (isNaN(time)) {
      rej("only number is permitted");
    }
    setTimeout(res, time);
  });
};

setTimeoutP(3000)
  .then(function () {
    console.log("done!");
  })
  .catch(function (err) {
    console.log(err);
  });
```

위처럼 res만 달랑 써서 성공했단 거만 알려주고 아무것도 안보낼 수도 있고

res()안에 벨류 보낼수 있음

```jsx
let setTimeoutP = function (time) {
  return new Promise(function (res, rej) {
    if (isNaN(time)) {
      rej("only number is permitted");
    }
    setTimeout(() => {
      res("haha");
    }, time);
  });
};

setTimeoutP(2000)
  .then(function (val) {
    console.log(val);
  })
  .catch(function (err) {
    console.log(err);
  });
```

→2초뒤에 haha찍힘

# 30강- 예제3-복잡한계산 Promise로 바꾸기

```jsx
const massiveProcess = function (num) {
  let result = 0;
  for (let i = num ** 7; i >= 0; i--) {
    result += Math.atan(i) * Math.tan(i);
  }
  return result;
};

let amt = massiveProcess(10);
console.log("The number is " + amt);

//more processing later on
console.log(5 * 5 + 100);
```

위 결과를 실행하면 아래 125결과 뜨기까지 시간이 좀 걸림.

그렇다고 아래처럼 setTimeout() 0초를 주면 아래코드가 빨리 진행되긴 하지만

```jsx
const massiveProcess = function (num) {
  let result = 0;
  setTimeout(() => {
    for (let i = num ** 7; i >= 0; i--) {
      result += Math.atan(i) * Math.tan(i);
    }
    return result;
  }, 0);
};

let amt = massiveProcess(10);
console.log("The number is " + amt);

//more processing later on
console.log(5 * 5 + 100);
```

원하는 결과가 아님. 언디파인드잖어

```jsx
The number is undefined
125
```

정답: return Promise로 하고, setTimout에 리턴대신 resolve를 invoke시킴.

```jsx
const massiveProcess = function (num) {
  return new Promise(function (resolve, reject) {
    if (isNaN(num)) {
      reject("please enter a number!");
    } else {
      let result = 0;
      setTimeout(() => {
        for (let i = num ** 7; i >= 0; i--) {
          result += Math.atan(i) * Math.tan(i);
        }
        resolve(result);
      }, 0);
    }
  });
};

massiveProcess(10)
  .then((result) => {
    console.log("The number is " + result);
  })
  .catch((error) => console.log(error));

//more processing later on
console.log(5 * 5 + 100);
```

이제 블로킹하는 것 없이 바로 125출력되고 그 담에 복잡한 계산 수행결과나옴

```jsx
125
The number is -2898550.376692899
```

# 31강 주의사항-Promise에서의 setTimeout

어차피 프로미스는 비동기인데 굳이 그 안에 setTimeout넣을 필요있나?

아래처럼 setTimeout주석처리해보면 결과똑같이 나옴

```jsx
const massiveProcess = function (num) {
  return new Promise(function (resolve, reject) {
    if (isNaN(num)) {
      reject("please enter a number!");
    } else {
      let result = 0;
      // setTimeout(() => {
      for (let i = num ** 7; i >= 0; i--) {
        result += Math.atan(i) * Math.tan(i);
      }
      resolve(result);
      // }, 0);
    }
  });
};
```

그러나 차이존재함!

setTimeout을 넣었을땐: 125먼저 뜨고 그담에 1초정도뒤에 복잡한계산 결과가 뜸

위처럼 주석처리 하면: 첨에 아무것도 안뜨고 1초정도뒤에 125먼저 뜨고 바로 복잡계산결과 뜸

왜이럼? 

설명1: 프로미스로만 처리하면 js안에서 직접 다 돌리는데, setTimeout을 하면 브라우저나 node같이 js바깥에서 대신 돌려주기때문임. javascript handles everything in the promise!!

설명2: 버튼눌러서 이벤트발생시키거나 셋타임아웃 이런건 macro task라 함.이런 macro task들은 micro taks들이 다 끝나기 전까지. 고로 그냥 프로미스에서는 다 js가 처리해서 저 for루프 복잡한 계산은 micro task에 해당하기 때문에 우선순위가 됨. 그래서 계산 다 하고나서야 그다음 콘솔로그찍게 되서 시간이 오래걸림.

- 마이크로 vs 매크로

```jsx
setTimeout(()=>{console.log('Task1')}); //Macro

let promiseTask = new Promise((resolve, reject)=>{ //Micro
	resolev();
});
promiseTask.then(()=>{console.log('Task2')})

console.log('Main');
```

결과:

```jsx
Main
Task2
Task1
```

마이크로가 먼저실행되고, 그담에 매크로.

# 34강- static method:  all, race

이건 쉬우니까 검색하면 나옴. Promise.all() 이런식으로 쓰는 Promise의 static method임.

```jsx
Promise.all([p1,p2,p3]) //p1,p2,p3모두 리졸브되야 then넘어감
	.then((msg)=>{console.log(msg)}); //msg똑같이 [p1,p2,p3]나옴

Promise.all([p1,p2,p3])
	.then((msg)=>{console.log(msg)}); //젤빨리 도착한애만 보여줌
```

- 예제:

```jsx
var MAINAPP = (function (nsp) {
  "use strict";

  let url = "https://jsonplaceholder.typicode.com/";

  /*
    Change this code so that it uses Promise.all to respond once all of the promises have returned. Provide a notification to the console when the promises have completed.
    */
  fetch(url + "posts/")
    .then((response1) => response1.json())
    .then((posts) => (nsp.posts = posts))
    .catch((err) => console.log(`Problem retrieving posts: ${err}`));

  fetch(url + "comments/")
    .then((response2) => response2.json())
    .then((comments) => (nsp.comments = comments))
    .catch((err) => console.log(`Problem retrieving comments: ${err}`));

  fetch(url + "todos/")
    .then((response3) => response3.json())
    .then((todos) => (nsp.todos = todos))
    .catch((err) => console.log(`Problem retrieving todos: ${err}`));

  //public
  return nsp;
})(MAINAPP || {});

MAINAPP();
```

→

```jsx
let p1 = fetch(url + "posts/").then((response1) => response1.json()),
    p2 = fetch(url + "comments/").then((response2) => response2.json()),
    p3 = fetch(url + "tools/").then((response3) => response3.json());

  Promise.all([p1, p2, p3])
    .then((msg) => {
      nsp.posts = msg[0];
      nsp.comments = msg[1];
      nsp.todos = msg[2];
      console.log("we have received the data!");
    })
    .catch((err) => console.log(err));
```
