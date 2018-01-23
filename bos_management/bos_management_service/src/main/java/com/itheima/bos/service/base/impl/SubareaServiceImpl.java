package com.itheima.bos.service.base.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.base.SubareaRepository;
import com.itheima.bos.domain.base.SubArea;
import com.itheima.bos.service.base.SubareaService;

/**  
 * 管理分区      
 */

@Service   //Service层注解扫描
@Transactional  //开启事务
public class SubareaServiceImpl implements SubareaService {

    @Autowired
    private SubareaRepository subareaRepository;
    
    @Override
    public void save(SubArea subArea) {
        subareaRepository.save(subArea);
    }

    @Override
    public Page<SubArea> findAll(Specification specification, Pageable pageable) {
        return subareaRepository.findAll(specification,pageable);
    }

    @Override
    public List<SubArea> findSubAreaUnAssociated() {
          
        return subareaRepository.findByFixedAreaIdIsNull();
    }

    @Override
    public List<SubArea> findSubAreaAssociated2FixedArea(Long id) {
          
        // TODO Auto-generated method stub  
        return subareaRepository.findByFixedAreaId(id);
    }

   
    @Override
    public void assignSubArea2FixedArea(Long[] subAreaIds, Long fid) {
        
        if (fid != null) {
            
            //解除指定定区的分区绑定
            subareaRepository.unbindByFixedAreaId(fid);
            
            if (subAreaIds != null && subAreaIds.length > 0) {
                for (Long sid : subAreaIds) {
                    
                    //添加分区
                    subareaRepository.bindFixedAreaById(fid,sid);
                }
            }
        }
        
        
    }


}
  
