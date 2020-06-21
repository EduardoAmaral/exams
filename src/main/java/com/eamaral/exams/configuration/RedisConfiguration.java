package com.eamaral.exams.configuration;

import com.eamaral.exams.message.application.redis.RedisMessageListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisConfiguration {

    private final RedisMessageListener listener;

    private final String host;

    private final String password;

    private final Integer port;


    public RedisConfiguration(RedisMessageListener listener,
                              @Value("${spring.redis.host}") String host,
                              @Value("${spring.redis.password}") String password,
                              @Value("${spring.redis.port:6379}") Integer port) {
        this.host = host;
        this.password = password;
        this.port = port;
        this.listener = listener;
    }

    @Bean
    public JedisConnectionFactory connectionFactory() {
        RedisStandaloneConfiguration redis = new RedisStandaloneConfiguration(host, port);
        redis.setPassword(password);
        return new JedisConnectionFactory(redis);
    }

    @Bean
    public StringRedisTemplate template() {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(connectionFactory());
        return template;
    }

    @Bean
    public RedisMessageListenerContainer listenerContainer() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory());

        container.addMessageListener(
                new MessageListenerAdapter(listener, "onQuestionCommentMessage"),
                new PatternTopic("question.comments"));

        return container;
    }
}
