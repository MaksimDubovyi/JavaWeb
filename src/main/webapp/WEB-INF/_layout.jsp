
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  String contextPath = request.getContextPath() ;  // база сайту - домашнє посилання
  String pageName =           // Вилучаємо значення атрибуту (закладеного у сервлеті)
          (String)            // оскільки значення Object, необхідне пряме перетворення
                  request             // об'єкт request доступний у всіх JSP незалежно від сервлетів
                          .getAttribute(      //
                                  "pageName")     // збіг імен зі змінною - не вимагається
                  + ".jsp" ;           // Параметри можна модифікувати
%>
<html>
<head>
  <meta charset="utf-8" />
  <!--Import Google Icon Font-->
  <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
  <!--Import materialize.css-->
  <link type="text/css" rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css"  media="screen,projection"/>

  <!--Let browser know website is optimized for mobile-->
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
</head>
<body>

<jsp:include page="nav.jsp">
  <jsp:param name="_pageName" value="<%=pageName%>" />
  <jsp:param name="_contextPath" value="<%=contextPath%>" />
</jsp:include>


<div class="container">
  <jsp:include page="<%=pageName%>"/>
</div>
<!--JavaScript at end of body for optimized loading-->
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js"></script>
<p>
  Встановлене кодування: <%= request.getAttribute("charset") %>
  <%-- атрибут встановлюється у фільтрі CharsetFilter --%>
</p>
<jsp:include page="footer.jsp"/>

  </body>
</html>