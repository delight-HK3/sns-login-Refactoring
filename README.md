## 작업계기 
OAuth2.0기술에대해 이론을 정리하던 중 예전에 개발한 OAuth2.0을 활용한 SNS로그인 코드를 보게 되었습니다. 기존코드는 **FeignClient**을 사용하여 엑세스 토큰 및 유저 정보를 불러오는 방식을 사용했습니다.
하지만 기존의 코드를 분석해보니 다음과 같은 문제를 확인 할 수 있었습니다.
> SNSLogin Repository : https://github.com/delight-HK3/testproject.git

<br>

### 현재 코드의 문제점
1. SNS별로 Authorization서버주소, Resource서버주소 모두 다르기 때문에 주소별로 FeginClient를 만들어야한다.
2. 똑같은 기능에 똑같은 코드의 반복하기에 중복 코드가 늘어난다.
3. 코드의 가독성 저하

물론 기존의 FeginClient방식이 좋지않은 것은 아닙니다, FeginClient를 쓰면서 서버간 통신 코드가 간결해졌고 만약 Authorization서버 및 Resource서버의 도메인이 같았으면 FeginClient를 계속 사용했을 것 입니다. 하지만 Authorization서버 및 Resource서버의 도메인이 모두 달랐고 결과적으로 서버요청 기능 파일이 늘어났습니다.

<br>

### 문제해결
1번문제
- FeginClient 대신 RestTemplate객체, HttpEntity객체를 사용하여 서버와 통신하고 값을 ResponseEntity로 받는방법을 사용했습니다.
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
또한 JsonNode를 사용하면서 복잡한 구조의 Response를 저장할 수 있게 되었습니다.

<br>

2번문제
- 중복코드는 Authorization서버 및 Resource서버 요청코드 였기에 이 문제는 어떻게 보면 properties설정을 @Value 어노테이션으로 받아왔기에 유연하게 받는 것이 불가능했습니다. 그래서 Environment객체를 활용해 properties설정파일 값을 받아왔습니다.

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
위의 방식처럼 userType을 변수로 받아 SNS별로 설정값이 다르게 입력되도록 변경했습니다. 

3번문제
- 코드 가독성문제는 기능별로 Authorization서버역할과 Resource서버의 역할을 확실히 구분지어 주석으로 작성했습니다.

<br>

### 실행 시 주의사항
1. application.properties 파일의 SNS 클라이언트 및 비밀키를 사용자가 발급받은 것으로 입력 해야합니다.
2. 각 SNS별 callback 주소또한 사용자가 설정한 것으로 입력 해야합니다.
<br>

### 참고한 git Repository
https://github.com/darren-gwon/springboot_oauth_example.git

