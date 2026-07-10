package servlet;

import dao.UserDAO;
import model.User;
import util.HashUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(request.getContextPath() + "/register.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String fullname = request.getParameter("fullname");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        if (isBlank(fullname) || isBlank(username) || isBlank(password) || isBlank(confirmPassword)) {
            request.setAttribute("error", "Vui lòng nhập đầy đủ thông tin.");
            keepFormData(request, fullname, username);
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Mật khẩu xác nhận không khớp.");
            keepFormData(request, fullname, username);
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        String trimmedUsername = username.trim();
        if (userDAO.usernameExists(trimmedUsername)) {
            request.setAttribute("error", "Tên đăng nhập này đã tồn tại.");
            keepFormData(request, fullname, username);
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        String hashedPassword = HashUtil.sha256(password);
        User user = new User(0, trimmedUsername, hashedPassword, fullname.trim(), "user");

        if (userDAO.register(user)) {
            request.setAttribute("success", "Đăng ký thành công. Hãy đăng nhập bằng tài khoản mới.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Đăng ký không thành công. Vui lòng thử lại.");
            keepFormData(request, fullname, username);
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private void keepFormData(HttpServletRequest request, String fullname, String username) {
        request.setAttribute("fullname", fullname == null ? "" : fullname);
        request.setAttribute("username", username == null ? "" : username);
    }
}
