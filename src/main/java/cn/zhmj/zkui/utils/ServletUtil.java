/**
 *
 * Copyright (c) 2014, Deem Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 */
package cn.zhmj.zkui.utils;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

public enum ServletUtil {

	INSTANCE;
    private Logger logger = LoggerFactory.getLogger(ServletUtil.class);

    public ServletUtil getLogger() {
    	return getLogger(ServletUtil.class);
	}

    public ServletUtil getLogger(Class<?> clazz) {
    	logger = LoggerFactory.getLogger(clazz);
    	return ServletUtil.INSTANCE;
	}

    public void info(String message) {
		logger.info("message - {}", message);
	}
    
    public void info(HttpServletRequest request) {
		logger.info("[{}] - no params", request.getRequestURI());
	}
	
    public void info(HttpServletRequest request, Object params) {
		logger.info("[{}] - params - {}", request.getRequestURI(), JSONObject.toJSONString(params));
	}

    public void debug(String message) {
		logger.debug("message - {}", message);
	}
    
    public void debug(HttpServletRequest request) {
		logger.debug("[{}] - no params", request.getRequestURI());
	}
	
    public void debug(HttpServletRequest request, Object params) {
		logger.debug("[{}] - params - {}", request.getRequestURI(), JSONObject.toJSONString(params));
	}

    //Using X-Forwarded-For to capture IP addresses coming via load balancer.
    public String getRemoteAddr(HttpServletRequest request) {
        String remoteAddr = request.getHeader("X-Forwarded-For");
        if (remoteAddr == null) {
            remoteAddr = request.getRemoteAddr();
        }
        return remoteAddr;
    }

}
