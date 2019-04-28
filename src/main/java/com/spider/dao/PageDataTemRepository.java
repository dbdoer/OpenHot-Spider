package com.spider.dao;

import com.spider.entity.PageDataTem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PageDataTemRepository extends JpaRepository<PageDataTem,Integer> {

    @Transactional
    @Modifying
    @Query(value = "TRUNCATE TABLE page_data_tem",nativeQuery = true)
   void truncateTable();


}
