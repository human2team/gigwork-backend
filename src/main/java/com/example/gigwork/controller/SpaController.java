package com.example.gigwork.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class SpaController {
    
    /**
     * SPA(Single Page Application) 라우팅 지원
     * 프론트엔드 경로는 모두 index.html로 forward하여 React Router가 처리하도록 함
     */
    @GetMapping(value = {
        "/jobseeker",
        "/jobseeker/**",
        "/employer",
        "/employer/**",
        "/login/**",
        "/signup/**",
        "/terms",
        "/privacy"
    })
    public String forward() {
        return "forward:/index.html";
    }
}
