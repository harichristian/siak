/**
 * Copyright 2010 the original author or authors.
 *
 * This file is part of Zksample2. http://zksample2.sourceforge.net/
 *
 * Zksample2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Zksample2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Zksample2.  If not, see <http://www.gnu.org/licenses/gpl.html>.
 */
package id.ac.idu.backend.service.impl;

import id.ac.idu.backend.dao.ArticleDAO;
import id.ac.idu.backend.model.Article;
import id.ac.idu.backend.service.ArticleService;

import java.util.List;

/**
 * EN: Service implementation for methods that depends on <b>Articles</b>.<br>
 * DE: Service Methoden Implementierung betreffend <b>Artikel</b>.<br>
 *
 * @author bbruhns
 * @author sgerth
 */
public class ArticleServiceImpl implements ArticleService {

    private ArticleDAO articleDAO;

    public ArticleDAO getArticleDAO() {
        return articleDAO;
    }

    public void setArticleDAO(ArticleDAO articleDAO) {
        this.articleDAO = articleDAO;
    }

    @Override
    public Article getNewArticle() {
        return getArticleDAO().getNewArticle();
    }

    @Override
    public void delete(Article entity) {
        getArticleDAO().delete(entity);
    }

    @Override
    public List<Article> getAllArticles() {
        return getArticleDAO().getAllArticles();
    }

    @Override
    public Article getArticleById(long art_id) {
        return null;
    }

    @Override
    public void saveOrUpdate(Article entity) {
        getArticleDAO().saveOrUpdate(entity);
    }

    @Override
    public List<Article> getArticlesLikeArticleNumber(String text) {
        return getArticleDAO().getArticlesLikeArticleNumber(text);
    }

    @Override
    public List<Article> getArticlesLikeName(String text) {
        return getArticleDAO().getArticlesLikeName(text);
    }

    @Override
    public int getCountAllArticles() {
        return getArticleDAO().getCountAllArticles();
    }

}
