import java.util.Calendar;

public class main {
    public static void main(String args[]) {
        Calendar currTimeDate = Calendar.getInstance();
        System.out.println("Date and time: " + currTimeDate.getTime());
        System.out.println("Timezone: "+ currTimeDate.getTimeZone().getDisplayName());
        System.out.println("Offset: " + (currTimeDate.getTimeZone().getRawOffset() != 0 ? (currTimeDate.getTimeZone().getRawOffset()/1000/60/60): "UTC"));
    }
}
