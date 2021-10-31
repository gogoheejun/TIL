# 79강-유효성검사 자동화-AOP처리 1

AOP = Aspect Oriented Programming.관점지향프로그래밍

공통기능과 핵심기능이란 게 있어. 

핵심기능은 로그인, 회원가입 이런거고, 공통기능은 전처리(유효성검사,보안), 후처리(로그남기기)인데, 오히려 공통기능이 핵심기능보다 더 길기도 해.

근데 공통기능은 로그인이나 회원가입이나 댓글이나 다 똑같은 코드야. 그래서 공통기능이라고 함.

그래서 필터처리를 하면 핵심기능에서 코드가 짧아짐.

예를 들면 회원정보업데이트 컨트롤러(UserApiController)보면 에러처리하는게 더길어. 저 코드를 따로 빼자는거임

```jsx
@PutMapping("/api/user/{id}")
	public CMRespDto<?> update(
			@PathVariable int id, 
			@Valid UserUpdateDto userUpdateDto, 
			BindingResult bindingResult,//위치 개중요. 꼭@Valid가 적혀있는 애 다음 파라미터로 적어야됨
			@AuthenticationPrincipal PrincipalDetails principalDetails){
		
		if(bindingResult.hasErrors()) {
			Map<String,String> errorMap = new HashMap<>();
			for(FieldError error: bindingResult.getFieldErrors()){
				errorMap.put(error.getField(), error.getDefaultMessage());
			}
			throw new CustomValidationApiException("유효성검사 실패함",errorMap);
		}else {
			User userEntity = userService.회원수정(id, userUpdateDto.toEntity());
			principalDetails.setUser(userEntity);//세션정보변경
			return new CMRespDto<>(1,"회원수정완료",userEntity); //응답시 userEntity의 모든 getter함수가 호출되고 JSON으로 파싱해서 응답함
		}
	}
```

그러기 위해선 외부라이브러리 하나 추가

pom에 추가

```jsx
<!-- https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-aop -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
    <version>2.4.5</version>
</dependency>
```

ValidationAdvice만들기-짱신기

일단 다음처럼 뼈대 만듦. @Around, @Before,@After,@Component,@Aspect 등등 유용한 어노테이션들을 붙임

```jsx
@Component //ioc에 띄워야 하는데, 애매할땐 component. restController,Service이런애들이다 component를 상속해서 만든애들임
@Aspect //이걸 써야 aop할수있는 애가 됨
public class ValidationAdvice {

	@Around("execution(*com.cos.photogramstart.web.api.*Controller.*(..))") //어떤 특정함수시작 전에 시작해서 끝날때까지 관여하기...@Before는 시작전에 발동, @After는 끝난담에 발동
	public Object apiAdvice(ProceedingJoinPoint proceedingJoinPoint)throws Throwable {
		//proceedingJoinPoint란, 접근하는 클래스의 함수의 모든곳에 접근할 수 있는 변수임.

		return proceedingJoinPoint.proceed();
	}
	
	@Around("execution(*com.cos.photogramstart.web.*Controller.*(..))")
	public Object advice(ProceedingJoinPoint proceedingJoinPoint)throws Throwable {
		
		return proceedingJoinPoint.proceed();
	}
}
```

---

# 80강-유효성검사 자동화-AOP처리 2

proceedingJoinPoint가 어떤애들 갖고있는지 보고싶어서 다음처럼 해봄

```jsx
@Component //ioc에 띄워야 하는데, 애매할땐 component. restController,Service이런애들이다 component를 상속해서 만든애들임
@Aspect //이걸 써야 aop할수있는 애가 됨
public class ValidationAdvice {

	@Around("execution(* com.cos.photogramstart.web.api.*Controller.*(..))") //어떤 특정함수시작 전에 시작해서 끝날때까지 관여하기...@Before는 시작전에 발동, @After는 끝난담에 발동
	public Object apiAdvice(ProceedingJoinPoint proceedingJoinPoint)throws Throwable {
		//proceedingJoinPoint란, 접근하는 클래스의 함수의 모든곳에 접근할 수 있는 변수임.
		
		Object[] args = proceedingJoinPoint.getArgs();
		for(Object arg: args) {
			System.out.println(arg);
		}
		
		return proceedingJoinPoint.proceed();//다시 돌아가서 원래함수 실행시킴
	}
	
	@Around("execution(* com.cos.photogramstart.web.*Controller.*(..))")
	public Object advice(ProceedingJoinPoint proceedingJoinPoint)throws Throwable {
		
		Object[] args = proceedingJoinPoint.getArgs();
		for(Object arg: args) {
			System.out.println(arg);
		}
		
		return proceedingJoinPoint.proceed();
	}
}
```

아 근데 또 터져.

com.cos.photogramstart.domain.user.User.images, could not initialize proxy - no Session

User가 image부를때 무한참조 걸려서 저러는거임.

아니 @JsonIgnoreProperties했는데?왜 저럼? 

지금은 내가 걍 한번 print하려는거라서 그런거임. json으로 파싱해서 부르는 게아니라;

다음처럼 프린트하면 toString이 지금 자동으로 되는데,  image 부분 삭제해버리면 됨

```jsx
@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", name=" + name + ", website="
				+ website + ", bio=" + bio + ", email=" + email + ", phone=" + phone + ", gender=" + gender
				+ ", profileImageUrl=" + profileImageUrl + ", role=" + role + ", createDate="
				+ createDate + "]";
	}
```

