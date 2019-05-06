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

https://developers.google.com/sheets/api/reference/rest/v4/spreadsheets.values/batchGet?apix_params=%7B%22spreadsheetId%22%3A%221SrXbQcyicuQvAC14mJH7_J1IIhOdMpVrpviMUC7XKYU%22%2C%22dateTimeRenderOption%22%3A%22SERIAL_NUMBER%22%2C%22majorDimension%22%3A%22ROWS%22%2C%22ranges%22%3A%5B%22%272018%20Tidsprogram%20L%C3%B6r%27!A3%3AF43%22%5D%2C%22valueRenderOption%22%3A%22FORMATTED_VALUE%22%7D

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

public class SheetsExample {
  public static void main(String args[]) throws IOException, GeneralSecurityException {
    // The ID of the spreadsheet to retrieve data from.
    //String spreadsheetId = "my-spreadsheet-id"; // TODO: Update placeholder value.
    String spreadsheetId = "1SrXbQcyicuQvAC14mJH7_J1IIhOdMpVrpviMUC7XKYU"; 

    // The A1 notation of the values to retrieve.
    //List<String> ranges = new ArrayList<>(); // TODO: Update placeholder value.
    List<String> ranges = new ArrayList<>(); // TODO: Update placeholder value.
    String range1 = "2018 Tidsprogram Lör!A3:F43";

    // How values should be represented in the output.
    // The default render option is ValueRenderOption.FORMATTED_VALUE.
    //String valueRenderOption = ""; // TODO: Update placeholder value.
    String valueRenderOption = ValueRenderOption.FORMATTED_VALUE; 

    // How dates, times, and durations should be represented in the output.
    // This is ignored if value_render_option is
    // FORMATTED_VALUE.
    // The default dateTime render option is [DateTimeRenderOption.SERIAL_NUMBER].
    //String dateTimeRenderOption = ""; // TODO: Update placeholder value.
    String dateTimeRenderOption = DateTimeRenderOption.SERIAL_NUMBER;

    Sheets sheetsService = createSheetsService();
    Sheets.Spreadsheets.Values.BatchGet request =
        sheetsService.spreadsheets().values().batchGet(spreadsheetId);
    request.setRanges(ranges);
    request.setValueRenderOption(valueRenderOption);
    request.setDateTimeRenderOption(dateTimeRenderOption);

    BatchGetValuesResponse response = request.execute();

    // TODO: Change code below to process the `response` object:
    System.out.println(response);
  }

  public static Sheets createSheetsService() throws IOException, GeneralSecurityException {
    HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
    JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

    // TODO: Change placeholder below to generate authentication credentials. See
    // https://developers.google.com/sheets/quickstart/java#step_3_set_up_the_sample
    //
    // Authorize using one of the following scopes:
    //   "https://www.googleapis.com/auth/drive"
    //   "https://www.googleapis.com/auth/drive.file"
    //   "https://www.googleapis.com/auth/drive.readonly"
    //   "https://www.googleapis.com/auth/spreadsheets"
    //   "https://www.googleapis.com/auth/spreadsheets.readonly"
    GoogleCredential credential = null;

    return new Sheets.Builder(httpTransport, jsonFactory, credential)
        .setApplicationName("Google-SheetsSample/0.1")
        .build();
  }
}
