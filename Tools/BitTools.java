package Tools;

import java.math.BigInteger;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import Encryption.Decryptor;
import Player.Character;

public class BitTools {
	
	public static void printPacket(ByteBuffer buf) {
		byte[] decrypted = new byte[buf.get(0)-8];
		buf.get(decrypted, 8, decrypted.length);
		decrypted = Decryptor.Decrypt(decrypted);
		for(int i=0;i<decrypted.length;i++) {
			//System.out.printf("%02x ", (decrypted[i]&0xFF));
		}
	}
	
	
	// String to key String - int
	public static int StringToKey(String one, Map<Integer, String> Hashmap) {
		if(Hashmap.containsValue(one)){
		Iterator<Entry<Integer, String>> iter = Hashmap.entrySet().iterator();   
		while(iter.hasNext()) {
			Entry<Integer, String> pairs = iter.next();
			 if(one.equals(pairs.getValue())) {
			      return pairs.getKey();
			 }
		}}
		return 1337;
	}
	
	// Value to key int - int
	public static int ValueToKey(int one, Map<Integer, Integer> Hashmap) {
		if(Hashmap.containsValue(one)){
		Iterator<Entry<Integer, Integer>> iter = Hashmap.entrySet().iterator();   
		while(iter.hasNext()) {
			Entry<Integer, Integer> pairs = iter.next();
			 if(one == pairs.getValue()) {
			      return pairs.getKey();
			 }
		}}
		return 0;
	}
	
	// Value to key int - int
	public static int ValueToKey_InvCheckSTACK(Character cur, int Stack, int one, Map<Integer, Integer> Hashmap) {
		if(Hashmap.containsValue(one)){
		Iterator<Entry<Integer, Integer>> iter = Hashmap.entrySet().iterator();   
		while(iter.hasNext()) {
			Entry<Integer, Integer> pairs = iter.next();
			 if(one == pairs.getValue()){
				int STACK = cur.getInventorySTACK(pairs.getKey()) + Stack; 
				// if its above 100 stack then
				if(STACK <= 100){
				return pairs.getKey();	
				}
			 }
		}}
		return 1337;
	}
	
	// this is used for exp system like in mh
public static byte[] LongToByteArrayREVERSE(long var) {
	byte[] msg = ByteBuffer.allocate(8).putLong(var).array();
	for (int left = 0, right = msg.length - 1; left < right; ++left, --right) {
	    byte temp = msg[left]; 
	    msg[left]  = msg[right]; 
	    msg[right] = temp;
	}
	return msg;       
}
	
	 public static long ipToLong(InetAddress ip) {
	        byte[] octets = ip.getAddress();
	        long result = 0;
	        for (byte octet : octets) {
	            result <<= 8;
	            result |= octet & 0xff;
	        }
	        return result;
	    }
	 
	 public static int ChangeAnyDigit4DIGITPASS(int FourDigitPass, int digit, int replace){
			int number = FourDigitPass;
			int[] ParsedItemID = new int[]{0,0,0,0};
			String StringParsedItemID = "";
			int y = 3;
			while (number > 0) {
				ParsedItemID[y] = number % 10;
			    number = number / 10;
			    y--;
			}
			ParsedItemID[digit] = replace;

			for(int i=0;i<ParsedItemID.length;i++) {
				StringParsedItemID += Integer.toString(ParsedItemID[i]);
			}//System.out.println("RETURN: "+Integer.valueOf(StringParsedItemID));
			return Integer.valueOf(StringParsedItemID);
		}
	 
		public static int ChangeAnyDigit(int ItemID, int digit, int replace){
			int number = ItemID;
			int[] ParsedItemID = new int[]{0,0,0,0,0,0,0,0,0};
			String StringParsedItemID = "";
			int y = 8;
			while (number > 0) {
				ParsedItemID[y] = number % 10;
			    number = number / 10;
			    y--;
			}
			ParsedItemID[digit] = replace;

			for(int i=0;i<ParsedItemID.length;i++) {
				StringParsedItemID += Integer.toString(ParsedItemID[i]);
			}//System.out.println("RETURN: "+Integer.valueOf(StringParsedItemID));
			return Integer.valueOf(StringParsedItemID);
		}
	
