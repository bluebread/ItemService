package lab2.itemService.annotation;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpMethod;
import org.springframework.util.Assert;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.annotation.AbstractNamedValueMethodArgumentResolver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

public class RequestJsonBodyParamResolver extends AbstractNamedValueMethodArgumentResolver {
    private static final String CACHE_KEY = RequestJsonBodyParamResolver.class.getName() + "_JSON";

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(RequestJsonBodyParam.class) != null;
    }

    @Override
    protected NamedValueInfo createNamedValueInfo(MethodParameter parameter) {
        RequestJsonBodyParam ann = parameter.getParameterAnnotation(RequestJsonBodyParam.class);
        Assert.state(ann != null, "No RequestJsonBodyParam annotation");
        return new NamedValueInfo(ann.value(), ann.required(), ann.defaultValue());
    }

    @Override
    protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest request) throws Exception {
        System.out.println(name);
        ServletWebRequest webRequest = (ServletWebRequest) request;
//        if (webRequest.getHttpMethod() != HttpMethod.POST &&
//                webRequest.getHttpMethod() != HttpMethod.PUT) {
//            throw new ServletException("SimpleJsonBodyParam only use on POST and PUT");
//        }
        if (webRequest.getHttpMethod() == HttpMethod.GET) {
            throw new ServletException("SimpleJsonBodyParam only use on POST and PUT");
        }

        JSONObject jsonObject = (JSONObject) webRequest.getAttribute(CACHE_KEY, ServletWebRequest.SCOPE_REQUEST);
        if (Objects.isNull(jsonObject)) {
            try {
                jsonObject = JSON.parseObject(webRequest.getNativeRequest(HttpServletRequest.class).getInputStream(), JSONObject.class);
                System.out.println(jsonObject);
            } catch (JSONException e) {
                throw new ServletException("Request body is invalid JSON format");
            }
            webRequest.setAttribute(CACHE_KEY, jsonObject, ServletWebRequest.SCOPE_REQUEST);
        }

        return jsonObject.get(name);
    }
}
