package com.itheima.bos.dao.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.domain.base.Standard;

/**  
 * 派送标准  的接口
 * 
 * 接口参数：
 * 参数一：操作的数据对应的实体类
 * 参数二：类中主键的类型
 */
public interface StandardRepository extends JpaRepository<Standard, Long>{

    Standard findByName(String name);
    
    //JPQL语句  === HQL语句
   // @Query("from Standard where name like ?")
    @Query(value="select * from T_STANDARD where C_NAME like ?",nativeQuery=true)
    List<Standard> findByNameLikeXXXX(String name);
    
    @Transactional
    @Modifying
    @Query("delete from Standard where name=?")
    void deleteByName(String name);
}
  