	public static int byteToInt(byte b) {
		return (int) b & 0xFF;
	}
		
	public static float byteArrayToFloat(byte[] b) {
		return Float.intBitsToFloat(((b[0]&0xff)<<24) | ((b[1]&0xff)<<16) | ((b[2]&0xff)<<8) | (b[3]&0xff));
	}
		
	public static int byteArrayToInt(byte[] b) {
		return (int)((b[3]&0xff)<<24) | ((b[2]&0xff)<<16) | ((b[1]&0xff)<<8) | (b[0]&0xff);
	}
		
		
	public static byte[] intToByteArray(int var) {
		return new byte[] {
					(byte)var,
					(byte)(var >>> 8),
					(byte)(var >>> 16),
	                (byte)(var >>> 24)
	    };           
	}
	
	public static byte[] shortToByteArray(short sval) {
		return new byte[] {
				(byte)(sval & 0xff),
				(byte)((sval >> 8) & 0xff)	
		};
	}
	
	
	public static byte[] floatToByteArray(float f) {
		return intToByteArray(Float.floatToRawIntBits(f));
	}
	
	public static String stringtoHexString(String arg) {
		return String.format("%x", new BigInteger(arg.getBytes()));
	}
		
	public static byte[] stringToByteArray(String s) {
		return s.getBytes();
	}
	  
	public static String byteArrayToString(byte[] baww) {
		return new String(baww);
	}
	
	
	public static long ByteArrayToLong(byte[] data) {
	    if (data == null || data.length != 8) return 0x0;
	    // ----------
	    return (long)(
	            // (Below) convert to longs before shift because digits
	            //         are lost with ints beyond the 32-bit limit
	            (long)(0xff & data[7]) << 56  |
	            (long)(0xff & data[6]) << 48  |
	            (long)(0xff & data[5]) << 40  |
	            (long)(0xff & data[4]) << 32  |
	            (long)(0xff & data[3]) << 24  |
	            (long)(0xff & data[2]) << 16  |
	            (long)(0xff & data[1]) << 8   |
	            (long)(0xff & data[0]) << 0
	            );
	}
	
