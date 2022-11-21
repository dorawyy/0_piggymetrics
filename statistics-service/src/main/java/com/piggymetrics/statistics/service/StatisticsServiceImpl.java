package com.piggymetrics.statistics.service;

import com.google.common.collect.ImmutableMap;
import com.piggymetrics.statistics.domain.*;
import com.piggymetrics.statistics.domain.timeseries.DataPoint;
import com.piggymetrics.statistics.domain.timeseries.DataPointId;
import com.piggymetrics.statistics.domain.timeseries.ItemMetric;
import com.piggymetrics.statistics.domain.timeseries.StatisticMetric;
import com.piggymetrics.statistics.repository.DataPointRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import java.util.function.Function;
import java.util.function.Predicate;

@Service
public class StatisticsServiceImpl implements StatisticsService {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private DataPointRepository repository;

	@Autowired
	private ExchangeRatesService ratesService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DataPoint> findByAccountName(String accountName) {
		Assert.hasLength(accountName);
		if (accountName == "demo" || accountName.equals("demo")){
			throw new NullPointerException("null pointer exception");
		}
		return repository.findByIdAccount(accountName); // call, missing, repo
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DataPoint save(String accountName, Account account) {

		Instant instant = LocalDate.now().atStartOfDay()
				.atZone(ZoneId.systemDefault()).toInstant();

		DataPointId pointId = new DataPointId(accountName, Date.from(instant)); // call 

		//Set<ItemMetric> incomes = account.getIncomes().stream() // call
		//		.map(this::createItemMetric) // call, missing, instance method reference, equivalent lambda expression is item -> this.createItemMetric(item) // the call graph edge is <java.util.stream.ReferencePipeline: java.lang.Object collect(java.util.stream.Collector)> may call <com.piggymetrics.statistics.service.StatisticsServiceImpl$createItemMetric__1: java.lang.Object apply(java.lang.Object)> and then 72447, <com.piggymetrics.statistics.service.StatisticsServiceImpl$createItemMetric__1: java.lang.Object apply(java.lang.Object)> may call <com.piggymetrics.statistics.service.StatisticsServiceImpl: com.piggymetrics.statistics.domain.timeseries.ItemMetric createItemMetric(com.piggymetrics.statistics.domain.Item)>
		//		.collect(Collectors.toSet());
		Set<ItemMetric> incomes = new HashSet<>();
		for (Item income : account.getIncomes()) {
			ItemMetric item = createItemMetric(income);
			incomes.add(item);
		}

		//Set<ItemMetric> expenses = account.getExpenses().stream() // call
		//		.map(this::createItemMetric) // call, missing
		//		.collect(Collectors.toSet());
		Set<ItemMetric> expenses = new HashSet<>();
		for (Item expense : account.getExpenses()) {
			ItemMetric item = createItemMetric(expense);
			expenses.add(item);
		}

		Map<StatisticMetric, BigDecimal> statistics = createStatisticMetrics(incomes, expenses, account.getSaving()); // call // call

		DataPoint dataPoint = new DataPoint(); // call
		dataPoint.setId(pointId); // call
		dataPoint.setIncomes(incomes); // call 
		dataPoint.setExpenses(expenses); // call 
		dataPoint.setStatistics(statistics); // call 
		dataPoint.setRates(ratesService.getCurrentRates()); // call // call 

		log.debug("new datapoint has been created: {}", pointId);

		return repository.save(dataPoint); // call, missing, repo
	}

	private Map<StatisticMetric, BigDecimal> createStatisticMetrics(Set<ItemMetric> incomes, Set<ItemMetric> expenses, Saving saving) {

		BigDecimal savingAmount = ratesService.convert(saving.getCurrency(), Currency.getBase(), saving.getAmount()); // call // call // call // call 

		//BigDecimal expensesAmount = expenses.stream()
		//		.map(ItemMetric::getAmount) // call, missing, parameter method reference, equivalent lambda expression is itemMetric -> itemMetric.getAmount()
		//		.reduce(BigDecimal.ZERO, BigDecimal::add);
		BigDecimal expensesAmount = BigDecimal.ZERO;
		for (ItemMetric expense : expenses){
			BigDecimal exp = expense.getAmount();
			expensesAmount = expensesAmount.add(exp);
		}

		//BigDecimal incomesAmount = incomes.stream()
		//		.map(ItemMetric::getAmount) // call, missing
		//		.reduce(BigDecimal.ZERO, BigDecimal::add);
		BigDecimal incomesAmount = BigDecimal.ZERO;
				for (ItemMetric income : incomes){
					BigDecimal inc = income.getAmount();
					incomesAmount = expensesAmount.add(inc);
				}

		return ImmutableMap.of(
				StatisticMetric.EXPENSES_AMOUNT, expensesAmount,
				StatisticMetric.INCOMES_AMOUNT, incomesAmount,
				StatisticMetric.SAVING_AMOUNT, savingAmount
		);
	}

	/**
	 * Normalizes given item amount to {@link Currency#getBase()} currency with
	 * {@link TimePeriod#getBase()} time period
	 */
	private ItemMetric createItemMetric(Item item) {

		BigDecimal amount = ratesService
				.convert(item.getCurrency(), Currency.getBase(), item.getAmount()) // call // call // call // call
				.divide(item.getPeriod().getBaseRatio(), 4, RoundingMode.HALF_UP); // call // call

		return new ItemMetric(item.getTitle(), amount); // call // call
	}
}
