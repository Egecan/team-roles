package com.ege.teams.service

import com.ege.teams.common.Constants.GET_TEAMS_ENDPOINT
import com.ege.teams.model.TeamDetailsResponse
import com.ege.teams.model.TeamsResponse
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class TeamService(private val restTemplate: RestTemplate) {

    fun getTeam(id: String): TeamDetailsResponse? {
        return restTemplate.getForObject("$GET_TEAMS_ENDPOINT/$id", TeamDetailsResponse::class.java)
    }

    fun getAllTeams(): Array<TeamsResponse>? {
        return restTemplate.getForObject(GET_TEAMS_ENDPOINT, Array<TeamsResponse>::class.java)
    }
}
