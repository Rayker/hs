package ru.ardecs.hs.hsclient;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.SerializableEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ardecs.hs.hscommon.entities.Doctor;
import ru.ardecs.hs.hscommon.entities.Hospital;
import ru.ardecs.hs.hscommon.entities.Speciality;
import ru.ardecs.hs.hscommon.models.TicketModel;
import ru.ardecs.hs.hscommon.models.VisitModel;
import ru.ardecs.hs.hscommon.requestmodels.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class ApiWrapperImpl implements ApiWrapper {
	@Autowired
	private CloseableHttpClient httpclient;

	private URI createUri(String path, List<NameValuePair> nvps) throws URISyntaxException {
		return new URIBuilder()
				.setScheme("http")
				.setHost("localhost")
				.setPort(8090)
				.setPath(path)
				.addParameters(nvps)
				.build();
	}

	private CloseableHttpResponse execute(HttpRequestBase httpRequest) throws IOException {
		return httpclient.execute(httpRequest);
	}

	private <T> T sendGet(URI uri, Type type) throws IOException {
		HttpGet httpGet = new HttpGet(uri);
		CloseableHttpResponse response = httpclient.execute(httpGet);
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
//		Type listType = new TypeToken<T>() {
//		}.getType();
		return new Gson().fromJson(rd, type);
	}

	private <T> T parse(CloseableHttpResponse response) throws IOException {
		Type type = new TypeToken<T>() {
		}.getType();
		BufferedReader responseReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		return new Gson().fromJson(responseReader, type);
	}

	private CloseableHttpResponse sendPost(String path, List<NameValuePair> nvps) throws URISyntaxException, IOException {
		HttpPost httpPost = new HttpPost(createUri(path, new ArrayList<>()));
		httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		return execute(httpPost);
	}

	@Override
	public List<Speciality> specialities() throws IOException, URISyntaxException {
		return this.<ArrayList<Speciality>>sendGet(createUri("/specialities.json", new ArrayList<>()), new TypeToken<List<Speciality>>() {
		}.getType());
	}

	@Override
	public List<Hospital> hospitals(HospitalsRequestModel requestModel) throws URISyntaxException, IOException {
		URI uri = createUri("/hospitals.json", new ArrayList<>());
		Type type = new TypeToken<List<Hospital>>() {
		}.getType();
		return this.<ArrayList<Hospital>>sendGet(uri, type);
	}

	@Override
	public List<Doctor> doctors(DoctorsRequestModel doctorsRequestModel) throws URISyntaxException, IOException {
		List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair("specialityId", String.valueOf(doctorsRequestModel.getSpecialityId())));
		params.add(new BasicNameValuePair("hospitalId", String.valueOf(doctorsRequestModel.getHospitalId())));
		URI uri = createUri("/doctors.json", params);
		return this.<ArrayList<Doctor>>sendGet(uri, new TypeToken<List<Doctor>>() {
		}.getType());
	}

	@Override
	public List<Date> choseDate(Long doctorId) throws IOException, URISyntaxException {

		return this.sendGet(
				createUri("/doctors/" + doctorId + "/workdays.json", new ArrayList<>()),
				new TypeToken<List<Date>>() {}.getType());
	}

	@Override
	public List<VisitModel> times(IntervalsRequestModel intervalsRequestModel, String sessionId) throws URISyntaxException, IOException {
		ArrayList<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair("doctorId", String.valueOf(intervalsRequestModel.getDoctorId())));
		SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
		params.add(new BasicNameValuePair("date", format.format(intervalsRequestModel.getDate())));
		params.add(new BasicNameValuePair("sessionId", sessionId));

		Type type = new TypeToken<List<VisitModel>>() {}.getType();
		return this.sendGet(createUri("/visits/all.json", params), type);
	}

	@Override
	public void cache(VisitFormRequestModel visitFormRequestModel, String sessionId) throws IOException, URISyntaxException {
		ArrayList<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair("jobIntervalId", String.valueOf(visitFormRequestModel.getJobIntervalId())));
		params.add(new BasicNameValuePair("numberInInterval", String.valueOf(visitFormRequestModel.getNumberInInterval())));
		SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
		params.add(new BasicNameValuePair("date", format.format(visitFormRequestModel.getDate())));
		params.add(new BasicNameValuePair("sessionId", sessionId));
		sendPost("/cache/visits", params);
	}

	@Override
	public long createVisit(VisitCreatingRequestModel visitCreatingRequestModel, String sessionId) throws IOException {
		throw new NotImplementedException();
	}

	@Override
	public TicketModel getTicketModel(Long reservedTimeId) throws IOException, URISyntaxException {
		return this.sendGet(createUri("/visits/" + reservedTimeId + ".json", new ArrayList<>()), TicketModel.class);
	}

	@Override
	public boolean delete(Long reservedTimeId) {
		throw new NotImplementedException();
	}
}
