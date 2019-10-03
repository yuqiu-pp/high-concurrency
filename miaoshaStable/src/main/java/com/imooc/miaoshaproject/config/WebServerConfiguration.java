package com.imooc.miaoshaproject.config;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;


// 当Spring容器内没有TomcatEmbeddedServletContainerFactory这个bean时，
// 会把此bean加载进spring容器中

@Component   // 将类加载为bean
public class WebServerConfiguration implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {

    @Override
    public void customize(ConfigurableWebServerFactory factory) {
        // 使用对应的工程类提供给我们的接口定制化Tomcate connector
        // 外挂tomcat时，配置都在connector下
        ((TomcatServletWebServerFactory)factory).addConnectorCustomizers(new TomcatConnectorCustomizer() {
            @Override
            public void customize(Connector connector) {
                Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();

                // 定制化keepalivetimeout,30秒内没有请求服务器断开keepalive
                protocol.setKeepAliveTimeout(30000);
                // 当客户端发送超过10000个请求，则自动断开keepalive连接
                protocol.setMaxKeepAliveRequests(10000);

            }
        });
    }
}
