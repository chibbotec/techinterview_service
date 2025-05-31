package com.ll.techinterview.global.monitoring;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import net.ttddyy.dsproxy.ExecutionInfo;
import net.ttddyy.dsproxy.QueryInfo;
import net.ttddyy.dsproxy.listener.QueryExecutionListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * 데이터 소스 프록시를 통해 SQL 쿼리를 모니터링하고 로그를 수집하는 리스너
 */
@Slf4j
@Component
public class P6SpyEventListener implements QueryExecutionListener {

  private final RestTemplate restTemplate;

  private static final Pattern SELECT_PATTERN = Pattern.compile("^SELECT", Pattern.CASE_INSENSITIVE);
  private static final Pattern INSERT_PATTERN = Pattern.compile("^INSERT", Pattern.CASE_INSENSITIVE);
  private static final Pattern UPDATE_PATTERN = Pattern.compile("^UPDATE", Pattern.CASE_INSENSITIVE);
  private static final Pattern DELETE_PATTERN = Pattern.compile("^DELETE", Pattern.CASE_INSENSITIVE);

  private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

  public P6SpyEventListener(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  @Override
  public void beforeQuery(ExecutionInfo executionInfo, java.util.List<QueryInfo> queryInfoList) {
    // 쿼리 실행 전 처리 (필요시 구현)
  }

  @Override
  public void afterQuery(ExecutionInfo executionInfo, java.util.List<QueryInfo> queryInfoList) {
    // 쿼리 실행 시간을 밀리초 단위로 변환
    long executionTimeMs = executionInfo.getElapsedTime();

    if (queryInfoList.isEmpty()) {
      return;
    }

    QueryInfo queryInfo = queryInfoList.get(0);
    String query = queryInfo.getQuery().trim();

    // 쿼리 타입 감지
    String queryType = determineQueryType(query);

    // 현재 시간을 포맷팅
    String formattedTime = LocalDateTime.now().format(TIME_FORMATTER);

    // 간단한 로깅
    log.debug("SQL: {}, Type: {}, Time: {}ms",
        query.substring(0, Math.min(query.length(), 100)),
        queryType,
        executionTimeMs);

    // Admin 서비스로 로그 전송
    sendQueryLogToAdminService(formattedTime, "techinterview", queryType, executionTimeMs, formatSqlForDisplay(query));
  }

  /**
   * SQL 쿼리의 타입을 결정합니다 (SELECT, INSERT, UPDATE, DELETE 등)
   */
  private String determineQueryType(String query) {
    if (SELECT_PATTERN.matcher(query).find()) {
      return "SELECT";
    } else if (INSERT_PATTERN.matcher(query).find()) {
      return "INSERT";
    } else if (UPDATE_PATTERN.matcher(query).find()) {
      return "UPDATE";
    } else if (DELETE_PATTERN.matcher(query).find()) {
      return "DELETE";
    } else {
      return "OTHER";
    }
  }

  /**
   * 표시를 위해 SQL 쿼리를 포맷팅합니다 (길이 제한, 민감 정보 처리 등)
   */
  private String formatSqlForDisplay(String sql) {
    // SQL 길이가 긴 경우 잘라냄
    if (sql.length() > 200) {
      return sql.substring(0, 200) + "...";
    }
    return sql;
  }

  /**
   * Admin 서비스로 쿼리 로그를 전송합니다.
   */
  private void sendQueryLogToAdminService(String time, String service, String type,
      long executionTime, String sql) {
    try {
      Map<String, Object> logData = new HashMap<>();
      logData.put("source", "p6spy");
      logData.put("time", time);
      logData.put("service", service);
      logData.put("type", type);
      logData.put("executionTime", executionTime);
      logData.put("sql", sql);

      // 디버그 로깅 추가
      log.debug("Sending query log to Admin service: {}", logData);

      // Admin 서비스로 POST 요청 보내기
//      restTemplate.postForEntity("http://172.30.1.23:9100/api/metrics/log", logData, Void.class);

      log.info("Successfully sent query log to Admin service====================================");
    } catch (Exception e) {
      log.error("Admin 서비스에 로그 전송 실패: {}", e.getMessage(), e);
    }
  }
}