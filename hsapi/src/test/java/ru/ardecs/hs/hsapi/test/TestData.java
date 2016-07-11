package ru.ardecs.hs.hsapi.test;

import ru.ardecs.hs.hsapi.cache.CachedVisit;

import java.util.Date;

public class TestData {
	public final String firstSessionId = "firstSessionId";
	public final String secondSessionId = "secondSessionId";
	public final String thirdSessionId = "thirdSessionId";
	public final Date date = new Date();
	public final long doctorId = 1L;
	public final CachedVisit firstVisit = new CachedVisit(doctorId, 1L, 0, date);
	public final CachedVisit secondVisit = new CachedVisit(doctorId, 1L, 1, date);

}
