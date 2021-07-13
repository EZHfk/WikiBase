package com.wikibase.controller;

import com.wikibase.req.EbookQueryReq;
import com.wikibase.req.EbookSaveReq;
import com.wikibase.resp.CommonResp;
import com.wikibase.resp.EbookResp;
import com.wikibase.resp.PageResp;
import com.wikibase.service.EbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;

@RestController //用来返回字符串；@Controller用来返回页面（前后端分离基本用不到）
@RequestMapping("/ebook")
public class EbookController {

    @Autowired
    private EbookService ebookService;

    @GetMapping("/list")
    public CommonResp list(@Valid EbookQueryReq req){
        CommonResp<PageResp<EbookResp>> resp = new CommonResp<>();
        PageResp<EbookResp> list = ebookService.list(req);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody EbookSaveReq req){
        CommonResp resp = new CommonResp<>();
        ebookService.save(req);
        return resp;
    }

    @PostMapping(value = "/saveImage")
    public CommonResp save(@RequestParam("file") MultipartFile file) throws IOException {
        CommonResp resp = new CommonResp<>();
        ebookService.saveImage(file);
        return resp;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long id){
        CommonResp resp = new CommonResp<>();
        ebookService.delete(id);
        return resp;
    }

}
