import java.io.IOException;

import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

public class TimeScheduleTable {
    private List<TimeScheduleEvent> data;

    public TimeScheduleTable() {
	data = new ArrayList<TimeScheduleEvent>();
    }

    public void putRow(TimeScheduleEvent e) {
	data.add(e);
    }

    public TimeScheduleEvent getRow(int i) {
	return data.get(i);
    }

    public void importRunData(String day, List<List<Object>> values) {
        if (values == null || values.isEmpty()) {
            System.out.println("No data found.");
	} else {
	    for (List row: values) {
		if(!row.isEmpty()) {
		    TimeScheduleEvent e = new TimeScheduleEvent();
		    e.importRunData(day, row);
		    data.add(e);
		}
	    }
	}
    }
    private static boolean isKlass(String s) {
	if(s.startsWith("P") || s.startsWith("F") || s.startsWith("M") || s.startsWith("K")) {
	    return true;
	} else {
	    return false;
	}
    }

    private static String calcGren(String arena) {
	return arena.toLowerCase().trim().split(" ")[0];
    }

    public boolean importTechData(String day, List<List<Object>> input) {
	/*
	  run row: [Teknik Tid, Längd I, Längd II, Höjd I, Höjd II, Kula, Vikt]
	  run row: [, inhoppning, inhoppning, inhoppning, inhoppning]
	  run row: [09:00, F13, F13, F11, F11, F12]
	  run row: [, 45 min, 45 min, 40 min, 40 min, 60 min]
	  run row: []
	  run row: [10:00, inhoppning, inhoppning, inhoppning, inhoppning, instötning]
	  run row: [10:20, P13, P13, P11, P11, P12]
	  run row: [, 45 min, 45 min, 40 min, 40 min, 45 min]
	  run row: []
	  run row: [11:00, inhoppning, inhoppning]
	  run row: [11:20, P9, P9, , , , inkastning]
	  run row: [11:40, 45 min, 45 min, , , , F15]
	  run row: [, , , , , , 30 min]
	*/

	boolean status = true;
	if(input == null || input.isEmpty()) {
	    //System.out.println("No input");
	    status = false;
	} else {
	    Object[] arenas = input.get(0).toArray();
	    int noArenas = arenas.length;
	    for(int i=1; i < input.size()-1; i++) { //forall rows
		//System.out.println("processing "+input.get(i));
		for(int j=1; j < input.get(i).size(); j++) { //forall arenas available
		    String timestamp = (String) input.get(i).get(0);
		    String arenainfo = (String) input.get(i).get(j);
		    //System.out.println("found timestamp "+timestamp+ " and arenainfo "+arenainfo);
		    if (isKlass(arenainfo)) { //store event
			String duration = "00 min";
			//System.out.println("i+1 "+input.get(i+1).size()+" : "+j+" :"+input.get(i+1)+":");
			if (!(input.get(i+1).isEmpty()) && input.get(i+1).size() > j) {
			    duration = (String) input.get(i+1).get(j);
			}
			TimeScheduleEvent e = new TimeScheduleEvent(day,
								    timestamp,
								    arenainfo,
								    calcGren((String) arenas[j]),
								    duration,
								    (String) arenas[j]);
			data.add(e);
		    }
		}
	    }
	}

	return status;
    }


    public String toString() {
      String START = "";
      String SEP = "\n";
      String END = "\n";

      String result = START;

	for(TimeScheduleEvent row : data) {
	    result = result+row.toString()+SEP;
	}

  result = result+END;

	return result;
    }

    /*
     * Out format: Dag; Starttid(HH:MM); Klass; Gren;
     */
    public void printCVS() {
      String START = "";
      String SEP = "\n";
      String END = "\n";

	String result = START;

	for(int i=0; i<this.data.size()-1; i++) {
	    if(!data.get(i).sameEvent(data.get(i+1))) {
		result = result+data.get(i).toCVS()+SEP;
	    }
	}
	result = result+data.get(this.data.size()-1).toCVS()+END;

	System.out.println(result);
    }

    public void printJSON() {
      String START = "[\n";
      String SEP = ", \n";
      String END = "]\n";

	String days = "";
	String arenas = "";
	String events = "";

  String result = START;

	for(int i=0; i<this.data.size()-1; i++) {
	    if(!data.get(i).sameEvent(data.get(i+1))) {
		result = result+data.get(i).toJSON()+SEP;
	    }
	}
	result = result+data.get(this.data.size()-1).toJSON()+END;

	System.out.println(result);
    }


