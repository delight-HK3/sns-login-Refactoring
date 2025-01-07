## 작업계기 
OAuth관련 이론을 정리하던 중 예전에 제작한 snslogin 코드를 보게 되었습니다 기존에는 FeignClient을 사용하여 엑세스 토큰 및 유저 정보를 불러오는 방식을 사용했습니다 하지만 기존의 코드에 다음과 같은 문제를 지적했습니다.

### 문제점 
1. 한 FeignClient파일에서 하나의 서버만 통신이 가능하기에 sns방식이 추가될 때 마다 FeignClient파일을 늘려야한다.
2. 똑같은 기능에 똑같은 코드의 반복
3. 코드의 가독성 저하

물론 기존의 FeginClient방식이 나쁘다는 것은 아닙니다, FeginClient를 쓰면서 서버간 통신 코드가 간결해졌고 만약 인증서버 및 리소스 서버의 호출 도메인이 같았으면 FeginClient를 계속 사용했을 것 입니다. 하지만 인증 및 리소스 서버의 URL이 전부
달랐기에 수정할 수 밖에 없었습니다.
<br><br>
이 Repository에 있는 코드는 기존의 testproject Repository에 있는 sns로그인 기능을 수정하고자 만들었기에
DB 커넥션 풀 및 기존에 testproject에 있는기능은 존재하지 않습니다.

> 기존 snslogin 기능 Repository : https://github.com/delight-HK3/testproject.git
<br>

### 결과화면

![화면 캡처 2025-01-07 151432](https://github.com/user-attachments/assets/b65485d3-2134-4772-99ae-fa8a9d4e02d6)

프로젝트를 실행하고 localhost:8099/ 으로 이동하면 다음과 같은 화면이 출력됩니다.<br>
상단의 sns 로그인 방식 3가지 중 1개를 선택하여 로그인을 진행하면 터미널창에 해당하는 유저의 정보를 출력합니다.<br>

### 문제해결
- 한 FeignClient파일에서 하나의 서버만 통신이 가능하기에 sns방식이 추가될 때 마다 FeignClient파일을 늘려야한다.
  - 이 문제점은 FeginClient보다 앞서나온 RestTemplte와 기존에는 @Value 어노테이션을 활용해 properties파일을 가져왔으나 Environment 객체를 활용해 설정 값을 가져오는 방식을 유연화 시켰습니다.


## 참고한 git repository
https://github.com/darren-gwon/springboot_oauth_example.git <br>
https://github.com/vvsungho/social-login-server.git
