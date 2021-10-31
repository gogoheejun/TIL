시작에 앞서, Authentication과 Authorization은 다른 개념임을 인지하자.

Authentication는 클라이언트가 자기가 누구라고 주장하는 바에 대해 검증하는 것이고, Authorization은 클라이언트의 identity나 permission level에 다라 특정시스템으로 배정하는것임

# Setting up AuthModule

cmd로 

```jsx
PS D:\13_nestjs\00_udemy\nestjs-task-management> nest g module auth
```

```jsx
PS D:\13_nestjs\00_udemy\nestjs-task-management> nest g service auth --no-spec
PS D:\13_nestjs\00_udemy\nestjs-task-management> nest g controller auth --no-spec
```

이렇게 하면, app.modules.ts에 AuthModule추가되고

```jsx
@Module({
  imports: [
    TasksModule,
    TypeOrmModule.forRoot({
      type: 'postgres',
      host: 'localhost',
      port: 5432,
      username:'postgres',
      password:'postgres',
      database:'task-management',
      autoLoadEntities:true,
      synchronize:true,
    }),
    AuthModule
  ],
})
export class AppModule {}
```

auth폴더에 auth.controller, auth.module, auth.service 생김

- tasks할때와 마찬가지로 기본 구성파일 설정

dto만들고

```jsx
export class AuthCredentialsDto{
  username: string;
  password: string;
}
```

user.entity

```jsx
import { Column, Entity, PrimaryGeneratedColumn } from 'typeorm';

@Entity()
export class User{
  @PrimaryGeneratedColumn('uuid')
 id: string;

 @Column()
 username: string;

 @Column()
 password: string;
}
```

user.repository

```jsx
import { EntityRepository, Repository } from 'typeorm';
import { User } from './user.entity';

@EntityRepository(User)
export class UsersRePository extends Repository<User>{

}
```

auth.module

```jsx
@Module({
  imports:[TypeOrmModule.forFeature([UsersRePository])],
  providers: [AuthService],
  controllers: [AuthController],
})
export class AuthModule {}
```

# Sign up 기본api 만들기

repository

```jsx
@EntityRepository(User)
export class UsersRePository extends Repository<User>{
  async createUser(authCredentialsDto: AuthCredentialsDto):Promise<void>{
    const {username, password} = authCredentialsDto;

    const user = this.create({username, password});
    await this.save(user);
  }
}
```

 

서비스

```jsx
@Injectable()
export class AuthService {
  constructor(
    @InjectRepository(UsersRePository)
    private userRepository: UsersRePository,
  ){}

  async signUp(authCredentialsDto:AuthCredentialsDto): Promise<void>{
    return this.userRepository.createUser(authCredentialsDto)
  }
}
```

controller

```jsx
@Controller('auth')
export class AuthController {
  constructor(private authService:AuthService){}

  @Post('/signup')
  signUp(@Body() authCredentialsDto:AuthCredentialsDto):Promise<void>{
    return this.authService.signUp(authCredentialsDto);
  }
}
```

# dto 제약걸기

매치 첫번째 인자에 정규표현식으로 표현, 두번째 인자에는 메시지

- Passwords will contain at least 1 upper case letter
- Passwords will contain at least 1 lower case letter
- Passwords will contain at least 1 number or special character
- There is **no** length validation (min, max) in this regex!

```jsx
export class AuthCredentialsDto{
  @IsString()
  @MinLength(8)
  @MaxLength(20)
  username: string;

  @IsString()
  @MinLength(8)
  @MaxLength(32)
  @Matches(/((?=.*\d)|(?=.*\W+))(?![.\n])(?=.*[A-Z])(?=.*[a-z]).*$/,
    {message:'password is too weak'
  })
  password: string;
}
```

# signup 시 duplicated ID 처리

엔터티에 @Column({unique:true}) 제약조건 붙여줌

```jsx
@Entity()
export class User{
  @PrimaryGeneratedColumn('uuid')
 id: string;

 @Column({unique:true})
 username: string;

 @Column()
 password: string;
}
```

근데 이렇게만 하면 중복아이디로 가입할때 500에러가 뜸 이걸 원치 않음.

repository에 (service에 하는 사람도 있는데 이사람은 repository에 하는게 심플하대)

try/catch 해줌. 이케하면 중복아이디 가입시에 409에러 뜨게 되고 메시지도 아래처럼 나감

