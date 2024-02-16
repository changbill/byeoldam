# 포팅 메뉴얼

---

## 1. 프로젝트 개요

많은 사람들이 자신의 일상을 공유, 기록하기 위해 SNS 서비스, 인스타그램을 많이 활용한다. 하지만 해당 서비스를 이용하는 많은 사람은 자신의 행복한 모습, 자랑거리 등의 보여주기식 사진들을 주로 게시하고, 광고 목적의 사진들이 주로 올라온다. 때문에 사람들의 행복한 게시물을 보며 그렇지 않은 사람들은 불안함을 느끼게 된다. 과연 인스타그램과 같은 SNS 서비스가 우리에게 긍정적인 영향을 주는지 생각하게 되었고, 사진의 가치가 서비스에 가려 많이 묻힌다고 생각하였다.

따라서 우리 9조는 많은 사람들에게 사진이 주는 가치를 일깨워 주고 싶었고, 사진의 가치가 조금 더 부각될 수 있도록 환경을 제공하고자 프로젝트를 기획하게 되었다. 갤러리 형식의 별자리 SNS ‘별을담다’는 나의 추억이 담긴 사진들을 나만의 별자리로 만들어 기록하고, 사진 각각에 대해 private/public 권한을 두어 오롯이 나의 사진들을 보관하고 볼 수 있도록 하였다.

사람들이 ‘별을담다’를 통해 사진으로 순간을 기록하고, 사람들과 공유하고 추억을 나누면 어떨까요?

## 2. 프로젝트 사용 도구

- 이슈관리 : JIRA, Git Kraken, SourceTree
- 형상관리 : Gitlab, Gerrit
- 커뮤니케이션 : notion, Mattermost
- 디자인 : Figma
- UCC : 프리미어 프로, 애프터이펙트
- CI/CD : Jenkins

## 3. 개발 환경

- VSCode
- Intellij IDEA Ultimate 2023.3
- Java 17
- Spring Boot 3.2.2
- Gradle 8.5
- Node.JS v20.10.0
- DJango
- Detectron

## 4. 기술 세부 스택<Spring Boot>

- Lombok : 1.18.30
- Spring Data JPA : 3.2.2
- Spring security : 6.2.1
- Spring-oauth2 : 6.2.1
- Mariadb-driver : 3.3.2
- Spring-data-redis : 3.2.2
- Spring-data-mongodb : 4.11.1
- starter-mail : 3.0.5
- MongoDB : 4.2.2
- Spring doc : 2.0.2
- Spring-cloud-aws : 2.2.6
- jsonwebtoken : 0.11.5

## 5. 기술 세부 스택<AI>

- nvidia-driver : 12.1
- nvcc : 12.1
- detectron2 : 0.6
- pytorch : 2.1.1
- torchvision : 0.16.1
- ninja : 1.11.1.1
- pip : 23.3.1
- shapely : 2..0.2
- pandas : 2.2.0
- opencv-python : 4.9.0.80

## 6. 기술 세부 스택<프론트엔드>

- react : 18.2.0
- react-router-dom : 6.22.0
- konva : 9.3.2
- react-konva : 18.2.10
- three : 0.160.0
- @react-three/drei : 9.96.1
- react-three/fiber : 8.15.14
- color-thief-node : 1.0.4
- image-js : 0.35.5

## 7. 기술 세부 스택<Infra>

- Docker 25.0.0
- Docker-compose 1.25.0
- Jenkins : 2.426.2
- Nginx : 1.18.0

## 8. 외부 서비스

- Google OAuth
- Kakao OAuth
- Naver OAuth
- S3 Bucket

## 9. Gitignore

- DB Volume 내용
- .env
- node_modules

## 10. 빌드

### 1. 환경변수 형태(백엔드)

SPRING_DATASOURCE_USERNAME= {데이터베이스 계정 이름}

SPRING_DATASOURCE_PASSWORD={데이터베이스 계정 비밀번호}

JWT_SECRET_KEY_CODE={JWT 토큰 암호화를 위한 키 값}

JWT_TOKEN_SECRET= {리프레시 토큰 전용 시크릿 값}

EMAIL_APP_PASSWORD={이메일 서비스 시크릿 값}

