package com.ksoot.problem.demo.repository;

import com.ksoot.problem.demo.model.State;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface StateRepository extends MongoRepository<State, String> {

  @Query("{'$or':[ { 'id' : ?0 }, { 'code' : ?0 } ] }")
  Optional<State> findByIdOrCode(final String idOrCode);
}
