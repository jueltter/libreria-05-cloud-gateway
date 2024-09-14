package ec.com.samagua.libreria05cloudgateway;

import ec.com.samagua.libreria05cloudgateway.filters.SetMethodGatewayFilterFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;

@SpringBootApplication
@EnableDiscoveryClient
public class Libreria05CloudGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(Libreria05CloudGatewayApplication.class, args);
    }

    @Bean
    public RouteLocator buscadorRoutes(RouteLocatorBuilder builder, SetMethodGatewayFilterFactory filter) {
        final String SERVICE_NAME = "libreria-03-microservicio-buscador";
        final String OLD_BASE_PATH = "/libreria-03-microservicio-buscador/libros";
        final String NEW_BASE_PATH = "/libros";

        return builder.routes()
                .route(OLD_BASE_PATH + "/search", r -> r
                        .path(OLD_BASE_PATH + "/search")
                        .and()
                        .method(HttpMethod.POST)
                        .filters(f -> f.cacheRequestBody(String.class)
                                .setPath(NEW_BASE_PATH)
                                .filter(filter.apply(SetMethodGatewayFilterFactory.Config.getInstance(SetMethodGatewayFilterFactory.Operation.SEARCH))))
                        .uri("lb://" + SERVICE_NAME))
                .route(OLD_BASE_PATH + "/create", r -> r
                        .path(OLD_BASE_PATH + "/create")
                        .and()
                        .method(HttpMethod.POST)
                        .filters(f -> f.setPath(NEW_BASE_PATH)
                                .filter(filter.apply(SetMethodGatewayFilterFactory.Config.getInstance(SetMethodGatewayFilterFactory.Operation.CREATE))))
                        .uri("lb://" + SERVICE_NAME))
                .route(OLD_BASE_PATH + "/get", r -> r
                        .path(OLD_BASE_PATH + "/get")
                        .and()
                        .method(HttpMethod.POST)
                        .filters(f -> f.cacheRequestBody(String.class)
                                .setPath(NEW_BASE_PATH)
                                .filter(filter.apply(SetMethodGatewayFilterFactory.Config.getInstance(SetMethodGatewayFilterFactory.Operation.GET))))
                        .uri("lb://" + SERVICE_NAME))
                .route(OLD_BASE_PATH + "/update", r -> r
                        .path(OLD_BASE_PATH + "/update")
                        .and()
                        .method(HttpMethod.POST)
                        .filters(f -> f.cacheRequestBody(String.class)
                                .setPath(NEW_BASE_PATH)
                                .filter(filter.apply(SetMethodGatewayFilterFactory.Config.getInstance(SetMethodGatewayFilterFactory.Operation.UPDATE))))
                        .uri("lb://" + SERVICE_NAME))
                .route(OLD_BASE_PATH + "/partial-update", r -> r
                        .path(OLD_BASE_PATH + "/partial-update")
                        .and()
                        .method(HttpMethod.POST)
                        .filters(f -> f.cacheRequestBody(String.class)
                                .setPath(NEW_BASE_PATH)
                                .filter(filter.apply(SetMethodGatewayFilterFactory.Config.getInstance(SetMethodGatewayFilterFactory.Operation.PARTIAL_UPDATE))))
                        .uri("lb://" + SERVICE_NAME))
                .route(OLD_BASE_PATH + "/delete", r -> r
                        .path(OLD_BASE_PATH + "/delete")
                        .and()
                        .method(HttpMethod.POST)
                        .filters(f -> f.cacheRequestBody(String.class)
                                .setPath(NEW_BASE_PATH)
                                .filter(filter.apply(SetMethodGatewayFilterFactory.Config.getInstance(SetMethodGatewayFilterFactory.Operation.DELETE))))
                        .uri("lb://" + SERVICE_NAME))
                .build();
    }

    @Bean
    public RouteLocator operadorRoutes(RouteLocatorBuilder builder, SetMethodGatewayFilterFactory filter) {
        final String SERVICE_NAME = "libreria-04-microservicio-operador";
        final String OLD_BASE_PATH = "/libreria-04-microservicio-operador/alquileres";
        final String NEW_BASE_PATH = "/alquileres";

        return builder.routes()
                .route(OLD_BASE_PATH + "/search", r -> r
                        .path(OLD_BASE_PATH + "/search")
                        .and()
                        .method(HttpMethod.POST)
                        .filters(f -> f.cacheRequestBody(String.class)
                                .setPath(NEW_BASE_PATH)
                                .filter(filter.apply(SetMethodGatewayFilterFactory.Config.getInstance(SetMethodGatewayFilterFactory.Operation.SEARCH))))
                        .uri("lb://" + SERVICE_NAME))
                .route(OLD_BASE_PATH + "/create", r -> r
                        .path(OLD_BASE_PATH + "/create")
                        .and()
                        .method(HttpMethod.POST)
                        .filters(f -> f.setPath(NEW_BASE_PATH)
                                .filter(filter.apply(SetMethodGatewayFilterFactory.Config.getInstance(SetMethodGatewayFilterFactory.Operation.CREATE))))
                        .uri("lb://" + SERVICE_NAME))
                .route(OLD_BASE_PATH + "/get", r -> r
                        .path(OLD_BASE_PATH + "/get")
                        .and()
                        .method(HttpMethod.POST)
                        .filters(f -> f.cacheRequestBody(String.class)
                                .setPath(NEW_BASE_PATH)
                                .filter(filter.apply(SetMethodGatewayFilterFactory.Config.getInstance(SetMethodGatewayFilterFactory.Operation.GET))))
                        .uri("lb://" + SERVICE_NAME))
                .route(OLD_BASE_PATH + "/partial-update", r -> r
                        .path(OLD_BASE_PATH + "/partial-update")
                        .and()
                        .method(HttpMethod.POST)
                        .filters(f -> f.cacheRequestBody(String.class)
                                .setPath(NEW_BASE_PATH)
                                .filter(filter.apply(SetMethodGatewayFilterFactory.Config.getInstance(SetMethodGatewayFilterFactory.Operation.PARTIAL_UPDATE))))
                        .uri("lb://" + SERVICE_NAME))
                .build();
    }

}
