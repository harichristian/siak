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
package id.ac.idu.backend.dao;

import id.ac.idu.backend.model.Article;

import java.util.List;

/**
 * EN: DAO methods Interface for working with Article data.<br>
 * DE: DAO Methoden Interface fuer die Artikel Daten.<br>
 *
 * @author bbruhns
 * @author sgerth
 */
public interface ArticleDAO {

    /**
     * EN: Get a new Article object.<br>
     * DE: Gibt ein neues Artikel Objekt zurueck.<br>
     *
     * @return Article
     */
    public Article getNewArticle();

    /**
     * EN: Get a list of all Articles.<br>
     * DE: Gibt eine Liste aller Artikel zurueck.<br>
     *
     * @return List of Articles / Liste von Artikeln
     */
    public List<Article> getAllArticles();

    /**
     * EN: Get the count of all Articles.<br>
     * DE: Gibt die Anzahl aller Artikel zurueck.<br>
     *
     * @return int
     */
    public int getCountAllArticles();

    /**
     * EN: Get an article by its ID.<br>
     * DE: Gibt einen Artikel anhand seiner ID zurueck.<br>
     *
     * @param id / the persistence identifier / der PrimaerKey
     * @return Article / Artikel
     */
    public Article getArticleById(long id);

    /**
     * EN: Gets a list of Articles where the articleNo is like %text% .<br>
     * DE: Gibt eine Liste aller Artikel zurueck deren ArtikelNummer gleich like
     * %text% ist.<br>
     *
     * @param text the matching text / zu uebereinstimmende Zeichenfolge
     * @return List of Articles / Liste of Artikel
     */
    public List<Article> getArticlesLikeArticleNumber(String text);

    /**
     * EN: Gets a list of Articles where the article name contains the %string%
     * .<br>
     * DE: Gibt eine Liste aller Artikel zurueck bei denen der ArtikelName
     * %string% enthaelt.<br>
     *
     * @param text the matching text / zu uebereinstimmende Zeichenfolge
     * @return List of Articles / Liste of Artikel
     */
    public List<Article> getArticlesLikeName(String string);

    /**
     * EN: Saves new or updates an article.<br>
     * DE: Speichert neu oder aktualisiert einen Artikel.<br>
     */
    public void saveOrUpdate(Article entity);

    /**
     * EN: Deletes an article.<br>
     * DE: Loescht einen Artikel.<br>
     */
    public void delete(Article entity);

    /**
     * EN: Saves an article.<br>
     * DE: Speichert einen Artikel.<br>
     */
    public void save(Article entity);

}
