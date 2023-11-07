package shop.ecoswapshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import shop.ecoswapshop.repository.NotificationRepository;

import java.security.Principal;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {

    private final NotificationRepository notificationRepository;

    @ModelAttribute("unreadNotificationsCount")
    public long getUnreadNotidicationsCount(Principal principal) {
        if (principal != null) {
            return notificationRepository.countByReceiverIdAndIsRead(principal.getName(),false);
        }
        return 0;
    }
}
