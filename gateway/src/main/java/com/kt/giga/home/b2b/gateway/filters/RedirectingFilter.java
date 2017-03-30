package com.kt.giga.home.b2b.gateway.filters;


import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sshinb-uf24 on 2017-01-25.
 */

@Slf4j
public class RedirectingFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return "route";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
        return rewriteRules.containsKey(request.getRequestURI());
    }

    private Map<String, String> rewriteRules = new HashMap<String, String>() {
        {
            put("/core/devices", "/dms/devices");
            put("/core/contract", "/bss/contract");
        }
    };

    @Override
    public Object run() {

        RequestContext     ctx = RequestContext.getCurrentContext();
        HttpServletRequest req = ctx.getRequest();

        String requestURI = ctx.get("requestURI").toString();

        if (rewriteRules.containsKey(requestURI)) {

            log.debug("RequestURI : {}", requestURI);
            ctx.put("requestURI", rewriteRules.get(requestURI));
        }

        return null;
    }
}