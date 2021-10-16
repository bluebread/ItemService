package lab2.itemService.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

/**
 * Response body structure
 */
public final class JsonResult {
    // Pre-define instance for error
    public static final Error ERROR_UNAUTHORIZED = error(HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.getReasonPhrase());
    public static final Error ERROR_NOT_FOUND = error(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.getReasonPhrase());
    public static final Error ERROR_BAD_REQUEST = error(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase());
    public static final Error ERROR_INVALID_PARAMETER = error(HttpStatus.BAD_REQUEST, "Invalid parameter");
    public static final Error ERROR_INTERNAL = error(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    public static final Error ERROR_NOT_IMPLEMENTED = error(HttpStatus.NOT_IMPLEMENTED, HttpStatus.NOT_IMPLEMENTED.getReasonPhrase());

    // Pre-define instance for success
    public static final JSONStringer SUCCESS = success(new Object());

    /**
     * 返回成功数据
     *
     * @param result 数据
     * @param <E>    数据类型
     * @return JSON数据对象
     */
    @SuppressWarnings("deprecation")
    public static <E> JSONStringer success(E result) {
        Objects.requireNonNull(result);
        if (ParserConfig.isPrimitive2(result.getClass()) || result instanceof Iterable || result.getClass().isArray()) {
//            System.out.println("PrimitiveWrapper!!!!!!");
            return new Success<>(new PrimitiveWrapper<>(result));
        } else {
            return new Success<>(result);
        }
    }

    // Inner error
    private static Error error(HttpStatus status, String message) {
        Error error = new Error(status);
        error.setCode(status.value());
        error.setError(message);
        return error;
    }

    // Serialize self to JSON
    public static abstract class JSONStringer {
        @Override
        public String toString() {
            return JSON.toJSONString(this,
                    SerializerFeature.QuoteFieldNames.getMask() |
                            SerializerFeature.WriteEnumUsingName.getMask() |
                            SerializerFeature.SkipTransientField.getMask() |
                            SerializerFeature.DisableCircularReferenceDetect.getMask());
        }

        /**
         * 封装为 HttpEntity 对象
         *
         * @return HttpEntity 对象
         */
        public abstract HttpEntity<String> toHttpEntity();
    }

    // Success definition
    public static class Success<E> extends JSONStringer {
        @JSONField(unwrapped = true)
        private final int code = 200;
        private final String message = "success";
        private final E data;

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        private Success(E data) {
            this.data = data;
        }

        public E getData() {
            return data;
        }

        @Override
        public HttpEntity<String> toHttpEntity() {
            return ResponseEntity.
                    status(HttpStatus.OK).
                    contentType(MediaType.APPLICATION_JSON).
                    body(toString());
        }
    }

    // Error definition
    public static class Error extends JSONStringer {
        /**
         * Result error status
         */
        private final HttpStatus status;

        /**
         * Result error code
         */
        @JSONField(ordinal = 1)
        private int code;

        /**
         * Error message
         */
        @JSONField(ordinal = 2)
        private String error;

        private Error(HttpStatus status) {
            this.status = status;
        }

        public int getCode() {
            return code;
        }

        private void setCode(int code) {
            this.code = code;
        }

        public String getError() {
            return error;
        }

        private void setError(String error) {
            this.error = error;
        }

        /**
         * Error timestamp
         */
        @JSONField(ordinal = 4)
        public String getTimestamp() {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            return simpleDateFormat.format(new Date());
        }

        /**
         * 封装错误数据
         *
         * @param message 错误详细信息
         * @return 派生出的新JSON数据对象
         */
        public Error error(String message) {
            Error error = new Error(this.status);
            error.setCode(this.code);
            error.setError(message);
            return error;
        }

        /**
         * 封装错误数据
         *
         * @param code    错误代码
         * @param message 错误详细信息
         * @return 派生出的新JSON数据对象
         */
        public Error error(int code, String message) {
            Error error = new Error(this.status);
            error.setCode(code);
            error.setError(message);
            return error;
        }

        @Override
        public HttpEntity<String> toHttpEntity() {
            return ResponseEntity.status(status).
                    contentType(MediaType.APPLICATION_JSON).
                    body(toString());
        }
    }

    // Wrap primitive type
    private static class PrimitiveWrapper<T> {
        private final T data;

        public PrimitiveWrapper(T data) {
            this.data = data;
        }

        public T getData() {
            return data;
        }
    }
}