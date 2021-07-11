package com.wikibase.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wikibase.domain.User;
import com.wikibase.domain.UserExample;
import com.wikibase.exception.BusinessException;
import com.wikibase.exception.BusinessExceptionCode;
import com.wikibase.mapper.UserMapper;
import com.wikibase.req.UserQueryReq;
import com.wikibase.req.UserSaveReq;
import com.wikibase.resp.UserResp;
import com.wikibase.resp.PageResp;
import com.wikibase.util.CopyUtil;
import com.wikibase.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
    @Resource
    private UserMapper userMapper;

    @Resource
    private SnowFlake snowFlake;

    public PageResp<UserResp> list(UserQueryReq req){
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        if(!ObjectUtils.isEmpty(req.getLoginName())) {
            criteria.andLoginNameEqualTo(req.getLoginName());
        }

        PageHelper.startPage(req.getPage(),req.getSize());
        List<User> userList = userMapper.selectByExample(userExample);

        PageInfo<User> pageInfo = new PageInfo<>(userList);
        LOG.info("Total Result Num: {}",pageInfo.getTotal());
        LOG.info("Total Page Num: {}",pageInfo.getPages());

        // List Copy
        List<UserResp> userRespList = CopyUtil.copyList(userList,UserResp.class);

        // Individual Copy
        //UserResp userResp = CopyUtil.copy(user,UserResp.class);
        PageResp<UserResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(userRespList);
        return pageResp;
    }

    /**
     * Save Post Edit
     */
    public void save(UserSaveReq req){
        User user = CopyUtil.copy(req,User.class);

        if(ObjectUtils.isEmpty(req.getId())){
            if(ObjectUtils.isEmpty(selectByLoginName(req.getLoginName()))){
                //New Post
                user.setId(snowFlake.nextId());
                userMapper.insert(user);
            }
            else{
                throw new BusinessException(BusinessExceptionCode.USER_LOGIN_NAME_EXIST);
            }
        }
        else {
            //Update
            user.setLoginName(null);
            userMapper.updateByPrimaryKeySelective(user);
        }
    }

    /**
     * Delete Post
     */
    public void delete(Long id){
        userMapper.deleteByPrimaryKey(id);
    }

    public User selectByLoginName(String loginName){
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andLoginNameEqualTo(loginName);
        List<User> userList = userMapper.selectByExample(userExample);
        if(CollectionUtils.isEmpty(userList)){
            return null;
        }
        return userList.get(0);
    }
}
