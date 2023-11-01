package shop.ecoswapshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import shop.ecoswapshop.domain.Notification;
import shop.ecoswapshop.repository.NotificationRepository;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationRepository notificationRepository;

    @GetMapping
    public String getAllNotifications(Model model, Principal principal) {
        List<Notification> notifications = notificationRepository.findByReceiverId(principal.getName());
        model.addAttribute("notifications", notifications);
        return "notifications";  // Thymeleaf의 notifications.html 템플릿을 의미합니다.
    }

    @GetMapping("/{notificationId}/read")
    public String readNotification(@PathVariable Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(() ->
                new RuntimeException("Notification not found with ID: " + notificationId));
        notification.setRead(true);  // 예를 들어 'read'라는 boolean 필드가 있다고 가정
        notificationRepository.save(notification);
        return "redirect:/notifications";  // 알림 페이지로 다시 리다이렉트합니다.
    }
}
