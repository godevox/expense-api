package api.godevox.filters;

import api.godevox.helpers.ResponseHelper;
import api.godevox.models.response.ResponseModel;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class RequestBodyValidator implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(RequestBodyValidator.class);
    private static final int MAX_PAYLOAD_SIZE = 1024; // 1KB

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("Initializing RequestBodyValidator");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        if ("POST".equalsIgnoreCase(httpServletRequest.getMethod())) {
            CachedBodyHttpServletRequest cachedBodyHttpServletRequest = new CachedBodyHttpServletRequest(httpServletRequest);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            InputStream inputStream = cachedBodyHttpServletRequest.getInputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            int totalBytesRead = 0;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                totalBytesRead += bytesRead;
                if (totalBytesRead > MAX_PAYLOAD_SIZE) {
                    logger.warn("Request payload size exceeds 1KB");
                    sendErrorResponse(httpServletResponse);
                    return;
                }
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }

            chain.doFilter(cachedBodyHttpServletRequest, response);
        } else {
            chain.doFilter(request, response);
        }
    }

    private void sendErrorResponse(HttpServletResponse response) throws IOException {
        ResponseEntity<ResponseModel<String>> errorResponse = ResponseHelper.createErrorResponse("Request payload size exceeds 1KB", HttpStatus.BAD_REQUEST.getReasonPhrase());
        ResponseHelper.writeErrorResponse(response, errorResponse);
    }

    @Override
    public void destroy() {
        logger.info("Destroying RequestBodyValidator");
    }

    private static class CachedBodyHttpServletRequest extends HttpServletRequestWrapper {

        private final byte[] cachedBody;

        public CachedBodyHttpServletRequest(HttpServletRequest request) throws IOException {
            super(request);
            InputStream requestInputStream = request.getInputStream();
            this.cachedBody = requestInputStream.readAllBytes();
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            return new CachedBodyServletInputStream(this.cachedBody);
        }
    }

    private static class CachedBodyServletInputStream extends ServletInputStream {

        private final ByteArrayInputStream byteArrayInputStream;

        public CachedBodyServletInputStream(byte[] cachedBody) {
            this.byteArrayInputStream = new ByteArrayInputStream(cachedBody);
        }

        @Override
        public boolean isFinished() {
            return byteArrayInputStream.available() == 0;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener readListener) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int read() throws IOException {
            return byteArrayInputStream.read();
        }
    }
}