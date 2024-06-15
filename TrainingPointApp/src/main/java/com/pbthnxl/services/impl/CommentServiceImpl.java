/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.services.impl;

import com.pbthnxl.dto.CommentDTO;
import com.pbthnxl.pojo.Comment;
import com.pbthnxl.repositories.CommentRepository;
import com.pbthnxl.services.CommentService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author hieu
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepo;

    @Override
    public List<Comment> getCommentsByActivityId(int activityId) {
        return this.commentRepo.getCommentsByActivityId(activityId);
    }

    @Override
    public void saveOrUpdate(Comment comment) {
        this.commentRepo.saveOrUpdate(comment);
    }

    @Override
    public void delete(int id) {
        this.commentRepo.delete(id);
    }

    @Override
    public CommentDTO convertToDTO(Comment c, String username) {
        CommentDTO dto = new CommentDTO();
        
        dto.setId(c.getId());
        dto.setUserId(c.getUserId().getId());
        dto.setContent(c.getContent());
        dto.setCreatedAt(c.getCreatedAt());
        dto.setName(c.getUserId().getLastName() + " " + c.getUserId().getFirstName());
        dto.setAvatar(c.getUserId().getAvatar());
        dto.setLikes(this.commentRepo.countLikes(c.getId()));
        dto.setLiked(this.commentRepo.isUserLikedComment(c.getId(), username));
        
        return dto;
    }

    @Override
    public Comment getCommentById(int id) {
        return this.commentRepo.getCommentById(id);
    }

    @Override
    public int countLikes(int id) {
        return this.commentRepo.countLikes(id);
    }

    @Override
    public boolean isUserLikedComment(Integer commentId, String username) {
        return this.commentRepo.isUserLikedComment(commentId, username);
    }
    
}
