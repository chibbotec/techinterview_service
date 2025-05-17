package com.ll.techinterview.global.monitoring;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;
import net.ttddyy.dsproxy.ExecutionInfo;
import net.ttddyy.dsproxy.QueryInfo;
import net.ttddyy.dsproxy.listener.QueryExecutionListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@Slf4j
@Component
public class JPAQueryMetricsListener implements QueryExecutionListener {

  private final Counter selectCounter;
  private final Counter insertCounter;
  private final Counter updateCounter;
  private final Counter deleteCounter;
  private final Counter otherCounter;
  private final Timer queryTimer;

  private static final Pattern SELECT_PATTERN = Pattern.compile("^SELECT", Pattern.CASE_INSENSITIVE);
  private static final Pattern INSERT_PATTERN = Pattern.compile("^INSERT", Pattern.CASE_INSENSITIVE);
  private static final Pattern UPDATE_PATTERN = Pattern.compile("^UPDATE", Pattern.CASE_INSENSITIVE);
  private static final Pattern DELETE_PATTERN = Pattern.compile("^DELETE", Pattern.CASE_INSENSITIVE);

  public JPAQueryMetricsListener(MeterRegistry meterRegistry) {
    this.selectCounter = Counter.builder("jpa.query.select")
        .description("SELECT 쿼리 수")
        .register(meterRegistry);

    this.insertCounter = Counter.builder("jpa.query.insert")
        .description("INSERT 쿼리 수")
        .register(meterRegistry);

    this.updateCounter = Counter.builder("jpa.query.update")
        .description("UPDATE 쿼리 수")
        .register(meterRegistry);

    this.deleteCounter = Counter.builder("jpa.query.delete")
        .description("DELETE 쿼리 수")
        .register(meterRegistry);

    this.otherCounter = Counter.builder("jpa.query.other")
        .description("기타 쿼리 수")
        .register(meterRegistry);

    this.queryTimer = Timer.builder("jpa.query.execution.time")
        .description("쿼리 실행 시간")
        .register(meterRegistry);
  }

  @Override
  public void beforeQuery(ExecutionInfo executionInfo, List<QueryInfo> list) {
    // 쿼리 실행 전 로직 (필요시 구현)
  }

  @Override
  public void afterQuery(ExecutionInfo executionInfo, List<QueryInfo> queryInfoList) {
    // 쿼리 실행 시간 측정
    long elapsedTime = executionInfo.getElapsedTime();
    queryTimer.record(elapsedTime, TimeUnit.MILLISECONDS);

    // 쿼리 종류별 카운터 증가
    if (queryInfoList.isEmpty()) {
      return;
    }

    String query = queryInfoList.get(0).getQuery().trim();

    if (SELECT_PATTERN.matcher(query).find()) {
      selectCounter.increment();
      logQueryMetric("SELECT", elapsedTime);
    } else if (INSERT_PATTERN.matcher(query).find()) {
      insertCounter.increment();
      logQueryMetric("INSERT", elapsedTime);
    } else if (UPDATE_PATTERN.matcher(query).find()) {
      updateCounter.increment();
      logQueryMetric("UPDATE", elapsedTime);
    } else if (DELETE_PATTERN.matcher(query).find()) {
      deleteCounter.increment();
      logQueryMetric("DELETE", elapsedTime);
    } else {
      otherCounter.increment();
      logQueryMetric("OTHER", elapsedTime);
    }
  }

  private void logQueryMetric(String queryType, long elapsedTime) {
    if (elapsedTime > 100) { // 100ms 이상 걸린 쿼리만 로깅
      log.warn("🔍 Slow {} query detected - took {} ms", queryType, elapsedTime);
    } else {
      log.debug("✅ {} query executed in {} ms", queryType, elapsedTime);
    }
  }
}