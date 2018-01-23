package com.itheima.crm.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.crm.dao.CustomerRepository;
import com.itheima.crm.domain.Customer;
import com.itheima.crm.service.CustomerService;

/**  
 * ClassName:CustomerServiceImpl <br/>  
 * Function:  <br/>  
 * Date:     Jan 20, 2018 4:01:51 PM <br/>       
 */

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService{

    @Autowired
    private CustomerRepository customerRepository;
    
    @Override
    public List<Customer> findAll() {
        
        return customerRepository.findAll();
    }

    @Override
    public List<Customer> findCustomerUnAssociated() {
        return customerRepository.findByFixedAreaIdIsNull();
    }

    @Override
    public List<Customer> findCustomerAssociated2FixedArea(String fixedAreaId) {
        return customerRepository.findByFixedAreaId(fixedAreaId);
    }

    @Override
    public void assignCustomers2FixedArea(Long[] customerIds,String fixedAreaId) {
        
        if (StringUtils.isNotEmpty(fixedAreaId)) {
            
            //将所有的指定id定区中的客户设置定区为null
            customerRepository.unbindByFixedAreaId(fixedAreaId);
           
            if (customerIds != null && customerIds.length > 0) {
                //添加客户
                for (Long customerId : customerIds) {
                    
                    customerRepository.bindFixedAreaById(fixedAreaId,customerId);
                }
            }
        }
    }

    /**  
     * 添加用户
     */
    @Override
    public void saveCustomer(Customer customer) {
        customerRepository.save(customer);
    }
    
    @Override
    public Customer findByTelephone(String telephone) {
        System.out.println("findByTelephone");
        return customerRepository.findByTelephone(telephone);
    }
    
    @Override
    public void active(String telephone) {
        System.out.println("active");
        customerRepository.active(telephone);  
    }
    
    @Override
    public Customer login(String telephone, String password) {
        return customerRepository.findByTelephoneAndPassword(telephone,password);
    }

}
  
