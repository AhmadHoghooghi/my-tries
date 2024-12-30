package com.rhotiz.container.demo.hbase;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class HbaseAdminService {

    Logger logger = LoggerFactory.getLogger(HbaseAdminService.class);

    @Autowired
    Connection connection;


    void createTable(String tableNameString) {
        try (Admin admin = connection.getAdmin()) {
            TableName tableName = TableName.valueOf(tableNameString);
            boolean isAvailable = admin.isTableAvailable(tableName);
            if (!isAvailable) {
                TableDescriptor tableDescriptor = TableDescriptorBuilder.newBuilder(tableName)
                        .setColumnFamily(
                                ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes("cf"))
                                        .build())
                        .build();
                admin.createTable(tableDescriptor);
                logger.info("Table {} created successfully", tableNameString);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void deleteTableIfExists(String tableNameString) {
        try (Admin admin = connection.getAdmin()) {
            TableName tableName = TableName.valueOf(tableNameString);
            boolean isAvailable = admin.isTableAvailable(tableName);
            if (isAvailable) {
                admin.disableTable(tableName);
                admin.deleteTable(tableName);
                logger.info("table {} deleted successfully", tableNameString);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
