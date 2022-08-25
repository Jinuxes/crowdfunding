package com.starrysea.crowd.mvc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration  // 将当前类标记为配置类
@EnableWebSecurity  // 启用Web环境下权限控制功能
// 启用全局方法权限控制功能，并设置prePostEnabled=true。保证@PreAuthority、@PostAuthority、@PreFilter、@PostFilter生效
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class WebAppSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;  //Autowired先进行ByType注入，所以即使不是crowdUserDetailsService名字也能把这个装配进去。

    @Bean
    public BCryptPasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity security) throws Exception {
        security
                .authorizeRequests()  // 对请求进行授权
                .antMatchers("/admin/to/login/page.html")  //针对登录页进行设置
                .permitAll()  //无条件访问
                .antMatchers("/bootstrap/**")  //针对静态资源进行设置
                .permitAll()  //无条件访问
                .antMatchers("/crowd/**")
                .permitAll()
                .antMatchers("/css/**")
                .permitAll()
                .antMatchers("/fonts/**")
                .permitAll()
                .antMatchers("/img/**")
                .permitAll()
                .antMatchers("/jquery/**")
                .permitAll()
                .antMatchers("/layer/**")
                .permitAll()
                .antMatchers("/script/**")
                .permitAll()
                .antMatchers("/ztree/**")
                .permitAll()
                .antMatchers("/admin/get/page.html")  // 针对分页显示Admin数据设定访问权限
                // .hasRole("经理")  // 要求具备经理角色
                .access("hasRole('经理') OR hasRole('部长')")
                .anyRequest()  // 其它任意的请求
                .authenticated()  // 需要认证后才能访问
                .and()
                .exceptionHandling()
                .accessDeniedHandler(new AccessDeniedHandler() {
                    @Override
                    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
                        httpServletRequest.setAttribute("exception",e);
                        httpServletRequest.getRequestDispatcher("/WEB-INF/system-error.jsp").forward(httpServletRequest,httpServletResponse);
                    }
                })
                .and()
                .csrf()  //防跨站请求伪造功能
                .disable()  // 禁用
                .formLogin()  // 开启表单登录的功能
                .loginPage("/admin/to/login/page.html")  // 指定登录页面
                .loginProcessingUrl("/security/do/login.html")  // 指定处理登录请求的地址
                .defaultSuccessUrl("/admin/to/main/page.html",true)  //指定登录成功后前往的地址
                .usernameParameter("loginAcct")  // 账号的请求参数名
                .passwordParameter("userPswd")  // 密码的请求参数名
                .and()
                .logout()  // 开启退出登录功能
                .logoutUrl("/security/do/logout.html")  // 指定退出登录地址
                .logoutSuccessUrl("/admin/to/login/page.html")  // 指定退出成功以后前往的地址
                ;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        // 临时使用内存版登录的模式测试代码
        // builder.inMemoryAuthentication().withUser("tom").password("123456789").roles("ADMIN");

        // 正式功能中使用基于数据库的认证
        builder
                .userDetailsService(userDetailsService)
                .passwordEncoder(getPasswordEncoder())
                ;
    }
}
