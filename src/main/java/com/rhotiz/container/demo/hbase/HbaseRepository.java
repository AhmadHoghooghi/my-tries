package com.rhotiz.container.demo.hbase;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.IOException;

@Repository
@ConditionalOnProperty(name = "hbase.interaction.config.enabled", havingValue = "true")
public class HbaseRepository {

    @Autowired
    Connection connection;


    public byte[] readCell(String tableName, String  rowKey, String columnFamily, String qualifier) {
        try (Table table = connection.getTable(TableName.valueOf(tableName))) {
            Get g = new Get(Bytes.toBytes(rowKey));
            Result result = table.get(g);
            return result.getValue(Bytes.toBytes(columnFamily), Bytes.toBytes(qualifier));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void put(String tableName, String rowKey, String columnFamily, String qualifier, String value) {
        try (Table table = connection.getTable(TableName.valueOf(tableName))) {
            Put put = new Put(Bytes.toBytes(rowKey));
            put.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(qualifier),
                    Bytes.toBytes(value));
            table.put(put);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
