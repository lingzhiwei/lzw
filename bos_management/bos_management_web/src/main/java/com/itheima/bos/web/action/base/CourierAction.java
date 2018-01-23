package com.itheima.bos.web.action.base;

import java.io.IOException;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.base.Courier;
import com.itheima.bos.service.base.CourierService;
import com.itheima.utils.PropertyOperator;

import net.sf.json.JsonConfig;

/**  
 * 快递员     
 */
@Controller //action层 注解扫描
@Scope("prototype")  //多例
@Namespace("/") //名称空间 
@ParentPackage("struts-default")  //父包
public class CourierAction extends BaseAction<Courier> {

    @Autowired
    private CourierService courierService;

    @Action(value="courierAction_save",results={
            @Result(name="success",location="/pages/base/courier.html",
                    type="redirect")
    })
    public String save() {
        courierService.save(getModel());
        
        return SUCCESS;
    }
    
    
   /* @Action(value="courierAction_pageQuery")
    public String pageQuery() throws IOException {
        
        //构造查询条件
        Specification specification = new Specification<Courier>() {

            @Override
            public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query,
                    CriteriaBuilder cb) {
                
                String courierNum = model.getCourierNum();
                Standard standard = model.getStandard();
                String company = model.getCompany();
                String type = model.getType();
                
                //声明一个集合，封装多个条件
                ArrayList<Predicate> list = new ArrayList<>(); 
                
                if (StringUtils.isNotEmpty(courierNum)) {
                    Predicate p1 = cb.equal(root.get("courierNum").as(String.class), courierNum);
                    list.add(p1);
                }
                if (standard != null) {
                    String name = standard.getName();
                    if (StringUtils.isNotEmpty(name)) {
                        //连接查询，指定连接对象
                        Join<Object, Object> join = root.join("standard");
                        Predicate p2 = cb.equal(join.get("name").as(String.class), name);
                        list.add(p2);
                    }
                }
                if (StringUtils.isNotEmpty(company)) {
                    Predicate p3 = cb.equal(root.get("company").as(String.class), company);
                    list.add(p3);
                }
                if (StringUtils.isNotEmpty(type)) {
                    Predicate p4 = cb.equal(root.get("type").as(String.class), type);
                    list.add(p4);
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
        Page<Courier> page = courierService.findAll(specification,pageable);
        
        
        //解决懒加载
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[]{"fixedAreas","takeTime"});
        
        page2json(page, jsonConfig);
        
        return NONE;
    }*/
    
    
    /**  
     * 分页查询（多条件）的第二种方法
     */
    @Action(value="courierAction_pageQuery")
    public String pageQuery() throws IOException{
        //使用工具类，判断model是否是空串，是就赋值为null
        PropertyOperator propertyOperator = new PropertyOperator();
        Courier courier = propertyOperator.replacePropertyFromEmptyToNull(getModel());
        
        Page<Courier> couriers = courierService.queryPage(courier,new PageRequest(page - 1, rows));
        
        //解决懒加载
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[]{"fixedAreas","takeTime"});
        
        page2json(couriers, jsonConfig);
        return null;
    }
    
    
    private String ids;
    public void setIds(String ids) {
        this.ids = ids;
    }
    
    @Action(value="courierAction_del",results={
            @Result(name="success",location="/pages/base/courier.html",type="redirect")
    })
    public String del() throws IOException {
        courierService.batchDel(ids);
        return SUCCESS;
    }
    
    @Action(value="courierAction_restore",results={
            @Result(name="success",location="/pages/base/courier.html",type="redirect")
    })
    public String restore() throws IOException {
        courierService.restore(ids);
        return SUCCESS;
    }

    /**  
     * 查询所有未作废的快递员
     */
    @Action(value="courierAction_findAllActive")
    public String findAllActive() throws IOException{
      //构造查询条件
        Specification specification = new Specification<Courier>() {

            @Override
            public Predicate toPredicate(Root<Courier> root,
                    CriteriaQuery<?> query, CriteriaBuilder cb) {
                
                //添加条件，deltag不等于‘1’，即没被作废
                return cb.notEqual(root.get("deltag").as(Character.class), '1');
            }};
        
        Page<Courier> page = courierService.findAll(specification, null);    
        List<Courier> list = page.getContent();
        
        //解决懒加载
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[]{"fixedAreas","takeTime"});
        
        list2json(list, jsonConfig);
        return null;
    }
   
}
  
