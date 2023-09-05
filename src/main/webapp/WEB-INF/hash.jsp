<%--
  Created by IntelliJ IDEA.
  User: Пользователь
  Date: 05.09.2023
  Time: 20:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String hash =  (String) request.getAttribute( "hash");
%>
<div class="col" style="margin-top: 5%">
    <div class="input-field col s6">
        <input value="" id="addhash" type="text" class="validate">
        <label class="active" for="addhash">Поле для вводу</label>
    </div>

    <div class="input-field col s6">
        <input value="<%=hash%>" id="myhash" type="text" class="validate">
        <label class="active" for="myhash">Hash</label>
    </div>
</div>

