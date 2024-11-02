package api.godevox.config;

import api.godevox.filters.RequestBodyValidator;
import api.godevox.filters.RequestHeaderValidator;
import api.godevox.filters.ResponseHeaderFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    private static final Logger logger = LoggerFactory.getLogger(FilterConfig.class);

    @Bean
    public FilterRegistrationBean<RequestHeaderValidator> requestHeaderValidator() {
        logger.info("Creating RequestHeaderValidator filter registration bean");
        FilterRegistrationBean<RequestHeaderValidator> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RequestHeaderValidator());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(1);
        logger.info("RequestHeaderValidator filter configured with order 1 and URL pattern /*");
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<ResponseHeaderFilter> responseHeaderFilter() {
        logger.info("Creating ResponseHeaderFilter filter registration bean");
        FilterRegistrationBean<ResponseHeaderFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new ResponseHeaderFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(3);
        logger.info("ResponseHeaderFilter filter configured with order 3 and URL pattern /*");
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<RequestBodyValidator> requestBodyValidator() {
        logger.info("Creating RequestBodyValidator filter registration bean");
        FilterRegistrationBean<RequestBodyValidator> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RequestBodyValidator());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(2);
        logger.info("RequestBodyValidator filter configured with order 2 and URL pattern /*");
        return registrationBean;
    }
}