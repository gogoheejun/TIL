# 3주차(1)

# ajax, api

### api사용해서 ajax안에 넣어서 내용물 바꾸기

대충 다음과 같은 api를 받아오면,

```jsx
{
articles: [
{
comment: " 안녕하세요. 케이론입니다!오늘 리뷰할 영화는그린 북입니다.지극히 개인적인 몇 줄 평<그린 북>은 2018 토론토국제영화제 관객상 수상과다가오는 골든 글로브 시상식에서감독상, 남우주연상 등 5개 부문 노미네이트는 물론크리틱...",
desc: "1962년 미국, 입담과 주먹만 믿고 살아가던 토니 발레롱가(비고 모텐슨)는 교양과 우아함 그 자체인천재...",
image: "https://movie-phinf.pstatic.net/20190115_228/1547528180168jgEP7_JPEG/movie_image.jpg?type=m665_443_2",
title: "그린 북",
url: "https://movie.naver.com/movie/bi/mi/basic.nhn?code=171539"
},
{
comment: " 문득 이런 생각이 들 때가 있습니다. 내가 이 나라가 아닌 다른 나라에서 다른 인종으로 태어났다면 어떤 삶을 살고 있을까. 이 나라에서 태어나 자라며 살아가고 있는 지금의 삶을 생각해 보았을 때 다른 나라에서 태어나고 자랐다면 지...",
desc: "나를 세상에 태어나게 한 "부모님을 고소하고 싶어요..."-출생기록조차 없이 살아온 어쩌면 1...",
image: "https://movie-phinf.pstatic.net/20190109_149/1546998123676c6LjJ_JPEG/movie_image.jpg?type=m665_443_2",
title: "가버나움",
url: "https://movie.naver.com/movie/bi/mi/basic.nhn?code=174830"
},
.....생략
```

화면이 딱 켜지면 listing함수를 실행시켜라

```jsx
<script>

        $(document).ready(function () {
            $('#cards-box').empty() //얘는 가짜자료들 삭제시키려고
            listing();
        });
....생략
```

listing함수는 요렇게 되어있음. ajax를 활용...핵심은 temp_html형식안에다가 넣어서 #cards-box라는 id가진 애한테 append시킨다는 것

```jsx
function listing() {
            $.ajax({
                type: "GET",
                url: "http://spartacodingclub.shop/post",
                data: {},
                success: function (response) {
                    let rows = response['articles']
                    for(let i=0; i<rows.length; i++){
                        let comment = rows[i]['comment']
                        let desc = rows[i]['desc']
                        let image = rows[i]['image']
                        let title = rows[i]['title']
                        let url = rows[i]['url']

                        let temp_html = `<div class="card">
                                            <img class="card-img-top"
                                                 src="${image}"
                                                 alt="Card image cap">
                                            <div class="card-body">
                                                <a href="${url}" class="card-title">${title}</a>
                                                <p class="card-text">${desc}</p>
                                                <p class="card-text comment">${comment}</p>
                                            </div>
                                        </div>`
                        $('#cards-box').append(temp_html)
                    }
                }
            })
        }
```

# 파이썬활용

미세먼지 api가 다음처럼 되어있음.

```jsx
{
RealtimeCityAir: {
list_total_count: 25,
RESULT: {
CODE: "INFO-000",
MESSAGE: "정상 처리되었습니다"
},
row: [
{
MSRDT: "202107062300",
MSRRGN_NM: "도심권",
MSRSTE_NM: "중구",
PM10: 37,
PM25: 20,
O3: 0.038,
NO2: 0.034,
CO: 0.4,
SO2: 0.004,
IDEX_NM: "보통",
IDEX_MVL: 68,
ARPLT_MAIN: "PM25"
},
{
MSRDT: "202107062300",
MSRRGN_NM: "도심권",
MSRSTE_NM: "종로구",
PM10: 36,
PM25: 21,
O3: 0.038,
NO2: 0.033,
CO: 0.5,
SO2: 0.005,
IDEX_NM: "보통",
IDEX_MVL: 68,
ARPLT_MAIN: "PM25"
},
....생략
```

파이썬 requests 라이브러리 설치하고

다음처럼 적음. 걍 r.json()하는건 이렇게 하는거구나 하면됨.

```jsx
import requests # requests 라이브러리 설치 필요

r = requests.get('http://openapi.seoul.go.kr:8088/6d4d776b466c656533356a4b4b5872/json/RealtimeCityAir/1/99')
rjson = r.json()
gus = rjson['RealtimeCityAir']['row']

for gu in gus:
    gu_name = gu['MSRSTE_NM']
    gu_mise = gu['IDEX_MVL']
    if(gu_mise>70):
        print(gu_name, gu_mise)
```

