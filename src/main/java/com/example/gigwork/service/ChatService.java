package com.example.gigwork.service;

import com.example.gigwork.dto.ChatRequest;
import com.example.gigwork.dto.ChatResponse;
import com.example.gigwork.dto.FastApiRequest;
import com.example.gigwork.dto.FastApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final WebClient webClient;

    public ChatResponse processChat(ChatRequest request) {
        try {
            // Spring DTO를 FastAPI 요청 형식으로 변환
            Map<String, Object> condition = convertToConditionMap(request.getCondition());
            
            FastApiRequest fastApiRequest = FastApiRequest.builder()
                    .userId(null) // 필요시 사용자 ID 설정
                    .text(request.getText())
                    .condition(condition)
                    .search(request.getSearch() != null ? request.getSearch() : false)
                    .build();

            log.info("Sending request to FastAPI: {}", fastApiRequest);

            // FastAPI로 POST 요청
            // chat이 아닌 /chat/임을 유의
            // fastapi에서 main.py에서 /chat으로 받고 다시 chat.py에서 /로 받고 있음 (향후 수정)
            FastApiResponse fastApiResponse = webClient.post()
                    .uri("/chat/")
                    .bodyValue(fastApiRequest)
                    .retrieve()
                    .bodyToMono(FastApiResponse.class)
                    .block();

            log.info("Received response from FastAPI: {}", fastApiResponse);

            // FastAPI 응답을 Spring 응답 형식으로 변환
            return convertToResponse(fastApiResponse);
            
        } catch (Exception e) {
            log.error("Error calling FastAPI: ", e);
            // 에러 발생 시 샘플 데이터 반환
            return createSampleResponse();
        }
    }

    private Map<String, Object> convertToConditionMap(com.example.gigwork.dto.UserJobPreferences preferences) {
        if (preferences == null) {
            return new HashMap<>();
        }
        
        Map<String, Object> condition = new HashMap<>();
        if (preferences.getGender() != null) condition.put("gender", preferences.getGender());
        if (preferences.getAge() != null) condition.put("age", preferences.getAge());
        if (preferences.getPlace() != null) condition.put("place", preferences.getPlace());
        if (preferences.getWorkDays() != null) condition.put("work_days", preferences.getWorkDays());
        if (preferences.getStartTime() != null) condition.put("start_time", preferences.getStartTime());
        if (preferences.getEndTime() != null) condition.put("end_time", preferences.getEndTime());
        if (preferences.getHourlyWage() != null) condition.put("hourly_wage", preferences.getHourlyWage());
        if (preferences.getRequirements() != null) condition.put("requirements", preferences.getRequirements());
        if (preferences.getCategory() != null) condition.put("category", preferences.getCategory());
        
        return condition;
    }

    private ChatResponse convertToResponse(FastApiResponse fastApiResponse) {
        if (fastApiResponse == null || !Boolean.TRUE.equals(fastApiResponse.getSuccess())) {
            return createSampleResponse();
        }

        // FastAPI의 state에서 데이터 추출
        Map<String, Object> state = fastApiResponse.getState();
        if (state == null || state.get("condition") == null) {
            return createSampleResponse();
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> condition = (Map<String, Object>) state.get("condition");
        
        ChatResponse response = new ChatResponse();
        response.setGender(getStringValue(condition, "gender"));
        response.setAge(getIntegerValue(condition, "age"));
        response.setPlace(getStringValue(condition, "place"));
        response.setWorkDays(getStringValue(condition, "work_days"));
        response.setStartTime(getStringValue(condition, "start_time"));
        response.setEndTime(getStringValue(condition, "end_time"));
        response.setHourlyWage(getIntegerValue(condition, "hourly_wage"));
        response.setRequirements(getStringValue(condition, "requirements"));
        response.setCategory(getStringValue(condition, "category"));
        
        return response;
    }

    private String getStringValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        return value != null ? value.toString() : null;
    }

    private Integer getIntegerValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) return null;
        if (value instanceof Integer) return (Integer) value;
        if (value instanceof Number) return ((Number) value).intValue();
        try {
            return Integer.parseInt(value.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private ChatResponse createSampleResponse() {
        ChatResponse response = new ChatResponse();
        response.setGender("남");
        response.setAge(25);
        response.setPlace("서울 강남구");
        response.setWorkDays("주 5일");
        response.setStartTime("09:00");
        response.setEndTime("18:00");
        response.setHourlyWage(15000);
        response.setRequirements("경력 무관");
        response.setCategory("서빙");
        return response;
    }
}
