package com.itheima.bos.web.action.base;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.base.FixedArea;
import com.itheima.bos.domain.base.SubArea;
import com.itheima.bos.service.base.FixedAreaService;
import com.itheima.domain.Customer;

import net.sf.json.JsonConfig;

/**  
 * 定区     
 */
@Controller //action层 注解扫描
@Scope("prototype")  //多例
@Namespace("/") //名称空间 
@ParentPackage("struts-default")  //父包
public class FixedAreaAction extends BaseAction<FixedArea> {

    @Autowired
    private FixedAreaService fixedAreaService;

    @Action(value="fixedAreaAction_save",results={
            @Result(name="success",location="/pages/base/fixed_area.html",
                    type="redirect")
    })
    public String save() {
        fixedAreaService.save(getModel());
        
        return SUCCESS;
    }
    
    @Action("fixedAreaAction_pageQuery")
    public String pageQuery() throws IOException {
        
        Pageable pageable = new PageRequest(page - 1, rows);
        
        Page<FixedArea> page = fixedAreaService.findAll(pageable);
        
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[]{"subareas","couriers"});
        
        page2json(page, jsonConfig);
        
        return NONE;
    }
   
    @Action("fixedAreaAction_findCustomersUnAssociated")
    public String findCustomersUnAssociated() throws IOException {
        
        //向crm发送请求
        List<Customer> list = (List<Customer>) WebClient
            .create("http://localhost:8180/crm/webService/cs/findCustomerUnAssociated")
            .type(MediaType.APPLICATION_JSON) //指定客户端传个服务端的数据格式
            .accept(MediaType.APPLICATION_JSON) //指定客户端能接收的数据格式
            .getCollection(Customer.class);
        
        list2json(list, null);
        
        return NONE;
    }
    
    // 向CRM发起请求,查询关联到指定定区的客户
    @Action("fixedAreaAction_findCustomerAssociated2FixedArea")
    public String findCustomerAssociated2FixedArea() throws IOException {
        
        //向crm发送请求
        List<Customer> list = (List<Customer>) WebClient
                .create("http://localhost:8180/crm/webService/cs/findCustomerAssociated2FixedArea")
                .type(MediaType.APPLICATION_JSON) //指定客户端传个服务端的数据格式
                .accept(MediaType.APPLICATION_JSON) //指定客户端能接收的数据格式
                .query("fixedAreaId", getModel().getId())
                .getCollection(Customer.class);
        
        list2json(list, null);
        
        return NONE;
    }
    
    private Long[] customerIds;
    public void setCustomerIds(Long[] customerIds) {
        this.customerIds = customerIds;
    }
    
    // 向CRM发起请求,更新指定定区客户
    @Action(value="fixedAreaAction_assignCustomers2FixedArea",results = {
            @Result(name="success",location="/pages/base/fixed_area.html",type="redirect")
    })
    public String assignCustomers2FixedArea() throws IOException {
        
        //向crm发送请求
       WebClient.create("http://localhost:8180/crm/webService/cs/assignCustomers2FixedArea")
                .type(MediaType.APPLICATION_JSON) //指定客户端传个服务端的数据格式
                .accept(MediaType.APPLICATION_JSON) //指定客户端能接收的数据格式
                .query("customerIds", customerIds)
                .query("fixedAreaId", getModel().getId())
                .put(null);//不需要返回值
        
        return SUCCESS;
    }
    
    private Long courierId;
    private Long takeTimeId;
    public void setCourierId(Long courierId) {
        this.courierId = courierId;
    }
    public void setTakeTimeId(Long takeTimeId) {
        this.takeTimeId = takeTimeId;
    }
    
    @Action("fixedAreaAction_associationCourierToFixedArea")
    public void associationCourierToFixedArea() throws IOException {
        System.out.println("关联请求到来。。。。。");
        
        //参数：定区id，时间id，快递员id
        fixedAreaService.associationCourierToFixedArea(getModel().getId(),takeTimeId,courierId);
      
    }
    
}
  
