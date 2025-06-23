import com.lostcity.server.model.entity.player.Player;
import java.io.*;

public class PlayerSaveEditor {
    public static void main(String[] args){
        String playerName = "Unsinkable";
        String saveFilePath = "\"C:\\Users\\pauls\\OneDrive\\Desktop\\University of Glasgow\\Semester 3\\MScProject\\Server-main\\engine\\data\\players\\main\\unsinkable.sav\"";

        Player loadedPlayer = null;
        try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(playerName))){
            loadedPlayer = (Player) in.readObject();
            System.out.println("Player loaded successfully: " + loadedPlayer.getUsername());
        } catch (FileNotFoundException fnfe){
            System.err.println("Player save file wasn't found: " + saveFilePath);
            System.err.println("Make sure the path is correct and contains the file.");
            fnfe.printStackTrace();
            return;
        } catch (IOException ioe){
            System.err.print("ERROR: " + ioe.getMessage());
            ioe.printStackTrace();
            return;
        }

        if (loadedPlayer != null){
            System.out.println();
            loadedPlayer.getCombat().setHitpoints(99);
            loadedPlayer.setRunEnergy(100.0f);
            loadedPlayer.setLocation(new com.lostcity.server.model.world.region.Location(3222, 3222, 0));
        }
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(saveFilePath))){
            out.writeObject(loadedPlayer);
            System.out.println("Player data saved.");
        } catch (IOException ioe){
            System.out.println("Error saving: " + ioe.getMessage());
            ioe.printStackTrace();
        }

    }
}
