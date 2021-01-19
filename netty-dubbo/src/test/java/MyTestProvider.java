import com.wf.netty.service.impl.RegisterCenterImpl;
import com.wf.netty.service.impl.ServiceDiscoveryServiceImpl;

import java.io.IOException;

/**
 * @description:
 * @author: it.wf
 * @create: 2021-01-15 11:12
 **/
public class MyTestProvider {

    public static void main(String[] args) throws IOException {
        RegisterCenterImpl registerCenter = new RegisterCenterImpl();
        registerCenter.register("com.wf.netty.service.MyService","127.0.0.1:8080");


        System.in.read();
    }
}
