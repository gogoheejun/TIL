user.entity에 속성추가

eager를 true로 주면 user를 부를때 task까지 매핑 다 해서 가져옴. 한곳이 true면 반대쪽은 false여야 함

```jsx
@Entity()
export class User{
  @PrimaryGeneratedColumn('uuid')
 id: string;

 @Column({unique:true})
 username: string;

 @Column()
 password: string;

 @OneToMany(_type=>Task, task=>task.user, {eager:true})
 task: Task[];
}
```

task.entity에도 추가

```jsx
@Entity()
export class Task{
  @PrimaryGeneratedColumn('uuid')//uuid안하면 기본으론 123순서임
  id: string;

  @Column()
  title:string;

  @Column()
  description:string;

  @Column()
  status:TaskStatus;

  @ManyToOne(_type=>User, user=>user.tasks, {eager:false})
  user:User;
}
```

# Make Users Own Tasks

createTask 할때 user정보도 같이 넣는것임

이전에 get-user.decorator만들었던거 기억나지. 데코레이터가 어노테이션임

```jsx
import { createParamDecorator, ExecutionContext } from '@nestjs/common';
import { User } from './user.entity';

export const GetUser = createParamDecorator(
  (_data,ctx: ExecutionContext):User =>{
  const req = ctx.switchToHttp().getRequest();
  return req.user;
})
```

tasks.controller에서 데코레이터 활용하자. 인자에 User넣어줌

```jsx
@Post()
  createTask(
    @Body() CreateTaskDto:CreateTaskDto,
    @GetUser() user:User,
    ):Promise<Task>{ //Body를 CreateTaskDto에다가 담음
    return this.tasksService.createTask(CreateTaskDto,user);
  }
```

서비스도 User넣고

```jsx
createTask(createTaskDto:CreateTaskDto, user:User):Promise<Task>{
    return this.taskRepository.createTask(createTaskDto, user);
  }
```

리파지토리도 User넣음. 글구 task create할때 user를 넣어줌

```jsx
async createTask(createTaskDto: CreateTaskDto, user:User): Promise<Task>{
    const{title,description} = createTaskDto;

    const task = this.create({
      title,
      description,
      status:TaskStatus.OPEN,
      user,
    });

    await this.save(task);
    return task;
  }
```

그럼 실제로 디비에 userId가 저장됨

![Untitled](https://user-images.githubusercontent.com/78577071/130719832-2c76966b-6363-4a53-82d3-fae49b5bf8d4.png)

근데 문제점이 리턴으로 task를 리턴해줄때, 아래처럼 비번같은 정보까지 다 보여줌

```jsx
{
    "title": "cook dinner",
    "description": "im hungry!",
    "status": "OPEN",
    "user": {
        "id": "3a544560-d805-4beb-a768-ab88971343b3",
        "username": "user1",
        "password": "$2b$10$f1eaHzDUQU3Z6VYgVSWNH.TBCaalIPgZavp1UxBnqV5scHk.LLuxu",
        "tasks": []
    },
    "id": "249578d1-30e5-4c5c-8ede-207d1038bd6b"
}
```

위 리턴값에서 user안보이게 하기

task.entity.ts에 @Exclude({toPlainOnly:true}) 추가

```jsx
@ManyToOne(_type=>User, user=>user.tasks, {eager:false})
  @Exclude({toPlainOnly:true})
  user:User;
}
```

글구 src에 transform.interceptor파일추가. 이거 걍 복붙함.(강사색히도 이렇게함)

```jsx
import {
  NestInterceptor,
  ExecutionContext,
  Injectable,
  CallHandler,
} from '@nestjs/common';
import { classToPlain } from 'class-transformer';
import { map } from 'rxjs/operators';

@Injectable()
export class TransformInterceptor implements NestInterceptor {
  intercept(context: ExecutionContext, next: CallHandler<any>) {
    return next.handle().pipe(map(data => classToPlain(data)));
  }
}
```

그럼 이제 포스트맨으로 create a task하면 결과에 민감한 정보 가릴수있게됨

```jsx
{
    "title": "cook dinner",
    "description": "im hungry!",
    "status": "OPEN",
    "id": "9d351e59-8727-49e5-adf4-20035240889f"
}
```

# 현재 user가 쓴 글만 보도록 하기(Restrict)

위에서 했듯이, GetUser 데코레이터 추가하고, 인자에 user받기

```jsx
@Get()
  getTasks(
    @Query() filterDto: GetTasksFilterDto, 
    @GetUser() user:User,
    ):Promise<Task[]>{
 
    return this.tasksService.getTasks(filterDto,user);
  }
```

서비스도

```jsx
getTasks(filterDto: GetTasksFilterDto, user:User):Promise<Task[]>{
    return this.taskRepository.getTasks(filterDto,user);
  }
```

레파지토리도

근데 ㅈㄴ쉬운게 query.where()넣으면 user꺼만 갖고오게됨 개쉽

```jsx
async getTasks(filterDto:GetTasksFilterDto, user:User):Promise<Task[]>{
    const {status,search} = filterDto;
    const query = this.createQueryBuilder('task');
    query.where({user});//현재 유저꺼만 가져오도록

    if(status){
      query.andWhere('task.status= :status',{status});
    }
    if(search){
      query.andWhere(
        `LOWER(task.title) LIKE LOWER(:search) OR LOWER(task.description) LIKE LOWER(:search)`,//다 소문자로 변환
        {search: `%${search}%`},
      );
    }
    const tasks = await query.getMany();
    return tasks;
  }
```

다른 api들도 이렇게 리스트릭트하면됨
