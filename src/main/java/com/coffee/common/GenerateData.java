package com.coffee.common;

import com.coffee.constant.Category;
import com.coffee.entity.Product;

import java.io.File;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class GenerateData {

    private final String imageFolder = "c:\\shop\\images";
    private final Random random = new Random();

    // 이미지 폴더 내 파일명 가져오기
    public List<String> getImageFileNames() {
        File folder = new File(imageFolder);
        List<String> imageFiles = new ArrayList<>();

        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println(imageFolder + " 폴더가 존재하지 않습니다");
            return imageFiles;
        }

        String[] imageExtensions = {".jpg", ".jpeg", ".png"};
        File[] fileList = folder.listFiles();

        if (fileList != null) {
            for (File file : fileList) {
                if (file.isFile() && Arrays.stream(imageExtensions)
                        .anyMatch(ext -> file.getName().toLowerCase().endsWith(ext))) {
                    imageFiles.add(file.getName());
                }
            }
        }
        return imageFiles;
    }

    // 상품 생성
    public Product createProduct(String imageName) {
        Product product = new Product();

        String lower = imageName.toLowerCase();

        // 카테고리 결정
        if (lower.contains("americano") || lower.contains("latte") || lower.contains("milk") ||
                lower.contains("coffee") || lower.contains("cappuccino") || lower.contains("juice") ||
                lower.contains("wine")) {
            product.setCategory(Category.BEVERAGE);
        } else if (lower.contains("croissant") || lower.contains("ciabatta") ||
                lower.contains("brioche") || lower.contains("baguette") || lower.contains("scone") ||
                lower.contains("pretzel") || lower.contains("muffin")) {
            product.setCategory(Category.BREAD);
        } else if (lower.contains("cake") || lower.contains("macaron") ||
                lower.contains("pie") || lower.contains("tart")) {
            product.setCategory(Category.CAKE);
        } else {
            product.setCategory(Category.ALL);
        }

        // 이름 추출
        String name = formatNameFromImage(imageName);
        product.setName(name);

        // 설명 자동 생성 (랜덤 맛/형용사 조합)
        String[] tastes = {"달콤하고", "고소하고", "부드럽고", "상큼하고", "진한", "담백하고", "촉촉한", "향긋한"};
        String[] features = {"풍미가 느껴져요", "맛이 나요", "향이 가득해요", "식감이 좋아요", "기분이 좋아져요"};
        String desc = String.format("%s는 %s %s.", name,
                tastes[random.nextInt(tastes.length)],
                features[random.nextInt(features.length)]);
        product.setDescription(desc);

        // 이미지 파일명
        product.setImage(imageName);

        // 100원 단위 가격 설정
        int price = switch (product.getCategory()) {
            case BEVERAGE -> getPriceRangeHundreds(3500, 6000);
            case BREAD -> getPriceRangeHundreds(2000, 5000);
            case CAKE -> getPriceRangeHundreds(5000, 9000);
            default -> 3000;
        };
        product.setPrice(price);

        // 재고 (10 단위)
        product.setStock(getRandomRangeTens(50, 200));

        // 등록일
        product.setInputdate(LocalDate.now().minusDays(getRandomRange(1, 30)));

        return product;
    }

    // 100원 단위 가격
    private int getPriceRangeHundreds(int min, int max) {
        int raw = getRandomRange(min, max);
        return (raw / 100) * 100; // 100원 단위로 절삭
    }

    // 10 단위 재고
    private int getRandomRangeTens(int min, int max) {
        int raw = getRandomRange(min, max);
        return (raw / 10) * 10; // 10단위로 절삭
    }

    private int getRandomRange(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    // 파일명 → 상품명
    private String formatNameFromImage(String fileName) {
        String name = fileName.substring(0, fileName.lastIndexOf("."));
        name = name.replaceAll("_", " ").replaceAll("\\d+", "").trim();

        Map<String, String> dictionary = Map.ofEntries(
                // ☕ 음료류
                Map.entry("americano", "아메리카노"),
                Map.entry("latte", "바닐라라떼"),
                Map.entry("milk", "우유"),
                Map.entry("coffee", "커피"),
                Map.entry("cappuccino", "카푸치노"),
                Map.entry("juice", "주스"),
                Map.entry("wine", "와인"),

                // 🍞 빵류
                Map.entry("croissant", "크로아상"),
                Map.entry("ciabatta", "치아바타"),
                Map.entry("brioche", "브리오슈"),
                Map.entry("baguette", "바게트"),
                Map.entry("pretzel", "프레첼"),
                Map.entry("scone", "스콘"),
                Map.entry("focaccia", "포카치아"),
                Map.entry("donut", "도넛"),
                Map.entry("muffin", "머핀"),
                Map.entry("roll", "버터롤"),
                Map.entry("bread", "식빵"),
                Map.entry("bun", "모닝빵"),
                Map.entry("pie", "애플파이"),
                Map.entry("tart", "타르트"),

                // 🍰 디저트류
                Map.entry("cake", "케이크"),
                Map.entry("macaron", "마카롱")
        );

        for (String key : dictionary.keySet()) {
            if (name.toLowerCase().contains(key)) {
                return dictionary.get(key);
            }
        }

        return name;
    }

    // 전체 이미지로 상품 생성
    public List<Product> createAllProducts() {
        return getImageFileNames().stream()
                .map(this::createProduct)
                .collect(Collectors.toList());
    }
}
