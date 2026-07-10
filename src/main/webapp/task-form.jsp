<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ page import="model.User" %>
<%@ page import="model.Task" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Công việc | TaskFlow</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<%
    User currentUser = (User) session.getAttribute("user");
    if (currentUser == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    Task task = (Task) request.getAttribute("task");
    boolean edit = task != null && task.getId() > 0;
    String formAction = edit ? "update" : "insert";
    String pageTitle = edit ? "Cập nhật công việc" : "Thêm công việc mới";
    String statusValue = task != null && task.getStatus() != null && !task.getStatus().isEmpty()
            ? task.getStatus() : "Chua lam";
    String priorityValue = task != null && task.getPriority() != null && !task.getPriority().isEmpty()
            ? task.getPriority() : "Trung binh";
%>

<header class="header">
    <a class="logo" href="${pageContext.request.contextPath}/task?action=list">TaskFlow</a>
    <div class="user-area">
        <div class="user-text">
            <div class="user-name"><%= currentUser.getFullname() %></div>
            <div class="user-role">Người dùng</div>
        </div>
        <a class="btn btn-secondary btn-sm" href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
    </div>
</header>

<main class="main">
    <div class="form-panel panel">
        <h1><%= pageTitle %></h1>

        <%
            String error = (String) request.getAttribute("error");
            if (error != null) {
        %>
            <div class="alert alert-danger"><%= error %></div>
        <%
            }
        %>

        <form action="${pageContext.request.contextPath}/task?action=<%= formAction %>" method="post">
            <% if (edit) { %>
                <input type="hidden" name="id" value="<%= task.getId() %>">
            <% } %>

            <div class="form-group">
                <label class="form-label" for="title">Tên công việc</label>
                <input class="form-control" type="text" id="title" name="title"
                       value="<%= task != null && task.getTitle() != null ? task.getTitle() : "" %>" required>
            </div>

            <div class="form-group">
                <label class="form-label" for="deadline">Hạn hoàn thành</label>
                <input class="form-control" type="date" id="deadline" name="deadline"
                       value="<%= task != null && task.getDeadline() != null ? task.getDeadline() : "" %>" required>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label class="form-label" for="status">Trạng thái</label>
                    <select class="form-control" id="status" name="status" required>
                        <option value="Chua lam" <%= "Chua lam".equals(statusValue) ? "selected" : "" %>>Chưa làm</option>
                        <option value="Dang lam" <%= "Dang lam".equals(statusValue) ? "selected" : "" %>>Đang làm</option>
                        <option value="Hoan thanh" <%= "Hoan thanh".equals(statusValue) ? "selected" : "" %>>Hoàn thành</option>
                    </select>
                </div>

                <div class="form-group">
                    <label class="form-label" for="priority">Mức ưu tiên</label>
                    <select class="form-control" id="priority" name="priority" required>
                        <option value="Thap" <%= "Thap".equals(priorityValue) ? "selected" : "" %>>Thấp</option>
                        <option value="Trung binh" <%= "Trung binh".equals(priorityValue) ? "selected" : "" %>>Trung bình</option>
                        <option value="Cao" <%= "Cao".equals(priorityValue) ? "selected" : "" %>>Cao</option>
                    </select>
                </div>
            </div>

            <div class="form-group">
                <label class="form-label" for="description">Mô tả</label>
                <textarea class="form-control form-textarea" id="description" name="description"><%= task != null && task.getDescription() != null ? task.getDescription() : "" %></textarea>
            </div>

            <div class="form-actions">
                <button class="btn btn-primary" type="submit">Lưu công việc</button>
                <a class="btn btn-secondary" href="${pageContext.request.contextPath}/task?action=list">Hủy</a>
            </div>
        </form>
    </div>
</main>
</body>
</html>
