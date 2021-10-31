첨 시작할때 application.yml파일이 있어

이 파일은 스프링프레임워크로 어떤 공장을 만들건데, 공장을 만들기 위한 문서임

ex)

1. 두번째 공장

    첫번째 공장이 아니고 두번째 공장을 짓는것

    → context-path: /

2. 공장 입구는 서쪽

    서쪽으로 와야 들어올수있어!

    →port:8080

3. 음료요청은 전부 한글문서로 변경해서 받는다
→charset: utf-8

    ```yaml
    //위1~3번
    server:
      port: 8080
      servlet:
        context-path: /
        encoding:
          charset: utf-8
          enabled: true
    ```

4. 음료는 전부 콜라로 만들어서 출시된다

    →jsp로 만들어서 응답한다는것(json, xml, html 등으로 바꿀수있음)

    ```yaml
    spring:
      mvc:
        view:
          prefix: /WEB-INF/views/
          suffix: .jsp
    ```

5. 음료창고는 컨테이너 박스를 사용한다

    벽돌집, 비닐하우스 등등 정하기 가능. 여기선 마리아디비

    →driver-class-name: org.mariadb.jdbc.Driver

    ```yaml
    datasource:
        driver-class-name: org.mariadb.jdbc.Driver
        url: jdbc:mariadb://localhost:3306/photogram?serverTimezone=Asia/Seoul
        username: cos
        password: cos1234
    ```

6. 음료는 요청에 따라 캔 , 패트병으로 출시된다

    →JPA(ORM)으로 원하는 대로 출시됨. 🔥

7. 공장이 재가동 될때 기존에 만들었던 음료는 버리지 않는다

    → ddl-auto: update....만약 create로 설정하면 재가동할때마다 새로만듦

    ```yaml
    jpa:
        open-in-view: true
        hibernate:
          ddl-auto: update
          naming:
            physical-strategy: org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
        show-sql: true
    ```

8. 음료요청시 a4용지 2장 이상의 문서는 받지 않는다

    요청이 2메가 넘어가면 안받는다

    →max-file-size: 2MB

    ```yaml
    servlet:
        multipart:
          enabled: true
          max-file-size: 2MB
    ```

9. 음료요청은 아무나 할 수 없다. 암호를 아는 사람만 요청한다.

    ```yaml
    security:
        user:
          name: test
          password: 1234
    ```
