package com.lostkingdom.demo.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lostkingdom.demo.dto.UserAdminDTO;
import com.lostkingdom.demo.entity.AuthUser;
import com.lostkingdom.demo.enums.CommonExceptionEnums;
import com.lostkingdom.demo.enums.UserStatusEnum;
import com.lostkingdom.demo.exception.DemoException;
import com.lostkingdom.demo.mapper.AuthUserMapper;
import com.lostkingdom.demo.service.IUserAdminService;
import com.lostkingdom.demo.util.BCryptPasswordUtil;
import com.lostkingdom.demo.util.CollectionUtil;
import com.lostkingdom.demo.vo.reqVo.UserAdminReqVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @program: demo
 * @description: 用户管理业务实现类
 * @author: jiang.yin
 * @create: 2020-03-17 10:27
 **/
@Service
public class IUserAdminServiceImpl implements IUserAdminService {

    @Autowired
    AuthUserMapper authUserMapper;

    @Override
    @Transactional
    public Integer addUser(UserAdminReqVo vo) {
        //
        if(StringUtils.isEmpty(vo.getUsername())){
            throw new DemoException(CommonExceptionEnums.NO_USERNAME);
        }
        if(StringUtils.isEmpty(vo.getPassword())){
            throw new DemoException(CommonExceptionEnums.NO_PASSWORD);
        }
        if(StringUtils.isEmpty(vo.getName())){
            throw new DemoException(CommonExceptionEnums.NO_TRUENAME);
        }
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AuthUser creator = authUserMapper.selectUser(user.getUsername());
        if(creator == null){
            throw new DemoException(CommonExceptionEnums.NO_CREATOR_INFO);
        }
        //
        AuthUser newUser = new AuthUser();
        BeanUtils.copyProperties(vo,newUser);
        newUser.setStatus(UserStatusEnum.ENABLE.getValue());
        newUser.setCreateBy(creator.getId());
        newUser.setCreateUsername(creator.getUsername());
        newUser.setCreateTimestamp(new Date());
        newUser.setPassword(BCryptPasswordUtil.passwordEncoder(newUser.getPassword()));

        return authUserMapper.insert(newUser);
    }

    @Override
    @Transactional
    public Integer deleteUser(UserAdminReqVo vo) {
        if(vo.getId() == null || vo.getId() == 0){
            throw new DemoException(CommonExceptionEnums.NO_USER_ID);
        }
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AuthUser updater = authUserMapper.selectUser(user.getUsername());
        if(updater == null){
            throw new DemoException(CommonExceptionEnums.NO_CREATOR_INFO);
        }
        AuthUser oldUser = authUserMapper.selectById(vo.getId());
        if(oldUser == null){
            throw new DemoException(CommonExceptionEnums.NO_USER_INFO);
        }
        oldUser.setStatus(UserStatusEnum.DISABLE.getValue());
        return authUserMapper.updateById(oldUser);
    }

    @Override
    @Transactional
    public Integer updateUser(UserAdminReqVo vo) {
        //
        if(vo.getId() == null || vo.getId() == 0){
            throw new DemoException(CommonExceptionEnums.NO_USER_ID);
        }
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AuthUser updater = authUserMapper.selectUser(user.getUsername());
        if(updater == null){
            throw new DemoException(CommonExceptionEnums.NO_CREATOR_INFO);
        }
        //
        AuthUser oldUser = authUserMapper.selectById(vo.getId());
        if(oldUser == null){
            throw new DemoException(CommonExceptionEnums.NO_USER_INFO);
        }
        //
        if(!(vo.getName() == null || vo.getName().equals(""))){
            oldUser.setName(vo.getName());
        }
        if(!(vo.getPhone() == null || vo.getPhone().equals(""))){
            oldUser.setPhone(vo.getPhone());
        }
        if(!(vo.getStatus() == null || vo.getStatus().equals(""))){
            oldUser.setStatus(UserStatusEnum.valueOf(vo.getStatus()).getValue());
        }
        oldUser.setUpdateTimestamp(new Date());
        oldUser.setUpdateBy(updater.getId());
        oldUser.setUpdateUsername(updater.getUsername());
        return authUserMapper.updateById(oldUser);
    }

    @Override
    public PageInfo queryUser(UserAdminReqVo vo) {
        if(vo.getPage() > 0 && vo.getPageSize() > 0){
            PageHelper.startPage(vo.getPage(),vo.getPageSize());
        }
        List<AuthUser> users = authUserMapper.getAllAuthUser();
        List<UserAdminDTO> result = CollectionUtil.listCopy(UserAdminDTO.class,users);
        return new PageInfo<>(result);
    }

    @Override
    public AuthUser queryUserDetail(UserAdminReqVo vo) {
        return null;
    }
}
