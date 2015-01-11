package stokic;

import org.apache.commons.cli.*;

/**
 * Kommandozeilen Argumente werden im GNU Style geparst.
 * 
 * @author Stokic Stefan
 * @version 1.1
 */
public class CLIparser {

	private String[] args;
	private Options options;
	
	private String hostname = "localhost",
					username = System.getProperty("user.name"),
					password = "",
					dbName = null,
					fieldToSort = "",
					sortOrder = "ASC",
					whereCondition = "",
					delimiter = ";",
					commaSeparatedList = null,
					outFile = null,
					tableName = null;

	public CLIparser(String[] args){

		this.args = args;
		this.options = new Options();

		Option hostname = OptionBuilder.withArgName("Hostname")
				.hasArg()
				.withDescription("Hostname des DBMS. Standard: localhost")
				.isRequired(false)
				.create("h");
		Option username = OptionBuilder.withArgName("Benutzername")
				.hasArg()
				.withDescription("Benutzername. Standard: Benutzername des im Betriebssystem angemeldeten Benutzers")
				.isRequired(false)
				.create("u");
		Option password = OptionBuilder.withArgName("Passwort")
				.hasArg(false)
				.withDescription("Passwort. Standard: keins")
				.isRequired(false)
				.create("p");
		Option dbName = OptionBuilder.withArgName("Datenbank Name")
				.hasArg()
				.withDescription("Name der Datenbank")
				.create("d");
		Option fieldToSort = OptionBuilder.withArgName("Feld Name")
				.hasArg()
				.withDescription("Feld, nach dem sortiert werden soll (nur eines m�glich, Standard: keines)")
				.isRequired(false)
				.create("s");
		Option sortOrder = OptionBuilder.withArgName("Sortierrichtung")
				.hasArg()
				.withDescription("Sortierrichtung. Standard: ASC")
				.isRequired(false)
				.create("r");
		Option whereCondition = OptionBuilder.withArgName("eine Bedingung")
				.hasArg()
				.withDescription("eine Bedingung in SQL-Syntax, die um Filtern der Tabelle verwendet wird. Standard: keine")
				.isRequired(false)
				.create("w");
		Option delimiter = OptionBuilder.withArgName("Trennzeichen")
				.hasArg()
				.withDescription("Trennzeichen, dass f�r die Ausgabe verwendet werden soll. Standard: ;")
				.isRequired(false)
				.create("t");
		Option commaSeparatedList = OptionBuilder.withArgName("Kommagetrennte Liste (ohne Leerzeichen)")
				.hasArg()
				.withDescription("Kommagetrennte Liste (ohne Leerzeichen) der Felder, die im Ergebnis enthalten sein sollen. * soll akzeptiert werden (Pflicht)")
				.create("f");
		Option outFile = OptionBuilder.withArgName("Name der Ausgabedatei")
				.hasArg()
				.withDescription("Name der Ausgabedatei. Standard: keine -> Ausgabe auf der Konsole")
				.isRequired(false)
				.create("o");
		Option tableName = OptionBuilder.withArgName("Tabellenname")
				.hasArg()
				.withDescription("Tabellenname (Pflicht)")
				.create("T");
		

		this.options.addOption(hostname);
		this.options.addOption(username);
		this.options.addOption(password);
		this.options.addOption(dbName);
		this.options.addOption(fieldToSort);
		this.options.addOption(sortOrder);
		this.options.addOption(whereCondition);
		this.options.addOption(delimiter);
		this.options.addOption(commaSeparatedList);
		this.options.addOption(outFile);
		this.options.addOption(tableName);

	}

	/**
	 * Methode die die Argumente parst und wenn sie korrekt sind, wird Sekretariat ausgef�hrt
	 */
	public boolean parse(){

		GnuParser parser = new GnuParser();

		try{

			CommandLine line = parser.parse(this.options, this.args);

			if(line.hasOption("h") && line.hasOption("u") && line.hasOption("p") && line.hasOption("d") &&
					line.hasOption("s") && line.hasOption("r") && line.hasOption("w") &&
					line.hasOption("t") && line.hasOption("f") && line.hasOption("o") &&
					line.hasOption("T")){
				
				this.hostname = line.getOptionValue("h");
				this.username = line.getOptionValue("u");
				this.password = line.getOptionValue("p");
				this.dbName = line.getOptionValue("d");
				this.fieldToSort = line.getOptionValue("s");
				
				if(line.hasOption("r") && line.hasOption("s"))
					this.sortOrder = line.getOptionValue("r");
				
				this.whereCondition = line.getOptionValue("w");
				this.delimiter = line.getOptionValue("t");
				this.commaSeparatedList = line.getOptionValue("f");
				this.outFile = line.getOptionValue("o");
				this.tableName = line.getOptionValue("T");
				
				return true;
				
			}else if(!line.hasOption("d") || !line.hasOption("f") || !line.hasOption("T")) {
				
				this.help();
				
				return false;
			}

		}catch(ParseException e){

			this.help();
			
			return false;
		}
		
		return false;
	}

	/**
	 * Methode die eine Hilfestellung zeigt
	 */
	public void help(){

		HelpFormatter hf = new HelpFormatter();
		hf.printHelp("The Exporter", this.options);
	}

	public String getHostname() {
		
		return hostname;
	}

	public String getUsername() {
		
		return username;
	}

	public String getPassword() {
		
		return password;
	}

	public String getDbName() {
		
		return dbName;
	}

	public String getFieldToSort() {
		
		return fieldToSort;
	}

	public String getSortOrder() {
		
		return sortOrder;
	}

	public String getWhereCondition() {
		
		return whereCondition;
	}

	public String getDelimiter() {
		
		return delimiter;
	}

	public String getCommaSeparatedList() {
		
		return commaSeparatedList;
	}

	public String getOutFile() {
		
		return outFile;
	}

	public String getTableName() {
		
		return tableName;
	}
	
	public String optionsToString() {
		
		return this.hostname + ";" + this.username + ";" + this.password + ";" + this.dbName + ";" + 
				this.fieldToSort + ";" + this.sortOrder + ";" + this.whereCondition + ";" + this.delimiter +
				";" + this.commaSeparatedList + ";" + this.outFile + ";" + this.tableName;
	}
}