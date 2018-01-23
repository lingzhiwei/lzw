package com.itheima.bos.web.action.base;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.springframework.data.domain.Page;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * 抽取共有代码，第二种方法
 */
public class CommonAction<T> extends ActionSupport implements ModelDriven<T> {

    private T model;
    private Class<T> clazz;
    
    //通过子类调用构造方法，来传入泛型，创建model实例
    public CommonAction(Class<T> clazz) {
        this.clazz = clazz;
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

        // 响应个客户端
        HttpServletResponse response = ServletActionContext.getResponse();

        // 解决json类型的中文乱码
        response.setContentType("application/json;charset=UTF-8");

        response.getWriter().println(json);
    }

}
