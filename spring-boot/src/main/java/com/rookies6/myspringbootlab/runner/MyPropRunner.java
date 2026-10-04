package com.rookies6.myspringbootlab.runner;

import com.rookies6.myspringbootlab.config.MyEnvironment;
import com.rookies6.myspringbootlab.property.MyPropProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class MyPropRunner implements ApplicationRunner {
    //application.properties 에 있는 환경변수를 @Value 로 Load
    @Value("${myprop.username}")
    private String username;

    @Value("${myprop.port}")
    private int port;

    @Autowired
    private MyPropProperties properties;

    //활성화된 프로파일(prod/test)에 따라 주입되는 Bean 이 달라진다
    @Autowired
    private MyEnvironment myEnvironment;

    private final Logger logger = LoggerFactory.getLogger(MyPropRunner.class);

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.debug("${myprop.username} = {}", username);
        logger.debug("${myprop.port} = {}", port);

        logger.info("MyPropProperties getUsername() = {}", properties.getUsername());
        logger.info("MyPropProperties getPort() = {}", properties.getPort());

        logger.info("현재 활성화된 MyEnvironment Bean = {}", myEnvironment);
    }
}
