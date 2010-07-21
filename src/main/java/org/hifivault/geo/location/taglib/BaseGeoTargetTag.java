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
package org.hifivault.geo.location.taglib;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hifivault.geo.location.GeoInfo;
import org.hifivault.geo.location.GeoLocationManager;

/**
 * Target the country ID to another id
 * 
 * @author Wouter Heijke / HiFiVault.org
 */
public abstract class BaseGeoTargetTag extends SimpleTagSupport {

    private static Log log = LogFactory.getLog(BaseGeoTargetTag.class);

    /**
     * JSP variable name.
     */
    public String var;

    @Override
    public void doTag() throws JspException, IOException {
        PageContext ctx = (PageContext) getJspContext();
        HttpServletRequest req = (HttpServletRequest) ctx.getRequest();
        HttpSession session = ctx.getSession();

        String mappedCode = defaultId();
        GeoInfo geo = (GeoInfo) session.getAttribute(GeoLocationManager
                .getGeoInfoAttribute());
        if (geo != null) {
            String source = geo.getCountryCode();
            String code = geo.getTargetCode();
            mappedCode = mapId(code);
            if (log.isDebugEnabled()) {
                log.debug("====>GeoLocate: '" + source + "/" + code + "' to '"
                        + mappedCode + "'");
            }
        }

        // handle result
        if (var != null) {
            // put in variable
            if (mappedCode.length() > 0) {
                req.setAttribute(var, mappedCode);
            } else {
                req.removeAttribute(var);
            }
        } else {
            // write
            ctx.getOut().print(mappedCode);
        }
    }

    protected abstract String mapId(String code);

    protected abstract String defaultId();

    public void setVar(String var) {
        this.var = var;
    }

}
