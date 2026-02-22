package ss.fortberg.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule

val objectMapper = ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT)
    .registerModule(KotlinModule.Builder().build())