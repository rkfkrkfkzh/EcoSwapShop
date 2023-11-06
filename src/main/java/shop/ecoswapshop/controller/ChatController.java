package shop.ecoswapshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import shop.ecoswapshop.domain.ChatMessage;
import shop.ecoswapshop.domain.ChatSession;
import shop.ecoswapshop.domain.Notification;
import shop.ecoswapshop.domain.Product;
import shop.ecoswapshop.exception.ChatSessionNotFoundException;
import shop.ecoswapshop.repository.ChatMessageRepository;
import shop.ecoswapshop.repository.ChatSessionRepository;
import shop.ecoswapshop.repository.NotificationRepository;
import shop.ecoswapshop.repository.ProductRepository;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatMessageRepository chatMessageRepository;
    private final ProductRepository productRepository;
    private final NotificationRepository notificationRepository; // 알림 저장을 위한 Repository
    private final ChatSessionRepository chatSessionRepository;

    @MessageMapping("/chat/{sessionId}")
    @SendTo("/topic/messages/{sessionId}")
    @Transactional
    public ChatMessage sendMessage(@DestinationVariable UUID sessionId,
                                   ChatMessage chatMessage,
                                   Principal principal) {
        // sessionId만으로도 채팅에 메시지를 연결하기 충분하다고 가정합니다.
        // 필요한 다른 속성들을 설정하기 위해 연관된 채팅 세션을 검색해야 합니다.
        ChatSession chatSession = chatSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ChatSessionNotFoundException("Invalid sessionId: " + sessionId));
        // 메시지에 대한 Product 객체를 찾음
        Product product = productRepository.findById(chatSession.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("Invalid productId: " + chatSession.getProductId()));
        // 현재 로그인한 사용자를 확인
        String currentUserId = principal.getName();

        chatMessage.setProduct(product);
        // 메시지에 ChatSession 설정
        chatMessage.setChatSession(chatSession);

        // 채팅 메시지를 저장합니다.
        ChatMessage savedMessage = chatMessageRepository.save(chatMessage);
        if (savedMessage == null) {
            throw new RuntimeException("채팅 메시지를 저장하지 못했습니다.");
        }

        // 알림 생성
        Notification notification = new Notification();
        notification.setSenderId(chatMessage.getSenderId());
        notification.setReceiverId(chatMessage.getReceiverId());
        notification.setContent("새 메시지가 도착했습니다.");
        notification.setProductName(chatMessage.getProduct().getProductName());
        notification.setProductId(chatMessage.getProduct().getId());
        notification.setChatSession(chatMessage.getChatSession()); // 여기에서 ChatSession을 설정합니다.

        notificationRepository.save(notification);
        return savedMessage;
    }

    @GetMapping("/start/{productId}")
    public String startChat(@PathVariable Long productId, Model model, Principal principal) {
        // productId로 제품을 찾고, 그제품의 판매자 memberId를 가져옴
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Invalid productId: " + productId));

        String sellerId = product.getMember().getUsername();
        String buyerId = null;

        String currentUserId = principal.getName();

        List<ChatMessage> chatHistory = chatMessageRepository.findByProduct_IdAndSenderIdAndReceiverId(productId, currentUserId, sellerId);

        if (currentUserId.equals(sellerId)) {
            // 현재 사용자가 판매자인 경우
            chatHistory = chatMessageRepository.findByProduct_IdAndReceiverId(productId, sellerId);
            if (!chatHistory.isEmpty()) {
                buyerId = chatHistory.get(0).getSenderId();
            }
        } else {
            // 현재 사용자가 구매자인 경우
            chatHistory = chatMessageRepository.findByProduct_IdAndSenderIdAndReceiverId(productId, currentUserId, sellerId);
            if (!chatHistory.isEmpty()) {
                buyerId = chatHistory.get(0).getSenderId();
            } else {
                buyerId = currentUserId;
            }
        }

        if (buyerId == null || buyerId.equals(sellerId)) {
            // 구매자 아이디를 찾을 수 없거나 판매자와 동일한 경우의 처리
            // 예) 에러 메시지 출력, 다른 페이지로 리다이렉트 등
            // 이 부분은 실제 시나리오에 맞게 구현 필요
            return "error"; // 예시로 에러 페이지로 리다이렉트
        }

        if (currentUserId.equals(sellerId)) {
            model.addAttribute("receiverId", buyerId);
            model.addAttribute("senderId", sellerId);
        } else {
            model.addAttribute("receiverId", sellerId);
            model.addAttribute("senderId", buyerId);
        }

        // 이미 존재하는 ChatSession을 찾거나 새로 만듭니다.
        ChatSession chatSession = chatSessionRepository.findByProductIdAndSellerIdAndBuyerId(productId, sellerId, buyerId);
        if (chatSession == null) {
            chatSession = new ChatSession();
            chatSession.setProductId(productId);
            chatSession.setSellerId(sellerId);
            chatSession.setBuyerId(buyerId);
            chatSessionRepository.save(chatSession);
        }

        model.addAttribute("sessionId", chatSession.getSessionId());

        model.addAttribute("chatHistory", chatHistory);
        return "chat";
    }

    @GetMapping("/session/{sessionId}")
    public String joinChatSession(@PathVariable UUID sessionId, Model model, Principal principal) {
        ChatSession chatSession = chatSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ChatSessionNotFoundException("Invalid sessionId: " + sessionId));

        String currentUserId = principal.getName();

        // 사용자가 세션의 구매자 또는 판매자인지 확인
        if (!currentUserId.equals(chatSession.getBuyerId()) && !currentUserId.equals(chatSession.getSellerId())) {
            // 사용자가 해당 세션의 구매자나 판매자가 아니라면 접근을 거부합니다.
            throw new AccessDeniedException("You do not have permission to access this chat session.");
        }

        List<ChatMessage> chatHistory = chatMessageRepository.findByChatSession_SessionId(sessionId);
        model.addAttribute("chatHistory", chatHistory);
        model.addAttribute("sessionId", chatSession.getSessionId());
        model.addAttribute("receiverId", chatSession.getBuyerId().equals(currentUserId) ? chatSession.getSellerId() : chatSession.getBuyerId());
        model.addAttribute("senderId", chatSession.getSellerId());

        return "chat";
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public static class ProductNotFoundException extends RuntimeException {
        public ProductNotFoundException(String message) {
            super(message);
        }
    }
}

