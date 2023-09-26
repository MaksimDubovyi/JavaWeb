<%@ page contentType="text/html;charset=UTF-8" %>
<h2 class="center-align">Реєстрація нового користувача</h2>
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
<div class="container" style="margin-top: 10px; width: 80%">
    <div class="card-panel grey lighten-5">
        <div class="row">
            <div class="row">
                <div class="input-field col s6">
                    <i class="material-icons prefix">account_circle</i>
                    <input id="reg-name" name="reg-name" type="text" class="validate">
                    <label for="reg-name">Ім'я</label>
                </div>
                <div class="input-field col s6">
                    <i class="material-icons prefix">account_circle</i>
                    <input id="reg-lastname" name="reg-lastname" type="text" class="validate">
                    <label for="reg-lastname">Прізвище</label>
                </div>
            </div>
            <div class="row">
                <div class="input-field col s6">
                    <i class="material-icons prefix">email</i>
                    <input id="reg-email" name="reg-email" type="text" class="validate">
                    <label for="reg-email">Пошта</label>
                </div>
                <div class="input-field col s6">
                    <i class="material-icons prefix">phone_iphone</i>
                    <input id="reg-phone" name="reg-phone" type="text" class="validate">
                    <label for="reg-phone">Телефон</label>
                </div>
            </div>
            <div class="row">
                <div class="input-field col s6">
                    <i class="material-icons prefix">child_friendly</i>
                    <input id="reg-birthdate" name="reg-birthdate" type="date" class="validate">
                    <label for="reg-birthdate">День народження</label>
                </div>
                <div class="file-field input-field col s6">
                    <div class="btn">
                        <span>File</span>
                        <input type="file" id="reg-avatar" name="reg-avatar">
                    </div>
                    <div class="file-path-wrapper">
<%--                        <input type="file" id="reg-avatar" name="reg-avatar" accept="image/*" multiple>--%>
                        <input class="file-path validate" type="text"  placeholder="Виберіть аватар">
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="input-field col s6">
                    <i class="material-icons prefix">badge</i>
                    <input id="reg-login" name="reg-login" type="text" class="validate">
                    <label for="reg-login">Логін</label>
                </div>
                <div class="input-field col s6">
                    <i class="material-icons prefix">lock</i>
                    <input id="reg-password" name="reg-password" type="text" class="validate">
                    <label for="reg-password">Пароль</label>
                </div>
            </div>
            <div class="row">
                <div class="input-field col s6">
                    <i class="material-icons prefix">translate</i>
                    <select name="reg-culture" id="reg-culture">
                        <option value="" disabled selected>Оберіть культуру</option>
                        <% for (int i=0; i<culture.length;i++) { %>
                        <option value="<%=culture[i]%>"><%=culture[i]%></option>
                        <% } %>
                    </select>
                    <label>Локалізація</label>
                </div>
                <div class="input-field col s6">
                    <i class="material-icons prefix">traffic</i>
                    <select name="reg-gender" id="reg-gender">
                        <option value="" disabled selected>Оберіть стать</option>
                        <% for (int i=0; i<genders.length;i++) { %>
                        <option value="<%=genders[i]%>"><%=genders[i]%></option>
                        <% } %>
                    </select>
                    <label>Гендер</label>
                </div>
            </div>
            <button style="color: white; margin-top: 5%; width: 100%" class="btn waves-effect waves-light blue-grey darken-4 lighten-3 offset-s1" type="submit">
                <i class="material-icons right">check</i>Реєстрація
            </button>
        </div>
    </div>
</div>
<script>
    document.addEventListener('DOMContentLoaded', function() {
        var elems = document.querySelectorAll('select');
        M.FormSelect.init(elems, {});

        const signupButton = document.querySelector('button[type="submit"]');
        if( signupButton ) {
            signupButton.addEventListener( 'click', signupClick ) ;
        }
        else {
            console.error( 'signupButton not found' )
        }
    });
    function signupClick() {
        console.log( 'Sending...' ) ;
        const nameInput = document.getElementById('reg-name');
        if( ! nameInput ) throw "input id='reg-name' not found" ;
        const lastnameInput = document.getElementById('reg-lastname');
        if( ! lastnameInput ) throw "input id='reg-lastname' not found" ;
        const emailInput = document.getElementById('reg-email');
        if( ! emailInput ) throw "input id='reg-email' not found" ;
        const phoneInput = document.getElementById('reg-phone');
        if( ! phoneInput ) throw "input id='reg-phone' not found" ;
        const birthdateInput = document.getElementById('reg-birthdate');
        if( ! birthdateInput ) throw "input id='reg-birthdate' not found" ;
        const avatarInput = document.getElementById('reg-avatar');
        if( ! avatarInput ) throw "input id='reg-avatar' not found" ;
        const loginInput = document.getElementById('reg-login');
        if( ! loginInput ) throw "input id='reg-login' not found" ;
        const passwordInput = document.getElementById('reg-password');
        if( ! passwordInput ) throw "input id='reg-password' not found" ;
        const cultureInput = document.getElementById('reg-culture');
        if( ! cultureInput ) throw "input id='reg-culture' not found" ;
        const genderInput = document.getElementById('reg-gender');
        if( ! genderInput ) throw "input id='reg-gender' not found" ;

        const formData = new FormData() ;
        if( emailInput.value.trim().length < 2 ) {
            alert( "Електронна пошта є обов'язковою" ) ;
            return ;
        }
        formData.append( emailInput.name, emailInput.value ) ;

        if( loginInput.value.trim().length < 2 ) {
            alert( "Логін занадто короткий або не введений" ) ;
            return ;
        }
        formData.append( loginInput.name, loginInput.value ) ;

        if( passwordInput.value.trim().length < 2 ) {
            alert( "Пароль занадто короткий або не введений" ) ;
            return ;
        }



        console.log(avatarInput.files[0])

        formData.append( passwordInput.name, passwordInput.value ) ;
        formData.append( nameInput.name, nameInput.value ) ;

        formData.append( lastnameInput.name, lastnameInput.value ) ;
        formData.append( phoneInput.name, phoneInput.value ) ;
        formData.append( birthdateInput.name, birthdateInput.value ) ;
        formData.append(avatarInput.name, avatarInput.files[0]);

        formData.append( cultureInput.name, cultureInput.value ) ;
        formData.append( genderInput.name, genderInput.value ) ;

    fetch(window.location.href,{method:'POST',body:formData})
    .then(r=>r.text())
        .then(console.log)

        // let fileData = new FormData() ;
        // fileData.append( 'upload', avatarInput.files[0] ) ;
        // fetch( 'http://2760141.ji513362.web.hosting-test.net/', {
        //     method: 'POST',
        //     body: fileData
        // }).then( r => {
        //     if(r.status === 200) return r.text();
        //     else console.error( r.text() ) ;
        // } ).then( t => {
        //     console.log(t);  // t - ім'я збереженого файлу
        //     formData.append( 'avatar', t ) ;
        //     /*
        // fetch( window.location.href, {
        //     method: 'POST',
        //     body: formData
        // }).then( r => r.json() ).then( console.log ) ;*/
        // } ) ;

    }
</script>