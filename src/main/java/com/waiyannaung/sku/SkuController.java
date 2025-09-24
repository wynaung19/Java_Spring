package com.waiyannaung.sku;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SkuController {
    @GetMapping("/w")
    public String hello(Model model) {
        model.addAttribute("data", "방갑습니다.");
        return "w";
    }

    @GetMapping("/l2")
    public String l2(Model model) {
        model.addAttribute("data", "Something.");
        return "link2";
    }

    @GetMapping("/test1")
    public String thymeleaf_test1(Model model) {
        model.addAttribute("data1", "<h2> 방갑습니다 </h2>");
        model.addAttribute("data2", "태그의 속성 값");
        model.addAttribute("link", 01);
        model.addAttribute("name", "홍길동");
        model.addAttribute("para1", "001");
        model.addAttribute("para2", 002);
        return "thymeleaf_test1";
    }

}
