package com.rhotiz.container.demo.hbase;

import jakarta.annotation.PostConstruct;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.TableNotFoundException;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@ConditionalOnProperty(name = "hbase.interaction.config.enabled", havingValue = "true")
public class HbaseService {
    Logger logger = LoggerFactory.getLogger(HbaseService.class);

    @PostConstruct
    void a() throws IOException {
        Connection conn = null;
        try {
            Configuration conf = HBaseConfiguration.create();
            conf.set("hbase.zookeeper.quorum", "127.0.0.1");
            conf.set("hbase.zookeeper.property.clientPort", "2181"); //2181 is the default port
            conn = ConnectionFactory.createConnection(conf);
            Table table = null;
            try {
                table = conn.getTable(TableName.valueOf("test"));
                Get g = new Get(Bytes.toBytes("row1"));
                Result result = table.get(g);
                System.out.println(result);
                byte[] value = result.getValue(Bytes.toBytes("cf"), Bytes.toBytes("a"));
                System.out.println("value: " + Bytes.toString(value));
            } catch (TableNotFoundException tnfe) {
                throw new RuntimeException("HBase table not found.", tnfe);
            } catch (IOException ioe) {
                throw new RuntimeException("Cannot create connection to HBase.", ioe);
            } finally {
                if (table != null) {
                    try {
                        table.close();
                    } catch (IOException e) {
                        logger.error("Error while closing table", e);
                    }
                }
            }

        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (IOException e) {
                    logger.error("Error while closing connection", e);
                }
            }
        }
    }

//    void b(){
//        TableDescriptorBuilder
//                .newBuilder(TableName.valueOf("my-table"))
//                .setColumnFamily(new ColumnFamilyDescriptor())
//                .build();
//
//
//        HTableDescriptor desc = new HTableDescriptor(table1);
//        desc.addFamily(new HColumnDescriptor(family1));
//        desc.addFamily(new HColumnDescriptor(family2));
//        admin.createTable(desc);
//    }
}
