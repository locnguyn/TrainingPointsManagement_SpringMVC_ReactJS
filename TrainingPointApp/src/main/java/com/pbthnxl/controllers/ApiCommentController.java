  /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.controllers;

import com.pbthnxl.pojo.Comment;
import com.pbthnxl.pojo.Interaction;
import com.pbthnxl.services.CommentService;
import com.pbthnxl.services.InteractionService;
import com.pbthnxl.services.UserService;
import java.security.Principal;
import java.util.Date;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author DELL
 */
@CrossOrigin
@RestController
@RequestMapping("/api/comment")
public class ApiCommentController {
    @Autowired
    private CommentService commentService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private InteractionService interactionService;
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable(value = "id") Integer id, Principal p) {
        Comment c = commentService.getCommentById(id);
        if(c != null && c.getUserId().getUsername().equals(p.getName())){
            this.commentService.delete(c.getId());
            return new ResponseEntity<>("Deleted successfully!", HttpStatus.OK);
        }
        return new ResponseEntity<>("Delete failed!", HttpStatus.BAD_REQUEST);
    }
    
    @PatchMapping(path = "/{id}", consumes = {
        MediaType.APPLICATION_JSON_VALUE
    })
    public ResponseEntity<String> update(@PathVariable(value = "id") Integer id, @RequestBody Map<String, String> params, Principal p) {
        Comment c = commentService.getCommentById(id);
        if(c != null && c.getUserId().getUsername().equals(p.getName())){
            c.setContent(params.get("content"));
            this.commentService.saveOrUpdate(c);
            return new ResponseEntity<>("Updated successfully!", HttpStatus.OK);
        }
        return new ResponseEntity<>("Update failed!", HttpStatus.BAD_REQUEST);
    }
    
    @PostMapping("/{id}/like/")
    public ResponseEntity<String> likeOrUnlike(@PathVariable(value = "id") Integer id, Principal p) {
        Integer userId = this.userService.getIdByUsername(p.getName());

        Interaction interaction = this.interactionService.getInteractionByUserIdAndCommentId(userId, id);

        if (interaction == null) {
            interaction = new Interaction();
            interaction.setUserId(this.userService.getUserByUsername(p.getName()));
            interaction.setCommentId(this.commentService.getCommentById(id));
            interaction.setCreatedAt(new Date());

            this.interactionService.save(interaction);
        } else {
            this.interactionService.delete(interaction.getId());
        }

        return new ResponseEntity<>("Successfully!", HttpStatus.OK);
    }
}
