controller/auth.js에서 아래처럼 설정한게 있음. 코드에 남기면 너무 위험

```jsx
// TODO: Make it secure!
~~const jwtSecretKey = 'F2dN7x8HVzBWaQuEEDnhsvHXRWqAR63z';
const jwtExpiresInDays = '2d';
const bcryptSaltRounds = 12;~~

...
...
function createJwtToken(id) {
  return jwt.sign({ id }, jwtSecretKey, { expiresIn: jwtExpiresInDays });
}
```

→해결

1. npm i dote 로 라이브러리 설치먼저하고
2. .env파일 만들어서 위에서 지운내용을 여기다가 입력

```jsx
JWT_SECRET=F2dN7x8HVzBWaQuEEDnhsvHXRWqAR63z

JWT_EXPIRES_SEC=86400
BCRYPT_SALT_ROUNDS=12
```

1. 참고로 .girignore에선 노드몬이랑 닷엔브 기록해줘야 깃헙에 안올라감

```jsx
node_modules/
.env
```

1. app.js에 임포트하고 컨피그함수실행.

```jsx
...
import dotenv from 'dotenv';
dotenv.config();
...
console.log(process.env.JWT_SECRET);
...
```

이케 하면 console.log(process.env)라고 했을때 dotenv에 기록한 값들이 추가되잇음.

즉, 코드에 저장시키는 게 아니라, 서버를 돌리는 컴퓨터자체의 환경변수에다가 추가시키큰것임.(JAVA_HOME 이런거처럼)

# configuration 리팩토링

1. 문제점1

걍 .env에다가 쓰면 이름 갖다쓰기 힘듦. 자동완성도 안되고.→오타발생가능성있음

config.js파일 만들어서 거기다가 때려박을거임

방금 app.js에다가 dotenv임포트하고 실행했던걸 이 config.js파일에다가 잘라서 붙여넣음

config.js

```jsx
import dotenv from "dotenv";
dotenv.config();

export const config = {
  jwt: {
    secretKey: required(JWT_SECRET),
    expireINSec: parseInt(required(JWT_EXPIRES_SEC, 86400)),
  },
  bcrypt: {
    saltRounds: parseInt(required(BCRYPT_SALT_ROUNDS, 12)),
  },
  host: {
    port: parseInt(required("HOST_PORT", 8080)),
  },
};
```

이제 저걸 활용

controller/auth.js

```jsx
export async function signup(req, res) {
...
	const hashed = await bcrypt.hash(password, config.bcrypt.saltRounds);
...
}

function createJwtToken(id) {
  return jwt.sign({ id }, config.jwt.secretKey, {
    expiresIn: config.jwt.expireINSec,
  });
}
```

다른곳들도 다 바꿔줌.

1. 문제점2

process.env.JWT_SECRET이렇게 있는거만 안쓰고, 없는거 process.env.LALALA이렇게 했을때 없다고 서버시작하자마자 알려주는코드작성

config.js

```jsx
import dotenv from "dotenv";
dotenv.config();

function required(key, defaultValue = undefined){
  const value = process.env[key]||defaultValue;
  if(value==null){//null, undefined 모두 true
    throw new Error(`Key ${key} is undefined`);
  }
	return value;
}
export const config = {
  jwt: {
    secretKey: required(JWT_SECRET),
    expireINSec: parseInt(required(JWT_EXPIRES_SEC, 86400)),
  },
  bcrypt: {
    saltRounds: parseInt(required(BCRYPT_SALT_ROUNDS, 12)),
  },
  host: {
    port: parseInt(required("HOST_PORT", 8080)),
  },
};
```

이제 이케하면 .env에다가 막 수정하고 그래도 괜찮아짐.

```jsx
~~JWT_SECRET=F2dN7x8HVzBWaQuEEDnhsvHXRWqAR63z~~ 얘를 삭제하면 에러띄어줌. 기본값이 없으니까

JWT_EXPIRES_SEC=86400  
~~BCRYPT_SALT_ROUNDS=12~~  //아예삭제해도 알아서 됨. 기본값이 있으니까
```
