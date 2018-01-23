package com.itheima.bos.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.itheima.bos.domain.base.Courier;

/**  
 * ClassName:CourierRepository <br/>  
 * Function:  <br/>  
 * Date:     Jan 15, 2018 4:49:50 PM <br/>       
 */
public interface CourierRepository  extends JpaRepository<Courier, Long>,JpaSpecificationExecutor<Courier>  {
    @Query("update Courier set deltag = ? where id = ?")
    @Modifying
    void updateDelTagById(Character i,long l);

}
  
