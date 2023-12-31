## Summary
gRPC 서비스의 안정적인 운영 방법과 gRPC 게이트웨이에 대한 내용

## Concept
- ### 7장 서비스 수준 gRPC 실행
    - #### gRPC 애플리케이션 테스트
        - ##### gRPC 서버, 클라이언트 테스트
            - Junit 이용
            - 모키토를 이용하여 서버 모킹
    - #### 배포
      - 도커로 애플리케이션을 컨테이너화 후 쿠버네티스를 이용하여 배포한다라고.. 한다

    - #### 관찰 가능성
      - 시스템 내부 상태가 외부 출력의 지식으로부터 얼마나 잘 추론될 수 있는지 나타내는 척도
      - 지금 시스템에 문제가 있는가?에 대한 대답을 하기 위한 것
        - 메트릭
            - 시스템 수준(system-level)
                - CPU 사용량
                - Memory 사용량등
            - 애플리케이션 수준(application-level)
                - 인바운드 요청률
                - 요청 에러율등
                - 오픈센서스(https://opencensus.io/guides/grpc/)
                - 프로메테우스(메트릭 수집 에이전트) & 그라파나(시각화 도구)
        - 로그
          - 시간이 지남에 따라 발생하는 불변의 타임스탬프된 개발 이벤트 기록
          - 메트릭보다 생성하기 쉽고 구체적
          - 단점은 메트릭에 비해 비용이 많이 든다는 점
          - gRPC 인터셉터를 이용하여 로깅처리
        - 추적
          - 엔드투엔드 요청 흐름을 구성하는 일련의 관련 이벤트를 나타낸다.
            - etc
                - slueth-fuel https://cheese10yun.github.io/slueth-fuel/

    - #### 디버깅과 문제 해결
      - 문제의 근본 원인을 찾고 애플리케이션에서 발생한 문제를 해결하는 프로세스    
      - 하위 환경(개발 또는 테스트 환경)에서 동일한 문제를 재현해야 한다   
      - 프로덕션 환경과 유사한 요청 부하를 생성하는 도구들이 필요

- ### 8장 gPRC 생태계
    - #### gRPC 게이트웨이
      - golang만 지원   
      - https://github.com/grpc-ecosystem/grpc-gateway 
    - #### gRPC를 위한 HTTP/JSON 트랜스코딩   
      - Restful API를 grpc로 변환하는 기법
  
    - #### gRPC 서버 리플렉션 프로토콜
      - 서버 리플렉션은 gRPC 서버에서 정의된 서비스로, 해당 서버에서 공개적으로 액세스 가능한 gRPC 서비스의 정보를 제공한다.   
      - 서버 리플렉션은 서버에 등록된 서비스의 서비스 정의를 클라이언트 애플리케이션에 제공 하는 것이다.   
      - 클라이언트는 서비스와 통신하고자 미리 컴파일된 서비스 정의가 필요하지 않다.   

## Advantages

## Disadvantages

## Example Case
Homework 참고

## Wrap-up
