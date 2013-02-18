package com.migrate.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

@Configuration
public class WebConfig extends WebMvcConfigurationSupport {

	@Override
	@Bean
	public FormattingConversionService mvcConversionService() {
		return super.mvcConversionService();
	}

	@Override
	@Bean
	public Validator mvcValidator() {
		return super.mvcValidator();
	}

	@Override
	@Bean
	public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
		return super.requestMappingHandlerAdapter();
	}

	@Override
	@Bean
	public RequestMappingHandlerMapping requestMappingHandlerMapping() {
		RequestMappingHandlerMapping handlerMapping = super
				.requestMappingHandlerMapping();
		handlerMapping.setUseSuffixPatternMatch(false);
		handlerMapping.setUseTrailingSlashMatch(true);
		return handlerMapping;

	}
}
