package com.waiyannaung.sku.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.waiyannaung.sku.model.domain.TestDB;
import com.waiyannaung.sku.model.service.TestService;

@Controller
public class SkuController {
    @Autowired
    TestService testService;

    @GetMapping("/testdb")
    public String getAllTestDBs(Model model) {
        TestDB test = testService.findByName("WaiYan");
        model.addAttribute("data4", test);
        System.out.println("Data 4 : " + test);
        return "testdb";
    }

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
