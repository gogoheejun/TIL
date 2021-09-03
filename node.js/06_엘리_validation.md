# Validation

유효성검사는 앞단에서 하면 할수록 좋음. 서버 돌아가는 건 모두 비용이므로

- 라이브러리 다운

```jsx
npm i express-validator
```

- 사용방법:

sanitization도 동시에 해줌. 공백문자제거, 대소문자 일관성있게 하기 등등 데이터가 일관성있게 저장될 수 있도록 처리해주는 것임.

app.post('/:id',validateTweet, validateAuth, 컨트롤러 ) 이렇게 가운데에 껴놓는거임

```jsx
import express from 'express';
import { body, param, validationResult } from 'express-validator';

const app = express();
app.use(express.json());

//여기서 벨리데이션
const validate = (req, res, next) => {
  const errors = validationResult(req);
  if (errors.isEmpty()) {
    return next(); //에러없으면 패스
  }
  return res.status(400).json({ message: errors.array()[0].msg }); //에러들 중 첫번째 에러만 보여줌
};

app.post(
  '/users',
  [
    body('name').trim().isLength({ min: 2 }).withMessage('이름은 두글자 이상!'), //sanitization:공백문자제거
    body('age').isInt().withMessage('숫자를 입력해'),
    body('email').isEmail().withMessage('이메일 입력해요').normalizeEmail(),
    body('job.name').notEmpty(),
    validate,
  ],
  (req, res, next) => {
    console.log(req.body);
    res.sendStatus(201);
  }
);

app.get(
  '/:email',
  [param('email').isEmail().withMessage('이메일 입력해요'), validate],
  (req, res, next) => {
    res.send('💌');
  }
);

app.listen(8080);
```
