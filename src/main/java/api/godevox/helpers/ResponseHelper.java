package api.godevox.helpers;

import api.godevox.constants.MessageConstants;
import api.godevox.models.error.ErrorModel;
import api.godevox.models.response.ResponseModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public class ResponseHelper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> ResponseEntity<ResponseModel<T>> createSuccessResponse(T data, String message) {
        ResponseModel<T> response = new ResponseModel.Builder<T>()
                .status(MessageConstants.SUCCESS_STATUS)
                .data(data)
                .build();
        return ResponseEntity.ok(response);
    }

    public static <T> ResponseEntity<ResponseModel<T>> createErrorResponse(String message, String errorCode) {
        ErrorModel error = new ErrorModel(errorCode, message);
        ResponseModel<T> response = new ResponseModel.Builder<T>()
                .status(MessageConstants.ERROR_STATUS)
                .error(error)
                .build();
        return ResponseEntity.status(500).body(response);
    }

    public static void writeErrorResponse(HttpServletResponse response, ResponseEntity<ResponseModel<String>> responseEntity) throws IOException {
        response.setStatus(responseEntity.getStatusCode().value());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(responseEntity.getBody()));
    }
}