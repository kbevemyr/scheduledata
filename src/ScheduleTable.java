/*
 * BEFORE RUNNING:
 * ---------------
 * 1. If not already done, enable the Google Sheets API
 *    and check the quota for your project at
 *    https://console.developers.google.com/apis/api/sheets
 * 2. Install the Java client library on Maven or Gradle. Check installation
 *    instructions at https://github.com/google/google-api-java-client.
 *    On other build systems, you can add the jar files to your project from
 *    https://developers.google.com/resources/api-libraries/download/sheets/v4/java
 */
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.BatchGetValuesResponse;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

public class ScheduleTable {

    public ScheduleTable() {
    }

    public boolean load() {
	/* 
	   loadEventTable
	   loadGrenTable

GET https://sheets.googleapis.com/v4/spreadsheets/1SrXbQcyicuQvAC14mJH7_J1IIhOdMpVrpviMUC7XKYU/values:batchGet?dateTimeRenderOption=SERIAL_NUMBER&majorDimension=ROWS&ranges='2018+Tidsprogram+L%C3%B6r'!A3%3AF43&valueRenderOption=FORMATTED_VALUE&fields=spreadsheetId%2CvalueRanges&key={YOUR_API_KEY}

	*/
    }

    public static void main(String [] args) {
    }
    
}
