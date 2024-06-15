/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.controllers;

import com.pbthnxl.dto.ActivityDTO;
import com.pbthnxl.dto.CommentDTO;
import com.pbthnxl.pojo.Comment;
import com.pbthnxl.pojo.Interaction;
import com.pbthnxl.pojo.Registration;
import com.pbthnxl.services.ActivityParticipationTypeService;
import com.pbthnxl.services.ActivityService;
import com.pbthnxl.services.ArticleService;
import com.pbthnxl.services.CommentService;
import com.pbthnxl.services.FacultyService;
import com.pbthnxl.services.InteractionService;
import com.pbthnxl.services.RegistrationService;
import com.pbthnxl.services.UserService;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author hieu
 */
@RestController
@RequestMapping("/api/activity")
public class ApiActivityController {

    @Autowired
    private ActivityService acService;
    @Autowired
    private FacultyService facultyService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private RegistrationService regisService;
    @Autowired
    private UserService userSerivce;
    @Autowired
    private ActivityParticipationTypeService acPartService;
    @Autowired
    private CommentService coService;
    @Autowired
    private InteractionService interactionService;

    // danh sach hoat dong
    @CrossOrigin
    @RequestMapping("/list/")
    public ResponseEntity<List<ActivityDTO>> list(@RequestParam Map<String, String> params) {

        return new ResponseEntity<>(this.acService.findFilteredActivitiesDTO(params), HttpStatus.OK);
    }

    // dang ky hoat dong
    @CrossOrigin
    @PostMapping("/registration/")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestParam Map<String, String> params, Principal p) {
        Registration r = new Registration();

        r.setStudentId(this.userSerivce.getUserByUsername(p.getName()).getStudent());
        r.setActivityParticipationTypeId(acPartService.getActivityParticipationTypeById(Integer.parseInt(params.get("acPartTypeId"))));
        r.setRegistrationDate(new Date());

        this.regisService.save(r);
    }

    // chi tiet hoat dong
    @CrossOrigin
    @GetMapping("/{id}")
    public ResponseEntity<ActivityDTO> activiyDetails(@PathVariable(value = "id") Integer id, Principal p) {
        System.out.println(p.getName());
        return new ResponseEntity<>(this.acService.getActivityByIdDTO(id, p.getName()), HttpStatus.OK);
    }

    // them comment cho hoat dong
    @CrossOrigin
    @PostMapping(path = "/{id}/add-comment/", consumes = {
        MediaType.APPLICATION_JSON_VALUE
    })
    public ResponseEntity<CommentDTO> addComment(@PathVariable(value = "id") Integer id, @RequestBody Map<String, String> params, Principal p) {
        Comment c = new Comment();

        c.setUserId(this.userSerivce.getUserByUsername(p.getName()));
        c.setActivityId(this.acService.getActivityById(id));
        c.setContent(params.get("content"));
        c.setCreatedAt(new Date());

        this.coService.saveOrUpdate(c);

        return new ResponseEntity<>(this.coService.convertToDTO(c, p.getName()), HttpStatus.CREATED);
    }

    @CrossOrigin
    @PostMapping("/{id}/like/")
    public ResponseEntity<String> likeOrUnlike(@PathVariable(value = "id") Integer id, Principal p) {
        Integer userId = this.userSerivce.getIdByUsername(p.getName());

        Interaction interaction = this.interactionService.getInteractionByUserIdAndActivityId(userId, id);

        if (interaction == null) {
            interaction = new Interaction();
            interaction.setUserId(this.userSerivce.getUserByUsername(p.getName()));
            interaction.setActivityId(this.acService.getActivityById(id));
            interaction.setCreatedAt(new Date());

            this.interactionService.save(interaction);
        } else {
            this.interactionService.delete(interaction.getId());
        }

        return new ResponseEntity<>("Successfully!", HttpStatus.OK);
    }

}
