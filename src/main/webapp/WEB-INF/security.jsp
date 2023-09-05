<%@ page import="java.io.FileReader" %>
<%@ page import="java.util.Scanner" %>
<%@ page import="java.io.IOException" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: Пользователь
  Date: 04.09.2023
  Time: 17:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
  List<String> buf = new ArrayList<>();

  try(FileReader reader =new FileReader("RegistrationFilter.txt"); Scanner scanner =new Scanner(reader))
  {int x=0;
    while (scanner.hasNext()) {
      x++;
      buf.add(x+") "+scanner.nextLine());
    }
  }
  catch (IOException ex)
  {
    System.out.println(ex.getMessage());
  }
%>



<ul>
  <% for (int i=0; i< buf.size();i++){%>
  <li class="myLi"><%=buf.get(i)%></li> <%}%>
</ul>
