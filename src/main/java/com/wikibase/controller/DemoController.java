package com.wikibase.controller;

import com.wikibase.domain.Demo;
import com.wikibase.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController //用来返回字符串；@Controller用来返回页面（前后端分离基本用不到）
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    private DemoService demoService;

    @GetMapping("/list")
    public List<Demo> list(){
        return demoService.list();
    }
}
