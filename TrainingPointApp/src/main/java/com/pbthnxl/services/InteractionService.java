/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.pbthnxl.services;

import com.pbthnxl.pojo.Interaction;

/**
 *
 * @author DELL
 */
public interface InteractionService {
    void save(Interaction i);
    void delete(int id);
    Interaction getInteractionByUserIdAndActivityId(int userId, int activityId);
    Interaction getInteractionByUserIdAndCommentId(int userId, int commentId);
}
