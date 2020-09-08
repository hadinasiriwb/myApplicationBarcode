package com.appsng.greendaoapp.db;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.appsng.greendaoapp.db.product;

import com.appsng.greendaoapp.db.productDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig productDaoConfig;

    private final productDao productDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        productDaoConfig = daoConfigMap.get(productDao.class).clone();
        productDaoConfig.initIdentityScope(type);

        productDao = new productDao(productDaoConfig, this);

        registerDao(product.class, productDao);
    }
    
    public void clear() {
        productDaoConfig.clearIdentityScope();
    }

    public productDao getProductDao() {
        return productDao;
    }

}
