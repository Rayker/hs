package ru.ardecs.hs.hsclient.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
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
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// TODO: 7/1/16 refactor
public class ApiWrapperImpl implements ApiWrapper {
	private final CloseableHttpClient httpClient;
	private final String host;
	private final int port;

	public ApiWrapperImpl(CloseableHttpClient httpClient, String host, int port) {
		this.httpClient = httpClient;
		this.host = host;
		this.port = port;
	}

	private URI createUri(String path, List<NameValuePair> nvps) throws URISyntaxException {
		return new URIBuilder()
				.setScheme("http")
				.setHost(host)
				.setPort(port)
				.setPath(path)
				.addParameters(nvps)
				.build();
	}

	private <T> T sendGetAndParseResponse(String path, List<NameValuePair> nvps, Type type) throws IOException, URISyntaxException {
		HttpGet httpGet = createGetRequest(path, nvps);
		CloseableHttpResponse response = httpClient.execute(httpGet);
		return parse(response, type);
	}

	private <T> T parse(CloseableHttpResponse response, Type type) throws IOException {
		BufferedReader responseReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		return new Gson().fromJson(responseReader, type);
	}

	private CloseableHttpResponse sendPost(String path, List<NameValuePair> nvps) throws URISyntaxException, IOException {
		HttpPost httpPost = createPostRequest(path, nvps);
		return httpClient.execute(httpPost);
	}

	private HttpPost createPostRequest(String path, List<NameValuePair> nvps) throws URISyntaxException, UnsupportedEncodingException {
		HttpPost httpPost = new HttpPost(createUri(path, new ArrayList<>()));
		httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		return httpPost;
	}

	private HttpGet createGetRequest(String path, List<NameValuePair> nvps) throws URISyntaxException, UnsupportedEncodingException {
		return new HttpGet(createUri(path, nvps));
	}

	@Override
	public List<Speciality> specialities() throws IOException, URISyntaxException {
		return this.<ArrayList<Speciality>>sendGetAndParseResponse("/specialities.json", new ArrayList<>(), new TypeToken<List<Speciality>>() {
		}.getType());
	}

	@Override
	public List<Hospital> hospitals(HospitalsRequestModel requestModel) throws URISyntaxException, IOException {
		List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair("specialityId", String.valueOf(requestModel.getSpecialityId())));
		Type type = new TypeToken<List<Hospital>>() {
		}.getType();
		return this.<ArrayList<Hospital>>sendGetAndParseResponse("/hospitals.json", params, type);
	}

	@Override
	public List<Doctor> doctors(DoctorsRequestModel doctorsRequestModel) throws URISyntaxException, IOException {
		List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair("specialityId", String.valueOf(doctorsRequestModel.getSpecialityId())));
		params.add(new BasicNameValuePair("hospitalId", String.valueOf(doctorsRequestModel.getHospitalId())));
		return this.<ArrayList<Doctor>>sendGetAndParseResponse("/doctors.json", params, new TypeToken<List<Doctor>>() {
		}.getType());
	}

	@Override
	public List<Date> choseDate(Long doctorId) throws IOException, URISyntaxException {
		return this.sendGetAndParseResponse(
				"/doctors/" + doctorId + "/workdays.json",
				new ArrayList<>(),
				new TypeToken<List<Date>>() {
				}.getType());
	}

	@Override
	public List<VisitModel> times(IntervalsRequestModel intervalsRequestModel, String sessionId) throws URISyntaxException, IOException {
		ArrayList<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair("doctorId", String.valueOf(intervalsRequestModel.getDoctorId())));
		SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
		params.add(new BasicNameValuePair("date", format.format(intervalsRequestModel.getDate())));
		params.add(new BasicNameValuePair("sessionId", sessionId));

		Type type = new TypeToken<List<VisitModel>>() {
		}.getType();
		return this.sendGetAndParseResponse("/visits/all.json", params, type);
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
	public Long createVisit(VisitCreatingRequestModel visitCreatingRequestModel, String sessionId) throws IOException, URISyntaxException {
		ArrayList<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair("jobIntervalId", String.valueOf(visitCreatingRequestModel.getJobIntervalId())));
		params.add(new BasicNameValuePair("numberInInterval", String.valueOf(visitCreatingRequestModel.getNumberInInterval())));
		params.add(new BasicNameValuePair("visitorName", String.valueOf(visitCreatingRequestModel.getVisitorName())));
		SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
		params.add(new BasicNameValuePair("visitorBirthday", format.format(visitCreatingRequestModel.getVisitorBirthday())));
		params.add(new BasicNameValuePair("date", format.format(visitCreatingRequestModel.getDate())));
		params.add(new BasicNameValuePair("sessionId", sessionId));
		return parse(sendPost("/visits", params), Long.class);
	}

	@Override
	public TicketModel getTicketModel(Long reservedTimeId) throws IOException, URISyntaxException {
		return this.sendGetAndParseResponse("/visits/" + reservedTimeId + "/ticket.json", new ArrayList<>(), TicketModel.class);
	}

	@Override
	public boolean delete(Long reservedTimeId) {
		throw new NotImplementedException();
	}
}
