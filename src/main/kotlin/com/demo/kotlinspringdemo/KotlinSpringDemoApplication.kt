package com.demo.kotlinspringdemo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Service
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.web.bind.annotation.*
import org.springframework.jdbc.core.query
import java.util.*

// Initializes Spring component scanning for annotated classes below.
@SpringBootApplication
class KotlinSpringDemoApplication

fun main(args: Array<String>) {
    runApplication<KotlinSpringDemoApplication>(*args)
}
// Controller class for mapping http requests
@RestController
class MessageController(val service: MessageService) {
    @GetMapping("/")
    fun index(): List<Message> = service.findMessages()
    @GetMapping("/{id}")
    fun index(@PathVariable id: String): List<Message> =
        service.findMessageById(id)

    @PostMapping("/")
    fun post(@RequestBody message: Message) {
        service.save(message)
    }
}
//@Table annotation declares to Spring Data that class is mapping for db table.
@Table("MESSAGES")
data class Message(@Id var id:String?, val text:String)

@Service
class MessageService(val db: MessageRepository) {
    fun findMessages(): List<Message> = db.findAll().toList()

    fun findMessageById(id: String): List<Message> = db.findById(id).toList()

    fun save(message: Message) {
        db.save(message)
    }

    fun <T : Any> Optional<out T>.toList(): List<T> =
        if (isPresent) listOf(get()) else emptyList()
}
// Creates interface facilitating Spring Data's CrudRepo interface.
interface MessageRepository : CrudRepository<Message, String>