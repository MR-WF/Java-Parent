import com.wf.netty.service.MyService;
import com.wf.netty.service.ServiceDiscoveryService;
import com.wf.netty.service.impl.ServiceDiscoveryServiceImpl;
import com.wf.netty.utils.proxy.RpcClientProxy;
import io.netty.bootstrap.ServerBootstrap;

/**
 * @description:
 * @author: it.wf
 * @create: 2021-01-15 17:32
 **/
public class MyTestRpc {
    public static void main(String[] args) {
        ServiceDiscoveryService serviceDiscoveryService = new ServiceDiscoveryServiceImpl();
        String url = serviceDiscoveryService.discover("com.wf.netty.service.MyService");
        RpcClientProxy rpcClientProxy = new RpcClientProxy(serviceDiscoveryService);

        MyService myService = rpcClientProxy.create(MyService.class);
        System.out.println(myService.helloWord("我是客户端"));
    }
}
