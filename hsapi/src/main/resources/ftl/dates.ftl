<!DOCTYPE html>
<html lang="ru-RU">
    <head>
        <meta charset="utf-8">
        <title>title</title>
    </head>
    <body>
        <form method='GET' action='intervals.html'>
            <input type='hidden' name='doctorId' value='${doctorId}'>
            <#list dates as date>
                <label><input type='radio' name='date' value='${date?date?string("dd.MM.yyyy")}'>${date?date?string("dd.MM.yyyy")}<br></label>
            </#list>
            <input type='submit'>
        </form>
    </body>
</html>
