package com.wikibase.controller;

import com.wikibase.req.DocQueryReq;
import com.wikibase.req.DocSaveReq;
import com.wikibase.resp.DocResp;
import com.wikibase.resp.CommonResp;
import com.wikibase.resp.PageResp;
import com.wikibase.service.DocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@RestController //用来返回字符串；@Controller用来返回页面（前后端分离基本用不到）
@RequestMapping("/doc")
public class DocController {

    @Autowired
    private DocService docService;

    @GetMapping("/all")
    public CommonResp all(){
        CommonResp<List<DocResp>> resp = new CommonResp<>();
        List<DocResp> list = docService.all();
        resp.setContent(list);
        return resp;
    }

    @GetMapping("/list")
    public CommonResp list(@Valid DocQueryReq req){
        CommonResp<PageResp<DocResp>> resp = new CommonResp<>();
        PageResp<DocResp> list = docService.list(req);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody DocSaveReq req){
        CommonResp resp = new CommonResp<>();
        docService.save(req);
        return resp;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable String id){
        CommonResp resp = new CommonResp<>();
        List<String> list = Arrays.asList(id.split(","));
        docService.delete(list);
        return resp;
    }

}
