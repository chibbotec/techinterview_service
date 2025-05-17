package com.ll.techinterview.global.config;

import com.ll.techinterview.global.monitoring.JPAQueryMetricsListener;
import lombok.RequiredArgsConstructor;
import net.ttddyy.dsproxy.listener.ChainListener;
import net.ttddyy.dsproxy.listener.DataSourceQueryCountListener;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
@RequiredArgsConstructor
public class DataSourceConfig {

  private final JPAQueryMetricsListener jpaQueryMetricsListener;

  @Bean
  @Primary
  public DataSource dataSource(DataSourceProperties properties) throws SQLException {
    final DataSource originalDataSource = properties.initializeDataSourceBuilder().build();

    // 리스너 체인 생성
    ChainListener chainListener = new ChainListener();
    chainListener.addListener(new DataSourceQueryCountListener());
    chainListener.addListener(jpaQueryMetricsListener);

    // 프록시 데이터소스 생성
    return ProxyDataSourceBuilder
        .create(originalDataSource)
        .name("MonitoredDataSource")
        .listener(chainListener)
        .countQuery()
        .multiline()
        .build();
  }
}