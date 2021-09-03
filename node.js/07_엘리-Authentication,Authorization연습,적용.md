세션,쿠키방법은 알고있으니 패스. 이 방법은 stateful한 방법이라 서버확장할때 한계가 있음

# bcrypt

구성: alg+cost(암호화정도)+salt+hash

비크립트는 알고리즘으로 hash를 만들 순 있지만, 만들어진 해시를 다시 비번으로 바꿀수없음.(**단방향)**

근데 ㄱ많은 경우의 수를 해커들이 표로 정리해놨기에 salt를 추가해줘서 그 경우의 수를 사실상 유추할 수 없도록함.

- 연습해보기

```jsx
npm i bcrypt
```

복잡도 올릴수록 기하급수적으로 오름.너무 높이면 cpu성능안되서 보통 8~12권장됨

```jsx
const bcrypt = require("bcrypt");

const password = "abcd1234";
const hashed = bcrypt.hashSync(password, 10);//복잡도 10. 일단 동기적으로 연습해봄
console.log(hashed);

const result = bcrypt.compareSync("abcd1234", hashed);
console.log(result);
```

→

$2b$10$2WNLXtftIb.9y1oRvTW9F.hAEHwQ3ag8otQzXQWj3Qvf2Gvz.AbDK
true

# JWT 기본연습

install

```jsx
npm i jsonwebtoken
```

```jsx
const jwt = require("jsonwebtoken");

const secret = "test";
const token = jwt.sign(
  {
    id: "userId",
    isAdmin: true,
  },
  secret,
  { expiresIn: 2 }
);

jwt.verify(token, secret, (error, decoded) => {
  console.log(error,decoded);
});
```

→

eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6InVzZXJJZCIsImlzQWRtaW4iOnRydWUsImlhdCI6MTYzMDY2MzYwOSwiZXhwIjoxNjMwNjYzNjExfQ.CkykSae6BtFts4ksg_QKu6hOfVu2io5eWFRiZ7vChCc
null { id: 'userId', isAdmin: true, iat: 1630663609, exp: 1630663611 }

# Dwitter예제에 JWT 적용

이 예제에서는 jwt를 사용할 것임. 

이유는

1. restful API서비스 백엔드이기 때문임. 즉 웹브라우저에게 http-only쿠키를 보내는 서버가 아니라 다양한 클라이언트를 위한 것임
2. 마이크로서비스, 확장성을 위해

- app.js에 요렇게 추가해줘야지

```jsx
import authRouter from './router/auth.js';

app.use('/auth', authRouter);
```

- 이제 라우터 가서 작업해줘야지

router/auth.js 생성. 유효성검사도 해줬음

```jsx
import express from 'express';
import {} from 'express-async-errors';
import { body } from 'express-validator';
import { validate } from '../middleware/validator.js';
import * as authController from '../controller/auth.js';
import { isAuth } from '../middleware/auth.js';

const router = express.Router();

const validateCredential = [
  body('username')
    .trim()
    .notEmpty()
    .withMessage('username should be at least 5 characters'),
  body('password')
    .trim()
    .isLength({ min: 5 })
    .withMessage('password should be at least 5 characters'),
  validate,
];

const validateSignup = [
  ...validateCredential,//이건 위에 username,password 유효성검사 재활용
  body('name').notEmpty().withMessage('name is missing'),
  body('email').isEmail().normalizeEmail().withMessage('invalid email'),
  body('url')
    .isURL()
    .withMessage('invalid URL')
    .optional({ nullable: true, checkFalsy: true }),
  validate,
];

//이제 컨트롤러에게 위임
router.post('/signup', validateSignup, authController.signup);

router.post('/login', validateCredential, authController.login);

router.get('/me', isAuth, authController.me); //여기의 isAuth는 아래에 따로 설명함

export default router;
```

- 이제 컨트롤러:

