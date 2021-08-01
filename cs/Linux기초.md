다음 노트는 유튜브 뉴렉처의 리눅스강의를 보고 필기한 내용!


# 역사

첨에 러시아할배가 하드웨어로부터 독립적인 운영체제를 처음 만듦. 이전엔 하드웨어 바뀌면 프로그램 다 바꿨어야 했어ㅠㅠ. 이제 운영체제만 있으면 하드웨어 바껴도 노상관인거임. 이때 운영체제가 B 언어 기반임.

벨연구소에서 데니스리치가 B언어 계량해서 C언어로 유닉스 계량함.

근데 유닉스가 유료화가 됨. 그래서 GNU라는 단체가 FSF만듦(free software foundation), 즉 공짜유닉스. 그러다 Linuns Torvalds라는 사람이 혼자 유닉스를 개인pc에서도 실행될 수 있도록 리눅스 만듦.

- 오픈소스란?

공짜는 맞지만, 라이센스는 있다. 우리가 흔히 보는 라이센스는 Apache, GPL(GNU general public license), MIT 등의 라이센스들이 있음

**GPL**: gpl적용된 sw이용해 개량한 sw는 개발한 sw의 소스코드역시 공개해야함

**아파치**: 수정해도 소스코드 공개의무 없음. 단, 아파치에 의해 만들었다는 저작권은 표시해야함

**MIT**: 아무 의무 없음

# 리눅스 설치하기

