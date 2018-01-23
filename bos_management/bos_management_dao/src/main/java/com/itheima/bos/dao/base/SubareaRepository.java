package com.itheima.bos.dao.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.itheima.bos.domain.base.SubArea;

/**  
 * 管理分区  的接口
 * 
 * 接口参数：
 * 参数一：操作的数据对应的实体类
 * 参数二：类中主键的类型
 */
public interface SubareaRepository extends JpaRepository<SubArea, Long>,JpaSpecificationExecutor<SubArea>{

    List<SubArea> findByFixedAreaIdIsNull();

    List<SubArea> findByFixedAreaId(Long id);

    @Modifying
    @Query("update SubArea set fixedArea.id = null where fixedArea.id = ?")
    void unbindByFixedAreaId(Long id);

    @Modifying
    @Query("update SubArea set fixedArea.id = ? where id = ?")
    void bindFixedAreaById(Long fid, Long sid);
   
}
  
