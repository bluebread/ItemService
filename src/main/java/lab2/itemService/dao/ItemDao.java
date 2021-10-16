package lab2.itemService.dao;

import lab2.itemService.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemDao extends JpaRepository<Item, Long>{

    List<Item> findByitemNum(long itenNum);

    List<Item> findByitemName(String name);

    void deleteByitemNum(long itemNum);

}
