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
        <div>${visitorBirthday}</div>

        <div>Cancellation code: ${cancellationCode}</div>
    </body>
</html>
