package servlet;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String contextPath = req.getContextPath();
        String path = req.getRequestURI().substring(contextPath.length());
        HttpSession session = req.getSession(false);
        boolean loggedIn = session != null && session.getAttribute("user") != null;
        boolean authPage = path.equals("/") || path.equals("/login.jsp") || path.equals("/register.jsp")
                || path.equals("/login") || path.equals("/register");
        boolean staticResource = path.startsWith("/css/");

        if (staticResource) {
            chain.doFilter(request, response);
            return;
        }

        if (loggedIn && authPage) {
            resp.sendRedirect(contextPath + "/task?action=list");
            return;
        }

        if (!loggedIn && !authPage) {
            req.setAttribute("error", "Vui lòng đăng nhập để tiếp tục.");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
            return;
        }

        if (!loggedIn && path.equals("/")) {
            resp.sendRedirect(contextPath + "/login.jsp");
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
