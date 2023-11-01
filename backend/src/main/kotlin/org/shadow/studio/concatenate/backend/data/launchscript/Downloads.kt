package org.shadow.studio.concatenate.backend.data.launchscript

data class Downloads(
    val client: Client,
    val client_mappings: ClientMappings,
    val server: Server,
    val server_mappings: ServerMappings
)