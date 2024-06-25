/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.services.impl;

import com.pbthnxl.pojo.Article;
import com.pbthnxl.repositories.ArticleRepository;
import com.pbthnxl.services.ArticleService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author DELL
 */
@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleRepository articleRepository;

    @Override
    public List<Article> getArticles() {
        return this.articleRepository.getArticles();
    }

    @Override
    public Article getArticleById(int id) {
        return this.articleRepository.getArticleById(id);
    }

    @Override
    public void addOrUpdate(Article c) {
        this.articleRepository.addOrUpdate(c);
    }

    @Override
    public void delete(Article c) {
        this.articleRepository.delete(c);
    }

    @Override
    public Article findByName(String name) {
        return this.articleRepository.findByName(name);
    }
    
}
