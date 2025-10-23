package com.coffee.service;

import com.coffee.constant.Category;
import com.coffee.entity.Product;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

// Creteria : 판단이나 결정을 짓기 위한 기준
// Specification : JPA의 Creteria API를 사용하여 where 절(검색 조건)을 객체 지향적으로 만들기 위하여 사용되는 인터페이스입니다.
// 즉, JPA를 사용하여 동적 쿼리를 만들어 주는 인터페이스입니다.
public class ProductSpecification {
    /*
        사용자가 지정한 날짜 범위의 상품 목록만 필터링해주는 Specification입니다.
        searchDateType : 검색할 날짜 범위를 의미하는 문자열
        반환 값 : 검색된 범위의 상품 목록을 조회하기 위한 Specification 객체 정보
    */
    public static Specification<Product> hasDateRange(String searchDateType){
        return new Specification<Product>() {
            @Override
            public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                LocalDateTime  now = LocalDateTime .now() ; // 현재 시각

                LocalDateTime startDate = null ; // 검색 시작 일자

                // 선택한 콤보 박스의 값을 보고, 검색 시작 일자를 계산합니다.
                switch (searchDateType){
                    case "1d":
                        startDate = now.minus(1, ChronoUnit.DAYS) ;
                        break;
                    case "1w":
                        startDate = now.minus(1, ChronoUnit.WEEKS) ;
                        break;
                    case "1m":
                        startDate = now.minus(1, ChronoUnit.MONTHS) ;
                        break;
                    case "6m":
                        startDate = now.minus(6, ChronoUnit.MONTHS) ;
                        break;
                    case "all":
                    default: // 전체 기간 조회
                        return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
                }
                // 상품 입고 일자(inputdate)가 검색 시작 일자(startDate) 이후의 상품들만 검색합니다.
                return criteriaBuilder.greaterThanOrEqualTo(root.get("inputdate"), startDate);
            }
        };
    }

    /*
        사용자가 지정한 특정 카테코리에 속하는 상품 목록만 필터링해주는 Specification입니다.
        category : 검색할 카테고리 문자열
        반환 값 : 해당 카테고리에 해당하는 상품 목록을 조회하기 위한 Specification 객체 정보
    */
    public static Specification<Product> hasCategory(Category category){
        return new Specification<Product>() {
            @Override
            public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                System.out.println("카테고리 정보");
                System.out.println(category);
                if(category == Category.ALL){
                    // conjunction() 메소드는 논리적으로 항상 true인 객체를 반환해 줍니다.
                    // sql의 where 구문을 만들지 않습니다.
                    return criteriaBuilder.conjunction();
                }else{
                    return criteriaBuilder.equal(root.get("category"), category);
                }
            }
        };
    }

    /*
        상품 이름에 특정 키워드가 포함된 상품 목록만 필터링해주는 Specification입니다.
        keyword : 상품 이름에 포함될 키워드
        반환 값 : 상품 이름에 대한 키워드가 포함된 상품 목록들을 조회하기 위한 Specification 객체 정보
    */
    public static Specification<Product> hasNameLike(String keyword){
        return new Specification<Product>() {
            @Override
            public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.like(root.get("name"), "%" + keyword + "%");
            }
        };
    }

    /*
    상품 상세 설명에 특정 키워드가 포함된 상품 목록만 필터링해주는 Specification입니다.
    keyword : 상품 상세 설명에 포함될 키워드
    반환 값 : 상품 상세 설명에 대한 키워드가 포함된 상품 목록들을 조회하기 위한 Specification 객체 정보
*/
    public static Specification<Product> hasDescriptionLike(String keyword){
        return new Specification<Product>() {
            @Override
            public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.like(root.get("description"), "%" + keyword + "%");
            }
        };
    }
}
