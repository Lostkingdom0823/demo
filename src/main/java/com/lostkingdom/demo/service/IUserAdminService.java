package com.lostkingdom.demo.service;

import com.github.pagehelper.PageInfo;
import com.lostkingdom.demo.entity.AuthUser;
import com.lostkingdom.demo.vo.reqVo.UserAdminReqVo;

public interface IUserAdminService {

    Integer addUser(UserAdminReqVo vo);

    Integer deleteUser(UserAdminReqVo vo);

    Integer updateUser(UserAdminReqVo vo);

    PageInfo queryUser(UserAdminReqVo vo);

    AuthUser queryUserDetail(UserAdminReqVo vo);

    Integer changePassword(UserAdminReqVo vo);

}
