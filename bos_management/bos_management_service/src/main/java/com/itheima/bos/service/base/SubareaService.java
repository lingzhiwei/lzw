package com.itheima.bos.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.itheima.bos.domain.base.SubArea;

/**  
 * ClassName:SubareaService <br/>  
 * Function:  <br/>  
 * Date:     Jan 18, 2018 3:33:27 PM <br/>       
 */
public interface SubareaService {

    void save(SubArea subArea);

    Page<SubArea> findAll(Specification specification, Pageable pageable);

    List<SubArea> findSubAreaUnAssociated();

    List<SubArea> findSubAreaAssociated2FixedArea(Long id);

    void assignSubArea2FixedArea(Long[] subAreaIds, Long id);

}
  
