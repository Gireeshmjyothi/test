dependencies {
    implementation 'org.apache.spark:spark-core_2.13:3.5.1'
    implementation 'org.apache.spark:spark-sql_2.13:3.5.1'
    implementation 'com.zaxxer:HikariCP:5.0.0'  // HikariCP connection pool
    implementation 'org.postgresql:postgresql:42.3.5'  // PostgreSQL JDBC driver
}

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class DataSourceManager {

    private static HikariDataSource dataSource;

    public static DataSource getDataSource() {
        if (dataSource == null) {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:postgresql://pg-36cdad80-rajput-d178.f.aivencloud.com:10052/defaultdb");
            config.setUsername("avnadmin");
            config.setPassword("AVNS__gIHpnNG1mpYDSlt9pT");
            config.setDriverClassName("org.postgresql.Driver");
            config.setMaximumPoolSize(10);  // Set the pool size based on your requirement
            config.setMinimumIdle(5);  // Set minimum idle connections
            config.setIdleTimeout(30000);  // Set idle timeout
            config.setConnectionTimeout(20000);  // Set connection timeout

            dataSource = new HikariDataSource(config);
        }
        return dataSource;
    }

    // Method to close the DataSource when the application shuts down
    public static void close() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class SparkDatabaseService {

    private static final SparkSession sparkSession = SparkSession.builder()
            .appName("SparkDatabaseApp")
            .master("local[*]")
            .getOrCreate();

    private static final DataSource dataSource = DataSourceManager.getDataSource();

    public Dataset<Row> getDbDataSet() {
        // Obtain a connection from HikariCP pool
        try (Connection connection = dataSource.getConnection()) {
            Properties connectionProperties = new Properties();
            connectionProperties.put("user", "avnadmin");
            connectionProperties.put("password", "AVNS__gIHpnNG1mpYDSlt9pT");

            // Load dataset from PostgreSQL using HikariCP connection pool
            String table = "employee";
            return sparkSession.read()
                    .jdbc("jdbc:postgresql://pg-36cdad80-rajput-d178.f.aivencloud.com:10052/defaultdb", 
                        table, 
                        connectionProperties);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
