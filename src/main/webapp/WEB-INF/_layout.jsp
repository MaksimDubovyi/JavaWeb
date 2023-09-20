
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  StringBuffer url = request.getRequestURL();
  String url2 = url.toString();
  String contextPath  = url2.replaceAll("WEB-INF/_layout.jsp", "");

  String url3 = request.getContextPath() ;  // база сайту - домашнє посилання
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

  <link type="text/css" rel="stylesheet" href="<%=contextPath%>/css.css"/>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">
  <!--Let browser know website is optimized for mobile-->
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
</head>
<body>
<jsp:include page="nav.jsp">
  <jsp:param name="_pageName" value="<%=pageName%>" />
  <jsp:param name="_contextPath" value="<%=contextPath%>" />
</jsp:include>

<div style="display: flex">
  <div class="layoutLeft" >
    <img class="layoutImg" src="img/java.png"/>
    <p class="layoutTxtJava" src="img/java.png" class="flow-text">JAVA</p>


    <jsp:include page="leftNav.jsp">
      <jsp:param name="_pageName" value="<%=pageName%>" />
    </jsp:include>
  </div>

  <div class="container">
    <jsp:include page="<%=pageName%>"/>
  </div>
</div>

<!--JavaScript at end of body for optimized loading-->
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js"></script>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/js/bootstrap.bundle.min.js" integrity="sha384-HwwvtgBNo3bZJJLYd8oVXjrBZt8cqVSpeBNS5n7C8IVInixGAoxmnlMuBnhbgrkm" crossorigin="anonymous"></script>
<p>
  Встановлене кодування: <%= request.getAttribute("charset") %>
  <%-- атрибут встановлюється у фільтрі CharsetFilter --%>
</p>
<jsp:include page="footer.jsp"/>

<%-- Materialaze Modal (Auto)--%>
<div id="auth-modal" class="modal">
  <div class="modal-content">
    <h4>Автентифікація</h4>
    <div class="row valign-wrapper">
      <div class="input-field col s6">
        <i class="material-icons prefix">account_circle</i>
        <input id="auth-login" name="auth-login" type="text" class="validate">
        <label for="auth-login">Логін</label>
      </div>
      <div class="input-field col s6">
        <i class="material-icons prefix">mode_edit</i>
        <input id="auth-password" name="auth-password" type="text" class="validate">
        <label for="auth-password">Пароль</label>
      </div>
    </div>
    <p id="result"  style="color: red"></p>
  </div>
  <div class="modal-footer">
    <a href="<%= contextPath %>signup" class="modal-close waves-effect waves-green btn-flat teal lighten-3">Реєстрація</a>
    <a href="#!" class="modal-close waves-effect waves-green btn-flat indigo lighten-3">Забув пароль</a>
    <a href="#!" class="waves-effect waves-green btn-flat green lighten-3" id="submit">Вхід</a>

  </div>
</div>
<script>
  document.addEventListener('DOMContentLoaded', function() {

    const elems = document.querySelectorAll('.modal');
    M.Modal.init(elems,
    {opacity:0.5});
    const signupButton = document.getElementById('submit');

    if( signupButton ) {
      signupButton.addEventListener( 'click', loginClick ) ;
    }
    else {
      console.error( 'signupButton not found' )
    }
  });

  function loginClick() {

    const loginInput = document.getElementById('auth-login');
    if( ! loginInput ) throw "input id='auth-login' not found" ;
    const passwordInput = document.getElementById('auth-password');
    if( ! passwordInput ) throw "input id='auth-password' not found" ;

    if( loginInput.value.trim().length < 2 ) {
      alert( "Логін занадто короткий або не введений!" ) ;
      return ;
    }

    if( passwordInput.value.trim().length < 2 ) {
      alert( "Пароль занадто короткий або не введений" ) ;
      return ;
    }


    const url = `/JavaWeb/signup?auth-login=${loginInput.value}&auth-password=${passwordInput.value}`;

    fetch(url, { method: 'PUT' })
            .then(response => {
              return response.json(); // Парсимо відповідь як JSON
            })
            .then(data => {
              // data буде об'єктом, який містить поля statusCode та message
              console.log(data.statusCode);
              console.log(data.message);
              const result = document.getElementById('result');
              result.textContent=data.message;
            })
            .catch(error => {
              console.error('Fetch Error:', error);
            });
  }
</script>
  </body>
</html>
