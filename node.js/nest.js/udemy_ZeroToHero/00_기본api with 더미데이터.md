![Untitled](https://user-images.githubusercontent.com/78577071/130404728-a1e50c81-1b20-4c26-b701-c286c1065998.png)

- API   
만들 api들   
GET- tasks/   
GET- tasks/:id    
POST- tasks/    
DELETE- tasks/:id   
PATCH- task/:id/status    
POST- auth/signup   
POST- auth/signin   

# Creating a Task Module

cli로 다음처럼 설치. 

```jsx
nest g module tasks
```

→ 결과:  taks폴더에 tasks.module.ts 파일 하나 만들고, app.modules.ts에 임포트시킴

CREATE src/tasks/tasks.module.ts (82 bytes)
UPDATE src/app.module.ts (159 bytes)

# Controllers

![Untitled 1](https://user-images.githubusercontent.com/78577071/130404806-96d1344a-1275-44c4-a518-19a6497a076e.png)

cli로 설치

```jsx
nest g controller tasks --no-spec  //스펙파일 원래 같이 설치되는데 얘 빼고(원래 중요하지만 현재는 제외)
```

결과:

CREATE src/tasks/tasks.controller.ts (99 bytes)
UPDATE src/tasks/tasks.module.ts (170 bytes)

# Providers

can be injected into constructors if decorated as an @Injectable, via dependency injection

## Services

defined as providers. Not all providers are services

singleton when wrapped with @Injectable() and provided to a module

it is the main source of business logic. For example, a service will be called from a controller to vialidate data,  create an item in the databse and return a response.
![Untitled 2](https://user-images.githubusercontent.com/78577071/130404837-bbee99ec-8b8d-4906-ae4a-cfd10ab43710.png)

![Untitled 3](https://user-images.githubusercontent.com/78577071/130404849-729c5364-00cf-4ce1-91d7-07a90521ce04.png)


## Dependency Injection in NestJs

we define the dependencies in the constructor of the class. NesjJS will take care of the injetion for the injection for us, and it will then be availiable as a class property

![Untitled 4](https://user-images.githubusercontent.com/78577071/130404861-284ac332-b8e2-49c4-a165-b6d42989802d.png)

# Creating Service

CREATE src/tasks/tasks.service.ts (89 bytes)
UPDATE src/tasks/tasks.module.ts (248 bytes)

tasks.module

```jsx
import { Module } from '@nestjs/common';
import { TasksController } from './tasks.controller';
import { TasksService } from './tasks.service'; //이거 추가

@Module({
  controllers: [TasksController],
  providers: [TasksService], //얘도 추가. 이 서비스를 controller에 inject할수있도록함
})
export class TasksModule {}
```

tasks.controller

```jsx
@Controller('tasks')
export class TasksController {
  tasksService:TasksService;
  constructor(tasksService: TasksService){
    this.tasksService = tasksService;
  }
  helloWorld(){
    this.tasksService.doSomething();
  }
}
//////////////////////////
//위처럼 해도 되지만
//constructor()안에다가 private이란 접근제한자를 붙여줌으로써 다 생략가능.

import { Controller } from '@nestjs/common';
import { TasksService } from './tasks.service';

@Controller('tasks')
export class TasksController {

  constructor(private tasksService: TasksService){}
  
  helloWorld(){
    this.tasksService.doSomething();
  }
}
```

# Controller와 service 분리

tasks.controllers

컨트롤러는 단순히 receive a request하고 전달하고 return response하는것 뿐임.

```jsx
import { Controller, Get } from '@nestjs/common';
import { TasksService } from './tasks.service';

@Controller('tasks')
export class TasksController {
  constructor(private tasksService: TasksService){} //서비스를 inject시킴
 
  @Get()
  getAllTasks(){
    return this.tasksService.getAllTasks();
  }
}
```

tasks.service

여기에 비즈니스로직

```jsx
import { Injectable } from '@nestjs/common';

@Injectable() //싱글톤으로 만들어줌
export class TasksService {
  private tasks = [];

  getAllTasks(){
    return this.tasks;
  }
}
```

 

# task model 만들기

tasks/task.model

```jsx
export interface Task{
  id: string;
  title: string;
  description:string;
  status: TaskStatus;
}

enum TaskStatus{
  OPEN = 'OPEN',
  IN_PROGRESS = 'IN_PROGRESS',
  DOND = 'DONE',
}
```

만든거 써먹자. 서비스랑 컨트롤러에다가

service

```jsx
@Injectable()
export class TasksService {
  private tasks: Task[] = [];

  getAllTasks(): Task[]{
    return this.tasks;
  }
}
```

controller

```jsx
@Controller('tasks')
export class TasksController {
  constructor(private tasksService: TasksService){}

  @Get()
  getAllTasks():Task[]{
    return this.tasksService.getAllTasks();
  }
}
```

# CreateTask만들기

id값 추가할때 유일한 아이디값 넣도록 라이브러리 추가

```jsx
yarn add uuid
```

서비스에서 새로운 비즈니스로직 구현하는 함수넣고

```jsx
@Injectable()
export class TasksService {
...
		createTask(title:string, description:string):Task {
		    const task: Task = {
		      id: uuid(),
		      title,
		      description,
		      status: TaskStatus.OPEN,
		    };
		
		    //만들었으니 기존 배열에 넣기
		    this.tasks.push(task);
		    return task;
		  }
```

컨트롤러에 추가. 

[http://localhost:3000/tasks](http://localhost:3000/tasks) 경로로 post요청날리면 받는거임

```jsx
@Controller('tasks')
export class TasksController {
  constructor(private tasksService: TasksService){}

  @Post()
  createTask(@Body() body){ //바디를 자동으로 body변수에다가 넣어줌
    console.log('body',body);
  }
```

결과;

body {
title: 'Clean my room',
description: 'do clean right now!',
bananas: 'are tasy'
}

위처럼 body로 다 받는대신 인자별로 따로 받으면 더 편함

```jsx
import { Body, Controller, Get, Post } from '@nestjs/common';
import { title } from 'process';
import { Task } from './task.model';
import { TasksService } from './tasks.service';

@Controller('tasks')
export class TasksController {
  constructor(private tasksService: TasksService){}

  @Get()
  getAllTasks():Task[]{
    return this.tasksService.getAllTasks();
  }

  @Post()
  createTask(
    @Body('title') title:string,
    @Body('description') description:string
  ):Task{ 
    return this.tasksService.createTask(title,description);
  }
}
```

포스트맨 결과:

{

"id": "88c6e57b-acbe-4ef7-a353-26bd879ef539",

"title": "Clean my room",

"description": "do clean right now!",

"status": "OPEN"

}

# Dto

위에서 컨트롤러가 title, description이렇게 딱딱 받아왔는데, 만약 다른요소를 추가하고 싶으면 일일히 다 바꿔야 하는 문제발생.

dto를 활용하면 dto만 바꿔주면 다 해결됨

dto폴더에 create-task.dto.ts 

```jsx
export class CreateTaskDto{
  title: string;
  description:string;
}
```

컨트롤러

기존코드 수정

```jsx
@Post()
  createTask(@Body() CreateTaskDto:CreateTaskDto):Task{ 
    return this.tasksService.createTask(CreateTaskDto);
  }
```

서비스

기존코드 수정-

```jsx
createTask(createTaskDto:CreateTaskDto):Task {
    const{title,description} = createTaskDto; //분해할당

    const task: Task = {
      id: uuid(),
      title,
      description,
      status: TaskStatus.OPEN,
    };

    //만들었으니 기존 배열에 넣기
    this.tasks.push(task);
    return task;
  }
```

# Delete 나혼자 해보기

서비스.- 

id찾아서 걔를 삭제하려고하다보니 애먹었는데, 보니까 그냥 id다른애들만 새로 저장하면 되는거였어...

```jsx
deleteById(id:string){
    this.tasks = this.tasks.filter((task)=>task.id !==id);
  }
```

컨트롤러

```jsx
@Delete('/:id')
  deleteById(@Param('id') id: string):void{
    return this.tasksService.deleteById(id);
  }
```

끝!

# 업데이트(Patch)

서비스

```jsx
updateTaskStatus(id:string, status:TaskStatus){
    const task = this.getTaskById(id);
    task.status = status;
    return task;
  }
```

컨트롤러

```jsx
@Patch('/:id/status')
  updateTaskStatus(
    @Param('id') id:string,
    @Body('status') status:TaskStatus,
  ):Task{
    return this.tasksService.updateTaskStatus(id,status);
  }
```

포스트맨으로 테스트할때, body를 url-encoded로 하고 키를 status, 벨류를 IN_PROGRESS로.
