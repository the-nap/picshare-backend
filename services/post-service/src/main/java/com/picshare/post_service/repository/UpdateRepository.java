package com.picshare.post_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.picshare.post_service.entity.UpdateEntity;
import com.picshare.post_service.entity.UpdateEntity.UpdateId;
import com.picshare.post_service.entity.UpdateEntity.UpdateStatus;

@Repository
public interface UpdateRepository extends JpaRepository<UpdateEntity, UpdateId>{

  List<UpdateEntity> findTop100ByStatusOrderByIdUserId(UpdateStatus status);

}
