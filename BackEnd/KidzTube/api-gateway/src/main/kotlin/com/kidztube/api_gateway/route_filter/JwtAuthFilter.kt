package com.kidztube.api_gateway.route_filter

import com.kidztube.api_gateway.utils.JwtConfig
import io.jsonwebtoken.Jwts
import org.apache.http.HttpHeaders
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
@ConfigurationProperties(prefix = "jwt")
class JwtAuthFilter(
    private val jwtConfig: JwtConfig
) : GlobalFilter{

    override fun filter(
        exchange: ServerWebExchange?,
        chain: GatewayFilterChain?
    ): Mono<Void?>? {
        val path = exchange?.request?.path.toString()

        val publicPaths = listOf(
            "/login",
            "/register"
        )

        // Skip token validation for public endpoints
        if (publicPaths.any { path.startsWith(it) }) {
            return chain?.filter(exchange)
        }
        if (path.startsWith("/content/video/save")) {
            val authHeader = exchange?.request?.headers?.getFirst(HttpHeaders.AUTHORIZATION)
            if (authHeader == null || !authHeader.startsWith("Bearer ")){
                exchange?.response?.statusCode = HttpStatus.UNAUTHORIZED
                return exchange?.response?.setComplete()
            }
            val token = authHeader.removePrefix("Bearer ")
            try {
                val claims = Jwts.parserBuilder()
                    .setSigningKey(jwtConfig.secret.toByteArray())
                    .build()
                    .parseClaimsJws(token)
                    .body
            }catch (e: Exception){
                exchange.response.statusCode = HttpStatus.UNAUTHORIZED
                return exchange.response.setComplete()
            }
            return chain?.filter(exchange)
        }
        return null
    }


}