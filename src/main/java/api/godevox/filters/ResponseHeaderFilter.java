package api.godevox.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ResponseHeaderFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(ResponseHeaderFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("Initializing ResponseHeaderFilter");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("X-Content-Type-Options", "nosniff");
        httpServletResponse.setHeader("X-Frame-Options", "DENY");
        httpServletResponse.setHeader("X-XSS-Protection", "1; mode=block");
        httpServletResponse.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains");
        httpServletResponse.setHeader("Content-Security-Policy", "default-src 'self'");
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        logger.info("Destroying ResponseHeaderFilter");
    }
}