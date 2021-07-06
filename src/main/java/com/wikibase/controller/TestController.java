package com.wikibase.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController //用来返回字符串；@Controller用来返回页面（前后端分离基本用不到）
public class TestController {

    /**
     * GET,POST,PUT,DELETE:增删改查
     * @return
     */
    //@RequestMapping(value = "/hello",method = RequestMethod.GET)
    @GetMapping("/hello")
    public String hello(){
        return "Hello World";
    }
}
