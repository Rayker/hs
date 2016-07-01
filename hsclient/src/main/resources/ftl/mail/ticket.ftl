<div>${hospital}</div>
<div>${doctor} (${doctorSpeciality})</div>
<div>${visitDate?date?string("dd.MM.yyyy")?date}</div>
<div>Time: ${visitTime}</div>
<div>Visitor:</div>
<div>${visitorName}</div>
<div>${visitorBirthday?date?string("dd.MM.yyyy")?date}</div>
<div>Cancellation code: ${visitId}</div>