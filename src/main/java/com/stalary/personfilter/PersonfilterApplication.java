package com.stalary.personfilter;

import com.stalary.personfilter.filter.CrossOriginFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.context.request.async.DeferredResult;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.Filter;
import javax.sql.DataSource;

@SpringBootApplication
@Configuration
@EnableSwagger2
@EnableTransactionManagement
public class PersonfilterApplication {

    public static void main(String[] args) {
        SpringApplication.run(PersonfilterApplication.class, args);
    }

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .genericModelSubstitutes(DeferredResult.class)
                .useDefaultResponseMessages(false)
                .apiInfo(apiInfo())
                .pathMapping("/")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.stalary.personfilter.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("人才筛选")
                .description("源码请访问：https://github.com/stalary/personfilter")
                .termsOfServiceUrl("stalary.com")
                .version("1.0")
                .build();
    }

    @Bean
    @Primary
    @ConfigurationProperties(prefix="spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public FilterRegistrationBean crossOriginFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(crossOriginFilter());
        registration.addUrlPatterns("*");
        registration.setName("crossOriginFilter");
        registration.setOrder(1);
        return registration;
    }

    @Bean(name = "crossOriginFilter")
    public Filter crossOriginFilter() {
        return new CrossOriginFilter();
    }
}
