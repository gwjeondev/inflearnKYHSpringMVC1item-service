package hello.itemservice.web.basic;


import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);

        return "basic/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);

        return "basic/item";
    }

    @GetMapping("/add")
    public String addForm() {
        return "basic/addForm";
    }

/*    ModelAttribute는 사실 queryParameter를 받는 역할도 수행하지만 model 객체에 자동으로 addAttribute 역할을 수행해준다.
    보통 @ModelAttribute("name")의 value 값이 model의 name이 되는데 이 value 역시 생략하면 클래스의 이름으로 model 객체에 등록 해준다.
    ex1) @ModelAttribute("item") Item item -> model.addAttribute("item", item)
    ex2) @ModelAttribute Item item -> model.addAttribute("item", item): 클래스명이 첫글자를 소문자로 변경후 model attribute의 이름이 된다.
    ex3) Item item -> model.addAttribute("item", item): ModelAttribute를 생략할 수도 있다.
    */
    @PostMapping("/add")
    public String addItem(@ModelAttribute Item item, RedirectAttributes redirectAttributes) {
        Item savedItem = itemRepository.save(item);

        //redirectAttribute를 이용시 url 인코딩도 지원, pathVariable이나 queryParameter로 활용 가능함.
        //redirect url에 포함 될 경우 pathVariable로 넘어가고 아닐 경우 queryParameter로 넘어간다.
        //다음과 같은 경우는 itemId는 pathVariable로 이용되고 status는 queryParameter로 이용된다.
        //redirect:/basic/items/{itemId}?status=true
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);

        //return "redirect:";
        //return "basic/item";
        //return "redirect:/basic/items/" + item.getId();  //prg post redirect get
        return "redirect:/basic/items/{itemId}"; //prg post redirect get
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String editItem(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/basic/items/{itemId}";
    }

    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct
    public void init() {
        Item itemA = new Item("itemA", 10000, 10);
        Item itemB = new Item("itemB", 10000, 10);
        itemRepository.save(itemA);
        itemRepository.save(itemB);
    }
}
