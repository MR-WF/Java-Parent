import com.wf.netty.service.impl.ServiceDiscoveryServiceImpl;

import java.io.IOException;

/**
 * @description:
 * @author: it.wf
 * @create: 2021-01-15 11:41
 **/
public class MyTestConsumer {

    public static void main(String[] args) throws IOException {

        ServiceDiscoveryServiceImpl serviceDiscoveryService = new ServiceDiscoveryServiceImpl();
        System.out.println(serviceDiscoveryService.discover("com.wf.netty.service.MyService"));
       // System.in.read();
    }
}
