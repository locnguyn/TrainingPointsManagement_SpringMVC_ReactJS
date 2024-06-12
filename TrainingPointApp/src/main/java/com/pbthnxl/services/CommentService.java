/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.pbthnxl.services;


import com.pbthnxl.dto.CommentDTO;
import com.pbthnxl.pojo.Comment;
import java.util.List;

/**
 *
 * @author hieu
 */
public interface CommentService {
    List<Comment> getCommentsByActivityId(int activityId);
    void save(Comment comment);
    void delete(int id);
    CommentDTO convertToDTO(Comment c);
}
