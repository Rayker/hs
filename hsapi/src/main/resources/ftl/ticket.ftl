<!DOCTYPE html>
<html lang="ru-RU">
    <head>
        <meta charset="utf-8">
        <title>title</title>
    </head>
    <body>
        <div>${model.hospital}</div>
        <div>${model.doctor} (${model.doctorSpeciality})</div>
        <div>${model.visitDate?date}</div>
        <div>Time: ${model.visitTime}</div>
        <div>Cancellation code: ${model.cancellationCode}</div>
    </body>
</html>
