<%@ page contentType="text/html;charset=UTF-8" %>

<!--JavaScript at end of body for optimized loading-->
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js"></script>
<footer class="page-footer black">
    <div class="container">
        <div class="row">
            <div class="col l6 s12">
                <h5 class="white-text">Footer Content</h5>
                <p class="grey-text text-lighten-4">Д.З. Розмітка: додати футер (взяти зразок material) JSP: створити циклом таблицю, яка виводить назви днів тижня День Назва 1 Пн 2 Вт .....</p>
            </div>
            <div class="col l4 offset-l2 s12">
                <h5 class="white-text">Дні тижня</h5>
                <ul><%String[] days = {"Понеділок", "Вівторок", "Середа", "Четвер", "П`ятниця", "Субота", "Неділя"};
                 for (int i=0; i<days.length;i++){%>
                    <li><a class="grey-text text-lighten-3" href="#!"><%=days[i]%></a></li> <%}%>

                </ul>
            </div>
        </div>
    </div>
    <div class="footer-copyright">
        <div class="container">
            © 2014 Дні тижня
        </div>
    </div>
</footer>