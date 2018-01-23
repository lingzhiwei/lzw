package com.itheima.bos.web.action.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.base.Area;
import com.itheima.bos.domain.base.Courier;
import com.itheima.bos.domain.base.Standard;
import com.itheima.bos.service.base.AreaService;
import com.itheima.bos.service.base.StandardService;
import com.itheima.utils.PinYin4jUtils;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * 派送区域
 */
@Controller // action层 注解扫描
@Scope("prototype") // 多例
@Namespace("/") // 名称空间
@ParentPackage("struts-default") // 父包
public class AreaAction extends BaseAction<Area> {

    @Autowired
    private AreaService areaService;

    private File file;
    private String fileFileName;

    public void setFile(File file) {
        this.file = file;
    }

    public void setFileFileName(String fileFileName) {
        this.fileFileName = fileFileName;
    }

    @Action(value = "areaAction_upload", results = {@Result(name = "success",
            location = "/pages/base/area.html", type = "redirect")})
    public String upload() {

        System.out.println(fileFileName);

        // 创建一个集合封装区域
        ArrayList<Area> list;
        try {
            HSSFWorkbook hWorkbook =
                    new HSSFWorkbook(new FileInputStream(file));
            // 选择工作簿
            HSSFSheet sheet = hWorkbook.getSheetAt(0);

            list = new ArrayList<>();
            // 遍历工作簿中的所有行
            for (Row row : sheet) {
                int rowNum = row.getRowNum();
                // 跳过第一行
                if (rowNum == 0) {
                    continue;
                }
                // 读取每一行的数据,跳过第一列
                String province = row.getCell(1).getStringCellValue();
                String city = row.getCell(2).getStringCellValue();
                String district = row.getCell(3).getStringCellValue();
                String postcode = row.getCell(4).getStringCellValue();

                // 获取城市编码 citycode 和简码 shortcode

                // 去掉末尾的省、市、区
                province = province.substring(0, province.length() - 1);
                city = city.substring(0, city.length() - 1);
                district = district.substring(0, district.length() - 1);

                // 将汉字转化为拼音,得到城市编码
                String citycode =
                        PinYin4jUtils.hanziToPinyin(city, "").toUpperCase();

                String[] headByString = PinYin4jUtils
                        .getHeadByString(province + city + district);
                String shortcode =
                        PinYin4jUtils.stringArrayToString(headByString);

                // System.out.println(shortcode);
                // System.out.println(province);

                // 封装一个area对象
                Area area = new Area();
                area.setProvince(province);
                area.setCity(city);
                area.setDistrict(district);
                area.setPostcode(postcode);
                area.setCitycode(citycode);
                area.setShortcode(shortcode);

                list.add(area);
            }
            // 保存一个集合数据
            areaService.save(list);

            // 释放资源
            hWorkbook.close();
        } catch (Exception e) {
            //
            e.printStackTrace();
            return ERROR;
        }

        return SUCCESS;
    }

    private int page;
    private int rows;

    public void setPage(int page) {
        this.page = page;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    @Action(value = "areaAction_pageQuery")
    public String pageQuery() throws IOException {

        Pageable pageable = new PageRequest(page - 1, rows);
        Page<Area> page = areaService.findAll(pageable);
        
        // 解决懒加载
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[] {"subareas"});
        
        page2json(page, jsonConfig);

        return NONE;
    }
    
    private String q;
    public void setQ(String q) {
        this.q = q;
    }
    /**  
     * 管理分区页面，查询所有区域
     */
    @Action(value = "areaAction_findAll")
    public String findAll() throws IOException {
        List<Area> list;
        
        if (StringUtils.isNoneEmpty(q)) {
            list = areaService.findByQ(q);
        }else {
            Page<Area> page = areaService.findAll(null);
            
            list = page.getContent();
        }
        
        
        // 解决懒加载
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[] {"subareas"});
        
        list2json(list, jsonConfig);
        
        return NONE;
    }
}
