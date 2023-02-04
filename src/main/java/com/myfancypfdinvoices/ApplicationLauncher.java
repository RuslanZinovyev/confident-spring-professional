package com.myfancypfdinvoices;

import com.myfancypfdinvoices.context.ApplicationConfiguration;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;

public class ApplicationLauncher {
    public static void main(String[] args) throws LifecycleException {

        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);
        tomcat.getConnector();

        Context context = tomcat.addContext("", null);

        WebApplicationContext applicationContext = createApplicationContext(context.getServletContext());
        DispatcherServlet dispatcherServlet = new DispatcherServlet(applicationContext);
        Wrapper servlet = Tomcat.addServlet(context, "dispatcherServlet", dispatcherServlet);
        servlet.setLoadOnStartup(1);
        servlet.addMapping("/*");

        tomcat.start();
    }

    public static WebApplicationContext createApplicationContext(ServletContext servletContext) {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(ApplicationConfiguration.class);
        context.setServletContext(servletContext);
        context.refresh();
        context.registerShutdownHook();

        return context;
    }
}