```jsx
import jwt from 'jsonwebtoken';
import bcrypt from 'bcrypt';
import {} from 'express-async-errors';
import * as userRepository from '../data/auth.js';

// TODO: Make it secure! 여긴 나중에 처리. 서버에 남겨두면 안됨
const jwtSecretKey = 'F2dN7x8HVzBWaQuEEDnhsvHXRWqAR63z';
const jwtExpiresInDays = '2d';
const bcryptSaltRounds = 12;

//회원가입
export async function signup(req, res) {
  const { username, password, name, email, url } = req.body;
	//중복아이디 있는지 검사
  const found = await userRepository.findByUsername(username);
  if (found) {
    return res.status(409).json({ message: `${username} already exists` });
  }
	//중복아이디 없으면 디비에 암호화해서 등록
  const hashed = await bcrypt.hash(password, bcryptSaltRounds);
  const userId = await userRepository.createUser({
    username,
    password: hashed,
    name,
    email,
    url,
  });
	//jwt토큰만들어서 리턴(createJwtToken함수는 아래에 정의)
  const token = createJwtToken(userId);
  res.status(201).json({ token, username });
}

//로그인
export async function login(req, res) {
  const { username, password } = req.body;
	//해당 사용자가 db에 존재하는지 먼저 확인
  const user = await userRepository.findByUsername(username);
  if (!user) {
    return res.status(401).json({ message: 'Invalid user or password' });
  }
	//비번검사(비크립트로)
  const isValidPassword = await bcrypt.compare(password, user.password);
  if (!isValidPassword) {
    return res.status(401).json({ message: 'Invalid user or password' });
  }
	//jwt토큰만들어서 리턴
  const token = createJwtToken(user.id);
  res.status(200).json({ token, username });
}

function createJwtToken(id) {
  return jwt.sign({ id }, jwtSecretKey, { expiresIn: jwtExpiresInDays });
}

export async function me(req, res, next) {
  const user = await userRepository.findById(req.userId);
  if (!user) {
    return res.status(404).json({ message: 'User not found' });
  }
  res.status(200).json({ token: req.token, username: user.username });
}
```

# Auth 미들웨어

헤더관련 참고(Authroization, Bearer 쓰는지):

[https://developer.mozilla.org/en-US/docs/Web/HTTP/Authentication](https://developer.mozilla.org/en-US/docs/Web/HTTP/Authentication)

로그인한 사용자가 내 토큰이 아직 유효한지 확인할 때가 있음

GET [http://localhost:8080/auth/me](http://localhost:8080/auth/me)  

헤더:

키: Authorizaton, 벨류: Bearer sdfoasfasdofmasdo(토큰복붙) 

위 정보로 클라이언트가 보낼거임.

middleware/auth.js 

isAuth의 목적: 

헤더에 'Authorization'이 있는지, 있다면 jwt가 있는지, jwt 검증, jwt주인유저 실제로 있는지, 

```jsx
import jwt from 'jsonwebtoken';
import * as userRepository from '../data/auth.js';

const AUTH_ERROR = { message: 'Authentication Error' };

export const isAuth = async (req, res, next) => {
  const authHeader = req.get('Authorization');
  if (!(authHeader && authHeader.startsWith('Bearer '))) {
    return res.status(401).json(AUTH_ERROR);
  }

  const token = authHeader.split(' ')[1];//Bearer abcdabcdabcd 형식이므로
  // TODO: Make it secure!
  jwt.verify(
    token,
    'F2dN7x8HVzBWaQuEEDnhsvHXRWqAR63z',
    async (error, decoded) => {
      if (error) {
        return res.status(401).json(AUTH_ERROR);
      }
      const user = await userRepository.findById(decoded.id);
      if (!user) {
        return res.status(401).json(AUTH_ERROR);
      }
      req.userId = user.id; // req.customData
      next();
    }
  );
};
```

- 추가활용용도: isAuth미들웨어로 tweet을 로그인한 사람만 쓸 수있도록 해줄수 있음

이런식으로 

```jsx
router.get('/', isAuth, tweetController.getTweets);

// GET /tweets/:id
router.get('/:id', isAuth, tweetController.getTweet);
```

# Authorization

자기가 쓴글만 고칠 수 있도록!

controller/tweets.js 

```jsx
export async function updateTweet(req, res, next) {
  const id = req.params.id;
  const text = req.body.text;
  const tweet = await tweetRepository.getById(id);
  //쓴게 없는데 뭘고치니?
  if (!tweet) {
    return res.status(404).json({ message: `Tweet not found: ${id}` });
  }
  //너가 쓴글 아니야!
  if (tweet.userId !== req.userId) {
    return res.sendStatus(403);
  }
  //위 경우가 다 아니라면 업데이트해서 리턴쓰
  const updated = await tweetRepository.update(id, text);
  res.status(200).json(updated);
}

export async function deleteTweet(req, res, next) {
  const id = req.params.id;
  const tweet = await tweetRepository.getById(id);
  if (!tweet) {
    return res.status(404).json({ message: `Tweet not found: ${id}` });
  }
  if (tweet.userId !== req.userId) {
    return res.sendStatus(403);
  }
  await tweetRepository.remove(id);
  res.sendStatus(204);
}
```
