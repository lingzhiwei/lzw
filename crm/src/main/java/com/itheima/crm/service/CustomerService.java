package com.itheima.crm.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.itheima.crm.domain.Customer;



/**  
 *  提供服务的接口    
 */

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface CustomerService {

    @GET
    @Path("/findAll")
    public List<Customer> findAll();
    
    //查找未关联定区的客户
    @GET
    @Path(value = "/findCustomerUnAssociated")
    public List<Customer> findCustomerUnAssociated();
    
    //查找关联到指定定区的客户,传定区id
    @GET
    @Path(value = "/findCustomerAssociated2FixedArea")
    public List<Customer> findCustomerAssociated2FixedArea(@QueryParam("fixedAreaId") String fixedAreaId);
    
    //更新指定定区客户
    @PUT
    @Path(value = "/assignCustomers2FixedArea")
    public void assignCustomers2FixedArea(@QueryParam("customerIds") Long[] customerIds,@QueryParam("fixedAreaId") String fixedAreaId);
    
    //添加客户
    @POST
    @Path(value = "/saveCustomer")
    public void saveCustomer(Customer customer);
    
    //判断用户是否已经激活
    @GET
    @Path(value = "/findByTelephone")
    public Customer findByTelephone(@QueryParam("telephone") String telephone);
    
    //激活客户
    @PUT
    @Path(value = "/active")
    public void active(@QueryParam("telephone") String telephone);
    
    
    //客户登录
    @GET
    @Path(value = "/login")
    public Customer login(@QueryParam("telephone") String telephone,@QueryParam("password") String password);
}
  
