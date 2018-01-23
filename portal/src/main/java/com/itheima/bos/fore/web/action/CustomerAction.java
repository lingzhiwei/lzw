package com.itheima.bos.fore.web.action;

import java.util.concurrent.TimeUnit;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.domain.Customer;
import com.itheima.utils.MailUtils;
import com.itheima.utils.SmsUtils3;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**  
 *        
 */

@Controller
@Scope("prototype") // 等价于applicationContext.xml中scope属性
@Namespace("/") // 等价于struts.xml中package节点中namespace属性
@ParentPackage("struts-default") // 等价于struts.xml中package节点中extends属性
public class CustomerAction extends ActionSupport
        implements ModelDriven<Customer> {

    private Customer model = new Customer();

    @Override
    public Customer getModel() {
        return model;
    }

    @Action("customerAction_sendSMS")
    public String sendSMS() {

        try {
            // 获取随机验证码
            String code = RandomStringUtils.randomNumeric(6);
            System.out.println("code : " + code + model.getTelephone());
            // 存入session中
            ServletActionContext.getRequest().getSession().setAttribute("code",
                    code);

            // 发送短信
            SmsUtils3.sendSms(model.getTelephone(), code);
        } catch (ClientException e) {

            e.printStackTrace();
        }
        return NONE;
    }

    private String checkcode;

    public void setCheckcode(String checkcode) {
        this.checkcode = checkcode;
    }

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Action(value = "customerAction_regist",
            results = {
                    @Result(name = "success", location = "/signup-success.html",
                            type = "redirect"),
                    @Result(name = "error", location = "/signup-fail.html",
                            type = "redirect")})
    public String regist() {

        // 获取生成的验证码
        String code = (String) ServletActionContext.getRequest().getSession()
                .getAttribute("code");

        if (StringUtils.isNotEmpty(checkcode) && StringUtils.isNotEmpty(code)
                && checkcode.equals(code)) {

            System.out.println(model.getEmail());
            // 向crm发送请求，注册用户
            WebClient
                    .create("http://localhost:8180/crm/webService/cs/saveCustomer")
                    .type(MediaType.APPLICATION_JSON) // 指定客户端传个服务端的数据格式
                    .accept(MediaType.APPLICATION_JSON) // 指定客户端能接收的数据格式
                    .post(model);

            // 发送邮件
            //// 生成验证码
            String activeCode = RandomStringUtils.randomNumeric(36);
            //// 将验证码存储到redis中，以用户输入的手机号为key，并设置有效期
            redisTemplate.opsForValue().set(model.getTelephone(), activeCode, 1,
                    TimeUnit.DAYS);
            //// 设置邮件内容
            String emailBody =
                    "感谢您注册速运快递 ,请您在24小时内点击下面的链接激活您的帐号<br><a href='http://localhost:8280/portal/customerAction_active.action?telephone="
                            + model.getTelephone() + "&activeCode=" + activeCode
                            + "'>激活帐号</a>";
            //// 发送邮件
            MailUtils.sendMail("zs@store.com", "激活邮件", emailBody);

            return SUCCESS;
        }

        return ERROR;
    }

    // 获取激活码
    private String activeCode;

    public void setActiveCode(String activeCode) {
        this.activeCode = activeCode;
    }

    @Action(value = "customerAction_active",
            results = {
                    @Result(name = "success", location = "/login.html",
                            type = "redirect"),
                    @Result(name = "actived", location = "/login.html",
                            type = "redirect"),
                    @Result(name = "error", location = "/signup-fail.html",
                            type = "redirect")})
    public String active() {
        System.out.println("点击激活！！！");

        // 获取存储在redis中的激活码
        String serverCode =
                redisTemplate.opsForValue().get(model.getTelephone());

        System.out.println(serverCode + ":::::" + activeCode);

        if (StringUtils.isNotEmpty(serverCode)
                && StringUtils.isNotEmpty(activeCode)
                && serverCode.equals(activeCode)) {

            System.out.println("========================");
            // 判断用户是否已经激活
            Customer customer = WebClient
                    .create("http://localhost:8180/crm/webService/cs/findByTelephone")
                    .type(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .query("telephone", model.getTelephone())
                    .get(Customer.class);

            System.out.println("++++++++++++++++++++++++++++++");

            // 用户已激活
            if (customer != null && customer.getType() != null
                    && customer.getType() == 1) {
                return "actived";
            }

            // 激活用户
            WebClient
                    .create("http://localhost:8180/crm/webService/cs/active")
                    .type(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .query("telephone", model.getTelephone()).put(null);

            return SUCCESS;

        }

        return ERROR;

    }

    
    
    @Action(value = "customerAction_login",
            results = {
                    @Result(name = "success", location = "/index.html",
                            type = "redirect"),
                    @Result(name = "unactived", location = "/login.html",
                            type = "redirect"),
                    @Result(name = "error", location = "/signup-fail.html",
                            type = "redirect")})
    public String login() {
        System.out.println("登录！！！");

        // 获取生成的验证码
        String validateCode = (String) ServletActionContext.getRequest()
                .getSession().getAttribute("validateCode");

        System.out.println(validateCode + ":::::" + checkcode);

        // 如果输入的验证码等于生成的验证码
        if (StringUtils.isNotEmpty(checkcode)
                && StringUtils.isNotEmpty(validateCode)
                && validateCode.equals(checkcode)) {

            System.out.println("========================");
            // 判断用户是否存在
            Customer customer = WebClient
                    .create("http://localhost:8180/crm/webService/cs/login")
                    .type(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .query("telephone", model.getTelephone())
                    .query("password", model.getPassword()).get(Customer.class);

            System.out.println("++++++++++++++++++++++++++++++");

            if (customer != null && customer.getType() != null) {
                if (customer.getType() == 1) {
                    // 用户存在，并且已激活,登录成功
                    return SUCCESS;
                } else {
                    // 用户存在，但是未激活
                    return "unactived";
                }
            }

        }

        return ERROR;
    }

}
