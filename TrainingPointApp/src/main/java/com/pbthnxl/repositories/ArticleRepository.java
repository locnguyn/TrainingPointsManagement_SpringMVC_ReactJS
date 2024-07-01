/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.pbthnxl.repositories;

import com.pbthnxl.pojo.Article;
import java.util.List;

/**
 *
 * @author DELL
 */
public interface ArticleRepository {
    List<Article> getArticles();
    Article getArticleById(int id);
    void addOrUpdate(Article c);
    void delete(Article c);
    Article findByName(String name);
}
