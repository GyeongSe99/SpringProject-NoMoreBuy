# SpringProject-NoMoreBuy

뷰티 관련 절약 서비스의 REST API입니다. <br/><br/>
구매 내역을 카테고리 별로 나누어 보기 쉽게 정리 한 후 같은 카테고리, 비슷한 태그의 아이템을 구매한 후 등록할 경우 이미 비슷한 아이템이 있음을 알려주고 이를 통해 충동적인 소비 습관을 개선할 수 있는 서비스입니다. <br/><br/>
이 후 필요없는 상품은 중고거래로 등록할 수 있게 하여 보다 효율적인 뷰티 라이프를 즐길 수 있도록 합니다.<br/><br/>


## <개발환경>

Spring boot 2.7.14

Spring web

Spring security

Spring data jpa

Mysql
Websocket

Lombok

Validation

<br/>

## ERD

![nomorebuy table2](https://github.com/GyeongSe99/SpringProject-NoMoreBuy/assets/126084941/2b0c5e7f-c947-4ee4-8235-7f443c2d26a9)
<br/><br/>
## <회원 관련 기능>

[회원 가입 기능]

- 카카오 로그인 API를 이용하여 로그인 후 사용자 정보(이메일)를 통해 기존 회원인지 판단합니다. 기존 회원이 아닌 경우, 사용자 정보로 회원 가입을 진행합니다.
  
- 회원가입 요청 값
  
  | 이름  | 타입  | 설명  | 필수  |
  | --- | --- | --- | --- |
  | 회원 이메일 | String | 회원 이메일.<br/>카카오 API를 통해 가져온 이메일 정보를 사용하므로 사용자가 입력하지 않습니다. | O   |
  | 회원 명 | String | 회원 닉네임.<br/>(선택) 카카오 API를 통해 가져온 닉네임 정보를 사용할 수도 있습니다. | O   |
  | 회원 계좌번호 | String | 중고 거래에 사용될 계좌번호로 거래 시에만 노출됩니다. | X   |
  | 오픈 카카오톡 링크 | String | 중고 거래 시 판매자와 구매자 사이의 대화수단으로 사용할 수 있는 오픈 카카오톡 링크를 입력합니다. | X   |
  
<br/>
[로그인 기능]

- 카카오 로그인 API를 이용하여 로그인합니다.
  
<br/>
[회원 정보 조회 - 개인]

- 개인정보를 조회할 수 있습니다.
<br/>
[회원 정보 조회 - public]

- 본인이 아닌 사용자가 특정 회원 정보를 조회할 수 있습니다.
- 조회 목록
  
  - 회원 닉네임
  - 오픈 카카오톡 링크
  - 판매중인 상품
  
<br/>
[회원 정보 수정]

- 닉네임과 계좌번호, 오픈 카카오톡 링크를 설정하고 변경할 수 있습니다.

<br/><br/>

## <상품 관련 기능>
<br/>
[상품명 중복체크]
해당 유저가 등록한 상품 리스트에서 상품명 전체가 같거나 연속된 6글자 이상이 같을 때 같은 이름의 상품이 이미 등록되어있음을 상태코드로 반환합니다.
<br/>
[상품 정보 등록 - 일반]

- 온라인 및 오프라인에 구매한 상품을 등록합니다.
  
- 상품명 중복체크 이후 등록하기 전 비슷한 상품 등록 여부를 확인합니다.
  같은 카테고리, 같은 상세 카테고리, 태그 2개 이상이 일치하면 비슷한 리스트가 있다는 상태코드와 상품 리스트를 반환합니다.
  해당하지 않는 경우 기본 코드를 반환합니다.
  
- 상품 등록 요청값
  
  | 이름  | 타입  | 설명  | 필수  |
  | --- | --- | --- | --- |
  | 상품명 | String | 상품의 이름을 입력합니다. | O   |
  | 상품 가격 | int | 할인이 적용되지 않은 상품의 가격을 입력합니다. | O   |
  | 상품 카테고리 | enum | 상품의 카테고리를 선택합니다. <br/>enum타입으로 등록된 옷, 악세잡화 중에 하나를 선택합니다. | O   |
  | 상품 상세 카테고리 | enum | 상품의 상세 카테고리를 선택합니다. <br/> <br/>상세 카테고리 리스트 <br/>[옷] <br/> - 셔츠/블라우스 <br/> - 티셔츠 <br/> - 니트 <br/> - 가디건 <br/> - 아우터 <br/> - 원피스 <br/> - 스커트 <br/> - 팬츠 <br/>[악세잡화] | O   |
  | 상품 구매일 | DateTime | 상품 구매일자를 입력합니다. | O   |
  | 상품 구매 갯수 | INT | 상품 구매 갯수를 입력합니다. | O   |
  | 구매일 별 상품 구매가격 | int | 특정 구매일에 구매한 상품의 가격을 입력합니다. 할인율을 비교하는데 사용됩니다. | O   |
  | 태그  | String | 상품당 최대 5개의 태그를 입력할 수 있습니다. | X   |
  | 상품 색상 카테고리 | enum | 상품의 색상을 선택합니다. <br/>동일한 상품을 색상을 달리 사는 경우를 고려하여 색상 태그는 여러개 추가 가능합니다. | X   |
  | 상품 퍼스널 컬러 분류 | enum | 상품의 퍼스널 컬러 분류 카테고리를 선택합니다.<br/>동일한 상품을 색상을 달리 사는 경우를 고려하여 퍼스널컬러 분류 태그는 여러개 추가 가능합니다. | X   |
  | 별점  | int | 0~5까지의 점수를 입력합니다. | X   |
  | 즐겨찾기 등록여부 | boolean | 즐겨찾기 여부(true/false) | X   |
  | 재구매 의사 | enum | 재구매의사 있어요 - REBUY <br/>재구매의사 없어요 - NO_REBUY <br/>모르겠어요 - HOLD | X   |
  | 구매 이유 | String | 구매한 이유를 500자 내외 text로 입력합니다. | X   |
  | 사용 후기 | String | 사용한 후기를 500자 내외 text로 입력합니다. | X   |
  | 상품 이미지 | String | 상품 이미지 파일을 등록합니다. | X   |
  
<br/>
[등록된 상품 및 가격 조회]

- 요청값
  
  | 이름  | 타입  | 설명  | 필수  |
  | --- | --- | --- | --- |
  | 구매한 달 | int | 월별 구매 상품 조회 시 사용 됩니다. | X   |
  | 카테고리 | enum | 카테고리를 선택합니다. 카테고리에 해당하는 상품들을 조회할때 사용합니다. | X   |
  
- 전체 구매 상품 조회 : 어떠한 요청 값도 입력하지 않은 경우 전체 리스트를 조회한 시점의 달에 등록된 상품 전체를 조회합니다.
  
- 월별 구매 상품 조회 : 선택한 달에 등록된 상품 리스트를 조회합니다. 구매 일자가 현재와 가까운 날짜 순으로 정렬된 값을 조회합니다.
  
- 카테고리 별 상품 조회 :
  
  - 대분류 카테고리(옷, 악세잡화)에 포함되는 상품을 날짜 순으로 정렬된 값을 조회합니다.
  - 대분류 카테고리에서 상세 카테고리(중분류 카테고리) 선택 시 해당 상세 카테고리의 품목들을 현재와 가까운 날짜 순으로 조회할 수 있도록 합니다.
  - 카테고리, 색상 카테고리, 퍼스널컬러 카테고리, 태그는 개별의 카테고리로 중복 선택이 가능합니다.
- 재구매율 높은 순서대로 조회 : 재구매율 높은 순서대로 조회합니다.
  
<br/>
[상품 상세 정보 조회]

- 각 상품들에 대한 상세 정보를 조회합니다.
<br/>
[상품 상세 정보 수정]

- 각 상품들에 대한 상세 정보를 수정합니다.
<br/>
[재구매 상품 날짜 추가]

- 이미 등록된 상품을 재구매 한 경우 날짜만 추가하여 수정할 수 있도록 합니다.
<br/>
[상품 상세 정보 삭제]

- 각 상품들에 대한 상세 정보 삭제 기능

<br/><br/>
## <중고 거래 게시판 기능>
<br/>
[중고 거래 게시판 등록]

- 상품 등록되어 있는 상품 정보를 그대로 가져와서 저장.
- 게시글 정보
  
  - 판매자id
  - 상품 id
  - 거래 금액
  - 거래 장소 : 네이버 geotagging api를 이용하여 위치 정보를 받아와 도로명 주소로 저장합니다.
  - 거래 x 좌표
  - 거래장소 y 좌표
  - 거래진행중 여부(거래 진행중일 경우 거래 신청을 할 수 없도록 lock을 거는 용도)
  
<br/>
[중고 거래 게시판 조회]

모든 거래는 새로 등록된 순서대로 나열합니다.

- 전체 게시글 조회 : 위치 상관없이 새로 등록된 순서대로 모든 지역에 있는 거래를 나열합니다.
- 가까이 있는 거래 순으로 조회 : 네이버 geotagging api를 이용하여 받아온 현재 위치 정보를 기준으로 가까운순대로 나열합니다.
- 설정된 동네에 포함되는 거래 조회 : 동네 정보를 최대 3개 받아 그 동네와 일치하는 주소의 거래를 나열합니다.

<br/><br/>

## <중고 거래 기능>
<br/>
[중고 거래 기능]

- 카카오페이 api 결제 기능을 통해 관리
- 거래 진행 시 구매자가 관리자(사업주)의 계좌로 입금하면 판매자에게 입금 완료라는 알림을 보냅니다. 판매자가 입금 완료 알림을 확인하면 물건을 배송하고 물건을 받은 구매자가 확인 버튼을 누르고 판매자도 확인버튼을 누르면 판매자에게 입금되는 시스템.
- 거래가 일주일 이상 상태 변화가 없는 경우 자동으로 완료.
- 구매자 → 관리자 → 판매자 / 중간에서 관리자가 결제 관리함.

<br/><br/>
## <중고 거래 취소 기능>
<br/>
[중고 거래 취소/완료 기능 - 구매자 기능]

- 중고거래 취소 시 판매자 완료 상태 확인 후 / 관리자 → 구매자에게 재입금
- 중고거래 완료 시 관리자 → 판매자에게 입금
<br/>
[중고거래 취소/완료 기능 - 판매자 기능]

- 중고 거래 취소 시 관리자 → 구매자에게 재입금