{

"statusCode": 409,

"message": "username alreadly exists",

"error": "Conflict"

}

```jsx
@EntityRepository(User)
export class UsersRePository extends Repository<User>{
  async createUser(authCredentialsDto: AuthCredentialsDto):Promise<void>{
    const {username, password} = authCredentialsDto;

    const user = this.create({username, password});
    try{
      await this.save(user);
    }catch(error){
      if(error.code === '23505'){//duplicate username일때 23505라고 db에서 말해줌
        throw new ConflictException('username alreadly exists');
      }else{
        throw new InternalServerErrorException();
      }
    }
  }
}
```

# 비번 안전하게 저장하기(bcrypt)

먼저 알아야 할게, 내가 친 비번은 해시 알고리즘을 통해 해시된 채로 db에 저장됨. 그리고 담에 로그인할때 어케 검사하냐? ~~그 저장된 해시를 다시 해독하는게 아니라~~, 로그인할때 입력값을 해시로 바꿔서 디비에 있는 해시랑 비교하는 것임.

근데 문제가 있음: 해커들이 db가져가면 **rainbow테이블**(ㅈㄹ많은 비번=해시비번 적어놓은거) 비교해가며 풀수있음.

해결책: prefix_123456 이런식으로 **prefix(salt)**를 붙이는것임→레인보우테이블공격 거의 불가

repository에 가서 다음처럼 salt만들고 새로운 비번 만들어서 디비에 저장하면됨

```jsx
@EntityRepository(User)
export class UsersRePository extends Repository<User>{
  async createUser(authCredentialsDto: AuthCredentialsDto):Promise<void>{
    const {username, password} = authCredentialsDto;

    //hash
    const salt = await bcryprt.genSalt();
    const hashedPassword = await bcryprt.hash(password,salt);
    const user = this.create({username, password:hashedPassword});
...
...
```

똑같은 비번으로 저장되어도 전혀다른 salt가 생기고 전혀다른 해시로 저장됨.

위처럼 하는게 최선의 방법이라고 함.

# Sign in

service

 bcrypt.compare()활용함

```jsx
async signIn(authCredentialsDto:AuthCredentialsDto): Promise<string>{
    const {username, password} = authCredentialsDto;
    const user = await this.userRepository.findOne({username}); //먼저 그 유저가 있는지 찾음
	//유저만 먼저 검사하지 않은건 유저가 있는지 없는지에 대한 정보를 가져갈수도 잇기 때문

    if(user && (await bcrypt.compare(password, user.password))){
      return 'success';
    }else{
      throw new UnauthorizedException('Please check your login credentials');
    }
  }
```

controller

```jsx
@Post('/signin')
  signIn(@Body() authCredentialsDto:AuthCredentialsDto):Promise<string>{
    return this.authService.signIn(authCredentialsDto);
  }
```

포스트맨으로 해보면, 로그인성공하면 success뜨고 아니면 아래처럼 401뜸

{

"statusCode": 401,

"message": "Please check your login credentials",

"error": "Unauthorized"

}

# JWT

