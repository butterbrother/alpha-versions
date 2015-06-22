package sq;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSetMetaData;
import java.sql.Types;

class SQLNet {
	public static final String DBURL = "jdbc:oracle:thin:@:1521:";
	public static final String DBUSER = "";
	public static final String DBPASS = "";

	public static void main(String[] args) throws SQLException {
	        
		//Юзавем драйвер из ojdbc
		DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
	     	// Устанавливаем соединение
	        Connection con = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
		// Создаём выражение для соединения
	        Statement statement = con.createStatement();
 
		ResultSet rs = statement.executeQuery("select count(*) as count,sysdate from v$session v where v.blocking_session is not null");
		if (rs.next()) {
			long count = rs.getInt(1);
			Date currentDate = rs.getDate(2);
			System.out.println(count + " database locks detected at " + currentDate);
		}
		rs = statement.executeQuery("select t.SES_ADDR, t.start_time, v.sid, v.serial#, ps.sql_text as prev_sql, NVL(cs.sql_text, '') as cur_sql from v$transaction t join v$session v on v.SADDR = t.SES_ADDR join v$sql ps on ps.sql_id = v.prev_sql_id left join v$sql cs on cs.sql_id = v.sql_id order by t.start_time");

		ResultSetMetaData rcs = rs.getMetaData();
		// Получаем все заголовки столбцов
		int columnCount=rcs.getColumnCount();
		for (int i=1; i<= columnCount; i++) {
			System.out.print(rcs.getColumnLabel(i));
			if (i != columnCount)
				System.out.print(";");
		}
		System.out.println("");

		// Получаем все типа столбцов
		for (int i=1; i<= columnCount; i++) {
			System.out.print("<" + rcs.getColumnType(i) + ">");
			if (i != columnCount)
				System.out.print(";");
		}
		System.out.println("");

		//Выводим их
		while (rs.next()) {
			for (int i=1; i<=columnCount; i++) {
				// согласно типам
				switch (rcs.getColumnType(i)) {
					case Types.VARBINARY:
					case Types.VARCHAR:
						try {
							System.out.print(rs.getString(i));
						} catch (SQLException exc) {
							System.out.print("(null)");
						}
						break;
					case Types.BLOB:
						System.out.print("(blob)");
						break;
					case Types.NUMERIC:
					case Types.BIGINT:
						try {
							System.out.print(rs.getLong(i));
						} catch (SQLException exc) {
							System.out.print("(null)");
						}
						break;
					case Types.INTEGER:
						try {
							System.out.print(rs.getInt(i));
						} catch (SQLException exc) {
							System.out.print("(null)");
						}
						break;
					case Types.DATE:
						try {
							int HH=rs.getDate(i).getHours();
							int MM=rs.getDate(i).getMinutes();
							int SS=rs.getDate(i).getSeconds();
							System.out.print(rs.getDate(i) + " " + HH + ":" + MM + ":" + SS);
						} catch (SQLException exc) {
							System.out.print("(null)");
						}
						break;
					default:
							System.out.print("(unknown)");
				}
				if (i != columnCount)
					System.out.print(";");
			}
			System.out.println("");

		}
		rs.close();
		statement.close();
		con.close();
	}
}
