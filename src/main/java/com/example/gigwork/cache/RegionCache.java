package com.example.gigwork.cache;

import com.example.gigwork.model.Region;
import com.example.gigwork.util.RegionParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import org.springframework.context.event.EventListener;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import java.util.ArrayList;
import java.util.List;

@Component
public class RegionCache {
    private List<Region> regions = new ArrayList<>();

    @Value("${location.api.base-url}")
    private String locationApiBaseUrl;
    @Value("${location.api.key}")
    private String apiKey;

    @EventListener(ApplicationReadyEvent.class)
    public void loadRegions() {
        int page = 1;
        int numOfRows = 1000;
        RestTemplate restTemplate = new RestTemplate();
        List<Region> allRegions = new ArrayList<>();
        while (true) {
            String url = locationApiBaseUrl + "?serviceKey=" + apiKey + "&type=1&pageNo=" + page + "&numOfRows=" + numOfRows;
            System.out.println("[RegionCache] 요청 URL: " + url);
            String xml = restTemplate.getForObject(url, String.class);
            List<Region> pageRegions = RegionParser.parse(xml);
            System.out.println("[RegionCache] page " + page + " 로드: " + pageRegions.size() + "건");
            if (pageRegions.isEmpty()) break;
            allRegions.addAll(pageRegions);
            System.out.println("[RegionCache] 누적: " + allRegions.size() + "건");
            page++;
        }
        this.regions = allRegions;
        System.out.println("[RegionCache] 행정구역 캐싱 완료, 총 개수: " + regions.size());
    }

    public List<Region> getRegions() {
        return regions;
    }
}
