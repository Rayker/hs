<!DOCTYPE html>
<html lang="ru-RU">
    <head>
        <meta charset="utf-8">
        <title>title</title>
    </head>
    <body>
        <#list cities as city>
            <a href='/cities/${city.id}/specialities.html'>${city.displayedName}</a>
            <br />
        </#list>
    </body>
</html>
