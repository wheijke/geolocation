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

/**
 * Visitor's geographic information
 * 
 * @author Wouter Heijke / HiFiVault.org
 */
public class GeoInfo {

    private String ip;

    private String countryCode;

    private String targetCode;

    public GeoInfo(String ip, String countryCode, String targetCode) {
        this.ip = ip;
        this.countryCode = countryCode;
        this.targetCode = targetCode;
    }

    public String getIp() {
        return ip;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getTargetCode() {
        return targetCode;
    }

    @Override
    public String toString() {
        return "GeoInfo[" + ip + "," + countryCode + "," + targetCode + "]";
    }
}
