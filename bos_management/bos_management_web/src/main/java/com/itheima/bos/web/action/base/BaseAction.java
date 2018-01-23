package com.itheima.bos.web.action.base;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.formula.functions.T;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.hibernate.boot.registry.StandardServiceInitiator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.base.Area;
import com.itheima.bos.domain.base.Standard;
import com.itheima.bos.service.base.StandardService;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * 抽取共有代码，第一种方法
 */
public class BaseAction<T> extends ActionSupport implements ModelDriven<T> {

    private T model;

    // 通过构造方法，获取具体的子类的实例
    public BaseAction() {
        // 获取子类的字节码
        Class<? extends BaseAction> childClazz = this.getClass();
        // 得到带泛型的父类（即本类）的字节码
        Type genericSuperclass = childClazz.getGenericSuperclass();
        // 得到Type的实现类对象
        ParameterizedType parameterizedType =
                (ParameterizedType) genericSuperclass;
        // 调用实现类中的方法，得到所有泛型组成的数组
        Type[] types = parameterizedType.getActualTypeArguments();
        // 获取需要的泛型
        Class<T> clazz = (Class<T>) types[0];
        try {
            model = clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @Override
    public T getModel() {
        return model;
    }

    protected int page;
    protected int rows;

    public void setPage(int page) {
        this.page = page;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    // 将page转化为json
    public void page2json(Page<T> page, JsonConfig jsonConfig)
            throws IOException {

        // 得到总数据条数
        long total = page.getTotalElements();
        // 得到当前页数据
        List<T> content = page.getContent();

        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("rows", content);

        // 转化为json数据
        String json;
        if (jsonConfig == null) {
            json = JSONObject.fromObject(map).toString();
        } else {
            json = JSONObject.fromObject(map, jsonConfig).toString();
        }

        // 响应个客户端
        HttpServletResponse response = ServletActionContext.getResponse();

        // 解决json类型的中文乱码
        response.setContentType("application/json;charset=UTF-8");

        response.getWriter().println(json);

    }

    // 将list集合转化为json
    public void list2json(List list, JsonConfig jsonConfig)
            throws IOException {

        // 转化为json数据
        String json;
        if (jsonConfig == null) {
            json = JSONArray.fromObject(list).toString();
        } else {
            json = JSONArray.fromObject(list, jsonConfig).toString();
        }

        // 响应给客户端
        HttpServletResponse response = ServletActionContext.getResponse();

        // 解决json类型的中文乱码
        response.setContentType("application/json;charset=UTF-8");

        response.getWriter().println(json);
    }

}
