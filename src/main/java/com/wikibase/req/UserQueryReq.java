package com.wikibase.req;

import com.wikibase.resp.PageResp;
import sun.jvm.hotspot.debugger.Page;

public class UserQueryReq extends PageReq {

    private String loginName;


    public String getLoginName() {
        return loginName;
    }


    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    @Override
    public String toString() {
        return "UserQueryReq{" +
                "loginName='" + loginName + '\'' +
                '}';
    }
}