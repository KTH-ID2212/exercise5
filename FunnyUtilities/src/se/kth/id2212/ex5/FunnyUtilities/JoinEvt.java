package se.kth.id2212.ex5.FunnyUtilities;

import java.io.Serializable;
import java.util.UUID;

public class JoinEvt implements Serializable
{
    private final UUID playerID;
    private final String player;
    private final int avatar;

    public JoinEvt(UUID playerID, String player, int avatar)
    {
        this.playerID = playerID;
        this.player = player;
        this.avatar = avatar;
    }

    public UUID getPlayerID()
    {
        return playerID;
    }

    public String getPlayer()
    {
        return player;
    }

    public int getAvatar()
    {
        return avatar;
    }

    @Override
    public String toString()
    {
        return "JoinEvt{" +
                "player='" + player + '\'' +
                ", avatar=" + avatar +
                '}';
    }
}
