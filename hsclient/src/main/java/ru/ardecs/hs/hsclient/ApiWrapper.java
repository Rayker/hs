package ru.ardecs.hs.hsclient;

import ru.ardecs.hs.hscommon.entities.Doctor;
import ru.ardecs.hs.hscommon.entities.Hospital;
import ru.ardecs.hs.hscommon.entities.ReservedTime;
import ru.ardecs.hs.hscommon.entities.Speciality;
import ru.ardecs.hs.hscommon.models.VisitModel;
import ru.ardecs.hs.hscommon.requestmodels.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;

public interface ApiWrapper {
	List<Speciality> specialities() throws IOException, URISyntaxException;

	List<Hospital> hospitals(HospitalsRequestModel requestModel) throws URISyntaxException, IOException;

	List<Doctor> doctors(DoctorsRequestModel doctorsRequestModel) throws URISyntaxException, IOException;

	List<Date> choseDate(Long doctorId) throws IOException, URISyntaxException;

	List<VisitModel> times(IntervalsRequestModel intervalsRequestModel, String sessionId) throws URISyntaxException, IOException;

	void cache(VisitFormRequestModel visitFormRequestModel, String sessionId);

	long createVisit(VisitCreatingRequestModel visitCreatingRequestModel, String sessionId) throws IOException;

	ReservedTime getReservedTime(Long reservedTimeId) throws IOException;

	boolean delete(Long reservedTimeId);
}
