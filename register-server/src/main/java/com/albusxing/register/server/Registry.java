package com.albusxing.register.server;
import lombok.extern.slf4j.Slf4j;
import java.util.HashMap;
import java.util.Map;

/**
 * 注册表
 * @author liguoqing
 */
@Slf4j
public class Registry {

    /**
     * 注册表对象是一个单例
     */
    private static final Registry INSTANCE = new Registry();

    /**
     * 注册表中的核心数据结构
     * 负责存储所有的服务实例信息
     * Map:  <服务名称，<服务实例id, 服务实例信息>>
     */
    private Map<String, Map<String, ServiceInstance>> registry = new HashMap<>();

    /**
     * 私有化构造器
     */
    private Registry(){
    }

    /**
     * 获取注册表实例
     * @return
     */
    public static Registry getInstance() {
        return INSTANCE;
    }


    /**
     * 服务注册
     * @param serviceInstance 服务实例
     */
    public void register(ServiceInstance serviceInstance) {
        Map<String, ServiceInstance> serviceInstanceMap = registry.get(serviceInstance.getServiceName());
        if (null == serviceInstanceMap) {
            serviceInstanceMap = new HashMap<>();
        }
        serviceInstanceMap.put(serviceInstance.getServiceInstanceId(), serviceInstance);
        registry.put(serviceInstance.getServiceName(), serviceInstanceMap);
        log.info("服务实例【{}】完成注册", serviceInstance.getServiceInstanceId());
        log.info("注册表：{}", registry);
    }

    /**
     * 获取服务实例
     * @param serviceName 服务名称
     * @param serviceInstanceId 服务实例id
     * @return
     */
    public ServiceInstance getServiceInstance(String serviceName, String serviceInstanceId) {
        return registry.get(serviceName).get(serviceInstanceId);
    }

    /**
     * 获取注册中心中注册的服务实例
     * @return
     */
    public Map<String, Map<String, ServiceInstance>> getRegistry() {
        return registry;
    }

    /**
     * 移除服务实例
     * @param serviceName
     * @param serviceInstanceId
     */
    public void removeServiceInstance(String serviceName, String serviceInstanceId) {
        log.info("移除服务实例:[{}]({})", serviceName, serviceInstanceId);
        Map<String, ServiceInstance> serviceInstanceMap = registry.get(serviceName);
        serviceInstanceMap.remove(serviceInstanceId);
    }
}
