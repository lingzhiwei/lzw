package com.itheima.bos.service.base;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itheima.bos.domain.base.Area;

/**  
 * ClassName:AreaService <br/>  
 * Function:  <br/>  
 * Date:     Jan 17, 2018 4:14:59 PM <br/>       
 */
public interface AreaService {

    void save(ArrayList<Area> list);

    Page<Area> findAll(Pageable pageable);

    List<Area> findByQ(String q);

}
  
