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
package org.hifivault.geo.location;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * A spring bean to resolve visitor's IP to a ISO2 country code
 * 
 * @author Wouter Heijke / HiFiVault.org
 */
public class GeoLocationManager {

    private static final Log log = LogFactory.getLog(GeoLocationManager.class);

    private static GeoLocationService geoLocationService;

    private GeoInfo geoInfo = null;

    private String notfound = "US";

    private static final String geoInfoAttribute = "org.hifivault.geolocation.GeoInfo";

    @Required
    public static void setGeoLocationService(
            GeoLocationService geoLocationService) {
        GeoLocationManager.geoLocationService = geoLocationService;
    }

    public void setNotfound(String notfound) {
        this.notfound = notfound;
    }

    public GeoInfo getGeoInfo(HttpSession session) {
        Object geoi = session.getAttribute(getGeoInfoAttribute());
        if (geoi == null)
            return null;
        if (!(geoi instanceof GeoInfo)) {
            log.error("GeoInfo: ERROR: bad atttibute " + getGeoInfoAttribute()
                    + ": (" + geoi.getClass().getName() + ")");
            return null;
        }
        GeoInfo mp = (GeoInfo) geoi;
        return mp;
    }

    public GeoInfo getGeoInfo(HttpServletRequest httpServletRequest) {
        return getGeoInfo(httpServletRequest.getSession());
    }

    public GeoInfo resolve(HttpSession session,
            HttpServletRequest httpServletRequest,
            HashMap<String, String> override) {
        if (session == null)
            throw new NullPointerException("session");

        String ip = httpServletRequest.getRemoteAddr();
        String code = geoLocationService.getCountryCode(ip);
        String target = code;

        if (code != null) {
            if (code.equalsIgnoreCase("--")) {
                code = notfound;
                target = notfound;
                if (log.isDebugEnabled()) {
                    log.error("unable to resolve ip to location '" + ip
                            + "' using '" + notfound + "'");
                }
            }
            if (override != null) {
                String manual = override.get(code);
                if (manual != null) {
                    target = manual;
                }
            }
            GeoInfo gi = new GeoInfo(ip, code, target);
            session.setAttribute(getGeoInfoAttribute(), gi);
            return gi;
        } else {
            log.error("could not resolve ip to location '" + ip + "'");
        }

        return null;
    }

    public GeoInfo getGeoInfo() {
        return geoInfo;
    }

    public static String getGeoInfoAttribute() {
        return geoInfoAttribute;
    }

}