실행결과:

```jsx
성동구 72.0
중랑구 72.0
동대문구 73.0
성북구 75.0
도봉구 82.0
강북구 74.0
노원구 74.0
....생략
```

참고로 rjson만 print해보면 다음처럼 나오는데 ajax의 response랑 똑같.

```jsx
{'RealtimeCityAir': {'list_total_count': 25, 'RESULT': {'CODE': 'INFO-000', 'MESSAGE': '정상 처리되었습니다'}, 'row': [{'MSRDT': '202107062300', 'MSRRGN_NM': '도심권', 'MSRSTE_NM': '중구', 'PM10': 37.0, 'PM25': 20.0, 'O3': 0.038, 'NO2': 0.034, 'CO': 0.4, 'SO2': 0.004, 'IDEX_NM': '보통', 'IDEX_MVL': 68.0, 'AR
```

**암튼,** requests를 통해 쉽게 파싱할수있다.

# 크롤링

크롤링 예전에 함 해봐서 겁이 나진 않지만, 동시에 기억도 나지 않는다ㅋㅋ

BeautifulSoup을 써서

[네이버영화](https://movie.naver.com/movie/sdb/rank/rmovie.nhn?sel=pnt&date=20200303) 페이지를 크롤링해보자

일단, 코드는 다음과 같음

```jsx
import requests
from bs4 import BeautifulSoup

headers = {'User-Agent' : 'Mozilla/5.0 (Windows NT 10.0; Win64; x64)AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36'}
data = requests.get('https://movie.naver.com/movie/sdb/rank/rmovie.nhn?sel=pnt&date=20200303',headers=headers)

soup = BeautifulSoup(data.text, 'html.parser')

trs = soup.select('#old_content > table > tbody > tr')
for tr in trs:
    a_tag = tr.select_one('td>div>a')
    if(a_tag is not None):
        rank = tr.select_one('td:nth-child(1) > img')['alt']
        title = a_tag.text
        star = tr.select_one('td.point').text
        print(rank,title,star)
```

header는 자동으로 들어가는거 막기 위해 해주는것임.

*크롤링은 정답이 없어서 홈피를 선택할때마다 다르다*

- 규칙찾기.(영화제목을 찾는걸 예시로)

찾고싶은 곳에 마우스우클릭→검사→copy→copy selector

> #old_content > table > tbody > tr:nth-child(2) > td.title > div > a

바로 다음거는

> #old_content > table > tbody > tr:nth-child(3) > td.title > div > a

이렇게 하면 각이 나옴. 반복적으로 나오는 애까지만 다음처럼 select해주고,

```jsx
trs = soup.select('#old_content > table > tbody > tr')
```

trs를 print해보면서 반복문으로 그 하위 것들을 찾으면된다!

```jsx
for tr in trs:
    a_tag = tr.select_one('td>div>a')
	//가끔 a_tag가 none일때도 있어서ㅋ
    if(a_tag is not None):
        rank = tr.select_one('td:nth-child(1) > img')['alt']
        title = a_tag.text
        star = tr.select_one('td.point').text
        print(rank,title,star)
```

# 디비설치(몽고디비,Robo3t)

- robo3t?

처음 들어보는데, 몽고디비의 데이터들을 시각화해서 볼 수 있게 해주는 애임

참고로 db는 그냥 워드,한글,메모장 이런것처럼 데이터를 관리해주는 프로그램일 뿐임. 그중 하나가 몽고db임.

암튼 일단, 내 로컬호스트 서버에다가 db를 만든다.

```jsx
from pymongo import MongoClient
client = MongoClient('localhost', 27017)
db = client.dbsparta
```

글고 필요한 작업들을 하면 됨.

> nsert/find/update/delete

```jsx
# 저장 - 예시
doc = {'name':'bobby','age':21}
db.users.insert_one(doc)

# 한 개 찾기 - 예시
user = db.users.find_one({'name':'bobby'})

# 여러개 찾기 - 예시 ( _id 값은 제외하고 출력)
//나이조건 없이 다 찾고 싶을땐 걍 빈괄호find({},{'_id':False})
same_ages = list(db.users.find({'age':21},{'_id':False}))

# 바꾸기 - 예시
db.users.update_one({'name':'bobby'},{'$set':{'age':19}})

# 지우기 - 예시
db.users.delete_one({'name':'bobby'})
```
