package com.coffee.common;

import com.coffee.constant.Category;
import com.coffee.entity.Product;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

// 테스트 용도를 위하여 '회원', '상품' 등의 임시 데이터를 생성하기 위한 자바 클래스
public class GenerateData {
    // 주의) SpringBoot가 아닌 일반 Test 모드에서는 @Value를 사용할 수 없습니다.
    private final String imageFolder = "c:\\shop\\images" ;

    // 특정 폴더 내에 들어 있는 모든 이미지 파일의 이름을 List 컬렉션으로 반환해 줍니다.
    public List<String> getImageFileNames() {
        // File : 파일이나 폴더를 객체 형태로 다루고자 할때 사용하는 클래스
        File folder = new File(imageFolder);
        List<String> imageFiles = new ArrayList<>() ; // 이미지 이름들을 저장할 컬렉션

        // exists()는 해당 객체가 실제로 존재하면 true
        // isDirectory()는 해당 객체가 폴더이면 true
        if(!folder.exists() || !folder.isDirectory()){
            System.out.println(imageFolder + " 폴더가 존재하지 않습니다");
            return imageFiles ;
        }

        // 추가 코딩 예정
        String[] imageExtensions = {".jpg", ".jpeg", ".png"}; // 관심 있는 파일의 확장자
        File[] fileList = folder.listFiles(); // 파일 객체 목록

        // 모든 파일의 이름을 소문자로 변경 후 확장자와 비교후 조건에 부합하면 컬렉션에 추가합니다.
        if (fileList != null) {
            for (File file : fileList) {
                if (file.isFile() && Arrays.stream(imageExtensions)
                        .anyMatch(ext -> file.getName().toLowerCase().endsWith(ext))) {
                    imageFiles.add(file.getName());
                }
            }
        }

        return imageFiles ;
    }

    public Product createProduct(int index, String imageName) {
        Product product = new Product();

        switch (index % 3) {
            case 0:
                product.setCategory(Category.BREAD);
                break;
            case 1:
                product.setCategory(Category.BEVERAGE);
                break;
            case 2:
                product.setCategory(Category.CAKE);
                break;
        }

        String productName = getProductName();
        product.setName(productName);
        String description = getDescriptionData(productName);
        product.setDescription(description);
        product.setImage(imageName);
        product.setPrice(1000 * getRandomDataRange(1, 10));
        product.setStock(111 * getRandomDataRange(1, 9));
        LocalDate sysdate = LocalDate.now();
        product.setInputdate(sysdate.minusDays(index));
        return product;
    }

    private int getRandomDataRange(int start, int end) {
        // start <= somedata <= end
        return new Random().nextInt(end) + start;
    }

    private String getDescriptionData(String name) {
        String[] description = {"엄청 달아요.", "맛있어요.", "맛없어요.", "떫어요.", "엄청 떫어요.", "아주 떫어요.", "새콤해요.", "아주 상큼해요.", "아주 달아요."};
        return name + "는(은) " + description[new Random().nextInt(description.length)];
    }

    private String getProductName() {
        String[] fruits = {"아메리카노", "바닐라라떼", "우유", "에스프레소", "크로아상", "치아바타", "당근 케이크"};
        return fruits[new Random().nextInt(fruits.length)];
    }

}
