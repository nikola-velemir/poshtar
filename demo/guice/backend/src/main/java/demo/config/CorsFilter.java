package demo.config;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;

@Provider // This tells Jersey to automatically register this class
public class CorsFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext,
                       ContainerResponseContext responseContext) throws IOException {

        // Allow your frontend origin (change * to your specific URL in production)
        responseContext.getHeaders().add("Access-Control-Allow-Origin", "*");

        // Allow specific headers required by your app
        responseContext.getHeaders().add("Access-Control-Allow-Headers",
                "origin, content-type, accept, authorization, x-requested-with");

        responseContext.getHeaders().add("Access-Control-Allow-Credentials", "true");

        // Allow all standard REST methods
        responseContext.getHeaders().add("Access-Control-Allow-Methods",
                "GET, POST, PUT, DELETE, OPTIONS, HEAD");

        // Control how long the results of a preflight request can be cached (12 hours)
        responseContext.getHeaders().add("Access-Control-Max-Age", "1209600");
    }
}