virtual box로 가상컴터 하나만듦. 오라클사이트가서 ([https://www.oracle.com/virtualization/technologies/vm/downloads/virtualbox-downloads.html](https://www.oracle.com/virtualization/technologies/vm/downloads/virtualbox-downloads.html)) 다운받고 다음다음 누르다가, 메모리랑 하드디스크 할당해줌. 난 메모리에 2기가 할당하고, 하드는 60기가줌. 근데 하드는 어차피 동적 할당이라 맥시멈이 60기가란 거임.

이제 디스크 설치해야함

[https://ubuntu.com/download/server](https://ubuntu.com/download/server) 여기서 옵션2로 가서 다운로드 ㄱㄱ(난 현재 20버전). 완료되면 벌츄어박스 설정들어가서 광학드라이브에 방금 다운받은애 올려줌

리눅스 서버 켜주면  됨. 그럼 불안하게 막 중간중간 빨간글씨 뜨는데 무시하고 기달.

아래처럼 되면 성공
![Untitled](https://user-images.githubusercontent.com/78577071/127602221-d6fbdb9d-47e9-4e80-b0c2-695f49c69ac2.png)

이제 입맛에 맞게 설정하면됨. 난 다 기본적인걸로 하고, [유튭](https://www.youtube.com/watch?v=K7IWOmC9mwM&list=PLq8wAnVUcTFU9zLWK-dHWrvTJ0PF8Y0Sf&index=8) 보면서 똑같이 했다.

마지막에 엄청 오래기다림....다 되면 리부트하고 아디(내이름ㅋ) 비번(게임비번) 입려해주면 끝

성공!
![Untitled 1](https://user-images.githubusercontent.com/78577071/127602231-28a3b8ff-d9c6-4a3d-a809-42c7cefb9237.png)

# Bourn shell 이용하기

여러 쉘들이 있는데 본쉘은 기본적으로 다 있어서 이거쓰자

root권한실행: $sudo reboot 이런식으로

그러나 매번 수두쓰기 귀찮으니 root사용자로 전환하면

$sudu su -root 이케 하면됨. switch user라는거임 

$sudu su : 암것도 안쓰면 루트로 아예 바뀌는 건 아니고 권한만 바뀜

리눅스는 root로 로그인 못함(보안때매). 일반 아디로 들어가서 루트로 전환해줘야해.

한번 루트유저로 바꿔보자

```bash
$sudu su -root 
```

이케 하면 $가 #으로 바뀐다ㅎ

관리자권한으론 특정한 일만 하는게 안전하니, 다시 돌아오려면

```bash
exit
```

---

루트경로로 가기

```bash
cd / 
```

구조:
![Untitled 2](https://user-images.githubusercontent.com/78577071/127602249-f8a52e4e-7e92-4679-a480-571ace89febe.png)

- home에 가서 ls 쳐보면 내 아이디로 된 폴더있음 heejun으로.. 마치 윈도우즈의 user느낌
- mnt 또는 media: 장치연결할 때 이 장소에 씀. 마치 윈도우즈의 d드라이브 외부저장소 뭐 그런거
- usr: 윈도우즈의 program files 역할임. 프로그램 설치하는곳
- etc: usr에 넣은 프로그램에 필요한 설정들. 윈도우즈의 레지스트리
- var: usr에 넣은 프로그램에 필요한 데이터들
- bin: 모든 유저가 사용하는 실행파일들
- sbin: 어드민이 사용하는 실행파일들

# 11강-파일경로순회

- pwd

print working directory..현재 디렉토리 경로 출력

- cd /

루트로 가기

- cd ~

내 홈으로 가기( /home/heejun)경로로 이동해짐

- cd ../

부모로 가기

- man ls

ls명령어에 대한 메뉴얼보기

# 12강-파일관리 명령어

- mkdir : 디렉토리생성
- rmdir : 디렉토리 삭제
- touch : 빈 파일생성
- mv : 파일이동/이름변경
- rm : 파일삭제
- cp : 파일복사

- touch test.txt : 테스트라는 텍스트 파일을 만듦
- mv test.txt workspace/

 파일이동..맨마지막 /는 디렉토리를 의미하기때매 디렉토리 뒤에는 보통 붙여줌

- rm -r workspace/

원래 rm은 파일만 삭제가능해서 디렉토리삭제 불가. rmdir은 디렉토리삭제 가능하지만 안에 파일이나 디렉토리 또 있으면 삭제불가. 그래서 -r (recursive) 옵션 붙여주면 통채로 삭제가능

- rm -ri aa

rm -r은 넘나 무서워 다 없애니까. 그래서 aa붙여주면 하나 삭제할때마다 물어봄. y로 대답해주면 됨

- cp test.txt test2.txt

test란 파일을 test2란 이름으로 복사

# 13,14강-파일편집기(vi, nano)

윈도우즈의 아래한글같은 걍 편집기일 뿐임.

- vi
- 
![Untitled 3](https://user-images.githubusercontent.com/78577071/127602261-bd23bef1-d102-4c49-87d4-90af2f86962e.png)

- vi Hello.java

헬로자바에 들어가서 편집시작!

그담에 i,a,o 등을 누를수있어

i = insert모드로 바뀜 그담에 내용입력하고

ESC→:w   저장됨

저장 후 :q 누르면 나가짐

o = 누르면 다음줄로 커서가 이동하면서 insert모드가 됨

 

---

이제 nano.

nano는 기본편집기임

nano [Hello.java](http://hello.java) 라고치면 실행됨

그럼 아래에 명령어 설명 다 써있어. 보고쓰면 됨

# 15강-파일찾기, 파일정보확인하기

```jsx
find ./ -name *.java
```

-현재 경로에서 모든 자바파일찾아라.

```jsx
find ./ -name *.java -size +1c
```

현재경로에서 1바이트(한글자) 이상인 자바파일 찾아라.

c안붙이면 디폴트로 b로 인식하는데 이건 512바이트임. c 붙여야 1바이트로 인식함.

```jsx
cat Hello.java
```

그냥 Hello.java파일을 읽어서 보여줌

```jsx
head -n2 Hello.java
```

앞 2줄만 보여준다. n은 넘버. 

```jsx
tail -n2 Hello.java
```

뒤에서 2줄 보여준다

```jsx
grep class Hello.java //헬로자바 파일에서 class란 글자를 뒤져서 찾아줌

grep "Hello li" Hello.java // 두단어 이상도 찾기 가능

grep -i "Hello li" Hello.java //대소문자 구분안함
```

```jsx
ls Hello[12].java //대괄호 안의 글자는 한글자씩 OR의 의미를 가짐. Hello1, Hello2를 의미
```

```jsx
cmp Hello.java Hello1.java //두 파일 비교
```

→몇번째 바이트에서, 몇번재 라인에서 다른지보여줌

```jsx
diff Hello.java Hello1.java
```

→ 다른걸 아예 프린트해서 보여줌

```jsx
file Hello
```

Hello란 파일의 확장자가 없을때가 많어 리눅스에선.. 그래서 어떤 종류의 파일인지 알려줌

# 16강-유용한 명령어

```jsx
history //지금까지 친 명령어 다 나옴

!39  //history목록에서 39번째 명령어 실행
```

```jsx
history > test   //출력을 화면이 아닌 test파일을 만들어서 거기다가 출력해라
```

```jsx
echo "hi" //걍 화면에 hi출력

echo "hi" > test //test파일에다가가 hi글자 저장. 단, 기존 글들 다 삭제하고 새로작성됨
```

```jsx
echo "okay" >> test //얘는 새로 엎는게 아니라 추가시킴
```

```jsx
cat test | grep h  //원래 cat은 그냥 읽어서 출력인데, |를 쓰면 파이핑이 됨. 출력을 하지 않고 오른쪽으로 넘겨서 거기서 실행됨
```

```jsx
ls | less  //리스트 너무 길어서 화면 넘칠때 끊어서 보여줌. 화면나갈땐 q
ls | more
```

```jsx
cat test | sort //정렬해서 보여준다
cat test | sort -r //반대정렬
```

```jsx
cat test | sort -r | grep hi //이렇게 파이프 이어나갈 수 잇음
```

```jsx
touch test1; echo "okay" >> test1; cat test1  //;이용하면 연속해서 써서 한방에 실행가능!
```

# 17강-파일 압축관리(tar)

파일압축 방법: tar(Tape ARchive) + zip

tar로 묶어서 압축하는거임

-f  파일이름을 지정

-c  파일을 tar로 묶음

-x  tar압축을 품

-v  내용을 자세히 출력//큰 파일 압축풀때 시간이 좀 걸려서 먹통된 느낌날때 이거써주면 진행과정보여줌

-z  gzip으로 압축하거나 해제함

-t  목록 출력

-P  파일권한을 젖아

-C  경로를 지정

- 압축하기

```jsx
tar -cf name.tar a b c //a,b,c세 파일을 name.tar라는 이름으로 묶어서 파일만든다.cf에서 c는 create f는 파일이름지정할때 씀
```

근데 그냥 tar만 하면 오히려 사이즈가 더 커짐ㅋㅋ

그래서 압축 ㄱ

```jsx
tar -zcf name.tar.gz a b c // 압축까지 해서 만들라면 z붙이고, 파일확장자는 gz로 해주면됨
```

- 압축풀기

```jsx
tar -xvf name.tar //name.tar파일을 풀어라
```

```jsx
tar -zxvf name.tar.gz//압축까지 풀어라
```

# 18강-리눅스ㅔ 압축본 jdk 설치하기(wget,tar)

리눅스 다운로드폴더에가서 다음을 입력

```jsx
wget -c --no-cookies --no-check-certificate --header "Cookie: oraclelicense=accept-securebackup-cookie" https://javadl.oracle.com/webapps/download/GetFile/1.8.0_261-b12/a4634525489241b9a9e1aa73d9e118e6/linux-i586/jdk-8u261-linux-x64.tar.gz
```

그리고 압축풀면됨

```jsx
tar -zxvf name.tar.gz//압축까지 풀어라
```

이제 압축 풀었으니  작동되는 지 보려면, 

jdk의 bin에 가서 

```jsx
java -version  입력!! 아근데 아직 경로지정 안해서 안되고

./java -version 이렇게 하면 확인됨!
```

# 19강-링크파일 사용하기(In,path,cp)

링크파일은 마치 윈도우즈의 바로가기 같은거임

- Symbolic Link

원본파일을 가리키고 있는 별도의 링크파일. -s옵션을 붙여서 만듦

```jsx
ln -s test1.txt test1.1n  //test1.txt의 링크파일인 test1.ln 파일을 만듦. 심볼릭 링크로!
```

- Hard Link

앤 별도의 파일이 아니라 그냥 별칭임. 그냥 이름만 다른 다른파일 새로 생성임. 원본파일 삭제해도 하드링크파일은 그대로 남아있음. 별로 안씀.

링크파일은 그냥 심볼릭 링크라 생각하면 됨

# 20,21강-링크파일 실습,Resolving

리눅스는 명령어 입력이 됐을때 1. 쉘이 가지고 있는 명령어인지, 그게 아니면 2.디렉토리(현재 디렉토리가 아니라 $PATH)를 뒤짐→ 아무데도 없다면 **resolving error**

그냥 $bin에서 java -version은 안돼

./java -verison 이렇게 해야 현재디렉토리에서 java라는 애를 찾아라! 가 되서 실행됨

 

방법은 PATH에다가 저 경로를 등록하는 방법도 있고, 아니면, 원래 있는 경로에 링크파일을 등록하는 방법이 있다

# 23강-링크파일 이용한 실행파일 리졸빙

원래 있는 경로에 링크파일을 만들자.

PATH경로는 다음처럼 되어있음. home/heejun/bin을 먼저 뒤지고, user/local/sbin도 뒤지고..이렇게 계속 뒤지는거임.
![Untitled 4](https://user-images.githubusercontent.com/78577071/127602275-d74b960c-be34-40f7-ab02-57e2b81c8040.png)


 home/heejun/bin 을 뒤지니까 여기다가 링크파일 만들면 되겠다

 home/heejun/bin 에 가서 

```jsx
ln -s ~/download/jdk1.8.0_161/bin/java java 라고 치면 java라는 링크파일생김
```

ls -l로 확인해보면

![Untitled 5](https://user-images.githubusercontent.com/78577071/127602283-23fe0573-c9e4-4867-b841-2e130b8a5645.png)

굿굿

javac(자바컴파일러)도 똑같이 만듦

# 24,25강-사용자 추가하기(useradd,usermod,userdel)

리눅스가 원랜 intel cpu에서 돌아가는 pc의 unix 이지만,꼭 개인의 pc뿐 아니라, 서버에다 자바 웹을 올렸을때 협업하는 사람들이 와서 같이 접근해야 할 때가 있는데, 내 비밀번호를 알려주긴 좀 그래.

고로 사용자 추가할 수 있어야한다.

```jsx
useradd //사용자추가
usermod //사용자변경
userdel //사용자삭제
```

예시

```jsx
useradd dragon//드래곤이란 사용자추가..안되면 sudo 붙이셈
$cat /etc/passwd  //사용자정보는 etc의 passwd에 저장되어있어서 보여줌...cat대신 tail해도됨
```

![Untitled 6](https://user-images.githubusercontent.com/78577071/127602290-a989a542-bcd4-4be8-8cc7-c7eae76444ae.png)


cat해보면 사용자리스트 나오는데 맨앞은 인간을 위한 user명, 그담 1001이게 사실 진짜 식별번호임. 그담 1001은 그룹명인데 앞의 1001이랑 같게 추가됨. 

group도 et/group에 있어서 

```jsx
tail -n3 /etc/group //이케 치면 그룹들 다 나옴
groups dragon //드래곤이 포함된 그룹검색됨
```

```jsx
sudo passwd dragon  //비번설정
```

```jsx
sudo mkdir /home/dragon //dragon이 첨 로그인할때 위치할 폴더 생성해줌.
//이케하면 드래곤이 로그인할때 자동으로 여기로 위치하게 됨
```

근데 드래곤이 자기폴더에다가 파일생성 권한이 아직 안생김

아래보면 drwxr...이거 맨 앞의 d는 디렉토리를 의미. 첫번째 heejun은 그 디렉토리의 소유자, 두번째 heejun은 그 디렉토리의 소유그룹임. 근데 dragon은아직 안정해져서 글을 못쓰는것임

![Untitled 7](https://user-images.githubusercontent.com/78577071/127602299-de1ff9a2-1746-4207-9d6d-d57641341fcb.png)

- 다른 유저로 로그인해보기

그럴라면 다른 단말기가있어야 하는데 alt+f2누르면 새창켜짐

alt+f1은 창1, alt+f2는 창2이 되는 거임

# 26강- 디렉토리 소유권 변경하기(chown)

방금 위에서 dragon이 dragon디렉토리의 소유권이 없어서 파일 생성 못했었음

chown을 쓰면 됨.change owner!

```jsx
sudo chown dragon:dragon ../dragon    //dragon디렉토리의 주인과 주인그룹을 dragon,dragon으로!
```

첫째 dragon: 주인이름

둘째: 주인그룹이름

이제 alt+f2눌러서 다른창 가서 dragon으로 로그인한담에 

touch test 하면 dragon디렉토리에 test파일 생성된다!
