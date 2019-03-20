package com.atguigu.scw.manager.dao;

import com.atguigu.scw.manager.bean.TUserToken;
import com.atguigu.scw.manager.bean.TUserTokenExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TUserTokenMapper {
    long countByExample(TUserTokenExample example);

    int deleteByExample(TUserTokenExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TUserToken record);

    int insertSelective(TUserToken record);

    List<TUserToken> selectByExample(TUserTokenExample example);

    TUserToken selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TUserToken record, @Param("example") TUserTokenExample example);

    int updateByExample(@Param("record") TUserToken record, @Param("example") TUserTokenExample example);

    int updateByPrimaryKeySelective(TUserToken record);

    int updateByPrimaryKey(TUserToken record);
}