jquery 보니까 id값이랑 class값 갖고올 때 주로 쓰고 다른건 그냥 바닐라로 하는듯.

- 특히 빽틱으로 쓰는거 신기.

```jsx
function q3() {
    // 1. input-q3 값을 가져온다.
    let newName = $('#input-q3').val();
    if (newName == '') {
        alert('이름을 입력하세요');
        return;
    }
    // 2. 가져온 값을 이용해 names-q3에 붙일 태그를 만든다. (let temp_html = `<li>${가져온 값}</li>`)
    let temp_html = `<li>${newName}</li>`;
    // 3. 만들어둔 temp_html을 names-q3에 붙인다.(jQuery의 $('...').append(temp_html)을 이용하면 굿!)
    $('#names-q3').append(temp_html);
}

//아래처럼 되어있는곳에 append 하는거임
================================
<ul id="names-q3">
      <li>세종대왕</li>
      <li>임꺽정</li>
</ul>
```

변수에다가 ``를 사용해서 코드를 지정하고, 붙일곳에 append하면 들어감.


### json쉽게보는 구글 익스텐션

[https://chrome.google.com/webstore/detail/jsonview/chklaanhfefbnpoihckbnefhakgolnmc?hl=ko](https://chrome.google.com/webstore/detail/jsonview/chklaanhfefbnpoihckbnefhakgolnmc?hl=ko)

# ajax

ajax는 jquery 있어야만 작동함. 

> <script src="[https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js](https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js)"></script>


*걍 ajax는 통째로 이렇게 생긴애구나!!*하는 게 맞음. 원리 이해하고 쓰는 사람 1도 없음

```jsx
$.ajax({
  type: "GET",
  url: "http://openapi.seoul.go.kr:8088/6d4d776b466c656533356a4b4b5872/json/RealtimeCityAir/1/99",
  data: {},
  success: function(response){
    console.log(response['RealtimeCityAir']['row'][0])
  }
})
```

위에서 data: post할때 쓰는거

url은 미세먼지 데이터가져옴

석세스하면 값이 response로 옴.

# ajax연습1 서울시미세먼지


```jsx
<script>
        function q1() {
            // 여기에 코드를 입력하세요
            $('#names-q1').empty();
            $.ajax({
                type: "GET",
                url: "http://openapi.seoul.go.kr:8088/6d4d776b466c656533356a4b4b5872/json/RealtimeCityAir/1/99",
                data: {},
                success: function (response) {
                    let rows = response['RealtimeCityAir']['row']
                    for (let i = 0; i < rows.length; i++) {
                        let gu_name = rows[i]['MSRRGN_NM']
                        let gu_mise = rows[i]['IDEX_MVL']

                        let temp_html='';
                        if(gu_mise>60) {
                          temp_html = `<li class="bad">${gu_name} : ${gu_mise} </li>`
                        }else{
                          temp_html = `<li>${gu_name} : ${gu_mise} </li>`
                        }
                        $('#names-q1').append(temp_html);
                    }
                }
            })
        }
    </script>
```
# 로딩되면 바로 실행
$(document).ready(function () {
        get_rate();
    });


```
<script>
    $(document).ready(function () {
        get_rate();
    });

    function get_rate(){
        $.ajax({
            type: "GET",
            url: "http://spartacodingclub.shop/sparta_api/rate",
            data: {},
            success: function (response) {
                let now_rate = response['rate'];
                $('#now-rate').text(now_rate);
            }
        })
    }

    function order() {
        alert('주문이 완료되었습니다!');
    }
</script>
```
