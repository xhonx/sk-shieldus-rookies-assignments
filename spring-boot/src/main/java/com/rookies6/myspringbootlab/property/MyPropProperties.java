package com.rookies6.myspringbootlab.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

//application.properties 의 myprop.* 환경변수를 저장하고 조회하는 사용자 정의 Properties 클래스
@Component
@ConfigurationProperties("myprop")
@Getter @Setter
public class MyPropProperties {
    private String username;
    private int port;
}
