/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.formatters;

import com.pbthnxl.pojo.Article;
import java.text.ParseException;
import java.util.Locale;
import org.springframework.format.Formatter;

/**
 *
 * @author DELL
 */
public class ArticleFormatter implements Formatter<Article> {

    @Override
    public String print(Article object, Locale locale) {
        return String.valueOf(object.getId());
    }

    @Override
    public Article parse(String id, Locale locale) throws ParseException {
        Article c = new Article();
        c.setId(Integer.parseInt(id));
        
        return c;
    }
    
}
