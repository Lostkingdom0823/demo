package com.lostkingdom.demo.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.lostkingdom.demo.entity.AuthUser;
import com.lostkingdom.demo.entity.RestBody;
import com.lostkingdom.demo.mapper.AuthUserMapper;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * @author yin.jiang
 * @date 2019/10/18 14:35
 */
@RestController
public class LoginController extends BaseController {

    @Autowired
    AuthUserMapper authUserMapper;

//    @GetMapping("login")
//    public ModelAndView loginView(){
//        return new ModelAndView("login");
//    }

    @RequestMapping("/")
    public String loginSuccess() {
        return JSON.toJSONString(getUser());
    }

    @RequestMapping("loginFailure")
    public String loginFailure(HttpServletRequest request, HttpServletResponse response){
        return JSON.toJSONString(RestBody.failure(HttpStatus.UNAUTHORIZED.value(), "登录失败了，老哥"));
    }

    @RequestMapping(value = "test",method = RequestMethod.POST)
    public void excelExportTest(HttpServletResponse response){
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(null,"sheet1", ExcelType.XSSF),
                AuthUser.class, authUserMapper.getAllAuthUser());
        try {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + URLEncoder.encode("中文测试.xlsx", "UTF-8"));
            workbook.write(response.getOutputStream());
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (IOException e) {
        }

    }

    @RequestMapping(value = "getToken",method = RequestMethod.POST)
    public String getToken(AuthUser authUser){
        return JSON.toJSONString(SecurityContextHolder.getContext().getAuthentication().getDetails());
    }

}
