<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Checkout</title>
    <!-- Bootstrap CSS -->
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { font-family: Arial, sans-serif; }
        #navbar { margin-bottom: 20px; }
        #navbar a { margin-right: 15px; text-decoration: none; }
        h2 { color: #333; }
        form { margin-top: 20px; }
        .btn-danger { background-color: #dc3545; color: #fff; border: none; }
    </style>
</head>
<body>
    <div class="container">
        <nav class="navbar navbar-expand-lg navbar-light bg-light" id="navbar">
            <a class="navbar-brand" href="/home">Home</a>
            <a class="nav-link" href="/order">Order</a>
            <a class="nav-link" href="/logout">Logout</a>
            <a class="nav-link" href="/account/${userId}">Account</a>
        </nav>

        <h2>Your Order</h2>
        <div class="card">
            <div class="card-body">
                <p><strong>METHOD:</strong> <c:out value="${currentOrder.deliveryMethod}" /></p>
                <p><strong>QTY:</strong> <c:out value="${currentOrder.qty}" /></p>
                <p><strong>Size:</strong> <c:out value="${currentOrder.pizzaSize}" /></p>
                <p><strong>Crust:</strong> <c:out value="${currentOrder.crustType}" /></p>
                <p><strong>Toppings:</strong>
                    <c:forEach var="topping" items="${currentOrder.toppings}">
                        <c:out value="${topping}" />
                        <c:if test="${not empty topping}">, </c:if>
                    </c:forEach>
                </p>
                <p><strong>Price:</strong> $<c:out value="${price}" /></p>

                <form action="/checkout/${currentOrder.id}/delete" method="post">
                    <input type="hidden" name="_method" value="delete">
                    <input type="submit" class="btn btn-danger" value="Start Over">
                </form>
                <form action="/purchase/${currentOrder.id}" method="post">
                    <input type="submit" class="btn btn-primary" value="Purchase">
                </form>
            </div>
        </div>
    </div>

    <!-- Bootstrap JS and dependencies -->
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.3/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
