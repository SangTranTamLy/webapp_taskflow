package servlet;

import dao.TaskDAO;
import model.Task;
import model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebServlet("/task")
public class TaskServlet extends HttpServlet {
    private final TaskDAO taskDAO = new TaskDAO();
    private final List<String> statuses = Arrays.asList("Chua lam", "Dang lam", "Hoan thanh");
    private final List<String> priorities = Arrays.asList("Thap", "Trung binh", "Cao");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "new":
                request.getRequestDispatcher("/task-form.jsp").forward(request, response);
                break;
            case "edit":
                showEditForm(request, response, currentUser);
                break;
            case "delete":
                deleteTask(request, response, currentUser);
                break;
            case "list":
            default:
                listTasks(request, response, currentUser);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        User currentUser = getCurrentUser(request);
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String action = request.getParameter("action");
        if ("insert".equals(action)) {
            insertTask(request, response, currentUser);
        } else if ("update".equals(action)) {
            updateTask(request, response, currentUser);
        } else {
            response.sendRedirect(request.getContextPath() + "/task?action=list");
        }
    }

    private void listTasks(HttpServletRequest request, HttpServletResponse response, User currentUser)
            throws ServletException, IOException {
        request.setAttribute("taskList", taskDAO.findByUserId(currentUser.getId()));
        request.getRequestDispatcher("/dashboard.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response, User currentUser)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Task task = taskDAO.findByIdAndUserId(id, currentUser.getId());
            if (task == null) {
                response.sendRedirect(request.getContextPath() + "/task?action=list");
                return;
            }
            request.setAttribute("task", task);
            request.getRequestDispatcher("/task-form.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/task?action=list");
        }
    }

    private void insertTask(HttpServletRequest request, HttpServletResponse response, User currentUser)
            throws ServletException, IOException {
        Task task = readTask(request, 0, currentUser.getId());
        if (!isValid(request, task)) {
            request.setAttribute("task", task);
            request.getRequestDispatcher("/task-form.jsp").forward(request, response);
            return;
        }

        if (taskDAO.insert(task)) {
            response.sendRedirect(request.getContextPath() + "/task?action=list");
        } else {
            request.setAttribute("error", "Không thể thêm công việc.");
            request.setAttribute("task", task);
            request.getRequestDispatcher("/task-form.jsp").forward(request, response);
        }
    }

    private void updateTask(HttpServletRequest request, HttpServletResponse response, User currentUser)
            throws ServletException, IOException {
        int id;
        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/task?action=list");
            return;
        }

        Task task = readTask(request, id, currentUser.getId());
        if (!isValid(request, task)) {
            request.setAttribute("task", task);
            request.getRequestDispatcher("/task-form.jsp").forward(request, response);
            return;
        }

        if (taskDAO.update(task)) {
            response.sendRedirect(request.getContextPath() + "/task?action=list");
        } else {
            request.setAttribute("error", "Không thể cập nhật công việc.");
            request.setAttribute("task", task);
            request.getRequestDispatcher("/task-form.jsp").forward(request, response);
        }
    }

    private void deleteTask(HttpServletRequest request, HttpServletResponse response, User currentUser)
            throws IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            taskDAO.delete(id, currentUser.getId());
        } catch (NumberFormatException ignored) {
        }
        response.sendRedirect(request.getContextPath() + "/task?action=list");
    }

    private Task readTask(HttpServletRequest request, int id, int userId) {
        return new Task(
                id,
                userId,
                clean(request.getParameter("title")),
                clean(request.getParameter("description")),
                clean(request.getParameter("deadline")),
                clean(request.getParameter("status")),
                clean(request.getParameter("priority"))
        );
    }

    private boolean isValid(HttpServletRequest request, Task task) {
        if (task.getTitle().isEmpty() || task.getDeadline().isEmpty()
                || task.getStatus().isEmpty() || task.getPriority().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập đầy đủ tên, hạn, trạng thái và mức ưu tiên.");
            return false;
        }

        if (!statuses.contains(task.getStatus())) {
            request.setAttribute("error", "Trạng thái không hợp lệ.");
            return false;
        }

        if (!priorities.contains(task.getPriority())) {
            request.setAttribute("error", "Mức ưu tiên không hợp lệ.");
            return false;
        }

        return true;
    }

    private User getCurrentUser(HttpServletRequest request) {
        return (User) request.getSession().getAttribute("user");
    }

    private String clean(String value) {
        return value == null ? "" : value.trim();
    }
}
