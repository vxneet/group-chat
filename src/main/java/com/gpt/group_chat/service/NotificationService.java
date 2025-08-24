package com.gpt.group_chat.service;

import com.gpt.group_chat.controller.NotificationResponse;
import com.gpt.group_chat.model.*;
import com.gpt.group_chat.repository.NotificationRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public NotificationService(NotificationRepository notificationRepository, SimpMessagingTemplate messagingTemplate){
        this.notificationRepository = notificationRepository;
        this.messagingTemplate = messagingTemplate;
    }

    public void notifyVlogAssigned(Vlog vlog){
        User assignedUser = vlog.getAssignedUser();
        Group group = vlog.getGroup();

        //Create Notification for the user
        Notification notification = new Notification();
        notification.setRecipient(assignedUser);
        notification.setGroup(group);
        notification.setType(NotificationType.VLOG_ASSIGNED);
        notification.setTitle("Vlog Assignment");
        notification.setMessage("You've been selected to create today's vlog for "+group.getName());
        notification.setRelatedVlog(vlog);

        notificationRepository.save(notification);

        //Send notification (real time mein)
        NotificationResponse response = new NotificationResponse(
                notification.getId(),
                notification.getTitle(),
                notification.getMessage(),
                notification.getType().toString(),
                notification.getCreatedAt()
        );

        messagingTemplate.convertAndSend("/topic/notifications/"+assignedUser.getId(), response);
    }
    public void notifyVlogSubmitted(Vlog vlog) {
        Group group = vlog.getGroup();
        Set<User> members = group.getMembers();

        for (User member : members) {
            if (!member.getId().equals(vlog.getAssignedUser().getId())) {
                Notification notification = new Notification();
                notification.setRecipient(member);
                notification.setGroup(group);
                notification.setType(NotificationType.VLOG_SUBMITTED);
                notification.setTitle("New Vlog Posted");
                notification.setMessage(vlog.getAssignedUser().getUsername() + " has submitted their vlog!");
                notification.setRelatedVlog(vlog);

                notificationRepository.save(notification);

                // Send real-time notification
                NotificationResponse response = new NotificationResponse(
                        notification.getId(),
                        notification.getTitle(),
                        notification.getMessage(),
                        notification.getType().toString(),
                        notification.getCreatedAt()
                );

                messagingTemplate.convertAndSend("/topic/notifications/" + member.getId(), response);
            }
        }
    }

    public void notifyGroupMessage(Message message) {
        Group group = message.getGroup();
        Set<User> members = group.getMembers();

        for (User member : members) {
            if (!member.getId().equals(message.getSender().getId())) {
                Notification notification = new Notification();
                notification.setRecipient(member);
                notification.setGroup(group);
                notification.setType(NotificationType.GROUP_MESSAGE);
                notification.setTitle("New Message");
                notification.setMessage(message.getSender().getUsername() + ": " +
                        (message.getContent().length() > 50 ?
                                message.getContent().substring(0, 50) + "..." :
                                message.getContent()));

                notificationRepository.save(notification);

                // Send real-time notification
                NotificationResponse response = new NotificationResponse(
                        notification.getId(),
                        notification.getTitle(),
                        notification.getMessage(),
                        notification.getType().toString(),
                        notification.getCreatedAt()
                );

                messagingTemplate.convertAndSend("/topic/notifications/" + member.getId(), response);
            }
        }
    }
}
