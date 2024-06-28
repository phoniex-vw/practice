package com.yw2ugly.converter;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.format.Formatter;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.bind.ServletRequestParameterPropertyValues;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.InvocableHandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.ServletRequestDataBinderFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Slf4j
public class TestServletRequestBinder {

    public static void main(String[] args) throws Exception {
        //1.initBinder
        InvocableHandlerMethod invocableHandlerMethod = new InvocableHandlerMethod(new WebConfig(), WebConfig.class.getMethod("initBinder", WebDataBinder.class));
        //2.conversionService
        ConfigurableWebBindingInitializer webBindingInitializer = new ConfigurableWebBindingInitializer();
        FormattingConversionService formattingConversionService = new FormattingConversionService();
        formattingConversionService.addFormatter(new CustomDateFormatter2("conversionService added"));
        webBindingInitializer.setConversionService(formattingConversionService);
        ServletRequestDataBinderFactory servletRequestDataBinderFactory =
                new ServletRequestDataBinderFactory(List.of(invocableHandlerMethod),webBindingInitializer);

        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.setParameter("age","23");
        httpServletRequest.setParameter("name","Amanda");
        httpServletRequest.setParameter("d1","1992|08|11");
        httpServletRequest.setParameter("d2","2023:09:30");

        ServletWebRequest servletWebRequest = new ServletWebRequest(httpServletRequest);
        PropertyItem propertyItem = new PropertyItem();
        WebDataBinder binder = servletRequestDataBinderFactory.createBinder(servletWebRequest, propertyItem, "xx");

        ServletRequestParameterPropertyValues propertyValues = new ServletRequestParameterPropertyValues(httpServletRequest);

        binder.bind(propertyValues);

        log.info("ServetRequestDataBinder >>>>>> {} ", propertyItem);


        // default conversion service
        ConfigurableWebBindingInitializer webBindingInitializer1 = new ConfigurableWebBindingInitializer();
        DefaultConversionService defaultConversionService = new DefaultConversionService();
        webBindingInitializer1.setConversionService(defaultConversionService);
        ServletRequestDataBinderFactory servletRequestDataBinderFactory1 = new ServletRequestDataBinderFactory(null, webBindingInitializer1);

        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest("GET","/t2");
        mockHttpServletRequest.setParameter("d2","2023/08/11");
        PropertyItem propertyItem1 = new PropertyItem();
        WebDataBinder binder1 = servletRequestDataBinderFactory1.createBinder(new ServletWebRequest(mockHttpServletRequest), propertyItem1, "xx");
        ServletRequestParameterPropertyValues propertyValues1 = new ServletRequestParameterPropertyValues(mockHttpServletRequest);
        binder1.bind(propertyValues1);

        log.info(" default Conversion service ------>{} ",propertyItem1);

    }


    static class WebConfig {
        @InitBinder
        public void initBinder(WebDataBinder dataBinder){
            //add custom converters
            dataBinder.addCustomFormatter(new CustomDateFormatter1("initBinder added"));
        }
    }

    @Slf4j
    static class CustomDateFormatter1 implements Formatter<Date> {

        private String desc;

        public CustomDateFormatter1(String desc) {
            this.desc = desc;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy|MM|dd");
        @Override
        public Date parse(String text, Locale locale) throws ParseException {
            log.info(" >>>>>>>>> {} ",desc);
            return dateFormat.parse(text);
        }

        @Override
        public String print(Date date, Locale locale) {
            log.info(" >>>>>>>>> {} ",desc);
            return dateFormat.format(date);
        }
    }


    @Slf4j
    static class CustomDateFormatter2 implements Formatter<Date> {

        private String desc;

        public CustomDateFormatter2(String desc) {
            this.desc = desc;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd");
        @Override
        public Date parse(String text, Locale locale) throws ParseException {
            log.info(" >>>>>>>>> {} ",desc);
            return dateFormat.parse(text);
        }

        @Override
        public String print(Date date, Locale locale) {
            log.info(" >>>>>>>>> {} ",desc);
            return dateFormat.format(date);
        }
    }

}
