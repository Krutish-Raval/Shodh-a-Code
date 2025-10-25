package com.shodh.shodh_a_code.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class MongoConnectionTest {

    private static final Logger logger = LoggerFactory.getLogger(MongoConnectionTest.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    public boolean testConnection() {
        try {
            // Test connection by getting database name
            String dbName = mongoTemplate.getDb().getName();
            logger.info("✅ Successfully connected to MongoDB Atlas database: {}", dbName);

            // Test by getting collection names
            var collections = mongoTemplate.getCollectionNames();
            logger.info("📊 Available collections: {}", collections);

            return true;
        } catch (Exception e) {
            logger.error("❌ Failed to connect to MongoDB Atlas: {}", e.getMessage());
            return false;
        }
    }

    public void logConnectionInfo() {
        // try {
        // String dbName = mongoTemplate.getDb().getName();
        // String host =
        // mongoTemplate.getDb().getMongoClient().getClusterDescription().getServerDescriptions()
        // .iterator().next().getAddress().toString();

        // logger.info("🔗 MongoDB Atlas Connection Info:");
        // logger.info(" Database: {}", dbName);
        // logger.info(" Host: {}", host);
        // logger.info(" Status: Connected ✅");
        // } catch (Exception e) {
        // logger.error("Failed to get connection info: {}", e.getMessage());
        // }
    }
}
