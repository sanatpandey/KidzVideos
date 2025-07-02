import io.netty.handler.codec.http.HttpObjectAggregator
import org.springframework.boot.web.embedded.netty.NettyServerCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class NettyConfig {

    @Bean
    fun nettyServerCustomizer(): NettyServerCustomizer {
        return NettyServerCustomizer { httpServer ->
            httpServer
                .httpRequestDecoder { decoderSpec ->
                    decoderSpec
                        .maxInitialLineLength(16384)
                        .maxHeaderSize(16384)
                        .maxChunkSize(10 * 1024 * 1024)
                }
                .doOnConnection { conn ->
                    conn.addHandlerLast("aggregator", HttpObjectAggregator(100 * 1024 * 1024))
                }
        }
    }
}
