package com.itheima.bos.web.action.base;

import java.io.IOException;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.base.TakeTime;
import com.itheima.bos.service.base.TakeTimeService;

/**  
 * 收派时间    
 */
@Controller //action层 注解扫描
@Scope("prototype")  //多例
@Namespace("/") //名称空间 
@ParentPackage("struts-default")  //父包
public class TakeTimeAction extends BaseAction<TakeTime>{

    @Autowired
    private TakeTimeService takeTimeService;
    
    /**  
     * 查询所有的收派时间
     * @throws IOException 
     */
    @Action("takeTimeAction_findAll")
    public String findAll() throws IOException{
        List<TakeTime> list = takeTimeService.findAll();
        
        list2json(list, null);
        return null;
    }
    
}
  
