package ru.ardecs.hs.central.api;

import ru.ardecs.hs.common.entities.Doctor;
import ru.ardecs.hs.common.entities.Hospital;
import ru.ardecs.hs.common.entities.shared.Speciality;
import ru.ardecs.hs.common.models.TicketModel;
import ru.ardecs.hs.common.models.VisitModel;
import ru.ardecs.hs.common.requestmodels.*;

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

	void cache(VisitFormRequestModel visitFormRequestModel, String sessionId) throws IOException, URISyntaxException;

	Long createVisit(VisitCreatingRequestModel visitCreatingRequestModel, String sessionId) throws IOException, URISyntaxException;

	TicketModel getTicketModel(Long reservedTimeId) throws IOException, URISyntaxException;

	boolean delete(Long reservedTimeId);

	boolean ping();
}
