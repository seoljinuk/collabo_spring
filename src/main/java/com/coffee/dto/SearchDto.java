package com.coffee.dto;

import com.coffee.constant.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// 상품에 대한 필드 검색시 상품의 검색 조건시 사용하는 클래스입니다.
@Getter @Setter @ToString
@AllArgsConstructor // 인스턴스 변수 모두를 매개 변수로 입력 받는 생성자를 만들어 줍니다.
public class SearchDto {
    // 조회할 날짜 검색 범위를 선정하기 위한 변수, 현재 시간과 상품 입고일을 비교하여 처리합니다.
    // all(전체 기간), 1d(하루), 1w(일주일), 1m(한달), 6m(여섯달)
    private String searchDateType ; // 기간 검색 콤보 박스

    private Category category ; // 검색하고자 하는 특정 카테고리 콤보 박스

    // 상품 검색 모드 콤보 박스_상품 이름(name) 또는 상품 설명(description)
    private String searchMode ;

    private String searchKeyword ; // 검색 키워드 입력 상자
}
