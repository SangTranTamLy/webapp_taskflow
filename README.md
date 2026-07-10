# TaskFlow

Ứng dụng web Java Servlet/JSP để quản lý công việc cá nhân.

## Chức năng

- Đăng ký tài khoản.
- Đăng nhập bằng mật khẩu băm SHA-256.
- Đăng xuất và bảo vệ phiên đăng nhập bằng `AuthFilter`.
- Thêm, sửa, xóa và xem danh sách công việc theo từng người dùng.

## Cấu hình database

Project đang dùng database cũ:

```text
Database: WebApp_db
User: root
Password: 123456
```

Nếu MySQL của bạn khác thông tin trên, sửa trong:

```text
src/main/java/context/DBContext.java
```

## Chạy project

```bash
mvn clean package
```

Deploy file:

```text
target/taskflow.war
```

lên Tomcat.
