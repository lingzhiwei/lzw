package com.itheima.bos.service.base.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.base.CourierRepository;
import com.itheima.bos.domain.base.Courier;
import com.itheima.bos.service.base.CourierService;

/**  
 * 派送标准      
 */

@Service   //Service层注解扫描
@Transactional  //开启事务
public class CourierServiceImpl implements CourierService {

    @Autowired
    private CourierRepository courierRepository;
    
    @Override
    public void save(Courier model) {
        courierRepository.save(model);
    }

    @Override
    public Page<Courier> findAll(Specification specification,Pageable pageable) {
        return courierRepository.findAll(specification,pageable);
    }

    @Override
    public void batchDel(String ids) {
        if (StringUtils.isNotEmpty(ids)) {
            //不为空
            String[] strings = ids.split(",");
            for (String id : strings) {
                courierRepository.updateDelTagById('1',Long.parseLong(id));
            }
        }
        
    }

    @Override
    public void restore(String ids) {
        if (StringUtils.isNotEmpty(ids)) {
            //不为空
            String[] strings = ids.split(",");
            for (String id : strings) {
                courierRepository.updateDelTagById('0',Long.parseLong(id));
            }
        }
    }

    /**  
     * 分页搜索查询（多条件）的另一种实现方法，使用JPA自带的QBE查询
     */
    @Override
    public Page<Courier> queryPage(Courier model, Pageable pageable) {
        //默认以等于查询，当有其他条件时，要设置Example匹配器
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                //CONTAINING 包含，表示模糊查询like
                .withMatcher("company", ExampleMatcher.GenericPropertyMatcher.of(ExampleMatcher.StringMatcher.CONTAINING))
                //连接查询
                .withMatcher("standard.name", ExampleMatcher.GenericPropertyMatcher.of(ExampleMatcher.StringMatcher.CONTAINING));
        Example<Courier> courierExample = Example.of(model,exampleMatcher);
        Page<Courier> couriers = courierRepository.findAll(courierExample,pageable);
        return couriers;
    }

}
  
