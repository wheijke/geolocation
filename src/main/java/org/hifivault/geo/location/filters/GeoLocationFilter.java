/*
 * Copyright 2009 Wouter Heijke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.hifivault.geo.location.filters;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hifivault.geo.location.GeoInfo;
import org.hifivault.geo.location.GeoLocationManager;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * This filters resolves the GEO location of each visitor based on their IP
 * 
 * @author Wouter Heijke / HiFiVault.org
 */
public class GeoLocationFilter implements Filter {

    private static final Log log = LogFactory.getLog(GeoLocationFilter.class);

    private GeoLocationManager manager;

    private HashMap<String, String> override = new HashMap<String, String>();

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        if (log.isDebugEnabled()) {
            log.debug("==>GeoLocationFilter doFilter<==");
        }

        // get session
        HttpSession session = httpServletRequest.getSession();

        GeoInfo location = manager.getGeoInfo(session);
        if (location == null) {
            location = manager.resolve(session, httpServletRequest, override);
        }

        if (log.isDebugEnabled()) {
            log.debug("GeoInfo='" + location + "'");
        }

        chain.doFilter(request, response);
        return;

    }

    public void init(FilterConfig filterConfig) throws ServletException {

        BeanFactory beanFactory = WebApplicationContextUtils
                .getRequiredWebApplicationContext(filterConfig
                        .getServletContext());
        manager = (GeoLocationManager) beanFactory
                .getBean("geoLocationManager");

        Enumeration<String> names = filterConfig.getInitParameterNames();
        while (names.hasMoreElements()) {
            String key = names.nextElement();
            String value = filterConfig.getInitParameter(key);
            if (value != null && value.length() > 0) {
                override.put(key.toUpperCase(), value.toUpperCase());
            }
        }
    }

    public void destroy() {

    }

}
