package Database;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import Configuration.Configuration;
import Configuration.ConfigurationManager;


public class SQLconnection {
	private ArrayBlockingQueue<Connection> pool;
	private ExecutorService threadPool = Executors.newCachedThreadPool();
	private static SQLconnection instance; 
	private int Query_Size_Pool = 20; // choose the query size pool 
	 private Configuration conf;
	
	public SQLconnection() {
		this.pool = new ArrayBlockingQueue<Connection>(Query_Size_Pool);
		this.conf = ConfigurationManager.getConf("GameServer");
		this.init(Query_Size_Pool);
	}
	
	   public synchronized static SQLconnection getInstance(){
	        if (instance == null){
	                instance = new SQLconnection();
	        }
	        return instance;
	    }
	
	private void init(int size) {		
		final String url = "jdbc:mysql://localhost/openheroes";
	    Driver driver = new RuntimeDriverLoader().loadDriver("mysql-connector-java-5.0.8-bin.jar");
	    Connection con = null;
		for(int i=0;i<size;i++) {
			try {
				DriverManager.registerDriver(new DriverWrapper(driver));
				con = DriverManager.getConnection(url, this.conf.getVar("username"), this.conf.getVar("password"));
				this.pool.offer(con);
			} catch (SQLException e) {
				int a = e.getErrorCode();
				if (a == 0) {System.out.println("Fatal error: Database connection failed"); }
				else if (a == 1045){System.out.println("Fatal error("+i+"): Access denied"); }
				else if (a == 1044){System.out.println("Fatal error("+i+"): Access denied"); }
				else if (a == 1049){System.out.println("Fatal error: Database doesnt exist"); }
				else {System.out.println("Fatal error: " + a);}
			}
		}
	}
	
	public Connection getaConnection() {
		try {
			return this.pool.take();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	

	//executeQuery
	public ResultSet executeQuery(PreparedStatement ps) {
		final ArrayBlockingQueue<Connection> connections = this.pool;
		final PreparedStatement statement = ps;
		FutureTask<ResultSet> ft = new FutureTask<ResultSet>(new Callable<ResultSet>() {
			@Override
			public ResultSet call() throws Exception {
				Connection cn = statement.getConnection();
				ResultSet rval = null;
				try{
						  rval = statement.executeQuery();
		    	} catch (Exception e) {
		    		//e.printStackTrace();  enable this to debug any querys
		    	}
				connections.offer(cn);
				return rval;
			}
		});
		this.threadPool.execute(ft);
		try{
		while (!ft.isDone()); //wait for database to return ResultSet	
		ResultSet set = ft.get();
		return set;
    	} catch (Exception e) {
    		//e.printStackTrace();  enable this to debug any querys
	    }
		return null;
	}
	//execute
	public boolean execute(PreparedStatement ps) {
		final ArrayBlockingQueue<Connection> connections = this.pool;
		final PreparedStatement statement = ps;
		FutureTask<Boolean> ft = new FutureTask<Boolean>(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				Connection cn = statement.getConnection();
				Boolean rval = null;
				try{
						rval = statement.execute();
		    	} catch (Exception e) {
		    		//e.printStackTrace();  enable this to debug any querys
		    	}
				connections.offer(cn);
				return rval;
			}
		});
		this.threadPool.execute(ft);
		try{
		while (!ft.isDone()); //wait for database to return ResultSet	
		boolean gg = ft.get();
		return gg;
    	} catch (Exception e) {
    	//e.printStackTrace();  enable this to debug any querys
	    }
		return false;
	}
	
}
