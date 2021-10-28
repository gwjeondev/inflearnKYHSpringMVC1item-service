package hello.itemservice.domain.item;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class ItemRepositoryTest {

    private ItemRepository itemRepository = new ItemRepository();

    @AfterEach
    void afterEach() {
        itemRepository.clearStore();
    }

    @Test
    void save() {
        //given
        Item item = new Item("itemA", 36000, 5);

        //when
        Item savedItem = itemRepository.save(item);

        //then
        Item findItem = itemRepository.findById(item.getId());
        assertThat(savedItem).isSameAs(findItem);
    }

    @Test
    void findById() {
    }

    @Test
    void findAll() {
        //given
        Item itemA = new Item("itemA", 36000, 5);
        Item itemB = new Item("itemB", 32500, 10);

        Item savedItemA = itemRepository.save(itemA);
        Item savedItemB = itemRepository.save(itemB);

        //when
        List<Item> resultItems = itemRepository.findAll();

        //then
        assertThat(2).isEqualTo(resultItems.size());
        assertThat(resultItems).contains(itemA, itemB);
    }

    @Test
    void update() {
        //given
        Item item = new Item("item", 25000, 25);
        Item savedItem = itemRepository.save(item);

        //when
        Item updateItem = new Item("updateItem", 15000, 50);
        itemRepository.update(item.getId(), updateItem);

        //then
        assertThat(item.getItemName()).isEqualTo(updateItem.getItemName());
    }
}
