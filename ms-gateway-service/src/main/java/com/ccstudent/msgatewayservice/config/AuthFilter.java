package com.ccstudent.msgatewayservice.config;

import com.ccstudent.msgatewayservice.dto.TokenDto;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    private final WebClient.Builder webClient;

    public AuthFilter(WebClient.Builder webClient) {
        super(Config.class);
        this.webClient = webClient;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, HttpStatus.BAD_REQUEST);
            }

            String tokenHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
                return onError(exchange, HttpStatus.BAD_REQUEST);
            }

            String token = tokenHeader.substring(7);

            return webClient.build()
                    .post()
                    .uri("http://ms-auth-service/auth/validate?token=" + token)
                    .retrieve()
                    .bodyToMono(TokenDto.class)
                    .map(t -> {
                        t.getToken(); // opcional, puedes eliminar esta línea si no se usa
                        return exchange;
                    })
                    .flatMap(chain::filter);
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        return response.setComplete();
    }

    public static class Config {
        // Puedes agregar configuración personalizada aquí si la necesitas
    }
}
