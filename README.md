# 트리플 여행자클럽 마일리지 서비스
## 사용 기술

- JAVA 8, Spring Boot, Spring Data JPA, MySQL

## 실행 방법

```mysql
create user triple@localhost identified by 'triple';
create database triple;
grant all privileges on triple.* to triple@localhost;
```

```
git clone https://github.com/han-gyeong/TRIPLE_homework.git
cd TRIPLE_homework
java -jar ./build/libs/TravelerMileage-0.0.1-SNAPSHOT.jar
```

## DDL
- ORM 을 이용하였으므로 자동으로 생성되지만, 참고를 위해 작성하였습니다.

```sql
create table POINT_HISTORY
(
    id      binary(16) primary key,
    amount  integer      not null,
    comment varchar(255) not null,
    user_id binary(16)   not null
);

create table REVIEW_HISTORY
(
    review_id    binary(16) primary key,
    earned_point integer    not null,
    has_content  boolean    not null,
    has_photo    boolean    not null,
    is_first     boolean    not null,
    place_id     boolean    not null,
    user_id      binary(16) not null
);
```

## API 문서
#### POST /events

- 요청 방식
```http request
POST /events
```

- 요청 데이터 예시
```json
{
  "type": "REVIEW",
  "action": "ADD",
  "reviewId": "240a0158-dc5f-4878-9381-ebb7b2667213",
  "content": "이쁩니다!",
  "attachedPhotoIds": [
    "ABCD",
    "ABCE",
    "ABCF"
  ],
  "userId": "3ede0ef2-92b7-4817-a5f3-0c575361f742",
  "placeId": "2e4baf1c-5acb-4efb-a1af-eddada31b00f"
}
```

- 응답 데이터 예시
```json
{
  "message": "정상처리 되었습니다.",
  "content": null
}
```

#### GET /point/{userId}

- 요청 방식
```http request
GET /point/{userId}

예시) localhost:8080/point/3ede0ef2-92b7-4817-a5f3-0c575361f742
```

- 응답 결과 예시
```json
{
  "message": "정상처리 되었습니다.",
  "content": 3
}
```