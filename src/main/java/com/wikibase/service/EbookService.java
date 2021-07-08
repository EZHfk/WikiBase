package com.wikibase.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wikibase.domain.Ebook;
import com.wikibase.domain.EbookExample;
import com.wikibase.mapper.EbookMapper;
import com.wikibase.req.EbookQueryReq;
import com.wikibase.req.EbookSaveReq;
import com.wikibase.resp.EbookResp;
import com.wikibase.resp.PageResp;
import com.wikibase.util.CopyUtil;
import com.wikibase.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class EbookService {

    private static final Logger LOG = LoggerFactory.getLogger(EbookService.class);
    @Resource
    private EbookMapper ebookMapper;

    @Resource
    private SnowFlake snowFlake;

    public PageResp<EbookResp> list(EbookQueryReq req){
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        if(!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");
        }

        PageHelper.startPage(req.getPage(),req.getSize());
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);

        PageInfo<Ebook> pageInfo = new PageInfo<>(ebookList);
        LOG.info("Total Result Num: {}",pageInfo.getTotal());
        LOG.info("Total Page Num: {}",pageInfo.getPages());

        // List Copy
        List<EbookResp> ebookRespList = CopyUtil.copyList(ebookList,EbookResp.class);

        // Individual Copy
        //EbookResp ebookResp = CopyUtil.copy(ebook,EbookResp.class);
        PageResp<EbookResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(ebookRespList);
        return pageResp;
    }

    /**
     * Save Post Edit
     */
    public void save(EbookSaveReq req){
        Ebook ebook = CopyUtil.copy(req,Ebook.class);

        if(ObjectUtils.isEmpty(req.getId())){
            //New Post
            ebook.setId(snowFlake.nextId());
            ebookMapper.insert(ebook);
        }
        else {
            //Update
            ebookMapper.updateByPrimaryKey(ebook);
        }
    }
}
