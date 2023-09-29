
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
    <div id="confirm-email"></div>
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
    <a style="margin-right:10px;" href="<%= contextPath %>signup" class="modal-close waves-effect waves-green btn-flat teal lighten-3">Реєстрація</a>
    <a style="margin-right:10px;" href="#!" class="modal-close waves-effect waves-green btn-flat indigo lighten-3 " >Забув пароль</a>
    <a style="margin-right:10px;" href="#!" class="waves-effect waves-green btn-flat green lighten-3" id="submit">Вхід</a>
    <a style="margin-right:100px;" href="#!" class="waves-effect waves-green btn-flat red lighten-3" id="exit">Вихід</a>
  </div>
</div>
<script>
  document.addEventListener('DOMContentLoaded', function() {

    const elems = document.querySelectorAll('.modal');
    M.Modal.init(elems,
    {opacity:0.5});


      const token = window.localStorage.getItem('token');
      const submitBtn=document.getElementById("submit")
      const exitBtn=document.getElementById("exit")
      exitBtn.addEventListener( 'click', exitClick ) ;
      if(token)
        submitBtn.style.display="none";
      else
        exitBtn.style.display="none";



    const signupButton = document.getElementById('submit');

    if( signupButton ) {
      signupButton.addEventListener( 'click', loginClick ) ;
    }
    else {
      console.error( 'signupButton not found' )
    }

    window.addEventListener('hashchange', frontRouter);
    frontRouter();


  });
  function exitClick()
  {
    const submitBtn=document.getElementById("submit")
    const exitBtn=document.getElementById("exit")
    exitBtn.style.display="none";
    submitBtn.style.display="block";
    window.localStorage.clear();
    window.location.href="<%=url3%>/";
  }
  function  frontRouter(){

    console.log(location.hash)
    switch (location.hash)
    {
      case '#front':
        loadFrontRage();
        break;
      default:
    }


  }

  function loadFrontRage()
  {
    //перевіряємо чи є тоекен у
    const token = window.localStorage.getItem('token');
    if(!token)
    {//не автиризований режим
      alert('Дана сторінка вимагає автентифікації')
      window.location.href="<%=url3%>/";
      return;
    }
    //намагаємось декодувати токен та визначити термін придатності
    try{
      var data=JSON.parse(atob(token))

      let dateNow = Date.now(); // Поточна дата у мілісекундах
      let dataExp = Date.parse(data.exp); // Перетворення строки у дату

      console.log(dateNow + '\n' + dataExp+'\n'+data.exp);
      if(dateNow>=dataExp)
      {
        alert('Токен не коректний повторіть автентифікацію!!! ')
        window.location.href="<%=url3%>/";
        return;
      }

    }
    catch (ex)
    {
      alert('Токен не коректний повторіть автентифікацію ')
      window.location.href="<%=url3%>/";
      return;
    }

    const  headers = (token==null)?{}:{
      'Authorization':`Bearer ${token}`
    }
    fetch('<%=url3%>/front',{
      method:'GET',
      headers:headers
    }).then(r=>r.json())
            .then(j=>{
              if(typeof j.login!='undefined')
              {
                const userAvatar=document.getElementById("user-avatar");

                if(!userAvatar)throw "user-avatar not found";
                if(j.avatar!=null&&j.avatar!="undefined")
                {
                  let i = j.avatar.lastIndexOf('\\');
                  if(i>-1)
                  {
                    j.avatar=j.avatar.substring(i+1);
                  }
                }
                else
                {
                  j.avatar="default.png"
                }
                userAvatar.innerHTML=`<img style="max-height:50px; border-radius:15px; margin-right:2%;" src="<%=url3%>/upload/${j.avatar}" />`
// ---------------------- CONFIRM EMAIL --------------------------
                if(typeof j.emailConfirmCode=='string'&& j.emailConfirmCode.length > 0 ) {  // є код -- пошта не підтверджена
                  const confirmDiv = document.getElementById("confirm-email");
                  if( ! confirmDiv ) throw "confirm-email not found" ;
                  confirmDiv.innerHTML = `Ваша пошта не підтверджена, введіть код з е-листа
    <div class="input-field inline"><input id='email-code'/></div><button onclick='confirmCodeClick()'>Підтвердити</button>` ;
                  confirmDiv.style.border = "1px solid maroon" ;
                  confirmDiv.style.padding = "5px 10px" ;
                }
              }
              console.log(j)
            })
  }
  function confirmCodeClick() {
    const emailCodeInput = document.getElementById("email-code");
    if( ! emailCodeInput ) throw "email-code not found" ;
    fetch('<%=url3%>/signup?code='+emailCodeInput.value,{
      method:"PATCH",
      headers:{ 'Authorization':`Bearer `+ window.localStorage.getItem('token')}
    }).then(r=>{
      if(r.status==202)
      {
        alert("Пошту підтверджено");
        window.location.reload();//оновлюємо сторінку - має зникнути поле підтвердження
      }
      else
      {
        r.text().then(alert)//передаемо в alert результат r.text()
      }
    })
    console.log( emailCodeInput.value ) ;
  }

   function loginClick() {
    const loginInput = document.getElementById('auth-login');
    if( ! loginInput ) throw "input id='auth-login' not found" ;
    const passwordInput = document.getElementById('auth-password');
    if( ! passwordInput ) throw "input id='auth-password' not found" ;

    const authLogin = loginInput.value.trim() ;
    if( authLogin.length < 2 ) {
      alert( "Логін занадто короткий або не введений!" ) ;
      return ;
    }
    const authPassword = passwordInput.value ;
    if( authPassword.length < 2 ) {
      alert( "Пароль занадто короткий або не введений" ) ;
      return ;
    }
    // const url = `<%= contextPath %>/signup?auth-login=${authLogin}&auth-password=${authPassword}`;
    const url = `<%=url3%>/signup` ;
    // let formData = new FormData();
    // formData.append('auth-login',authLogin );
    // formData.append('auth-password',authPassword );
    // fetch(url, { method: 'PUT', body: formData })
    fetch(url, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        'auth-login': authLogin,
        'auth-password': authPassword
      })
    })
            .then(response => {
              return response.json(); // Парсимо відповідь як JSON
            })
            .then(data => {
// data буде об'єктом, який містить поля statusCode та message
              console.log(data);
              if(data.statusCode==200)
              {
                //декодуємо токен, дізнаємось дати
                let token = JSON.parse(atob(data.message))
                alert('token expires'+ token.exp)
                window.localStorage.setItem('token',data.message);
                const submitBtn=document.getElementById("submit")
                const exitBtn=document.getElementById("exit")
                exitBtn.style.display="block";
                submitBtn.style.display="none";
                loginInput.value="";
                passwordInput.value="";
                var instance = M.Modal.getInstance(document.getElementById("auth-modal"));
                instance.close();
              }
              else
              {  const result = document.getElementById('result');
                result.textContent=data.message;}



            })
            .catch(error => {
              console.error('Fetch Error:', error);
            });
  }
</script>
  </body>
</html>
