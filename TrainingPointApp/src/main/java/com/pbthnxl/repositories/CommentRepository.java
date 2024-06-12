/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.repositories;

import com.pbthnxl.pojo.Comment;
import java.util.List;

/**
 *
 * @author hieu
 */
public interface CommentRepository {

    List<Comment> getCommentsByActivityId(int activityId);
    void save(Comment comment);
    void delete(int id);

}
