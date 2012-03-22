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
package id.ac.idu.backend.model;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.HashSet;
import java.util.Set;

/**
 * EN: Model class for <b>SecUser</b>.<br>
 * DE: Model Klasse fuer <b>User</b>.<br>
 *
 * @author bbruhns
 * @author sgerth
 */
public class SecUser implements java.io.Serializable, Entity {

    private static final long serialVersionUID = -8443234918260997954L;

    private long id = Long.MIN_VALUE;
    private int version;
    private String usrLoginname;
    private String usrPassword;
    private String usrLastname;
    private String usrFirstname;
    private String usrEmail;
    private String usrLocale;
    private int usrEnabled = 1;
    private int usrAccountnonexpired = 1;
    private int usrCredentialsnonexpired = 1;
    private int usrAccountnonlocked = 1;
    private String usrToken;
    private Set<SecUserrole> secUserroles = new HashSet<SecUserrole>(0);

    public boolean isNew() {
        return (getId() == Long.MIN_VALUE);
    }

    public SecUser() {
    }

    public SecUser(long id, String usrLoginname, String usrPassword, int usrEnabled, int usrAccountnonexpired,
                   int usrCredentialsnonexpired, int usrAccountnonlocked) {
        this.setId(id);
        this.usrLoginname = usrLoginname;
        this.usrPassword = usrPassword;
        this.usrEnabled = usrEnabled;
        this.usrAccountnonexpired = usrAccountnonexpired;
        this.usrCredentialsnonexpired = usrCredentialsnonexpired;
        this.usrAccountnonlocked = usrAccountnonlocked;
    }

    public SecUser(long id, String usrLoginname, String usrPassword, String usrLastname, String usrFirstname,
                   String usrEmail, String usrLocale, int usrEnabled, int usrAccountnonexpired,
                   int usrCredentialsnonexpired, int usrAccountnonlocked, Set<SecUserrole> secUserroles) {
        this.setId(id);
        this.usrLoginname = usrLoginname;
        this.usrPassword = usrPassword;
        this.usrLastname = usrLastname;
        this.usrFirstname = usrFirstname;
        this.usrEmail = usrEmail;
        this.usrLocale = usrLocale;
        this.usrEnabled = usrEnabled;
        this.usrAccountnonexpired = usrAccountnonexpired;
        this.usrCredentialsnonexpired = usrCredentialsnonexpired;
        this.usrAccountnonlocked = usrAccountnonlocked;
        this.secUserroles = secUserroles;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    /**
     * EN: Hibernate version field. Do not touch this!.<br>
     * DE: Hibernate Versions Info. Bitte nicht benutzen!<br>
     */
    public int getVersion() {
        return this.version;
    }

    /**
     * EN: Hibernate version field. Do not touch this!.<br>
     * DE: Hibernate Versions Info. Bitte nicht benutzen!<br>
     */
    public void setVersion(int version) {
        this.version = version;
    }

    public String getUsrLoginname() {
        return this.usrLoginname;
    }

    public void setUsrLoginname(String usrLoginname) {
        this.usrLoginname = usrLoginname;
    }

    public String getUsrPassword() {
        return this.usrPassword;
    }

    public void setUsrPassword(String usrPassword) {
        this.usrPassword = usrPassword;
    }

    public String getUsrLastname() {
        return this.usrLastname;
    }

    public void setUsrLastname(String usrLastname) {
        this.usrLastname = usrLastname;
    }

    public String getUsrFirstname() {
        return this.usrFirstname;
    }

    public void setUsrFirstname(String usrFirstname) {
        this.usrFirstname = usrFirstname;
    }

    public String getUsrEmail() {
        return this.usrEmail;
    }

    public void setUsrEmail(String usrEmail) {
        this.usrEmail = usrEmail;
    }

    public String getUsrLocale() {
        return usrLocale;
    }

    public void setUsrLocale(String usrLocale) {
        this.usrLocale = usrLocale;
    }

    public int isUsrEnabled() {
        return this.usrEnabled;
    }

    public void setUsrEnabled(int usrEnabled) {
        this.usrEnabled = usrEnabled;
    }

    public Boolean checkUsrEnabled() {
        return (this.isUsrEnabled() == 1);
    }

    public void putUsrEnabled(Boolean usrEnabled) {
        this.setUsrEnabled(((usrEnabled) ? 1 : 0));
    }

    public int isUsrAccountnonexpired() {
        return this.usrAccountnonexpired;
    }

    public void setUsrAccountnonexpired(int usrAccountnonexpired) {
        this.usrAccountnonexpired = usrAccountnonexpired;
    }

    public Boolean checkUsrAccountnonexpired() {
        return (this.isUsrAccountnonexpired() == 1);
    }

    public void putUsrAccountnonexpired(Boolean usrAccountnonexpired) {
        this.setUsrAccountnonexpired(((usrAccountnonexpired) ? 1 : 0));
    }

    public int isUsrCredentialsnonexpired() {
        return this.usrCredentialsnonexpired;
    }

    public void setUsrCredentialsnonexpired(int usrCredentialsnonexpired) {
        this.usrCredentialsnonexpired = usrCredentialsnonexpired;
    }

    public Boolean checkUsrCredentialsnonexpired() {
        return (this.isUsrCredentialsnonexpired() == 1);
    }

    public void putUsrCredentialsnonexpired(Boolean usrCredentialsnonexpired) {
        this.setUsrCredentialsnonexpired(((usrCredentialsnonexpired) ? 1 : 0));
    }

    public int isUsrAccountnonlocked() {
        return this.usrAccountnonlocked;
    }

    public void setUsrAccountnonlocked(int usrAccountnonlocked) {
        this.usrAccountnonlocked = usrAccountnonlocked;
    }

    public Boolean checkUsrAccountnonlocked() {
        return (this.isUsrAccountnonlocked() == 1);
    }

    public void putUsrAccountnonlocked(Boolean usrAccountnonlocked) {
        this.setUsrAccountnonlocked(((usrAccountnonlocked) ? 1 : 0));
    }

    public String getUsrToken() {
        return this.usrToken;
    }

    public void setUsrToken(String usrToken) {
        this.usrToken = usrToken;
    }

    public Set<SecUserrole> getSecUserroles() {
        return this.secUserroles;
    }

    public void setSecUserroles(Set<SecUserrole> secUserroles) {
        this.secUserroles = secUserroles;
    }

    @Override
    public int hashCode() {
        return Long.valueOf(getId()).hashCode();
    }

    public boolean equals(SecUser secUser) {
        return getId() == secUser.getId();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof SecUser) {
            SecUser secUser = (SecUser) obj;
            return equals(secUser);
        }

        return false;
    }

    public String toString() {
        return new ToStringBuilder(this).append("id", getId()).toString();
    }

}
