package daon.be.agent.data.source.kis.common;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.lang.reflect.RecordComponent;
import java.util.ArrayList;
import java.util.List;

public final class KisQueryParamMapper {

    private KisQueryParamMapper() {
    }

    public static List<KisQueryParam> toQueryParams(Object request) {
        if (request == null) {
            return List.of();
        }
        if (!request.getClass().isRecord()) {
            throw new IllegalArgumentException("KIS request must be a record: " + request.getClass().getName());
        }

        List<KisQueryParam> queryParams = new ArrayList<>();

        for (RecordComponent component : request.getClass().getRecordComponents()) {
            Object value = readValue(component, request);
            if (value == null) {
                continue;
            }

            JsonProperty jsonProperty = component.getAccessor().getAnnotation(JsonProperty.class);
            String name = jsonProperty == null || jsonProperty.value().isBlank()
                    ? component.getName()
                    : jsonProperty.value();

            queryParams.add(new KisQueryParam(name, String.valueOf(value)));
        }

        return queryParams;
    }

    private static Object readValue(RecordComponent component, Object request) {
        try {
            return component.getAccessor().invoke(request);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("KIS request 값을 읽지 못했습니다: " + component.getName(), e);
        }
    }

    public record KisQueryParam(String name, String value) {
    }
}
