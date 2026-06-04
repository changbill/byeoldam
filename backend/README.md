# ByeolDam Backend

ByeolDam Backend는 "별을 담다" 서비스의 Spring Boot API 서버다. 사용자가 이미지 기반 게시물을 만들고, 게시물을 별자리 단위로 묶어 관리하며, 다른 사용자와 팔로우 관계 및 공유 별자리를 구성할 수 있게 한다.

## 주요 기능

- 이메일 회원가입, 로그인, JWT 기반 인증, 토큰 갱신, 로그아웃
- Google, Naver, Kakao OAuth2 로그인 연동
- 이메일 인증 코드 발송 및 검증
- 사용자 프로필, 공개/비공개 설정, 프로필 이미지 관리
- 팔로우 요청, 승인, 취소, 팔로워/팔로잉 조회
- 게시물 생성, 수정, 삭제, 복원, 조회, 좋아요
- 해시태그 기반 게시물 검색과 제목 검색
- 별자리 생성, 수정, 삭제, 사용자 공유, 권한 변경, 좋아요
- 별자리 윤곽선 이미지와 좌표 데이터 관리
- 댓글과 대댓글 생성, 수정, 삭제, 조회
- 이미지 업로드 및 AWS S3 저장
- AI 이미지 분석용 Django 서버 연동

## 기술 스택

- Java 21
- Spring Boot 3.2.2
- Spring Web, Spring Security, Spring OAuth2 Client
- Spring Data JPA, MariaDB
- Spring Data Redis
- Spring Data MongoDB
- Lombok
- springdoc-openapi Swagger UI
- AWS S3 SDK
- Gradle
- Docker Compose

주의: `Dockerfile`의 런타임 이미지는 `openjdk:17`이지만 Gradle 설정은 Java 21을 사용한다. 컨테이너 실행 환경과 빌드 타깃 버전을 맞추는 것이 좋다.

## 실행 요구사항

로컬 실행에는 다음 외부 의존성이 필요하다.

- MariaDB: `localhost:3306`, database `byeol_dam`
- Redis: `localhost:6379`
- MongoDB: `localhost:27017`, database `byeol_dam`
- AWS S3 버킷 및 접근 권한
- Gmail SMTP 앱 비밀번호
- OAuth2 클라이언트 ID/Secret
- JWT Secret

필수 환경변수:

```text
SPRING_DATASOURCE_USERNAME
SPRING_DATASOURCE_PASSWORD
SPRING_PROFILES_ACTIVE
JWT_SECRET_KEY_CODE
JWT_TOKEN_SECRET
EMAIL_APP_PASSWORD
GOOGLE_OAUTH_CLIENT_ID
GOOGLE_OAUTH_CLIENT_SECRET
NAVER_OAUTH_CLIENT_ID
NAVER_OAUTH_CLIENT_SECRET
KAKAO_OAUTH_CLIENT_ID
KAKAO_OAUTH_CLIENT_SECRET
```

## 로컬 인프라 실행

애플리케이션을 직접 실행하고 데이터 저장소만 Docker로 띄우려면 다음을 사용한다.

```bash
docker compose -f docker-compose-local.yml up -d
```

전체 스택을 Compose로 실행하려면 다음을 사용한다.

```bash
docker compose up -d --build
```

## 애플리케이션 실행

```bash
./gradlew bootRun
```

Windows PowerShell에서는 다음을 사용할 수 있다.

```powershell
.\gradlew.bat bootRun
```

기본 프로필은 `local`이다. `prod` 프로필은 서비스명을 기준으로 MariaDB, Redis, MongoDB에 접근하고 서버 포트를 `8081`로 사용한다.

## 테스트

```bash
./gradlew test
```

현재 테스트 파일은 기본 컨텍스트 테스트와 일부 별자리/윤곽선 테스트가 포함되어 있다. `src/main/resources/application-test.yml`은 비어 있으므로 테스트 환경 격리는 아직 정리되지 않았다.

## API 문서

애플리케이션 실행 후 Swagger UI에서 컨트롤러별 API를 확인할 수 있다.

```text
/swagger-ui/index.html
/v3/api-docs
```

대부분의 서비스 API는 `/api/v1` 하위에 있다. 별자리 API는 `/api/v1/constellations`, AI API는 `/api/v1/ai`를 사용한다.

## 디렉터리 구조

```text
src/main/java/com/ssafy/star
  ai/                AI 이미지 분석 Django 서버 연동
  article/           게시물, 해시태그, 게시물 좋아요
  comment/           댓글과 대댓글
  common/            공통 설정, 응답, 예외, S3, 이미지 유틸
  constellation/     별자리, 공유 사용자, 별자리 좋아요
  contour/           MongoDB 기반 윤곽선 좌표 데이터
  global/auth/       JWT 인증 필터와 토큰 유틸
  global/email/      이메일 인증 코드 발송/캐시
  global/oauth/      OAuth2 로그인 처리
  image/             이미지 메타데이터
  search/            게시물/별자리/사용자 검색
  user/              사용자, 팔로우, 리프레시 토큰
```

## 운영상 주의사항

- JWT 인증은 Stateless 방식이며 `/api/**` 대부분은 인증이 필요하다.
- 공개 API는 회원가입, 로그인, 이메일 인증, 이메일/닉네임 중복 확인, 일부 공개 프로필/카운트 조회다.
- 게시물과 사용자는 soft delete를 사용한다. 게시물은 복원 기능이 있고, 사용자는 `deleted_at is NULL` 조건이 적용된다.
- 별자리 윤곽선 데이터는 MongoDB에 저장되고, 별자리 엔티티는 MongoDB 문서 ID를 `contour_id`로 참조한다.
- 이미지 파일은 S3에 업로드된다. AI 분석 임시 이미지는 S3에 업로드한 뒤 Django 서버 응답 후 삭제를 시도한다.
- CORS 설정은 `application.yml`의 `cors.*` 속성으로 관리한다.
- 외부 Django AI 서버 주소가 코드에 하드코딩되어 있다.
