package lab2.itemService.controller;

import lab2.itemService.entity.Item;
import lab2.itemService.service.ItemService;
import lab2.itemService.service.lmpl.ItemServiceImpl;
import lab2.itemService.util.JsonResult;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Controller
@RequestMapping("/item")
@AllArgsConstructor
public class itemController {

    private ItemService itemService = new ItemServiceImpl();

    @RequestMapping(value = "/listAll", method = RequestMethod.GET)
    public HttpEntity<String> viewAll(){
        List<Item> itemList = itemService.seeAll();
        return JsonResult.success(itemList).toHttpEntity();
    }

    @RequestMapping(value = "/findItem", method = RequestMethod.GET)
    public HttpEntity<String> getItem(@RequestParam(value = "id") long id) {
        List<Item> itemList = itemService.findByNUM(id);
        return JsonResult.success(itemList).toHttpEntity();
    }

    @RequestMapping(value = "/addItem", method = RequestMethod.POST)
    public HttpEntity<String> addItem(@RequestBody Item item) {
        if(!itemService.update(item)) {
            return JsonResult.ERROR_BAD_REQUEST.toHttpEntity();
        }
        return JsonResult.SUCCESS.toHttpEntity();
    }

    @RequestMapping(value = "/deleteItem", method = RequestMethod.DELETE)
    public HttpEntity<String> deleteItem(@RequestParam(value = "itNum") long num) {
        if(!itemService.deleteByNum(num)) {
            return  JsonResult.ERROR_BAD_REQUEST.toHttpEntity();
        }
        return JsonResult.SUCCESS.toHttpEntity();
    }

    @RequestMapping(value = "/updateItem", method = RequestMethod.PUT)
    public HttpEntity<String> updateItem(@RequestBody Item item) {
        if (!itemService.update(item)) {
            return JsonResult.ERROR_BAD_REQUEST.toHttpEntity();
        }
        return JsonResult.SUCCESS.toHttpEntity();
    }
}
