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
commit ;

-- 가입한 회원 중에서 '관리자'의 Role을 'ADMIN'으로 변경
update members set role = 'ADMIN' where email = 'admin@naver.com';
commit ;

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
select * from products where product_id = 62 ; 

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
-----------------------------------------------------------------
-- 주문 상품 세션
----------------------------------------------------------------- 
desc order_products ;

select * from order_products ;
--------------------------------------------------------------------------------------
-- 누가 무슨 상품을 주문했나요?

select m.member_id, m.name, o.order_id, op.product_id, op.quantity, p.name, p.price 
from ((members m join orders o 
on m.member_id = o.member_id) join order_products op
on o.order_id = op.order_id) join products p
on op.product_id = p.product_id ;
--------------------------------------------------------------------------------------

select * from products where image like '%bigs%';




