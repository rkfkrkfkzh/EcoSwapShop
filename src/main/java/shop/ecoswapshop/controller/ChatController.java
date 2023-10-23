package shop.ecoswapshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import shop.ecoswapshop.domain.ChatMessage;
import shop.ecoswapshop.domain.Product;
import shop.ecoswapshop.repository.ChatMessageRepository;
import shop.ecoswapshop.repository.ProductRepository;

import java.security.Principal;

@Controller
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatMessageRepository chatMessageRepository;
    private final ProductRepository productRepository;

    @MessageMapping("/chat/{receiverId}")
    @SendTo("/topic/{receiverId}")
    public ChatMessage sendMessage(@DestinationVariable String receiverId, ChatMessage chatMessage) {
        // 메시지를 데이터베이스에 저장
        chatMessageRepository.save(chatMessage);
        return chatMessage;
    }

    @GetMapping("/start/{productId}")
    public String startChat(@PathVariable Long productId, Model model, Principal principal) {
        // productId로 제품을 찾고, 그제품의 판매자 memberId를 가져옴
        Product product = productRepository.findById(productId).
                orElseThrow(() -> new IllegalArgumentException("Invalid productId: " + productId));
        String receiverId = product.getMember().getId().toString();
        model.addAttribute("receiverId", receiverId);
        model.addAttribute("senderId", principal.getName()); // 현재 로그인한 사용자의 ID
        return "chat";
    }
}

