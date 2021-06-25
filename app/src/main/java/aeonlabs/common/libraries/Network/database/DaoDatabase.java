package aeonlabs.common.libraries.Network.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DaoDatabase {

    @Query("SELECT * FROM queue")
    List<EntityQueue> getAllQueues();

   @Query("SELECT CodQueue FROM queue where Title LIKE  :title AND Description LIKE :description AND Url LIKE :url")
    int GetQueueCode(String title, String description, String url);

    @Query("SELECT COUNT(*) from queue")
    int countQueue();

    @Insert
    void insertQueue(EntityQueue... queue);

    @Query("DELETE FROM queue where CodQueue LIKE  :cod_queue")
    void deleteQueue(int cod_queue);



    @Query("SELECT * FROM fields where CodQueue like :cod_queue")
    List<EntityFields> getAllFields(int cod_queue);

    @Query("SELECT COUNT(*) from fields where CodQueue like :cod_queue")
    int countFields(int cod_queue);

    @Insert
    void insertField(EntityFields... fields);

   @Query("DELETE FROM fields where CodQueue LIKE  :cod_queue")
   void deleteFieldByQueue(int cod_queue);



    @Query("SELECT * FROM files where CodQueue like :cod_queue")
    List<EntityFiles> getAllFiles(int cod_queue);

    @Query("SELECT COUNT(*) from files where CodQueue like :cod_queue")
    int countFiles(int cod_queue);

    @Insert
    void insertFile(EntityFiles... file);

    @Delete
    void deleteFile(EntityFiles cod_file);

 @Query("DELETE FROM files where CodQueue LIKE  :cod_queue")
 void deleteFileByQueue(int cod_queue);

}