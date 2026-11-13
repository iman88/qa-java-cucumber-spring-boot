package com.qa.kelvin.config;

import net.datafaker.Faker;
import com.qa.kelvin.annotation.LazyConfiguration;
import org.springframework.context.annotation.Bean;

@LazyConfiguration
public class FakerConfig {

    @Bean
    public Faker getFaker(){
        return new Faker();
    }

}
