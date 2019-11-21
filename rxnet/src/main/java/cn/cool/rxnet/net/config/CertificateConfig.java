package cn.cool.rxnet.net.config;

import java.util.HashMap;
import java.util.Map;

/**
 *@author boluo
 *类描述：处理多个二级域名所需要的证书
 *版本号：1.0
 *需要在appplication的onCreate后初始化
 *修改时间： 2019/6/28
 */
public class CertificateConfig {
    public Map<String,Object> certificates;
    private CertificateConfig(){
        certificates = new HashMap<>();
    }
    public static CertificateConfig geCertificateConfig(){
        return SingletonCertificateConfig.instance;
    }
    private static class SingletonCertificateConfig{
        private final static CertificateConfig instance = new CertificateConfig();
    }
}
