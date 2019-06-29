package com.sjl.health.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

@Configuration
public class RedisConfig {
	
	//springBoot自动帮我们在容器中生成了一个RedisTemplate和一个StringRedisTemplate,但是这个RedisTemplate的泛型是<object,object>
	@SuppressWarnings("all")
	//写代码不方便，需要写转换后的代码，我哦么需要一个泛型为<Stirng,object>形式的RedisTemplate,并且这个RedisTemplate没有设置数据存在Redis时，key
	//以及value的序列化方式
	@Bean
	public RedisTemplate<String,Object>redisTemplate(RedisConnectionFactory factory){
		
		RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();	
		template.setConnectionFactory(factory);
		Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
		ObjectMapper om = new ObjectMapper();
		om.setVisibility(PropertyAccessor.ALL,JsonAutoDetect.Visibility.ANY);
		om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		jackson2JsonRedisSerializer.setObjectMapper(om);
		StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
		// key采用String的序列化方式
		template.setKeySerializer(stringRedisSerializer);
		// hash的key也采用String的序列化方式
		template.setHashKeySerializer(stringRedisSerializer);
		// value序列化方式采用jackson
		template.setValueSerializer(jackson2JsonRedisSerializer);
		// hash的value序列化方式采用jackson
		template.setHashValueSerializer(jackson2JsonRedisSerializer);
		template.afterPropertiesSet();
		return template;
		
	}

}
