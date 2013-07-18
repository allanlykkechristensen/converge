/*
 * Copyright (C) 2010 - 2012 Interactive Media Management
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later 
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT 
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more 
 * details.
 *
 * You should have received a copy of the GNU General Public License along with 
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package dk.i2m.converge.ejb.services;

import dk.i2m.converge.ejb.entities.security.UserAccount;
import dk.i2m.converge.utils.LdapUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.CommunicationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import org.apache.commons.lang3.StringUtils;

/**
 * Stateless session bean providing a service for managing users.
 *
 * @author Allan Lykke Christensen
 */
@Stateless()
public class DirectoryServiceBean implements DirectoryServiceLocal {

    private static final Logger LOG = Logger.getLogger(DirectoryServiceBean.class.getName());

    @EJB
    private DaoServiceLocal daoService;

    @EJB
    private ConfigurationServiceLocal cfgService;

    private SearchControls sc = new SearchControls();

    /** DN of the group containing normal users. */
    private String groupOfUsers = "";

    /** DN of the group containing administrative users. */
    private String groupOfAdministrators = "";

    /** Field mappings used for retrieving records from the directory. */
    private Map<String, String> fieldMapping = new HashMap<String, String>();

    /**
     * Event handler executed after the bean has been constructed.
     */
    @PostConstruct
    private void startup() {
        setupUserMapping();
    }

    /** {@inheritDoc } */
    @Override
    public boolean exists(String id) {
        boolean exists = false;

        // Scope of search (search in the subtree of the base dn)
        this.sc.setSearchScope(SearchControls.SUBTREE_SCOPE);

        // Field containing the username
        String uid = getFieldMapping(LdapFieldMapping.USER_MAPPING_USERNAME);

        try {
            // Get directory connection
            DirContext dirCtx = getDirectoryConnection();

            // Contruct the search filter
            String filter = "(" + uid + "=" + id + ")";

            // Conduct search
            NamingEnumeration results;
            results = dirCtx.search(this.groupOfUsers, filter, this.sc);

            // If the search has a result, the user exists
            exists = results.hasMore();

            // Close the connection
            closeDirectoryConnection(dirCtx);
        } catch (NamingException e) {
            LOG.log(Level.WARNING, "", e);
        }

        return exists;
    }

    /** {@inheritDoc} */
    @Override
    public void syncWithDirectory(UserAccount userAccount) throws UserNotFoundException, DirectoryException {
        if (userAccount == null) {
            throw new UserNotFoundException("null user account cannot be synchronised");
        }

        LOG.log(Level.INFO, "Synchronising [{0}/{1}/{2}] with directory",
                new Object[]{userAccount.getUsername(), userAccount.getDistinguishedName(), userAccount.getId()});

        boolean found = false;
        this.sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
        String id = userAccount.getUsername();
        String uid = getFieldMapping(LdapFieldMapping.USER_MAPPING_USERNAME);

        DirContext dirCtx = null;
        try {
            String base = cfgService.getString(ConfigurationKey.LDAP_BASE);
            dirCtx = getDirectoryConnection();
            NamingEnumeration results = dirCtx.search(base, "({0}={1})",
                    new Object[]{uid, id}, this.sc);

            if (results.hasMoreElements() && !found) {
                SearchResult sr = (SearchResult) results.next();

                updateUser(userAccount, sr.getAttributes());
                userAccount.setDistinguishedName(sr.getNameInNamespace());
                found = true;
            }

            closeDirectoryConnection(dirCtx);
        } catch (CommunicationException e) {
            closeDirectoryConnection(dirCtx);
            throw new DirectoryException("Could not connect to directory", e);
        } catch (NamingException e) {
            closeDirectoryConnection(dirCtx);
            throw new DirectoryException("Could not connect to directory", e);
        }

        if (!found) {
            throw new UserNotFoundException("User [" + id
                    + "] was not found in directory");
        }
    }

