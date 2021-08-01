# 34강-부팅과정과 런레벨(Run Level)

- 리눅스부팅순서
1. BIOS(basic input output system)-컴터내에서 얘가 프로그램돌리기 좋은 환경인지 체크(메모리, 모니터 등등 다 확인)
2. Master Boot Record(MBR) 
3. LILO or GRUB - 운영체제 선택할수있는 곳
4. Kernel - 선택된 운영체제의 커널이 로드됨
5. 이때 초기화과정됨

    init: process number 1(PID=1)

    서비스프로그램들이 로드됨

6. Run Level에 따라 스크립트가 나뉘어서 서비스를 나눠 실행시킴(윈도우즈에선 f8누르면 안전모드 정상모드 이런거 있는것과 같음)
- 런레벨
![Untitled](https://user-images.githubusercontent.com/78577071/127763591-5c5082b7-2f83-469b-bba1-dc304551f411.png)

---

실습:

- 현재 프로세스 확인(자세한건 나중에)

```bash
ps -p 1 ef  //1번프로세스 목록
```

![Untitled 1](https://user-images.githubusercontent.com/78577071/127763595-fdc63db0-d9e8-49cb-a0c4-d2db7d56a245.png)

→sbin 안의 init이란 애가 1번(맨왼쪽숫자)을 차지하고있음

- 런레벨에 따라 실행되는 프로세스종류

```bash
cd /etc/rc  글구 tab 두번 치면
```

![Untitled 2](https://user-images.githubusercontent.com/78577071/127763603-20d24ed8-72c7-4a8e-80dc-05c8dd2addb3.png)

```bash
/etc/rc6.d로 가서 ls해보면
```

![Untitled 3](https://user-images.githubusercontent.com/78577071/127763605-bfbbaeaa-0856-4276-8b5c-105496924fa7.png)

링크파일들이 보임

```bash
cat K01atd 해보면
```

![Untitled 4](https://user-images.githubusercontent.com/78577071/127763608-773462a4-1aee-4cba-b0dc-d1e593c5fd9e.png)

```bash
같은 위치에서 
sudo init 6  이라 하면 재부팅됨
```

```bash
sudo init 1   은 복구모드(Single user mode)
```

 

**리눅스는 기본적으로 3레벨을 사용**함. 3번도 함 봐보자

![Untitled 5](https://user-images.githubusercontent.com/78577071/127763611-f6963c15-fbec-4016-819b-101d2665cd6b.png)


S: 첫인자가 start란 뜻..

K: 첫인지가 kill.즉 stop하란것

01 이런건 우선순위임. 만약 02로 놓으면 01 먼저 실행하고 그담에 02함

---

근데 힘들게 저렇게 init 숫자 눌러야 하는건 아님. 명령어로 하면돼

![Untitled 6](https://user-images.githubusercontent.com/78577071/127763620-76fb8e21-28f5-46da-a032-32de5cb79531.png)

shutdown이라 하면 같이 사용하는 사용자들에게 곧 셧다운할거니까 준비하세요~라고 방송나가

완전 끌라면

poweroff하면 됨

# 35강-프로필과 환경변수 설정

정상적인 모드로 실행되서 런레벨 3으로 왔다면,

아이디, 패스워드 입력해서 로그인을 함. 

그담에 어떤 일이 일어나는가?

1. /etc/profie  : 모든 사용자에게 적용되는 설정

 2.  ~/.proifle : 추가로 개인적인 설정

위 두개파일이 실행됨

---

- /etc/profile 수정해보자.

sudo nano로 profile.d들어가서 맨 아래에 alias aa='ls -l' 한번써보면ㅋㅋ

이제 로그아웃하고 다시 들어가서 aa만 입력하면 ls-l 실행됨ㅋ

alt f2로 다른 창에서 dragon이 로그인해도 똑같이 적용된다

이번엔 /etc/profile.d에 가서 sudo nano [alias.sh](http://alias.sh) 파일만듦(shell파일). 

파일에서 alias aa='ls -l'을 입력하고 저장

근데 신기한건 저 쉘파일에 실행권한을 주지 않아도 밖에서 aa치면 ls-l 작동함. 

why?→로그인하면서 /etc/profile파일을 다 읽기 때문임

---

- 이번엔 개인화된 프로필에 넣어보자

근데 이건 숨김폴더에 있기 때문에 ls -a로 검색하면 .profile보임

얘도 nano 로 수정해서 위와 똑같이 해보면 heejun사용자에선 먹히지만 dragon사용자에선 먹히지 않는다

![Untitled 7](https://user-images.githubusercontent.com/78577071/127763625-7a74b129-3c9d-4b6e-af3c-fb38a9861b85.png)

로그인 하지 않고 쉘 실행할때(리눅스 윈도우즈처럼 그래픽으로 실행할때) 비로그인 쉘+ /.bashrc가 실행됨.

근데 환경변수에다가 jdk설정할 때 설정할때, 혼자쓸땐 ./profile, 전역적으로 쓸땐 etc/proifle에다 하면 되나? **노노!!**

저 로그인쉘이 시작하기 전에 **/etc/environment**라 하는 파일이 있는데, 얘를 읽어들이는 PAM이란 애가 있어

- PAM(Pluggable Authentication Modules)

다양한 인증이 필요한 애들(login,sshd 등등)이 있는데, 그걸 통합적으로 인증하려고 관리하는 것임

![Untitled 8](https://user-images.githubusercontent.com/78577071/127763628-700d4d8b-d8dd-4b7f-8da3-7fde1112c245.png)

이케 하면 전역적으로 모든 사용자가 저 패스정보를 쓸수있음

# 36강-jdk 전역화하기

예전에 jdk를 download/에 설치하고 바로가기를 bin/에다가 만들었었음

이제 jdk를 나혼자가 아니라 dragon유저도 같이 쓰고 싶어

→윈도우즈에서 파일들을 c드라이브에 programfiles에 놓듯이 usr/이란 디렉토리에 놓는다

![Untitled 9](https://user-images.githubusercontent.com/78577071/127763629-0f9e136e-02d4-4cb3-b158-c9b267c09450.png)

이동!

할줄알지?ㅋㅋ

```bash
sudo mkdir /usr/local/java  로 디렉토리만들고
sudo mv ./jdk1.8.0_161/ /usr/local/java/  로 옮기면됨
```

이제 Path 수정

```bash
sudo nano /etc/environment 해서 
제일 마지막에
:/usr/local/java/jdk.1.8.0_161/bin 쓰고저장
```

글구 아직 PATH가 업데이트 되지 않아서 바로 java -version 못읽음

```bash
source /etc/environment  로 이 경로 읽어라! 하면 이제 Path에 업데이트됨
source ~/.profile 도해줌. 

근데 귀찮으면 그냥 재로그인 하면됨
```

이제 java -version 잘 됨. dragon에서도 잘 된다

아직 살짝 그런게 

![Untitled 10](https://user-images.githubusercontent.com/78577071/127763633-2d9ce195-ff14-4375-af29-0742a98092ce.png)

주인이 다 heejun이야. 다행히 모든유저(맨뒤3개)가 r-x라서 읽거나 실행하는데 문제없어서 오케이. 단 나중에 톰캣으로 쓸땐 그룹 필요

# 37강-Debain 설치패키지 관리자

[package.ubuntu.com](http://package.ubuntu.com) 가서 tree라는 패키지 다운로드해보자

tree패키지는 ls로 목록 보여줄때, 디렉토리가 있으면 그 안의 목록도 보여줌

```sql
다운로드 디렉토리가서
wget http://kr.archive.ubuntu.com/ubuntu/pool/universe/t/tree/tree_1.8.0-1_amd64.deb
```

```sql
sudo dpkg -i tree_1.8.0-1_amd64.deb //설치하기
```

이제 tree라고 치면 아래처럼 나옴 신기

![Untitled 11](https://user-images.githubusercontent.com/78577071/127763636-fb94cb1e-4cf3-4ae4-b001-d95e3f66b679.png)

```sql
dpkg -l //현재 시스템에 설치된 모든파일들 보여줌
dpkg -l tree  //그 목록중에 tree가 설치되었는지 확인
dpkg -L tree  //어디에 설치되어있는지??
```

```sql
sudo dpkg -r tree  //삭제하기..단 r은 실행파일만삭제
sudo dpkg -r tree  //-P는 설정까지 삭제
```

---

이제 jdk가보면 다음처럼 .deb 파일로 되어있는걸로 나중에 설치할수있음

![Untitled 12](https://user-images.githubusercontent.com/78577071/127763638-d2f66807-897b-470d-8c49-6333de83b007.png)

# 38강- 향상된 패키지 관리도구!!(APT)-JDK설치

이제 debian도 잘 안씀. 왜냐면 왠만한 툴들이 우분투 패키지 저장소에 다 있어

저 패키지에 있는 애들 목록을 볼수있는 도구가 APT임. 글구 다운로드하면 알아서 패키지까지 풀어서 설치까지 끝내줌.

그니까 내가 할일은 /etc/apt/sources.list보고 설치하는 것뿐임. 이미 웬만한거 다있지만 더 추가하고픈거 하면됨

/etc/apt/sources.list 가서 cat해보면 파일 엄청 많은데, 이건 Canocial이 지원하는거라 써있음. 파일명 맨뒤에 main은 무료, restricted는 유료, universe는 Canonical이 만들지 않은 무료, multiverse는 Canonical이 만들지 않은 유료

```sql
apt-cache  ///etc/apt/sources.list에 있는 목록들을 캐시로 담고있는거임
apt-cache pkgnames   //패키지목록들 다 뜸 개많아
apt-cache search jdk //jdk란 글자들어간 목록찾아라
```

- apk로 jdk설치해보기

```sql
apt-cache pkgnames | grep jdk  //패키지목록 중 jdk 들어간애들만 찾음
```

목록 중에 보면 openjdk-11-jdk 있는데

```sql
sudo apt-get install openjdk-11-jdk  쳐서 설치
```

엄청 오래걸림...

아근데 나 이전에 jdk8버전 설치했었는데 어케된거지?

whereis java해보면 다음처럼 됨. 지금 apt로 설치한건 /user/bin/java이므로 이게 앞으로 실행되는것임. 이전에 압축파일 풀었던 건 뒤로 밀림(jdk1.8이거)

![Untitled 13](https://user-images.githubusercontent.com/78577071/127763640-3564c558-5566-4feb-b5cf-097e87e16834.png)

# 39강-PPA와 update-java-alternative이용한 jdk설치

/etc/apt/sources.list에 없는 걸 추가하고 싶다면, add-apt-repository ppa: 라고하고 apt-get update하면 됨.

이때 ppa란 Personal Package Archive라고 함.

```sql
sudo add-apt-repository ppa:openjdk-r/ppa  //제공사이트에 적힌거 고대로 적는거임

sudo apt-get update  //업데이트해야 소스리스트에 보임
```

글고 설치

```sql
sudo apt-get install openjdk-8-jdk
```

성공!

~~근데 강의에선 오라클8설치하는데 지금은 막힘; 걍패스~~

명령어 짧게쓰기. apt-get install이나 apt install이나 똑같음. apt다음 명령어에 다 모아놨음

```sql
sudo apt install 파일명   
```

---

지금까지 java 여러개썼는데, 다른 버전을 쓰고 싶다면?

```sql
update-java-alternatives -l    //java 버전목록 보여줌

sudo update-java-alternatives -s java-1.11.0-openjdk-amd64  //이케하면 11버전으로 바뀜(8도똑같이하면됨)
```

# 40강-update-alternatives

방금 전 강의에서 자바버전 바꾸는 원리가 무엇일까?

보통 파일을 실행시킬 때 PATH에 경로를 추가하거나 or 있는 경로에 링크파일을 추가함,

경로추가하는 방법으로 하면 경로 ㅈㄴ많아지는 문제발생

링크파일추가하는 방법은 링크파일 너무 많아지면 서로 이름 같아서 충돌할수있음

 그럼 어캄??

→ 하나의 링크파일을 만들고 얘가 가리키는 파일을 때에 따라 달리하도록 하면 됨.

근데 HOW?

링크파일 삭제하고 다시만들어?

ㅇㅇ예스!근데 그걸 한방에 할수있는 명령어가 바로 update-alternatives임

ex) 편집기.

editor란 이름으로 여러개 편집기가 등록되어있음. 어떤 편집기를 쓸것인가?

```sql
sudo update-alternatives --config editor
```

지금 0번에 별표있지. 바꾸고 싶으면 바꿀 번호입력하면 되는것임

![Untitled 14](https://user-images.githubusercontent.com/78577071/127763643-79912677-d465-4d89-b065-cf4a01415281.png)

---

ex)test1파일과 test2파일을 똑같이 bb명령어로 받는상황이야

~bin/bb파일을 bb명령어로 실행하면 ~test2.sh가 실행되도록 설정.마지막1은 안중요 걍식별자용

```sql
sudo upate-alternatives --install ~bin/bb bb ~/test1.sh 1
sudo upate-alternatives --install ~bin/bb bb ~/test2.sh 2
```

지금 bb해보면 test2.sh가 실행됨

test1로 바꾸고 싶어

```sql
sudo update-alternatives --config bb
```

![Untitled 15](https://user-images.githubusercontent.com/78577071/127763645-edd01e6e-3593-484e-a64d-8a9e13035246.png)

이 상황에서 1번누르면 됨

오케이

---

그렇다면 jdk 버전바꾸는것도 저렇게 하면 되는가?

노노!! 왜냐면 jdk버전 하나 바꾸려면 관련된거 update해야할게 너무 많아. 

```sql
얼마나 많은지 보고싶으면
update-alternatives --all  누르면 다 보여줌. 
```

 

이걸 해결해주기 위해

```sql
update-java-alternatives  를쓰면 자바 링크파일 한방에 다 교채됨
```
