package com.gangulwar

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.mindrot.jbcrypt.BCrypt
import java.io.File

fun Application.configureRouting() {
    val username = "admin"
    val hashedPassword = BCrypt.hashpw("password123", BCrypt.gensalt())

    install(Authentication) {
        basic("auth-basic") {
            realm = "Access to simple UI"
            validate { credentials ->
                if (credentials.name == username && BCrypt.checkpw(credentials.password, hashedPassword)) {
                    UserIdPrincipal(credentials.name)
                } else null
            }
        }
    }

    routing {
        staticFiles("/static", File("src/main/resources/static"))

        get("/") {
            call.respondRedirect("/static/index.html")
        }

        authenticate("auth-basic") {
            get("/secure") {
                call.respondText("You are logged in!", ContentType.Text.Plain)
            }
        }
    }
}
