# sns login Refactoring
OAuth2.0기술에대해 이론을 정리하던 중 예전에 개발한 OAuth2.0을 활용한 SNS로그인 코드를 보게 되었습니다. 기존코드는 **FeignClient**을 사용하여 엑세스 토큰 및 유저 정보를 불러오는 방식을 사용했습니다.
하지만 기존의 코드를 분석해보니 다음과 같은 문제를 확인 할 수 있었습니다.
> SNSLogin Repository : https://github.com/delight-HK3/testproject.git

```
기술스택

Framework : Spring boot 3.3.6
Language : Java 17
Tool : Visual Studio Code
```

### 문제점

#### 2025/02/06 발견
1. FeginClient 대신 RestTemplate를 사용한 것은 좋았으나 확인해보니 deprecated될 뻔했던 기술인 것을 확인
   - RestTemplate 대신에 좀더 고도화된 RestClient를 도입할 계획

#### 2025/02/04 발견
1. SNS별로 Authorization서버주소, Resource서버주소 모두 다르기 때문에 주소별로 FeginClient를 만들어야한다.
   - 물론 기존의 FeginClient를 쓰면서 서버간 통신 코드가 간결해졌습니다, 만약 Authorization서버 및 Resource서버의 도메인이 같았으면 FeginClient를 계속 사용했을 것 입니다. 하지만 Authorization서버 및 Resource서버의 도메인이 모두 달랐고 결과적으로 서버요청 기능 파일이 늘어났습니다.

2. 똑같은 기능에 똑같은 코드의 반복하기에 중복 코드가 늘어난다.
   - accessToken과 Resource서버에서 유저정보를 가져오는 코드는 같은데 구현체마다 중복되는 코드가 생기게 되었습니다.

3. 코드의 가독성 저하문제

---

### 문제해결

#### 2025/04/05 (1번 문제해결)
(FeginClient 대신 RestTemplate를 사용한 것은 좋았으나 확인해보니 deprecated될 뻔했던 기술인 것을 확인)
- RestTemplate에서 RestClient로 변경했습니다.
```java
// RestTemplate 방식
HttpHeaders headers = new HttpHeaders();
headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

HttpEntity entity = new HttpEntity(params, headers);

ResponseEntity<JsonNode> responseNode = 
        restTemplate.exchange(accesstokenUrl, HttpMethod.POST, entity, JsonNode.class);

// RestClient 방식
ResponseEntity<JsonNode> responseNode = restClient.post()
                                .uri(accesstokenUrl)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .body(params)
                                .retrieve()
                                .toEntity(JsonNode.class);

```

#### 2025/04/05 (추가 문제해결)
기존에 controller에서 SNS별로 JsonNode 객체를 userResponse에 저장하는 로직을 작성했는데 가독성이 떨어지고 controller에서 처리하는 것 보다는 service로직에서 정리는 것이 좋다고 판단해 service에서 수정되도록 수정했습니다. 추가로 userResponse에 setter를 제거하고 빌더패턴으로 변경했습니다.

```java
// controller 방식
switch (userType) {
    case GOOGLE: { // 구글 sns로그인 출력결과 처리
        userresponse.setId(jsonResponse.get("id").asText());
        userresponse.setEmail(jsonResponse.get("email").asText());
        userresponse.setNickname(jsonResponse.get("name").asText());
        break;
    } case KAKAO: { // 카카오 sns로그인 출력결과 처리
        userresponse.setId(jsonResponse.get("id").asText());
        userresponse.setEmail(jsonResponse.get("kakao_account").get("email").asText());
        userresponse.setNickname(jsonResponse.get("kakao_account").get("profile").get("nickname").asText());
        break;
    } case NAVER: { // 네이버 sns로그인 출력결과 처리
        userresponse.setId(jsonResponse.get("response").get("id").asText());
        userresponse.setEmail(jsonResponse.get("response").get("birthday").asText());
        userresponse.setNickname(jsonResponse.get("response").get("nickname").asText());
        break;
    } default: {
        throw new RuntimeException("NONE SOCIAL TYPE");
    }
}

// service 방식 (구글 로그인)
// 구글 로그인 jsonNode 정리
@Override
public userResponse getResponse(JsonNode jsonNode) {
        return userResponse.builder()
                            .id(jsonNode.get("id").asText())
                            .email(jsonNode.get("email").asText())
                            .nickname(jsonNode.get("name").asText())
                            .build();
}
```


