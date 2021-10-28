## CloneCoin-BackEnd


### 아키텍쳐
#### < MSA 구조 >
- DDD의 전략적 설계 방식을 사용하기 위해 팀원들과 Event Storming 진행
- Event, Command, Actor, Interface, Aggregate, Policy를 파악하여 서버를 Bounded Context 단위로 나눔
- ServiceDiscovery(Eureka) 서버를 구축하고 서버 등록
- Gateway를 통하여 filter를 거친 뒤 올바른 end-point로 라우팅

#### < WalletWrite >
- Wallet server를 WalletWrite, WalletRead로 나누어 CQRS 패턴 적용
- WalletWrite = CRUD 중 CUD 담당 (Command 담당 서버)
- WalletRead = CRUD 중 R 담당 (Query 담당 서버)
- 이를 통해 WalletWrite 서버의 부하를 줄이고, 조회대기 시간도 줄임
- 두 서버의 데이터 일관성을 맞추기 위해 비동기 통신 Kafka 사용
- WalletWrite 서버가 장애를 일으켜도 조회 서비스는 사용 가능

---
### 기술 스택
- IDE : Intellij
- SpringBoot : 2.4.11
- maven
- Java 11
- DB = local:H2 / dev:H2 / prod:AWS RDS(Mysql)
- JPA
- 비동기 통신 = Kafka
- 배포 = AWS EC2
- Swagger
---
### 기능구현
- 리더의 일일 수익률 계산
- WalletRead로 정보 제공
---
### 구조
- adaptor
- config
- domain
- repository
- rest
- service
