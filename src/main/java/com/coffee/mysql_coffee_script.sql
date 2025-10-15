-- coffee 데이터 베이스 삭제
drop database coffee ;

-- coffee 데이터 베이스 생성
create database coffee ;

-- 데이터 베이스 목록 확인
show databases;

-- coffee 데이터 베이스를 사용하겠습니다.
use coffee ;

-- 테이블 목록을 보여 주세요.
show tables ;

-- auto_increment : 숫자가 1씩 자동으로 커짐, 오라클의 시퀀스와 유사
create table coffee(
	id int auto_increment primary key,
	name varchar(50) not null,
	type varchar(30),
	price decimal(5, 2) not null 
);

insert into coffee (name, type, price) values
('Espresso', 'Espresso', 3.50),
('Latte', 'Milk Coffee', 4.50),
('Cappuccino', 'Milk Coffee', 4.00),
('Americano', 'Black Coffee', 3.00);

commit;

select * from coffee;

-----------------------------------------------------------------
-- 회원 세션
----------------------------------------------------------------- 
-- 테이블 구조 보기
desc members ;

select * from members ; 

delete from members ;
delete from members where member_id >= 50 ;
commit ;

-- 가입한 회원 중에서 '관리자'의 Role을 'ADMIN'으로 변경
update members set role = 'ADMIN' where email = 'admin@naver.com';
commit ;

delete from cart_products ;
delete from carts ;
delete from order_products ;
delete from orders ;

-----------------------------------------------------------------
-- 상품 세션
----------------------------------------------------------------- 
-- 테이블 구조 보기
describe products ;

-- 상품 개수
select count(*) from products ; 

select * from products ; 

-- 상품 목록 페이지 : 상품 조회시 id를 역순으로 조회해서 보여 주기
select * from products order by product_id desc ; 

-- 특정 상품 조회하기
select * from products where product_id = 266 ; 

delete from products ;
commit ;
-----------------------------------------------------------------
-- 카트 세션
----------------------------------------------------------------- 
desc carts ;

select * from carts ;
-----------------------------------------------------------------
-- 카트 상품 세션
----------------------------------------------------------------- 
desc cart_products ;

select * from cart_products ;

-- 연관 관계를 맺고 있어서, 삭제시 순서가 필요합니다.
drop tables cart_products;
drop tables carts;
-----------------------------------------------------------------
-- 장바구니 담기 테스트 시나리오
-----------------------------------------------------------------
-- 로그인할 사람의 id를 확인하세요.
select * from members ;

-- 장바구니에 담을 상품의 id를 확인하세요.
select * from products order by product_id desc ;

-- 카트 정보 조인하기
select m.member_id, m.name, c.cart_id, cp.cart_product_id, cp.product_id, cp.quantity, p.name, p.price, p.stock
from ((members m join carts c
on m.member_id = c.member_id) join cart_products cp
on c.cart_id = cp.cart_id) join products p
on cp.product_id = p.product_id ;

delete from cart_Products;
delete from cart;
commit;
-----------------------------------------------------------------
-- 주문 세션
----------------------------------------------------------------- 
desc orders ;

select * from orders ;

select * from orders where order_id = 3 ;
-----------------------------------------------------------------
-- 주문 상품 세션
----------------------------------------------------------------- 
desc order_products ;

select * from order_products ;
--------------------------------------------------------------------------------------
-- 카트 내역 데이터 지우기
delete from cart_products ;
delete from carts ;
commit ;
--------------------------------------------------------------------------------------
-- 주문 내역 데이터 지우기
delete from order_products ;
delete from orders ;
commit;
--------------------------------------------------------------------------------------
-- 누가 무슨 상품을 주문했나요?

select m.member_id, m.name, o.order_id, op.product_id, op.quantity, p.name, p.price 
from ((members m join orders o 
on m.member_id = o.member_id) join order_products op
on o.order_id = op.order_id) join products p
on op.product_id = p.product_id 
order by order_id desc, product_id asc ;
--------------------------------------------------------------------------------------
-- 주문 상태 `삭제` 테스트
--------------------------------------------------------------------------------------
-- (1) 주문 번호 4번이라고 가정
select * from orders where order_id = 4 ;

-- (2) 주문 상품 조회해서 product_id와 quantity 컬럼 확인
select product_id, quantity from order_products where order_id = 4 ;
-- 63	7
-- 62	5
-- 61	3

-- (3) 상품들의 재고 수량 파악
select product_id, stock from products where product_id in(61, 62, 63);
-- 61	768
-- 62	101
-- 63	433
	
-- (4) 화면에서 해당 상품 '취소'하기

-- (5) (1), (2), (3) 다시 실행
--------------------------------------------------------------------------------------
-- 홈페이지 관련 sql
select * from products where image like '%bigs%';
--------------------------------------------------------------------------------------
-- 페이징 처리 관련 sql
--------------------------------------------------------------------------------------
SET @pageNumber := 2;
SET @pageSize := 6;
SET @myoffset := (@pageNumber - 1) * @pageSize;

-- SELECT *
-- FROM products
-- ORDER BY product_id DESC
-- LIMIT (@pageSize) OFFSET (@myoffset);

---------------

-- 이 코드는 MySQL에서 사용하는 UPDATE + WITH(공통 테이블 식, CTE) 구문으로,
-- products 테이블에 있는 행들의 inputdate 값을 
-- 행 번호 순서대로 1일 전, 2일 전, 3일 전… 식으로 날짜를 변경하는 SQL 문입니다.
-- 임의의 날짜로 모두 초기화합니다.
UPDATE products SET inputdate = '2025-01-01';

WITH Ranked AS (
    select product_id, inputdate, ROW_NUMBER() OVER (ORDER BY product_id) AS rn
    from products
)
UPDATE products p
JOIN Ranked r ON p.product_id = r.product_id
SET p.inputdate = CURDATE() - INTERVAL r.rn DAY;

commit ;

desc products;
-- 필드 검색
select * from products order by product_id desc ;

-- 1주일 전까지 검색
select * 
from products
WHERE inputdate >= NOW() - INTERVAL 1 WEEK
ORDER BY product_id DESC;

select count(*) 
from products
WHERE inputdate >= NOW() - INTERVAL 1 WEEK
ORDER BY product_id DESC;

-- 1개월 전까지 검색
select * 
from products
WHERE inputdate >= NOW() - INTERVAL 1 MONTH
ORDER BY product_id DESC;

select count(*) 
from products
WHERE inputdate >= NOW() - INTERVAL 1 MONTH
ORDER BY product_id DESC;


-- 카테고리 검색
-- 카테고리가 ~~인 것만 검색
SET @category = 'beverage';

select * 
from products
WHERE category = upper(@category)
ORDER BY product_id DESC;

select count(*) 
from products
WHERE category = upper(@category)
ORDER BY product_id DESC; 

-- 카테고리가 ~~이고, 상품 이름이 ~~를 포함하는 데이터 검색
SET @category = 'cake';
SET @myname = '%바닐라%' ;

select * 
from products
WHERE name LIKE @myname
AND category = upper(@category)
ORDER BY product_id DESC;

select count(*) 
from products
WHERE name LIKE @myname
AND category = upper(@category)
ORDER BY product_id DESC;

-- 카테고리가 ~~이고, 상품 설명이 ~~를 포함하는 데이터 검색
SET @category = 'cake';
SET @mydescription = '%새콤%' ;

select * 
from products
WHERE description LIKE @mydescription
AND category = upper(@category)
ORDER BY product_id DESC;

select count(*) 
from products
WHERE description LIKE @mydescription
AND category = upper(@category)
ORDER BY product_id DESC;

