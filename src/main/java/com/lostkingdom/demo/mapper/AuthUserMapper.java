package com.lostkingdom.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lostkingdom.demo.entity.AuthUser;
import com.lostkingdom.demo.vo.reqVo.UserAdminReqVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author kingj
 * @date 2019/8/13 15:09
 */
@Mapper
public interface AuthUserMapper extends BaseMapper<AuthUser> {

    List<AuthUser> getAllAuthUser();

    AuthUser getAuthUserById(Long id);

    AuthUser getAuthUserByUsername(String username);

    AuthUser selectUser(String username);

    List<AuthUser> getPagedAuthUser(UserAdminReqVo vo);

    int insert(AuthUser authUser);

    int updateById(AuthUser authUser);

    AuthUser selectById(Long id);

}
