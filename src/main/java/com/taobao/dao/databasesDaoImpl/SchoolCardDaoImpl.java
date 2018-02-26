package com.taobao.dao.databasesDaoImpl;

import com.taobao.dao.databasesDao.SupperBaseDAOImp;
import com.taobao.dao.entity.SchoolCard;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Create by zhangpe0312@qq.com on 2018/2/26.
 */
@Service
public class SchoolCardDaoImpl extends SupperBaseDAOImp<SchoolCard> {

    private static Logger logger = Logger.getLogger(SchoolCardDaoImpl.class);

    @Override
    public <T> List<T> findByCriteria(T object, Integer startRow, Integer pageSize) {
        return null;
    }

    @Override
    public <T> Long findByCriteriaCount(T object) {
        return null;
    }

    /**
     * 通过校园卡账号和密码获取校园卡实体
     * @param cardId 校园卡卡号
     * @param password 校园卡密码
     * @return 获取到的校园卡实体
     */
    public SchoolCard findCardByIDAndPassword(String cardId , String password){

        try {
            SchoolCard card = findByProperty("schoolID",cardId);

            if (card.getPassword().equals(password)){
                return card;
            }
            logger.error("校园卡 " + cardId + " 验证密码失败 ");
            return null;
        }catch (Exception e){
            e.printStackTrace();
            logger.error("校园卡 " + cardId + " 获取失败 " + e);
            return null;
        }
    }
}
