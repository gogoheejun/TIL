# 도커

- 도커 깔렸나 확인

cmd에 

```jsx
docker
```

- run하기

```jsx
 docker run --name postgres-nest -p 5432:5432 -e POSTGRES_PASSWORD=postgres -d postgres
```

run : container를 run해라

--name postgress-nest :  postgress-nest으로 이름만들어라

-p 5432:5432 : 포트 연결해주는거 5432번포트 접속하면 도커의 5432로 간다

-e POSTGRESS_PASSWORD=postgress : 환경설정으로 비번설정

-d : detached mode로 run해라. 터미널 닫아도 계속 run

postgress: 클라우드에 실제로 저장된 컨테이너이름

- 근데 에러남;;;해겨

WSL 2 installation is incomplete 에러가 생김

해결방법:

1. 파워셀 관리자권한으로 실행
2. dism.exe /online /enable-feature /featurename:Microsoft-Windows-Subsystem-Linux /all /norestart 입력(리눅스 서브시스템 활성명령어)
3. dism.exe /online /enable-feature /featurename:VirtualMachinePlatform /all /norestart 입력(가상머신플랫폼 기능 활성화)
4. x64 머신용 최신 WSL2 Linux 커널 업데이트 패키지 다운로드, 설치([wslstorestorage.blob.core.windows.net/wslblob/wsl_update_x64.msi](http://wslstorestorage.blob.core.windows.net/wslblob/wsl_update_x64.msi))
5. 도커 재시작하면 성공!

- 이제 위에 docker run하는 명령어 다시 치면 해시같은거 뜸 그럼성공

docker container ls라 쳐서 나오면 잘되는거

- 간단한 명령어

```jsx
docker container stop postres-nest  //컨테이너 스탑. start는 start쓰면됨
docker container rm postgres-nest //삭제
```

# pgAdmin

이건 영상보면서 따라함 걍

![Untitled](https://user-images.githubusercontent.com/78577071/130481392-b5e21b4f-ebd2-48e4-abb5-57a5daf2030d.png)

# ORM

Object-Relational Mapping is a technique that lets you query and manipulate data from a databse, using an object-oriented paradigm

- pros

-writing the data model in one place- easier to maintain. Less repetition

-lots of things done automatically -database handling, data types, relations etc.

-no need to write SQL syntax

-databse abstraction. you can change the databse type whenever you wish

-leverages OOP

- cons

-have to learn it

-performance is alrgiht, but easy to neglect

-easy to forget what's happening behind

# TypeORM

it is an ORM library that can run in node.js and Typescript.

- example

retrieving all tasks owned by 'Ashely' and are of status "DONE"

-typeORM

```jsx
const task = await Task.find({status:'DONE', user:'Ashley'});
```

-pure js

```jsx
let tasks;
	db.query('SELECT * FROM tasks WHERE status = "DONE" AND user = "Ashley"',(err, result)=>{
	if(err){
		throw new Error('Could not retrieve tasks!');
	}
	tasks = result.rows;
});
```

# DB Connection

- typeOrm 모듈추가

vsc 프로젝트 터미널에

```jsx
yarn add typeorm @nestjs/typeorm pg
```

app.modules가서 라이브러리추가. 자세한 설명은 다시 공부해야할듯

```jsx
import { Module } from '@nestjs/common';
import { TasksModule } from './tasks/tasks.module';
import { TypeOrmModule } from '@nestjs/typeorm';
@Module({
  imports: [
    TasksModule,
    TypeOrmModule.forRoot({
      type: 'postgres',
      host: 'localhost',
      port:5432,
      username:'postgres',
      password:'postgres',
      database:'task-management',
      autoLoadEntities:true,
      synchronize:true,
    })
  ],
})
export class AppModule {}
```

- Entity 추가

tasks파일에  task.entity.ts 이름으로 추가. 이름 컨벤션지켜야함

```jsx
import { Column, Entity, PrimaryGeneratedColumn } from 'typeorm';
import { TaskStatus } from './task.model';

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
}
```

- Create a Tasks Repository

장기적으로 리파지토리를 따로 만들어 관리하는 게 훨씬 도움되는 습관이다

task.repository.ts

```jsx
import { EntityRepository, Repository } from 'typeorm';
import { Task } from './task.entity';

@EntityRepository(Task)
export class TaskRepository extends Repository<Task>{

}
```

만든 리파지토리를 tasks모듈 어디서나 쓸수 있도록 **dependency injection** 함

tasks.modules.ts(모듈 정의하는곳)에 가서 import프로포티를 넣고 다음처럼 함

```jsx
import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { TasksController } from './tasks.controller';
import { TaskRepository } from './tasks.repository';
import { TasksService } from './tasks.service';

@Module({
  imports:[TypeOrmModule.forFeature([TaskRepository])],
  controllers: [TasksController],
  providers: [TasksService],
})
export class TasksModule {}
```

-참고로 app.modules에서는  TypeOrmModule.forRoot라 하고, 그 아래(sub-module)인 tasks.modules에선 TypeOrmModule.forFeature라 했는데, 보통 이런 패턴을 씀

- modle 수정

task.model.ts 필요없음(entity.ts만들어놨기때문) 대신 enum만 필요

이름  task-status.enum.ts로 수정하고 Task interface는 삭제

```jsx
~~export interface Task{
	id:string,
	title:string;
	description: string;
	status: TaskStatus;
}~~

export enum TaskStatus{
  OPEN = 'OPEN',
  IN_PROGRESS = 'IN_PROGRESS',
  DOND = 'DONE',
}
```

# service, controller 재작성(repository 활용)

- getTaskById

**tasks.service.ts** 

생성자에다가 의존성주입하고.

어싱크니까 promise로 반환하는거임.

```jsx
@Injectable()
export class TasksService {
  constructor(
    @InjectRepository(TaskRepository)
    private taskRepository:TaskRepository
  ){}

....중략
	async getTaskById(id:string): Promise<Task>{
	    const found = await this.taskRepository.findOne(id);
	    if(!found){
	      throw new NotFoundException(`Task with ID "${id}" not found`);
	    }
	    return found;
	  }
	
	//------------- 아래는 기존코드
	  // getTaskById(id:string): Task{
	  //   const found = this.tasks.find((task)=>task.id === id);
	
	  //   if(!found){
	  //     throw new NotFoundException(`Task with ID "${id}" not found`);
	  //   }
	  //   return found;
	  // }
```

controller.ts

```jsx
@Controller('tasks')
export class TasksController {
  constructor(private tasksService: TasksService){}

	@Get('/:id')
  getTaskById(@Param('id') id:string):Promise<Task>{
    return this.tasksService.getTaskById(id);
  }

//-----------------기존코드
// @Get('/:id')
  // getTaskById(@Param('id') id: string):Task{ //파라미터를 id에다가 담음
  //   return this.tasksService.getTaskById(id);
  // }
```

- createTask

repository에 함수추가

```jsx
@EntityRepository(Task)
export class TaskRepository extends Repository<Task>{
  async createTask(createTaskDto: CreateTaskDto): Promise<Task>{
    const{title,description} = createTaskDto;

    const task = this.create({
      title,
      description,
      status:TaskStatus.OPEN,
    });

    await this.save(task);
    return task;
  }
}
```

Service에서 사용

```jsx
createTask(createTaskDto:CreateTaskDto):Promise<Task>{
    return this.taskRepository.createTask(createTaskDto);
  }
```

컨트롤러에서 서비스 사용

```jsx
@Post()
  createTask(@Body() CreateTaskDto:CreateTaskDto):Promise<Task>{ //Body를 CreateTaskDto에다가 담음
    return this.tasksService.createTask(CreateTaskDto);
  }
```

- delete by id

delete와 remove 둘다 쓸 수 있음

```jsx
//delete
repository.delete(1); 
이렇게 걍 id나 객체조건 넣으면 해당되는거 바로 삭제. (바로 삭제하기에 그 엔터티가 db에 존재하는지 체크는 안함)

//remove
repository.remove(user); 객체를 넣어야함.즉 그 객체가 이미 존재해야 삭제할수있게 됨
```

→ delete이 더 간단하기에 delete쓰기로 함

service

id없는애를 삭제한다고 요청오면 404를 리턴시킴. throw저거 안하면 200나감ㅋㅋ

```jsx
async deleteTask(id:string): Promise<void>{
    const result = await this.taskRepository.delete(id);
    
//result만 콘솔해보면 1개 삭제시: DeleteResult { raw: [], affected: 1 }처럼 뜸.
    if(result.affected === 0){
      throw new NotFoundException(`Task with ID"${id}" not found`);
    }
  }
```

controller

```jsx
@Delete('/:id')
  deleteTask(@Param('id') id: string):Promise<void>{
    return this.tasksService.deleteTask(id);
  }
```

- update by id

service

```jsx
async updateTaskStatus(id:string, status:TaskStatus): Promise<Task>{
    const task = await this.getTaskById(id);

    task.status = status;
    await this.taskRepository.save(task);
    return task;
  }
```

controller

```jsx
@Patch('/:id/status')
  updateTaskStatus(
    @Param('id') id:string,
    @Body() updateTaskStatus:UpdateTaskStatusDto,
  ):Promise<Task>{
    const {status} = updateTaskStatus
    return this.tasksService.updateTaskStatus(id,status);
  }
```

- 필터검색

repository

*createQueryBuilder, andWhere, 등등 새로운것들 나옴*

```jsx
async getTasks(filterDto:GetTasksFilterDto):Promise<Task[]>{
    const {status,search} = filterDto;
    const query = this.createQueryBuilder('task');

    if(status){
      query.andWhere('task.status= :status',{status});
    }
    if(search){
      query.andWhere(//Lower는 소문자로 변환, LIKE는 포함되는 애들,
        `LOWER(task.title) LIKE LOWER(:search) OR LOWER(task.description) LIKE LOWER(:search)`,
        {search: `%${search}%`},
      );
    }
    const tasks = await query.getMany();
    return tasks;
  }
```

service

```jsx
getTasks(filterDto: GetTasksFilterDto):Promise<Task[]>{
    return this.taskRepository.getTasks(filterDto);
  }
```

controller

```jsx
@Get()
  getTasks(@Query() filterDto: GetTasksFilterDto):Promise<Task[]>{
    //if we have any filters defined, call tasksService.getTasksWithFilters
    //otherwise, just get all tasks
    return this.tasksService.getTasks(filterDto);
  }
```