    public void sort() {
	data.sort(Comparator.naturalOrder());
    }

    public boolean healthCheck() {
	boolean response = true;
	ArrayList abnormal = new ArrayList();
	for(int i=0; i < this.data.size(); i++) {
	    TimeScheduleEvent rowi = this.getRow(i);
	    for(int j=0; j < this.data.size(); j++) {
		TimeScheduleEvent rowj = this.getRow(j);
		if(!rowi.sameEvent(rowj)) {
		    if(rowi.getKlass().equals(rowj.getKlass())) {
			//System.out.println("comparing: "+rowi+"  "+rowj);
			if(rowi.overlap(rowj)) {
			    abnormal.add(rowi);
			    System.out.println("not ok: "+rowi+" overlaps with "+rowj);
			    response = false;
			}
		    }
		}
	    }
	}

	System.out.println("Health :\n"+abnormal.toString());
	return response;
    }


    public static void main(String [] args) {
	DataLoader dl = new DataLoader();
	TimeScheduleTable ts = new TimeScheduleTable();

/*
	// Orginalarket
	String spreadsheetId = "1YgIgo1cpksh5ZklpVy6J3A46mBmgKJe-ix56f_n4Mzw";
	//Katrins LABs version
	//String spreadsheetId = "1bEus2c9lQzqlXD3jjqXbHEkYulWfaken4AQYX_F3gSw";
	String runDataRangeL = "2018 Tidsprogram Lör!A5:F34";
	String runDataRangeS = "2018 Tidsprogram Sön!A7:F32";
	String techDataRangeL = "2018 Tidsprogram Lör!G3:M32";
	String techDataRangeS = "2018 Tidsprogram Sön!G3:K33";

  try {
      ts.importRunData("lördag", dl.getData(spreadsheetId, runDataRangeL));
      ts.importRunData("söndag", dl.getData(spreadsheetId, runDataRangeS));
      ts.importTechData("lördag", dl.getData(spreadsheetId, techDataRangeL));
      ts.importTechData("söndag", dl.getData(spreadsheetId, techDataRangeS));
  } catch (IOException e) {
      System.err.println("Unable to getData.");
  }
*/

  // SAYO Outdoor 2019
  String spreadsheetId = "1t1wx-Z743j8D83Acsl4XLswz3yJz-dYMtWTudK9X-nw";

  String runDataRangeF = "Fredag 2019!A7:F18";
  String runDataRangeL = "Lördag 2019!A3:F52";
  String runDataRangeS = "Söndag 2019!A4:F36";
  String techDataRangeF = "Fredag 2019!H2:N15";
  String techDataRangeLv = "Lördag 2019!H2:O55";
  String techDataRangeLs = "Lördag 2019!R2:S39";
  String techDataRangeSv = "Söndag 2019!H2:M40";
  String techDataRangeSs = "Söndag 2019!O2:P38";

	try {
      ts.importRunData("fredag", dl.getData(spreadsheetId, runDataRangeF));
	    ts.importRunData("lördag", dl.getData(spreadsheetId, runDataRangeL));
	    ts.importRunData("söndag", dl.getData(spreadsheetId, runDataRangeS));
      ts.importTechData("fredag", dl.getData(spreadsheetId, techDataRangeF));
	    ts.importTechData("lördag", dl.getData(spreadsheetId, techDataRangeLv));
      ts.importTechData("lördag", dl.getData(spreadsheetId, techDataRangeLs));
	    ts.importTechData("söndag", dl.getData(spreadsheetId, techDataRangeSv));
      ts.importTechData("söndag", dl.getData(spreadsheetId, techDataRangeSs));
	} catch (IOException e) {
	    System.err.println("Unable to getData.");
	}

	ts.sort();
	System.out.println(ts.toString());

	ts.printJSON();

	System.out.println(ts.healthCheck());



	/*
	TimeScheduleEvent e1 = ts.getRow(3);
	TimeScheduleEvent e2 = ts.getRow(4);
	System.out.println("sameEvent e1 e2"+e1.sameEvent(e2));
	System.out.println("sameEvent e1 e1"+e1.sameEvent(e1));
	System.out.println("getKlass e1"+e1.getKlass());
	System.out.println("getKlass e2"+e2.getKlass());
	System.out.println("overlap e1 e2"+e1.overlap(e2));
	System.out.println("overlap e1 e1"+e1.overlap(e1));
	*/
    }

}
