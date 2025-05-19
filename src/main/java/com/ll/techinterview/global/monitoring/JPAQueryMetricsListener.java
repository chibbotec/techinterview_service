package com.ll.techinterview.global.monitoring;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;
import net.ttddyy.dsproxy.ExecutionInfo;
import net.ttddyy.dsproxy.QueryInfo;
import net.ttddyy.dsproxy.listener.QueryExecutionListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@Slf4j
@Component
public class JPAQueryMetricsListener implements QueryExecutionListener {

  private final RestTemplate restTemplate;
  private final Counter selectCounter;
  private final Counter insertCounter;
  private final Counter updateCounter;
  private final Counter deleteCounter;
  private final Counter otherCounter;
  private final Timer queryTimer;

  // 추가: 쿼리 시간 분포별 카운터
  private final Counter fastQueryCounter;
  private final Counter mediumQueryCounter;
  private final Counter slowQueryCounter;
  private final Counter verySlowQueryCounter;

  private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

  private static final Pattern SELECT_PATTERN = Pattern.compile("^SELECT", Pattern.CASE_INSENSITIVE);
  private static final Pattern INSERT_PATTERN = Pattern.compile("^INSERT", Pattern.CASE_INSENSITIVE);
  private static final Pattern UPDATE_PATTERN = Pattern.compile("^UPDATE", Pattern.CASE_INSENSITIVE);
  private static final Pattern DELETE_PATTERN = Pattern.compile("^DELETE", Pattern.CASE_INSENSITIVE);

  public JPAQueryMetricsListener(MeterRegistry meterRegistry, RestTemplate restTemplate) {
    this.restTemplate = restTemplate;

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

    // 추가: 쿼리 시간 분포별 카운터 등록
    this.fastQueryCounter = Counter.builder("jpa.query.time.fast")
        .description("빠른 쿼리 (10ms 미만)")
        .register(meterRegistry);

    this.mediumQueryCounter = Counter.builder("jpa.query.time.medium")
        .description("보통 쿼리 (10-100ms)")
        .register(meterRegistry);

    this.slowQueryCounter = Counter.builder("jpa.query.time.slow")
        .description("느린 쿼리 (100-500ms)")
        .register(meterRegistry);

    this.verySlowQueryCounter = Counter.builder("jpa.query.time.very_slow")
        .description("매우 느린 쿼리 (500ms 이상)")
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
    String queryType = "OTHER";

    // 쿼리 타입 판별
    if (SELECT_PATTERN.matcher(query).find()) {
      selectCounter.increment();
      queryType = "SELECT";
    } else if (INSERT_PATTERN.matcher(query).find()) {
      insertCounter.increment();
      queryType = "INSERT";
    } else if (UPDATE_PATTERN.matcher(query).find()) {
      updateCounter.increment();
      queryType = "UPDATE";
    } else if (DELETE_PATTERN.matcher(query).find()) {
      deleteCounter.increment();
      queryType = "DELETE";
    } else {
      otherCounter.increment();
    }

    // 추가: 쿼리 시간 분포 측정
    if (elapsedTime < 10) {
      fastQueryCounter.increment();
    } else if (elapsedTime < 100) {
      mediumQueryCounter.increment();
    } else if (elapsedTime < 500) {
      slowQueryCounter.increment();
    } else {
      verySlowQueryCounter.increment();
    }

    // 로깅 및 Admin 서비스로 로그 전송
    logQueryMetric(queryType, elapsedTime, query);
  }

  private void logQueryMetric(String queryType, long elapsedTime, String query) {
    String formattedTime = LocalDateTime.now().format(TIME_FORMATTER);

    // Admin 서비스로 쿼리 로그 전송 (HTTP 요청)
    try {
      // 로컬 호스트나 Docker 네트워크 이름 사용 (Docker 환경에 맞게 조정 필요)
      String adminServiceUrl = "http://admin:9100/api/metrics/log";

      // REST 클라이언트를 사용하여 로그 정보 전송
      sendQueryLogToAdminService(formattedTime, "techinterview", queryType, elapsedTime,
          query.length() > 200 ? query.substring(0, 200) + "..." : query);
    } catch (Exception e) {
      log.error("Admin 서비스에 쿼리 로그 전송 실패: {}", e.getMessage());
    }

    // 로컬 로깅도 수행
    if (elapsedTime > 100) { // 100ms 이상 걸린 쿼리만 로깅
      log.warn("🔍 Slow {} query detected - took {} ms", queryType, elapsedTime);
    } else {
      log.debug("✅ {} query executed in {} ms", queryType, elapsedTime);
    }
  }

  // Admin 서비스로 로그 전송 메서드 구현
  private void sendQueryLogToAdminService(String time, String service, String type,
      long executionTime, String sql) {
    try {
      Map<String, Object> logData = new HashMap<>();
      logData.put("time", time);
      logData.put("service", service);
      logData.put("type", type);
      logData.put("executionTime", executionTime);
      logData.put("sql", sql);

      // Admin 서비스로 POST 요청 보내기
      restTemplate.postForEntity("http://admin:9100/api/metrics/log", logData, Void.class);
    } catch (Exception e) {
      log.error("Admin 서비스에 로그 전송 실패: {}", e.getMessage());
    }
  }
}