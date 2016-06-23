<!DOCTYPE html>
<html lang="ru-RU">
    <head>
        <meta charset="utf-8">
        <title>title</title>
    </head>
    <body>
        <form>
            <#list specialities as speciality>
                <input type='radio' name='scenarioId' value='${speciality.id}'>${speciality.name}</input>
            </#list>
        </form>
    </body>
</html>
