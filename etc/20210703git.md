# git

내용은 [드림코딩 강의](https://www.youtube.com/watch?v=Z9dvM7qgN9s)를 토대로 작성됨.

먼저 cmd에서 깃명령이 기본이니 cmd에서 git명령어를 연습해보는 게 좋다. *참고로, 기본 cmd는 넘나 흑백이니 conemu사에서 제공하는 cmder을 사용하면 좋음*

## 깃 구조

git status로 확인하면서 체크하면 좋음. 큰구조는 다음 그림처럼  되어있지만,,,

![01](https://user-images.githubusercontent.com/78577071/124314578-91fc7a80-dbad-11eb-9f6a-f599a7d04ace.png)

- working directory
    - untracked : 새로 만들어진애들. 또는 깃 초기화했을때
    - tracked: git이 이미 알고있는애들
        - unmodified
        - modified : 얘만 고쳐진거니까 staging area로 넘어갈수있음

**대충** **흐름을 보자면**

첨에 파일 만들면 untracked임. 

git add [파일명] 하면 staging area로 옮겨감.. 

git status 해보면 changes to be commited가 staging area에 있다는거임.

![02](https://user-images.githubusercontent.com/78577071/124314709-c3754600-dbad-11eb-91cf-256755377ff5.png)    

a.txt파일을 수정한다면 새로 수정된 내용은 아직 not staged 된거니까 저리 뜨고. 원본은 staging area에 있으면서 수정된건 *working directory의 tracked에 포함됨*. 왜냐면 원래 있던게 트래킹되고 있는거니까 untracked가 아닌거.   

![03](https://user-images.githubusercontent.com/78577071/124314704-c2441900-dbad-11eb-97cd-91a0259baa5b.png)

다시 git add a.txt해보면 수정한 내용이 staging area로 들어가서 working directory엔 암것도 안남음

마아아안약. staging area에 있는걸 다시 working directory로 옮기고 싶다면

git rm —cached * 하면됨. 이럼 다 untracked로 돌아감.

## sourcetree

cmd로만 보면 노잼이니..ㅎ

위쪽 박스가 staging area고 아래박스가 working directory임.

![04](https://user-images.githubusercontent.com/78577071/124314706-c2dcaf80-dbad-11eb-87db-1b49ccf5b63c.png)

git diff 

수정이 뭐가 됐는지

git commit
