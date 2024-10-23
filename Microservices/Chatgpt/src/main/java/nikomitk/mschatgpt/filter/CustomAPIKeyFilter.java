package nikomitk.mschatgpt.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * Custom filter to check the API key
 */
@Slf4j
public class CustomAPIKeyFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        // Conversion to HttpServletRequest to access the header
        if(servletRequest instanceof HttpServletRequest httpServletRequest) {

            httpServletRequest.getHeaderNames().asIterator().forEachRemaining(header -> log.info("Header: {}", httpServletRequest.getHeader(header)));
            log.info("Our api key from env: {}", System.getenv("OUR_API_KEY"));

            if(httpServletRequest.getRequestURL().toString().contains("/swagger") || httpServletRequest.getRequestURL().toString().contains("/api-docs")) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }

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