    /** {@inheritDoc } */
    @Override
    public UserAccount findById(String username) throws UserNotFoundException, DirectoryException {

        UserAccount ua;

        // Query database for user
        Map<String, Object> parameters = QueryBuilder.with(UserAccount.PARAM_USERNAME, username).parameters();
        List<UserAccount> matches = daoService.findWithNamedQuery(UserAccount.FIND_BY_USERNAME, parameters);

        if (matches.size() == 1) {
            // User found in database, disregard directory
            return matches.iterator().next();
        } else {
            // User was not in database, look-up user in directory
            LOG.log(Level.FINE, "UserAccount [{0}] not found in database", username);

            // Set-up account defaults
            ua = new UserAccount();
            ua.setUsername(username);
            ua.setTimeZoneAsString(cfgService.getString(ConfigurationKey.TIME_ZONE));

            try {
                syncWithDirectory(ua);
                LOG.log(Level.FINE, "Creating UserAccount [{0}] in database", username);
                return daoService.create(ua);
            } catch (DirectoryException ex) {
                throw ex;
            } catch (UserNotFoundException ex) {
                throw ex;
            }
        }
    }

    /**
     * Sets the properties of a {@link UserAccount} based on a
     * {@link SearchResult} from the directory. The process will validate each
     * {@link Attribute} to ensure no
     * <code>null</code> values or exceptions
     * occur.
     *
     * @param user {@link UserAccount} to update
     * @param sr   {@link SearchResult} containing the user information
     */
    private void updateUser(UserAccount user, final Attributes attrs) {

        String fieldUid = getFieldMapping(LdapFieldMapping.USER_MAPPING_USERNAME);
        String fieldMail = getFieldMapping(LdapFieldMapping.USER_MAPPING_EMAIL);
        String fieldCommonName = getFieldMapping(LdapFieldMapping.USER_MAPPING_COMMON_NAME);
        String fieldGivenName = getFieldMapping(LdapFieldMapping.USER_MAPPING_FIRST_NAME);
        String fieldSurname = getFieldMapping(LdapFieldMapping.USER_MAPPING_LAST_NAME);
        String fieldJobTitle = getFieldMapping(LdapFieldMapping.USER_MAPPING_JOB_TITLE);
        String fieldMobile = getFieldMapping(LdapFieldMapping.USER_MAPPING_MOBILE);
        String fieldOrganisation = getFieldMapping(LdapFieldMapping.USER_MAPPING_ORGANISATION);
        String fieldPhone = getFieldMapping(LdapFieldMapping.USER_MAPPING_PHONE);
        String fieldLanguage = getFieldMapping(LdapFieldMapping.USER_MAPPING_LANGUAGE);
        String fieldPhoto = getFieldMapping(LdapFieldMapping.USER_MAPPING_JPEG_PHOTO);

        user.setUsername(LdapUtils.validateAttribute(attrs.get(fieldUid)));
        user.setEmail(LdapUtils.validateAttribute(attrs.get(fieldMail)));
        user.setFullName(LdapUtils.validateAttribute(attrs.get(fieldCommonName)));
        user.setGivenName(LdapUtils.validateAttribute(attrs.get(fieldGivenName)));
        user.setJobTitle(LdapUtils.validateAttribute(attrs.get(fieldJobTitle)));
        user.setMobile(LdapUtils.validateAttribute(attrs.get(fieldMobile)));
        user.setOrganisation(LdapUtils.validateAttribute(attrs.get(fieldOrganisation)));
        user.setPhone(LdapUtils.validateAttribute(attrs.get(fieldPhone)));

        // If the user does not have a preferred language, get the one specified
        // in the LDAP directory
        if (StringUtils.isBlank(user.getPreferredLanguage())) {
            user.setPreferredLanguage(LdapUtils.validateAttribute(attrs.get(
                    fieldLanguage)));
        }

        // If no preferred language was specified in the profile nor in the LDAP
        // use the default language specified for the application
        if (StringUtils.isBlank(user.getPreferredLanguage())) {
            String language = cfgService.getString(ConfigurationKey.LANGUAGE);
            user.setPreferredLanguage(language);
        }
        user.setSurname(LdapUtils.validateAttribute(attrs.get(fieldSurname)));

//        Attribute attr = attrs.get(fieldPhoto);
//        if (attr != null) {
//            try {
//                user.setPhoto((byte[]) attr.get());
//            } catch (ClassCastException e) {
//                log.log(Level.WARNING, "LDAP connection is not configured to allow binary values for the field [" + fieldPhoto + "]", e);
//            } catch (NoSuchElementException e) {
//                log.log(Level.FINER, "Attribute did not exist", e);
//            } catch (NamingException e) {
//                log.log(Level.FINER, "Attribute value could not be obtained", e);
//            }
//        }
    }

