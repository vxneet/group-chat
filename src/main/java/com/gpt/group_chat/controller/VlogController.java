package com.gpt.group_chat.controller;

import com.gpt.group_chat.model.Group;
import com.gpt.group_chat.model.User;
import com.gpt.group_chat.model.Vlog;
import com.gpt.group_chat.model.VlogStatus;
import com.gpt.group_chat.repository.GroupRepository;
import com.gpt.group_chat.repository.UserRepository;
import com.gpt.group_chat.repository.VlogRepository;
import com.gpt.group_chat.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/vlogs")
public class VlogController {

    private final VlogRepository vlogRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final Random random = new Random();

    public VlogController(VlogRepository vlogRepository, GroupRepository groupRepository,
                          UserRepository userRepository, NotificationService notificationService) {
        this.vlogRepository = vlogRepository;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    @PostMapping("/assign/{groupId}")
    public ResponseEntity<?> assignDailyVlog(@PathVariable Long groupId){
        Optional<Group> groupOpt = groupRepository.findById(groupId);
        if(!groupOpt.isPresent()){
            return ResponseEntity.badRequest().body("Group not found");
        }

        Group group = groupOpt.get();
        LocalDate today = LocalDate.now();

        //Check if today's vlog is already assigned
        Optional<Vlog> existingAssignment = vlogRepository.findTodaysVlogAssignment(groupId, today);
        if(existingAssignment.isPresent()){
            return ResponseEntity.ok(existingAssignment.get());
        }

        //Get all grp members
        Set<User> members = group.getMembers();
        if(members.isEmpty()){
            return ResponseEntity.badRequest().body("No members in the group");
        }

        //randomly select
        List<User> memberList = new ArrayList<>(members);
        User selectedUser = memberList.get(random.nextInt(memberList.size()));

        //Create vlog assignment
        Vlog vlog = new Vlog();
        vlog.setGroup(group);
        vlog.setAssignedUser(selectedUser);
        vlog.setAssignmentDate(today);
        vlog.setStatus(VlogStatus.ASSIGNED);

        Vlog savedVlog = vlogRepository.save(vlog);
        // Send notification
        notificationService.notifyVlogAssigned(savedVlog);

        return ResponseEntity.ok(savedVlog);
    }

    @PostMapping("/submit/{vlogId}")
    public ResponseEntity<?> submitVlog(@PathVariable Long vlogId, @RequestBody VlogSubmissionRequest request){
        Optional<Vlog> vlogOpt = vlogRepository.findById(vlogId);
        if(!vlogOpt.isPresent()){
            return ResponseEntity.badRequest().body("Vlog assignment not found");
        }
        Vlog vlog = vlogOpt.get();
        vlog.setVlogUrl(request.getVlogUrl());
        vlog.setDescription(request.getDescription());
        vlog.setStatus(VlogStatus.SUBMITTED);
        vlog.setSubmittedAt(LocalDateTime.now());

        Vlog savedVlog = vlogRepository.save(vlog);
        // Send notification
        notificationService.notifyVlogSubmitted(savedVlog);

        return ResponseEntity.ok(savedVlog);
    }

    @GetMapping("/user/{userId}")
    public List<Vlog> getUserVlogs(@PathVariable Long userId){
        return vlogRepository.findByAssignedUserIdOrderByCreatedAtDesc(userId);
    }

    @GetMapping("/today/{groupId}")
    public ResponseEntity<?> getTodaysAssignment(@PathVariable Long groupId){
        LocalDate today = LocalDate.now();
        Optional<Vlog> todaysVlog = vlogRepository.findTodaysVlogAssignment(groupId, today);

        if(todaysVlog.isPresent()){
            return ResponseEntity.ok(todaysVlog.get());
        } else{
            return ResponseEntity.ok().body("No vlog assigned for today");
        }
    }

}
