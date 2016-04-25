
import java.net.*;
import java.io.*;


public class SimpleClient 
{
	
	public static void main( String[] argv ) 
	{
			String host = "localhost";
			int port = 7777;
			String line;
			BufferedReader br;
			OutputStream sos = null;
			InputStream sis = null;
			Socket sock = null;
			byte buf[];
			int n;
			
			switch( argv.length )
			{
				case 0:
					System.out.println( "Usage: client [host] port" );
					System.exit(-1);
					break;
				case 1:
					port = (new Integer(argv[0])).intValue();
					break;
				case 2:
					host = argv[0];
					port = (new Integer(argv[1])).intValue();
					break;
				default:
					System.out.println( "Usage: client [host] port" );
					System.exit(-1);
					break;
			}

			br = new BufferedReader( new InputStreamReader(System.in) );
			buf = new byte[512];

			try
			{
				sock = new Socket( InetAddress.getByName(host), port );
				sos = sock.getOutputStream();
				sis = sock.getInputStream();
			}
			catch ( UnknownHostException uhe )
			{
				System.out.println( "client: " + host + " cannot be resolved." );
				System.exit(-1);
			}
			catch ( IOException ioe )
			{
				System.out.println( "client: cannot initialize socket." );
				System.exit(-1);
			}
			for (;;)
			{
				try
				{
					System.out.println( "Enter a request for the server (type quit to quit):" );
					line = br.readLine();
					
					if ( line.startsWith( "quit" )) {
						sock.close();
						break;
					}
					
					line += "\n";
					//System.out.println( "Sending: " + line );
					sos.write( line.getBytes() );
					sos.flush();
					
					// read the result (hope it comes all at once)
					n = sis.read( buf );
					if ( n == -1 ) {
						System.out.println( "server closed the connection" );							
						System.exit(-1);
					}
					else {
						System.out.println( "server replied: " + new String( buf ) );
					}
				}
				catch ( IOException rwe )
				{
					System.out.println( "client: I/O error." );
				}
			}
		}
}


