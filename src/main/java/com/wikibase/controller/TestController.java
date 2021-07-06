package com.wikibase.controller;

import com.wikibase.domain.Test;
import com.wikibase.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController //用来返回字符串；@Controller用来返回页面（前后端分离基本用不到）
public class TestController {

    @Value("${test.hello:TEST}") //优先读配置项，没有就读：后的默认值
    private String hello;

    @Autowired
    private TestService testService;


    @GetMapping("")
    public String welcome(){
        return "Welcome to Spring";
    }
    /**
     * GET,POST,PUT,DELETE:增删改查
     * @return String
     */
    //@RequestMapping(value = "/hello",method = RequestMethod.GET)
    @GetMapping("/hello")
    public String hello(){
        return hello;
    }

    @GetMapping("/test/list")
    public List<Test> list(){
        return testService.list();
    }
}
