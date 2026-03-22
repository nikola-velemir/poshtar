package demo;

import com.google.inject.servlet.GuiceFilter;
import demo.config.ServletConfig;
import jakarta.servlet.DispatcherType;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;

import java.util.EnumSet;

public class App {
    public static void main(String[] args) throws Exception {
        Server server = new Server(5335);

        // 1. Create a ServletContextHandler (This is your "web app" container)
        ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        handler.setContextPath("/");

        // 2. Add the Guice Filter (This intercepts all requests)
        handler.addFilter(GuiceFilter.class, "/*", EnumSet.allOf(DispatcherType.class));

        // 3. Add the Guice Listener (This builds your Injector)
        handler.addEventListener(new ServletConfig());

        // 4. Add the Jersey Servlet (The "Default" servlet for Guice-Servlet apps)
        handler.addServlet(DefaultServlet.class, "/");

        server.setHandler(handler);
        System.out.println("Starting server on http://localhost:5335/api/...");
        server.start();
        server.join();
    }
}