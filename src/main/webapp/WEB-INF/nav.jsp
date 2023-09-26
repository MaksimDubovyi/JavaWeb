<%@ page contentType="text/html;charset=UTF-8" %>

<nav>
    <div style="padding-left: 20px" class=" nav-wrapper black">
        <a href="<%=request.getParameter("_contextPath")%>" class="brand-logo">Logo</a>
        <ul id="nav-mobile" class="right hide-on-med-and-down">

            <li <%if("jsp.jsp".equals(request.getParameter("_pageName"))) { %> class="myActive" <% }%>>
                <a href="#front">Front</a></li>

            <li <%if("jsp.jsp".equals(request.getParameter("_pageName"))) { %> class="myActive" <% }%>>
                <a href="jsp">JSP</a></li>

            <li><a href="#">Components</a></li>

            <li <%if("url.jsp".equals(request.getParameter("_pageName"))) { %> class="myActive" <% }%>>
                <a href="url">Url</a></li>

            <li <%if("about.jsp".equals(request.getParameter("_pageName"))) { %> class="myActive" <% }%>>
                <a href="about">About</a></li>

            <li <%if("security.jsp".equals(request.getParameter("_pageName"))) { %> class="myActive" <% }%>>
                <a href="security">Security</a></li>
            <!-- Modal Trigger -->
            <li><a style="color: white" class="waves-effect waves-light btn modal-trigger leftLi" href="#auth-modal">
                <span class="material-icons">login</span>
            </a></li>
            <li id="user-avatar">

            </li>
        </ul>
    </div>
</nav>
