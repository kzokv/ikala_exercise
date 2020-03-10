package org.ikala.helper;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
//Please ensure that all Class that need to become Spring beans should be put into below package
@ComponentScan(basePackages = {"org.ikala"})

public class IkalaBeans {
}
