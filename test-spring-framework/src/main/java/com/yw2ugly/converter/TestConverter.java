package com.yw2ugly.converter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.DirectFieldAccessor;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.SimpleTypeConverter;
import org.springframework.core.io.ResourceEditor;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestParameterPropertyValues;

@Slf4j
public class TestConverter {
    public static void main(String[] args) {

        //testSimpleTypeConverter();
        //testDirectFieldAccessor();
        //testBeanWrapperImpl();
        //testDataBinder();

        testServletRequestDataBinder();

    }

    private static void testServletRequestDataBinder() {
        PropertyItem propertyItem = new PropertyItem();
        ServletRequestDataBinder dataBinder = new ServletRequestDataBinder(propertyItem);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("age","23");
        request.setParameter("name","Andre");
        ServletRequestParameterPropertyValues propertyValues = new ServletRequestParameterPropertyValues(request);
        dataBinder.bind(propertyValues);

        log.info("ServletRequestDataBinder >>>> {} ",propertyItem);
    }

    private static void testDataBinder() {
        PropertyItem propertyItem = new PropertyItem();
        DataBinder dataBinder = new DataBinder(propertyItem);
        dataBinder.initDirectFieldAccess();
        MutablePropertyValues propertyValues = new MutablePropertyValues();
        propertyValues.add("age","23");
        propertyValues.add("name","Ward");
        dataBinder.bind(propertyValues);
        log.info("beanWrapperImpl {} ", propertyItem);

    }

    private static void testBeanWrapperImpl() {
        PropertyItem propertyItem = new PropertyItem();
        BeanWrapperImpl beanWrapper = new BeanWrapperImpl(propertyItem);
        beanWrapper.setPropertyValue("age","23");
        beanWrapper.setPropertyValue("name","harvey");
        log.info("beanWrapperImpl {} ", propertyItem);
    }

    private static void testDirectFieldAccessor() {
        FieldItem fieldItem = new FieldItem();
        DirectFieldAccessor directFieldAccessor = new DirectFieldAccessor(fieldItem);
        directFieldAccessor.setPropertyValue("age","23");
        directFieldAccessor.setPropertyValue("name","harvey");
        //directFieldAccessor.setPropertyValue("date","20230811");
        log.info("directFieldAccessor {} ",fieldItem);
    }

    private static void testSimpleTypeConverter() {
        SimpleTypeConverter simpleTypeConverter = new SimpleTypeConverter();
        Integer integer = simpleTypeConverter.convertIfNecessary("123", int.class);
        //Date date = simpleTypeConverter.convertIfNecessary("20230811", Date.class);
        log.info("integer >>> {}",integer);
        //log.info("localDate >>> {}",date);
    }
}