구조:
![Untitled](https://user-images.githubusercontent.com/78577071/130719689-a889e91d-fcdb-4c92-8b86-787545f37342.png)


signature는 header와 payloade로부터 발생된다는 것을 알아두삼.

다음을 터미널에서 설치

```jsx
yarn add @nestjs/jwt @nestjs/passport passport passport-jwt
```

auth.module 들어가서 import에 모듈추가

```jsx
@Module({
  imports:[
    PassportModule.register({defaultStrategy:'jwt'}),
    JwtModule.register({
      secret:'topSecret51',
      signOptions:{
        expiresIn:3600,//1hour
      },
    }),
    TypeOrmModule.forFeature([UsersRePository])],
  providers: [AuthService],
  controllers: [AuthController],
})
export class AuthModule {}
```

저 JwtModule 덕분에 우리는 토큰에 사인을 할 수 있음

# jwt 토큰에 사인하기(Authentication)

- payload 인터페이스 만듦. 자주 쓸거니깐

```jsx
export interface JwtPayload{
  username: string;
}
```

- 서비스

dto로 받아온 아디비번을 확인하고 있으면, payload를 만들고(이때 비번은 안넣음), 그 payload에 사인을 해서 토큰을 만듦.

```jsx
export class AuthService {
  constructor(
    @InjectRepository(UsersRePository)
    private userRepository: UsersRePository,
    private jwtSerive: JwtService, //jwtservice 추가
  ){}
...
...//로그인할때 사인해줘야 하니까 여기에 씀
async signIn(authCredentialsDto:AuthCredentialsDto): Promise<{accessToken: string}>{
    const {username, password} = authCredentialsDto;
    const user = await this.userRepository.findOne({username});

    if(user && (await bcrypt.compare(password, user.password))){
      const payload:JwtPayload = {username}; //비번은 토큰에 넣지 않음.
      const accessToken: string = await this.jwtSerive.sign(payload);
      return {accessToken};
    }else{
      throw new UnauthorizedException('Please check your login credentials');
    }
  }
```
![Untitled2](https://user-images.githubusercontent.com/78577071/130719697-99b0b177-0819-4620-bfe4-e5963e310a98.png)

- 컨트롤러

    ```jsx
    @Post('/signin')
      signIn(@Body() authCredentialsDto:AuthCredentialsDto):Promise<{accessToken: string}>{
        return this.authService.signIn(authCredentialsDto);
      }
    ```

    리턴으로 토큰을 주면, 유저는 저 토큰을 어딘가에 저장해야 나중에 서버에 다시 보낼텐데, 보통 web browser에다가 저장해놓음.

    # jwt Validation

    - jwt.strategy 만들기

    it contains the logic for authorizing the user. 이 로직을 이용해서 토큰을 validate할것임

    아래 명렁어로 패키지추가

    jwt를 타입스크립트에서 사용하기 쉽게 해주는거래

    ```jsx
    yarn add @types/passport-jwt
    ```

    auth에 새 파일 만듦

    jwt.strategy.ts

    ```jsx
    import { Injectable, UnauthorizedException } from '@nestjs/common';
    import { PassportStrategy } from '@nestjs/passport';
    import { InjectRepository } from '@nestjs/typeorm';
    import { ExtractJwt, Strategy } from 'passport-jwt';
    import { JwtPayload } from './jwt-payload.interface';
    import { User } from './user.entity';
    import { UsersRePository } from './users.repository';

    @Injectable()
    export class JwtStrategy extends PassportStrategy(Strategy){
      constructor(
        @InjectRepository(UsersRePository)
        private userRepository: UsersRePository,
      ){
        super({
          topSecret51:'topSecret51', //auth.modules.ts에 쓴거랑 똑같은걸로
          jwtFromRequest: ExtractJwt.fromAuthHeaderAsBearerToken(), //이게 가장 커먼한거래
        })
      }

      async validate(payload:JwtPayload): Promise<User>{
        const {username} = payload;
        const user:User = await this.userRepository.findOne({username});

        if(!user){
          throw new UnauthorizedException();
        }

        return user;
      }
    }
    ```

    auth.module에도 추가

    ```jsx
    exports: [JwtStrategy, PassportModule],//이 auth모듈을 임포트하면 쓸 수 있도록
    })
    ```

    - get-user.decorator

    사용자의 request에서 user정보만 가져오는 애 따로 만들어줌. user에 토큰사인이 있기때문.

    request할 때  authorization에 token딸려보내서 받은 api에서 req를 콘솔로그찍어보면

    다음 정보가 req에 추가되서 보임

    ```jsx
    user: User{
    	id:'sdfsadfsdfsdafasdf',
    	username:'heejun',
    	password:'asdfasfsdfasfdsafdasdfdasfsaf',//암호화된 비번
    }
    ```

    ```jsx
    import { createParamDecorator, ExecutionContext } from '@nestjs/common';
    import { User } from './user.entity';

    export const GetUser = createParamDecorator(
      (_data,ctx: ExecutionContext):User =>{
      const req = ctx.switchToHttp().getRequest();
      return req.user;
    })
    ```

    # guard적용하기

    task.module에 가서 import에 AuthModule 추가

    ```jsx
    @Module({
      imports:[TypeOrmModule.forFeature([TaskRepository]),AuthModule],
      controllers: [TasksController],
      providers: [TasksService],
    })
    ```

    task.controller에 가서

    아래처럼 하면 전체 라우트 보호됨

    ```jsx
    @Controller('tasks')
    @UseGuards(AuthGuard())
    export class TasksController {
    ```

    포스트맨에서 테스트:

    authorization에 bearer token 타입으로 토큰 안보내면 401 에러 리턴받음. 

    sign in해서 리턴받은 토큰 복사해서 bearer token에 넣고 다시 요청하면 201 성공!