    /**
     * Obtains the connection to the LDAP directory used for storing users and
     * user groups.
     *
     * @return Established connection to the used LDAP directory
     * @throws NamingException
     *          If the connection could not be established
     */
    private DirContext getDirectoryConnection() throws NamingException {
        Properties p = new Properties();
        p.put(Context.INITIAL_CONTEXT_FACTORY,
                "com.sun.jndi.ldap.LdapCtxFactory");
        p.put("com.sun.jndi.ldap.connect.pool",
                "true");
        p.put("com.sun.jndi.ldap.read.timeout",
                cfgService.getString(ConfigurationKey.LDAP_READ_TIMEOUT));
        p.put("com.sun.jndi.ldap.connect.timeout",
                cfgService.getString(ConfigurationKey.LDAP_CONNECT_TIMEOUT));
        p.put(Context.PROVIDER_URL,
                cfgService.getString(ConfigurationKey.LDAP_PROVIDER_URL));
        p.put(Context.SECURITY_AUTHENTICATION,
                cfgService.getString(ConfigurationKey.LDAP_SECURITY_AUTHENTICATION));
        p.put(Context.SECURITY_PRINCIPAL,
                cfgService.getString(ConfigurationKey.LDAP_SECURITY_PRINCIPAL));
        p.put(Context.SECURITY_CREDENTIALS,
                cfgService.getString(ConfigurationKey.LDAP_SECURITY_CREDENTIALS));

        if (LOG.isLoggable(Level.FINE)) {
            logLdapSettings(p);
        }

        LOG.log(Level.FINE, "Opening directory connection");
        DirContext dirContext = new InitialDirContext(p);

        return dirContext;
    }

    /**
     * Closes a connection to a directory unless it is already closed.
     * 
     * @param dirContext 
     *          Context of the directory connection to close
     */
    private void closeDirectoryConnection(DirContext dirContext) {
        LOG.log(Level.FINE, "Closing directory connection");
        if (dirContext == null) {
            LOG.log(Level.WARNING, "Could not close DirContext as it is null");
            return;
        }

        try {
            dirContext.close();
        } catch (NamingException ex) {
            LOG.log(Level.SEVERE, "Could not close DirContext. ", ex);
        }
    }

    /**
     * Outputs the LDAP settings to the log. Used for debugging purposes.
     * 
     * @param p 
     *          Properties containing the LDAP settings
     */
    private void logLdapSettings(Properties p) {
        LOG.log(Level.CONFIG, "INITIAL_CONTEXT_FACTORY: {0}",
                p.getProperty(Context.INITIAL_CONTEXT_FACTORY));
        LOG.log(Level.CONFIG, "PROVIDER_URL: {0}",
                p.getProperty(Context.PROVIDER_URL));
        LOG.log(Level.CONFIG, "SECURITY_AUTHENTICATION: {0}",
                p.getProperty(Context.SECURITY_AUTHENTICATION));
        LOG.log(Level.CONFIG, "SECURITY_PRINCIPAL: {0}",
                p.getProperty(Context.SECURITY_PRINCIPAL));
        LOG.log(Level.CONFIG, "SECURITY_CREDENTIALS: {0}",
                p.getProperty(Context.SECURITY_CREDENTIALS));
        LOG.log(Level.CONFIG, "LDAP_BASE: {0}",
                cfgService.getString(ConfigurationKey.LDAP_BASE));
        LOG.log(Level.CONFIG, "LDAP_CONNECTION_TIMEOUT: {0}",
                cfgService.getString(ConfigurationKey.LDAP_CONNECT_TIMEOUT));
        LOG.log(Level.CONFIG, "LDAP_READ_TIMEOUT: {0}",
                cfgService.getString(ConfigurationKey.LDAP_READ_TIMEOUT));
    }

