package api.godevox.filters;

import api.godevox.helpers.ResponseHelper;
import api.godevox.models.response.ResponseModel;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.UUID;

public class RequestHeaderValidator implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(RequestHeaderValidator.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("Initializing RequestHeaderValidator");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        String requestId = httpServletRequest.getHeader("X-Request-ID");

        if (requestId == null || !isValidUUID(requestId)) {
            logger.warn("Missing or invalid X-Request-ID header");
            sendErrorResponse(httpServletResponse);
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean isValidUUID(String uuid) {
        try {
            UUID.fromString(uuid);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private void sendErrorResponse(HttpServletResponse response) throws IOException {
        ResponseEntity<ResponseModel<String>> errorResponse = ResponseHelper.createErrorResponse("Missing or invalid X-Request-ID header", HttpStatus.BAD_REQUEST.getReasonPhrase());
        ResponseHelper.writeErrorResponse(response, errorResponse);
    }

    @Override
    public void destroy() {
        logger.info("Destroying RequestHeaderValidator");
    }
}