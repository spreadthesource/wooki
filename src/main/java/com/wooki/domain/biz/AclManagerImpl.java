package com.wooki.domain.biz;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.AccessControlEntry;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.wooki.domain.model.WookiEntity;

@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
public class AclManagerImpl implements AclManager
{

    private static Logger logger = LoggerFactory.getLogger(AclManager.class);

    @Autowired
    @Qualifier("aclServices")
    private MutableAclService mutableAclService;

    public void addPermission(WookiEntity secureObject, Permission permission, Class<?> clazz)
    {
        addPermission(secureObject, new PrincipalSid(getUsername()), permission, clazz);
    }

    public void addPermission(WookiEntity securedObject, Sid recipient, Permission permission,
            Class<?> clazz)
    {

        MutableAcl acl;
        ObjectIdentity oid = new ObjectIdentityImpl(clazz.getCanonicalName(), securedObject.getId());

        try
        {
            acl = (MutableAcl) mutableAclService.readAclById(oid);
        }
        catch (NotFoundException nfe)
        {
            acl = mutableAclService.createAcl(oid);
        }

        acl.insertAce(
                acl.getEntries() != null ? acl.getEntries().size() : 0,
                permission,
                recipient,
                true);
        mutableAclService.updateAcl(acl);

        if (logger.isDebugEnabled())
        {
            logger.debug("Added permission " + permission + " for Sid " + recipient
                    + " securedObject " + securedObject);
        }
    }

    public void deletePermission(WookiEntity securedObject, Sid recipient, Permission permission,
            Class<?> clazz)
    {
        ObjectIdentity oid = new ObjectIdentityImpl(clazz.getCanonicalName(), securedObject.getId());
        MutableAcl acl = (MutableAcl) mutableAclService.readAclById(oid);

        // Remove all permissions associated with this particular recipient
        // (string equality used to keep things simple)
        List<AccessControlEntry> entries = acl.getEntries();

        if (entries != null)
        {
            for (int i = 0; i < entries.size(); i++)
            {
                AccessControlEntry entry = entries.get(0);
                if (entry.getSid().equals(recipient) && entry.getPermission().equals(permission))
                {
                    acl.deleteAce(i);
                }
            }
        }

        mutableAclService.updateAcl(acl);

        if (logger.isDebugEnabled())
        {
            logger.debug("Deleted securedObject " + securedObject
                    + " ACL permissions for recipient " + recipient);
        }
    }

    protected String getUsername()
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth.getPrincipal() instanceof UserDetails)
        {
            return ((UserDetails) auth.getPrincipal()).getUsername();
        }
        else
        {
            return auth.getPrincipal().toString();
        }
    }

}
