<!DOCTYPE html>
<html lang="ru-RU">
    <head>
        <meta charset="utf-8">
        <title>title</title>
    </head>
    <body>
        <form method='POST' action='/visits'>
            <input type='hidden' name='numberInInterval' value='${numberInInterval}'>
            <input type='hidden' name='intervalId' value='${intervalId}'>
            <input type='hidden' name='date' value='${date?date?iso_local}'>
            <table>
                <tr>
                    <td>Имя посетителя:</td>
                    <td>
                        <input type='text' name='visitorName'>
                    </td>
                </tr>
                <tr>
                    <td>Дата рождения:</td>
                    <td>
                        <input type='text' name='visitorBirthday'>
                    </td>
                </tr>
                <tr><td colspan='2'>
                    <input type='submit'>
                </td></tr>
            </table>
        </form>
    </body>
</html>
