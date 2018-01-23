package portal;

import com.itheima.utils.MailUtils;

/**  
 * ClassName:MailTest <br/>  
 * Function:  <br/>  
 * Date:     Jan 23, 2018 3:32:44 PM <br/>       
 */
public class MailTest {

    public static void main(String[] args) {
        MailUtils.sendMail("zs@store.com", "测试", "测试发送邮件成功！");
        System.out.println("+++++++++++");
    }
}
  
