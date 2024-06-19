package com.example.FinalWorkDevelopmentOnSpringFramework.statistics.repository;

import com.example.FinalWorkDevelopmentOnSpringFramework.statistics.entety.Statistics;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatisticsRepository extends MongoRepository<Statistics, String> {

}