    /**
     * Setup field mapping for the directory service.
     */
    private void setupUserMapping() {
        groupOfUsers = cfgService.getString(ConfigurationKey.LDAP_GROUP_USERS);
        groupOfAdministrators = cfgService.getString(ConfigurationKey.LDAP_GROUP_ADMINISTRATORS);

        addMapping(LdapFieldMapping.USER_MAPPING_USERNAME, cfgService.getString(ConfigurationKey.LDAP_USER_MAPPING_USERNAME));
        addMapping(LdapFieldMapping.USER_MAPPING_EMAIL, cfgService.getString(ConfigurationKey.LDAP_USER_MAPPING_EMAIL));
        addMapping(LdapFieldMapping.USER_MAPPING_COMMON_NAME, cfgService.getString(ConfigurationKey.LDAP_USER_MAPPING_COMMON_NAME));
        addMapping(LdapFieldMapping.USER_MAPPING_FIRST_NAME, cfgService.getString(ConfigurationKey.LDAP_USER_MAPPING_FIRST_NAME));
        addMapping(LdapFieldMapping.USER_MAPPING_LAST_NAME, cfgService.getString(ConfigurationKey.LDAP_USER_MAPPING_LAST_NAME));
        addMapping(LdapFieldMapping.USER_MAPPING_JOB_TITLE, cfgService.getString(ConfigurationKey.LDAP_USER_MAPPING_JOB_TITLE));
        addMapping(LdapFieldMapping.USER_MAPPING_MOBILE, cfgService.getString(ConfigurationKey.LDAP_USER_MAPPING_MOBILE));
        addMapping(LdapFieldMapping.USER_MAPPING_ORGANISATION, cfgService.getString(ConfigurationKey.LDAP_USER_MAPPING_ORGANISATION));
        addMapping(LdapFieldMapping.USER_MAPPING_PHONE, cfgService.getString(ConfigurationKey.LDAP_USER_MAPPING_PHONE));
        addMapping(LdapFieldMapping.USER_MAPPING_LANGUAGE, cfgService.getString(ConfigurationKey.LDAP_USER_MAPPING_LANGUAGE));
        addMapping(LdapFieldMapping.USER_MAPPING_JPEG_PHOTO, cfgService.getString(ConfigurationKey.LDAP_USER_MAPPING_JPEG_PHOTO));
        addMapping(LdapFieldMapping.GROUP_MAPPING_NAME, cfgService.getString(ConfigurationKey.LDAP_GROUP_MAPPING_NAME));
        addMapping(LdapFieldMapping.GROUP_MAPPING_MEMBEROF, cfgService.getString(ConfigurationKey.LDAP_GROUP_MAPPING_MEMBEROF));
    }

    /**
     * Adds a field mapping to the mapping table.
     *
     * @param fieldIdentifier 
     *          Field identifier
     * @param fieldName       
     *          Real name of the field
     */
    private void addMapping(LdapFieldMapping fieldIdentifier, String fieldName) {
        this.fieldMapping.put(fieldIdentifier.name(), fieldName);
    }

    /**
     * Gets a field mapping from the mapping table.
     *
     * @param fieldIdentifier 
     *          Field identifier
     * @return Real name of the field given
     */
    private String getFieldMapping(LdapFieldMapping fieldMapping) {
        if (this.fieldMapping.containsKey(fieldMapping.name())) {
            return this.fieldMapping.get(fieldMapping.name());
        } else {
            return "";
        }
    }
}
