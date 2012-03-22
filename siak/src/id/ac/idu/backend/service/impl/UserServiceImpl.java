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

import id.ac.idu.backend.dao.*;
import id.ac.idu.backend.model.*;
import id.ac.idu.backend.service.UserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * EN: Service implementation for methods that depends on <b>Users</b>.<br>
 * DE: Service Methoden Implementierung betreffend <b>Users -Benutzer</b>.<br>
 *
 * @author bbruhns
 * @author sgerth
 */
public class UserServiceImpl implements UserService {
    private UserDAO userDAO;
    private OfficeDAO officeDAO;
    private SecUserroleDAO secUserroleDAO;
    private SecRoleDAO secRoleDAO;
    private SecRolegroupDAO secRolegroupDAO;
    private SecGrouprightDAO secGrouprightDAO;
    private SecGroupDAO secGroupDAO;
    private LanguageDAO languageDAO;

    private SecRightDAO secRightDAO;

    public LanguageDAO getLanguageDAO() {
        return languageDAO;
    }

    public void setLanguageDAO(LanguageDAO languageDAO) {
        this.languageDAO = languageDAO;
    }

    public SecGroupDAO getSecGroupDAO() {
        return secGroupDAO;
    }

    public void setSecGroupDAO(SecGroupDAO secGroupDAO) {
        this.secGroupDAO = secGroupDAO;
    }

    public SecGrouprightDAO getSecGrouprightDAO() {
        return secGrouprightDAO;
    }

    public void setSecGrouprightDAO(SecGrouprightDAO secGrouprightDAO) {
        this.secGrouprightDAO = secGrouprightDAO;
    }

    public SecRolegroupDAO getSecRolegroupDAO() {
        return secRolegroupDAO;
    }

    public void setSecRolegroupDAO(SecRolegroupDAO secRolegroupDAO) {
        this.secRolegroupDAO = secRolegroupDAO;
    }

    public SecUserroleDAO getSecUserroleDAO() {
        return secUserroleDAO;
    }

    public void setSecUserroleDAO(SecUserroleDAO secUserroleDAO) {
        this.secUserroleDAO = secUserroleDAO;
    }

    public SecRoleDAO getSecRoleDAO() {
        return secRoleDAO;
    }

    public void setSecRoleDAO(SecRoleDAO secRoleDAO) {
        this.secRoleDAO = secRoleDAO;
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public OfficeDAO getOfficeDAO() {
        return officeDAO;
    }

    public void setOfficeDAO(OfficeDAO officeDAO) {
        this.officeDAO = officeDAO;
    }

    @Override
    public SecUser getNewUser() {
        return getUserDAO().getNewSecUser();
    }

    @Override
    public List<SecUser> getAllUsers() {
        return getUserDAO().getAllUsers();
    }

    @Override
    public void saveOrUpdate(SecUser user) {
        getUserDAO().saveOrUpdate(user);
    }

    @Override
    public void delete(SecUser user) {
        getUserDAO().delete(user);
    }

    @Override
    public SecUser getUserByLoginname(final String userName) {
        return getUserDAO().getUserByLoginname(userName);
    }

    @Override
    public List<SecRole> getRolesByUser(SecUser user) {
        return getSecRoleDAO().getRolesByUser(user);
    }

    @Override
    public List<SecRole> getAllRoles() {
        return getSecRoleDAO().getAllRoles();
    }

    @Override
    public List<SecGroup> getGroupsByUser(SecUser user) {

        List<SecGroup> listGroup = new ArrayList<SecGroup>();

        listGroup = getSecGroupDAO().getGroupsByUser(user);

        return listGroup;
    }

    /*
      * Gets the rights for a specified user.<br> (non-Javadoc)
      *
      * @see
      * id.ac.idu.backend.service.UserService#getRightsByUser(id.ac.idu
      * .backend .model.SecUser)
      */
    @Override
    public Collection<SecRight> getRightsByUser(SecUser user) {
        return getSecRightDAO().getRightsByUser(user);
    }

    @Override
    public List<SecUser> getUsersLikeLastname(String value) {
        return getUserDAO().getUsersLikeLastname(value);
    }

    @Override
    public List<SecUser> getUsersLikeLoginname(String value) {
        return getUserDAO().getUsersLikeLoginname(value);
    }

    @Override
    public List<SecUser> getUsersLikeEmail(String value) {
        return getUserDAO().getUsersLikeEmail(value);
    }

    @Override
    public List<Language> getAllLanguages() {
        return getLanguageDAO().getAllLanguages();
    }

    @Override
    public Language getLanguageByLocale(String lan_locale) {
        return getLanguageDAO().getLanguageByLocale(lan_locale);
    }

    public SecRightDAO getSecRightDAO() {
        return secRightDAO;
    }

    public void setSecRightDAO(SecRightDAO secRightDAO) {
        this.secRightDAO = secRightDAO;
    }

    @Override
    public int getCountAllSecUser() {
        return getUserDAO().getCountAllSecUser();
    }
}
