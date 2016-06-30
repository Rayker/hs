<!DOCTYPE html>
<html lang="ru-RU">
    <head>
        <meta charset="utf-8">
        <title>title</title>
    </head>
    <body>
        <form method='GET' action='doctors.html'>
            <input type='hidden' name='specialityId' value='${specialityId}'>
            <#list hospitals as hospital>
                <label><input type='radio' name='hospitalId' value='${hospital.id}'>${hospital.name}<br></label>
            </#list>
            <input type='submit'>
        </form>
    </body>
</html>
