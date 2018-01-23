package portal;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**  
 * 测试类   
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class RedisTest {

    @Autowired
    private RedisTemplate<String,String> template;
    
    @Test
    public void test01(){
        //添加数据
        template.opsForValue().append("name3", "zhangsan");
    }
    
    @Test
    public void test02(){
        //删除数据
        template.delete("name2");
    }
     
    @Test
    public void test03(){
        //添加数据，并指定有效时间
        //设置有效时间为20秒
        template.opsForValue().set("name3", "zhangsan", 20, TimeUnit.SECONDS);
    }
}
  
