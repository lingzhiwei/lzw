package com.itheima.bos.web.action.base;

import java.io.IOException;
import java.util.List;

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

import com.itheima.bos.domain.base.Standard;
import com.itheima.bos.service.base.StandardService;

/**  
 * 收派标准     
 */
@Controller //action层 注解扫描
@Scope("prototype")  //多例
@Namespace("/") //名称空间 
@ParentPackage("struts-default")  //父包
public class StandardAction extends BaseAction<Standard> {

    @Autowired
    private StandardService standardService;

    @Action(value="standardAction_save",results={
            @Result(name="success",location="/pages/base/standard.html",
                    type="redirect")
    })
    public String save() {
        standardService.save(getModel());
        
        return SUCCESS;
    }
    
    
    @Action("standardAction_pageQuery")
    public String pageQuery() throws IOException {
        
        Pageable pageable = new PageRequest(page - 1, rows);
        
        Page<Standard> page = standardService.findAll(pageable);
        
        page2json(page, null);
        
        return NONE;
    }
    
    
    /**  
     * 获取所有分派标准，显示在增加快递员窗口
     */
    @Action("standardAction_findAll")
    public String findAll() throws IOException {
        
        Page<Standard> page = standardService.findAll(null);
        
        // 得到所有的收派标准
        List<Standard> list = page.getContent();
        list2json(list, null);
        
        return NONE;
    }
}
  
