package com.ksoot.problem.demo.config;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.lang.Nullable;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Configuration of web application with Servlet 3.0 APIs.
 *
 * @author Rajveer Singh
 */
@Log4j2
@Configuration
@AutoConfigureAfter(value = {CommonConfiguration.class})
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
class WebConfigurer implements ServletContextInitializer, WebMvcConfigurer {

  private final Environment env;

  private final ActuatorEndpointProperties actuatorEndpointProperties;

  private List<HandlerMethodArgumentResolver> argumentResolvers;

  private List<HandlerInterceptor> interceptors;

  WebConfigurer(
      final Environment env,
      @Nullable final ActuatorEndpointProperties actuatorEndpointProperties,
      @Nullable final List<HandlerMethodArgumentResolver> argumentResolvers,
      @Nullable final List<HandlerInterceptor> interceptors) {
    this.env = env;
    this.actuatorEndpointProperties = actuatorEndpointProperties;
    this.argumentResolvers = argumentResolvers;
    this.interceptors = interceptors;
  }

  @Override
  public void onStartup(final ServletContext servletContext) throws ServletException {
    if (this.env.getActiveProfiles().length != 0) {
      log.info(
          "Web application configuration, using profiles: {}",
          (Object[]) this.env.getActiveProfiles());
    }

    // App initialization and logging startup info
    AppInitializer.initialize(this.env, WebApplicationType.SERVLET);

    log.info("Web application fully configured");
  }

  @Override
  public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> argumentResolvers) {
    if (!CollectionUtils.isEmpty(this.argumentResolvers)) {
      this.argumentResolvers.forEach(argumentResolvers::add);
    }
    // Add any custom method argument resolvers
  }

  @Override
  public void addInterceptors(final InterceptorRegistry registry) {
    if (CollectionUtils.isNotEmpty(this.interceptors)) {
      for (HandlerInterceptor interceptor : this.interceptors) {
        registry
            .addInterceptor(interceptor)
            .addPathPatterns("/**")
            .excludePathPatterns(
                this.actuatorEndpointProperties != null
                    ? this.actuatorEndpointProperties.getPaths()
                    : new String[0]);
      }
    }
  }
}
