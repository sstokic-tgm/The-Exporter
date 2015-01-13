package stokic;

import java.io.*;
import java.sql.*;

/**
 * Mithilfe der uebergegebenen Argumente, wird eine Connection zur Datenbank erstellt, das SQL-Statement
 * zusammengestellt und anschliessend ausgefuehrt.
 * 
 * @author Stokic Stefan
 * @version 1.5
 */
public class ExporterStarter {

	protected static boolean successfullyParsed = false;
	protected static boolean successfullyConnected = false;

	public static void main(String[] args) {

		CLIparser cli = new CLIparser(args);
		cli.parse();
		successfullyParsed = cli.getIsEnoughArgs();

		if(successfullyParsed) {

			String sqlQuery = "";

			if(cli.getCommaSeparatedList().equals("\\*") || cli.getCommaSeparatedList().equals(".classpath"))
				sqlQuery = "SELECT " + "*" + " FROM " + cli.getTableName();
			else
				sqlQuery = "SELECT " + cli.getCommaSeparatedList() + " FROM " + cli.getTableName();


			if(!cli.getWhereCondition().equals(""))
				sqlQuery += " WHERE " + cli.getWhereCondition();

			if(!cli.getFieldToSort().equals(""))
				sqlQuery += " ORDER BY " + cli.getFieldToSort() + cli.getSortOrder();

			ConnectionLogic conLog = new ConnectionLogic();

			conLog.setConnection(cli.getHostname(), cli.getUsername(), cli.getPassword(), cli.getDbName());
			successfullyConnected = conLog.connect();

			if(successfullyConnected) {

				if(cli.getOutFile() == null) {

					try {

						ResultSet rs = conLog.execute(sqlQuery);
						ResultSetMetaData rsmd = rs.getMetaData();

						int columnCount = rsmd.getColumnCount();

						for(int i = 1; i <= columnCount; i++) {

							System.out.print(rsmd.getColumnName(i) + cli.getDelimiter());
						}

						System.out.println();

						while(rs.next()) {

							for(int i = 1; i <= columnCount; i++) {

								System.out.print(rs.getString(i) + cli.getDelimiter());
							}
							System.out.println();
						}

						rs.close();
						rs = null;

					}catch(SQLException sqle) {

						System.out.println("Error: " + sqle.getMessage());
					}		
				}else {

					try {

						File file = new File("./" + cli.getOutFile() + ".txt");
						file.createNewFile();
						PrintWriter fileWriter = new PrintWriter(file, "UTF-8");

						ResultSet rs = conLog.execute(sqlQuery);
						ResultSetMetaData rsmd = rs.getMetaData();

						int columnCount = rsmd.getColumnCount();

						for(int i = 1; i <= columnCount; i++) {

							fileWriter.print(rsmd.getColumnName(i) + cli.getDelimiter());
						}

						fileWriter.println();

						while(rs.next()) {

							for(int i = 1; i <= columnCount; i++) {

								fileWriter.print(rs.getString(i) + cli.getDelimiter());
							}
							fileWriter.println();
						}

						fileWriter.close();

						rs.close();
						rs = null;

						System.out.println(file.getAbsolutePath() + " successfully created!");

					} catch(IOException ioe) {

						System.out.println("Error: " + ioe.getMessage());

					}catch(SQLException sqle) {

						System.out.println("Error: " + sqle.getMessage());
					}	
				}
			}
			conLog.disconnect();
		}
	}
}