package lab2.itemService.service;

import lab2.itemService.entity.Item;

import java.util.ArrayList;
import java.util.List;

public interface ItemService {
    
    List<Item> findByNUM(long id);

    List<Item> findByName(String name);

    List<Item> seeAll();

    boolean deleteByNum(long id);

    boolean update(Item item);
}
