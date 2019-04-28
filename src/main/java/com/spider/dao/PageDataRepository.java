package com.spider.dao;

import com.spider.entity.PageData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PageDataRepository extends JpaRepository<PageData,Integer> {

    Page<PageData> getPageDataByLanguageAndTypeOrderById(String language, String type, Pageable page);

    @Transactional
    @Modifying
    @Query(value = "TRUNCATE TABLE page_data",nativeQuery = true)
    void truncateTable();

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO page_data (SELECT * FROM page_data_tem)",nativeQuery = true)
    void copyTable();
}