package GameServer.GamePackets;

public class Login {
	//tell client the authentication was a huge success
	public static final byte[] Authentication = new byte[] {
		(byte)0x46, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0xd9, (byte)0x00, (byte)0x1c, (byte)0x00, (byte)0x1c, (byte)0x00, 
		(byte)0x00, (byte)0x00, (byte)0x01, (byte)0x45, (byte)0xbb, (byte)0x1f, (byte)0x36, (byte)0xcb, (byte)0xfc, (byte)0x63, (byte)0x3f, (byte)0x11, (byte)0x50, (byte)0xaa, (byte)0xea, (byte)0x3a, 
		(byte)0x94, (byte)0x60, (byte)0x8e, (byte)0xed, (byte)0x0f, (byte)0x86, (byte)0xd5, (byte)0xb9, (byte)0xf1, (byte)0xd5, (byte)0x62, (byte)0xcf, (byte)0x90, (byte)0x7f, (byte)0x0e, (byte)0x00, 
		(byte)0x00, (byte)0x00, (byte)0x0d, (byte)0x87, (byte)0xc0, (byte)0x59, (byte)0x6c, (byte)0xe7, (byte)0xe8, (byte)0xd7, (byte)0xa8, (byte)0xbb, (byte)0xd8, (byte)0xce, (byte)0x15, (byte)0x6d, 
		(byte)0xac, (byte)0x83, (byte)0x21, (byte)0x4e, (byte)0x84, (byte)0x84, (byte)0x0a, (byte)0x00
	};
	
	
	//empty account - no characters
	public static final byte[] Authsucces = new byte[] {
		(byte)0x44, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x03, (byte)0x00, (byte)0x05, (byte)0x00, (byte)0x62, (byte)0x61, (byte)0x75, 
		(byte)0x6b, (byte)0x32, (byte)0x33, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
		(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x6d,
		(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, 
		(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, 
		(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, 
		(byte)0x00, (byte)0x00
	};
	
	//authentication failed
	public static final byte[] authFail = new byte[] {(byte)0x09, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x03, (byte)0x00, (byte)0x64, (byte)0x00, (byte)0xc9};
	
	
	//pleaselogin the website
	public static final byte[] pleaseloginwebsite = new byte[] {(byte)0x09, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x03, (byte)0x00, (byte)0x64, (byte)0x00, (byte)0xca};
	
	//pleaselogin the website respond to client
	public static final byte[] pleaselogin_respondtoclient = new byte[] {(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00};
		
		//banned
	public static final byte[] banned1 = new byte[] {(byte)0x09, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x03, (byte)0x00, (byte)0x64, (byte)0x00, (byte)0xf4};
		
		//banned respond to client
	public static final byte[] banned2 = new byte[] {(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00};
}