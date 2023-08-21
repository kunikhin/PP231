package org.example.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

// Интерфейс WebMvcConfigurer реализуется тогда, когда мы под себя хотим реализовать Spring MVC.
// В данном случае, мы вместо стандартного шаблонизатора хотим использовать thymeleaf (таймлИф), поэтому мы имплементим этот интерфейс

//Этот класс - замена файлу с описанием ДиспетчерСервлета. Теперь вся конфигурация ДиспетчерСервлета будет в этом классе.
@Configuration
@EnableWebMvc //Перевод - "делать возможным WebMvc", т.е. используется для включения Spring MVC в приложении
@EnableTransactionManagement
@ComponentScan("org.example")
public class WebConfig implements WebMvcConfigurer {

    private final ApplicationContext applicationContext;

    @Autowired // Внедряем applicationContext, как зависимость)
    public WebConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }


    //Ресурс распознавателя шаблонов
    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(applicationContext);
        templateResolver.setPrefix("/WEB-INF/views/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCacheable(true);
        templateResolver.setCharacterEncoding("UTF-8"); //Добавляем, чтобы отображались кириллические символы на web-странице
        return templateResolver;
    }


    //Двигатель шаблонов
    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        templateEngine.setEnableSpringELCompiler(true);
        return templateEngine;
    }


    // Здесь мы задаем шаблонизатор - распознаватель вьюшек. В данном случае - thymeleaf (таймлИф)

    public void configureViewResolvers(ViewResolverRegistry registry) {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine());
        resolver.setCharacterEncoding("UTF-8");//Добавляем, чтобы отображались кириллические символы на web-странице
        resolver.setContentType("text/html; charset=UTF-8");//Добавляем, чтобы отображались кириллические символы на web-странице
        registry.viewResolver(resolver);
    }
}