#### 2025/02/04 (1번 문제해결)
(SNS별로 Authorization서버주소, Resource서버주소 모두 다르기 때문에 주소별로 FeginClient를 만들어야한다.)
- FeginClient 대신 RestTemplate객체, HttpEntity객체를 사용하여 서버와 통신하고 값을 ResponseEntity로 받는방법을 사용했습니다. 또한 JsonNode를 사용하면서 복잡한 구조의 Response를 저장할 수 있게 되었습니다.

```java
// FeginClient 방식
@FeignClient(value = "googleAuth", url="https://oauth2.googleapis.com", configuration = {FeignConfiguration.class})
public interface GoogleAuthApi {
    @PostMapping("/token")
    ResponseEntity<String> getAccessToken(@RequestBody GoogleRequestAccessTokenDto requestDto);
}

// RestTemplate, HttpEntity, JsonNode 방식

// Authorization 서버를 통해 accesstoken 발급
private String getAccessToken(UserType userType, String authorizationCode){
    MultiValueMap<String,Object> params = new LinkedMultiValueMap<>();
    
    String accesstokenUrl = environment.getProperty("spring.OAuth2."+userType+".Authorization-url");
    
    // 각 sns별 Authorization 서버주소
    params.add("code",authorizationCode);
    params.add("client_id", environment.getProperty("spring.OAuth2."+userType+".client-id"));
    params.add("client_secret",environment.getProperty("spring.OAuth2."+userType+".client-secret"));
    params.add("redirect_uri", environment.getProperty("spring.OAuth2."+userType+".callback-url"));
    params.add("grant_type", "authorization_code"); 

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    HttpEntity entity = new HttpEntity(params, headers);

    ResponseEntity<JsonNode> responseNode = 
        restTemplate.exchange(accesstokenUrl, HttpMethod.POST, entity, JsonNode.class);
        
    JsonNode accessTokenNode = responseNode.getBody();

    return accessTokenNode.get("access_token").asText();
}
```

<br>

#### 2025/02/04 (2번 문제해결)
똑같은 기능에 똑같은 코드의 반복하기에 중복 코드가 늘어난다.

- 중복코드는 Authorization 서버 및 Resource 서버 요청 코드였기에 이 문제는 어떻게 보면 properties 설정을 @Value 으로 받아왔기에 유연하게 받는 것이 불가능했습니다. 그래서 Environment 객체를 활용해 properties 설정 파일값을 받아오는 방식으로 해결했습니다, userType을 변수로 받아 SNS별로 설정값이 다르게 입력되도록 변경했습니다. 

```java
// @Value 어노테이션 방식
@Value("${spring.OAuth2.Naver.client-id}")
private String NAVER_SNS_CLIENT_ID;

@Value("${spring.OAuth2.Naver.client-secret}")
private String NAVER_SNS_CLIENT_SECRET;

// Environment 방식
environment.getProperty("spring.OAuth2."+userType+".client-id"));
environment.getProperty("spring.OAuth2."+userType+".client-secret"));
``` 

<br>

#### 2025/02/04 (3번 문제해결)
- 코드 가독성문제는 기능별로 Authorization서버역할과 Resource서버의 역할을 확실히 구분지어 주석으로 작성했습니다.

<br>

#### 2025/02/04 예상치 못한 성과
원래는 SNS 로그인과 일반 로그인의 service를 공유하고 있었습니다. 그래서 일반 로그인과 SNS 로그인의 로직을 완전히 분리해 보니 프로그램의 확장성과 유연성이 상승하는 결과를 볼 수 있었습니다.

---

### 실행 시 주의사항
1. application.properties 파일의 SNS 클라이언트 및 비밀키를 사용자가 발급받은 것으로 입력 해야합니다.
2. 각 SNS별 callback 주소도 사용자가 설정한 것으로 입력해야합니다.
<br>

### 리펙토링 작업과 관련된 블로그 게시글
https://velog.io/@half-phycho/Refactoring-sns-로그인-리펙토링
<br>

### 참고한 git Repository
https://github.com/darren-gwon/springboot_oauth_example.git
