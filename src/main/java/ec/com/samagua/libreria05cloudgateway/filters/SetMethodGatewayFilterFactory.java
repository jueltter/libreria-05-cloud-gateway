package ec.com.samagua.libreria05cloudgateway.filters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Component
@Slf4j
public class SetMethodGatewayFilterFactory extends AbstractGatewayFilterFactory<SetMethodGatewayFilterFactory.Config> {

    public enum Operation {SEARCH, GET, CREATE, UPDATE, PARTIAL_UPDATE, DELETE}

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public GatewayFilter apply(SetMethodGatewayFilterFactory.Config config) {
        return (exchange, chain) -> {
            final ServerHttpRequest request = exchange.getRequest();
            ServerHttpRequest mutatedRequest = request;

            Entidad entidad;
            Map<String, String> requestBody;

            switch (config.getOperation()) {
                case SEARCH:
                    requestBody = getRequestBodyAsMap(exchange.getAttributes().get(ServerWebExchangeUtils.CACHED_REQUEST_BODY_ATTR));
                    MultiValueMap<String, String> queryParameters = new LinkedMultiValueMap<>();
                    requestBody.forEach(queryParameters::add);
                    URI newURI = UriComponentsBuilder.fromUri(request.getURI()).replaceQueryParams(queryParameters).build().toUri();
                    mutatedRequest = mutatedRequest.mutate().method(HttpMethod.GET).uri(newURI).build();
                    break;
                case GET:
                    entidad = getRequestBodyAsEntidad(exchange.getAttributes().get(ServerWebExchangeUtils.CACHED_REQUEST_BODY_ATTR));
                    mutatedRequest = mutatedRequest.mutate().method(HttpMethod.GET).path(request.getPath().value() + "/" + entidad.getId()).build();
                    break;
                case CREATE:
                    // do nothing
                    break;
                case UPDATE:
                    entidad = getRequestBodyAsEntidad(exchange.getAttributes().get(ServerWebExchangeUtils.CACHED_REQUEST_BODY_ATTR));
                    mutatedRequest = mutatedRequest.mutate().method(HttpMethod.PUT).path(request.getPath().value() + "/" + entidad.getId()).build();
                    break;
                case PARTIAL_UPDATE:
                    entidad = getRequestBodyAsEntidad(exchange.getAttributes().get(ServerWebExchangeUtils.CACHED_REQUEST_BODY_ATTR));
                    mutatedRequest = mutatedRequest.mutate().method(HttpMethod.PATCH).path(request.getPath().value() + "/" + entidad.getId()).build();
                    break;
                case DELETE:
                    entidad = getRequestBodyAsEntidad(exchange.getAttributes().get(ServerWebExchangeUtils.CACHED_REQUEST_BODY_ATTR));
                    mutatedRequest = mutatedRequest.mutate().method(HttpMethod.DELETE).path(request.getPath().value() + "/" + entidad.getId()).build();
                    break;
            }
            log.info("{} {}", mutatedRequest.getMethod(), mutatedRequest.getPath());
            return chain.filter(exchange.mutate().request(mutatedRequest).build());
        };
    }

    private Map<String, String> getRequestBodyAsMap(Object requestBody) {
        Optional<String> json = Stream.ofNullable(requestBody).map(obj -> (String) obj).filter(obj -> !obj.trim().isEmpty()).findFirst();

        return json.map(obj -> {
            try {
                return objectMapper.readValue(obj, new TypeReference<>() {
                });
            } catch (JsonProcessingException ignored) {
                return Collections.checkedMap(Collections.emptyMap(), String.class, String.class);
            }
        }).orElse(Collections.emptyMap());
    }

    private Entidad getRequestBodyAsEntidad(Object requestBody) {
        Optional<String> json = Stream.ofNullable(requestBody).map(obj -> (String) obj).filter(obj -> !obj.trim().isEmpty()).findFirst();

        return json.map(obj -> {
            try {
                return objectMapper.readValue(obj, Entidad.class);
            } catch (JsonProcessingException ignored) {
                return new Entidad();
            }
        }).orElse(new Entidad());
    }


    @Data
    public static class Config {
        private Operation operation;

        public static Config getInstance(Operation operation) {
            Config config = new Config();
            config.setOperation(operation);
            return config;
        }

    }
}
