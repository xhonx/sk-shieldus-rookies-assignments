package mylab.book.control;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import mylab.book.entity.Magazine;
import mylab.book.entity.Novel;
import mylab.book.entity.Publication;
import mylab.book.entity.ReferenceBook;

public class StatisticsAnalyzer {

    public Map<String, Double> calculateAveragePriceByType(Publication[] publications) {
        Map<String, Double> totalPriceMap = new HashMap<>();
        Map<String, Integer> countMap = new HashMap<>();

        for (Publication pub : publications) {
            String type = getPublicationType(pub);
            totalPriceMap.put(type, totalPriceMap.getOrDefault(type, 0.0) + pub.getPrice());
            countMap.put(type, countMap.getOrDefault(type, 0) + 1);
        }

        Map<String, Double> averageMap = new HashMap<>();
        for (String type : totalPriceMap.keySet()) {
            averageMap.put(type, totalPriceMap.get(type) / countMap.get(type));
        }
        return averageMap;
    }

    public Map<String, Double> calculatePublicationDistribution(Publication[] publications) {
        Map<String, Integer> countMap = new HashMap<>();
        for (Publication pub : publications) {
            String type = getPublicationType(pub);
            countMap.put(type, countMap.getOrDefault(type, 0) + 1);
        }

        Map<String, Double> distributionMap = new HashMap<>();
        int total = publications.length;
        for (String type : countMap.keySet()) {
            distributionMap.put(type, (countMap.get(type) * 100.0) / total);
        }
        return distributionMap;
    }

    public double calculatePublicationRatioByYear(Publication[] publications, String year) {
        int count = 0;
        for (Publication pub : publications) {
            String pubYear = pub.getPublishDate().substring(0, 4);
            if (pubYear.equals(year)) {
                count++;
            }
        }
        return (count * 100.0) / publications.length;
    }

    private String getPublicationType(Publication pub) {
        if (pub instanceof Novel) {
            return "소설";
        } else if (pub instanceof Magazine) {
            return "잡지";
        } else if (pub instanceof ReferenceBook) {
            return "참고서";
        }
        return "기타";
    }

    public void printStatistics(Publication[] publications) {
        DecimalFormat df = new DecimalFormat("#,###.##");

        System.out.println("===== 출판물 통계 분석 =====");
        System.out.println("1. 타입별 평균 가격:");
        Map<String, Double> averageMap = calculateAveragePriceByType(publications);
        for (Map.Entry<String, Double> entry : averageMap.entrySet()) {
            System.out.println("   - " + entry.getKey() + ": " + df.format(entry.getValue()) + "원");
        }

        System.out.println();
        System.out.println("2. 출판물 유형 분포:");
        Map<String, Double> distributionMap = calculatePublicationDistribution(publications);
        for (Map.Entry<String, Double> entry : distributionMap.entrySet()) {
            System.out.println("   - " + entry.getKey() + ": " + df.format(entry.getValue()) + "%");
        }

        System.out.println();
        double ratio2007 = calculatePublicationRatioByYear(publications, "2007");
        System.out.println("3. 2007년에 출판된 출판물 비율: " + df.format(ratio2007) + "%");
    }
}
