<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta charset="utf-8" />
    <!--Import Google Icon Font-->
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <!--Import materialize.css-->
    <link type="text/css" rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css"  media="screen,projection"/>

    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
</head>
<body>

<jsp:include page="nav.jsp"/>
<div class="container">
<h1>Java web</h1>
<p>
    Створюємо новий проєкт, Maven archetype -- ...-webapp
</p>
<p>
    Налаштовуємо конфігурацію запуску.
    Веб-проєкт запускається через веб-сервер, який не входить
    у "коробку" Java і вимагає окремого встановлення.
    Для цього існують:
</p>
<ul>
    <li>Apache Tomcat (для Java-8  -- До версії 9, рекомендовано 8)</li>
    <li>Glassfish (4 або 5)</li>
    <li>JBoss / WildFly (до 20)</li>
    <li>GAE - Google App Engine та інші хмарні сервіси</li>
</ul>
<p>
    Скачуємо будь-який сервер, у більшості випадків його достатньо
    розпакувати з архіву, окремого встановлення не вимагається.
    Не рекомендується створювати папки з не-ASCII символами в імені.
</p>
<p>
    Edit Configuration -- Add -- Tomcat local -- зазначаємо папку сервера (
    куди був розпакований).
    Встановлюємо артефакт для вивантаження (deploy) -- ...war exploded
    За бажанням змінюємо контекст артефакту (не обов'язково)
    OK
</p>
<p>
    Запускаємо, за наявності збою кодування додаємо у самий початок файлу
    &lt;%@ page contentType="text/html;charset=UTF-8" %>
    та додаємо тег &lt;meta charset="utf-8" /> у заголовок HTML
</p>
<p>
    Деплой проєкту виглядає наступним чином: <br/>
    - відбувається збірка (...war_exploded   war - web archive)<br/>
    - цей архів переноситься у спеціальну папку, на яку "дивиться" сервер<br/>
    - сервер при першому звертанні до сайту розпаковує архів і працює з його файлами<br/>
    Особливості - після запуску відкривається браузер та кнопка запуску
    перетворюється на "перезапуск", яка пропонує декілька варіантів
</p>
<ul>
    <li>Update resources - замінити у папці деплою лише статичні ресурси (html, css, ...)</li>
    <li>Update classes and resources - перекомпілювати класи та оновити їх та ресурси</li>
    <li>Redeploy - повторити процедуру деплою</li>
    <li>Restart Server - зупинити сервер та запустити його та процедуру деплою</li>
</ul>

<!--JavaScript at end of body for optimized loading-->
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js"></script>

</div>
</body>

<jsp:include page="footer.jsp"/>
</html>
