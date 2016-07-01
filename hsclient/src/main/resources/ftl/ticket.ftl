<!DOCTYPE html>
<html lang="ru-RU">
    <head>
        <meta charset="utf-8">
        <title>title</title>
    </head>
    <body>
        <div>${hospital}</div>
        <div>${doctor} (${doctorSpeciality})</div>

        <div>${visitDate?date?string("dd.MM.yyyy")?date}</div>
        <div>Time: ${visitTime}</div>

        <div>Visitor:</div>
        <div>${visitorName}</div>
        <div>${visitorBirthday?date?string("dd.MM.yyyy")?date}</div>

        <div>Cancellation code: ${visitId}</div>

        <form method='POST' action='ticket/send'>
            <input type='text' name='addressTo'>
            <input type='submit'>
        </form>
    </body>
</html>
