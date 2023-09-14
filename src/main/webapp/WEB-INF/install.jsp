<%@ page contentType="text/html;charset=UTF-8" %>

<%
    String[] genders = {
            "Чоловіча гендерна ідентичність",
            "Жіноча гендерна ідентичність",
            "Трансгендерна гендерна ідентичність",
            "Нон-бінарна гендерна ідентичність",
            "Гендерне поняття",
            "Два духи (Two-Spirit)",
            "Гендерне переходження",
            "Гендерно-нейтральні особи",
            "Гендерні варіанти",
            "Пангендер",
            "Демігендер",
            "Гендерне вираження",
            "Гендерний агент",
            "Воно",
            "Не вказувати"
    };
    String[] culture = {"uk-UA", "en-US", "en-CA", "fr-CA", "fr-FR", "de-DE", "it-IT","es-ES","en-GB","ja-JP"};
%>
<div class="container" style="margin-top: 10%; width: 80%">
  <div class="card-panel grey lighten-5">
    <div class="row">
<h2>Реєстрація нового користувача:</h2>

      <form action="" method="post" >

          <input id="input_firstName" name="input_firstName" type="text"  />
          <label class="jLabel" for="input_firstName">Ім'я:</label>

          <input id="input_lastName" name="input_lastName" type="text"  />
          <label class="jLabel" for="input_lastName">Прізвище:</label>

          <input id="input_email" name="input_email" type="email"  required />
          <label class="jLabel" for="input_email">Пошта: <i class="material-icons right">email</i></label>

          <input id="input_phone" name="input_phone" type="tel"  />
          <label class="jLabel" for="input_phone">Телефон: <i class="material-icons right">phone_iphone</i></label>

          <input id="input_birthdate" name="input_birthdate" type="date"/>
          <label class="jLabel" for="input_birthdate">День народження:<i class="material-icons right">child_friendly</i></label>

          <input  class="form-control form-control-lg" type="file" id="input_avatar" name="input_avatar" accept=".jpg, .jpeg, .png">
          <label class="jLabel" for="input_avatar">Виберіть аватар для завантаження: <i class="material-icons right">add_a_photo</i></label>

          <input id="input_login" name="input_login" type="text"  required />
          <label class="jLabel" for="input_login">Логін:</label>

          <input id="input_passwordDk" name="input_passwordDk" type="password"  required />
          <label class="jLabel" for="input_passwordDk">Пароль:<i class="material-icons right">lock</i></label>

          <select class="form-select" aria-label="Default select example" name="input_culture" id="input_culture">
              <option selected>Оберіть країну</option>
          <% for (int i=0; i<culture.length;i++){%>
              <option value="<%=culture[i]%>"><%=culture[i]%></option><%}%>
          </select>
          <label class="jLabel" for="input_culture">Країна:<i class="material-icons right">language</i></label>

          <select class="form-select" aria-label="Default select example" name="input_gender" id="input_gender">
              <option selected>Оберіть стать</option>
              <%for (int i=0; i<genders.length;i++){%>
              <option value="<%=genders[i]%>"><%=genders[i]%></option><%}%>
          </select>
          <label class="jLabel" for="input_gender">Стать:<i class="material-icons right">traffic</i></label>



            <button style="color: white; margin-top: 5%; width: 100%" class="btn waves-effect waves-light blue-grey darken-4 lighten-3 offset-s1" type="submit">
              <i class="material-icons right">check</i>Реєстрація
            </button>



      </form>



    </div>
  </div>
</div>