SPRING_PROFILES_ACTIVE={스프링 yml 실행파일 선택}

GOOGLE_OAUTH_CLIENT_ID={구글 소셜 클라이언트 ID}

GOOGLE_OAUTH_CLIENT_SECRET={구글 소셜 클라이언트 시크릿}

KAKAO_OAUTH_CLIENT_ID={카카오 소셜 클라이언트 ID}

KAKAO_OAUTH_CLIENT_SECRET={카카오 소셜 클라이언트 시크릿}

NAVER_OAUTH_CLIENT_ID={네이버 소셜 클라이언트 ID}

NAVER_OAUTH_CLIENT_SECRET={네이버 소셜 클라이언트 시크릿}
VITE_BASE_URL={서버 api 베이스 url}
VITE_OUATH_URL={OAuth 인증 URL}
VITE_GOOGLE_REDIRECT_URI={구글 리다이렉트 URL}
VITE_NAVER_REDIRECT_URI={네이버 리다이렉트 URL}
VITE_KAKAO_REDIRECT_URI={카카오 리다이렉트 URL}

### 2. 환경변수 형태(프론트엔드)

### 3. 빌드하기

1. 프론트

- npm i
- npm run build

2. 스프링 부트

- ./gradlew build -x test 로 app.jar 생성
- docker-compose 실행(docker-compose -f docker-compose-local.yml up)

3. 장고

- pip install -r requirements.txt
- python [manage.py](http://manage.py/) migrate
- python [manage.py](http://manage.py) runserver

### 4. 배포하기

1. Nginx

```jsx
server {
    server_name byeoldam.site; # managed by Certbot

        location / {
                proxy_set_header        HOST $http_host;
                proxy_set_header        X-Real-IP $remote_addr;
                proxy_set_header        X-Forwarded-For $proxy_add_x_forwarded_for;
                proxy_set_header        X-Forwarded-Proto $scheme;
                proxy_set_header        X-NginX-Proxy true;
                proxy_pass http://localhost:5173;
        }

        location /api {
                proxy_set_header        HOST $http_host;
                proxy_set_header        X-Real-IP $remote_addr;
                proxy_set_header        X-Forwarded-For $proxy_add_x_forwarded_for;
                proxy_set_header        X-Forwarded-Proto $scheme;
                proxy_set_header        X-NginX-Proxy true;
                proxy_pass http://localhost:8081;
        }

        location /oauth2 {
                proxy_set_header        HOST $http_host;
                proxy_set_header        X-Real-IP $remote_addr;
                proxy_set_header        X-Forwarded-For $proxy_add_x_forwarded_for;
                proxy_set_header        X-Forwarded-Proto $scheme;
                proxy_set_header        X-NginX-Proxy true;
                proxy_pass http://localhost:8081;
        }

        client_max_body_size 5M;

    listen [::]:443 ssl ipv6only=on; # managed by Certbot
    listen 443 ssl; # managed by Certbot
    ssl_certificate /etc/letsencrypt/live/byeoldam.site/fullchain.pem; # managed by Certbot
    ssl_certificate_key /etc/letsencrypt/live/byeoldam.site/privkey.pem; # managed by Certbot
    include /etc/letsencrypt/options-ssl-nginx.conf; # managed by Certbot
    ssl_dhparam /etc/letsencrypt/ssl-dhparams.pem; # managed by Certbot
}

server {
    if ($host = byeoldam.site) {
        return 301 https://$host$request_uri;
    } # managed by Certbot

        listen 80 ;
        listen [::]:80 ;
    server_name byeoldam.site;
    return 404; # managed by Certbot

}
```

2. Jenkins 빌드 스텝

```bash
# env 파일을 jenkins의 ByeolDam에 복사
cp /home/ubuntu/.env /var/lib/jenkins/workspace/ByeolDam/
cp /home/ubuntu/.env /var/lib/jenkins/workspace/ByeolDam/frontend

# app.jar 만들기
cd /var/lib/jenkins/workspace/ByeolDam/backend
chmod +x ./gradlew
./gradlew build -x test

cd /var/lib/jenkins/workspace/ByeolDam
docker compose down
docker compose build
docker compose up -d
```
