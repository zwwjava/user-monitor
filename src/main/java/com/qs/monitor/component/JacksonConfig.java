package com.qs.monitor.component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.stream.Stream;

/**
 * @author zhaww
 * @date 2020/5/6
 * @Description .
 */
@Configuration
public class JacksonConfig {

    private Jackson2ObjectMapperBuilder builder;

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customizer() {
        return builder -> {
            //builder.serializationInclusion(JsonInclude.Include.NON_NULL);
            builder.featuresToDisable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
            builder.serializerByType(MultipartFile.class, new JsonSerializer<MultipartFile>() {
                @Override
                public void serialize(MultipartFile value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                    gen.writeStartObject();
                    gen.writeStringField("originalFilename", value.getOriginalFilename());
                    gen.writeNumberField("size", value.getSize());
                    gen.writeStringField("contentType", value.getContentType());
                    gen.writeEndObject();
                }
            });
            Stream.of(
                    ServletRequest.class,
                    ServletResponse.class,
                    byte[].class
                    //Workbook.class
            ).forEach(clazz -> builder.serializerByType(clazz, new JsonSerializer<Object>() {
                @Override
                public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                    gen.writeString("[...]");
                }
            }));
            this.builder = builder;
        };
    }

    public ObjectMapper createObjectMapper() {
        return builder.build();
    }

}
