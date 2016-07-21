<!DOCTYPE html>
<html lang="ru-RU">
    <head>
        <meta charset="utf-8">
        <title>title</title>
    </head>
    <body>
        <form method='GET' action='/cities/${cityId}/hospitals.html'>
            <#list specialities as speciality>
                <label><input type='radio' name='specialityId' value='${speciality.id}'>${speciality.name}<br></label>
            </#list>
            <input type='submit'>
        </form>
    </body>
</html>
