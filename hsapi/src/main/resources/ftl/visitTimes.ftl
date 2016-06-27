<!DOCTYPE html>
<html lang="ru-RU">
    <head>
        <meta charset="utf-8">
        <title>title</title>
    </head>
    <body>
        <#list visits as visit>
            <form method='POST' action='ticket.html'>
                <input type='hidden' name='numberInInterval' value='${visit.numberInInterval}'>
                <input type='hidden' name='intervalId' value='${visit.intervalId}'>
                <input type='hidden' name='date' value='${date?date?iso_local}'>
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
