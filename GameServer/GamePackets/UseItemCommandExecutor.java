package GameServer.GamePackets;

import Connections.Connection;

public interface UseItemCommandExecutor {
	boolean execute(int ItemID, int DETERMINER,  Connection source);
}
