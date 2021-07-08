package com.wikibase.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wikibase.domain.Category;
import com.wikibase.domain.CategoryExample;
import com.wikibase.mapper.CategoryMapper;
import com.wikibase.req.CategoryQueryReq;
import com.wikibase.req.CategorySaveReq;
import com.wikibase.resp.CategoryResp;
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
public class CategoryService {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryService.class);
    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private SnowFlake snowFlake;

    public PageResp<CategoryResp> list(CategoryQueryReq req){
        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria = categoryExample.createCriteria();
//        if(!ObjectUtils.isEmpty(req.getName())) {
//            criteria.andNameLike("%" + req.getName() + "%");
//        }

        PageHelper.startPage(req.getPage(),req.getSize());
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);

        PageInfo<Category> pageInfo = new PageInfo<>(categoryList);
        LOG.info("Total Result Num: {}",pageInfo.getTotal());
        LOG.info("Total Page Num: {}",pageInfo.getPages());

        // List Copy
        List<CategoryResp> categoryRespList = CopyUtil.copyList(categoryList,CategoryResp.class);

        // Individual Copy
        //CategoryResp categoryResp = CopyUtil.copy(category,CategoryResp.class);
        PageResp<CategoryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(categoryRespList);
        return pageResp;
    }

    /**
     * Save Post Edit
     */
    public void save(CategorySaveReq req){
        Category category = CopyUtil.copy(req,Category.class);

        if(ObjectUtils.isEmpty(req.getId())){
            //New Post
            category.setId(snowFlake.nextId());
            categoryMapper.insert(category);
        }
        else {
            //Update
            categoryMapper.updateByPrimaryKey(category);
        }
    }

    /**
     * Delete Post
     */
    public void delete(Long id){
        categoryMapper.deleteByPrimaryKey(id);
    }
}
