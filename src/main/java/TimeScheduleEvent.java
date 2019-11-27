import java.util.List;
import java.time.LocalTime;
import java.lang.Comparable;
import java.lang.System;

public class TimeScheduleEvent implements Comparable<TimeScheduleEvent>{
    String id;
    String day;
    String startTime;
    String klass;
    String gren;
    int duration;
    String arena;
    String grentype;


    public TimeScheduleEvent () {
	this.id = ""+System.currentTimeMillis();
	this.day = "";
	this.startTime = "";
	this.klass = "";
	this.gren = "";
	this.duration = 0;
	this.arena = "";
	this.grentype = "";
    }

    public TimeScheduleEvent (String d, String st, String k, String g, String dt, String a, String gt) {
	// duration is assumed to be on format "33 min"
	this.id = ""+System.currentTimeMillis();
	this.day = d;
	this.startTime = parseTimeInput(st);
	this.klass = k;
	this.gren = g;
	this.duration = parseTechDuration(dt);
	this.arena = a;
	this.grentype = gt;
    }

    public String getDay() {
	return day;
    }

    public String getStartTime() {
	return startTime;
    }

    public int getStartTimeInMinutes() {
	LocalTime time = LocalTime.parse(startTime);
	int hh = time.getHour();
	int mm = time.getMinute();
	return (hh*60)+mm;
    }

    public String getKlass() {
	return this.klass;
    }

    public LocalTime getEndTime() {
	LocalTime st = LocalTime.parse(startTime);
	LocalTime et = st.plusMinutes(duration);
	return et;
    }

    @Override
    public int compareTo(TimeScheduleEvent other) {
	int response = this.getDay().compareTo(other.getDay());

	if(response == 0) {
	    response = this.getStartTime().compareTo(other.getStartTime());
	}

	return response;
    }

    public boolean importRunData(String d, List<Object> input) {
	/*
	  run row: [Starttid
	  löpning, Klass, Löpgren, # Heat, Tid, Ställ]
	  run row: []
	  row [13.49, P15, 60m Final, 1, 00.05, 00.05]
	  row [14.05, F15, 60m Final, 1, 00.05]
	  row []
	  row [14:30]
	*/

	boolean status;
	if(input == null || input.isEmpty() || input.size() < 5) {
	    System.out.println("No input or incomplete data.");
	    status = false;
	} else {
	    //System.out.println("INPUT ROW :"+input);
	    day = d;
	    startTime = (String) input.get(0);
	    klass = (String) input.get(1);
	    gren = (String) input.get(2);

	    int noHeat = new Integer((String) input.get(3));
	    int heatTid = parseRunDuration((String) input.get(4));
	    int stallTid = 0;
	    if(input.size() == 6) {
		stallTid = parseRunDuration((String) input.get(5));
	    }
	    duration = (noHeat*heatTid)+stallTid;

	    arena = "löpning";
	    grentype = "run";
	    status = true;
	}

	return status;
    }

    private static String parseTimeInput(String input) {
	String response = "00:00";
	if (input != "") {
	    LocalTime time = LocalTime.parse(input);
	    response = time.toString();
	}
	return response;
    }

    private static int parseRunDuration(String input) {
	// format "00.04"
	String[] dur = input.split("\\.");
	int d0 = 0;
	int d1 = 0;
	if (dur.length != 0) {
	    if(dur[0] != "") {
		d0 = new Integer(dur[0].trim());
	    }
	    if(dur[1] != "") {
		d1 = new Integer(dur[1].trim());
	    }
	}
	return 60*d0 + d1;
    }

    private static int parseTechDuration(String input) {
	// format "45 min";
	String[] dur = input.split("m");
	int d = 0;
	if(dur.length != 0) {
	    if(dur[0] != "") {
		d = new Integer(dur[0].trim());
	    }
	}
	return d;
    }

    public boolean sameEvent(TimeScheduleEvent other) {
	return (this.klass.equals(other.klass) && this.gren.equals(other.gren));
    }

    public boolean overlap(TimeScheduleEvent e) {
	boolean response = false;

	if (this.day == e.day) {
	    LocalTime st1 = LocalTime.parse(this.startTime);
	    LocalTime et1 = this.getEndTime();
	    LocalTime st2 = LocalTime.parse(e.startTime);
	    LocalTime et2 = e.getEndTime();

	    if (st2.isAfter(st1) && st2.isBefore(et1)) {
		response = true;
	    }
	    if (et2.isAfter(st1) && et2.isBefore(et1)) {
		response = true;
	    }
	}

	return response;
    }

    public String toCVS() {
	String START = "";
	String SEP = "; ";
	String END = "";

	return START+day+SEP+startTime+SEP+klass+SEP+gren+END;
    }

    private String q(String s) {
	return "\""+s+"\"";
    }

    public String toJSON() {
  String START = "{";
  String SEP = ", ";
  String END = "}";
  String Q = "\"";

  String stm = Integer.toString(this.getStartTimeInMinutes());

//  {id: "F15800m", day: "söndag", arena: "löpning", starttime: "912", duration: "18", class: "F15", gren: "800m", grentype: "run", preptime: "0"},
  return START+"id: "+q(klass+gren+arena)+SEP+"day: "+q(day)+SEP+"arena: "+q(arena)+SEP+"starttime: "+q(stm)+SEP+"duration: "+Q+duration+Q+SEP+"class: "+q(klass)+SEP+"gren: "+q(gren)+SEP+"grentype: "+Q+grentype+Q+SEP+"preptime: "+Q+"0"+Q+END;
    }

    public String toString() {
	String START = "";
	String SEP = "; ";
	String END = "";

	return START+arena+SEP+day+SEP+startTime+SEP+klass+SEP+gren+SEP+duration+END;
    }

    public static void main(String [] args) {
	TimeScheduleEvent e = new TimeScheduleEvent("Lördag", "12:20", "F15", "Kula", "33 min", "Kula/Vikt", "tech");

	System.out.println(e);
	System.out.println("Event ends at "+e.getEndTime().toString());
    }

}
