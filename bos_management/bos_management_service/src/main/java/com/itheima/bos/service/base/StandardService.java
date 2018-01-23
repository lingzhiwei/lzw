package com.itheima.bos.service.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itheima.bos.domain.base.Standard;

/**  
 * ClassName:StandardService <br/>  
 * Function:  <br/>  
 * Date:     Jan 14, 2018 6:34:11 PM <br/>       
 */
public interface StandardService {
    void save(Standard standard);

    Page<Standard> findAll(Pageable pageable);
}
  
