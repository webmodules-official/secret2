package ServerCore;

import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

import Player.Character;
import Player.Charstuff;
import Player.Player;
import Player.PlayerConnection;
import Tools.StringTools;

import Connections.Connection;


import logging.ServerLogger;

public class SelectorThread implements Runnable {
	private Selector selector;
	private boolean running = true;
	private ServerLogger logging = ServerLogger.getInstance();
	private ServerFacade facade = ServerFacade.getInstance();
	private ConnectionFinalizer disconnector = ConnectionFinalizer.getInstance();
	private ByteBuffer readBuffer;
	private ByteBuffer writeBuffer;
	public SelectorThread(Selector select, int readSize, int writeSize) {
		this.selector = select;
		this.readBuffer = ByteBuffer.allocateDirect(readSize);
		this.writeBuffer = ByteBuffer.allocateDirect(writeSize);
	}
	
	public Selector getSelector() {
		return this.selector;
	}

	@Override
    public void run() {
        int selected = 0;
        long[] iterations = new long[15];
        long before = 0;
        int counter = -1;
        long total = 0;
        long iteravg = 0;
        int size = iterations.length;
        while(running) {
                if(!this.disconnector.isEmpty()) {
                        this.disconnector.finalize();
                }
                try {
                        selected = this.selector.select(30);
                } catch (IOException e) {
                        this.logging.severe(this, "I/O Exception occured in selection. Closing selector");
                        this.running = false;
                }
                if(selected > 0) {
                        Set<SelectionKey> readySet = this.selector.selectedKeys();
                        Iterator<SelectionKey> keyIter = readySet.iterator();
                        SelectionKey curKey = null;
                        before = System.currentTimeMillis();
                        while(keyIter.hasNext()) {
                                curKey = keyIter.next();
                                if(curKey.isValid() && curKey.isReadable()) {
                                        this.read((SocketChannel)curKey.channel());
                                } else if(curKey.isValid() && curKey.isWritable()) {
                                        this.write((SocketChannel)curKey.channel());
                                } else if(!curKey.isValid()) {
                                        curKey.cancel();
                                        Connection con = this.facade.getConnectionByChannel((SocketChannel)curKey.channel());
                                        if(con != null) {
                                                this.disconnect(con);
                                        }
                                }
                                keyIter.remove();
                        }
                        counter = ((counter+1)%size);
                        iterations[counter] = (System.currentTimeMillis() - before);
                        if(counter == (size-1)) {
                                for(int i=0;i<size;i++) {
                                        total += iterations[i];
                                }
                                iteravg = total / size;
                                //System.out.println(size + " Selections have passed. Average time of iterating over all selected keys: " + iteravg);
                                Charstuff.getInstance().size = size;
                                Charstuff.getInstance().iteravg = iteravg;
                                total = 0;
                        }
                } else {
                        try {
                                Thread.sleep(10);
                        } catch (InterruptedException e) {
                                e.printStackTrace();
                        }
                }
        }
}
	
	public void setRunning(boolean bol) {
		this.running = bol;
	}
	
	public void register(SocketChannel chan, int ops) {
		try {
			chan.register(this.selector, ops);
		} catch (ClosedChannelException e) {
			this.logging.warning(this, "An attempt to register closed channel in selector was made");
		}
	}
	
	public void unregisterChannel(SocketChannel chan) {
		chan.keyFor(this.selector).cancel();
	}
	
	public void setOps(SocketChannel chan, int ops) {
		chan.keyFor(this.selector).interestOps(ops);
	}
	
	public void registerForFinalization(Connection con) {
		this.disconnector.addFinalizableConnection(con);
	}
	
