<%@ page contentType="text/html;charset=UTF-8" %>

<nav>
    <div style="padding-left: 20px" class=" nav-wrapper blue-grey ">
        <a href="<%=request.getParameter("_contextPath")%>" class="brand-logo">Logo</a>
        <ul id="nav-mobile" class="right hide-on-med-and-down">

            <li <%if("jsp.jsp".equals(request.getParameter("_pageName"))) { %> class="active" <% }%>>
                <a href="jsp">JSP</a></li>

            <li><a href="#">Components</a></li>

            <li <%if("url.jsp".equals(request.getParameter("_pageName"))) { %> class="active" <% }%>>
                <a href="url">Url</a></li>

            <li <%if("about.jsp".equals(request.getParameter("_pageName"))) { %> class="active" <% }%>>
                <a href="about">About</a></li>

        </ul>
    </div>
</nav>
