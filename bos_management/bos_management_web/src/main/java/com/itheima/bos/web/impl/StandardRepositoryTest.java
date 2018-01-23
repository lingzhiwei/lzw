package com.itheima.bos.web.impl;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.base.StandardRepository;
import com.itheima.bos.domain.base.Standard;

/**  
 *测试类，通过派送标准，测试Spring Data JPA
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class StandardRepositoryTest {
    
    @Autowired
    private StandardRepository standardRepository;
    
    @Test
    public void test(){
        List<Standard> list = standardRepository.findAll();
        for (Standard standard : list) {
            System.out.println(standard);
        }
    }
    
    
    @Test
    public void testAdd() {
        Standard standard = new Standard();
        standard.setName("李四四");
        standard.setMaxLength(100);
        standardRepository.save(standard);
    }
    @Test
    public void testFindOne() {
        
        Standard standard = standardRepository.findOne(1L);
        
    }
    @Test
    public void testFindByName() {
        
        Standard standard = standardRepository.findByName("李四");
        System.out.println(standard);
    }
    
    @Test
    public void testFindByNameLikeXXXX() {
        
        List<Standard> list = standardRepository.findByNameLikeXXXX("李四%");
        for (Standard standard : list) {
            
            System.out.println(standard);
        }
    }
    
    @Test
    public void testDeleteByName() {
       standardRepository.deleteByName("李四四");
       
    }
}
  
