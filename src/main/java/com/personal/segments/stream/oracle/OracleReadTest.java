package com.personal.segments.stream.oracle;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 普通单线程读取处理数据库Resultset
 * 
 * @author ke
 *
 */
public class OracleReadTest {

	private static final String DRIVER_NAME = "oracle.jdbc.driver.OracleDriver";
	private static final String SID_CONN = "jdbc:oracle:thin:@%s:%s:%s";
	private static final String SELECT_TABLE = "SELECT * FROM JIGEW";

	public static void main(String[] args) throws Exception {

		Class.forName(DRIVER_NAME);
		String url = String.format(SID_CONN, "192.168.188.151", "1521", "DBONE");
		Connection connection = DriverManager.getConnection(url, "scott", "tiger");
		readTest(connection);
		connection.close();
	}

	private static void readTest(Connection connection) throws Exception {
		long start = System.currentTimeMillis();
		ResultSet resultSet = connection.createStatement().executeQuery(SELECT_TABLE);
		ResultSetMetaData metaData = resultSet.getMetaData();
		String[] row = new String[metaData.getColumnCount()];
		int columns = metaData.getColumnCount();
		resultSet.setFetchSize(63333);
		for (int i = 0; i < columns; i++) {
			row[i] = metaData.getColumnName(i + 1);
		}
		String destPath = "C:\\Users\\ke\\Desktop\\aa.zip";
		try (ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(destPath)),
				Charset.forName("utf8"))) {
			zos.putNextEntry(new ZipEntry("JIGEW.csv"));
			while (resultSet.next()) {
				for (int i = 1; i <= columns; i++) {
					row[i - 1] = resultSet.getString(i);
				}
				zos.write((String.join(",", row) + "\n").getBytes("utf8"));
				Thread.sleep(1);
			}
			zos.closeEntry();
		}

		System.err.println("时间：" + (System.currentTimeMillis() - start));
	}

}
