/*
 * Copyright 2017 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.opensymphony.xwork2.spring;

import com.opensymphony.xwork2.*;
import com.opensymphony.xwork2.config.providers.XmlConfigurationProvider;
import com.opensymphony.xwork2.util.ProxyUtil;
import org.springframework.context.ApplicationContext;

/**
 * Test various utility methods dealing with spring proxies.
 *
 */
public class SpringProxyUtilTest extends XWorkTestCase {
    private ApplicationContext appContext;

    @Override public void setUp() throws Exception {
        super.setUp();

        // Set up XWork
        XmlConfigurationProvider provider = new XmlConfigurationProvider("com/opensymphony/xwork2/spring/actionContext-xwork.xml");
        container.inject(provider);
        loadConfigurationProviders(provider);
        appContext = ((SpringObjectFactory)container.getInstance(ObjectFactory.class)).appContext;
    }

    public void testIsSpringAopProxy() throws Exception {
        Object simpleAction = appContext.getBean("simple-action");
        assertFalse(ProxyUtil.isSpringAopProxy(simpleAction));

        Object proxiedAction = appContext.getBean("proxied-action");
        assertTrue(ProxyUtil.isSpringAopProxy(proxiedAction));

        Object autoProxiedAction = appContext.getBean("auto-proxied-action");
        assertTrue(ProxyUtil.isSpringAopProxy(autoProxiedAction));

        Object pointcuttedTestBean = appContext.getBean("pointcutted-test-bean");
        assertTrue(ProxyUtil.isSpringAopProxy(pointcuttedTestBean));

        Object pointcuttedTestSubBean = appContext.getBean("pointcutted-test-sub-bean");
        assertTrue(ProxyUtil.isSpringAopProxy(pointcuttedTestSubBean));

        Object testAspect = appContext.getBean("test-aspect");
        assertFalse(ProxyUtil.isSpringAopProxy(testAspect));
    }

    public void testSpringUltimateTargetClass() throws Exception {
        Object simpleAction = appContext.getBean("simple-action");
        Class<?> simpleActionUltimateTargetClass = ProxyUtil.springUltimateTargetClass(simpleAction);
        assertEquals(SimpleAction.class, simpleActionUltimateTargetClass);

        Object proxiedAction = appContext.getBean("proxied-action");
        Class<?> proxiedActionUltimateTargetClass = ProxyUtil.springUltimateTargetClass(proxiedAction);
        assertEquals(SimpleAction.class, proxiedActionUltimateTargetClass);

        Object autoProxiedAction = appContext.getBean("auto-proxied-action");
        Class<?> autoProxiedActionUltimateTargetClass = ProxyUtil.springUltimateTargetClass(autoProxiedAction);
        assertEquals(SimpleAction.class, autoProxiedActionUltimateTargetClass);

        Object pointcuttedTestBean = appContext.getBean("pointcutted-test-bean");
        Class<?> pointcuttedTestBeanUltimateTargetClass = ProxyUtil.springUltimateTargetClass(pointcuttedTestBean);
        assertEquals(TestBean.class, pointcuttedTestBeanUltimateTargetClass);

        Object pointcuttedTestSubBean = appContext.getBean("pointcutted-test-sub-bean");
        Class<?> pointcuttedTestSubBeanUltimateTargetClass = ProxyUtil.springUltimateTargetClass(pointcuttedTestSubBean);
        assertEquals(TestSubBean.class, pointcuttedTestSubBeanUltimateTargetClass);

        Object testAspect = appContext.getBean("test-aspect");
        Class<?> testAspectUltimateTargetClass = ProxyUtil.springUltimateTargetClass(testAspect);
        assertEquals(TestAspect.class, testAspectUltimateTargetClass);
    }
}
