<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng nhập | TaskFlow</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="auth-wrapper">
    <div class="auth-card panel">
        <div class="auth-header">
            <span class="brand-pill">TaskFlow</span>
            <h1>Đăng nhập</h1>
            <p>Quản lý công việc cá nhân bằng tài khoản của bạn.</p>
        </div>

        <%
            String error = (String) request.getAttribute("error");
            String success = (String) request.getAttribute("success");
            String username = (String) request.getAttribute("username");
            if (username == null) username = "";

            if (error != null) {
        %>
            <div class="alert alert-danger"><%= error %></div>
        <%
            }
            if (success != null) {
        %>
            <div class="alert alert-success"><%= success %></div>
        <%
            }
        %>

        <form action="${pageContext.request.contextPath}/login" method="post">
            <div class="form-group">
                <label class="form-label" for="username">Tên đăng nhập</label>
                <input class="form-control" type="text" id="username" name="username"
                       value="<%= username %>" required autocomplete="off">
            </div>

            <div class="form-group">
                <label class="form-label" for="password">Mật khẩu</label>
                <input class="form-control" type="password" id="password" name="password" required>
            </div>

            <button class="btn btn-primary" type="submit">Đăng nhập</button>
        </form>

        <div class="auth-footer">
            Chưa có tài khoản?
            <a href="${pageContext.request.contextPath}/register.jsp">Đăng ký</a>
        </div>
    </div>
</div>
</body>
</html>
