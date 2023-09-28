
<%


    String contextPath = request.getContextPath() ;  // база сайту - домашнє посилання

%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<h1>Робота з електронною поштою</h1>
<div class="row">
    <div class="col-s1">
        <button style="color: white" class="waves-effect waves-light btn black" onclick="textEmailClick()">
            Text
            <i class="material-icons right">send</i>
        </button>
    </div>
    <div class="col-s1">
        <button style="color: white" class="waves-effect waves-light btn black" onclick="htmlEmailClick()">
            HTML
            <i class="material-icons right">send</i>
        </button>
    </div>
</div>
<div class="row">
    <div class="col s12 m8 offset-m2 l6 offset-l3">
        <div class="card-panel grey lighten-5 z-depth-1">
            <div class="row valign-wrapper">
                <div class="col s2">
                    <i class="material-icons right">send</i>
                </div>
                <div class="col s10">
                  <span class="black-text" id="email-result">
                    Статус повідомлення буде відображено тут після натискання на одну з кнопок
                      надсилання
                  </span>
                </div>
            </div>
        </div>
    </div>
</div>


<p>
    Для надсилання пошти необхідно налаштувати SMTP (Simple Mail Transfer Protocol).
    Це потребує електронної пошти, хостінг (сервіс) якої дозволяє SMTP.
    На прикладі Gmail це вимагає включення двохфакторної автентифікації.
    <strong>Не використовуйте власну пошту.</strong>
</p>

<script>
    function textEmailClick()
    {
        fetch("<%=contextPath%>/email",{
            method:"MAIL"
        }).then(r=>r.text())
            .then(t=>
            {
                document.getElementById("email-result").innerText=t;
            })
    }
    function htmlEmailClick()
    {
        fetch("<%=contextPath%>/email",{
            method:"PATCH"
        }).then(r=>r.text())
            .then(t=>
            {
                document.getElementById("email-result").innerText=t;
            })
    }
</script>
