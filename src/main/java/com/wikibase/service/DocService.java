package com.wikibase.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wikibase.domain.Doc;
import com.wikibase.domain.DocExample;
import com.wikibase.mapper.DocMapper;
import com.wikibase.req.DocQueryReq;
import com.wikibase.req.DocSaveReq;
import com.wikibase.resp.DocResp;
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
public class DocService {

    private static final Logger LOG = LoggerFactory.getLogger(DocService.class);
    @Resource
    private DocMapper docMapper;

    @Resource
    private SnowFlake snowFlake;

    public PageResp<DocResp> list(DocQueryReq req){
        DocExample docExample = new DocExample();
        docExample.setOrderByClause("sort asc");
        DocExample.Criteria criteria = docExample.createCriteria();

        PageHelper.startPage(req.getPage(),req.getSize());
        List<Doc> docList = docMapper.selectByExample(docExample);

        PageInfo<Doc> pageInfo = new PageInfo<>(docList);
        LOG.info("Total Result Num: {}",pageInfo.getTotal());
        LOG.info("Total Page Num: {}",pageInfo.getPages());

        // List Copy
        List<DocResp> docRespList = CopyUtil.copyList(docList,DocResp.class);

        // Individual Copy
        //DocResp docResp = CopyUtil.copy(doc,DocResp.class);
        PageResp<DocResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(docRespList);
        return pageResp;
    }

    public List<DocResp> all(){
        DocExample docExample = new DocExample();
        docExample.setOrderByClause("sort asc");
        List<Doc> docList = docMapper.selectByExample(docExample);

        PageInfo<Doc> pageInfo = new PageInfo<>(docList);
        LOG.info("Total Result Num: {}",pageInfo.getTotal());
        LOG.info("Total Page Num: {}",pageInfo.getPages());

        // List Copy
        List<DocResp> docRespList = CopyUtil.copyList(docList,DocResp.class);

        return docRespList;
    }

    /**
     * Save Post Edit
     */
    public void save(DocSaveReq req){
        Doc doc = CopyUtil.copy(req,Doc.class);

        if(ObjectUtils.isEmpty(req.getId())){
            //New Post
            doc.setId(snowFlake.nextId());
            docMapper.insert(doc);
        }
        else {
            //Update
            docMapper.updateByPrimaryKey(doc);
        }
    }

    /**
     * Delete Post
     */
    public void delete(Long id){
        docMapper.deleteByPrimaryKey(id);
    }

    /**
     * Delete List of Post
     */
    public void delete(List<String> idsStr){
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        criteria.andIdIn(idsStr);
        docMapper.deleteByExample(docExample);
    }
}
