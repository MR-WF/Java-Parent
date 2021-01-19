import com.wf.netty.service.MyService;
import com.wf.netty.service.RegisterCenter;
import com.wf.netty.service.impl.MyServiceImpl;
import com.wf.netty.service.impl.RegisterCenterImpl;
import com.wf.netty.utils.server.RpcServer;

/**
 * @description:
 * @author: it.wf
 * @create: 2021-01-15 17:37
 **/
public class MyRpcServer {
    public static void main(String[] args) {
        MyService myService = new MyServiceImpl();
        RegisterCenter registerCenter = new RegisterCenterImpl();
        RpcServer rpcServer = new RpcServer(registerCenter, "127.0.0.1:8088");
        rpcServer.bind(myService);
        rpcServer.publisher();
    }
}
