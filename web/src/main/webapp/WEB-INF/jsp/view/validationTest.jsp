<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Customer Save Page</title>
    <style>
        .error {
            color: #ff0000;
            font-style: italic;
            font-weight: bold;
        }
    </style>
</head>
<body>

<form:form method="POST" commandName="userInfo" action="save.do">
    <table>
        <tr>
            <td>이름:</td>
            <td><form:input path="name" /></td>
            <td><form:errors path="name" cssClass="error" /></td>
        </tr>
        <tr>
            <td>이메일:</td>
            <td><form:input path="email" /></td>
            <td><form:errors path="email" cssClass="error" /></td>
        </tr>
        <tr>
            <td>부서/직급:</td>
            <td><form:input path="division" /></td>
            <td><form:errors path="division" cssClass="error" /></td>
        </tr>
        <tr>
            <td>계정이름:</td>
            <td><form:input path="accountName" /></td>
            <td><form:errors path="accountName" cssClass="error" /></td>
        </tr>
        <tr>
            <td>비밀번호:</td>
            <td><form:password path="password" /></td>
            <td><form:errors path="password" cssClass="error" /></td>
        </tr>
        <tr>
            <td>비밀번호확인:</td>
            <td><form:password path="confirmPassword"/></td>
            <td><form:errors path="confirmPassword" cssClass="error" /></td>
        </tr>
        <tr>
            <td>휴대폰번호:</td>
            <td><form:input path="phone" /></td>
            <td><form:errors path="phone" cssClass="error" /></td>
        </tr>
        <tr>
            <td colspan="3"><input type="submit" value="Save Customer"></td>
        </tr>
    </table>

</form:form>

</body>
</html>