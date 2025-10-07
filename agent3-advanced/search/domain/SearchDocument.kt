package com.spiritscribe.search.domain

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType
import java.time.Instant

@Document(indexName = "spiritscribe")
data class SearchDocument(
    @Id
    val id: String,
    
    @Field(type = FieldType.Text, analyzer = "korean")
    val title: String,
    
    @Field(type = FieldType.Text, analyzer = "korean")
    val content: String,
    
    @Field(type = FieldType.Keyword)
    val type: String, // "user", "checkin", "whiskey"
    
    @Field(type = FieldType.Keyword)
    val userId: String? = null,
    
    @Field(type = FieldType.Keyword)
    val whiskeyId: String? = null,
    
    @Field(type = FieldType.Date)
    val createdAt: Instant,
    
    @Field(type = FieldType.Integer)
    val score: Int = 0
)
