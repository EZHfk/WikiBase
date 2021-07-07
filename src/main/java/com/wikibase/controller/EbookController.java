package com.wikibase.controller;

import com.wikibase.domain.Demo;
import com.wikibase.domain.Ebook;
import com.wikibase.req.EbookReq;
import com.wikibase.resp.CommonResp;
import com.wikibase.resp.EbookResp;
import com.wikibase.service.DemoService;
import com.wikibase.service.EbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController //用来返回字符串；@Controller用来返回页面（前后端分离基本用不到）
@RequestMapping("/ebook")
public class EbookController {

    @Autowired
    private EbookService ebookService;

    @GetMapping("/list")
    public CommonResp list(EbookReq req){
        CommonResp<List<EbookResp>> resp = new CommonResp<>();
        List<EbookResp> list = ebookService.list(req);
        resp.setContent(list);
        return resp;
    }

    @GetMapping("/listAll")
    public CommonResp listAll(){
        CommonResp<List<EbookResp>> resp = new CommonResp<>();
        List<EbookResp> list = ebookService.listAll();
        resp.setContent(list);
        return resp;
    }
}
