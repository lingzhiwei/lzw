package com.itheima.bos.service.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.itheima.bos.domain.base.Courier;

/**  
 * ClassName:CourierService <br/>  
 * Function:  <br/>  
 * Date:     Jan 15, 2018 4:44:53 PM <br/>       
 */
public interface CourierService {

    void save(Courier model);

    Page<Courier> findAll(Specification specification, Pageable pageable);

    void batchDel(String ids);

    void restore(String ids);

    Page<Courier> queryPage(Courier model, Pageable pageable);

}
  