자 이제 로그인해보면,

콘솔에 다음이 찍힘

```jsx
PrincipalDetails(user=User [id=1, username=ssar, password=$2a$10$gV.ZRp9frmndbzeUVikTIOGdnSbFubhmUsQoaNRNBAQrF7J3ZC/Dm, name=쌀, website=null, bio=null, email=ssar@nate.com, phone=null, gender=null, profileImageUrl=879227e0-cac6-4a6b-a647-ae9d76e16d32_cat2.jfif, role=ROLE_USER, createDate=2021-07-27T00:02:20.753363])
Page request [number: 0, size 3, sort: UNSORTED]
```

저건 ImageApiController에서 다음함수의 인자가 찍힌거임

```jsx
@GetMapping("api/image")
	public ResponseEntity<?> imageStroy(@AuthenticationPrincipal PrincipalDetails principalDetails,
			@PageableDefault(size=3) Pageable pageable){
		Page<Image> images = imageService.이미지스토리(principalDetails.getUser().getId(), pageable);
		return new ResponseEntity<>(new CMRespDto<>(1,"성공",images),HttpStatus.OK);
	}
```

댓글을 써보면

콘솔에 다음이 찍힘

```jsx
CommentDto(content=test댓글!, imageId=4)
org.springframework.validation.BeanPropertyBindingResult: 0 errors
PrincipalDetails(user=User [id=1, username=ssar, password=$2a$10$gV.ZRp9frmndbzeUVikTIOGdnSbFubhmUsQoaNRNBAQrF7J3ZC/Dm, name=쌀, website=null, bio=null, email=ssar@nate.com, phone=null, gender=null, profileImageUrl=879227e0-cac6-4a6b-a647-ae9d76e16d32_cat2.jfif, role=ROLE_USER, createDate=2021-07-27T00:02:20.753363])
```

이것도 CommentApiController의 댓글api 메소드의 인자를 다 들고 온거임

```jsx
@PostMapping("/api/comment")
	public ResponseEntity<?> commentSave(@Valid @RequestBody CommentDto commentDto,BindingResult bindingResult,@AuthenticationPrincipal PrincipalDetails principalDetails) {
```

위의 콘솔결과를 보면 BeanPropertyBindingResult도 들고 오는 걸 볼 수 있음. 즉 ValidationAdvice 에서 에러처리를 할 수 있단거임.

와우~~

다음처럼 원래 각각의 api에 있던 에러처리하는 애들 데려와서 여기다 넣으면 된다!

```jsx
public class ValidationAdvice {

	@Around("execution(* com.cos.photogramstart.web.api.*Controller.*(..))") //어떤 특정함수시작 전에 시작해서 끝날때까지 관여하기...@Before는 시작전에 발동, @After는 끝난담에 발동
	public Object apiAdvice(ProceedingJoinPoint proceedingJoinPoint)throws Throwable {
		//proceedingJoinPoint란, 접근하는 클래스의 함수의 모든곳에 접근할 수 있는 변수임.
		
		Object[] args = proceedingJoinPoint.getArgs();
		for(Object arg: args) {
			if(arg instanceof BindingResult) {
				System.out.println("유효성 검사를 하는 함수입니다!!");
				BindingResult bindingResult = (BindingResult) arg;
				
				if(bindingResult.hasErrors()) {
					Map<String,String> errorMap = new HashMap<>();
					for(FieldError error: bindingResult.getFieldErrors()){
						errorMap.put(error.getField(), error.getDefaultMessage());
					}
					throw new CustomValidationApiException("유효성검사 실패함",errorMap);
				}
			}
		}
		
		return proceedingJoinPoint.proceed();//다시 돌아가서 원래함수 실행시킴
	}
...
```

참고로 throw가 있는곳에선 핸들러로 넘기기 때문에 그 위치에서 종료되고 그 아래부터는 다 무효가 됨.

이제 원래CommentApiController에 있던 BindingResult를 처리하던 if문 없애도 된다.깔끔!

```jsx
public class CommentApiController {

	private final CommentService commentService;

	@PostMapping("/api/comment")
	public ResponseEntity<?> commentSave(@Valid @RequestBody CommentDto commentDto,BindingResult bindingResult,@AuthenticationPrincipal PrincipalDetails principalDetails) {
		
		Comment comment = commentService.댓글쓰기(commentDto.getContent(), commentDto.getImageId(), principalDetails.getUser().getId());//content,imageId,userId 날림
		return new ResponseEntity<>(new CMRespDto<>(1,"댓글쓰기성공",comment),HttpStatus.CREATED);
	}
...
```

UserApiController의 update(), AuthController의 signup함수도 마찬가지.

단, 다음처럼 BindingResult를 통해 에러처리하지 않는애들은 깍둑이임. 어쩔수없..

```jsx
@PostMapping("/image")
	public String imageUpload(ImageUploadDto imageUploadDto, @AuthenticationPrincipal PrincipalDetails principalDetails){
		
		if(imageUploadDto.getFile().isEmpty()) {
			throw new CustomValidationException("이미지가 첨부되지 않았습니다", null);
		}
```

끝!

정리해보면, 유효성검사할때 필요한건 앞으로 

DTO만들고 validation(낫블랭크 같은거) 걸어주고, 컨트롤러에서 파라미터 받을때 valid넣고 bindingResult넣으면 끝!!
