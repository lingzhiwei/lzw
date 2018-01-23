package com.itheima.bos.web.action.base;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.base.Area;
import com.itheima.bos.domain.base.FixedArea;
import com.itheima.bos.domain.base.SubArea;
import com.itheima.bos.service.base.SubareaService;
import com.itheima.utils.PropertyOperator;

import net.sf.json.JsonConfig;

/**
 * 管理分区
 */
@Controller // action层 注解扫描
@Scope("prototype") // 多例
@Namespace("/") // 名称空间
@ParentPackage("struts-default") // 父包
public class SubareaAction extends BaseAction<SubArea> {

    @Autowired
    private SubareaService subareaService;

    @Action(value="subareaAction_save",results={
            @Result(name="success",location="/pages/base/sub_area.html",
                    type="redirect")
    })
    public String save() {
        subareaService.save(getModel());
        
        return SUCCESS;
    }
    
   
    @Action("subareaAction_pageQuery")
    public String pageQuery() throws IOException {
        
        final SubArea model = getModel();
        //构造查询条件
        Specification specification = new Specification<SubArea>() {

            @Override
            public javax.persistence.criteria.Predicate toPredicate(
                    Root<SubArea> root, CriteriaQuery<?> query,
                    CriteriaBuilder cb) {
                
                String keyWords = model.getKeyWords();
                Area area = model.getArea();
                FixedArea fixedArea = model.getFixedArea();
                
                //声明一个集合，封装多个条件
                ArrayList<Predicate> list = new ArrayList<>(); 
                
                if (StringUtils.isNotEmpty(keyWords)) {
                    Predicate p1 = cb.like(root.get("keyWords").as(String.class), "%"+keyWords+"%");
                    list.add(p1);
                }
                if (area != null) {
                    String province = area.getProvince();
                    String city = area.getCity();
                    String district = area.getDistrict();
                    
                    if (StringUtils.isNotEmpty(province)) {
                        //连接查询，指定连接对象
                        Join<Object, Object> join = root.join("area");
                        Predicate p2 = cb.equal(join.get("province").as(String.class), province);
                        list.add(p2);
                    }
                    if (StringUtils.isNotEmpty(city)) {
                        //连接查询，指定连接对象
                        Join<Object, Object> join = root.join("area");
                        Predicate p3 = cb.equal(join.get("city").as(String.class), city);
                        list.add(p3);
                    }
                    if (StringUtils.isNotEmpty(district)) {
                        //连接查询，指定连接对象
                        Join<Object, Object> join = root.join("area");
                        Predicate p4 = cb.equal(join.get("district").as(String.class), district);
                        list.add(p4);
                    }
                }
                if (fixedArea != null) {
                    String id = fixedArea.getId().toString();
                    if (StringUtils.isNotEmpty(id)) {
                        //连接查询，指定连接对象
                        Join<Object, Object> join = root.join("fixedArea");
                        Predicate p5 = cb.equal(join.get("id").as(Long.class), id);
                        list.add(p5);
                    }
                }
               
                // 如果没有输入查询条件
                if (list.size() == 0) {
                    return null;
                }
                
                //将集合转换成数组
                Predicate[] arr = new Predicate[list.size()];
                list.toArray(arr);
                
                //多条件查询
                Predicate ps = cb.and(arr);
                return ps;
            }};
        
        Pageable pageable = new PageRequest(page - 1, rows);
        Page<SubArea> page = subareaService.findAll(specification,pageable);
        
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[]{"subareas","couriers"});
        page2json(page, jsonConfig);
        
        return NONE;
    }
    
    /**  
     * 查询所有未关联定区的分区 
     */
    @Action("subAreaAction_findSubAreaUnAssociated")
    public String findSubAreaUnAssociated() throws IOException{
        
        List<SubArea> list = subareaService.findSubAreaUnAssociated();
        
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[]{"subareas","couriers"});
        
        list2json(list, jsonConfig);
        
        return null;
    }
    
    
    /**  
     * 查询所有指定定区的分区
     */
    @Action("subAreaAction_findSubAreaAssociated2FixedArea")
    public String findSubAreaAssociated2FixedArea() throws IOException{
        
        List<SubArea> list = subareaService.findSubAreaAssociated2FixedArea(getModel().getFixedArea().getId());
        
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[]{"subareas","couriers"});
        
        list2json(list, jsonConfig);
        
        return null;
    }
    
    
    private Long[] subareaIDS;
    public void setSubareaIDS(Long[] subareaIDS) {
        System.out.println("------------------");
        this.subareaIDS = subareaIDS;
    }
    
   
    
    /**  
     *更新指定定区的分区
     */
    @Action(value="subAreaAction_assignSubArea2FixedArea",results={
            @Result(name="success",location="/pages/base/fixed_area.html",
                    type="redirect")
    })
    public String assignSubArea2FixedArea() throws IOException{
        System.out.println(subareaIDS.length+"=====================");
        
        System.out.println(getModel().getFixedArea().getId()+"+++++++++++++++++");
        
        subareaService.assignSubArea2FixedArea(subareaIDS,getModel().getFixedArea().getId());
        
        return SUCCESS;
    }
    
    
    
    
    
}
