<!DOCTYPE html>
<html lang="ru-RU">
    <head>
        <meta charset="utf-8">
        <title>title</title>
    </head>
    <body>
        <#list visits as visit>
            <form method='POST' action='new.html'>
                <input type='hidden' name='numberInInterval' value='${visit.numberInInterval}'>
                <input type='hidden' name='jobIntervalId' value='${visit.jobIntervalId}'>
                <input type='hidden' name='date' value='${date?date?string("dd.MM.yyyy")}'>
                ${visit.visitTime}
                <#if visit.reserved >
                    reserved
                <#else>
                    <input type='submit'>
                </#if>
            </form>
        </#list>
    </body>
</html>
