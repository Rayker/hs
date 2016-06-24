<!DOCTYPE html>
<html lang="ru-RU">
    <head>
        <meta charset="utf-8">
        <title>title</title>
    </head>
    <body>
        <form method='POST' action='doctors.html'>
            <input type='hidden' name='specialityId' value='${specialityId}'>
            <input type='hidden' name='hospitalId' value='${hospitalId}'>
            <#list doctors as doctor>
                <label><input type='radio' name='doctorId' value='${doctor.id}'>${doctor.fullname}<br></label>
            </#list>
            <input type='submit'>
        </form>
    </body>
</html>
