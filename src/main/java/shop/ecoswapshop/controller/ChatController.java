package shop.ecoswapshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import shop.ecoswapshop.domain.ChatMessage;
import shop.ecoswapshop.domain.Product;
import shop.ecoswapshop.repository.ChatMessageRepository;
import shop.ecoswapshop.repository.ProductRepository;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatMessageRepository chatMessageRepository;
    private final ProductRepository productRepository;

    @MessageMapping("/chat/{receiverId}/{productId}")
    @SendTo("/topic/{receiverId}/{productId}")
    @Transactional
    public ChatMessage sendMessage(@DestinationVariable String receiverId,
                                   @DestinationVariable Long productId,
                                   ChatMessage chatMessage) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Invalid productId: " + productId));
        chatMessage.setProduct(product);
        // 메시지를 데이터베이스에 저장
        ChatMessage savedMessage = chatMessageRepository.save(chatMessage);
        if (savedMessage == null) {
            throw new RuntimeException("Failed to save the chat message");
        }
        return savedMessage;
    }

    @GetMapping("/start/{productId}")
    public String startChat(@PathVariable Long productId, Model model, Principal principal) {
        // productId로 제품을 찾고, 그제품의 판매자 memberId를 가져옴
        Product product = productRepository.findById(productId).
                orElseThrow(() -> new ProductNotFoundException("Invalid productId: " + productId));
        String receiverId = product.getMember().getUsername();
        model.addAttribute("receiverId", receiverId);
        model.addAttribute("senderId", principal.getName()); // 현재 로그인한 사용자의 ID

        List<ChatMessage> chatHistory = chatMessageRepository.findByProduct_Id(productId);
        model.addAttribute("chatHistory", chatHistory);
        return "chat";
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public static class ProductNotFoundException extends RuntimeException {
        public ProductNotFoundException(String message) {
            super(message);
        }
    }

    @GetMapping("/rooms")
    public ResponseEntity<List<ChatMessage>> getMyRooms(Principal principal) {
        String currentUserId = principal.getName();
        List<ChatMessage> messages = chatMessageRepository.findByReceiverId(currentUserId);
        return ResponseEntity.ok(messages);
    }

}

