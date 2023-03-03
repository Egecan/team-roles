package com.ege.teams.model

data class TeamDetailsResponse(
    val id: String,
    val name: String,
    var teamLeadId: String,
    var teamMemberIds: List<String>
)
