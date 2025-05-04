package org.example.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

@Configuration
public class DataSourceInitializerConfig {

    @Value("${spring.sql.init.mode}")
    private String initMode;

    /**
     * Configures a DataSourceInitializer to run SQL scripts for database initialization if needed.
     * This is controlled by the spring.sql.init.mode property in application.properties.
     */
    @Bean
    public DataSourceInitializer dataSourceInitializer(DataSource dataSource) {
        DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);

        if ("always".equals(initMode)) {
            ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
            
            // Add initialization scripts if they exist in the classpath
            try {
                ClassPathResource schemaScript = new ClassPathResource("schema.sql");
                if (schemaScript.exists()) {
                    populator.addScript(schemaScript);
                }
                
                ClassPathResource dataScript = new ClassPathResource("data.sql");
                if (dataScript.exists()) {
                    populator.addScript(dataScript);
                }
                
                initializer.setDatabasePopulator(populator);
            } catch (Exception e) {
                System.err.println("Warning: Could not find initialization scripts: " + e.getMessage());
            }
        }

        return initializer;
    }
}