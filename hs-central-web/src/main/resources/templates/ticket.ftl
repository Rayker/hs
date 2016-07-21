<!DOCTYPE html>
<html lang="ru-RU">
    <head>
        <meta charset="utf-8">
        <title>title</title>
    </head>
    <body>
        <div>${model.hospital}</div>
        <div>${model.doctor} (${model.doctorSpeciality})</div>

        <div>${model.visitDate?date?string("dd.MM.yyyy")?date}</div>
        <div>Time: ${model.visitTime}</div>

        <div>Visitor:</div>
        <div>${model.visitorName}</div>
        <div>${model.visitorBirthday?date?string("dd.MM.yyyy")?date}</div>

        <div>Cancellation code: ${model.visitId}</div>

        <form method='POST' action='/cities/${cityId}/visits/${model.visitId}/ticket/send'>
            <input type='text' name='addressTo'>
            <input type='submit'>
        </form>
    </body>
</html>