	public static byte[] LongToByteArray(long data) {
	    return new byte[] {
	        (byte)((data >> 56) & 0xff),
	        (byte)((data >> 48) & 0xff),
	        (byte)((data >> 40) & 0xff),
	        (byte)((data >> 32) & 0xff),
	        (byte)((data >> 24) & 0xff),
	        (byte)((data >> 16) & 0xff),
	        (byte)((data >> 8) & 0xff),
	        (byte)((data >> 0) & 0xff),
	    };
	}
	
	
	public static byte[] toByta(byte data) {
	    return new byte[]{data};
	}
	public static byte[] toByta(byte[] data) {
	    return data;
	}
	/* ========================= */
	public static byte[] toByta(short data) {
	    return new byte[] {
	        (byte)((data >> 8) & 0xff),
	        (byte)((data >> 0) & 0xff),
	    };
	}
	public static byte[] toByta(short[] data) {
	    if (data == null) return null;
	    // ----------
	    byte[] byts = new byte[data.length * 2];
	    for (int i = 0; i < data.length; i++)
	        System.arraycopy(toByta(data[i]), 0, byts, i * 2, 2);
	    return byts;
	}
	/* ========================= */
	public static byte[] toByta(char data) {
	    return new byte[] {
	        (byte)((data >> 8) & 0xff),
	        (byte)((data >> 0) & 0xff),
	    };
	}
	public static byte[] toByta(char[] data) {
	    if (data == null) return null;
	    // ----------
	    byte[] byts = new byte[data.length * 2];
	    for (int i = 0; i < data.length; i++)
	        System.arraycopy(toByta(data[i]), 0, byts, i * 2, 2);
	    return byts;
	}
	/* ========================= */
	public static byte[] toByta(int data) {
	    return new byte[] {
	        (byte)((data >> 24) & 0xff),
	        (byte)((data >> 16) & 0xff),
	        (byte)((data >> 8) & 0xff),
	        (byte)((data >> 0) & 0xff),
	    };
	}
	public static byte[] toByta(int[] data) {
	    if (data == null) return null;
	    // ----------
	    byte[] byts = new byte[data.length * 4];
	    for (int i = 0; i < data.length; i++)
	        System.arraycopy(toByta(data[i]), 0, byts, i * 4, 4);
	    return byts;
	}
	/* ========================= */
	public static byte[] toByta(long data) {
	    return new byte[] {
	        (byte)((data >> 56) & 0xff),
	        (byte)((data >> 48) & 0xff),
	        (byte)((data >> 40) & 0xff),
	        (byte)((data >> 32) & 0xff),
	        (byte)((data >> 24) & 0xff),
	        (byte)((data >> 16) & 0xff),
	        (byte)((data >> 8) & 0xff),
	        (byte)((data >> 0) & 0xff),
	    };
	}
	public static byte[] toByta(long[] data) {
	    if (data == null) return null;
	    // ----------
	    byte[] byts = new byte[data.length * 8];
	    for (int i = 0; i < data.length; i++)
	        System.arraycopy(toByta(data[i]), 0, byts, i * 8, 8);
	    return byts;
	}
	/* ========================= */
	public static byte[] toByta(float data) {
	    return toByta(Float.floatToRawIntBits(data));
	}
	public static byte[] toByta(float[] data) {
	    if (data == null) return null;
	    // ----------
	    byte[] byts = new byte[data.length * 4];
	    for (int i = 0; i < data.length; i++)
	        System.arraycopy(toByta(data[i]), 0, byts, i * 4, 4);
	    return byts;
	}
	/* ========================= */
	public static byte[] toByta(double data) {
	    return toByta(Double.doubleToRawLongBits(data));
	}
	public static byte[] toByta(double[] data) {
	    if (data == null) return null;
	    // ----------
	    byte[] byts = new byte[data.length * 8];
	    for (int i = 0; i < data.length; i++)
	        System.arraycopy(toByta(data[i]), 0, byts, i * 8, 8);
	    return byts;
	}
	/* ========================= */
	public static byte[] toByta(boolean data) {
	    return new byte[]{(byte)(data ? 0x01 : 0x00)}; // bool -> {1 byte}
	}
	public static byte[] toByta(boolean[] data) {
	    // Advanced Technique: The byte array containts information
	    // about how many boolean values are involved, so the exact
	    // array is returned when later decoded.
	    // ----------
	    if (data == null) return null;
	    // ----------
	    int len = data.length;
	    byte[] lena = toByta(len); // int conversion; length array = lena
	    byte[] byts = new byte[lena.length + (len / 8) + (len % 8 != 0 ? 1 : 0)];
	    // (Above) length-array-length + sets-of-8-booleans +? byte-for-remainder
	    System.arraycopy(lena, 0, byts, 0, lena.length);
	    // ----------
	    // (Below) algorithm by Matthew Cudmore: boolean[] -> bits -> byte[]
	    for (int i = 0, j = lena.length, k = 7; i < data.length; i++) {
	        byts[j] |= (data[i] ? 1 : 0) << k--;
	        if (k < 0) { j++; k = 7; }
	    }
	    // ----------
	    return byts;
	}
	/* ========================= */
	public static byte[] toByta(String data) {
	    return (data == null) ? null : data.getBytes();
	}
	public static byte[] toByta(String[] data) {
	    // Advanced Technique: Generates an indexed byte array
	    // which contains the array of Strings. The byte array
	    // contains information about the number of Strings and
	    // the length of each String.
	    // ----------
	    if (data == null) return null;
	    // ---------- flags:
	    int totalLength = 0; // Measure length of final byte array
	    int bytesPos = 0; // Used later
	    // ----- arrays:
	    byte[] dLen = toByta(data.length); // byte array of data length
	    totalLength += dLen.length;
	    int[] sLens = new int[data.length]; // String lengths = sLens
	    totalLength += (sLens.length * 4);
	    byte[][] strs = new byte[data.length][]; // array of String bytes
	    // ----- pack strs:
	    for (int i = 0; i < data.length; i++) {
	        if (data[i] != null) {
	            strs[i] = toByta(data[i]);
	            sLens[i] = strs[i].length;
	            totalLength += strs[i].length;
	        } else {
	            sLens[i] = 0;
	            strs[i] = new byte[0]; // prevent null entries
	        }
	    }
	    // ----------
	    byte[] bytes = new byte[totalLength]; // final array
	    System.arraycopy(dLen, 0, bytes, 0, 4);
	    byte[] bsLens = toByta(sLens); // byte version of String sLens
	    System.arraycopy(bsLens, 0, bytes, 4, bsLens.length);
	    // -----
	    bytesPos += 4 + bsLens.length; // mark position
	    // -----
	    for (byte[] sba : strs) {
	        System.arraycopy(sba, 0, bytes, bytesPos, sba.length);
	        bytesPos += sba.length;
	    }
	    // ----------
	    return bytes;
	}
	/* ========================= */
	/* "byte[] data --> primitive type" Methods */
	/* ========================= */
	public static byte toByte(byte[] data) {
	    return (data == null || data.length == 0) ? 0x0 : data[0];
	}
	public static byte[] toByteA(byte[] data) {
	    return data;
	}
	/* ========================= */
	public static short toShort(byte[] data) {
	    if (data == null || data.length != 2) return 0x0;
	    // ----------
	    return (short)(
	            (0xff & data[0]) << 8   |
	            (0xff & data[1]) << 0
	            );
	}
	public static short[] toShortA(byte[] data) {
	    if (data == null || data.length % 2 != 0) return null;
	    // ----------
	    short[] shts = new short[data.length / 2];
	    for (int i = 0; i < shts.length; i++) {
	        shts[i] = toShort( new byte[] {
	            data[(i*2)],
	            data[(i*2)+1]
	        } );
	    }
	    return shts;
	}
	/* ========================= */
	public static char toChar(byte[] data) {
	    if (data == null || data.length != 2) return 0x0;
	    // ----------
	    return (char)(
	            (0xff & data[0]) << 8   |
	            (0xff & data[1]) << 0
	            );
	}
	public static char[] toCharA(byte[] data) {
	    if (data == null || data.length % 2 != 0) return null;
	    // ----------
	    char[] chrs = new char[data.length / 2];
	    for (int i = 0; i < chrs.length; i++) {
	        chrs[i] = toChar( new byte[] {
	            data[(i*2)],
	            data[(i*2)+1],
	        } );
	    }
	    return chrs;
	}
	/* ========================= */
	public static int toInt(byte[] data) {
	    if (data == null || data.length != 4) return 0x0;
	    // ----------
	    return (int)( // NOTE: type cast not necessary for int
	            (0xff & data[0]) << 24  |
	            (0xff & data[1]) << 16  |
	            (0xff & data[2]) << 8   |
	            (0xff & data[3]) << 0
	            );
	}
	public static int[] toIntA(byte[] data) {
	    if (data == null || data.length % 4 != 0) return null;
	    // ----------
	    int[] ints = new int[data.length / 4];
	    for (int i = 0; i < ints.length; i++)
	        ints[i] = toInt( new byte[] {
	            data[(i*4)],
	            data[(i*4)+1],
	            data[(i*4)+2],
	            data[(i*4)+3],
	        } );
	    return ints;
	}
	/* ========================= */
	public static long toLong(byte[] data) {
	    if (data == null || data.length != 8) return 0x0;
	    // ----------
	    return (long)(
	            // (Below) convert to longs before shift because digits
	            //         are lost with ints beyond the 32-bit limit
	            (long)(0xff & data[0]) << 56  |
	            (long)(0xff & data[1]) << 48  |
	            (long)(0xff & data[2]) << 40  |
	            (long)(0xff & data[3]) << 32  |
	            (long)(0xff & data[4]) << 24  |
	            (long)(0xff & data[5]) << 16  |
	            (long)(0xff & data[6]) << 8   |
	            (long)(0xff & data[7]) << 0
	            );
	}
	public static long[] toLongA(byte[] data) {
	    if (data == null || data.length % 8 != 0) return null;
	    // ----------
	    long[] lngs = new long[data.length / 8];
	    for (int i = 0; i < lngs.length; i++) {
	        lngs[i] = toLong( new byte[] {
	            data[(i*8)],
	            data[(i*8)+1],
	            data[(i*8)+2],
	            data[(i*8)+3],
	            data[(i*8)+4],
	            data[(i*8)+5],
	            data[(i*8)+6],
	            data[(i*8)+7],
	        } );
	    }
	    return lngs;
	}
	/* ========================= */
	public static float toFloat(byte[] data) {
	    if (data == null || data.length != 4) return 0x0;
	    // ---------- simple:
	    return Float.intBitsToFloat(toInt(data));
	}
	public static float[] toFloatA(byte[] data) {
	    if (data == null || data.length % 4 != 0) return null;
	    // ----------
	    float[] flts = new float[data.length / 4];
	    for (int i = 0; i < flts.length; i++) {
	        flts[i] = toFloat( new byte[] {
	            data[(i*4)],
	            data[(i*4)+1],
	            data[(i*4)+2],
	            data[(i*4)+3],
	        } );
	    }
	    return flts;
	}
	/* ========================= */
	public static double toDouble(byte[] data) {
	    if (data == null || data.length != 8) return 0x0;
	    // ---------- simple:
	    return Double.longBitsToDouble(toLong(data));
	}
	public static double[] toDoubleA(byte[] data) {
	    if (data == null) return null;
	    // ----------
	    if (data.length % 8 != 0) return null;
	    double[] dbls = new double[data.length / 8];
	    for (int i = 0; i < dbls.length; i++) {
	        dbls[i] = toDouble( new byte[] {
	            data[(i*8)],
	            data[(i*8)+1],
	            data[(i*8)+2],
	            data[(i*8)+3],
	            data[(i*8)+4],
	            data[(i*8)+5],
	            data[(i*8)+6],
	            data[(i*8)+7],
	        } );
	    }
	    return dbls;
	}
	/* ========================= */
	public static boolean toBoolean(byte[] data) {
	    return (data == null || data.length == 0) ? false : data[0] != 0x00;
	}
	public static boolean[] toBooleanA(byte[] data) {
	    // Advanced Technique: Extract the boolean array's length
	    // from the first four bytes in the char array, and then
	    // read the boolean array.
	    // ----------
	    if (data == null || data.length < 4) return null;
	    // ----------
	    int len = toInt(new byte[]{data[0], data[1], data[2], data[3]});
	    boolean[] bools = new boolean[len];
	    // ----- pack bools:
	    for (int i = 0, j = 4, k = 7; i < bools.length; i++) {
	        bools[i] = ((data[j] >> k--) & 0x01) == 1;
	        if (k < 0) { j++; k = 7; }
	    }
	    // ----------
	    return bools;
	}
	/* ========================= */
	public static String toString(byte[] data) {
	    return (data == null) ? null : new String(data);
	}
	public static String[] toStringA(byte[] data) {
	    // Advanced Technique: Extract the String array's length
	    // from the first four bytes in the char array, and then
	    // read the int array denoting the String lengths, and
	    // then read the Strings.
	    // ----------
	    if (data == null || data.length < 4) return null;
	    // ----------
	    byte[] bBuff = new byte[4]; // Buffer
	    // -----
	    System.arraycopy(data, 0, bBuff, 0, 4);
	    int saLen = toInt(bBuff);
	    if (data.length < (4 + (saLen * 4))) return null;
	    // -----
	    bBuff = new byte[saLen * 4];
	    System.arraycopy(data, 4, bBuff, 0, bBuff.length);
	    int[] sLens = toIntA(bBuff);
	    if (sLens == null) return null;
	    // ----------
	    String[] strs = new String[saLen];
	    for (int i = 0, dataPos = 4 + (saLen * 4); i < saLen; i++) {
	        if (sLens[i] > 0) {
	            if (data.length >= (dataPos + sLens[i])) {
	                bBuff = new byte[sLens[i]];
	                System.arraycopy(data, dataPos, bBuff, 0, sLens[i]);
	                dataPos += sLens[i];
	                strs[i] = toString(bBuff);
	            } else return null;
	        }
	    }
	    // ----------
	    return strs;
	}
}
