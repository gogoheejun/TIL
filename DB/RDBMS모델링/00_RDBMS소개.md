# RDBMS 소개

## 주식별자(Primary Key)

Not Null & Unique 해야함

보조식별자? 

unique는 하지만, null은 허용 안되는 애

- Quiz: 다음조건을 만족하는 테이블을 엑셀로 만드시오

학생 수강신청관리

각 학생들은 수학,영어,국어,과학,국사 중 최소한 세 과목을 수강

학생 세명의 예제를 만들고 이들이 수강신청한 과목들을 관리

→

![Untitled](https://user-images.githubusercontent.com/78577071/127689863-36416998-2e3d-4ab7-a890-5286d973b6ed.png)

앗..난 저렇게 안했는데;;;ㅠㅠㅠ 아래는 내가 해본거... 내꺼무시하삼

![Untitled 1](https://user-images.githubusercontent.com/78577071/127689880-0e5e1436-410a-46cd-99fd-2c526bd19558.png)

**위 두개 예시 테이블의 차이의 *본질*을 깨달아야 함.** 

- 위 문제(학생수강 신청 관리) 시 고려사항

-테이블 변경시

만일 같은 과목을 담당하는 선생님들이 두명 이상으로 늘어날경우 대처방법

-데이터 오류시

과목명 오기, 학생명 오기 했을 경우 대처방법

한 학생이 동일한 과목 두번 이상 수강신청 한 경우 대처방법

한 과목도 신청하지 않은 학생 찾는 방법

동명이인 존재하는 경우 해결방법

한 과목이 폐강되었을 때 대처방법

# 주식별자와 데이터타입

어느 회사에 다음과 같은 테이블이 있어.
![Untitled 2](https://user-images.githubusercontent.com/78577071/127689893-b424f64d-285f-48be-a7f2-a6d231da7192.png)

어떤걸 주식별자로 할것인가?

일단 주민번호,폰번호,이메일,고객번호는 모두 유니크하기에 보조식별자는 될 수 있음

이중 폰번호,이메일은 없는 사람도 있을 수 있으니 주식별자 탈락. 

이제 주민번호 vs 고객번호 인데, 주민번호는 고객이 제공한 정보야. 보통 고객이 제공한 정보는 주식별자로 쓰지 않음 신뢰하기 어렵.

고로, 고객번호가 적합. 고객번호는 회사에서 부여한 것이기에 로직이 있을 것임.

- 결정자: 위 예에서 고객번호(결정자)를 알면 나머지(종속자)를 다 알수 있는 것.

결정자는 모두 후보 식별자임.

고객번호를 알면, 고객명을 알 수 있어

주민번호를 알면, 고객명을 알 수 있어.

폰번호를 알면, 고객명을 알 수 잇어

이메일을 알면, 고객명르 알 수 있어

- 데이터타입
- 정수
![Untitled 3](https://user-images.githubusercontent.com/78577071/127689906-45891737-7980-4314-bc6b-1eb5bc8092b1.png)

- 소수점: float,real,decimal(화폐단위때 이거씀 왜냐면 근사치안쓰는 대신 16바이트까지 씀.정확한 숫자야)
- 문자

char: char(6)이면 'abcdef' 이렇게 6글자만 들어감. 만약 ab만 넣으면 나머지공간은 그대로 남아있음

Varchar: varchar(50) 넣고 'ab'만 넣으면 2바이트만 할당됨. 메모리절약에 좋음

Text: varchar(max)랑 같음

단, Text나 varchar(max)는 PK못함

- 날짜,시간,화폐

날짜: datetime(8바이트), smalldatetime(4바이트)

화폐; money(달러,원,엔 등등이 있지만 근사치 오류있어서 그냥 Bigint씀 다)

# 주식별자(PK) 설계

- 인조식별자: 후보식별자가 없는경우 임의의 식별자를 만들어 부여한다.(후보식별자 있어도 하는경우많음)ㅋㅋ

- 만약 한달에 거래가 12만개가 발생한다면 거래건수를 int로 할것인가, tinyint로 할것인가? tinyint하면 255개면 끝나니까 못써 하루면 다 차버려서 에러나버림. int하면 21억이라는 숫자가 적힐 때까지는 안전하니까 int로 해.  그래서 제품생산 건수, 거래액 등 규모를 아는것도 중요함.

- pk가 123 56 9 이런식이라면, 오케이 굳이 왜 12345678이렇게 안됐냐 따지지 말아야. 유니크&낫널 이기만 하면 충분!

- 자동증분

  만약 여러명이 동시에 insert into aaa values('10','kim') 이렇게 아이디값(10)을 10번째라 입력하는 상황이라면...100명이 동시에 10번 아이디에 입력한다면 오류남. 유니크 위배. 젤빨리 입력한사람만 입력하겠지;;; 이런거 피하기 위함임. 그냥  insert into aaa values('kim')만 해도 id자동입력.. 이때 id 데이터타입은 int같은 숫자타입으로 해줘야 함. char같은걸로 주면 자동증분안됨
