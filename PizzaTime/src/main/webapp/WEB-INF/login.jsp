<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login</title>
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <style>
        body { font-family: Arial, sans-serif; }
        .container { margin-top: 20px; }
        .form-section { margin-bottom: 20px; }
        .form-section h1 { margin-bottom: 20px; }
        .form-section .form-group { margin-bottom: 15px; }
        .form-section .form-control { border-radius: 4px; }
        .form-section .btn-primary { border-radius: 4px; }
        .error-message { color: #d9534f; }
        .top-right-link {
            position: absolute;
            top: 10px;
            right: 10px;
        }
    </style>
</head>
<body>
    <div class="container">
        <!-- Registration link at the top right -->
        <div class="top-right-link">
            <p><a href="/register">Don't have an account? Register here</a></p>
        </div>

        <div class="form-section">
            <h1>Login</h1>
            <p class="error-message"><c:out value="${login_error}" /></p>
            <form method="post" action="/login" id="login-form">
                <div class="form-group">
                    <label for="email">Email:</label>
                    <input type="email" id="email" name="email" class="form-control"/>
                </div>
                <div class="form-group">
                    <label for="password">Password:</label>
                    <input type="password" id="password" name="password" class="form-control"/>
                </div>
                <button type="submit" class="btn btn-primary">Login!</button>
            </form>
        </div>
    </div>

    <!-- Bootstrap JS and dependencies -->
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.2/dist/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
