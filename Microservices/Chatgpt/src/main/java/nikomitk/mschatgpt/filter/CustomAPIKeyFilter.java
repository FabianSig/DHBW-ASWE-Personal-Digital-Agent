package nikomitk.mschatgpt.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Custom filter to check the API key
 */
public class CustomAPIKeyFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        // Conversion to HttpServletRequest to access the header
        if(servletRequest instanceof HttpServletRequest httpServletRequest) {

            String ourApiKey = httpServletRequest.getHeader("Authorization");

            if(ourApiKey == null || !ourApiKey.equals(System.getenv("OUR_API_KEY"))) {
                ((HttpServletResponse) servletResponse).sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            filterChain.doFilter(servletRequest, servletResponse);

        } else {
            throw new ServletException("Invalid request");
        }

    }

}
