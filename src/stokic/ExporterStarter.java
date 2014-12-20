package stokic;

public class ExporterStarter {

	public static void main(String[] args) {
		
		CLIparser cli = new CLIparser(args);
		cli.parse();
		
		System.out.println(cli.optionsToString());
	}
}