	private void read(SocketChannel fourchan) {
		Connection con = this.facade.getConnectionByChannel(fourchan);
		this.readBuffer.clear();
		if(con != null && con.getChan().isOpen()) {
			LinkedBlockingQueue<byte[]> readQueue = con.getReadBuffer();
			int read = 0;
			try {
				read = fourchan.read(this.readBuffer);
			} catch (IOException e) {
				this.logging.warning(this, "Error while reading socket"+e);
				this.disconnect(con);
			}catch (NullPointerException e){
			      this.logging.warning(this, StringTools.readStackTrace(e));
				  this.logging.warning(this, "NULL POINER WHILE READING read = fourchan.read");
			}
			if(read > 0) {
				try{
				this.readBuffer.flip();
				byte[] buffy = new byte[this.readBuffer.limit()];
				this.readBuffer.get(buffy);
				readQueue.offer(buffy);
				this.facade.getPckth().processList(con);
				}catch (NullPointerException e){
				      this.logging.warning(this, StringTools.readStackTrace(e));
					  this.logging.warning(this, "NULL POINER WHILE READING processList(con)");
				}
		        catch(BufferOverflowException be) {
		              this.logging.warning(this, "Buffer overflow while reading to: " + con.getIp() + " buffer: " + read);
		        }
			} else if(read == -1) {
				 this.disconnect(con);
			}	
		}else{
			try {
 				fourchan.close();
 			} catch (IOException e) {
				this.logging.warning(this, "Error while closing socket. No Connection instance associated with channel.");
 			}
		}
	}
	
     private void write(SocketChannel twochan) {
                Connection con = this.facade.getConnectionByChannel(twochan);
                if(con != null && con.getChan().isOpen()) {
                        this.writeBuffer.clear();
                        LinkedBlockingQueue<byte[]> writeQueue = con.getWriteBuffer();
                        SelectionKey skey = twochan.keyFor(this.selector);
                        byte[] tmp = writeQueue.poll();
                        if(tmp != null) {
                        this.writeBuffer.put(tmp);
                       
                        while(!writeQueue.isEmpty()) {
                                byte[] tmpb = writeQueue.peek();
                                if(tmpb != null) {
                                        if((this.writeBuffer.position()+tmpb.length) < 1452) {
                                                this.writeBuffer.put(tmpb);
                                                writeQueue.remove();
                                        } else {
                                                break;
                                        }
                                } else {
                                        writeQueue.remove();
                                }
                        }
                       
                        this.writeBuffer.flip();
                        
                       // if(con.getWriteStamp() == 0) {
                        //  con.setWriteStamp(System.currentTimeMillis());
                        // }
                        			
                       
                        int wrote = 0;
                        try {
                        	  wrote = twochan.write(this.writeBuffer);
                                if(this.writeBuffer.limit() > 0 && wrote < this.writeBuffer.limit()) {
                                        while(this.writeBuffer.remaining() > 0) {
                                                while(!skey.isWritable()) {}
                                                twochan.write(this.writeBuffer);
                                        }
                                }
                        } catch (IOException e) {
                           	 //Player wtf = ((PlayerConnection)con).getPlayer();
                    		//Character cur = ((PlayerConnection)con).getActiveCharacter();
                    		//cur.savecharacter();
                     		//System.out.println("Character:"+cur.getLOGsetName()+" failed to write "+wrote+"/"+this.writeBuffer.limit());
                             //this.logging.warning(this, StringTools.readStackTrace(e));
                      	     //this.logging.warning(this, "Username:"+wtf.getUsername()+" IsOpen:"+con.getChan().isOpen()+" IsConnected:"+con.getChan().isConnected()
                            // +"IP:"+con.getIp()+" SC:"+con.getChan()+" wrote:" + wrote+"/"+this.writeBuffer.limit()+" Con-Size:"+ServerFacade.getInstance().getConnectionCount()+" Error while writing socket!");
                     		this.disconnect(con);
                        } catch(BufferOverflowException be) {
                                this.logging.warning(this, "Buffer overflow while writing to: " + con.getIp() + " buffer: " + tmp);
                                writeQueue.remove();
                        }
                        }
                        if(writeQueue.isEmpty()) {
                                con.flipOps(SelectionKey.OP_READ);
                               // con.setWriteStamp(0);
                        } else {
                        	//if(System.currentTimeMillis()-con.getWriteStamp() > Connection.maxWriteTime) {
                    		//	this.read(twochan); //perform a read if time constraints have been met
                    		//	con.setWriteStamp(0);
                    	   //  }
                                this.selector.wakeup();
                        }
                }else{
                try {
                		twochan.close();
                		} catch (IOException e) {
                		this.logging.warning(this, "Error while closing socket. No Connection instance associated with channel.");
                	}
                }
        }
	
	private void disconnect(Connection cn) {
		this.disconnector.addFinalizableConnection(cn);
	}
	
}