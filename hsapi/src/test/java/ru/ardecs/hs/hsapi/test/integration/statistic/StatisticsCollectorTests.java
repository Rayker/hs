package ru.ardecs.hs.hsapi.test.integration.statistic;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import ru.ardecs.hs.hsapi.statistic.StatisticsCollector;
import ru.ardecs.hs.hsapi.test.TestData;
import ru.ardecs.hs.hsapi.test.Mocks;
import ru.ardecs.hs.hsapi.test.TestBeans;
import ru.ardecs.hs.hscommon.soap.generated.SendCityStatisticRequest;

import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestBeans.class, Mocks.class, PersistenceContext.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class,
//		TransactionalTestExecutionListener.class,
		DbUnitTestExecutionListener.class})
public class StatisticsCollectorTests {
	private TestData testData = new TestData();
	@Autowired
	private StatisticsCollector statisticsCollector;

	@Test
	@DatabaseSetup("statisticTestData.xml")
	public void collectStatisticsByDateGroupBySpecialityId_TwoSpecialities_Collect() {
		SendCityStatisticRequest statistics = statisticsCollector.collect(testData.date);
		assertEquals(2, statistics.getSpecialityStatistic().size());

		Map<Integer, Integer> specialityStatistics = statistics
				.getSpecialityStatistic()
				.stream()
				.collect(Collectors.toMap(
						s -> s.getId().intValueExact(),
						s -> s.getVisitsNumber().intValueExact()));
		assertEquals(4, (long) specialityStatistics.get(1));
		assertEquals(3, (long) specialityStatistics.get(2));
	}

	@Test
	public void collectStatisticsByDateGroupBySpecialityId_NoStatistics_Collect() {
		SendCityStatisticRequest statistics = statisticsCollector.collect(testData.date);
		assertEquals(0, statistics.getSpecialityStatistic().size());
	}
}