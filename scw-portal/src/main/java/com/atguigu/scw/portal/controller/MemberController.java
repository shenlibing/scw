package com.atguigu.scw.portal.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.atguigu.project.HttpClientUtil;
import com.atguigu.scw.manager.bean.TMemeber;
import com.atguigu.scw.manager.constant.Constants;
import com.atguigu.scw.portal.bean.ScwReturn;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 帮我们去调用restapi层的接口；
 * 
 * @ClassName MemberController
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author lfy
 * @Date 2017年7月15日 下午5:08:12
 * @version 1.0.0
 */
@RequestMapping("/member")
@Controller
public class MemberController {
    
    @Autowired
    RestApiServerInfo serverInfo;
    
    //来到实名认证页面
    @RequestMapping("/auth.html")
    public String toAuthPage(){
        
        return "member/accttype";
    }
    // /member/main.html
    @RequestMapping("/main.html")
    public String toMemberCenter(){
        return "member/member";
    }
    

    @RequestMapping("/login")
    public String login(TMemeber memeber,Model model,HttpSession session) throws Exception {

        // 构建参数
        Map<String, Object> params = new HashMap<>();
        params.put("loginacct", memeber.getLoginacct());
        params.put("userpswd", memeber.getUserpswd());
        // 1、登陆调用restapi登陆接口
        String string = HttpClientUtil.httpPostRequest(serverInfo.getRestApiURL() + "/member/login", params);
        System.out.println(string);
        // 逆向响应内容
        ScwReturn<TMemeber> result = new ObjectMapper().readValue(string.getBytes(),
                new TypeReference<ScwReturn<TMemeber>>() {
                });
        
        // 2、登陆成功来到首页，导航条变化
        if(result.getCode() == 1){
            //3、登陆成功！
            //3.1)、将用户放在session中
            session.setAttribute(Constants.LOGIN_USER, result.getContent());
            //只要有表单提交的时候，为了防止重复提交，我们都直接重定向到下一个地址
            return "redirect:/index.jsp";
        }else{
            // 3、登陆失败来到登陆页面，提示用户名密码错误
            model.addAttribute("msg", "登陆失败，用户名密码错误");
            return "forward:/login.jsp";
        }
            
        
    }

    @RequestMapping("/regist")
    public String regist(TMemeber memeber, Model model, RedirectAttributes attributes) {
        // 需要利用http工具去模拟发调用接口的请求
        // http://localhost:8082/scw-restapi/member/regist
        // <httpclient.version>4.5.3</httpclient.version>
        // httpclient使用java代码来模拟发送请求
        // 能收到api调用后产生的json数据；
        // 1、可以将产生的json逆向成对象ScwReturn<TMemeber>
        // 2、可以直接将json写给页面

        // 1、应该去发送请求来进行注册；java代码发请求
        String url = serverInfo.getRestApiURL() + "/member/regist";
        // 2、构建请求参数
        Map<String, Object> params = new HashMap<String, Object>();
        try {
            params.put("loginacct", memeber.getLoginacct());
            // 密码是业务逻辑加密的，我们不用管
            params.put("userpswd", memeber.getUserpswd());
            params.put("email", memeber.getEmail());
            // 响应内容；
            String response = HttpClientUtil.httpPostRequest(url, params);

            // 封装响应为对象
            ScwReturn<TMemeber> readValue = null;
            try {
                readValue = new ObjectMapper().readValue(response.getBytes(), new TypeReference<ScwReturn<TMemeber>>() {
                });
            } catch (Exception e) {
                System.out.println(e);
            }

            // 判断，注册成功！来到登陆页面
            if (readValue.getCode() == 1) {
                // 注册成功！来到登陆页面
                // model.addAttribute("msg", "注册成功！可以登陆了");
                // 重定向带数据，放在session中
                // 重定向带数据；
                // attributes.addFlashAttribute("msg", "注册成功！可以登陆了");
                // attributes.addAttribute("msg", "注册成功！可以登陆了");
                return "redirect:/login.jsp";
            } else {
                // 注册失败！来到注册页面进行回显
                model.addAttribute("msg", "用户名和邮箱已经被注册了！");
                return "forward:/reg.jsp";
            }
        } catch (UnsupportedEncodingException e) {
        }

        System.out.println("用户注册：" + memeber);
        // 注册成功以后可以来到一个页面；
        return "forward:/index.jsp";
    }
}
