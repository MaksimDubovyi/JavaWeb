<%@ page contentType="text/html;charset=UTF-8" %>
<%
    boolean how=false;
    String result = (String) request.getAttribute( "result" ) ;
    if ( result == null ) {
        result = "" ;
    }

%>
<div class="container" style="margin-top: 10%">
    <div class="card-panel grey lighten-5">
        <div class="row">
            <form action="" method="post" class="col s12">
                <div class="row valign-wrapper">
                    <div class="input-field col s8">
                        <input id="input_text"
                               name="input_text"
                               type="text"
                               data-length="10"
                        />
                        <label for="input_text">Введіть рядок:</label>
                    </div>
                    <div class="col s2">
                        <button style="color: white" class="btn waves-effect waves-light blue-grey darken-4 lighten-3 offset-s1" type="submit">
                            <i class="material-icons right">send</i>
                        </button>

                    </div>
                </div>
                <div class="row valign-wrapper">
                    <div class="input-field col s8">
                        <textarea id="textarea2" class="materialize-textarea" data-length="120"  name="save_text"  readonly><%=result%></textarea>
                        <label for="textarea2">Результат:</label>
                    </div>
                    <div class="col s2">
                        <button style="color: white" <%if(result==""){%>disabled<%}%> class="btn waves-effect waves-light blue-grey darken-4 lighten-2 offset-s1" type="submit" name="mode" value="download">
                            <i class="material-icons right">file_download</i>
                        </button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>