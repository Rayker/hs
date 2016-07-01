package ru.ardecs.hs.hsclient;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.SerializableEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ardecs.hs.hscommon.entities.Doctor;
import ru.ardecs.hs.hscommon.entities.Hospital;
import ru.ardecs.hs.hscommon.entities.ReservedTime;
import ru.ardecs.hs.hscommon.entities.Speciality;
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

	private <T> T getValue(URI uri, Type type) throws IOException {
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

	private CloseableHttpResponse sendPost(String path, Serializable ser) throws URISyntaxException, IOException {
		HttpPost httpPost = new HttpPost(createUri(path, new ArrayList<>()));
		httpPost.setEntity(new SerializableEntity(ser));
		return execute(httpPost);
	}

	@Override
	public List<Speciality> specialities() throws IOException {
		return this.<ArrayList<Speciality>>getValue(URI.create("http://localhost:8090/specialities.json"), new TypeToken<List<Speciality>>() {
		}.getType());
	}

	@Override
	public List<Hospital> hospitals(HospitalsRequestModel requestModel) throws URISyntaxException, IOException {
		URI uri = new URIBuilder("http://localhost/hospitals.json")
				.setPort(8090)
				.addParameter("specialityId", String.valueOf(requestModel.getSpecialityId()))
				.build();

		return this.<ArrayList<Hospital>>getValue(uri, new TypeToken<List<Hospital>>() {
		}.getType());
	}

	@Override
	public List<Doctor> doctors(DoctorsRequestModel doctorsRequestModel) throws URISyntaxException, IOException {
		URI uri = new URIBuilder("http://localhost:8090/doctors.json")
				.addParameter("specialityId", String.valueOf(doctorsRequestModel.getSpecialityId()))
				.addParameter("hospitalId", String.valueOf(doctorsRequestModel.getHospitalId()))
				.build();
		return this.<ArrayList<Doctor>>getValue(uri, new TypeToken<List<Doctor>>() {
		}.getType());
	}

	@Override
	public List<Date> choseDate(Long doctorId) throws IOException {
		return this.getValue(
				URI.create("http://localhost:8090/doctors/" + doctorId + "/workdays.json"),
				new TypeToken<List<Date>>() {}.getType());
	}

	@Override
	public List<VisitModel> times(IntervalsRequestModel intervalsRequestModel, String sessionId) {
		throw new NotImplementedException();
	}

	@Override
	public void cache(VisitFormRequestModel visitFormRequestModel, String sessionId) {
		throw new NotImplementedException();
	}

	@Override
	public long createVisit(VisitCreatingRequestModel visitCreatingRequestModel, String sessionId) throws IOException {
		throw new NotImplementedException();
	}

	@Override
	public ReservedTime getReservedTime(Long reservedTimeId) throws IOException {
		throw new NotImplementedException();
	}

	@Override
	public boolean delete(Long reservedTimeId) {
		throw new NotImplementedException();
	}
}
