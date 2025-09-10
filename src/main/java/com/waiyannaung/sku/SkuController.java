package com.waiyannaung.sku;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SkuController {
    @GetMapping("/w")
    public String hello(Model model) {
        model.addAttribute("data", "방갑습니다."); // model 설정
        return "w"; // hello.html 연결
    }

}
