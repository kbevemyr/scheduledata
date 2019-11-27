import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;


public class DataLoader {
    private static final String APPLICATION_NAME = "AthleticTimeScheduler";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this application.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    private Sheets service;

    /*// Unuesed 
    String dateTimeRenderOption = DateTimeRenderOption.FORMATTED_STRING;
    String majorDimension = MajorDimensionOption.ROWS;
    String valueRenderOption = ValueRenderOption.FORMATTED_VALUE;
    */
     
    DataLoader() {
	try {
	    this.service = createSheetService();
	} catch (IOException e) {
	    System.err.println("Unable to create DataLoader.");
	} catch (GeneralSecurityException e) {
	    System.err.println("Unable to create DataLoader.");
	}
    }

    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = SheetsQuickstart.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    private static Sheets createSheetService() throws IOException, GeneralSecurityException {
	// Build a new authorized API client service.
	NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
	return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
	    .setApplicationName(APPLICATION_NAME)
	    .build();
    }

    public List<List<Object>> getData(String s, String r) throws IOException {
	System.out.println("getData "+s);
	ValueRange response = service.spreadsheets().values()
                .get(s, r)
                .execute();
	
	List<List<Object>> values = response.getValues();
	System.out.println(r+" #values "+values.size());

	return values;
    }

    public static void main(String[] args) throws IOException, GeneralSecurityException {
	DataLoader dl = new DataLoader();
	/*
	String spreadsheetId = "1YgIgo1cpksh5ZklpVy6J3A46mBmgKJe-ix56f_n4Mzw";
	String runDataRangeL = "2018 Tidsprogram Lör!A3:F35";
	String techDataRangeL = "2018 Tidsprogram Lör!G3:M35";
	String runDataRangeS = "2018 Tidsprogram Sön!A3:F33";
	String techDataRangeS = "2018 Tidsprogram Sön!G3:K33";
	*/

	
	String spreadsheetId = "11i9JCTsDwCnt3mZgRzMnbc7gr4DAJWUzH84aolhU-2c";
	String runDataRangeL = "2019 Tidsprogram Lör!A5:F34";
	String techDataRangeL = "2019 Tidsprogram Lör!G5:M30";
	String runDataRangeS = "2019 Tidsprogram Sön!A7:F32";
	String techDataRangeS = "2019 Tidsprogram Sön!G5:K32";

	dl.getData(spreadsheetId, runDataRangeL);
	dl.getData(spreadsheetId, techDataRangeL);
		
	dl.getData(spreadsheetId, runDataRangeS);
	dl.getData(spreadsheetId, techDataRangeS);
    }
	
}


