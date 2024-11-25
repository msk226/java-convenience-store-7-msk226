# java-convenience-store-precourse

# 구조

````
> store 
    > controller
        - StoreController.java
    > converter
        - OrderConverter.java
        - ProductConverter.java
        - PromotionConverter.java 
    > model
        - Inventory.java
        - Order.java
        - OrderResult.java
        - Product.java
        - Promotion.java
        - Store.java   
    > service
        - StoreService.java
    > utils
        > constant
            - OrderConstant.java
            - ProductConstant.java
            - PromotionConstant.java
         > message
            - ErrorMessage.java
            - InputMessage.java
            - OutputMessage.java
    > view
        - InputView.java
        - OutputView.java
    - Application.java
````
---
# 클래스 역할과 책임

## Controller

### StoreController
- InputView, OutputView를 통해 사용자 입력을 받고 출력하는 역할
- 적절한 서비스 메서드를 호출하여 주문 및 결제 정보를 제공.
- 비즈니스 로직을 통해 얻은 값을 OutputView를 통해 반환 

---

## Converter

### OrderConverter
- 입력 받은 주문을 객체로 변환.
- 입력받은 주문 정보를 내부 모델인 `Order` 객체로 변환.

### ProductConverter
- `products.md` 에서 읽어온 정보를 받아 `Product` 객체로 변환.

### PromotionConverter 
- `promotions.md`에서 읽어온 정보를 받아 `Promotion` 객체로 변환 

---

## Model

### Inventory
- 상품의 재고를 조회하고, 재고를 차감하는 기능.
- 프로모션과 관련된 재고 관리를 처리.

### Order
- 고객이 주문한 상품과 수량을 저장.
- 주문에 대한 기본 정보 제공 (상품명, 수량 등).

### OrderResult
- 주문 후 결과를 처리 (가격 계산, 프로모션 적용 여부 등).
- 최종 결제 금액과 할인 적용 금액 계산.

### Product
- 상품의 가격, 이름, 프로모션 정보를 관리.

### Promotion
- 프로모션의 조건과 적용 방법 정의.
- 프로모션에 따른 증정품 로직 관리 

### Store
- 전체 재고 및 상품을 관리.
- 주문을 입력 받아 `Inventory`에게 전달하고, 결과를 받는 역할

---

## Service
### StoreService
- 재고 및 주문과 관련된 주요 로직을 처리.
- 프로모션 적용 여부 판단 및 할인 계산.
- 결제 관련 서비스.

---

## Utils

### OrderConstant
- 주문 처리 시 필요한 상수들을 정의

### ProductConstant
- 입력 받은 상품 정보를 파싱하기 위한 상수들을 정의 

### PromotionConstant
- 입력 받은 프로모션 정보를 파싱하기 위한 상수들을 정의

### ErrorMessage
- 에러 발생 시 출력할 에러 메시지들을 정의.

### InputMessage
- 사용자에게 입력을 요청할 때, 출력할 메시지들을 상수로 정의.

### OutputMessage
- 최종 출력 메시지를 상수로 정의

---
## View
### InputView
- 사용자로부터 입력을 받는 메소드를 가지고 있는 클래스

### OutputView
- 사용자에게 출력을 하는 메소드를 가지고 있는 클래스

---


# 기능 명세서 

## 1. 입/출력 처리

### - **상품 입력**
#### **출력**
- [x] `안녕하세요. W편의점입니다.` 메시지 출력: 프로그램이 시작될 때, 사용자를 맞이하는 인사 메시지가 출력됩니다.
- [x] `현재 보유하고 있는 상품입니다.` 메시지 출력: 상품 목록을 출력하기 전, 안내 메시지가 띄워져 사용자가 현재 재고 목록을 확인할 수 있습니다.
- [x] 보유하고 있는 상품 출력: `StoreController` 클래스에서 `storeService`를 통해 보유 중인 상품 목록을 출력합니다.

#### **입력**
- [x] `promotions.md` 파일로 부터 프로모션 정보 읽어오기: `promotions.md` 파일에서 프로모션 정보를 읽고, 해당 정보를 객체로 파싱합니다. 프로모션의 시작과 종료 날짜, 구매 수량, 증정 수량의 유효성도 체크합니다.
- [x] `products.md` 파일로 부터 상품 정보 읽어오기: `products.md` 파일에서 상품 정보를 읽어, 상품의 가격이 0 이상인지 검증한 후 `Product` 객체로 변환합니다.
    - 상품의 가격은 항상 0 이상이어야 합니다.
    - 프로모션의 구매 수량 및 증정 수량은 0 이상이어야 하며, 프로모션의 시작 날짜는 종료 날짜 이전이어야 한다는 조건을 검사합니다.

### - **주문 입력**
- [x] 사용자에게 구매할 상품 입력 받기: 사용자에게 입력 받은 주문 문자열을 파싱하여 상품을 조회합니다.
    - 예시 입력: `[사이다-2],[감자칩-1]`
- [x] `구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])` 메시지 출력: 사용자가 상품과 수량을 입력할 때 안내 메시지를 출력합니다.

### - **Y/N 입력**
- [x] 사용자에게 Y/N 입력 받기:
    - [x] 멤버십 할인 여부 확인: `멤버십 할인을 받으시겠습니까? (Y/N)` 메시지 출력
    - [x] 프로모션 적용이 가능한 상품에 대해 고객이 해당 수량만큼 가져오지 않았을 경우:
        - [x] `현재 {상품명}은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)` 메시지 출력
    - [x] 프로모션 재고가 부족하여 일부 수량을 프로모션 혜택 없이 결제해야 하는 경우:
        - [x] `현재 {상품명} {수량}개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)` 메시지 출력
    - [x] 추가 구매 여부 확인:
        - [x] `감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)` 메시지 출력

## 2. 상품 및 프로모션 조회

### - **상품 조회**
- [x] 사용자에게 입력 받은 주문 문자열 파싱하여 상품 조회
    - [x] 상품이 존재하지 않을 시 예외 발생
    - [x] 주문 받은 갯수가 재고보다 많을 시 예외 발생

### - **프로모션 조회**
- [x] 상품에 해당하는 프로모션 조회
    - [x] 프로모션 기간에 해당하지 않는 경우
    - [x] 프로모션 상품 재고가 남아있지 않은 경우
    - [x] 프로모션 적용이 가능한 상품에 대해 고객이 해당 수량만큼 가져오지 않았을 경우

## 3. 결제

### - **총 구매액**
- [x] 구매한 상품의 가격 구하기

### - **행사 할인**
- [x] 프로모션으로 구매한 상품의 가격 구하기

### - **멤버십 할인**
- [x] 프로모션 미적용 금액 계산하기
- [x] 프로모션 미적용 금액의 30% 할인
- [x] 멤버십 할인 한도 확인 (8,000원)

### - **내실 돈**
- [x] 모든 금액을 계산하여 지불해야 할 가격 구하기

## 4. 예외

- [x] 사용자가 잘못된 값을 입력할 경우 `IllegalArgumentException`를 발생시키고, "[ERROR]"로 시작하는 에러 메시지를 출력 후 그 부분부터 입력을 다시 받기
    - [x] 결제 실패 시, 변경 된 재고 다시 원래 상태로 되돌리기
