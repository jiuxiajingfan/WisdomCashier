package com.li.wisdomcashier.base.bean;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.shiro.authz.ModularRealmAuthorizer;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * @ClassName CustomerAuthrizer
 * @Description TODO
 * @Author Nine
 * @Date 2023/3/30 21:36
 * @Version 1.0
 */
public class CustomerAuthrizer extends ModularRealmAuthorizer {
    @Override
    public boolean isPermitted(PrincipalCollection principals, String permission) {
        assertRealmsConfigured();
        // 所有Realm
        Collection<Realm> realms = getRealms();
        HashMap<String, Realm> realmHashMap = new HashMap<>(realms.size());
        for (Realm realm : realms) {
            realmHashMap.put(realm.getName(), realm);
        }

        Set<String> realmNames = principals.getRealmNames();
        if (realmNames != null) {
            String realmName = null;
            Iterator it = realmNames.iterator();
            while (it.hasNext()) {
                realmName = ConvertUtils.convert(it.next());
                if (realmName.contains("AdminRealm")) {
                    return ((AdminRealm) realmHashMap.get("AdminRealm")).isPermitted(principals, permission);
                } else if (realmName.contains("UserRealm")) {
                    return ((UserRealm) realmHashMap.get("UserRealm")).isPermitted(principals, permission);
                }
                break;
            }
        }
        return false;
    }

    @Override
    public boolean hasRole(PrincipalCollection principals, String roleIdentifier) {
        assertRealmsConfigured();
        // 所有Realm
        Collection<Realm> realms = getRealms();
        HashMap<String, Realm> realmHashMap = new HashMap<>(realms.size());
        for (Realm realm : realms) {
            realmHashMap.put(realm.getName(), realm);
        }

        Set<String> realmNames = principals.getRealmNames();
        if (realmNames != null) {
            String realmName = null;
            Iterator it = realmNames.iterator();
            while (it.hasNext()) {
                realmName = ConvertUtils.convert(it.next());
                if (realmName.contains("AdminRealm")) {
                    return ((AdminRealm) realmHashMap.get("AdminRealm")).isPermitted(principals, roleIdentifier);
                } else if (realmName.contains("UserRealm")) {
                    return ((UserRealm) realmHashMap.get("UserRealm")).isPermitted(principals, roleIdentifier);
                }
                break;
            }
        }
        return false;
    }
}