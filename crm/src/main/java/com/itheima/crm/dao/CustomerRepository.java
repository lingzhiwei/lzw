package com.itheima.crm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.itheima.crm.domain.Customer;

/**
 * ClassName:CustomerRepository <br/>
 * Function: <br/>
 * Date: Jan 20, 2018 4:03:52 PM <br/>
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // 查找未关联定区的客户
    List<Customer> findByFixedAreaIdIsNull();

    // 查找关联到指定定区的客户,传定区id
    List<Customer> findByFixedAreaId(String fixedAreaId);

    // 将所有的指定id定区中的客户设置定区为null
    @Modifying
    @Query("update Customer set fixedAreaId = null where fixedAreaId = ? ")
    void unbindByFixedAreaId(String fixedAreaId);

    // 添加客户
    @Modifying
    @Query("update Customer set fixedAreaId = ? where id = ? ")
    void bindFixedAreaById(String fixedAreaId,Long id);

    
    Customer findByTelephone(String telephone);

    //激活用户
    @Modifying
    @Query("update Customer set type = 1 where telephone = ?")
    void active(String telephone);

    //登录
    Customer findByTelephoneAndPassword(String telephone, String password);

}
