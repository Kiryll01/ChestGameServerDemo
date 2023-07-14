package com.example.chestGameServer.Repositories;
import com.example.chestGameServer.Models.Game.GameRoom;
import org.springframework.data.repository.CrudRepository;

public interface RoomRepository extends CrudRepository<GameRoom,String> {

}
