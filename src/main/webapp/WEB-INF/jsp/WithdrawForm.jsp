<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<h1>${message}</h1>
<form action="withdraw" method="get">
	Enter Account Number: <input name="accountNumber"/><br/>
	Enter Amount : <input name="amount"/><br/>
	<input type="submit"/>
</form>

<a href="statementWithdraw?offset=1&size=5">Get Statement</a>
	<div>
	<table>
		<tr>
			<th>Transaction Id</th>
			<th>Account Number</th>
			<th>Amount</th>
			<th>Transaction Type</th>
			<th>Transaction Date</th>
			<th>Transaction Details</th>
			<th>Current Balance</th>
		</tr>
		<c:forEach var="transactions" items="${currentDataSet.transactions}">
			<tr>
				<td>${transactions.transactionId}</td>
				<td>${transactions.accountNumber}</td>
				<td>${transactions.amount}</td>
				<td>${transactions.transactionType}</td>
				<td>${transactions.transactionDate}</td>
				<td>${transactions.transactionDetails}</td>
				<td>${transactions.currentBalance}</td>
			</tr>
		</c:forEach>
	</table>
	<div>
		<a href="${currentDataSet.previousLink.href}">Previous</a> |
		<a href="${currentDataSet.nextLink.href}">Next</a>
	</div>
	</div>
</body>
</html>