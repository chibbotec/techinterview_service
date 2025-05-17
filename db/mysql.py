
import mysql.connector
import os
from dotenv import load_dotenv
from mysql.connector import Error

load_dotenv()

class MySQLConnection:
    _instance = None

    def __new__(cls):
        if cls._instance is None:
            cls._instance = super(MySQLConnection, cls).__new__(cls)
            cls._instance.connection = None
        return cls._instance

    def __init__(self):
        if not self.connection:
            self.connect()

    def connect(self):
        try:
            self.connection = mysql.connector.connect(
                host=os.getenv('MYSQL_HOST'),
                port=int(os.getenv('MYSQL_PORT', 3306)),
                user=os.getenv('MYSQL_USER', 'root'),
                password=os.getenv('MYSQL_PASSWORD', 'root123414'),
                database=os.getenv('MYSQL_DB_NAME', 'chibbo_interview'),
                connection_timeout=60,  # 연결 타임아웃 증가
                pool_reset_session=True  # 세션 재설정 활성화
            )
            print("MySQL 데이터베이스 연결 성공")
        except Error as e:
            print(f"Error: MySQL 연결 실패: {e}")
            raise e

    def get_connection(self):
        try:
            # 연결이 유효한지 확인 - 핵심 수정 부분!
            if self.connection and self.connection.is_connected():
                # 필요시 연결 확인 (ping)
                self.connection.ping(reconnect=True, attempts=3, delay=1)
                return self.connection
            else:
                # 연결이 닫혀 있으면 재연결
                print("MySQL 연결이 닫혀 있어 재연결 시도합니다.")
                self.connect()
                return self.connection
        except Error as e:
            print(f"Error: 연결 확인 중 오류 발생: {e}")
            # 연결에 문제가 있으면 재연결
            self.connect()
            return self.connection

    def close_connection(self):
        if self.connection and self.connection.is_connected():
            self.connection.close()
            print("MySQL 연결이 종료되었습니다.")

    def execute_query(self, query, params=None):
        cursor = None
        try:
            # 연결 상태 확인 및 필요시 재연결
            if not self.connection or not self.connection.is_connected():
                self.connect()
            
            cursor = self.connection.cursor(dictionary=True)
            cursor.execute(query, params)
            
            # SELECT 쿼리인 경우
            if query.strip().upper().startswith('SELECT'):
                result = cursor.fetchall()
                return result
            # INSERT, UPDATE, DELETE 쿼리인 경우
            else:
                self.connection.commit()
                return cursor.rowcount
                
        except Error as e:
            print(f"Error: 쿼리 실행 실패: {e}")
            if self.connection:
                self.connection.rollback()
            
            # 연결 오류일 경우 재연결 시도 후 다시 실행
            if "MySQL Connection not available" in str(e) or "Lost connection" in str(e):
                print("연결이 끊어졌습니다. 재연결 시도...")
                self.connect()
                return self.execute_query(query, params)  # 재귀적으로 다시 시도
            
            raise e
        finally:
            if cursor:
                cursor.close()
