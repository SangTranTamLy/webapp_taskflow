<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ page import="model.User" %>
<%@ page import="model.Task" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Bảng điều khiển | TaskFlow</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<%
    User currentUser = (User) session.getAttribute("user");
    if (currentUser == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
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
    <div class="page-head">
        <div class="page-title">
            <h1>Danh sách công việc</h1>
            <p>Theo dõi công việc, hạn hoàn thành và mức ưu tiên của bạn.</p>
        </div>
        <a class="btn btn-primary btn-sm" href="${pageContext.request.contextPath}/task?action=new">Thêm công việc</a>
    </div>

    <div class="task-grid">
        <%
            List<Task> tasks = (List<Task>) request.getAttribute("taskList");
            if (tasks != null && !tasks.isEmpty()) {
                for (Task task : tasks) {
                    String statusClass = "status-todo";
                    String statusText = "Chưa làm";
                    if ("Dang lam".equals(task.getStatus())) {
                        statusClass = "status-progress";
                        statusText = "Đang làm";
                    } else if ("Hoan thanh".equals(task.getStatus())) {
                        statusClass = "status-done";
                        statusText = "Hoàn thành";
                    }

                    String priorityClass = "priority-medium";
                    String priorityText = "Trung bình";
                    if ("Thap".equals(task.getPriority())) {
                        priorityClass = "priority-low";
                        priorityText = "Thấp";
                    } else if ("Cao".equals(task.getPriority())) {
                        priorityClass = "priority-high";
                        priorityText = "Cao";
                    }
        %>
            <article class="task-card panel">
                <div class="task-meta">
                    <span class="badge <%= statusClass %>"><%= statusText %></span>
                    <span class="badge <%= priorityClass %>"><%= priorityText %></span>
                </div>
                <h2 class="task-title"><%= task.getTitle() %></h2>
                <p class="task-desc">
                    <%= task.getDescription() != null && !task.getDescription().isEmpty()
                            ? task.getDescription()
                            : "Chưa có mô tả cho công việc này." %>
                </p>
                <div class="task-footer">
                    <span class="badge deadline">Hạn: <%= task.getDeadline() %></span>
                    <div class="actions">
                        <a class="btn btn-secondary btn-sm"
                           href="${pageContext.request.contextPath}/task?action=edit&id=<%= task.getId() %>">Sửa</a>
                        <a class="btn btn-danger btn-sm"
                           href="${pageContext.request.contextPath}/task?action=delete&id=<%= task.getId() %>"
                           onclick="return confirm('Bạn có chắc muốn xóa công việc này?');">Xóa</a>
                    </div>
                </div>
            </article>
        <%
                }
            } else {
        %>
            <div class="empty-state panel">
                <h2>Chưa có công việc nào</h2>
                <p>Hãy tạo công việc đầu tiên để bắt đầu quản lý ngay hôm nay.</p>
                <a class="btn btn-primary btn-sm" href="${pageContext.request.contextPath}/task?action=new">Thêm công việc</a>
            </div>
        <%
            }
        %>
    </div>
</main>
</body>
</html>
