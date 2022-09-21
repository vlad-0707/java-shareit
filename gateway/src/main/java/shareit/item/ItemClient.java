package shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import shareit.client.BaseClient;
import shareit.item.dto.CommentDto;
import shareit.item.dto.ItemDto;


import java.util.Map;

@Service
public class ItemClient extends BaseClient {

    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> search(Long userId, String text, int from, int size) {
        Map<String, Object> param = Map.of(
                "text", text,
                "from", from,
                "size", size
        );
        return get("/search?text={text}&from={from}&size={size}", userId, param);
    }

    public ResponseEntity<Object> getAllItemsByOwnerId(long userId, int from, int size) {
        Map<String, Object> param = Map.of(
                "from", from,
                "size", size
        );
        return get("", userId, param);
    }

    public ResponseEntity<Object> getId(long itemId, long userId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> addComment(long itemId, CommentDto commentDto, long userId) {
        return post("/" + itemId + "/comment", userId, commentDto);
    }

    public ResponseEntity<Object> updateItem(ItemDto itemDto, long itemId, long userId) {
        return patch("/" + itemId, userId, itemDto);
    }

    public ResponseEntity<Object> addNewItem(ItemDto itemDto, long userId) {
        return post("/", userId, itemDto);
    }
}
