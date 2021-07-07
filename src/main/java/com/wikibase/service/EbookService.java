package com.wikibase.service;

import com.wikibase.domain.Ebook;
import com.wikibase.domain.EbookExample;
import com.wikibase.mapper.EbookMapper;
import com.wikibase.req.EbookReq;
import com.wikibase.resp.EbookResp;
import com.wikibase.util.CopyUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class EbookService {

    @Resource
    private EbookMapper ebookMapper;

    public List<EbookResp> list(EbookReq req){
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        criteria.andNameLike("%"+req.getName()+"%");
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);

        // List Copy
        List<EbookResp> ebookRespList = CopyUtil.copyList(ebookList,EbookResp.class);

        // Individual Copy
        //EbookResp ebookResp = CopyUtil.copy(ebook,EbookResp.class);

        return ebookRespList;
    }

    public List<EbookResp> listAll(){
        List<Ebook> ebooks = ebookMapper.selectByExample(null);
        List<EbookResp> ebookRespList = CopyUtil.copyList(ebooks,EbookResp.class);
        return ebookRespList;
    }
}
