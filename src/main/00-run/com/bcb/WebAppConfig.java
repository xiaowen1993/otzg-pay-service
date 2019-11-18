package com.bcb;

import com.bcb.log.util.LogUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 系统访问控制类
 * @author G/2018/3/29 15:17
 */
@Configuration
//@Component
@ConfigurationProperties(prefix = "server")
public class WebAppConfig implements WebMvcConfigurer {

	//设置服务器访问文件路径
	public void setServUrl(String path) {
		LogUtil.setServUrl(path);
	}

	//设置服务器保存文件路径
	public void setFileSavePath(String path) {
		LogUtil.setFileSavePath(path);
	}

//	//注入拦截器
//	@Bean
//	public MemberLogInterceptor memberLogInterceptor(){
//		return new MemberLogInterceptor();
//	}
//
//	//注入拦截器
//	@Bean
//	public AdminLogInterceptor adminLogInterceptor(){
//		return new AdminLogInterceptor();
//	}
//
//	/**
//	 * 添加拦截器配置
//	 */
//	public void addInterceptors(InterceptorRegistry registry) {
//		//添加前端接口拦截器
//		registry.addInterceptor(memberLogInterceptor())
//				.addPathPatterns("/member/**")
//				.addPathPatterns("/message/**")
//				.addPathPatterns("/unit/**")
//				.addPathPatterns("/address/**")
//				.addPathPatterns("/order/**")
//				.addPathPatterns("/shoppingCart/**")
//				.addPathPatterns("/pay/**");
//
//		registry.addInterceptor(adminLogInterceptor())
//				.addPathPatterns("/admin/**")
//				.addPathPatterns("/approval/**");
//	}


//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        //第一个方法设置访问路径前缀，第二个方法设置资源路径
//        registry.addResourceHandler("/template").addResourceLocations("classpath:/template/");
//        super.addResourceHandlers(registry);
//    }



//	@Autowired
//	private RestTemplateBuilder builder;
//
//	@Bean
//	public RestTemplate restTemplate() {
//		return builder.build();
//	}

}
