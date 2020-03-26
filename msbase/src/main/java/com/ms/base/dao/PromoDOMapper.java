package com.ms.base.dao;

import com.ms.base.dataobject.PromoDO;

public interface PromoDOMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table promo_record
     *
     * @mbg.generated Wed Mar 25 23:04:56 CST 2020
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table promo_record
     *
     * @mbg.generated Wed Mar 25 23:04:56 CST 2020
     */
    int insert(PromoDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table promo_record
     *
     * @mbg.generated Wed Mar 25 23:04:56 CST 2020
     */
    int insertSelective(PromoDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table promo_record
     *
     * @mbg.generated Wed Mar 25 23:04:56 CST 2020
     */
    PromoDO selectByPrimaryKey(Integer id);

    PromoDO selectByItemId(Integer itemId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table promo_record
     *
     * @mbg.generated Wed Mar 25 23:04:56 CST 2020
     */
    int updateByPrimaryKeySelective(PromoDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table promo_record
     *
     * @mbg.generated Wed Mar 25 23:04:56 CST 2020
     */
    int updateByPrimaryKey(PromoDO record);
}