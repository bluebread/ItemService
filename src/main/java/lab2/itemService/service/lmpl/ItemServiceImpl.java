package lab2.itemService.service.lmpl;

import lab2.itemService.dao.ItemDao;
import lab2.itemService.entity.Item;
import lab2.itemService.service.ItemService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service("itemService")
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {

    private ItemDao itemDao;

    public ItemServiceImpl()
    {
        String hello = "Hello, world";
        System.out.println(hello);
    }

    @Override
    public List<Item> findByNUM(long num) {
        return new ArrayList<Item>();
        // return itemDao.findByitemNum(num);
    }

    @Override
    public List<Item> findByName(String name) {
        return new ArrayList<Item>();
        // return itemDao.findByitemName(name);
    }

    @Override
    public List<Item> seeAll() {
        return new ArrayList<Item>();
        // return itemDao.findAll();
    }

    @Override
    public boolean deleteByNum(long id) {
        return true;
        // List<Item> item = itemDao.findByitemNum(id);
        // if(Objects.isNull(item)) {
        //     return false;
        // }
        // itemDao.deleteByitemNum(id);
        // return true;
    }

    @Override
    public boolean update(Item item) {
        return true;
        // List<Item> it = itemDao.findByitemNum(item.getItemNum());
        // if(Objects.isNull(it)) {
        //     return false;
        // }
        // itemDao.save(item);
        // return true;
    }
}
