package timer;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class SystemTimer {
    private static int hour;  
    private static int minute;  
    private static int second;  
	private Timer timer;
	private static SystemTimer instance;
	
	private SystemTimer(){
		this.timer = new Timer(true);
	}
	public static synchronized SystemTimer getInstance(){
		if (instance == null){
			instance = new SystemTimer();
		}
		return instance;
	}
	
	public static long DaysToMiliseconds(long Days){
		long totaldHours=Days*24; 
		long totalMinutes=totaldHours*60;  
        long totalSeconds=totalMinutes*60;  
        long totalMilliSeconds=totalSeconds*1000;  
        return totalMilliSeconds;
	}
	
	public static long MinuteToMiliseconds(long minute){
        long totalSeconds=minute*60;  
        long totalMilliSeconds=totalSeconds*1000;  
        return totalMilliSeconds;
	}
	public static void GetTime(){
		long totalMilliSeconds=System.currentTimeMillis();  
		
        long totalSeconds=totalMilliSeconds/1000;  
        long totalMinutes=totalSeconds/60;  
        long totalHours=totalMinutes/60;  
        
        second=(int)(totalSeconds%60); 
        minute=(int)(totalMinutes%60);
        hour=(int)(totalHours%24);  
	}
	public void addTask(TimerTask task, long delay){
		this.timer.schedule(task, delay);
		
	}
	public void addTask(TimerTask task, Date date){
		this.timer.schedule(task, date);
	}

	public void addTask(TimerTask task, long delay, long period){
		this.timer.schedule(task, delay, period);
	}
	
    public static int getHour(){  
        return hour;  
    }  
    public static int getMinute(){  
        return minute;  
    }  
    public static int getSecond(){  
        return second;  
    }  
    public static void display()  
    {  
        System.out.println("Milisec:"+System.currentTimeMillis());
        System.out.println("Time:"+hour+":"+minute+":"+second);  
    } 

}
