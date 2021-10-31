# 구독하기api

# 36강-연관관계 개념

이해를 했으면 공식화해서 걍 외우자

- FK는 many가 가져간다

- N:N의 관계에서는 중간테이블이 생긴다. 그리고 중간테이블이 1이 되고 원래테이블은 N이 됨

  →N:1:N

# 37강-모델만들기

@Table는 나중에 걍 복붙하면 돼 외우지마. 1번유저가 2번유자를 구독할 때, 또 똑같이 1→2 구독 데이터가 중복되는거 방지용임

```java
package com.cos.photogramstart.domain.subscibe;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.cos.photogramstart.domain.user.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(
	uniqueConstraints = {
		@UniqueConstraint(
		name="subscribe_uk",
		columnNames= {"fromUserId","toUserId"}
		)
	}
)
public class Subscribe {

	@Id//얘를 프라이머리키로 설정
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@JoinColumn(name="fromUserId")//이렇게 컬럼명 만들어! 맘대로 만들지말고
	@ManyToOne
	private User fromUser;
	
	@JoinColumn(name="toUserId")
	@ManyToOne
	private User toUser;
	
	private LocalDateTime createDate;
	
	@PrePersist
	public void createDate() {
		this.createDate = LocalDateTime.now();
	}
}
```

# 38강-구독,구독취소 api만들기

지금껏 몇번 했듯이, Controller, Service, Repository 이케 필오

- SubscribeRepository.java

jpa 또는 쿼리짜는곳

```java
package com.cos.photogramstart.domain.subscibe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
public interface SubscribeRepository extends JpaRepository<Subscribe,Integer>{

	//이번엔 그냥 native쿼리짜는게 편함
	@Modifying//insert, delete, update를 네이티브 쿼리로 작성하려면 해당 어노테이션 필요
	@Query(value="INSERT INTO subscribe(fromUserId,toUserId,createDate) VALUES(:fromUserId,:toUserId,now())", nativeQuery=true)
	void mSubscribe(int fromUserId, int toUserId);
	
	@Query(value="DELETE FROM subscribe WHERE fromUserId=:fromUserId AND toUserId=:toUserId", nativeQuery=true)
	void mUnSubscribe(int fromUserId, int toUserId);
}
```

- SubscribeService.java

repository에서 짠 쿼리 활용해서 실제 써먹을 함수 만드는 곳

```java
package com.cos.photogramstart.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.photogramstart.domain.subscibe.SubscribeRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SubscribeService {

	private final SubscribeRepository subscribeRepository;
	
	@Transactional
	public void 구독하기(int fromUserId, int toUserId) {
		subscribeRepository.mSubscribe(fromUserId, toUserId);
	}
	
	@Transactional
	public void 구독취소하기(int fromUserId, int toUserId) {
		subscribeRepository.mUnSubscribe(fromUserId, toUserId);
	}
}
```

- SubscribeApiController.java

service에서 만든 함수 활용해서 api만드는 곳

```java
package com.cos.photogramstart.web.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.service.SubscribeService;
import com.cos.photogramstart.web.dto.CMRespDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class SubscirbeApiController {
	
	private final SubscribeService subscribeService;

	@PostMapping("/api/subscribe/{toUserId}") 
	public ResponseEntity<?> subscribe(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable int toUserId){//누가(principalDetails), 누구(@PathVariable)를 구독했는지
		subscribeService.구독하기(principalDetails.getUser().getId(),toUserId);
		return new ResponseEntity<>(new CMRespDto<>(1,"구독하기 성공",null),HttpStatus.OK);
	}
	
	@DeleteMapping("/api/subscribe/{toSuerId}")
	public ResponseEntity<?> unsubscribe(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable int toUserId){
		subscribeService.구독취소하기(principalDetails.getUser().getId(),toUserId);
		return new ResponseEntity<>(new CMRespDto<>(-1,"구독취소하기 성공",null),HttpStatus.OK);
	}
}
```

# 39강-만든 api 예외처리

만약 1번유저가 2번유저 구독 또했어. 그럼 에러떠서 서버 터짐 지금. 처리해줘야 해

- 다른 익셉션.java 복붙해서 CustomApiException 하나 만들어주고

```java
public class CustomApiException extends RuntimeException {

	//객체 구분할때 쓰는건데 무시하삼 안중요..jvm한테 중요한거지 나한텐 안중요
	private static final long serialVersionUID = 1L;
	
	
	//얘는 UserService.java에서 orElseThrow()안에 넣으려고 생성자 새로 만든거
	public CustomApiException(String message) {
		super(message);
	}
	
}
```

- 익셉션핸들러해서 받아서 처리해줌

```java
@ExceptionHandler(CustomApiException.class)//CustomValidationException이 발동하는 모든 익셉션을 이 함수가 가로채
	public ResponseEntity<?> apiException(CustomApiException e) {
		//e.getMessage()는 ajax가 error.responseJSON.message로 보내지고, e.getErrorMap()는 error.reponseJSON.data로 보내짐
		return new ResponseEntity<>(new CMRespDto<>(-1,e.getMessage(),null),HttpStatus.BAD_REQUEST);
	}
```

- 이제 Service에서 실제로 익셉션을 throw해줌

```java
@RequiredArgsConstructor
@Service
public class SubscribeService {

	private final SubscribeRepository subscribeRepository;
	
	@Transactional
	public void 구독하기(int fromUserId, int toUserId) {
		try {
			subscribeRepository.mSubscribe(fromUserId, toUserId);
		} catch (Exception e) {
			throw new CustomApiException("이미 구독을 하였습니다");
		}
	}
	
	@Transactional
	public void 구독취소하기(int fromUserId, int toUserId) {
		subscribeRepository.mUnSubscribe(fromUserId, toUserId);
	}
}
```

포스트맨으로 1번유저로 2번 두번연속 구독요청 보내면([http://localhost:8080/api/subscribe/2](http://localhost:8080/api/subscribe/2))

다음처럼 결과나옴

{

"code": -1,

"message": "이미 구독을 하였습니다",

"data": **null**

}
