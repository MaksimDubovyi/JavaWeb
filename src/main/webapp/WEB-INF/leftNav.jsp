<%--
  Created by IntelliJ IDEA.
  User: Пользователь
  Date: 05.09.2023
  Time: 17:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

    <ul>

      <li style="margin-top: 10%">
        <a <%if("jsp.jsp".equals(request.getParameter("_pageName"))) { %> class="leftLiActive" <% }else{%>class="leftLi"<%}%> href="jsp">JSP</a></li>
      <li style="margin-top: 10%">
        <a <%if("url.jsp".equals(request.getParameter("_pageName"))) { %> class="leftLiActive" <% }else{%>class="leftLi"<%}%> href="url">Url</a></li>

      <li style="margin-top: 10%">
        <a  <%if("about.jsp".equals(request.getParameter("_pageName"))) { %> class="leftLiActive" <% }else{%>class="leftLi"<%}%> href="about">About</a></li>

      <li style="margin-top: 10%">
        <a  <%if("security.jsp".equals(request.getParameter("_pageName"))) { %> class="leftLiActive" <% }else{%>class="leftLi"<%}%> href="security">Security</a></li>

        <li style="margin-top: 10%">
            <a  <%if("hash.jsp".equals(request.getParameter("_pageName"))) { %> class="leftLiActive" <% }else{%>class="leftLi"<%}%> href="hash">Hash</a></li>

    </ul>
