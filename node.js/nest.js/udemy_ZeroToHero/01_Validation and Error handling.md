![Untitled](https://user-images.githubusercontent.com/78577071/130405044-a127a04d-a7bf-4029-8dd4-b480e79c3c66.png)

라이브러리 추가

```jsx
yarn add class-validator class-transformer
```

main.ts에서 ValidationPipe()사용한다고 적어줌

```jsx
import { ValidationPipe } from '@nestjs/common';
import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);
  app.useGlobalPipes(new ValidationPipe());

  await app.listen(3000);
}
bootstrap();
```

- create하려는데 title, description 내용 없는경우

DTO에 가서 데코레이터(어노테이션) 붙여줌-이게 벨리데이터임

```jsx
import { IsNotEmpty } from 'class-validator';

export class CreateTaskDto{
  @IsNotEmpty()
  title: string;

  @IsNotEmpty()
  description:string;
}
```

- 없는 id로 getTaskById할 경우

컨트롤러는 건들필요없음. 서비스만 건드리면 돼. 개굿

```jsx
getTaskById(id:string): Task{
    const found = this.tasks.find((task)=>task.id === id);

    if(!found){
      throw new NotFoundException(`Task with ID "${id}" not found`);
    }

    return found;
  }
```

- 없는 id로 deleteTask할경우

```jsx
deleteTask(id:string){
    const found = this.getTaskById(id); //벨리데이션
    this.tasks = this.tasks.filter((task)=>task.id !== found.id);
  }
```

- status 변경시 enum에서 정한 것만 받기

새로운 dto를 만들면 됨

```jsx
import { IsEnum } from 'class-validator';
import { TaskStatus } from '../task.model';

export class UpdateTaskStatusDto{
  @IsEnum(TaskStatus)
  status: TaskStatus;
}
```

컨트롤어의 body전체를 dto로 받아줌

```jsx
@Patch('/:id/status')
  updateTaskStatus(
    @Param('id') id:string,
    @Body() updateTaskStatus:UpdateTaskStatusDto,
  ):Task{
    const {status} = updateTaskStatus //받은거 구조분해.서비스에서 해도 되고 여기서 해도 됨
    return this.tasksService.updateTaskStatus(id,status);
  }
```

포스트맨 테스트

[http://localhost:3000/tasks/a085b638-b690-49f1-ad32-dc3646c43a91/status](https://www.notion.so/a085b638b69049f1ad32dc3646c43a91) 페치

키:status, 벨류:IN_PROGRESS 하면 제대로 변경됨

키:status, 벨류:lalala  하면 400리턴됨 

오카이~

- 검색할때 status가 enum인지(IsEnum), 검색어가 string인지(IsString), 안써도 되는지 (IsOptional)

```jsx
import { IsEnum, IsOptional, IsString } from 'class-validator';
import { TaskStatus } from '../task.model';

export class GetTasksFilterDto{
  @IsOptional()
  @IsEnum(TaskStatus)
  status?: TaskStatus;

  @IsOptional()
  @IsString()
  search?: string; 
}
```

포스트맨: [http://localhost:3000/tasks?status=OPEN&search=cook](http://localhost:3000/tasks?status=OPEN&search=cook) 

으로 하면 검색됨

OPEN이 아니면 에러

isoptional 안쓰면 검색어 안 썼을때 에러남
