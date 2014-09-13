package test.eidos.kingchanllenge;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Map;

import com.sun.net.httpserver.*;
public class KingRequestHandler  implements HttpHandler{

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		Object response;
		int statusCode;
		OutputStream os=null;
		try {
            @SuppressWarnings("unchecked")
            Map<String, Object> params = (Map<String, Object>)exchange.getAttribute("parameters");

            if (params.get("request").equals("login")) {

            
            }
            else if (params.get("request").equals("score")) {

                   System.out.println("New request received to save the score" +
                        (String)params.get("score"));
                   System.out.println("New request received to save the score" +
                		   (String)params.get("sessionkey"));

     
            }
            else if (params.get("request").equals("highscorelist")) {

                // A list of the best scores has been requested
                System.out.println("Received a new request for the highest scores" +
                        "the level " + (String)params.get("levelid"));

               // This is a header to permit the download of the csv
                Headers headers = exchange.getResponseHeaders();
                headers.add("Content-Type", "text/csv");
                headers.add("Content-Disposition", "attachment;filename=myfilename.csv");
            }
            else {
                response = "Method not implemented";
                System.out.println(response);
              }
          }
        catch (NumberFormatException exception) {
            statusCode = HttpURLConnection.HTTP_BAD_REQUEST;
            response = "Wrong number format";
            System.out.println(response);
        }
        catch (Exception exception) {
            statusCode = HttpURLConnection.HTTP_BAD_REQUEST;
            response = exception.getMessage().toString();
            System.out.println(response);
             os = exchange.getResponseBody();
            os.write(response.toString().getBytes());

        }finally {
            // Send the header response
     
            // Send the body response
             os.close();
        }


    }
		
	}

